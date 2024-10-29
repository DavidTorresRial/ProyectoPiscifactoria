package commons;

import piscifactorias.Piscifactoria;
import piscifactorias.tipos.PiscifactoriaDeRio;
import tanque.Tanque;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.tipos.rio.Pejerrey;

import java.util.List;
import java.util.ArrayList;

public class Simulador {
    
    private int dias;
    private String nombreEntidad;

    private List<Piscifactoria> piscifacorias;
    private SistemaMonedas monedas;
    private InputHelper inputHelper;
    private MenuHelper menuHelper;
    private AlmacenCentral almacenCentral;

    public Simulador() {
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
        System.out.println("Seleccione una opción:");
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");
        System.out.println();
    
        for (int i = 0; i < piscifacorias.size(); i++) {
            Piscifactoria piscifactoria = piscifacorias.get(i);
            int vivos = piscifactoria.getTotalVivos();
            int total = piscifactoria.getTotalPeces();
            int capacidad = piscifactoria.getCapacidadTotal();
            String optionText = piscifactoria.getNombre() + " [" + vivos + "/" + total + "/" + capacidad + "]";
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
        System.out.println("Seleccione un Tanque:");

        List<Tanque> tanques = piscifactoria.getTanques();
        for (int i = 0; i < tanques.size(); i++) {
            Tanque tanque = tanques.get(i);
            String optionText = "Tanque " + (i + 1) + " [Tipo de pez: " + tanque.getTipoPezActual().getSimpleName() + "]";
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
    public void setPiscifacorias(List<Piscifactoria> piscifacorias) {
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