package commons;

import java.util.ArrayList;
import java.util.List;

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


public class Simulador {

    /** Días transcurridos en la simulación, comenzando en 0. */
    private int dias = 0;

    /** Lista de piscifactorías en el sistema. */
    private ArrayList<Piscifactoria> piscifacorias = new ArrayList<>();

    /** Nombre de la entidad o partida en la simulación. */
    private String nombreEntidad;

    /** Nombre de la piscifactoría. */
    private String nombrePiscifactoria;

    /** Sistema de estadísticas para registrar la cría, venta y ganancias de los peces. */
    private Estadisticas estadisticas = new Estadisticas(new String[]{
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
    private SistemaMonedas monedas;

    /** Almacén central de comida para abastecer las piscifactorías. */
    private AlmacenCentral almacenCentral;

    /** Utilidad para manejar la entrada de datos del usuario. */
    private InputHelper inputHelper;

    /** Utilidad para gestionar los menús de la simulación. */
    private MenuHelper menuHelper;

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        inputHelper = new InputHelper();
        menuHelper = new MenuHelper();

        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();
        nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");

        monedas = new SistemaMonedas(100);

        // Añadimos la nueva PiscifactoriaDeRio directamente a la lista
        piscifacorias.add(new PiscifactoriaDeRio(nombrePiscifactoria, monedas));
        piscifacorias.get(0).setComidaAnimalActual(piscifacorias.get(0).getCapacidadTotal());
        piscifacorias.get(0).setComidaVegetalActual(piscifacorias.get(0).getCapacidadTotal());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        menuHelper.clearOptions(); // Limpiar opciones previas

        menuHelper.addOption(1, "Estado general");
        menuHelper.addOption(2, "Estado piscifactoría");
        menuHelper.addOption(3, "Estado tanque");
        menuHelper.addOption(4, "Informes");
        menuHelper.addOption(5, "Icitopedia");
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
        menuHelper.clearOptions(); // Limpiar opciones previas

        System.out.println("\nSeleccione una opción:");
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");
        System.out.println();

        for (int i = 0; i < piscifacorias.size(); i++) {
            Piscifactoria piscifactoria = piscifacorias.get(i);
            String optionText = piscifactoria.getNombre() + " [" + piscifactoria.getTotalVivos() + "/"
                    + piscifactoria.getTotalPeces() + "/" + piscifactoria.getCapacidadTotal() + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        menuHelper.addOption(0, "Cancelar");

        menuHelper.showMenu();
    }

    /**
     * Muestra el menú de piscifactorías y permite al usuario seleccionar una de
     * ellas.
     * Devuelve la opción seleccionada o repite la solicitud si la selección es
     * inválida.
     *
     * @return La opción seleccionada por el usuario (índice de la piscifactoría) o
     *         0 para cancelar.
     */
    public int selectPisc() {
        menuPisc();
        int selection = inputHelper.readInt("Ingrese su selección: ");
        if (selection < 0 || selection > piscifacorias.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectPisc(); // Llamada recursiva hasta que se haga una selección válida
        }
        return selection;
    }

    /**
     * Método que muestra el menú de tanques de una piscifactoría y permite
     * seleccionar uno de ellos, devolviendo la opción seleccionada.
     * 
     * @param piscifactoria La piscifactoría que contiene los tanques a seleccionar.
     * @return La opción seleccionada por el usuario (índice del tanque) o 0 para
     *         cancelar.
     */
    public int selectTank(Piscifactoria piscifactoria) {
        menuHelper.clearOptions(); // Limpiar opciones previas

        System.out.println("\nSeleccione un Tanque:");
        List<Tanque> tanques = piscifactoria.getTanques();
        for (int i = 0; i < tanques.size(); i++) {
            Tanque tanque = tanques.get(i);
            // Verifica si el tipo de pez actual es nulo
            String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName()
                    : "Tanque sin tipo de pez asignado";
            String optionText = "Tanque " + (i + 1) + " [Tipo de pez: " + tipoPez + "]";
            menuHelper.addOption(i + 1, optionText);
        }

        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();

        int selection = inputHelper.readInt("Ingrese su selección: ");
        System.out.println();
        if (selection < 0 || selection > tanques.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectTank(piscifactoria); // Llamada recursiva hasta que se haga una selección válida
        }
        return selection;
    }

    /** Método que muestra el estado de las piscifactorías. */
    public void showGeneralStatus() {
        System.out.println("\n=================== " + nombreEntidad + " ===================");
        System.out.println("Día actual: " + dias);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.showStatus();
        }

        if (almacenCentral != null) {
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("\nNo hay Almacén Central disponible.");
        }
        System.out.println("======================================================");
    }

    /** Método que muestra el estado de una piscifactoría. */
    public void showSpecificStatus() {
        int selection = selectPisc();

        if (selection == 0) {
            System.out.println("Operación cancelada.");
            return;

        } else if (selection > 0 && selection <= piscifacorias.size()) {
            Piscifactoria piscifactoria = piscifacorias.get(selection - 1);
            piscifactoria.showTankStatus();
        } else {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
        }
    }

    public void showTankStatus() {
        int selection = selectPisc();

        if (selection == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Validar selección de piscifactoría
        if (selection < 1 || selection > piscifacorias.size()) {
            System.out.println("Selección de piscifactoría no válida. Inténtalo de nuevo.");
            return;
        }

        // Obtiene la piscifactoría seleccionada
        Piscifactoria piscifactoria = piscifacorias.get(selection - 1);

        // Paso 2: Selecciona un tanque dentro de la piscifactoría
        int tanqueSeleccionado = selectTank(piscifactoria);

        // Cancelar si se selecciona 0
        if (tanqueSeleccionado == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Validar selección de tanque
        if (tanqueSeleccionado < 1 || tanqueSeleccionado > piscifactoria.getTanques().size()) {
            System.out.println("Selección de tanque no válida. Inténtalo de nuevo.");
            return;
        }

        // Paso 3: Muestra el estado de todos los peces en el tanque seleccionado
        Tanque tanque = piscifactoria.getTanques().get(tanqueSeleccionado - 1);
        System.out.println("\nEstado de los peces en el tanque " + tanque.getNumeroTanque() + ":");

        // Verificar si hay peces en el tanque
        if (tanque.getPeces().isEmpty()) {
            System.out.println("Este tanque no contiene peces actualmente.");
        } else {
            for (int i = 0; i < tanque.getPeces().size(); i++) {
                tanque.getPeces().get(i).showStatus();
            }
        }
    }

    /**
     * Muestra un desglose de las estadísticas por cada tipo de pez.
     */
    public void showStats() {
        System.out.println("\n=============== Estadísticas ===============");
        estadisticas.mostrar();
        System.out.println("===========================================");
    }


    public void showIctio() {
    menuHelper.clearOptions(); // Limpiar opciones previas

    // Array de los peces específicos
    PecesDatos[] pecesEspecificos = {
        AlmacenPropiedades.SALMON_ATLANTICO,
        AlmacenPropiedades.TRUCHA_ARCOIRIS,
        AlmacenPropiedades.ARENQUE_ATLANTICO,
        AlmacenPropiedades.BESUGO,
        AlmacenPropiedades.LENGUADO_EUROPEO,
        AlmacenPropiedades.LUBINA_EUROPEA,
        AlmacenPropiedades.ROBALO,
        AlmacenPropiedades.CARPA_PLATEADA,
        AlmacenPropiedades.PEJERREY,
        AlmacenPropiedades.PERCA_EUROPEA,
        AlmacenPropiedades.SALMON_CHINOOK,
        AlmacenPropiedades.TILAPIA_NILO
    };

    // Agregar opciones de peces al menú
    for (int i = 0; i < pecesEspecificos.length; i++) {
        PecesDatos pez = pecesEspecificos[i];
        menuHelper.addOption(i + 1, pez.getNombre());
    }

    // Opción para cancelar
    menuHelper.addOption(0, "Cancelar");

    // Mostrar el menú
    menuHelper.showMenu();

    // Leer la selección del usuario
    int seleccion = inputHelper.readInt("Seleccione un pez para más información: ");

    // Verificar la selección
    if (seleccion == 0) {
        System.out.println("Operación cancelada.");
        return;
    } else if (seleccion < 1 || seleccion > pecesEspecificos.length) {
        System.out.println("Selección no válida. Inténtalo de nuevo.");
        showIctio(); // Mostrar menú de nuevo si la selección es inválida
        return;
    }

    // Obtener el pez seleccionado
    PecesDatos pezSeleccionado = pecesEspecificos[seleccion - 1];

    // Mostrar la información del pez seleccionado
    if (pezSeleccionado != null) {
        System.out.println("Información del pez seleccionado:");
        System.out.println("Nombre: " + pezSeleccionado.getNombre());
        System.out.println("Nombre científico: " + pezSeleccionado.getCientifico());
        System.out.println("Tipo: " + pezSeleccionado.getTipo());
        System.out.println("Coste: " + pezSeleccionado.getCoste() + " unidades");
        System.out.println("Monedas: " + pezSeleccionado.getMonedas());
        System.out.println("Huevos: " + pezSeleccionado.getHuevos());
        System.out.println("Ciclo: " + pezSeleccionado.getCiclo() + " días");
        System.out.println("Madurez: " + pezSeleccionado.getMadurez() + " días");
        System.out.println("Óptimo: " + pezSeleccionado.getOptimo() + " unidades");
        
        // Mostrar propiedades
        PecesProps[] propiedades = pezSeleccionado.getPropiedades();
        System.out.print("Propiedades: ");
        for (int i = 0; i < propiedades.length; i++) {
            System.out.print(propiedades[i]);
            if (i < propiedades.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        System.out.println("Tipo de cría: " + pezSeleccionado.getPiscifactoria());
    } else {
        System.out.println("No se encontró información para el pez seleccionado.");
    }

    // Volver a mostrar el menú de ictiología
    showIctio();
}






















    public void nextDay() {
        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;

        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.nextDay(); // Llama al método nextDay de cada piscifactoría

            // TODO implementar logica para que vaya sumando los peces vendidos y las
            // monedas ganadas a un total
        }

        // Muestra los resultados totales
        System.out.println(totalPecesVendidos + " peces vendidos por un total de " + totalMonedasGanadas + " monedas.");
    }

    /**
     * public void upgrade() {
     * 
     * while (true) {
     * // Configuramos el menú principal
     * menuHelper.clearOptions();
     * menuHelper.addOption(1, "Comprar edificios");
     * menuHelper.addOption(2, "Mejorar edificios");
     * menuHelper.addOption(3, "Cancelar");
     * 
     * // Mostramos el menú y obtenemos la opción seleccionada
     * menuHelper.showMenu();
     * int opcion = menuHelper.getSelection(); //TODO usar input helper
     * 
     * 
     * switch (opcion) {
     * case 1:
     * // Comprar edificios
     * menuHelper.clearOptions();
     * menuHelper.addOption(1, "Piscifactoría");
     * menuHelper.addOption(2, "Almacén central");
     * 
     * 
     * 
     * break;
     * 
     * case 2:
     * // Mejorar edificios
     * menuHelper.clearOptions();
     * menuHelper.addOption(1, "Piscifactoría");
     * menuHelper.addOption(2, "Almacén central");
     * 
     * menuHelper.showMenu();
     * int edificioAMejorar = menuHelper.getSelection();
     * 
     * if (edificioAMejorar == 1) {
     * // Mejorar piscifactoría
     * menuHelper.clearOptions();
     * menuHelper.addOption(1, "Comprar tanque");
     * menuHelper.addOption(2, "Aumentar almacén de comida");
     * 
     * 
     * 
     * } else if (edificioAMejorar == 2) {
     * // Mejorar almacén central (si se tiene)
     * 
     * 
     * 
     * } else {
     * System.out.println("Opción no válida. Por favor, intenta de nuevo.");
     * }
     * break;
     * 
     * case 3:
     * // Cancelar
     * System.out.println("Operación cancelada.");
     * return; // Salir del método
     * 
     * default:
     * System.out.println("Opción no válida. Por favor, intenta de nuevo.");
     * break;
     * }
     * }
     * }
     */

    public static void main(String[] args) {
        InputHelper inputHelper = new InputHelper(); // Crear una instancia de InputHelper
        Simulador simulador = new Simulador();

        simulador.init(); // Inicializa la simulación

        boolean running = true; // Controla el bucle principal
        while (running) {
            System.out.println();
            simulador.menu(); // Muestra el menú de opciones

            // Usar InputHelper para leer la selección del usuario
            int option = inputHelper.readInt("Ingrese su opción: ");

            switch (option) {
                case 1:
                    simulador.showGeneralStatus(); // Muestra el estado general
                    break;
                case 2:
                    simulador.showSpecificStatus(); // Muestra el estado de la piscifactoría seleccionada
                    break;
                case 3:
                    simulador.showTankStatus(); // Muestra el estado de un tanque específico
                    break;
                case 4:
                    simulador.showStats();
                    break;
                case 5:
                    simulador.showIctio();
                    break;
                case 6:
                    simulador.nextDay(); // Lógica para pasar al siguiente día
                    break;
                case 98:
                    // Lógica para agregar peces gratuitos a una piscifactoría
                    break;
                case 99:
                    simulador.getMonedas().ganarMonedas(1000);
                    System.out.println("\nSe han añadido 1000 monedas para pruebas.");
                    break;
                case 14: // Opción para salir
                    running = false; // Termina el bucle
                    System.out.println("\nSaliendo del simulador.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
    }

    /**
     * @return int return the dias
     */
    public int getDias() {
        return dias;
    }

    /**
     * @param dias the dias to set
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
     * @return List<Piscifactoria> return the piscifacorias
     */
    public List<Piscifactoria> getPiscifacorias() {
        return piscifacorias;
    }

    /**
     * @param piscifacorias the piscifacorias to set
     */
    public void setPiscifacorias(ArrayList<Piscifactoria> piscifacorias) {
        this.piscifacorias = piscifacorias;
    }

    /**
     * @return SistemaMonedas return the monedas
     */
    public SistemaMonedas getMonedas() {
        return monedas;
    }

    /**
     * @param monedas the monedas to set
     */
    public void setMonedas(SistemaMonedas monedas) {
        this.monedas = monedas;
    }

    /**
     * @return InputHelper return the inputHelper
     */
    public InputHelper getInputHelper() {
        return inputHelper;
    }

    /**
     * @param inputHelper the inputHelper to set
     */
    public void setInputHelper(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
    }
}