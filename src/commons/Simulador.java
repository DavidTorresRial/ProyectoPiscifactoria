package commons;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import helpers.InputHelper;
import helpers.MenuHelper;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import propiedades.PecesProps;
import estadisticas.Estadisticas;

import tanque.Tanque;

import peces.Pez;
import peces.tipos.doble.*;
import peces.tipos.mar.*;
import peces.tipos.rio.*;
import piscifactoria.Piscifactoria;

/**
 * La clase Simulador gestiona la simulación de una piscifactoría,
 * permitiendo realizar diversas operaciones de administración de peces,
 * tanques y recursos monetarios. Incluye un menú interactivo para la
 * navegación entre las diferentes opciones de gestión de la piscifactoría.
 * 
 * @author David, Fran, Marcos.
 */
public class Simulador {

    /** Días transcurridos en la simulación. */
    private int dias = 0;

    /** Lista de piscifactorías en el sistema. */
    private List<Piscifactoria> piscifactorias = new ArrayList<>();

    /** Nombre de la entidad o partida en la simulación. */
    private String nombreEntidad;

    /** Nombre de la piscifactoría. */
    private String nombrePiscifactoria;

    /** Sistema de estadísticas para registrar la cría, venta y ganancias de los peces. */
    public static Estadisticas estadisticas = new Estadisticas(new String[] {
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

    /** Sistema de monedas para manejar transacciones. */
    private SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Almacén central de comida para abastecer las piscifactorías. */
    private AlmacenCentral almacenCentral = new AlmacenCentral();

    /** Utilidad para manejar la entrada de datos del usuario. */
    private InputHelper inputHelper = InputHelper.getInstance();

    /** Utilidad para gestionar los menús de la simulación. */
    private MenuHelper menuHelper = new MenuHelper();

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();
        nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");

        piscifactorias.add(new Piscifactoria(nombrePiscifactoria, true));
        piscifactorias.get(0).setComidaAnimalActual(piscifactorias.get(0).getCapacidadMaximaComidaPiscifactoria());
        piscifactorias.get(0).setComidaVegetalActual(piscifactorias.get(0).getCapacidadMaximaComidaPiscifactoria());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        menuHelper.mostrarMenu(new String[] {
                "Estado general",
                "Estado piscifactoría",
                "Estado tanque",
                "Informes",
                "Ictiopedia",
                "Pasar día",
                "Comprar Comida",
                "Comprar peces",
                "Vender peces",
                "Limpiar tanque",
                "Vaciar tanque",
                "Mejorar",
                "Pasar varios días",
                "Salir"
        });
    }

    /** Método que muestra la lista de piscifactorías actuales en forma de menú. */
    public void menuPisc() {
        System.out.println("\nSeleccione una opción:");
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");

        String[] opciones = new String[piscifactorias.size()];

        for (int i = 0; i < piscifactorias.size(); i++) {
            Piscifactoria piscifactoria = piscifactorias.get(i);
            opciones[i] = piscifactoria.getNombre() + " [" + piscifactoria.getTotalVivos() + "/"
                    + piscifactoria.getTotalPeces() + "/" + piscifactoria.getCapacidadTotal() + "]";
        }
        menuHelper.mostrarMenuCancelar(opciones);
    }

    /**
     * Muestra la lista de piscifactorías actuales y permite seleccionar una.
     * 
     * @return La piscifactoría seleccionada.
     */
    public Piscifactoria selectPisc() {
        menuPisc();
        int seleccion = inputHelper.solicitarNumero(0, piscifactorias.size()) - 1;

        if (seleccion >= 0) {
            return piscifactorias.get(seleccion);
        } else {
            System.out.println("\nOperación cancelada.");
            return null;
        }
    }

    /**
     * Permite seleccionar un tanque y su piscifactoría.
     * 
     * @return Un Map.Entry con la piscifactoría y el tanque seleccionados, o null si se cancela.
     */
    public Map.Entry<Piscifactoria, Tanque> selectTank() {
        Piscifactoria piscifactoria = selectPisc();

        if (piscifactoria != null) {
            System.out.println("\nSeleccione un Tanque:");
            List<Tanque> tanques = piscifactoria.getTanques();

            String[] opcionesMenu = new String[tanques.size()];
            for (int i = 0; i < tanques.size(); i++) {
                Tanque tanque = tanques.get(i);
                String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName() : "Vacío";
                opcionesMenu[i] = "Tanque " + (i + 1) + " [" + tipoPez + "]";
            }
            menuHelper.mostrarMenuCancelar(opcionesMenu);

            int seleccion = inputHelper.solicitarNumero(0, tanques.size()) - 1;

            if (seleccion >= 0) {
                return new AbstractMap.SimpleEntry<>(piscifactoria, tanques.get(seleccion));
            } else {
                System.out.println("\nOperación cancelada.");
                return selectTank();
            }
        } else {
            return new AbstractMap.SimpleEntry<>(null, null);
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

        if (almacenCentral.isConstruido()) { // TODO mirar la implementacion del Almacén Central
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("No hay Almacén Central disponible.");
        }
        System.out.println("==================================================");
    }

    /** Muestra el estado de una piscifactoría seleccionada por el usuario. */
    public void showSpecificStatus() {
        Piscifactoria piscifactoria = selectPisc();

        if (piscifactoria == null) {
            return;
        } else {
            piscifactoria.showTankStatus();
        }
    }

    /** Muestra el estado de los peces en un tanque seleccionado de una piscifactoría. */
    public void showTankStatus() {
        Tanque tanqueSeleccionado = selectTank().getValue();

        if (tanqueSeleccionado == null) {
            return;
        } else {
            if (tanqueSeleccionado.getPeces().isEmpty()) {
                System.out.println("\nEste tanque no contiene peces actualmente.");
                return;
            } else {
                System.out.println("\nEstado de los peces en el Tanque " + tanqueSeleccionado.getNumeroTanque() + ":");

                for (Pez pez : tanqueSeleccionado.getPeces()) {
                    pez.showStatus();
                }
            }
        }
    }

    /** Muestra un desglose de las estadísticas por cada tipo de pez. */
    public void showStats() {
        System.out.println("\n=============== Estadísticas ===============");
        estadisticas.mostrar();
        System.out.println("===========================================");
    }

    /** Muestra el estado de un pez seleccionado por el usuario. */
    public void showIctio() {
        System.out.println("\nSelecciona un pez para consultar su información: ");
        menuHelper.mostrarMenuCancelar(new String[] {
                "Salmon del Atlántico",
                "Trucha Arcoíris",
                "Arenque del Atlántico",
                "Besugo",
                "Lenguado Europeo",
                "Lubina Rayada",
                "Robalo",
                "Carpa Plateada",
                "Pejerrey",
                "Perca Europea",
                "Salmón Chinook",
                "Tilapia del Nilo",
        });

        int opcion = inputHelper.solicitarNumero(0, 12);
        Pez pezSeleccionado = null;

        switch (opcion) {
            case 1:
                pezSeleccionado = new SalmonAtlantico(true);
                break;
            case 2:
                pezSeleccionado = new TruchaArcoiris(true);
                break;
            case 3:
                pezSeleccionado = new ArenqueDelAtlantico(true);
                break;
            case 4:
                pezSeleccionado = new Besugo(true);
                break;
            case 5:
                pezSeleccionado = new LenguadoEuropeo(true);
                break;
            case 6:
                pezSeleccionado = new LubinaRayada(true);
                break;
            case 7:
                pezSeleccionado = new Robalo(true);
                break;
            case 8:
                pezSeleccionado = new CarpaPlateada(true);
                break;
            case 9:
                pezSeleccionado = new Pejerrey(true);
                break;
            case 10:
                pezSeleccionado = new PercaEuropea(true);
                break;
            case 11:
                pezSeleccionado = new SalmonChinook(true);
                break;
            case 12:
                pezSeleccionado = new TilapiaDelNilo(true);
                break;
            case 0:
                System.out.println("\nOperación cancelada.");
                break;
            default:
                System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
        }

        if (opcion != 0) {
            System.out.println("\nInformación del pez seleccionado:");
            System.out.println("Nombre: " + pezSeleccionado.getDatos().getNombre());
            System.out.println("Nombre científico: " + pezSeleccionado.getDatos().getCientifico());
            System.out.println("Tipo: " + pezSeleccionado.getDatos().getTipo());
            System.out.println("Coste: " + pezSeleccionado.getDatos().getCoste());
            System.out.println("Monedas: " + pezSeleccionado.getDatos().getMonedas());
            System.out.println("Huevos: " + pezSeleccionado.getDatos().getHuevos());
            System.out.println("Ciclo: " + pezSeleccionado.getDatos().getCiclo());
            System.out.println("Madurez: " + pezSeleccionado.getDatos().getMadurez());
            System.out.println("Óptimo: " + pezSeleccionado.getDatos().getOptimo());

            PecesProps[] propiedades = pezSeleccionado.getDatos().getPropiedades();
            System.out.print("Propiedades: ");
            for (int i = 0; i < propiedades.length; i++) {
                System.out.print(propiedades[i]);
                if (i < propiedades.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("\nTipo de cría: " + pezSeleccionado.getDatos().getPiscifactoria());
        }
    }

    /** Simula un día en todas las piscifactorías. */
    public void nextDay() { // TODO implementar
        // int totalPecesVendidos = 0, totalMonedasGanadas = 0;

        dias++;

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.nextDay();
        }
        // showGeneralStatus();
        // System.out.println(totalPecesVendidos + " peces vendidos por un total de " +
        // totalMonedasGanadas + " monedas.");
    }

    /** Añade comida al almacén central o a una piscifactoría seleccionada. */
    public void addFood() { // TODO implementar
    }

    /** Añade un pez al tanque seleccionado. */
    public void addFish() {
        var selectTank = selectTank();
        Tanque tanqueSeleccionado = selectTank.getValue();

        if (tanqueSeleccionado != null) {
            int opcion;
            do {
                boolean esDeRio = selectTank.getKey().esDeRio();
                Class<?> tipoPezActual = tanqueSeleccionado.getTipoPezActual();

                String[] opcionesPeces;
                if (tipoPezActual == null) {
                    if (esDeRio) {
                        opcionesPeces = new String[] {
                                "Salmón del Atlántico",
                                "Trucha Arcoíris",
                                "Carpa Plateada",
                                "Pejerrey",
                                "Perca Europea",
                                "Salmón Chinook",
                                "Tilapia del Nilo"
                        };
                    } else {
                        opcionesPeces = new String[] {
                                "Salmon del Atlántico",
                                "Trucha Arcoíris",
                                "Arenque del Atlántico",
                                "Besugo",
                                "Lenguado Europeo",
                                "Lubina Rayada",
                                "Robalo"
                        };
                    }
                } else {
                    opcionesPeces = new String[] { tipoPezActual.getSimpleName() };
                }

                System.out.println();
                menuHelper.mostrarMenuCancelar(opcionesPeces);

                opcion = inputHelper.solicitarNumero(0, opcionesPeces.length);

                boolean sexo = tanqueSeleccionado.getHembras() <= tanqueSeleccionado.getMachos() ? false : true;

                Pez pezSeleccionado = null;

                if (tipoPezActual == null) {
                    switch (opcion) {
                        case 1:
                            pezSeleccionado = new SalmonAtlantico(sexo);
                            break;
                        case 2:
                            pezSeleccionado = new TruchaArcoiris(sexo);
                            break;
                        case 3:
                            pezSeleccionado = esDeRio ? new CarpaPlateada(sexo) : new ArenqueDelAtlantico(sexo);
                            break;
                        case 4:
                            pezSeleccionado = esDeRio ? new Pejerrey(sexo) : new Besugo(sexo);
                            break;
                        case 5:
                            pezSeleccionado = esDeRio ? new PercaEuropea(sexo) : new LenguadoEuropeo(sexo);
                            break;
                        case 6:
                            pezSeleccionado = esDeRio ? new SalmonChinook(sexo) : new LubinaRayada(sexo);
                            break;
                        case 7:
                            pezSeleccionado = esDeRio ? new TilapiaDelNilo(sexo) : new Robalo(sexo);
                            break;
                        case 0:
                            System.out.println("\nOperación cancelada.");
                            break;
                        default:
                            System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
                            continue;
                    }
                } else {
                    switch (opcion) {
                        case 1:
                            try {
                                pezSeleccionado = (Pez) tipoPezActual.getDeclaredConstructor(boolean.class)
                                        .newInstance(sexo);
                            } catch (Exception e) {
                                System.out.println("\nError al crear el pez: " + e.getMessage());
                                continue;
                            }
                            break;
                        case 0:
                            System.out.println("\nOperación cancelada.");
                            break;
                        default:
                            System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
                            continue;
                    }
                }
                if (opcion != 0 && tanqueSeleccionado.addFish(pezSeleccionado)) {
                    estadisticas.registrarNacimiento(pezSeleccionado.getNombre());
                    System.out.println(pezSeleccionado.getNombre() + " añadido exitosamente al tanque.");
                }
            } while (opcion != 0);
        }
    }

    /** Vende todos los peces adultos del tanque seleccionado, ganando monedas y registrando la venta. */
    public void sell() {
        Tanque tanqueSeleccionado = selectTank().getValue();
        if (tanqueSeleccionado != null) {
            int pecesVendidos = 0;
            int totalDinero = 0;

            Iterator<Pez> iterator = tanqueSeleccionado.getPeces().iterator();

            while (iterator.hasNext()) {
                Pez pez = iterator.next();

                if (pez.getEdad() >= pez.getDatos().getMadurez() && pez.isVivo()) {
                    monedas.ganarMonedas(pez.getDatos().getMonedas());
                    estadisticas.registrarVenta(pez.getNombre(), pez.getDatos().getMonedas());

                    totalDinero += pez.getDatos().getMonedas();
                    pecesVendidos++;

                    iterator.remove();
                }
            }
            if (pecesVendidos > 0) {
                System.out.println(pecesVendidos + " peces vendidos por " + totalDinero + " monedas.");
            } else {
                System.out.println("\nNo hay peces adultos para vender.");
            }
        }
    }

    /** Elimina todos los peces muertos del tanque seleccionado. */
    public void cleanTank() {
        Tanque tanque = selectTank().getValue();
        if (tanque != null) {
            List<Pez> peces = tanque.getPeces();

            for (int i = peces.size() - 1; i >= 0; i--) {
                Pez pez = peces.get(i);
                if (!pez.isVivo()) {
                    peces.remove(i);
                }
            }
            System.out.println("\nTodos los peces muertos han sido eliminados del tanque.");
        }
    }

    /** Vacía el tanque seleccionado eliminando todos los peces. */
    public void emptyTank() {
        Tanque tanque = selectTank().getValue();
        if (tanque != null) {
            tanque.getPeces().clear();
            System.out.println("\nEl tanque ha sido vaciado.");
        }
    }

    /**
     * Punto de entrada principal del simulador. Inicializa la simulación y
     * entra en un bucle principal que muestra el menú y ejecuta las acciones
     * seleccionadas por el usuario.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        InputHelper inputHelper = InputHelper.getInstance();
        Simulador simulador = new Simulador();

        simulador.init();

        boolean running = true;
        while (running) {

            System.out.println();
            simulador.menu();
            int option = inputHelper.readInt("Ingrese su opción: (Del menú principal): ");

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
                    // simulador.addFood();
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
                    // simulador.upgrade();
                    break;
                case 13:
                    // int dias = inputHelper.readInt("Ingrese los dias para avanzar en el
                    // simulador: ");
                    // simulador.avanzarDias(dias);
                    break;
                case 98:
                    // simulador.agregarPecesAleatorios();
                    break;
                case 99:
                    simulador.getMonedas().ganarMonedas(1000);
                    break;
                case 14:
                    running = false;
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
     * Retorna las piscifactorías.
     *
     * @return Lista de piscifactorías.
     */
    public List<Piscifactoria> getPiscifactorias() {
        return piscifactorias;
    }

    /**
     * Retorna el sistema de monedas.
     *
     * @return Sistema de monedas.
     */
    public SistemaMonedas getMonedas() {
        return monedas;
    }
}