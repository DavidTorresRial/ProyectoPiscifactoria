package commons;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import helpers.FileHelper;
import helpers.InputHelper;
import helpers.Logger;
import helpers.MenuHelper;
import helpers.Transcriptor;

import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import propiedades.PecesProps;
import estadisticas.Estadisticas;

import recompensas.CrearRecompensa;
import recompensas.UsarRecompensa;

import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;
import tanque.Tanque;

import peces.Pez;
import peces.tipos.doble.*;
import peces.tipos.mar.*;
import peces.tipos.rio.*;
import persistencia.GestorEstado;

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
    private static List<Piscifactoria> piscifactorias = new ArrayList<>();

    /** Nombre de la entidad o partida en la simulación. */
    public static String nombreEntidad;

    /** Nombre de la piscifactoría. */
    private String nombrePiscifactoria;

    /** Lista de nombres de peces implementados. */
    private final static String[] pecesImplementados = {
        AlmacenPropiedades.DORADA.getNombre(),
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

    /** Sistema de monedas para manejar transacciones. */
    public static SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Sistema de estadísticas para registrar la cría, venta y ganancias de los peces. */
    public static Estadisticas estadisticas;

    /** Almacén central de comida para abastecer las piscifactorías. */
    public static AlmacenCentral almacenCentral;

    /**  Logger para gestionar los registros de eventos y errores del sistema. */
    public static Logger logger;

    /**  Transcriptor para registrar los eventos de las piscifactorías en archivos. */
    public static Transcriptor transcriptor;

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        FileHelper.crearCarpetas(new String[] {"transcripciones", "logs", "saves", "rewards"});

        String respuesta = "N";

        if (FileHelper.hayContenidoEnDirectorio("saves")) {
            do {
                respuesta = InputHelper.readString("¿Desea cargar una partida existente? (S/N): ").toUpperCase();
                if (respuesta.equals("S")) {
                    String partida = FileHelper.mostrarMenuConArchivos("saves");
                    if (partida == null) {
                        respuesta = "N";
                        System.out.println();
                    } else {
                        logger = Logger.getInstance();
                        transcriptor = Transcriptor.getInstance(partida);

                        GestorEstado.load(this, partida);
                    }
                }
            } while (!respuesta.equals("S") && !respuesta.equals("N"));
        }

        if (respuesta.equals("N")) {
            nombreEntidad = InputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
            logger = Logger.getInstance();
            transcriptor = Transcriptor.getInstance(nombreEntidad);

            logger.log(nombreEntidad, "Inicio de la simulación: " + nombreEntidad);
            transcriptor.transcribir("Inicio de la simulación: " + nombreEntidad);
            transcriptor.transcribir("Dinero inicial: " + monedas.getMonedas() + " monedas.");

            transcriptor.transcribir("\n========= Peces =========\n" +
                "Rio:\n" +
                "  -" + AlmacenPropiedades.CARPA_PLATEADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.PEJERREY.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.PERCA_EUROPEA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.SALMON_CHINOOK.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.TILAPIA_NILO.getNombre() + "\n" +
                "\nMar:\n" +
                "  -" + AlmacenPropiedades.ARENQUE_ATLANTICO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.BESUGO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.LENGUADO_EUROPEO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.LUBINA_RAYADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.ROBALO.getNombre() + "\n" +
                "\nDoble:\n" +
                "  -" + AlmacenPropiedades.DORADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.SALMON_ATLANTICO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.TRUCHA_ARCOIRIS.getNombre()
            );
            estadisticas = new Estadisticas(pecesImplementados);
            transcriptor.transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");

            nombrePiscifactoria = InputHelper.readString("\nIngrese el nombre de la primera Piscifactoria: ");
            logger.log(nombreEntidad, "Piscifactoría inicial: " + nombrePiscifactoria + ".");
            transcriptor.transcribir("Piscifactoría inicial: " + nombrePiscifactoria + ".");

            piscifactorias.add(new PiscifactoriaDeRio(nombrePiscifactoria));
            piscifactorias.get(0).añadirComidaAnimal(piscifactorias.get(0).getCapacidadMaximaComida());
            piscifactorias.get(0).añadirComidaVegetal(piscifactorias.get(0).getCapacidadMaximaComida());
            
            GestorEstado.guardarEstado(this);
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
                "Recompensas",
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
            System.out.println(almacenCentral.toString());
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
        MenuHelper.mostrarMenuCancelar(pecesImplementados);

        int opcion = InputHelper.solicitarNumero(0, pecesImplementados.length);

        if (opcion != 0) {
            String pezNombreSeleccionado = pecesImplementados[opcion - 1];

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

            System.out.print("Alimentación: ");
            for (int i = 0; i < propiedades.length; i++) {
                switch (propiedades[i]) {
                    case CARNIVORO:
                        System.out.print("Carnívoro");
                        break;
                    case FILTRADOR:
                        System.out.print("Herbívoro");
                        break;
                    case OMNIVORO:
                        System.out.print("Omnívoro");
                        break;
                    default:
                        break;
                }
            }

            System.out.print("\nPropiedades: ");
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
    public void nextDay() {
        int pecesVendidos = 0, monedasGanadas = 0;
        int totalPecesVendidos = 0, totalMonedasGanadas = 0;

        dia++;

        System.out.println("\nFin del día " + dia + ".\n");
        for (Piscifactoria piscifactoria : piscifactorias) {
            int[] resultadoPiscifactoria = piscifactoria.nextDay();
            pecesVendidos = resultadoPiscifactoria[0];
            monedasGanadas = resultadoPiscifactoria[1];
            totalPecesVendidos += pecesVendidos;
            totalMonedasGanadas += monedasGanadas;
            System.out.println("Piscifactoría " + piscifactoria.getNombre() + ": " + pecesVendidos + " peces vendidos por " + monedasGanadas + " monedas");
        }
        System.out.println("\n" + totalPecesVendidos + " peces vendidos por un total de " + totalMonedasGanadas + " monedas.");
        logger.log(nombreEntidad, "Fin del día " + dia + ".");
        transcriptor.transcribir("Fin del día " + dia + ".");

        int pecesDeRio = 0, pecesDeMar = 0;

        for (Piscifactoria piscifactoria : piscifactorias) {
            if (piscifactoria instanceof PiscifactoriaDeRio) {
                pecesDeRio += piscifactoria.getTotalVivos();
            } else if (piscifactoria instanceof PiscifactoriaDeMar) {
                pecesDeMar += piscifactoria.getTotalVivos();
            }
        }

        transcriptor.transcribir("Peces actuales: " + pecesDeRio + " de río y " + pecesDeMar + " de mar.");
        transcriptor.transcribir(totalMonedasGanadas + " monedas ganadas por un total de " + monedas.getMonedas() + ".");

        transcriptor.transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");
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

                                            logger.log(nombreEntidad, cantidadComida + " de comida de tipo animal comprada. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
                                            transcriptor.transcribir(cantidadComida + " de comida de tipo animal comprada por " + costo + " monedas. Se almacena en la piscifactoria " + piscifactoria.getNombre() + ".");
                                        } else {
                                            piscifactoria.añadirComidaVegetal(cantidadComida);
                                            comidaActual += cantidadComida;
                                            System.out.println(
                                                    "\n" + cantidadComida + " de comida de tipo Vegetal comprada por "
                                                            + costo + " monedas. Se almacena en la piscifactoría "
                                                            + piscifactoria.getNombre() + ".");

                                            logger.log(nombreEntidad, cantidadComida + " de comida de tipo vegetal comprada. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
                                            transcriptor.transcribir(cantidadComida + " de comida de tipo vegetal comprada por " + costo + " monedas. Se almacena en la piscifactoria " + piscifactoria.getNombre() + ".");
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

                                        logger.log(nombreEntidad, cantidadComida + " de comida de tipo animal comprada. Se almacena en el almacén central.");
                                        transcriptor.transcribir(cantidadComida + " de comida de tipo animal comprada por " + costo + " monedas. Se almacena en el almacén central.");
                                    } else {
                                        almacenCentral.añadirComidaVegetal(cantidadComida);
                                        comidaActual += cantidadComida;
                                        System.out.println(
                                                "\n" + cantidadComida + " de comida de tipo Vegetal comprada por "
                                                        + costo + " monedas. Se almacena en el almacén central.\r");
                                        
                                        logger.log(nombreEntidad, cantidadComida + " de comida de tipo vegetal comprada. Se almacena en el almacén central.");
                                        transcriptor.transcribir(cantidadComida + " de comida de tipo animal comprada por " + costo + " monedas. Se almacena en el almacén central.");
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
                                "Dorada",
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
                                "Dorada",
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
                            pezSeleccionado = new Dorada(sexo);
                            break;
                        case 2:
                            pezSeleccionado = new SalmonAtlantico(sexo);
                            break;
                        case 3:
                            pezSeleccionado = new TruchaArcoiris(sexo);
                            break;
                        case 4:
                            pezSeleccionado = esDeRio ? new CarpaPlateada(sexo) : new ArenqueDelAtlantico(sexo);
                            break;
                        case 5:
                            pezSeleccionado = esDeRio ? new Pejerrey(sexo) : new Besugo(sexo);
                            break;
                        case 6:
                            pezSeleccionado = esDeRio ? new PercaEuropea(sexo) : new LenguadoEuropeo(sexo);
                            break;
                        case 7:
                            pezSeleccionado = esDeRio ? new SalmonChinook(sexo) : new LubinaRayada(sexo);
                            break;
                        case 8:
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
                    
                    logger.log(nombreEntidad, pezSeleccionado.getNombre() + (pezSeleccionado.isSexo() ? " (M)" : " (H)") + " comprado. Añadido al tanque" 
                            + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría " 
                            + selectTank.getKey().getNombre() + ".");
                    transcriptor.transcribir(pezSeleccionado.getNombre() + (pezSeleccionado.isSexo() ? " (M)" : " (H)") + " comprado por " 
                            + pezSeleccionado.getDatos().getCoste() + " monedas. Añadido al tanque " + tanqueSeleccionado.getNumeroTanque() 
                            + " de la piscifactoria " + selectTank.getKey().getNombre());
                }
            }
        }
    }

    /** Vende todos los peces adultos del tanque seleccionado, ganando monedas y registrando la venta. */
    public void sell() {
        var selectTank = selectTank();
        Tanque tanqueSeleccionado = selectTank.getValue();
        Piscifactoria.sellFish(tanqueSeleccionado);
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
            logger.log(nombreEntidad, "Limpiado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
            transcriptor.transcribir(("Limpiado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + "."));
        }
    }

    /** Vacía el tanque seleccionado eliminando todos los peces. */
    public void emptyTank() {
        var selectTank = selectTank();
        Tanque tanque = selectTank.getValue();
        if (tanque != null) {
            tanque.getPeces().clear();
            System.out.println("\nVaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
            logger.log(nombreEntidad, "Vaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() +".");
            transcriptor.transcribir("Vaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() +".");
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
                        logger.log(nombreEntidad, "Comprado el almacén central.");
                        transcriptor.transcribir("Comprado el almacén central.");
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

            int mejoraPiscifactoria = InputHelper.solicitarNumero(0, opcionesMejoraPiscifactoria.length);

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

                logger.log(nombreEntidad, "Comprada la piscifactoria de rio " + nombrePiscifactoria + ".");
                transcriptor.transcribir("Comprada la piscifactoria de rio "  + nombrePiscifactoria + " por " + costoPiscifactoríaRio + " monedas.");
            } else {
                System.out.println("\nNo tienes suficientes monedas para comprar la piscifactoría de río.");
            }
        } else if (tipoSeleccionado == 2) {
            if (monedas.gastarMonedas(costoPiscifactoríaMar)) {
                nuevaPiscifactoria = new PiscifactoriaDeMar(nombrePiscifactoria);
                System.out.println("\nComprada la piscifactoría de mar " + nombrePiscifactoria + " por " + costoPiscifactoríaMar + " monedas.");
                piscifactorias.add(nuevaPiscifactoria);

                logger.log(nombreEntidad, "Comprada la piscifactoria de mar " + nombrePiscifactoria + ".");
                transcriptor.transcribir("Comprada la piscifactoria de mar "  + nombrePiscifactoria + " por " + costoPiscifactoríaMar + " monedas.");
            } else {
                System.out.println("\nNo tienes suficientes monedas para comprar la piscifactoría de mar.");
            }
        }
    }

    /** Muestra un menú con las recompensas disponibles y permite al usuario seleccionar una. */
    private void recompensas() {
        int opcion;
        do {
            System.out.println("\n================== Recompensas Disponibles =================");
            String[] opciones = FileHelper.getRewards();
            String[] opcionesSinCorchete = FileHelper.getRewardsWithoutBrackets(opciones);

            MenuHelper.mostrarMenuCancelar(opciones);
            opcion = InputHelper.solicitarNumero(0, opciones.length) - 1;

            if (opcion >= 0 && opcion < opciones.length) {
                String seleccion = opcionesSinCorchete[opcion];

                switch (seleccion) {
                    case "Algas I":
                        UsarRecompensa.readFood("algas_1.xml");
                        break;

                    case "Comida I":
                        UsarRecompensa.readFood("comida_1.xml"); 
                        break;

                    case "Pienso I":
                        UsarRecompensa.readFood("pienso_1.xml"); 
                        break;

                    case "Monedas I":
                        UsarRecompensa.readCoins("monedas_1.xml"); 
                        break;

                    case "Tanque de rio":
                        Piscifactoria selectPiscRio = selectPisc();
                        if (selectPiscRio instanceof PiscifactoriaDeRio) {
                            if (selectPiscRio.getTanques().size() < selectPiscRio.getNumeroMaximoTanques()) {
                                if (UsarRecompensa.readTank("tanque_r.xml")) {
                                    selectPiscRio.getTanques().add(new Tanque(selectPiscRio.getTanques().size() + 1, 100));
                                }
                            } else {
                                System.out.println("\nCapacidad máxima alcanzada: no se pueden añadir más tanques a \"" + selectPiscRio.getNombre() + "\".");
                            }
                        } else {
                            System.out.println("\nNo puedes añadir el tanque a una Piscifactoria de Rio.");
                        }
                        break;

                    case "Tanque de mar":
                        Piscifactoria selectPiscMar = selectPisc();
                        if (selectPiscMar instanceof PiscifactoriaDeMar) {
                            if (selectPiscMar.getTanques().size() < selectPiscMar.getNumeroMaximoTanques()) {
                                if (UsarRecompensa.readTank("tanque_m.xml")) {
                                    selectPiscMar.getTanques().add(new Tanque(selectPiscMar.getTanques().size() + 1, 100));
                                }
                            } else {
                                System.out.println("\nCapacidad máxima alcanzada: no se pueden añadir más tanques a \"" + selectPiscMar.getNombre() + "\".");
                            }
                        } else {
                            System.out.println("\nNo puedes añadir el tanque a una Piscifactoria de Mar.");
                        }
                        break;

                    case "Piscifactoria de rio":
                        Piscifactoria pr = UsarRecompensa.readPiscifactoria(true);
                        if (pr != null) {
                            piscifactorias.add(pr);
                        }
                        break;

                    case "Piscifactoria de mar":
                        Piscifactoria pm = UsarRecompensa.readPiscifactoria(false);
                        if (pm != null) {
                            piscifactorias.add(pm);
                        }
                        break;

                    case "Almacen central":
                        if (almacenCentral == null) {
                            if (UsarRecompensa.readAlmacenCentral()) {
                                almacenCentral = new AlmacenCentral();
                            }
                        } else {
                            System.out.println("\nYa dispones de un Almacen Central.");
                        }
                        break;
                    
                    default:
                        System.out.println("\nOpción no válida.");
                }
            }
        } while (opcion != -1);
    }

    /** Genera diversas recompensas. */
    private void generarRecompensas() {
        CrearRecompensa.createAlgasReward(1);
        CrearRecompensa.createPiensoReward(1);
        CrearRecompensa.createComidaReward(1);

        CrearRecompensa.createMonedasReward(1);

        CrearRecompensa.createTanqueReward(1, "A");
        CrearRecompensa.createTanqueReward(2, "A");

        CrearRecompensa.createPiscifactoriaReward(1, "A");
        CrearRecompensa.createPiscifactoriaReward(1, "B");
        CrearRecompensa.createPiscifactoriaReward(2, "A");
        CrearRecompensa.createPiscifactoriaReward(2, "B");

        CrearRecompensa.createAlmacenReward("A");
        CrearRecompensa.createAlmacenReward("B");
        CrearRecompensa.createAlmacenReward("C");
        CrearRecompensa.createAlmacenReward("D");
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
                                            new Dorada(sexo),
                                            new SalmonAtlantico(sexo),
                                            new ArenqueDelAtlantico(sexo),
                                            new CarpaPlateada(sexo),
                                            new Pejerrey(sexo),
                                            new PercaEuropea(sexo),
                                            new SalmonChinook(sexo),
                                            new TilapiaDelNilo(sexo)
                                    }
                                    : new Pez[] {
                                            new Dorada(sexo),
                                            new SalmonAtlantico(sexo),
                                            new TruchaArcoiris(sexo),
                                            new ArenqueDelAtlantico(sexo),
                                            new Besugo(sexo),
                                            new LenguadoEuropeo(sexo),
                                            new LubinaRayada(sexo),
                                            new Robalo(sexo)
                                    })[random.nextInt(7)];
                    tanqueSeleccionado.getPeces().add(pezSeleccionado);
                }
                System.out.println("\nSe han añadido 4 " + pezSeleccionado.getNombre() + " al tanque "
                        + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría "
                        + piscifactoriaSeleccionada.getNombre() + ".");

                logger.log(nombreEntidad, "Añadidos peces mediante la opción oculta a la piscifactoría " + piscifactoriaSeleccionada.getNombre() + ".");
            }
        }
    }

    /**
     * Distribuye una cantidad de comida entre las piscifactorias según el tipo de comida.
     * 
     * @param cantidadComida La cantidad de comida a distribuir.
     * @param tipo El tipo de comida a distribuir ("algae", "animal", "general").
     */
    public static void distribuirComida(int cantidadComida, String tipo) {
     
        switch (tipo) {
            case "algae":
                int necesitanComidaVegetal = 0;

                do {
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            necesitanComidaVegetal++;
                        }
                    }
                    int comidaVegetalPorPiscifactoria = (necesitanComidaVegetal > 0) ? cantidadComida / necesitanComidaVegetal : 0;
        
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            int espacioDisponibleVegetal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaVegetalActual();
                            int comidaMaxima = Math.min(espacioDisponibleVegetal, comidaVegetalPorPiscifactoria);
                            piscifactoria.añadirComidaVegetal(comidaMaxima);
                            cantidadComida -= comidaMaxima;
                            necesitanComidaVegetal--;
                        }
                    }
                } while (necesitanComidaVegetal != 0);
                break;

            case "animal":
                int necesitanComidaAnimal = 0;

                do {
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            necesitanComidaAnimal++;
                        }
                    }
                    int comidaAnimalPorPiscifactoria = (necesitanComidaAnimal > 0) ? cantidadComida / necesitanComidaAnimal : 0;
        
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            int espacioDisponibleAnimal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaAnimalActual();
                            int comidaMaxima = Math.min(espacioDisponibleAnimal, comidaAnimalPorPiscifactoria);
                            piscifactoria.añadirComidaAnimal(comidaMaxima);
                            cantidadComida -= comidaMaxima;
                            necesitanComidaAnimal--;
                        }
                    }
                } while (necesitanComidaAnimal != 0);
                break;
    
            case "general":
                necesitanComidaAnimal = 0;
                necesitanComidaVegetal = 0;

                do {
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            necesitanComidaAnimal++;
                        }
                        if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            necesitanComidaVegetal++;
                        }
                    }
        
                    int comidaAnimalPorPiscifactoria = (necesitanComidaAnimal > 0) ? (cantidadComida / 2) / necesitanComidaAnimal : 0;
                    int comidaVegetalPorPiscifactoria = (necesitanComidaVegetal > 0) ? (cantidadComida / 2) / necesitanComidaVegetal : 0;
        
                    for (Piscifactoria piscifactoria : piscifactorias) {
                        if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            int espacioDisponibleAnimal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaAnimalActual();
                            int comidaMaxima = Math.min(espacioDisponibleAnimal, comidaAnimalPorPiscifactoria);
                            piscifactoria.añadirComidaAnimal(comidaMaxima);
                            cantidadComida -= comidaMaxima;
                            necesitanComidaAnimal--;
                        }
        
                        if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                            int espacioDisponibleVegetal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaVegetalActual();
                            int comidaMaxima = Math.min(espacioDisponibleVegetal, comidaVegetalPorPiscifactoria);
                            piscifactoria.añadirComidaVegetal(comidaMaxima);
                            cantidadComida -= comidaMaxima;
                            necesitanComidaVegetal--;
                        }
                    }
                } while (necesitanComidaAnimal != 0 && necesitanComidaVegetal != 0);
                break;
        }
    }

    /**
     * Método principal que gestiona el flujo del simulador, 
     * mostrando el menú y procesando las opciones del usuario.
     * El ciclo continúa hasta que el usuario decide salir.
     *
     * @param args Argumentos de línea de comandos, no utilizados.
     */
    public static void main(String[] args) {
        try {
            Simulador simulador = new Simulador();
            simulador.init();
    
            boolean running = true;
            while (running) {
                simulador.menu();
                int option = InputHelper.readInt("Ingrese su opción: ");
    
                switch (option) {
                    case 1: simulador.showGeneralStatus(); break;
                    case 2: simulador.showSpecificStatus(); break;
                    case 3: simulador.showTankStatus(); break;
                    case 4: simulador.showStats(); break;
                    case 5: simulador.showIctio(); break;
                    case 6: 
                        simulador.nextDay();
                        if (almacenCentral != null) {
                            almacenCentral.distribuirComida(Simulador.piscifactorias);
                        }
                        GestorEstado.guardarEstado(simulador);
                        break;
                    case 7: 
                        simulador.addFood();
                        if (almacenCentral != null) {
                            almacenCentral.distribuirComida(Simulador.piscifactorias);
                        }
                        break;
                    case 8: simulador.addFish(); break;
                    case 9: simulador.sell(); break;
                    case 10: simulador.cleanTank(); break;
                    case 11: simulador.emptyTank(); break;
                    case 12: simulador.upgrade(); break;
                    case 13: simulador.recompensas(); break;
                    case 14:
                        int dias = InputHelper.readInt("\nIngrese los días para avanzar en el simulador: ");
                        simulador.nextDay(dias);
                        break;
                    case 97: simulador.generarRecompensas(); break;
                    case 98: simulador.pecesRandom(); break;
                    case 99:
                        Simulador.monedas.ganarMonedas(1000);
                        System.out.println("\nAñadidas 1000 monedas mediante la opción oculta. Monedas actuales, " + monedas.getMonedas());
                        Simulador.logger.log(nombreEntidad, "Añadidas monedas mediante la opción oculta.");
                        break;
                    case 15: 
                        running = false;
                        logger.log(nombreEntidad, "Cierre de la partida");
                        GestorEstado.guardarEstado(simulador);
                        System.out.println("\nSaliendo del simulador.");
                        break;
                    default:
                        System.out.println("\nEntrada no válida. Por favor, ingrese un número entero.");
                }
            }
        } catch (Exception e) {
            Simulador.logger.logError("Error en el Main: " + e.getMessage());
        } finally {
            InputHelper.close();
            Simulador.logger.close();
        }
    }

    /**
     * Obtiene el día actual del simulador.
     *
     * @return el día actual.
     */
    public int getDia() {
        return dia;
    }

    /**
     * Establece el día actual del simulador.
     *
     * @param dia el nuevo día a establecer.
     */
    public void setDia(int dia) {
        this.dia = dia;
    }

    /**
     * Obtiene la lista de piscifactorías asociadas al simulador.
     *
     * @return una lista de objetos de tipo Piscifactoria.
     */
    public List<Piscifactoria> getPiscifactorias() {
        return piscifactorias;
    }

    /**
     * Obtiene el nombre de la entidad del simulador.
     *
     * @return el nombre de la entidad.
     */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /**
     * Establece el nombre de la entidad del simulador.
     *
     * @param nombreEntidad el nuevo nombre de la entidad.
     */
    public void setNombreEntidad(String nombreEntidad) {
        Simulador.nombreEntidad = nombreEntidad;
    }

    /**
     * Obtiene los nombres de los peces implementados en el simulador.
     *
     * @return un arreglo de cadenas con los nombres de los peces implementados.
     */
    public String[] getPecesImplementados() {
        return pecesImplementados;
    }

    /**
     * Establece las estadísticas del simulador.
     *
     * @param estadisticas el objeto de estadísticas a establecer.
     */
    public void setEstadisticas(Estadisticas estadisticas) {
        Simulador.estadisticas = estadisticas;
    }
}