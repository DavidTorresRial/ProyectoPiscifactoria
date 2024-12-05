package commons;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import helpers.FileHelper;
import helpers.InputHelper;
import helpers.Logger;
import helpers.MenuHelper;

import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import propiedades.PecesProps;

import estadisticas.Estadisticas;

import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;
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

    /** Días transcurridos en la simulación. */
    private int dia = 0;

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
    public static SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Almacén central de comida para abastecer las piscifactorías. */
    public static AlmacenCentral almacenCentral;

    public static Logger logger;

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        FileHelper.crearCarpetas(new String[] {"logs", "saves", "rewards"});

        if (FileHelper.hayContenidoEnDirectorio("saves")) {
            String respuesta;
            do {
                respuesta = InputHelper.readString("¿Desea cargar una partida existente? (S/N): ").toUpperCase();
                switch (respuesta) {
                    case "S":
                        String partida = FileHelper.mostrarMenuConArchivos("saves");
                        logger = Logger.getInstance(partida);
                        cargarEstado(partida);
                        break;
                    case "N":
                        nombreEntidad = InputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
                        logger = Logger.getInstance(nombreEntidad);
                        logger.log("Inicio de la simulación: " + nombreEntidad);

                        nombrePiscifactoria = InputHelper.readString("\nIngrese el nombre de la primera Piscifactoria: ");
                        logger.log("Piscifactoría inicial: " + nombrePiscifactoria);

                        piscifactorias.add(new PiscifactoriaDeRio(nombrePiscifactoria));
                        piscifactorias.get(0).añadirComidaAnimal(piscifactorias.get(0).getCapacidadMaximaComida());
                        piscifactorias.get(0).añadirComidaVegetal(piscifactorias.get(0).getCapacidadMaximaComida());
                        break;
                }
            } while (!respuesta.equals("S") && !respuesta.equals("N"));
        } else {
            nombreEntidad = InputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
            logger = Logger.getInstance(nombreEntidad);
            logger.log("Inicio de la simulación: " + nombreEntidad);

            nombrePiscifactoria = InputHelper.readString("\nIngrese el nombre de la primera Piscifactoria: ");
            logger.log("Piscifactoría inicial: " + nombrePiscifactoria);

            piscifactorias.add(new PiscifactoriaDeRio(nombrePiscifactoria));
            piscifactorias.get(0).añadirComidaAnimal(piscifactorias.get(0).getCapacidadMaximaComida());
            piscifactorias.get(0).añadirComidaVegetal(piscifactorias.get(0).getCapacidadMaximaComida());
        }
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        System.out.println("\n=========================== Menú ===========================");
        MenuHelper.mostrarMenu(new String[] {
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
        MenuHelper.mostrarMenuCancelar(opciones);
    }

    /**
     * Muestra la lista de piscifactorías actuales y permite seleccionar una.
     * 
     * @return La piscifactoría seleccionada.
     */
    public Piscifactoria selectPisc() {
        menuPisc();
        int seleccion = InputHelper.solicitarNumero(0, piscifactorias.size()) - 1;

        if (seleccion >= 0) {
            return piscifactorias.get(seleccion);
        } else {
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
            System.out.println("\n=================== Seleccione un Tanque ===================");
            List<Tanque> tanques = piscifactoria.getTanques();

            String[] opcionesMenu = new String[tanques.size()];
            for (int i = 0; i < tanques.size(); i++) {
                Tanque tanque = tanques.get(i);
                String tipoPez = tanque.getPeces().isEmpty() ? "Vacío" : tanque.getPeces().get(0).getNombre();
                opcionesMenu[i] = "Tanque " + (i + 1) + " [" + tipoPez + "]";
            }
            MenuHelper.mostrarMenuCancelar(opcionesMenu);

            int seleccion = InputHelper.solicitarNumero(0, tanques.size()) - 1;

            if (seleccion >= 0) {
                return new AbstractMap.SimpleEntry<>(piscifactoria, tanques.get(seleccion));
            } else {
                return selectTank();
            }
        } else {
            return new AbstractMap.SimpleEntry<>(null, null);
        }
    }

    /** Método que muestra el estado de las piscifactorías. */
    public void showGeneralStatus() {
        System.out.println("\n============================= " + nombreEntidad + " =============================");
        System.out.println("Día actual: " + dia);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.showStatus();
        }

        if (almacenCentral != null) {
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("\nNo hay Almacén Central disponible.");
        }
    }

    /** Muestra el estado de una piscifactoría seleccionada por el usuario. */
    public void showSpecificStatus() {
        Piscifactoria piscifactoria = selectPisc();

        if (piscifactoria != null) {
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
        System.out.println("\n======================= Estadísticas =======================");
        estadisticas.mostrar();
    }

    /** Muestra el estado de un pez seleccionado por el usuario. */
    public void showIctio() {
        System.out.println("\n======================== Ictiopedia ========================");
        String[] pecesNombres = {
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
        };
        MenuHelper.mostrarMenuCancelar(pecesNombres);

        int opcion = InputHelper.solicitarNumero(0, pecesNombres.length);

        if (opcion != 0) {
            String pezNombreSeleccionado = pecesNombres[opcion - 1];

            PecesDatos pezSeleccionado = AlmacenPropiedades.getPropByName(pezNombreSeleccionado);

            System.out.println("\nNombre: " + pezSeleccionado.getNombre());
            System.out.println("Nombre científico: " + pezSeleccionado.getCientifico());
            System.out.println("Tipo: " + pezSeleccionado.getTipo());
            System.out.println("Coste: " + pezSeleccionado.getCoste());
            System.out.println("Monedas: " + pezSeleccionado.getMonedas());
            System.out.println("Huevos: " + pezSeleccionado.getHuevos());
            System.out.println("Ciclo: " + pezSeleccionado.getCiclo());
            System.out.println("Madurez: " + pezSeleccionado.getMadurez());
            System.out.println("Óptimo: " + pezSeleccionado.getOptimo());

            PecesProps[] propiedades = pezSeleccionado.getPropiedades();
            System.out.print("Propiedades: ");
            for (int i = 0; i < propiedades.length; i++) {
                System.out.print(propiedades[i]);
                if (i < propiedades.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("\nTipo de cría: " + pezSeleccionado.getPiscifactoria());
        }
    }

    /** Simula un día en todas las piscifactorías. */
    public void nextDay() { // TODO implementar
        int pecesVendidos = 0, monedasGanadas = 0;
        int totalPecesVendidos = 0, totalMonedasGanadas = 0;

        dia++;

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.nextDay();
            System.out.println("\nPiscifactoría " + piscifactoria.getNombre() + ": " + pecesVendidos + " peces vendidos por " + monedasGanadas + " monedas");
            // totalPecesVendidos += piscifactoria.getPecesVendidos();
            // totalMonedasGanadas += piscifactoria.getMonedasGanadas();
        }
        System.out.println("\n" + totalPecesVendidos + " peces vendidos por un total de " + totalMonedasGanadas + " monedas.");
        logger.log("Fin del día " + dia + ".");
    }

    /** Simula varios días consecutivos en todas las piscifactorías. */
    public void nextDay(int dias) {
        for (int i = 0; i < dias; i++) {
            nextDay();
        }
    }

    /** Añade comida al almacén central si está construido, o a una piscifactoría seleccionada. */
    public void addFood() {
        String[] menuComida = { "Comida Animal", "Comida Vegetal" };
        String[] menuCantidad = { "5", "10", "25", "Llenar" };

        int capacidadTotal = 0;
        int comidaActual = 0;

        if (almacenCentral == null) {
            Piscifactoria piscifactoria = selectPisc();
            if (piscifactoria != null) {
                capacidadTotal = piscifactoria.getCapacidadMaximaComida();

                int opcionComida;
                do {
                    System.out.println("\n============= Añadir Comida a la Piscifactoría =============");
                    MenuHelper.mostrarMenuCancelar(menuComida);
                    opcionComida = InputHelper.solicitarNumero(0, menuComida.length);

                    if (opcionComida != 0) {
                        boolean animal = opcionComida == 1;

                        if (animal) {
                            comidaActual = piscifactoria.getComidaAnimalActual();
                        } else {
                            comidaActual = piscifactoria.getComidaVegetalActual();
                        }

                        int opcionCantidad;
                        do {
                            System.out.println("\n==================== Cantidad de Comida ====================");
                            MenuHelper.mostrarMenuCancelar(menuCantidad);
                            opcionCantidad = InputHelper.solicitarNumero(0, menuCantidad.length);

                            if (opcionCantidad != 0) {
                                int cantidadComida = switch (opcionCantidad) {
                                    case 1 -> 5;
                                    case 2 -> 10;
                                    case 3 -> 25;
                                    case 4 -> capacidadTotal - comidaActual;
                                    default -> 0;
                                };

                                if (!(comidaActual + cantidadComida > capacidadTotal) && !(comidaActual == capacidadTotal)) {
                                    int costo = monedas.calcularDescuento(cantidadComida);
                                    if (monedas.gastarMonedas(costo)) {
                                        if (animal) {
                                            piscifactoria.añadirComidaAnimal(cantidadComida);
                                            comidaActual += cantidadComida;
                                            System.out.println(
                                                    "\n" + cantidadComida + " de comida de tipo Animal comprada por "
                                                            + costo + " monedas. Se almacena en la piscifactoría "
                                                            + piscifactoria.getNombre() + ".");

                                            logger.log(cantidadComida + " de comida de tipo animal comprada. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
                                        } else {
                                            piscifactoria.añadirComidaVegetal(cantidadComida);
                                            comidaActual += cantidadComida;
                                            System.out.println(
                                                    "\n" + cantidadComida + " de comida de tipo Vegetal comprada por "
                                                            + costo + " monedas. Se almacena en la piscifactoría "
                                                            + piscifactoria.getNombre() + ".");

                                            logger.log(cantidadComida + " de comida de tipo vegetal comprada. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
                                        }
                                    }
                                } else {
                                    System.out.println("\nNo hay suficiente espacio para añadir esa cantidad de comida.");
                                }
                            }
                        } while (opcionCantidad != 0);
                    }
                } while (opcionComida != 0);
                addFood();
            }
        } else {
            capacidadTotal = almacenCentral.getCapacidadAlmacen();
            int opcionComida;
            do {
                System.out.println("\n============= Añadir Comida al Almacén Central =============");
                MenuHelper.mostrarMenuCancelar(menuComida);
                opcionComida = InputHelper.solicitarNumero(0, menuComida.length);

                if (opcionComida != 0) {
                    boolean animal = opcionComida == 1;

                    if (animal) {
                        comidaActual = almacenCentral.getCantidadComidaAnimal();
                    } else {
                        comidaActual = almacenCentral.getCantidadComidaVegetal();
                    }

                    int opcionCantidad;
                    do {
                        System.out.println("\n==================== Cantidad de Comida ====================");
                        MenuHelper.mostrarMenuCancelar(menuCantidad);
                        opcionCantidad = InputHelper.solicitarNumero(0, menuCantidad.length);

                        if (opcionCantidad != 0) {
                            int cantidadComida = switch (opcionCantidad) {
                                case 1 -> 5;
                                case 2 -> 10;
                                case 3 -> 25;
                                case 4 -> capacidadTotal - comidaActual;
                                default -> 0;
                            };

                            if (!(comidaActual + cantidadComida > capacidadTotal) && !(comidaActual == capacidadTotal)) {
                                int costo = monedas.calcularDescuento(cantidadComida);
                                if (monedas.gastarMonedas(costo)) {
                                    if (animal) {
                                        almacenCentral.añadirComidaAnimal(cantidadComida);
                                        comidaActual += cantidadComida;
                                        System.out.println(
                                                "\n" + cantidadComida + " de comida de tipo Animal comprada por "
                                                        + costo + " monedas. Se almacena en el almacén central.\r");

                                        logger.log(cantidadComida + " de comida de tipo animal comprada. Se almacena en el almacén central.");
                                    } else {
                                        almacenCentral.añadirComidaVegetal(cantidadComida);
                                        comidaActual += cantidadComida;
                                        System.out.println(
                                                "\n" + cantidadComida + " de comida de tipo Vegetal comprada por "
                                                        + costo + " monedas. Se almacena en el almacén central.\r");
                                        
                                        logger.log(cantidadComida + " de comida de tipo vegetal comprada. Se almacena en el almacén central.");
                                    }
                                }
                            } else {
                                System.out.println("\nNo hay suficiente espacio para añadir esa cantidad de comida.");
                            }
                        }
                    } while (opcionCantidad != 0);
                }
            } while (opcionComida != 0);
        }
    }

    /** Añade un pez al tanque seleccionado. */
    public void addFish() {
        var selectTank = selectTank();
        Tanque tanqueSeleccionado = selectTank.getValue();

        if (tanqueSeleccionado != null) {
            int opcion = -1;
            while (opcion != 0) {
                boolean esDeRio = selectTank.getKey() instanceof PiscifactoriaDeRio;

                String[] opcionesPeces;
                if (tanqueSeleccionado.getPeces().isEmpty()) {
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
                    opcionesPeces = new String[] { tanqueSeleccionado.getPeces().get(0).getNombre() };
                }

                System.out.println("\n======================= Pez a añadir =======================");
                MenuHelper.mostrarMenuCancelar(opcionesPeces);

                opcion = InputHelper.solicitarNumero(0, opcionesPeces.length);

                boolean sexo = tanqueSeleccionado.getHembras() <= tanqueSeleccionado.getMachos() ? false : true;

                Pez pezSeleccionado = null;

                if (tanqueSeleccionado.getPeces().isEmpty()) {
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
                    }
                } else {
                    switch (opcion) {
                        case 1:
                            Pez pez = tanqueSeleccionado.getPeces().get(0);
                            pezSeleccionado = pez.clonar(sexo);
                            break;
                        case 0:
                    }
                }
                if (opcion != 0 && tanqueSeleccionado.addFish(pezSeleccionado)) {
                    estadisticas.registrarNacimiento(pezSeleccionado.getNombre());
                    System.out.println("\n" + pezSeleccionado.getNombre() + (pezSeleccionado.isSexo() ? " (M)" : " (H)")
                            + " comprado por " + pezSeleccionado.getDatos().getCoste() + " monedas. Añadido al tanque "
                            + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría "
                            + selectTank.getKey().getNombre() + ".");
                    
                    logger.log(pezSeleccionado.getNombre() + (pezSeleccionado.isSexo() ? " (M)" : " (H)" + " comprado. Añadido al tanque" 
                            + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría " 
                            + selectTank.getKey().getNombre() + "."));
                }
            }
        }
    }

    /** Vende todos los peces adultos del tanque seleccionado, ganando monedas y registrando la venta. */
    public void sell() {
        var selectTank = selectTank();
        Tanque tanqueSeleccionado = selectTank.getValue();
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
                System.out.println("Vendidos " + pecesVendidos + " peces de la piscifactoría " + selectTank.getKey().getNombre() + " de forma manual por " + totalDinero + " monedas.");
                logger.log("Vendidos " + pecesVendidos + " peces de la piscifactoría " + selectTank.getKey().getNombre() + " de forma manual.");
            } else {
                System.out.println("\nNo hay peces adultos para vender.");
            }
        }
    }

    /** Elimina todos los peces muertos del tanque seleccionado. */
    public void cleanTank() {
        var selectTank = selectTank();
        Tanque tanque = selectTank.getValue();
        if (tanque != null) {
            List<Pez> peces = tanque.getPeces();

            for (int i = peces.size() - 1; i >= 0; i--) {
                Pez pez = peces.get(i);
                if (!pez.isVivo()) {
                    peces.remove(i);
                }
            }
            System.out.println("\nLimpiado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
            logger.log("Limpiado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
        }
    }

    /** Vacía el tanque seleccionado eliminando todos los peces. */
    public void emptyTank() {
        var selectTank = selectTank();
        Tanque tanque = selectTank.getValue();
        if (tanque != null) {
            tanque.getPeces().clear();
            System.out.println("\nVaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
            logger.log("Vaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() +".");
        }
    }

    /** Método que muestra un menú de que permite al usuario comprar o mejorar edificios. */
    public void upgrade() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=================== Gestión de Edificios ===================");
            String[] opcionesMenuPrincipal = { "Comprar edificios", "Mejorar edificios" };
            MenuHelper.mostrarMenuCancelar(opcionesMenuPrincipal);

            int opcionPrincipal = InputHelper.solicitarNumero(0, opcionesMenuPrincipal.length);

            switch (opcionPrincipal) {
                case 1:
                    gestionarCompraEdificios();
                    break;
                case 2:
                    gestionarMejoraEdificios();
                    break;
                case 0:
                    salir = true;
            }
        }
    }

    /** Método que gestiona la compra de edificios. */
    private void gestionarCompraEdificios() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===================== Comprar Edificio =====================");
            String[] opcionesCompra = (almacenCentral == null) ? new String[] { "Piscifactoría", "Almacén central" } : new String[] { "Piscifactoría" };
            MenuHelper.mostrarMenuCancelar(opcionesCompra);

            int opcionCompra = InputHelper.solicitarNumero(0, opcionesCompra.length);

            switch (opcionCompra) {
                case 1:
                    addPiscifactoria();
                    break;
                case 2:
                    if (monedas.gastarMonedas(2000)) {
                        almacenCentral = new AlmacenCentral();
                        System.out.println("\nComprado el almacén central.");
                        logger.log("Comprado el almacén central.");
                    } else {
                        System.out.println("\nNecesitas 2000 monedas para construir el almacén central.");
                    }
                    break;
                case 0:
                    salir = true;
            }
        }
    }

    /** Método que gestiona la mejora de edificios. */
    private void gestionarMejoraEdificios() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===================== Mejorar Edificio =====================");
            String[] opcionesMejora = (almacenCentral != null) ? new String[] { "Piscifactoría", "Almacén central" } : new String[] { "Piscifactoría" };

            MenuHelper.mostrarMenuCancelar(opcionesMejora);

            int opcionMejora = InputHelper.solicitarNumero(0, opcionesMejora.length);

            switch (opcionMejora) {
                case 1:
                    upgradePiscifactoria();
                    break;
                case 2:
                    almacenCentral.aumentarCapacidad();
                    break;
                case 0:
                    salir = true;
            }
        }
    }

    /** Permite al usuario mejorar una piscifactoría seleccionada. */
    public void upgradePiscifactoria() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=================== Mejorar Piscifactoría ==================");
            String[] opcionesMejoraPiscifactoria = { "Comprar tanque", "Aumentar almacén de comida" };
            MenuHelper.mostrarMenuCancelar(opcionesMejoraPiscifactoria);
            int mejoraPiscifactoria = InputHelper.readInt("Seleccione la mejora: ");

            Piscifactoria piscifactoriaSeleccionada = selectPisc();

            if (piscifactoriaSeleccionada != null) {
                switch (mejoraPiscifactoria) {
                    case 1:
                        piscifactoriaSeleccionada.addTanque();
                        break;
                    case 2:
                        piscifactoriaSeleccionada.upgradeFood();
                        break;
                    case 0:
                        salir = true;
                }
            }
            salir = true;
        }
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

    /** Agrega una nueva piscifactoría solicitando el nombre de la piscifactoría y el tipo (de río o de mar). */
    public void addPiscifactoria() {
        String nombrePiscifactoria = InputHelper.readString("\nIngrese el nombre de la piscifactoría: ");

        int costoPiscifactoríaRio = 500 * (contarPiscifactoriasDeRio() + 1);
        int costoPiscifactoríaMar = 2000 * (contarPiscifactoriasDeMar() + 1);

        String[] opcionesPiscifactoria = {
                "Piscifactoría de río (" + costoPiscifactoríaRio + " monedas)",
                "Piscifactoría de mar (" + costoPiscifactoríaMar + " monedas)",
        };
        System.out.println("\nSeleccione el tipo de piscifactoría: \n");
        MenuHelper.mostrarMenuCancelar(opcionesPiscifactoria);
        int tipoSeleccionado = InputHelper.solicitarNumero(0, opcionesPiscifactoria.length);

        Piscifactoria nuevaPiscifactoria = null;
        if (tipoSeleccionado == 1) {
            if (monedas.gastarMonedas(costoPiscifactoríaRio)) {
                nuevaPiscifactoria = new PiscifactoriaDeRio(nombrePiscifactoria);
                System.out.println("\nComprada la piscifactoría de rio " + nombrePiscifactoria + " por " + costoPiscifactoríaRio + " monedas.");
                piscifactorias.add(nuevaPiscifactoria);

                logger.log("Comprada la piscifactoria de rio " + nombrePiscifactoria + ".");
            } else {
                System.out.println("\nNo tienes suficientes monedas para comprar la piscifactoría de río.");
            }
        } else if (tipoSeleccionado == 2) {
            if (monedas.gastarMonedas(costoPiscifactoríaMar)) {
                nuevaPiscifactoria = new PiscifactoriaDeMar(nombrePiscifactoria);
                System.out.println("\nComprada la piscifactoría de mar " + nombrePiscifactoria + " por " + costoPiscifactoríaMar + " monedas.");
                piscifactorias.add(nuevaPiscifactoria);

                logger.log("Comprada la piscifactoria de mar " + nombrePiscifactoria + ".");
            } else {
                System.out.println("\nNo tienes suficientes monedas para comprar la piscifactoría de mar.");
            }
        }
    }

    /** Añade 4 peces al primer tanque de la piscifactoría seleccionada que tenga espacio suficiente. */
    public void pecesRandom() {
        Piscifactoria piscifactoriaSeleccionada = selectPisc();
        Random random = new Random();

        if (piscifactoriaSeleccionada != null) {
            List<Tanque> tanques = piscifactoriaSeleccionada.getTanques();
            Tanque tanqueSeleccionado = null;
            Pez pezSeleccionado = null;

            for (int i = 0; i < tanques.size(); i++) {
                if ((tanques.get(i).getCapacidad() - tanques.get(i).getPeces().size()) >= 4) {
                    tanqueSeleccionado = tanques.get(i);
                    break;
                }
            }

            if (tanqueSeleccionado != null) {
                boolean esDeRio = piscifactoriaSeleccionada instanceof PiscifactoriaDeRio;

                for (int i = 0; i < 4; i++) {
                    boolean sexo = tanqueSeleccionado.getHembras() <= tanqueSeleccionado.getMachos() ? false : true;

                    pezSeleccionado = (!tanqueSeleccionado.getPeces().isEmpty())
                            ? tanqueSeleccionado.getPeces().get(0).clonar(sexo)
                            : (esDeRio
                                    ? new Pez[] {
                                            new SalmonAtlantico(sexo),
                                            new ArenqueDelAtlantico(sexo),
                                            new CarpaPlateada(sexo),
                                            new Pejerrey(sexo),
                                            new PercaEuropea(sexo),
                                            new SalmonChinook(sexo),
                                            new TilapiaDelNilo(sexo)
                                    }
                                    : new Pez[] {
                                            new SalmonAtlantico(sexo),
                                            new TruchaArcoiris(sexo),
                                            new ArenqueDelAtlantico(sexo),
                                            new Besugo(sexo),
                                            new LenguadoEuropeo(sexo),
                                            new LubinaRayada(sexo),
                                            new Robalo(sexo)
                                    })[random.nextInt(6)];
                    tanqueSeleccionado.getPeces().add(pezSeleccionado);
                }
                System.out.println("\nSe han añadido 4 " + pezSeleccionado.getNombre() + " al tanque "
                        + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría "
                        + piscifactoriaSeleccionada.getNombre() + ".");

                logger.log("Añadidos peces mediante la opción oculta a la piscifactoría " + piscifactoriaSeleccionada.getNombre() + ".");
            }
        }
    }


















    public void guardarEstado() { // TODO 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Crear estructura principal con LinkedHashMap para preservar el orden
        Map<String, Object> estado = new LinkedHashMap<>();

        estado.put("implementados", Arrays.asList(
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
        ));
        estado.put("empresa", nombreEntidad);
        estado.put("dia", dia);
        estado.put("monedas", monedas.getMonedas());
        estado.put("orca", Simulador.estadisticas.exportarDatos(new String[] {
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
                AlmacenPropiedades.TILAPIA_NILO.getNombre()}));

        // Edificios - Almacén
        Map<String, Object> almacenMap = new LinkedHashMap<>();
        almacenMap.put("disponible", almacenCentral != null && almacenCentral.getCapacidadAlmacen() > 0);
        almacenMap.put("capacidad", almacenCentral != null ? almacenCentral.getCapacidadAlmacen() : 200);
        almacenMap.put("comida", Map.of(
                "vegetal", almacenCentral != null ? almacenCentral.getCantidadComidaVegetal() : 0,
                "animal", almacenCentral != null ? almacenCentral.getCantidadComidaAnimal() : 0
        ));
        estado.put("edificios", Map.of("almacen", almacenMap));

        // Piscifactorías
        List<Map<String, Object>> piscifactoriasList = new ArrayList<>();
        for (Piscifactoria piscifactoria : piscifactorias) {
            Map<String, Object> piscifactoriaMap = new LinkedHashMap<>();
            piscifactoriaMap.put("nombre", piscifactoria.getNombre());
            piscifactoriaMap.put("tipo", piscifactoria instanceof PiscifactoriaDeRio ? 0 : 1);
            piscifactoriaMap.put("capacidad", piscifactoria.getCapacidadMaximaComida());
            piscifactoriaMap.put("comida", Map.of(
                    "vegetal", piscifactoria.getComidaVegetalActual(),
                    "animal", piscifactoria.getComidaAnimalActual()
            ));

            // Tanques
            List<Map<String, Object>> tanquesList = new ArrayList<>();
            for (Tanque tanque : piscifactoria.getTanques()) {
                Map<String, Object> tanqueMap = new LinkedHashMap<>();
                if (!tanque.getPeces().isEmpty()) {
                    tanqueMap.put("pez", tanque.getPeces().get(0).getNombre());
                    tanqueMap.put("num", tanque.getNumeroTanque());
                    tanqueMap.put("datos", Map.of(
                            "vivos", tanque.getPeces().size(),
                            "maduros", tanque.getMaduros(),
                            "fertiles", tanque.getFertiles() 
                    ));

                    // Peces
                    List<Map<String, Object>> pecesList = new ArrayList<>();
                    for (Pez pez : tanque.getPeces()) {
                        Map<String, Object> pezMap = new LinkedHashMap<>();
                        pezMap.put("edad", pez.getEdad());
                        pezMap.put("sexo", pez.isSexo());
                        pezMap.put("vivo", pez.isVivo());
                        pezMap.put("maduro", pez.isMaduro());
                        pezMap.put("fertil", pez.isFertil());
                        pezMap.put("ciclo", pez.getDatos().getCiclo());
                        pezMap.put("alimentado", pez.isAlimentado());
                        pecesList.add(pezMap);
                    }
                    tanqueMap.put("peces", pecesList);
                } else {
                    tanqueMap.put("pez", "Sin peces");
                    tanqueMap.put("num", tanque.getNumeroTanque());
                    tanqueMap.put("datos", "No disponible");
                }
                tanquesList.add(tanqueMap);
            }
            piscifactoriaMap.put("tanques", tanquesList);
            piscifactoriasList.add(piscifactoriaMap);
        }
        estado.put("piscifactorias", piscifactoriasList);

        // Guardar el JSON en un archivo
        try (FileWriter writer = new FileWriter("saves/" + nombreEntidad + ".save")) {
            gson.toJson(estado, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarEstado(String archivoPartida) {
        try {
            // Leer el archivo JSON
            FileReader reader = new FileReader("saves/" + archivoPartida + ".save");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
    
            // Cargar empresa
            nombreEntidad = jsonObject.has("empresa") && !jsonObject.get("empresa").isJsonNull()
                    ? jsonObject.get("empresa").getAsString()
                    : "Empresa desconocida";
    
            // Cargar día y monedas
            dia = jsonObject.has("dia") && !jsonObject.get("dia").isJsonNull()
                    ? jsonObject.get("dia").getAsInt()
                    : 0;
            monedas.setMonedas(jsonObject.has("monedas") && !jsonObject.get("monedas").isJsonNull()
                    ? jsonObject.get("monedas").getAsInt()
                    : 0);
    
            // Cargar Almacén
            if (jsonObject.has("edificios") && !jsonObject.get("edificios").isJsonNull()) {
                JsonObject edificios = jsonObject.getAsJsonObject("edificios");
                if (edificios.has("almacen") && !edificios.get("almacen").isJsonNull()) {
                    JsonObject almacen = edificios.getAsJsonObject("almacen");

                    // Verificar si el almacén está disponible
                    boolean disponible = almacen.get("disponible").getAsBoolean();
                    if (disponible) {
                        int capacidadAlmacen = almacen.get("capacidad").getAsInt();
                        JsonObject comida = almacen.getAsJsonObject("comida");
                        int comidaVegetal = comida.get("vegetal").getAsInt();
                        int comidaAnimal = comida.get("animal").getAsInt();
                        almacenCentral = new AlmacenCentral(capacidadAlmacen, comidaVegetal, comidaAnimal);
                    } else {
                        almacenCentral = null; // No crear almacén si no está disponible
                    }
                }
            }

            // Cargar piscifactorías
            if (jsonObject.has("piscifactorias") && !jsonObject.get("piscifactorias").isJsonNull()) {
                piscifactorias.clear(); // Limpia la lista antes de cargar
                JsonArray piscifactoriasArray = jsonObject.getAsJsonArray("piscifactorias");
            
                for (JsonElement piscifactoriaElement : piscifactoriasArray) {
                    JsonObject piscifactoriaJson = piscifactoriaElement.getAsJsonObject();
            
                    // Crear piscifactoría
                    String nombre = piscifactoriaJson.get("nombre").getAsString();
                    int tipo = piscifactoriaJson.get("tipo").getAsInt();
                    Piscifactoria piscifactoria = (tipo == 0)
                            ? new PiscifactoriaDeRio(nombre)
                            : new PiscifactoriaDeMar(nombre);
            
                    // Configurar capacidades
                    int capacidadMaxima = piscifactoriaJson.get("capacidad").getAsInt();
                    piscifactoria.setCapacidadMaximaComida(capacidadMaxima);
            
                    // Configurar cantidades de comida
                    JsonObject comida = piscifactoriaJson.getAsJsonObject("comida");
                    int comidaVegetal = comida.get("vegetal").getAsInt();
                    int comidaAnimal = comida.get("animal").getAsInt();
                    piscifactoria.setCantidadComidaVegetal(comidaVegetal);
                    piscifactoria.setCantidadComidaAnimal(comidaAnimal);
            
                    // Cargar tanques
                    piscifactoria.getTanques().clear(); // Limpia tanques existentes para evitar duplicados
                    JsonArray tanquesArray = piscifactoriaJson.getAsJsonArray("tanques");
            
                    for (JsonElement tanqueElement : tanquesArray) {
                        JsonObject tanqueJson = tanqueElement.getAsJsonObject();
                        Tanque tanque = new Tanque(
                                tanqueJson.get("num").getAsInt(),
                                25 // O ajustar capacidad si es dinámico
                        );
            
                        // Cargar peces
                        if (tanqueJson.has("peces") && !tanqueJson.get("peces").isJsonNull()) {
                            JsonArray pecesArray = tanqueJson.getAsJsonArray("peces");
                            for (JsonElement pezElement : pecesArray) {
                                JsonObject pezJson = pezElement.getAsJsonObject();
            
                                // Crear pez y configurar atributos
                                String tipoPez = tanqueJson.has("pez") ? tanqueJson.get("pez").getAsString() : "Desconocido";
                                boolean sexo = pezJson.get("sexo").getAsBoolean();
                                Pez pez = crearPez(tipoPez, sexo);
            
                                if (pez != null) {
                                    pez.setEdad(pezJson.get("edad").getAsInt());
                                    pez.setVivo(pezJson.get("vivo").getAsBoolean());
                                    pez.setFertil(pezJson.get("fertil").getAsBoolean());
                                    pez.setCiclo(pezJson.get("ciclo").getAsInt());
                                    pez.setAlimentado(pezJson.get("alimentado").getAsBoolean());
                                    tanque.getPeces().add(pez);
                                }
                            }
                        }
            
                        piscifactoria.getTanques().add(tanque);
                    }
            
                    piscifactorias.add(piscifactoria);
                }
            }            
    
            System.out.println("Partida cargada exitosamente desde: " + archivoPartida);
    
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
    
    
    
    /**
     * Crea una instancia de Pez según su tipo y sexo.
     *
     * @param tipo El tipo del pez (nombre del pez).
     * @param sexo true para macho, false para hembra.
     * @return Una instancia del pez correspondiente o null si no se reconoce el tipo.
     */
    private Pez crearPez(String tipo, boolean sexo) {
        switch (tipo) {
            case "Pejerrey":
                return new Pejerrey(sexo);
            case "Trucha":
                //return new Trucha(sexo);
            case "Tilapia":
                //return new Tilapia(sexo);
            // Agrega más casos según sea necesario
            default:
                return null;
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
        Simulador simulador = new Simulador();
        simulador.init();

        boolean running = true;
        while (running) {

            simulador.menu();
            int option = InputHelper.readInt("Ingrese su opción: ");

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
                    if (almacenCentral != null) {
                        almacenCentral.distribuirComida(simulador.piscifactorias);
                    }
                    simulador.guardarEstado();
                    break;
                case 7:
                    simulador.addFood();
                    if (almacenCentral != null) {
                        almacenCentral.distribuirComida(simulador.piscifactorias);
                    }
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
                    int dias = InputHelper.readInt("\nIngrese los dias para avanzar en el simulador: ");
                    simulador.nextDay(dias);
                    break;
                case 98:
                    simulador.pecesRandom();
                    break;
                case 99:
                    Simulador.monedas.ganarMonedas(1000);
                    System.out.println("\nAñadidas 1000 monedas mediante la opción oculta. Monedas actuales, "
                            + monedas.getMonedas());
                    Simulador.logger.log("Añadidas monedas mediante la opción oculta.");
                    break;
                case 14:
                    running = false;
                    simulador.guardarEstado();
                    System.out.println("\nSaliendo del simulador.");
                    logger.log("Cierre de la partida");
                    break;
                default:
                    System.out.println("\nOpción no válida. Por favor, intente de nuevo.");
            }
        }
        InputHelper.close();
        Simulador.logger.close();
    }
}