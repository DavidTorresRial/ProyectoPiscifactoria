package commons;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import estadisticas.Estadisticas;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.Pez;
import peces.tipos.doble.SalmonAtlantico;
import peces.tipos.doble.TruchaArcoiris;
import peces.tipos.mar.ArenqueDelAtlantico;
import peces.tipos.mar.Besugo;
import peces.tipos.mar.LenguadoEuropeo;
import peces.tipos.mar.LubinaRayada;
import peces.tipos.mar.Robalo;
import peces.tipos.rio.CarpaPlateada;
import peces.tipos.rio.Pejerrey;
import peces.tipos.rio.PercaEuropea;
import peces.tipos.rio.SalmonChinook;
import peces.tipos.rio.TilapiaDelNilo;
import piscifactoria.Piscifactoria;
import propiedades.AlmacenPropiedades;
import propiedades.PecesProps;
import tanque.Tanque;

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
    public static SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Almacén central de comida para abastecer las piscifactorías. */
    public static AlmacenCentral almacenCentral;

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        nombreEntidad = InputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        nombrePiscifactoria = InputHelper.readString("\nIngrese el nombre de la primera Piscifactoria: ");

        piscifactorias.add(new Piscifactoria(nombrePiscifactoria, true));
        piscifactorias.get(0).añadirComidaAnimal(piscifactorias.get(0).getCapacidadMaximaComida());
        piscifactorias.get(0).añadirComidaVegetal(piscifactorias.get(0).getCapacidadMaximaComida());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
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
            MenuHelper.mostrarMenuCancelar(opcionesMenu);

            int seleccion = InputHelper.solicitarNumero(0, tanques.size()) - 1;

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
        System.out.println("\n============================= " + nombreEntidad + " =============================");
        System.out.println("Día actual: " + dias);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifactorias) {
            piscifactoria.showStatus();
        }
        System.out.println("\n============================================================");

        if (almacenCentral != null) {
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("No hay Almacén Central disponible.");
        }
        System.out.println("============================================================");
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

    /**
     * Muestra el estado de los peces en un tanque seleccionado de una
     * piscifactoría.
     */
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
        System.out.println("============================================================");
    }

    /** Muestra el estado de un pez seleccionado por el usuario. */
    public void showIctio() {
        System.out.println("\n======================== Ictiopedia ========================");
        MenuHelper.mostrarMenuCancelar(new String[] {
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

        int opcion = InputHelper.solicitarNumero(0, 12);
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
                System.out.println("============================================================");
                break;
            default:
                System.out.println("\nSelección inválida. Por favor, intente de nuevo.");
        }

        if (opcion != 0) {
            System.out.println("\nNombre: " + pezSeleccionado.getDatos().getNombre());
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
            System.out.println("============================================================");
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
                                            System.out.println("\n" + cantidadComida + " de comida de tipo Animal comprada por " + costo + " monedas. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
                                        } else {
                                            piscifactoria.añadirComidaVegetal(cantidadComida);
                                            comidaActual += cantidadComida;
                                            System.out.println("\n" + cantidadComida + " de comida de tipo Vegetal comprada por " + costo + " monedas. Se almacena en la piscifactoría " + piscifactoria.getNombre() + ".");
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
                                        System.out.println("\n" + cantidadComida + " de comida de tipo Animal comprada por " + costo + " monedas. Se almacena en el almacén central.\r");
                                    } else {
                                        almacenCentral.añadirComidaVegetal(cantidadComida);
                                        comidaActual += cantidadComida;
                                        System.out.println("\n" + cantidadComida + " de comida de tipo Vegetal comprada por "+ costo + " monedas. Se almacena en el almacén central.\r");
                                    }
                                }
                            } else {
                                System.out.println("\nNo hay suficiente espacio para añadir esa cantidad de comida.");
                            }
                        }
                    } while (opcionCantidad != 0);
                }
            } while (opcionComida != 0);
            System.out.println("\nOperación cancelada.");
        }
    }

    /** Añade un pez al tanque seleccionado. */
    public void addFish() {
        var selectTank = selectTank();
        Tanque tanqueSeleccionado = selectTank.getValue();

        if (tanqueSeleccionado != null) {
            int opcion = -1;
            while (opcion != 0) {
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
                MenuHelper.mostrarMenuCancelar(opcionesPeces);

                opcion = InputHelper.solicitarNumero(0, opcionesPeces.length);

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
                    System.out.println("\n" + pezSeleccionado.getNombre() + (pezSeleccionado.isSexo() ? " (M)" : " (H)")
                            + " comprado por " + pezSeleccionado.getDatos().getCoste() + " monedas. Añadido al tanque "
                            + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría "
                            + selectTank.getKey().getNombre() + ".");
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
        }
    }

    /** Vacía el tanque seleccionado eliminando todos los peces. */
    public void emptyTank() {
        var selectTank = selectTank();
        Tanque tanque = selectTank.getValue();
        if (tanque != null) {
            tanque.emptyTank();
            System.out.println("\nVaciado el tanque " + tanque.getNumeroTanque() + " de la piscifactoría " + selectTank.getKey().getNombre() + ".");
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
            if (p.esDeRio()) {
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
            if (!p.esDeRio()) {
                contador++;
            }
        }
        return contador;
    }
    
    /**
     * Método que muestra el menú de mejoras para el usuario y permite seleccionar y
     * realizar mejoras en los edificios.
     */ // TODO preguntar si la opcion de salir es cero o tres
    public void upgrade() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=================== Gestión de Edificios ===================");
            String[] opcionesMenuPrincipal = { "Comprar edificios", "Mejorar edificios" };
            MenuHelper.mostrarMenuCancelar(opcionesMenuPrincipal);

            int opcionPrincipal = InputHelper.solicitarNumero(0, opcionesMenuPrincipal.length);

            switch (opcionPrincipal) {
                case 1: // Comprar edificios
                    gestionarCompraEdificios();
                    break;
                case 2: // Mejorar edificios
                    gestionarMejoraEdificios();
                    break;
                case 0: // Cancelar
                    System.out.println("\nOperación cancelada.");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private void gestionarCompraEdificios() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===================== Comprar Edificio =====================");
            String[] opcionesCompra = (almacenCentral == null) ? new String[]{"Piscifactoría", "Almacén central"} : new String[]{"Piscifactoría"};
            MenuHelper.mostrarMenuCancelar(opcionesCompra);

            int opcionCompra = InputHelper.solicitarNumero(0, opcionesCompra.length);

            switch (opcionCompra) {
                case 1: // Piscifactoría
                    System.out.println("\nComprar Piscifactoria."); // TODO
                    break;
                case 2: // Almacén central
                    if (monedas.gastarMonedas(2000)) {
                        almacenCentral = new AlmacenCentral();
                        System.out.println("\nAlmacén central construido.");
                    } else {
                        System.out.println("\nNecesitas 2000 monedas para construir el almacén central.");
                    }
                    break;
                case 0: // Cancelar
                    System.out.println("\nOperación cancelada."); 
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private void gestionarMejoraEdificios() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===================== Mejorar Edificio =====================");
            String[] opcionesMejora = (almacenCentral != null) ? new String[]{"Piscifactoría", "Almacén central"} : new String[]{"Piscifactoría"};

            MenuHelper.mostrarMenuCancelar(opcionesMejora);

            int opcionMejora = InputHelper.solicitarNumero(0, opcionesMejora.length);

            switch (opcionMejora) {
                case 1: // Mejorar Piscifactoría
                    System.out.println("\nMejorar Piscifactoría."); // TODO
                    break;
                case 2: // Mejorar Almacén central
                    almacenCentral.aumentarCapacidad();
                    break;
                case 0: // Cancelar
                    System.out.println("\nOperación cancelada.");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
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

            System.out.println();
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
                    int dias = InputHelper.readInt("Ingrese los dias para avanzar en el simulador: ");
                    for (int i = 0; i < dias; i++) {
                    simulador.nextDay();
                    }
                    break;
                case 98:
                    simulador.pecesRandom();
                    break;
                case 99:
                    Simulador.monedas.ganarMonedas(1000);
                    System.out.println("\nAñadidas 1000 monedas mediante la opción oculta. Monedas actuales, " + monedas.getMonedas());
                    break;
                case 14:
                    running = false;
                    System.out.println("\nSaliendo del simulador.");
                    break;
                default:
                    System.out.println("\nOpción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        InputHelper.close();
    }

    public void pecesRandom() {
        Piscifactoria piscifactoriaSeleccionada = selectPisc();
    
        while (piscifactoriaSeleccionada != null) {
            List<Tanque> tanques = piscifactoriaSeleccionada.getTanques();
    
            if (tanques.isEmpty()) {
                System.out.println("\nLa piscifactoría no tiene tanques disponibles. Selecciona otra piscifactoría.");
                piscifactoriaSeleccionada = selectPisc();
            } else {
                // Seleccionar un tanque aleatorio
                Random random = new Random();
                Tanque tanqueSeleccionado = tanques.get(random.nextInt(tanques.size()));
    
                // Determinar si la piscifactoría es de río
                boolean esDeRio = piscifactoriaSeleccionada.esDeRio();
    
                // Determinar el tipo de pez permitido en el tanque
                Class<?> tipoPezActual = tanqueSeleccionado.getTipoPezActual();
                String tipoPezSeleccionado = (tipoPezActual != null)
                        ? tipoPezActual.getSimpleName()
                        : (esDeRio
                                ? new String[]{"SalmonAtlantico", "TruchaArcoiris", "CarpaPlateada", "Pejerrey", "PercaEuropea", "SalmonChinook", "TilapiaDelNilo"}
                                : new String[]{"SalmonAtlantico", "TruchaArcoiris", "ArenqueDelAtlantico", "Besugo", "LenguadoEuropeo", "LubinaRayada", "Robalo"}
                          )[random.nextInt(7)];
    
                boolean tipoCompatible = true;
                boolean espacioDisponible = true;
    
                // Agregar 4 peces del mismo tipo al tanque seleccionado.
                for (int i = 0; i < 4 && tipoCompatible && espacioDisponible; i++) {
                    boolean sexo = tanqueSeleccionado.getHembras() <= tanqueSeleccionado.getMachos();
                    Pez pezSeleccionado = switch (tipoPezSeleccionado) {
                        case "SalmonAtlantico" -> new SalmonAtlantico(sexo);
                        case "TruchaArcoiris" -> new TruchaArcoiris(sexo);
                        case "CarpaPlateada" -> esDeRio ? new CarpaPlateada(sexo) : new ArenqueDelAtlantico(sexo);
                        case "Pejerrey" -> esDeRio ? new Pejerrey(sexo) : new Besugo(sexo);
                        case "PercaEuropea" -> esDeRio ? new PercaEuropea(sexo) : new LenguadoEuropeo(sexo);
                        case "SalmonChinook" -> esDeRio ? new SalmonChinook(sexo) : new LubinaRayada(sexo);
                        case "TilapiaDelNilo" -> esDeRio ? new TilapiaDelNilo(sexo) : new Robalo(sexo);
                        case "ArenqueDelAtlantico" -> new ArenqueDelAtlantico(sexo);
                        case "Besugo" -> new Besugo(sexo);
                        case "LenguadoEuropeo" -> new LenguadoEuropeo(sexo);
                        case "LubinaRayada" -> new LubinaRayada(sexo);
                        case "Robalo" -> new Robalo(sexo);
                        default -> null;
                    };
    
                    if (pezSeleccionado == null) {
                        System.out.println("\nTipo de pez desconocido: " + tipoPezSeleccionado);
                        tipoCompatible = false;
                    } else if (tipoPezActual != null && !tipoPezActual.equals(pezSeleccionado.getClass())) {
                        System.out.println("\nEl tanque ya tiene un tipo de pez diferente: " + tipoPezActual.getSimpleName());
                        tipoCompatible = false;
                    } else if (tanqueSeleccionado.getPeces().size() >= tanqueSeleccionado.getCapacidad()) {
                        System.out.println("\nNo hay espacio suficiente en el tanque seleccionado para más peces.");
                        espacioDisponible = false;
                    } else {
                        tanqueSeleccionado.getPeces().add(pezSeleccionado);
                        if (tipoPezActual == null) {
                            tanqueSeleccionado.setTipoPezActual(pezSeleccionado.getClass());
                        }
                    }
                }
    
                if (tipoCompatible && espacioDisponible) {
                    System.out.println("\nSe han agregado 4 peces de tipo " + tipoPezSeleccionado + " al tanque " + tanqueSeleccionado.getNumeroTanque() + " de la piscifactoría " + piscifactoriaSeleccionada.getNombre() + ".");
                }
                piscifactoriaSeleccionada = null; // Salimos del bucle.
            }
        }
    }
    
    
    
    
    


}