package commons;

import piscifactorias.Piscifactoria;
import piscifactorias.tipos.PiscifactoriaDeRio;
import tanque.Tanque;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.Pez;
import peces.tipos.doble.SalmonAtlantico;
import peces.tipos.mar.Besugo;
import peces.tipos.rio.CarpaPlateada;
import peces.tipos.rio.Pejerrey;

import java.util.ArrayList;
import java.util.List;

import estadisticas.Estadisticas;

public class Simulador {

    private int dias;
    private ArrayList<Piscifactoria> piscifacorias;
    private String nombreEntidad;

    private SistemaMonedas monedas;
    private InputHelper inputHelper;
    private MenuHelper menuHelper;
    private AlmacenCentral almacenCentral;

    public Simulador() {
        inputHelper = new InputHelper();
        menuHelper = new MenuHelper();
        piscifacorias = new ArrayList<>();
    }

    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();

        monedas = new SistemaMonedas(100);

        String nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");
        System.out.println();

        // Añadimos la nueva PiscifactoriaDeRio directamente a la lista
        piscifacorias.add(new PiscifactoriaDeRio(nombrePiscifactoria, monedas));
        piscifacorias.get(0).setComidaAnimalActual(piscifacorias.get(0).getCapacidadTotal());
        piscifacorias.get(0).setComidaVegetalActual(piscifacorias.get(0).getCapacidadTotal());

        String nombrePiscifactoria2 = inputHelper.readString("Ingrese el nombre de la segunda Piscifactoria: ");
        System.out.println();
        String nombrePiscifactoria3 = inputHelper.readString("Ingrese el nombre de la tercera Piscifactoria: ");
        System.out.println();

        piscifacorias.add(new PiscifactoriaDeRio(nombrePiscifactoria2, monedas));
        piscifacorias.add(new PiscifactoriaDeRio(nombrePiscifactoria3, monedas));

        piscifacorias.get(0).addPez(new Pejerrey(false));
        piscifacorias.get(0).addPez(new Pejerrey(false));

        piscifacorias.get(1).addPez(new Pejerrey(false));
        piscifacorias.get(1).addPez(new Pejerrey(false));

        piscifacorias.get(1).addPez(new CarpaPlateada(false));
        piscifacorias.get(1).addPez(new SalmonAtlantico(false));
    }

    public void menu() {
        menuHelper.clearOptions(); // Limpiar opciones previas si es necesario

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

    public void menuPisc() {
        menuHelper.clearOptions(); // Limpiar opciones previas si es necesario

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

    public int selectPisc() {
        menuPisc();
        int selection = inputHelper.readInt("Ingrese su selección: ");
        System.out.println();
        if (selection < 0 || selection > piscifacorias.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectPisc(); // Llamada recursiva hasta que se haga una selección válida
        }
        return selection;
    }

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

    public void showSpecificStatus() {
        int selection = selectPisc();

        // Valida la selección
        if (selection == 0) {
            System.out.println("Operación cancelada.");
            return;
        } else if (selection > 0 && selection <= piscifacorias.size()) {
            // Obtiene la piscifactoría seleccionada
            Piscifactoria selectedPiscifactoria = piscifacorias.get(selection - 1);

            // Muestra el estado de todos los tanques de la piscifactoría seleccionada
            selectedPiscifactoria.showStatus();
        } else {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
        }
    }

    public void showTankStatus() {
        // Paso 1: Selecciona una piscifactoría
        int piscifactoriaSeleccionada = selectPisc();

        // Cancelar si se selecciona 0
        if (piscifactoriaSeleccionada == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Validar selección de piscifactoría
        if (piscifactoriaSeleccionada < 1 || piscifactoriaSeleccionada > piscifacorias.size()) {
            System.out.println("Selección de piscifactoría no válida. Inténtalo de nuevo.");
            return;
        }

        // Obtiene la piscifactoría seleccionada
        Piscifactoria piscifactoria = piscifacorias.get(piscifactoriaSeleccionada - 1);

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

    public void showStats(List<Pez> peces) { // TODO revisar
        // Extraer los nombres de los peces de la lista
        String[] nombres = new String[peces.size()];
        for (int i = 0; i < peces.size(); i++) {
            nombres[i] = peces.get(i).getNombre();
        }
    
        // Crear una instancia de Estadisticas con los nombres de los peces
        Estadisticas estadisticas = new Estadisticas(nombres);
    
        // Registrar nacimientos y ventas en la instancia de Estadisticas
        for (Pez pez : peces) {
            // Suponiendo que tienes un método para verificar si el pez ha sido vendido
            if (pez.isVivo()) {  
                estadisticas.registrarNacimiento(pez.getNombre());
            } else {
                // Obtener las monedas ganadas por la venta del pez
                int monedasGanadas = pez.getDatos().getMonedas();
                estadisticas.registrarVenta(pez.getNombre(), monedasGanadas);
            }
        }
    
        // Mostrar las estadísticas
        estadisticas.mostrar();
    }

    public void nextDay() {
        int totalPecesVendidos = 0; 
        int totalMonedasGanadas = 0;

        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.nextDay(); // Llama al método nextDay de cada piscifactoría

            // TODO implementar logica para que vaya sumando los peces vendidos y las monedas ganadas a un total 
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
                    //simulador.showStats();
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




//TODO : Ai que corregir estos errores.
    public void addFood() {
        // Si el almacenCentral está disponible, se omite la selección de piscifactoría
        if (almacenCentral != null) {
            System.out.println("Añadiendo comida al Almacén Central.");
            String tipoComida = selectFoodType(); // Método para seleccionar tipo de comida
            int cantidad = selectFoodAmount(); // Método para seleccionar cantidad
    
            // Calcular el costo
            int totalCost = calculateCost(cantidad);
    
            // Verificar si hay suficientes monedas
            if (monedas.getMonedas() < totalCost) {
                System.out.println("No tienes suficientes monedas para realizar esta compra.");
                return;
            }
    
            // Añadir comida al almacen central
            almacenCentral.agregarComida(TipoComida.valueOf(tipoComida.toUpperCase()), cantidad);
            monedas.gastarMonedas(totalCost);
    
            // Mostrar el estado
            System.out.printf("Añadida %d de comida %s%n", cantidad, tipoComida);
            System.out.printf("Depósito de comida %s del Almacén Central al %.2f%% de su capacidad. [%d/%d]%n",
                    tipoComida,
                    almacenCentral.getComidaActual(TipoComida.valueOf(tipoComida.toUpperCase())) / (float) almacenCentral.getCapacidadTotal(TipoComida.valueOf(tipoComida.toUpperCase())) * 100,
                    almacenCentral.getComidaActual(TipoComida.valueOf(tipoComida.toUpperCase())),
                    almacenCentral.getCapacidadTotal(TipoComida.valueOf(tipoComida.toUpperCase())));
        } else {
            // Seleccionar una piscifactoría
            int piscifactoriaSeleccionada = selectPisc();
            if (piscifactoriaSeleccionada == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            Piscifactoria piscifactoria = piscifacorias.get(piscifactoriaSeleccionada - 1);
    
            // Determinar el tipo de piscifactoría
            String tipoPiscifactoria = piscifactoria.getTipo(); // Suponiendo que este método existe
    
            // Elegir tipo de comida
            String tipoComida = selectFoodType(); // Método para seleccionar tipo de comida
            int cantidad = selectFoodAmount(); // Método para seleccionar cantidad
    
            // Calcular el costo
            int totalCost = calculateCost(cantidad);
    
            // Verificar si hay suficientes monedas
            if (monedas.getMonedas() < totalCost) {
                System.out.println("No tienes suficientes monedas para realizar esta compra.");
                return;
            }
    
            // Añadir comida a la piscifactoría según su tipo
            switch (tipoPiscifactoria) {
                case "RIO":
                    piscifactoria.addComida(TipoComida.valueOf(tipoComida.toUpperCase()), cantidad);
                    break;
                case "MAR":
                    piscifactoria.addComida(TipoComida.valueOf(tipoComida.toUpperCase()), cantidad);
                    break;
                default:
                    System.out.println("Tipo de piscifactoría desconocido.");
                    return;
            }
    
            monedas.gastarMonedas(totalCost);
    
            // Mostrar el estado
            System.out.printf("Añadida %d de comida %s%n", cantidad, tipoComida);
            System.out.printf("Depósito de comida %s de la piscifactoría %s al %.2f%% de su capacidad. [%d/%d]%n",
                    tipoComida,
                    piscifactoria.getNombre(),
                    piscifactoria.getComidaActual(TipoComida.valueOf(tipoComida.toUpperCase())) / (float) piscifactoria.getCapacidadTotal(TipoComida.valueOf(tipoComida.toUpperCase())) * 100,
                    piscifactoria.getComidaActual(TipoComida.valueOf(tipoComida.toUpperCase())),
                    piscifactoria.getCapacidadTotal(TipoComida.valueOf(tipoComida.toUpperCase())));
        }
    }
    
    private String selectFoodType() {
        menuHelper.clearOptions();
        menuHelper.addOption(1, "VEGETAL");
        menuHelper.addOption(2, "ANIMAL");
        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();
        int opcion = inputHelper.readInt("Seleccione el tipo de comida: ");
        switch (opcion) {
            case 1: return "VEGETAL";
            case 2: return "ANIMAL";
            case 0: 
                System.out.println("Operación cancelada.");
                return null;
            default:
                System.out.println("Opción no válida.");
                return selectFoodType(); // Repetir hasta seleccionar una opción válida
        }
    }
    
    private int selectFoodAmount() {
        menuHelper.clearOptions();
        menuHelper.addOption(1, "5");
        menuHelper.addOption(2, "10");
        menuHelper.addOption(3, "25");
        menuHelper.addOption(4, "Llenar");
        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();
        int opcion = inputHelper.readInt("Seleccione la cantidad de comida a añadir: ");
        switch (opcion) {
            case 1: return 5;
            case 2: return 10;
            case 3: return 25;
            case 4: return Integer.MAX_VALUE; // Llenar
            case 0:
                System.out.println("Operación cancelada.");
                return 0;
            default:
                System.out.println("Opción no válida.");
                return selectFoodAmount(); // Repetir hasta seleccionar una opción válida
        }
    }
    
    private int calculateCost(int cantidad) {
        int totalCost = cantidad; // 1 moneda por cada comida
        // Aplicar descuento
        if (cantidad >= 25) {
            int descuento = (cantidad / 25) * 5; // 5 monedas de descuento por cada 25
            totalCost -= descuento;
        }
        return totalCost;
    }
}