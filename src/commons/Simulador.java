package commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

    /** Días transcurridos en la simulación, comenzando en 0 */
    private int dias = 0;

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
    private InputHelper inputHelper = InputHelper.getInstance();

    /** Utilidad para gestionar los menús de la simulación */
    private MenuHelper menuHelper = new MenuHelper();

    /** Metodo que inicializa todo el sistema */
    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();
        nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");

        piscifactorias.add(new Piscifactoria(nombrePiscifactoria, true));
        piscifactorias.add(new Piscifactoria("Piscifactoria 2", false));
        piscifactorias.add(new Piscifactoria("Piscifactoria 3", true));
        piscifactorias.get(0).setComidaAnimalActual(piscifactorias.get(0).getCapacidadMaximaComidaPiscifactoria());
        piscifactorias.get(0).setComidaVegetalActual(piscifactorias.get(0).getCapacidadMaximaComidaPiscifactoria());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        menuHelper.mostrarMenuSinCancelar(new String[] {
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
        System.out.println();

        String[] opciones = new String[piscifactorias.size()];

        for (int i = 0; i < piscifactorias.size(); i++) {
            Piscifactoria piscifactoria = piscifactorias.get(i);
            opciones[i] = piscifactoria.getNombre() + " [" + piscifactoria.getTotalVivos() + "/"
                    + piscifactoria.getTotalPeces() + "/" + piscifactoria.getCapacidadTotal() + "]";
        }
        menuHelper.mostrarMenu(opciones);
    }

    /**
     * Muestra la lista de piscifactorías actuales y permite seleccionar una.
     * 
     * @return La piscifactoría seleccionada.
     */
    public int selectPisc() {
        menuPisc();
        return (inputHelper.solicitarNumero(0, piscifactorias.size()) - 1);
    }

    public int selectTank() {
        int piscifactoriaIndex = selectPisc();
        if (piscifactoriaIndex != -1) {
            List<Tanque> tanques = piscifactorias.get(piscifactoriaIndex).getTanques();
            System.out.println("\nSeleccione un Tanque:");

            String[] opcionesTanques = new String[tanques.size()];
            for (int i = 0; i < tanques.size(); i++) {
                Tanque tanque = tanques.get(i);
                String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName()
                        : "Vacío";
                opcionesTanques[i] = "Tanque " + (i + 1) + " [" + tipoPez + "]";
            }
            menuHelper.mostrarMenu(opcionesTanques);
            return inputHelper.solicitarNumero(0, tanques.size());

        } else {
            System.out.println("Operación cancelada.");
            return -1;
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

            // TODO
            //simulador.menuPisc();
            //System.out.println(simulador.selectPisc());
            System.out.println(simulador.selectTank());
            // TODO

            System.out.println();
            simulador.menu();
            int option = inputHelper.readInt("Ingrese su opción: (Del menú principal): ");

            switch (option) {
                case 1:
                    // simulador.showGeneralStatus();
                    break;
                case 2:
                    // simulador.showSpecificStatus();
                    break;
                case 3:
                    // simulador.showTankStatus();
                    break;
                case 4:
                    // simulador.showStats();
                    break;
                case 5:
                    // simulador.showIctio();
                    break;
                case 6:
                    // simulador.nextDay();
                    break;
                case 7:
                    // simulador.addFood();
                    break;
                case 8:
                    // simulador.addFish();
                    break;
                case 9:
                    // simulador.sell();
                    break;
                case 10:
                    // simulador.cleanTank();
                    break;
                case 11:
                    // simulador.emptyTank();
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