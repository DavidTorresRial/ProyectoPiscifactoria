package commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import helpers.InputHelper;
import helpers.MenuHelper;

import piscifactorias.Piscifactoria;
import piscifactorias.tipos.PiscifactoriaDeRio;
import piscifactorias.tipos.PiscifactoriaDeMar;

import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import propiedades.PecesProps;
import estadisticas.Estadisticas;

import tanque.Tanque;

import peces.Pez;
import peces.tipos.doble.*;
import peces.tipos.mar.*;
import peces.tipos.rio.*;

/**
 * La clase Simulador gestiona la simulación de una piscifactoría,
 * permitiendo realizar diversas operaciones de administración de peces,
 * tanques y recursos monetarios. Incluye un menú interactivo para la
 * navegación entre las diferentes opciones de gestión de la piscifactoría.
 * 
 * @author David, Fran, Marcos.
 */
public class Simulador {

    /** Días transcurridos en la simulación, comenzando en 1 */
    private int dias = 1;

    /** Lista de piscifactorías en el sistema */
    private List<Piscifactoria> piscifactorias = new ArrayList<>();

    /** Nombre de la entidad o partida en la simulación */
    private String nombreEntidad;

    /** Nombre de la piscifactoría */
    private String nombrePiscifactoria;

    /**
     * Sistema de estadísticas para registrar la cría, venta y ganancias de los
     * peces
     */
    private Estadisticas estadisticas = new Estadisticas(new String[] {
            AlmacenPropiedades.SALMON_ATLANTICO.getNombre(),
            AlmacenPropiedades.TRUCHA_ARCOIRIS.getNombre(),
            AlmacenPropiedades.ARENQUE_ATLANTICO.getNombre(),
            AlmacenPropiedades.BESUGO.getNombre(),
            AlmacenPropiedades.LENGUADO_EUROPEO.getNombre(),
            AlmacenPropiedades.LUBINA_RAYADA.getNombre(),
            AlmacenPropiedades.ROBALO.getNombre(),
            AlmacenPropiedades.CARPA_PLATEADA.getNombre(),
            AlmacenPropiedades.PEJERREY.getNombre(),
            AlmacenPropiedades.PERCA_EUROPEA.getNombre(),
            AlmacenPropiedades.SALMON_CHINOOK.getNombre(),
            AlmacenPropiedades.TILAPIA_NILO.getNombre()
    });

    /** Sistema de monedas para manejar transacciones */
    private SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Almacén central de comida para abastecer las piscifactorías */
    private AlmacenCentral almacenCentral = new AlmacenCentral();

    /** Utilidad para manejar la entrada de datos del usuario */
    private InputHelper inputHelper = new InputHelper();

    /** Utilidad para gestionar los menús de la simulación */
    private MenuHelper menuHelper = new MenuHelper();

    /** Metodo que inicializa todo el sistema */
    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();
        nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");

        piscifactorias.add(new PiscifactoriaDeRio(nombrePiscifactoria, monedas));
        piscifactorias.get(0).setComidaAnimalActual(piscifactorias.get(0).getCapacidadTotal());
        piscifactorias.get(0).setComidaVegetalActual(piscifactorias.get(0).getCapacidadTotal());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        menuHelper.clearOptions();

        menuHelper.addOption(1, "Estado general");
        menuHelper.addOption(2, "Estado piscifactoría");
        menuHelper.addOption(3, "Estado tanque");
        menuHelper.addOption(4, "Informes");
        menuHelper.addOption(5, "Ictiopedia");
        menuHelper.addOption(6, "Pasar día");
        menuHelper.addOption(7, "Comprar Comida");
        menuHelper.addOption(8, "Comprar peces");
        menuHelper.addOption(9, "Vender peces");
        menuHelper.addOption(10, "Limpiar tanque");
        menuHelper.addOption(11, "Vaciar tanque");
        menuHelper.addOption(12, "Mejorar");
        menuHelper.addOption(13, "Pasar varios dias");
        menuHelper.addOption(14, "Salir");

        menuHelper.showMenu();
    }

    /** Método que muestra la lista de piscifactorías actuales en forma de menú. */
    public void menuPisc() {
        menuHelper.clearOptions();

        System.out.println("\nSeleccione una opción:");
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");
        System.out.println();

        for (int i = 0; i < piscifactorias.size(); i++) {
            Piscifactoria piscifactoria = piscifactorias.get(i);
            String optionText = piscifactoria.getNombre() + " [" + piscifactoria.getTotalVivos() + "/"
                    + piscifactoria.getTotalPeces() + "/" + piscifactoria.getCapacidadTotal() + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        menuHelper.addOption(0, "Cancelar");

        menuHelper.showMenu();
    }

    /**
     * Muestra la lista de piscifactorías actuales y permite al usuario seleccionar
     * una.
     * 
     * @return La piscifactoría seleccionada, o null si se canceló la operación.
     */
    public Piscifactoria selectPisc() {
        while (true) {
            menuPisc();
            int selection = inputHelper.readInt("Ingrese su selección: ");

            if (selection == 0) {
                System.out.println("Operación cancelada.");
                return null;
            }

            if (selection < 1 || selection > piscifactorias.size()) {
                System.out.println("Selección no válida. Inténtalo de nuevo.");
                continue;
            }
            return piscifactorias.get(selection - 1);
        }
    }

    /**
     * Permite al usuario seleccionar un tanque de una piscifactoría específica.
     *
     * @return El tanque seleccionado por el usuario.
     */
    public Tanque selectTank() {
        Piscifactoria piscifactoria = selectPisc();

        if (piscifactoria == null) {
            return null;
        }

        while (true) {
            menuHelper.clearOptions();

            System.out.println("\nSeleccione un Tanque:");
            List<Tanque> tanques = piscifactoria.getTanques();

            for (int i = 0; i < tanques.size(); i++) {
                Tanque tanque = tanques.get(i);
                String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName()
                        : "Vacío";
                String optionText = "Tanque " + (i + 1) + " [" + tipoPez + "]";
                menuHelper.addOption(i + 1, optionText);
            }

            menuHelper.addOption(0, "Cancelar");
            menuHelper.showMenu();

            int selection = inputHelper.readInt("Ingrese su selección: ");
            System.out.println();

            if (selection == 0) {
                return null;
            }

            if (selection < 1 || selection > tanques.size()) {
                System.out.println("Selección no válida. Inténtalo de nuevo.");
                continue;
            }

            return tanques.get(selection - 1);
        }
    }

    /** Método que muestra el estado de las piscifactorías. */
    public void showGeneralStatus() {
        System.out.println("\n======================= " + nombreEntidad + " =======================");
        System.out.println("Día actual: " + dias);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.showStatus();
        }
        System.out.println("\n==================================================");

        if (almacenCentral.isConstruido()) {
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("No hay Almacén Central disponible.");
        }
        System.out.println("\n==================================================");
    }

    /** Muestra el estado de una piscifactoría seleccionada por el usuario. */
    public void showSpecificStatus() {
        Piscifactoria piscifactoria = selectPisc();

        if (piscifactoria == null) {
            return;
        }

        piscifactoria.showTankStatus();
    }

    /**
     * Muestra el estado de los peces en un tanque seleccionado de una
     * piscifactoría.
     */
    public void showTankStatus() {
        Tanque tanqueSeleccionado = selectTank();

        if (tanqueSeleccionado == null) {
            System.out.print("Operación cancelada.");
            return;
        }
        if (tanqueSeleccionado.getPeces().isEmpty()) {
            System.out.println("Este tanque no contiene peces actualmente.");
            return;
        }
        System.out.println("\nEstado de los peces en el tanque " + (tanqueSeleccionado.getNumeroTanque() + 1) + ":");

        for (Pez pez : tanqueSeleccionado.getPeces()) {
            pez.showStatus();
        }
    }

    /** Muestra un desglose de las estadísticas por cada tipo de pez */
    public void showStats() {
        System.out.println("\n=============== Estadísticas ===============");
        estadisticas.mostrar();
        System.out.println("===========================================");
    }

    /** Muestra el estado de un pez seleccionado por el usuario. */
    public void showIctio() {
        Pez pezSeleccionado = selectAnyFish(true);

        if (pezSeleccionado == null) {
            return;
        }

        System.out.println("\nInformación del pez seleccionado:");
        System.out.println("Nombre: " + pezSeleccionado.getDatos().getNombre());
        System.out.println("Nombre científico: " + pezSeleccionado.getDatos().getCientifico());
        System.out.println("Tipo: " + pezSeleccionado.getDatos().getTipo());
        System.out.println("Coste: " + pezSeleccionado.getDatos().getCoste() + " unidades");
        System.out.println("Monedas: " + pezSeleccionado.getDatos().getMonedas());
        System.out.println("Huevos: " + pezSeleccionado.getDatos().getHuevos());
        System.out.println("Ciclo: " + pezSeleccionado.getDatos().getCiclo() + " días");
        System.out.println("Madurez: " + pezSeleccionado.getDatos().getMadurez() + " días");
        System.out.println("Óptimo: " + pezSeleccionado.getDatos().getOptimo() + " unidades");

        PecesProps[] propiedades = pezSeleccionado.getDatos().getPropiedades();
        System.out.print("Propiedades: ");
        for (int i = 0; i < propiedades.length; i++) {
            System.out.print(propiedades[i]);
            if (i < propiedades.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        System.out.println("Tipo de cría: " + pezSeleccionado.getDatos().getPiscifactoria());
    }

    /**
     * Método para seleccionar un pez de cualquier tipo (río o mar) mediante un
     * menú.
     *
     * @param permitirHembras un booleano que indica si se permite seleccionar
     *                        hembras (true) o no (false).
     * @return un objeto de tipo Pez que representa el pez seleccionado.
     */
    public Pez selectAnyFish(boolean permitirHembras) {
        menuHelper.clearOptions();

        System.out.println("\nPeces disponibles: ");
        PecesDatos[] pecesEspecificos = {
                AlmacenPropiedades.SALMON_ATLANTICO,
                AlmacenPropiedades.TRUCHA_ARCOIRIS,

                AlmacenPropiedades.ARENQUE_ATLANTICO,
                AlmacenPropiedades.BESUGO,
                AlmacenPropiedades.LENGUADO_EUROPEO,
                AlmacenPropiedades.LUBINA_RAYADA,
                AlmacenPropiedades.ROBALO,

                AlmacenPropiedades.CARPA_PLATEADA,
                AlmacenPropiedades.PEJERREY,
                AlmacenPropiedades.PERCA_EUROPEA,
                AlmacenPropiedades.SALMON_CHINOOK,
                AlmacenPropiedades.TILAPIA_NILO
        };

        for (int i = 0; i < pecesEspecificos.length; i++) {
            PecesDatos pez = pecesEspecificos[i];
            menuHelper.addOption(i + 1, pez.getNombre());
        }

        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();

        int seleccion = inputHelper.readInt("Seleccione un pez: ");
        Pez pezSeleccionado = null;

        switch (seleccion) {
            case 1:
                pezSeleccionado = new SalmonAtlantico(!permitirHembras);
                break;
            case 2:
                pezSeleccionado = new ArenqueDelAtlantico(!permitirHembras);
                break;
            case 3:
                pezSeleccionado = new Besugo(!permitirHembras);
                break;
            case 4:
                pezSeleccionado = new LenguadoEuropeo(!permitirHembras);
                break;
            case 5:
                pezSeleccionado = new LubinaRayada(!permitirHembras);
                break;
            case 6:
                pezSeleccionado = new Robalo(!permitirHembras);
                break;
            case 7:
                pezSeleccionado = new TruchaArcoiris(!permitirHembras);
                break;
            case 8:
                pezSeleccionado = new CarpaPlateada(!permitirHembras);
                break;
            case 9:
                pezSeleccionado = new Pejerrey(!permitirHembras);
                break;
            case 10:
                pezSeleccionado = new PercaEuropea(!permitirHembras);
                break;
            case 11:
                pezSeleccionado = new SalmonChinook(!permitirHembras);
                break;
            case 12:
                pezSeleccionado = new TilapiaDelNilo(!permitirHembras);
                break;
            case 0:
                System.out.println("Operación cancelada.");
                return null;
            default:
                System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
                return null;
        }

        return pezSeleccionado;
    }

    /** Simula un día en todas las piscifactorías. */
    public void nextDay() {
        //int totalPecesVendidos = 0, totalMonedasGanadas = 0;

        dias++;

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.nextDay();
        }
        //showGeneralStatus();
        //System.out.println(totalPecesVendidos + " peces vendidos por un total de " + totalMonedasGanadas + " monedas.");
    }

    /**
     * Avanza la simulación un número específico de días.
     * 
     * @param numeroDeDias número de días a avanzar en la simulación.
     */
    public void avanzarDias(int numeroDeDias) {
        for (int i = 0; i < numeroDeDias; i++) {
            nextDay();
        }
    }

    /** Añade comida al almacén central o a una piscifactoría seleccionada. */
    public void addFood() {
        InputHelper inputHelper = new InputHelper();
        MenuHelper menuHelper = new MenuHelper();

        if (almacenCentral.isConstruido()) {
            menuHelper.clearOptions();
            menuHelper.addOption(1, "Comida Animal");
            menuHelper.addOption(2, "Comida Vegetal");
            menuHelper.addOption(3, "Cancelar");

            System.out.println("Seleccione el tipo de comida a añadir:");
            menuHelper.showMenu();

            int tipoComidaSeleccionado = inputHelper.readInt("Ingrese su opción:");

            if (tipoComidaSeleccionado == 3 || tipoComidaSeleccionado == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            menuHelper.clearOptions();
            menuHelper.addOption(1, "5");
            menuHelper.addOption(2, "10");
            menuHelper.addOption(3, "25");
            menuHelper.addOption(4, "Llenar");
            menuHelper.addOption(0, "Cancelar");

            System.out.println("Seleccione la cantidad de comida a añadir:");
            menuHelper.showMenu();

            int cantidadSeleccionada = inputHelper.readInt("Ingrese su opción: ");

            if (cantidadSeleccionada == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            int cantidadComida = 0;

            switch (cantidadSeleccionada) {
                case 1:
                    cantidadComida = 5;
                    break;
                case 2:
                    cantidadComida = 10;
                    break;
                case 3:
                    cantidadComida = 25;
                    break;
                case 4:
                    cantidadComida = almacenCentral.getCapacidadAlmacen()
                            - (almacenCentral.getCantidadComidaAnimal() + almacenCentral.getCantidadComidaVegetal());

                    if (cantidadComida <= 0) {
                        System.out.println("No hay suficiente espacio en el almacén central para añadir más comida.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Cantidad no válida.");
                    return;
            }

            // Comprobar si hay suficiente espacio en el almacén central
            if (tipoComidaSeleccionado == 1 && cantidadComida
                    + almacenCentral.getCantidadComidaAnimal() > almacenCentral.getCapacidadAlmacen()) {
                System.out.println("No hay suficiente espacio para añadir " + cantidadComida
                        + " de comida animal al almacén central.");
                return;
            } else if (tipoComidaSeleccionado == 2 && cantidadComida
                    + almacenCentral.getCantidadComidaVegetal() > almacenCentral.getCapacidadAlmacen()) {
                System.out.println("No hay suficiente espacio para añadir " + cantidadComida
                        + " de comida vegetal al almacén central.");
                return;
            }

            int totalCost = cantidadComida;
            if (cantidadComida >= 25) {
                totalCost -= (cantidadComida / 25) * 5;
            }

            if (tipoComidaSeleccionado == 1) {
                if (getMonedas().gastarMonedas(totalCost)) {
                    almacenCentral.setCantidadComidaAnimal(almacenCentral.getCantidadComidaAnimal() + cantidadComida);
                    System.out.println("Añadida " + cantidadComida + " de comida animal. "
                            + "Depósito de comida animal: " + almacenCentral.getCantidadComidaAnimal() + "/"
                            + almacenCentral.getCapacidadAlmacen() + " al "
                            + (almacenCentral.getCantidadComidaAnimal() * 100 / almacenCentral.getCapacidadAlmacen())
                            + "% de su capacidad.");
                } else {
                    System.out.println("No tienes suficientes monedas para añadir la comida animal.");
                }
            } else if (tipoComidaSeleccionado == 2) {
                if (getMonedas().gastarMonedas(totalCost)) {
                    almacenCentral.setCantidadComidaVegetal(almacenCentral.getCantidadComidaVegetal() + cantidadComida);
                    System.out.println("Añadida " + cantidadComida + " de comida vegetal. "
                            + "Depósito de comida vegetal: " + almacenCentral.getCantidadComidaVegetal() + "/"
                            + almacenCentral.getCapacidadAlmacen() + " al "
                            + (almacenCentral.getCantidadComidaVegetal() * 100 / almacenCentral.getCapacidadAlmacen())
                            + "% de su capacidad.");
                } else {
                    System.out.println("No tienes suficientes monedas para añadir la comida vegetal.");
                }
            }
        } else {
            // Si el almacén no está construido, se selecciona una piscifactoría
            Piscifactoria piscifactoriaSeleccionada = selectPisc();

            if (piscifactoriaSeleccionada != null) {
                menuHelper.clearOptions();
                menuHelper.addOption(1, "Comida Animal");
                menuHelper.addOption(2, "Comida Vegetal");
                menuHelper.addOption(3, "Cancelar");

                System.out.println("\nSeleccione el tipo de comida a añadir:");
                menuHelper.showMenu();

                int tipoComidaSeleccionado = inputHelper.readInt("Ingrese su opción: ");

                if (tipoComidaSeleccionado == 3 || tipoComidaSeleccionado == 0) {
                    System.out.println("Operación cancelada.");
                    return;
                }
                menuHelper.clearOptions();
                menuHelper.addOption(1, "5");
                menuHelper.addOption(2, "10");
                menuHelper.addOption(3, "25");
                menuHelper.addOption(4, "Llenar");
                menuHelper.addOption(0, "Cancelar");

                System.out.println("\nSeleccione la cantidad de comida a añadir:");
                menuHelper.showMenu();

                int cantidadSeleccionada = inputHelper.readInt("Ingrese su opción: ");

                if (cantidadSeleccionada == 0) {
                    System.out.println("Operación cancelada.");
                    return;
                }

                int cantidadComida = 0;

                switch (cantidadSeleccionada) {
                    case 1:
                        cantidadComida = 5;
                        break;
                    case 2:
                        cantidadComida = 10;
                        break;
                    case 3:
                        cantidadComida = 25;
                        break;
                    case 4:
                        if (piscifactoriaSeleccionada instanceof PiscifactoriaDeMar) {
                            cantidadComida = 100 - piscifactoriaSeleccionada.getComidaAnimalActual()
                                    - piscifactoriaSeleccionada.getComidaVegetalActual();
                        } else {
                            cantidadComida = 25 - piscifactoriaSeleccionada.getComidaAnimalActual()
                                    - piscifactoriaSeleccionada.getComidaVegetalActual();
                        }
                        if (cantidadComida <= 0) {
                            System.out.println("No hay suficiente espacio en la piscifactoría para añadir más comida.");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Cantidad no válida.");
                        return;
                }
                if (tipoComidaSeleccionado == 1 && cantidadComida + piscifactoriaSeleccionada
                        .getComidaAnimalActual() > piscifactoriaSeleccionada.getCapacidadTotal()) {
                    System.out.println("No hay suficiente espacio para añadir " + cantidadComida
                            + " de comida animal a la piscifactoría.");
                    return;
                } else if (tipoComidaSeleccionado == 2 && cantidadComida + piscifactoriaSeleccionada
                        .getComidaVegetalActual() > piscifactoriaSeleccionada.getCapacidadTotal()) {
                    System.out.println("No hay suficiente espacio para añadir " + cantidadComida
                            + " de comida vegetal a la piscifactoría.");
                    return;
                }

                int totalCost = cantidadComida;
                if (cantidadComida >= 25) {
                    totalCost -= (cantidadComida / 25) * 5;
                }

                if (tipoComidaSeleccionado == 1) {
                    if (getMonedas().gastarMonedas(totalCost)) {
                        piscifactoriaSeleccionada.setComidaAnimalActual(
                                piscifactoriaSeleccionada.getComidaAnimalActual() + cantidadComida);
                        System.out.println(
                                "Añadida " + cantidadComida + " de comida animal. " + "Depósito de comida animal: "
                                        + piscifactoriaSeleccionada.getComidaAnimalActual() + "/"
                                        + piscifactoriaSeleccionada.getCapacidadTotal() + " al "
                                        + (piscifactoriaSeleccionada.getComidaAnimalActual() * 100
                                                / piscifactoriaSeleccionada.getCapacidadTotal())
                                        + "% de su capacidad.");
                    } else {
                        System.out.println(
                                "No tienes suficientes monedas para añadir la comida animal a la piscifactoría.");
                    }
                } else if (tipoComidaSeleccionado == 2) {
                    if (getMonedas().gastarMonedas(totalCost)) {
                        piscifactoriaSeleccionada.setComidaVegetalActual(
                                piscifactoriaSeleccionada.getComidaVegetalActual() + cantidadComida);
                        System.out.println(
                                "Añadida " + cantidadComida + " de comida vegetal. " + "Depósito de comida vegetal: "
                                        + piscifactoriaSeleccionada.getComidaVegetalActual() + "/"
                                        + piscifactoriaSeleccionada.getCapacidadTotal() + " al "
                                        + (piscifactoriaSeleccionada.getComidaVegetalActual() * 100
                                                / piscifactoriaSeleccionada.getCapacidadTotal())
                                        + "% de su capacidad.");
                    } else {
                        System.out.println(
                                "No tienes suficientes monedas para añadir la comida vegetal a la piscifactoría.");
                    }
                }
            }
        }
    }

    /**
     * Método para seleccionar un pez mediante un menú.
     *
     * @param permitirHembras un booleano que indica si se permite seleccionar
     *                        hembras (true) o no (false).
     * @param esDeMar         un booleano que indica si se está seleccionando un pez
     *                        de mar (true) o de río (false).
     * @return un objeto de tipo Pez que representa el pez seleccionado por el
     *         usuario, o null si la selección fue inválida o si la operación fue
     *         cancelada.
     */
    public Pez selectFish(boolean permitirHembras, boolean esDeMar) {
        menuHelper.clearOptions();

        System.out.println("\nPeces disponibles: ");
        PecesDatos[] pecesEspecificos;

        if (esDeMar) {
            pecesEspecificos = new PecesDatos[] {
                    AlmacenPropiedades.SALMON_ATLANTICO,
                    AlmacenPropiedades.TRUCHA_ARCOIRIS,

                    AlmacenPropiedades.ARENQUE_ATLANTICO,
                    AlmacenPropiedades.BESUGO,
                    AlmacenPropiedades.LENGUADO_EUROPEO,
                    AlmacenPropiedades.LUBINA_EUROPEA,
                    AlmacenPropiedades.ROBALO
            };
        } else {
            pecesEspecificos = new PecesDatos[] {
                    AlmacenPropiedades.SALMON_ATLANTICO,
                    AlmacenPropiedades.TRUCHA_ARCOIRIS,

                    AlmacenPropiedades.CARPA_PLATEADA,
                    AlmacenPropiedades.PEJERREY,
                    AlmacenPropiedades.PERCA_EUROPEA,
                    AlmacenPropiedades.SALMON_CHINOOK,
                    AlmacenPropiedades.TILAPIA_NILO
            };
        }

        for (int i = 0; i < pecesEspecificos.length; i++) {
            PecesDatos pez = pecesEspecificos[i];
            menuHelper.addOption(i + 1, pez.getNombre());
        }

        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();

        int seleccion = inputHelper.readInt("Seleccione un pez: ");
        Pez pezSeleccionado = null;

        if (esDeMar) {
            switch (seleccion) {
                case 1:
                    pezSeleccionado = new SalmonAtlantico(!permitirHembras);
                    break;
                case 2:
                    pezSeleccionado = new ArenqueDelAtlantico(!permitirHembras);
                    break;
                case 3:
                    pezSeleccionado = new Besugo(!permitirHembras);
                    break;
                case 4:
                    pezSeleccionado = new LenguadoEuropeo(!permitirHembras);
                    break;
                case 5:
                    pezSeleccionado = new LubinaRayada(!permitirHembras);
                    break;
                case 6:
                    pezSeleccionado = new Robalo(!permitirHembras);
                    break;
                case 0:
                    System.out.println("Operación cancelada.");
                    return null;
                default:
                    System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
                    return null;
            }
        } else {
            switch (seleccion) {
                case 1:
                    pezSeleccionado = new SalmonAtlantico(!permitirHembras);
                    break;
                case 2:
                    pezSeleccionado = new TruchaArcoiris(!permitirHembras);
                    break;
                case 3:
                    pezSeleccionado = new CarpaPlateada(!permitirHembras);
                    break;
                case 4:
                    pezSeleccionado = new Pejerrey(!permitirHembras);
                    break;
                case 5:
                    pezSeleccionado = new PercaEuropea(!permitirHembras);
                    break;
                case 6:
                    pezSeleccionado = new SalmonChinook(!permitirHembras);
                    break;
                case 7:
                    pezSeleccionado = new TilapiaDelNilo(!permitirHembras);
                    break;
                case 0:
                    System.out.println("Operación cancelada.");
                    return null;
                default:
                    System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
                    return null;
            }
        }
        return pezSeleccionado;
    }

    /** Añade un pez al tanque seleccionado. */
    public void addFish() {
        Tanque tanqueSeleccionado = selectTank();

        if (tanqueSeleccionado == null) {
            System.out.println("Operación cancelada al seleccionar tanque.");
            return;
        }

        // Determina si el tanque es de río o de mar
        boolean esDeRio = piscifactorias.get(0).verificarTanqueYPiscifactoria(tanqueSeleccionado);
        Class<?> tipoPezActual = tanqueSeleccionado.getTipoPezActual();

        if (tipoPezActual == null) {
            System.out.println("El tanque está vacío. Puedes añadir peces de " + (esDeRio ? "río." : "mar."));
        } else {
            System.out.println("El tanque ya contiene: " + tipoPezActual.getSimpleName());
            System.out.println("Solo puedes añadir más " + tipoPezActual.getSimpleName() + ".");
        }

        // Usar el método selectFish para seleccionar un pez adecuado
        Pez pezSeleccionado = selectFish(true, !esDeRio); // Usamos !esDeRio para indicar que queremos peces de río si
                                                          // es de río

        if (pezSeleccionado == null) {
            System.out.println("Operación cancelada al seleccionar pez.");
            return;
        }

        // Comprobar si el pez seleccionado es del tipo correcto
        if (tipoPezActual != null && !tipoPezActual.equals(pezSeleccionado.getClass())) {
            System.out.printf("Este tanque solo acepta peces del tipo: %s, pero se intentó añadir: %s\n",
                    tipoPezActual.getSimpleName(), pezSeleccionado.getClass().getSimpleName());
            return;
        }

        // Intentar añadir el pez al tanque
        boolean added = tanqueSeleccionado.addPez(pezSeleccionado);
        if (added) {
            System.out.println("Pez añadido exitosamente al tanque.");
        } else {
            System.out.println("No se pudo añadir el pez al tanque. Tanque lleno o tipo de pez no permitido.");
        }
    }

    // Método para vender peces adultos de un tanque seleccionado
    public void sell() {
        Tanque tanqueSeleccionado = selectTank(); // Selección de tanque (y piscifactoría) automatizada
        if (tanqueSeleccionado == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        int pecesVendidos = 0;
        int totalDinero = 0;

        // Usamos un iterador para evitar ConcurrentModificationException
        Iterator<Pez> iterator = tanqueSeleccionado.getPeces().iterator();

        while (iterator.hasNext()) {
            Pez pez = iterator.next();

            // Verificamos si el pez es adulto y está vivo
            if (pez.getEdad() >= pez.getDatos().getMadurez() && pez.isVivo()) {
                // Actualizamos el total de dinero y la cuenta de peces vendidos
                totalDinero += pez.getDatos().getMonedas(); // Asumiendo que tienes un método para obtener el precio
                pecesVendidos++;

                // Eliminamos el pez del tanque
                iterator.remove();
            }
        }

        // Mostrar resultados de la venta
        if (pecesVendidos > 0) {
            System.out.println(pecesVendidos + " peces vendidos por " + totalDinero + " monedas.");
        } else {
            System.out.println("No se vendieron peces adultos en esta operación.");
        }
    }

    // Método para limpiar un tanque de todos los peces muertos
    public void cleanTank() {
        Tanque tanque = selectTank(); // Seleccionar un tanque
        if (tanque == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        List<Pez> peces = tanque.getPeces();

        // Usamos un bucle for en reversa para eliminar peces muertos
        for (int i = peces.size() - 1; i >= 0; i--) {
            Pez pez = peces.get(i);
            if (!pez.isVivo()) {
                peces.remove(i);
            }
        }
        System.out.println("Todos los peces muertos han sido eliminados del tanque.");
    }

    // Método para vaciar un tanque de todos los peces, independientemente de su
    // estado
    public void emptyTank() {
        Tanque tanque = selectTank();
        if (tanque == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        tanque.getPeces().clear(); // Elimina todos los peces del tanque
        System.out.println("El tanque ha sido vaciado.");
    }

    /**
     * Método que cuenta las piscifactorías de río.
     * 
     * @return El número de piscifactorías de río.
     */
    public int contarPiscifactoriasDeRio() {
        int contador = 0;
        for (Piscifactoria p : piscifactorias) {
            if (p instanceof PiscifactoriaDeRio) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método que cuenta las piscifactorías de mar.
     * 
     * @return El número de piscifactorías de mar.
     */
    public int contarPiscifactoriasDeMar() {
        int contador = 0;
        for (Piscifactoria p : piscifactorias) {
            if (p instanceof PiscifactoriaDeMar) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método que muestra el menú de mejoras para el usuario y permite
     * seleccionar y realizar mejoras en los edificios.
     */
    public void upgrade() {
        while (true) {
            menuHelper.clearOptions();

            System.out.println("\n=== Menú de Mejoras ===");
            menuHelper.addOption(1, "Comprar edificios");
            menuHelper.addOption(2, "Mejorar edificios");
            menuHelper.addOption(0, "Cancelar");

            menuHelper.showMenu();
            int opcion = inputHelper.readInt("Ingrese su opción: ");

            switch (opcion) {
                case 1:
                    // Comprar edificios
                    while (true) {
                        menuHelper.clearOptions();

                        System.out.println("\n=== Comprar Edificios ===");
                        menuHelper.addOption(1, "Piscifactoría");
                        if (almacenCentral != null && !almacenCentral.isConstruido()) {
                            menuHelper.addOption(2, "Almacén central");
                        }
                        menuHelper.addOption(0, "Cancelar");

                        menuHelper.showMenu();
                        int edificioAComprar = inputHelper.readInt("Seleccione el edificio a comprar: ");

                        if (edificioAComprar < 0 || (almacenCentral != null && edificioAComprar > 2)) {
                            System.out.println("\n¡Opción no válida! Por favor, intenta de nuevo.\n");
                            continue; // Volver a pedir la opción
                        }

                        if (edificioAComprar == 1) {
                            // Compra de Piscifactoría
                            String nombrePiscifactoria = inputHelper
                                    .readString("\nIngrese el nombre de la piscifactoría: ");

                            // Costos de la piscifactoría
                            int costoPiscifactoríaRio = 500 * (contarPiscifactoriasDeRio() + 1);
                            int costoPiscifactoríaMar = 2000 * (contarPiscifactoriasDeMar() + 1);

                            menuHelper.clearOptions();
                            System.out.println("\n=== Selección de Piscifactoría ===");
                            menuHelper.addOption(1, "Piscifactoría de río (" + costoPiscifactoríaRio + " monedas)");
                            menuHelper.addOption(2, "Piscifactoría de mar (" + costoPiscifactoríaMar + " monedas)");
                            menuHelper.addOption(0, "Cancelar");

                            menuHelper.showMenu();
                            int tipoSeleccionado = inputHelper.readInt("Seleccione el tipo de piscifactoría: ");

                            if (tipoSeleccionado < 0 || tipoSeleccionado > 2) {
                                System.out.println("\n¡Tipo de piscifactoría no válido! Intente de nuevo.\n");
                                continue;
                            }

                            Piscifactoria nuevaPiscifactoria = null;
                            if (tipoSeleccionado == 1) {
                                if (getMonedas().gastarMonedas(costoPiscifactoríaRio)) {
                                    nuevaPiscifactoria = new PiscifactoriaDeRio(nombrePiscifactoria, monedas);
                                    System.out.println(
                                            "\nPiscifactoría de Río '" + nombrePiscifactoria + "' comprada.\n");
                                } else {
                                    System.out.println(
                                            "\nNo tienes suficientes monedas para comprar la piscifactoría de río.\n");
                                }
                            } else if (tipoSeleccionado == 2) {
                                if (getMonedas().gastarMonedas(costoPiscifactoríaMar)) {
                                    nuevaPiscifactoria = new PiscifactoriaDeMar(nombrePiscifactoria, monedas);
                                    System.out.println(
                                            "\nPiscifactoría de Mar '" + nombrePiscifactoria + "' comprada.\n");
                                } else {
                                    System.out.println(
                                            "\nNo tienes suficientes monedas para comprar la piscifactoría de mar.\n");
                                }
                            } else if (tipoSeleccionado == 0) {
                                System.out.println("\nOperación cancelada.\n");
                                continue; // Volver al menú de edificios
                            }

                            // Añadir la nueva piscifactoría a la lista si fue creada
                            if (nuevaPiscifactoria != null) {
                                piscifactorias.add(nuevaPiscifactoria);
                            }

                        } else if (edificioAComprar == 2) {
                            // Comprar almacén central
                            if (almacenCentral != null && !almacenCentral.isConstruido()) {
                                if (getMonedas().gastarMonedas(2000)) {
                                    almacenCentral.construir();
                                    System.out.println("\nAlmacén central construido.\n");
                                } else {
                                    System.out.println(
                                            "\nNo tienes suficientes monedas para construir el almacén central.\n");
                                }
                            } else {
                                System.out.println("\nEl almacén central ya está construido.\n");
                            }
                        } else if (edificioAComprar == 0) {
                            System.out.println("\nOperación cancelada.\n");
                            break;
                        }
                    }
                    break;

                case 2:
                    // Mejorar edificios
                    while (true) {
                        menuHelper.clearOptions();
                        System.out.println("\n=== Mejorar Edificios ===");
                        menuHelper.addOption(1, "Piscifactoría");
                        menuHelper.addOption(2, "Almacén central");
                        menuHelper.addOption(0, "Cancelar");

                        menuHelper.showMenu();
                        int edificioAMejorar = inputHelper.readInt("Seleccione el edificio a mejorar: ");

                        if (edificioAMejorar < 0 || edificioAMejorar > 2) {
                            System.out.println("\n¡Opción no válida! Por favor, intenta de nuevo.\n");
                            continue;
                        }

                        if (edificioAMejorar == 1) {
                            // Mejorar piscifactoría
                            while (true) {
                                menuHelper.clearOptions();
                                System.out.println("\n=== Mejoras de Piscifactoría ===");
                                menuHelper.addOption(1, "Comprar tanque");
                                menuHelper.addOption(2, "Aumentar almacén de comida");
                                menuHelper.addOption(0, "Cancelar");
                        
                                menuHelper.showMenu();
                                int mejoraPiscifactoria = inputHelper.readInt("Seleccione la mejora: ");
                        
                                if (mejoraPiscifactoria < 0 || mejoraPiscifactoria > 2) {
                                    System.out.println("\n¡Opción no válida! Por favor, intenta de nuevo.\n");
                                    continue;
                                }
                        
                                if (mejoraPiscifactoria == 1) {
                                    // Comprar tanque
                                    int cantidadTanques = selectPisc().getTanques().size();
                                    int costoTanque = 150 * cantidadTanques;
                        
                                    if (getMonedas().gastarMonedas(costoTanque)) {
                                        System.out.println("\nTanque comprado para la piscifactoría.\n");
                                        break;
                                    } else {
                                        System.out.println("\nNo tienes suficientes monedas para comprar el tanque.\n");
                                    }
                                } else if (mejoraPiscifactoria == 2) {
                                    // Aumentar almacén de comida de la piscifactoría seleccionada
                                    Piscifactoria piscifactoriaSeleccionada = selectPisc(); // Método para seleccionar una piscifactoría
                                    if (piscifactoriaSeleccionada != null) {
                                        int costoAumento = 50; // Costo para aumentar la capacidad
                                        if (getMonedas().gastarMonedas(costoAumento)) {
                                            int nuevaCapacidad = piscifactoriaSeleccionada.setCapacidadMaximaComidaPiscifactoria(25);
                                            piscifactoriaSeleccionada.setCapacidadMaximaComidaPiscifactoria(nuevaCapacidad);
                                            System.out.println("\nCapacidad de comida aumentada en la piscifactoría.\n");
                                            break;
                                        } else {
                                            System.out.println("\nNo tienes suficientes monedas para aumentar el almacén de comida.\n");
                                        }
                                    } else {
                                        System.out.println("\nNo se seleccionó ninguna piscifactoría válida.\n");
                                    }
                                } else if (mejoraPiscifactoria == 0) {
                                    System.out.println("\nOperación cancelada.\n");
                                    break;
                                }
                            }
                        } else if (edificioAMejorar == 2) {
                            // Mejorar almacén central
                            if (almacenCentral != null && !almacenCentral.isConstruido()) {
                                System.out.println("\nEl almacén central no está construido. No se puede mejorar.\n");
                            } else {
                                while (true) {
                                    menuHelper.clearOptions();
                                    System.out.println("\n=== Mejoras de Almacén Central ===");
                                    menuHelper.addOption(1, "Aumentar capacidad");
                                    menuHelper.addOption(0, "Cancelar");

                                    menuHelper.showMenu();
                                    int mejoraAlmacenCentral = inputHelper.readInt("Seleccione la mejora: ");

                                    if (mejoraAlmacenCentral < 0 || mejoraAlmacenCentral > 1) {
                                        System.out.println("\n¡Opción no válida! Por favor, intenta de nuevo.\n");
                                        continue; // Volver a pedir la opción
                                    }

                                    if (mejoraAlmacenCentral == 1) {
                                        int costoMejora = 200; // Costo de mejora
                                        if (getMonedas().gastarMonedas(costoMejora)) {
                                            almacenCentral.aumentarCapacidad(50);
                                            System.out.println("\nCapacidad del almacén central aumentada.\n");
                                            break;
                                        } else {
                                            System.out.println(
                                                    "\nNo tienes suficientes monedas para aumentar la capacidad del almacén central.\n");
                                        }
                                    } else if (mejoraAlmacenCentral == 0) {
                                        System.out.println("\nOperación cancelada.\n");
                                        break;
                                    }
                                }
                            }
                        } else if (edificioAMejorar == 0) {
                            System.out.println("\nOperación cancelada.\n");
                            break;
                        }
                    }
                    break;

                case 0:
                    System.out.println("\nOperación cancelada.\n");
                    return;

                default:
                    System.out.println("\n¡Opción no válida! Por favor, intenta de nuevo.\n");
                    break;
            }
        }
    }

    /**
     * Añade cuatro peces seleccionados al azar de manera gratuita a una
     * piscifactoría elegida.
     * Solo se añaden peces compatibles con el tipo de piscifactoría (río o mar).
     */
    public void agregarPecesAleatorios() {

        Piscifactoria piscifactoria = selectPisc();
        if (piscifactoria == null) {
            System.out.println("Operación cancelada. No se seleccionó ninguna piscifactoría.");
            return;
        }

        Random rand = new Random();
        boolean esPiscifactoríaDeRio = piscifactoria instanceof PiscifactoriaDeRio;

        // Array de peces de río y de mar disponibles
        PecesDatos[] pecesDeRio = {
                AlmacenPropiedades.SALMON_ATLANTICO,
                AlmacenPropiedades.TRUCHA_ARCOIRIS,
                AlmacenPropiedades.PERCA_EUROPEA,
                AlmacenPropiedades.CARPA_PLATEADA,
                AlmacenPropiedades.PEJERREY
        };

        PecesDatos[] pecesDeMar = {
                AlmacenPropiedades.ARENQUE_ATLANTICO,
                AlmacenPropiedades.BESUGO,
                AlmacenPropiedades.LENGUADO_EUROPEO,
                AlmacenPropiedades.LUBINA_EUROPEA,
                AlmacenPropiedades.ROBALO
        };

        // Seleccionar el array adecuado según el tipo de piscifactoría
        PecesDatos[] pecesDisponibles = esPiscifactoríaDeRio ? pecesDeRio : pecesDeMar;

        // Seleccionar un pez aleatorio del array adecuado
        PecesDatos pezSeleccionadoDatos = pecesDisponibles[rand.nextInt(pecesDisponibles.length)];

        // Crear el pez seleccionado con sexo basado en la menor cantidad en el tanque
        Pez nuevoPez = null;
        boolean esMacho = piscifactoria.getTotalHembras() <= piscifactoria.getTotalMachos();

        // Crear el pez basado en la selección
        if (pezSeleccionadoDatos.equals(AlmacenPropiedades.SALMON_ATLANTICO)) {
            nuevoPez = new SalmonAtlantico(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.TRUCHA_ARCOIRIS)) {
            nuevoPez = new TruchaArcoiris(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.PERCA_EUROPEA)) {
            nuevoPez = new PercaEuropea(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.CARPA_PLATEADA)) {
            nuevoPez = new CarpaPlateada(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.PEJERREY)) {
            nuevoPez = new Pejerrey(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.ARENQUE_ATLANTICO)) {
            nuevoPez = new ArenqueDelAtlantico(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.BESUGO)) {
            nuevoPez = new Besugo(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.LENGUADO_EUROPEO)) {
            nuevoPez = new LenguadoEuropeo(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.LUBINA_EUROPEA)) {
            nuevoPez = new LubinaRayada(esMacho);
        } else if (pezSeleccionadoDatos.equals(AlmacenPropiedades.ROBALO)) {
            nuevoPez = new Robalo(esMacho);
        }

        // Añadir 4 peces del mismo tipo
        int pecesAnadidos = 0;
        for (int i = 0; i < 4; i++) {
            boolean pezAñadido = false;
            for (Tanque tanque : piscifactoria.getTanques()) {
                if (tanque.addPez(nuevoPez)) {
                    System.out.println("Pez añadido al tanque: " + nuevoPez.getDatos().getNombre());
                    pezAñadido = true;
                    pecesAnadidos++;
                    break;
                }
            }

            if (!pezAñadido) {
                System.out.println("No se pudo añadir el pez. La piscifactoría o tanque está lleno o no es compatible.");
                break;
            }
        }

        System.out.println("Se añadieron " + pecesAnadidos + " peces de forma gratuita a la piscifactoría seleccionada.");
    }

    /**
     * Punto de entrada principal del simulador. Inicializa la simulación y
     * entra en un bucle principal que muestra el menú y ejecuta las acciones
     * seleccionadas por el usuario.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        InputHelper inputHelper = new InputHelper();
        Simulador simulador = new Simulador();

        simulador.init();

        boolean running = true;
        while (running) {
            System.out.println();
            simulador.menu();

            int option = inputHelper.readInt("Ingrese su opción: ");

            switch (option) {
                case 1:
                    simulador.showGeneralStatus();
                    break;
                case 2:
                    simulador.showSpecificStatus();
                    break;
                case 3:
                    simulador.showTankStatus();
                    break;
                case 4:
                    simulador.showStats();
                    break;
                case 5:
                    simulador.showIctio();
                    break;
                case 6:
                    simulador.nextDay();
                    break;
                case 7:
                    simulador.addFood();
                    break;
                case 8:
                    simulador.addFish();
                    break;
                case 9:
                    simulador.sell();
                    break;
                case 10:
                    simulador.cleanTank();
                    break;
                case 11:
                    simulador.emptyTank();
                    break;
                case 12:
                    simulador.upgrade();
                    break;
                case 13:
                    int dias = inputHelper.readInt("Ingrese los dias para avanzar en el simulador: ");
                    simulador.avanzarDias(dias);
                    break;
                case 98:
                    simulador.agregarPecesAleatorios();
                    break;
                case 99:
                    simulador.getMonedas().ganarMonedas(1000);
                    break;
                case 14:
                    running = false; // Termina el bucle
                    System.out.println("\nSaliendo del simulador.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        inputHelper.close();
    }

    /**
     * @return el número de días
     */
    public int getDias() {
        return dias;
    }

    /**
     * Sets the number of days for the simulation.
     *
     * @param dias the number of days to set
     */
    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
     * @return String return the nombreEntidad
     */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /**
     * @param nombreEntidad the nombreEntidad to set
     */
    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    /**
     * @return List<Piscifactoria> return the piscifactorias
     */
    public List<Piscifactoria> getpiscifactorias() {
        return piscifactorias;
    }

    /**
     * @param piscifactorias the piscifactorias to set
     */
    public void setpiscifactorias(ArrayList<Piscifactoria> piscifactorias) {
        this.piscifactorias = piscifactorias;
    }

    /**
     * Devuelve el sistema de gestión de monedas utilizado en el simulador.
     *
     * @return El objeto SistemaMonedas actual.
     */
    public SistemaMonedas getMonedas() {
        return monedas;
    }

    /**
     * Establece el sistema de gestión de monedas utilizado en el simulador.
     *
     * @param monedas El objeto SistemaMonedas a establecer.
     */
    public void setMonedas(SistemaMonedas monedas) {
        this.monedas = monedas;
    }

    /**
     * Devuelve el objeto InputHelper utilizado para la lectura de datos desde
     * teclado.
     *
     * @return El objeto InputHelper actual.
     */
    public InputHelper getInputHelper() {
        return inputHelper;
    }

    /**
     * Establece el objeto InputHelper que se utilizará para la lectura de datos
     * desde teclado.
     *
     * @param inputHelper El objeto InputHelper a utilizar.
     */
    public void setInputHelper(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
    }
}