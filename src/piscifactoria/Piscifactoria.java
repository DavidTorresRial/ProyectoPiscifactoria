package piscifactoria;

import java.util.List;
import java.util.ArrayList;

import tanque.Tanque;

/** Clase abstracta que representa una piscifactoría que gestiona tanques de peces. */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría. */
    protected String nombre;

    /** Lista de tanques con distintos tipos de peces. */
    protected List<Tanque> tanques = new ArrayList<>();
    
    /** Número máximo de tanques permitidos en la piscifactoría. */
    protected final int numeroMaximoTanques = 10;
    
    /** Cantidad actual de comida animal. */
    public static int cantidadComidaAnimal; // TODO Preguntar si esto esta bien así en plan si se puede hacer public static este atributo o hay que hacerselo al getter o como

    /** Cantidad actual de comida vegetal. */
    public static int cantidadComidaVegetal; // TODO Preguntar si esto esta bien así en plan si se puede hacer public static este atributo o hay que hacerselo al getter o como

    /** Capacidad máxima para ambos tipos de comida. */
    protected int capacidadMaximaComida;

    /**
     * Constructor para crear una nueva piscifactoría.
     *
     * @param nombre  El nombre de la piscifactoría.
     */
    public Piscifactoria(String nombre) {        
        this.nombre = nombre;
    }

    /** Muestra toda la información de la piscifactoría. */
    public void showStatus() {
        System.out.println("\n=============== " + nombre + " ===============");
        
        int totalPeces = getTotalPeces();
        int capacidadTotal = getCapacidadTotal();
        int totalVivos = getTotalVivos();
        int totalAlimentados = getTotalAlimentados();
        int totalAdultos = getTotalAdultos();
        int totalHembras = getTotalHembras();
        int totalMachos = getTotalMachos();
        int totalFertiles = getTotalFertiles();
        int porcentajeOcupacion = (capacidadTotal > 0) ? (totalPeces * 100) / capacidadTotal : 0;
        int porcentajeVivos = (totalPeces > 0) ? (totalVivos * 100) / totalPeces : 0;
        int porcentajeAlimentados = (totalVivos > 0) ? (totalAlimentados * 100) / totalVivos : 0;
        int porcentajeAdultos = (totalVivos > 0) ? (totalAdultos * 100) / totalVivos : 0;
    
        System.out.println("Tanques: " + tanques.size());
        System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + porcentajeOcupacion + "%)");
        System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + porcentajeVivos + "%)");
        System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + porcentajeAdultos + "%)");
        System.out.println("Hembras / Machos: " + totalHembras + " / " + totalMachos);
        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);
    
        showFood();
    }
    

    /** Muestra el estado de cada tanque en la piscifactoría. */
    public void showTankStatus() {
        tanques.forEach(Tanque::showStatus);
    }

    /**
     * Muestra la información de los peces de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showFishStatus(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showFishStatus();
    }

    /**
     * Muestra la ocupación de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showCapacity(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showCapacity();
    }

    /** Muestra el estado actual del depósito de comida de la piscifactoría. */
    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");

        System.out.println("Comida vegetal al " + (cantidadComidaVegetal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaVegetal + "/" + capacidadMaximaComida + "]");
        System.out.println("Comida animal al " + (cantidadComidaAnimal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaAnimal + "/" + capacidadMaximaComida + "]");
    }

    /** Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y actualizando sus estados. */
    public void nextDay() {
        for (Tanque tanque : tanques) {
            tanque.nextDay();
        }
    }

    /** Mejora el almacén de comida aumentando su capacidad máxima. */
    public abstract void upgradeFood();

    /** Agrega un tanque, verifica monedas y el límite de tanques. */
    public abstract void addTanque();

    /**
     * Método para añadir comida animal al almacén.
     * 
     * @param cantidad La cantidad de comida animal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaAnimal(int cantidad) { // TODO Tambien preguntar si juntar estos dos métodos 
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaAnimal = nuevaCantidad;
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida animal: excede la capacidad.");
            return false;
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     * 
     * @param cantidad La cantidad de comida vegetal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaVegetal(int cantidad) {
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaVegetal = nuevaCantidad;          
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida vegetal: excede la capacidad.");
            return false;
        }
    }

    /**
     * Devuelve el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la lista de tanques de la piscifactoría.
     *
     * @return La lista de tanques.
     */
    public List<Tanque> getTanques() {
        return this.tanques;
    }

    /**
     * Obtiene la capacidad máxima de comida.
     * 
     * @return La capacidad máxima de comida.
     */
    public int getCapacidadMaximaComida() {
        return capacidadMaximaComida;
    }

    /**
     * Devuelve la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @return La cantidad actual de comida vegetal.
     */
    public int getComidaVegetalActual() {
        return cantidadComidaVegetal;
    }

    /**
     * Devuelve la cantidad actual de comida animal en la piscifactoría.
     *
     * @return La cantidad actual de comida animal.
     */
    public int getComidaAnimalActual() {
        return cantidadComidaAnimal;
    }

    /**
     * Devuelve el total de peces en la piscifactoría.
     *
     * @return El total de peces.
     */
    public int getTotalPeces() {
        int totalPeces = 0;
        for (Tanque tanque : tanques) {
            totalPeces += tanque.getPeces().size();
        }
        return totalPeces;
    }

    /**
     * Devuelve la capacidad total de todos los tanques de la piscifactoría.
     *
     * @return La capacidad total de los tanques.
     */
    public int getCapacidadTotal() {
        int capacidadTotal = 0;
        for (Tanque tanque : tanques) {
            capacidadTotal += tanque.getCapacidad();
        }
        return capacidadTotal;
    }

    /**
     * Devuelve el total de peces vivos en la piscifactoría.
     *
     * @return El total de peces vivos.
     */
    public int getTotalVivos() {
        int totalVivos = 0;
        for (Tanque tanque : tanques) {
            totalVivos += tanque.getVivos();
        }
        return totalVivos;
    }

    /**
     * Devuelve el total de peces alimentados en la piscifactoría.
     *
     * @return El total de peces alimentados.
     */
    public int getTotalAlimentados() {
        int totalAlimentados = 0;
        for (Tanque tanque : tanques) {
            totalAlimentados += tanque.getAlimentados();
        }
        return totalAlimentados;
    }

    /**
     * Devuelve el total de peces adultos en la piscifactoría.
     *
     * @return El total de peces adultos.
     */
    public int getTotalAdultos() {
        int totalAdultos = 0;
        for (Tanque tanque : tanques) {
            totalAdultos += tanque.getAdultos();
        }
        return totalAdultos;
    }

    /**
     * Devuelve el total de machos en la piscifactoría.
     *
     * @return El total de machos.
     */
    public int getTotalMachos() {
        int totalMachos = 0;
        for (Tanque tanque : tanques) {
            totalMachos += tanque.getMachos();
        }
        return totalMachos;
    }

    /**
     * Devuelve el total de hembras en la piscifactoría.
     *
     * @return El total de hembras.
     */
    public int getTotalHembras() {
        int totalHembras = 0;
        for (Tanque tanque : tanques) {
            totalHembras += tanque.getHembras();
        }
        return totalHembras;
    }

    /**
     * Devuelve el total de peces feudiles en la piscifactoría.
     *
     * @return El total de peces feudiles.
     */
    public int getTotalFertiles() {
        int totalFertiles = 0;
        for (Tanque tanque : tanques) {
            totalFertiles += tanque.getFertiles();
        }
        return totalFertiles;
    }

    /**
     * Devuelve una representación en cadena del estado de la piscifactoría.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "\nInformación de la Piscifactoría: " + nombre +
                "\n  Número de Tanques    : " + tanques.size() +
                "\n  Total de Peces       : " + getTotalPeces() + " (Ocupación: " + ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) +
                "\n  Peces Vivos          : " + getTotalVivos() +
                "\n  Peces Alimentados    : " + getTotalAlimentados() +
                "\n  Peces Adultos        : " + getTotalAdultos() +
                "\n  Hembras              : " + getTotalHembras() +
                "\n  Machos               : " + getTotalMachos() +
                "\n  Peces Fértiles       : " + getTotalFertiles() +
                "\n  Comida Vegetal       : " + getComidaVegetalActual() + " / " + getCapacidadTotal() +
                "\n  Comida Animal        : " + getComidaAnimalActual() + " / " + getCapacidadTotal();
    }
}
