package commons;

import piscifactorias.Piscifactoria;
import piscifactorias.tipos.PiscifactoriaDeRio;
import tanque.Tanque;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.tipos.rio.Pejerrey;

<<<<<<< HEAD
import java.util.List;
import java.util.ArrayList;
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> origin/DavidTrama

public class Simulador {
    
    private int dias;
<<<<<<< HEAD
    private String nombreEntidad;

    private List<Piscifactoria> piscifacorias;
=======
    private ArrayList<Piscifactoria> piscifacorias;
    private String nombreEntidad;

>>>>>>> origin/DavidTrama
    private SistemaMonedas monedas;
    private InputHelper inputHelper;
    private MenuHelper menuHelper;
    private AlmacenCentral almacenCentral;

    public Simulador() {
<<<<<<< HEAD
        this.dias = 0;
        this.piscifacorias = new ArrayList<>();
        this.monedas = new SistemaMonedas(100);
        this.inputHelper = new InputHelper();
        this.menuHelper = new MenuHelper();
        this.nombreEntidad = nombreEntidad;
        this.almacenCentral = null;
    }

    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombbre de la entidad/empresa/partida: ");
        System.out.println();

        String nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera de la Piscifactoria: ");
        System.out.println();

        PiscifactoriaDeRio piscifactoriaDeRio = new PiscifactoriaDeRio(nombrePiscifactoria, monedas);

        piscifactoriaDeRio.addPez(new Pejerrey(false));
        piscifactoriaDeRio.addPez(new Pejerrey(true));

        piscifacorias.add(piscifactoriaDeRio); 
        
        
    }

    public void menu() {
=======
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
    }

    public void menu() {
        menuHelper.clearOptions(); // Limpiar opciones previas si es necesario

>>>>>>> origin/DavidTrama
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

<<<<<<< HEAD
    public void menuPisc() {
        menuHelper.clearOptions(); // Limpiar opciones previas si es necesario
        System.out.println("Seleccione una opción:");
=======
    public void menuPisc() { //TODO se puede seleccionar la piscifactoria?
        System.out.println("\nSeleccione una opción:");
>>>>>>> origin/DavidTrama
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");
        System.out.println();
    
<<<<<<< HEAD
=======
        // Mostrar opciones
>>>>>>> origin/DavidTrama
        for (int i = 0; i < piscifacorias.size(); i++) {
            Piscifactoria piscifactoria = piscifacorias.get(i);
            int vivos = piscifactoria.getTotalVivos();
            int total = piscifactoria.getTotalPeces();
            int capacidad = piscifactoria.getCapacidadTotal();
<<<<<<< HEAD
            String optionText = piscifactoria.getNombre() + " [" + vivos + "/" + total + "/" + capacidad + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        menuHelper.addOption(0, "Cancelar");
    
        menuHelper.showMenu();
=======
    
            System.out.println((i + 1) + ".- " + piscifactoria.getNombre() + " [" + vivos + "/" + total + "/" + capacidad + "]");
        }
    
        System.out.println("0.- Cancelar");
    
        // Leer entrada del usuario
        int seleccion = inputHelper.readInt("Ingrese su selección: ");
        
        // Validar selección
        if (seleccion < 0 || seleccion > piscifacorias.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            menuPisc();  // Llamada recursiva para repetir el menú
        } else if (seleccion == 0) {
            System.out.println("Operación cancelada.");
        } else {
            Piscifactoria piscifactoriaSeleccionada = piscifacorias.get(seleccion - 1);
            System.out.println("Has seleccionado la piscifactoría: " + piscifactoriaSeleccionada.getNombre());
        }
>>>>>>> origin/DavidTrama
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
<<<<<<< HEAD
        System.out.println("Seleccione un Tanque:");

        List<Tanque> tanques = piscifactoria.getTanques();
        for (int i = 0; i < tanques.size(); i++) {
            Tanque tanque = tanques.get(i);
            String optionText = "Tanque " + (i + 1) + " [Tipo de pez: " + tanque.getTipoPezActual().getSimpleName() + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();

=======
        System.out.println("\nSeleccione un Tanque:");
    
        List<Tanque> tanques = piscifactoria.getTanques();
        for (int i = 0; i < tanques.size(); i++) {
            Tanque tanque = tanques.get(i);
            // Verifica si el tipo de pez actual es nulo
            String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName() : "Tanque sin tipo de pez asignado";
            String optionText = "Tanque " + (i + 1) + " [Tipo de pez: " + tipoPez + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        
        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();
    
>>>>>>> origin/DavidTrama
        int selection = inputHelper.readInt("Ingrese su selección: ");
        System.out.println();
        if (selection < 0 || selection > tanques.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectTank(piscifactoria); // Llamada recursiva hasta que se haga una selección válida
        }
        return selection;
    }
<<<<<<< HEAD

    

=======
    
>>>>>>> origin/DavidTrama
    public void showGeneralStatus() {
        System.out.println("Día actual: " + dias);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifacorias) {
            System.out.println("Piscifactoría: " + piscifactoria.getNombre());
            System.out.println("Comida vegetal actual: " + piscifactoria.getComidaVegetalActual());
            System.out.println("Comida animal actual: " + piscifactoria.getComidaAnimalActual());
            piscifactoria.showStatus();
        }

        if (almacenCentral != null) {
            int comidaVegetal = almacenCentral.getComidaVegetal();
            int comidaAnimal = almacenCentral.getComidaAnimal();
            int capacidadMaxVeg = almacenCentral.getCapacidadMaxVeg();
            int capacidadMaxAni = almacenCentral.getCapacidadMaxAni();

            System.out.println("Almacén Central:");
            System.out.println("Comida vegetal almacenada: " + comidaVegetal + " / " + capacidadMaxVeg + " (" + (comidaVegetal * 100 / capacidadMaxVeg) + "%)");
            System.out.println("Comida animal almacenada: " + comidaAnimal + " / " + capacidadMaxAni + " (" + (comidaAnimal * 100 / capacidadMaxAni) + "%)");
        } else {
            System.out.println("No hay Almacén Central disponible.");
        }
    }
<<<<<<< HEAD
    
=======

>>>>>>> origin/DavidTrama
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

<<<<<<< HEAD
=======














    public void upgrade() {
        
        while (true) {
            // Configuramos el menú principal
            menuHelper.clearOptions();
            menuHelper.addOption(1, "Comprar edificios");
            menuHelper.addOption(2, "Mejorar edificios");
            menuHelper.addOption(3, "Cancelar");
    
            // Mostramos el menú y obtenemos la opción seleccionada
            menuHelper.showMenu();
            int opcion = menuHelper.getSelection();
    
            switch (opcion) {
                case 1:
                    // Comprar edificios
                    menuHelper.clearOptions();
                    menuHelper.addOption(1, "Piscifactoría");
                    menuHelper.addOption(2, "Almacén central");
    


                    break;
    
                case 2:
                    // Mejorar edificios
                    menuHelper.clearOptions();
                    menuHelper.addOption(1, "Piscifactoría");
                    menuHelper.addOption(2, "Almacén central");
    
                    menuHelper.showMenu();
                    int edificioAMejorar = menuHelper.getSelection();
    
                    if (edificioAMejorar == 1) {
                        // Mejorar piscifactoría
                        menuHelper.clearOptions();
                        menuHelper.addOption(1, "Comprar tanque");
                        menuHelper.addOption(2, "Aumentar almacén de comida");
    
                       
                        
                    } else if (edificioAMejorar == 2) {
                        // Mejorar almacén central (si se tiene)
                        
                        
                        
                    } else {
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                    }
                    break;
    
                case 3:
                    // Cancelar
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
    
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                    break;
            }
        }
    }




















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








































    

    
    
    

>>>>>>> origin/DavidTrama
    public void showTankStatus() {
        // Selecciona una piscifactoría
        int piscifactoriaSeleccionada = selectPisc();

        // Valida la selección de la piscifactoría
        if (piscifactoriaSeleccionada == 0) {
            System.out.println("Operación cancelada.");
            return;
        } else if (piscifactoriaSeleccionada > 0 && piscifactoriaSeleccionada <= piscifacorias.size()) {
            // Obtiene la piscifactoría seleccionada
            Piscifactoria piscifactoria = piscifacorias.get(piscifactoriaSeleccionada - 1);

            // Selecciona un tanque dentro de la piscifactoría
            int tanqueSeleccionado = selectTank(piscifactoria);

            // Valida la selección del tanque
            if (tanqueSeleccionado == 0) {
                System.out.println("Operación cancelada.");
                return;
            } else if (tanqueSeleccionado > 0 && tanqueSeleccionado <= piscifactoria.getTanques().size()) {
                // Muestra el estado de los peces en el tanque seleccionado
                piscifactoria.showFishStatus(tanqueSeleccionado);
            } else {
                System.out.println("Selección de tanque no válida. Inténtalo de nuevo.");
            }
        } else {
            System.out.println("Selección de piscifactoría no válida. Inténtalo de nuevo.");
        }
    }

    public void nextDay() {
        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.nextDay();
            System.out.println();
        }
        System.out.println("Monedas: " + monedas.getMonedas());
        System.out.println();
    }
    


<<<<<<< HEAD
    public static void main(String[] args) {
        Simulador simulador = new Simulador();
        simulador.init();
        simulador.showTankStatus();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.nextDay();
        simulador.showTankStatus();
    }
=======
    
>>>>>>> origin/DavidTrama

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
<<<<<<< HEAD
    public void setPiscifacorias(List<Piscifactoria> piscifacorias) {
=======
    public void setPiscifacorias(ArrayList<Piscifactoria> piscifacorias) {
>>>>>>> origin/DavidTrama
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