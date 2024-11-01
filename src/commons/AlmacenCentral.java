package commons;

/**
 * Clase que representa un almacén central básico con atributos para comida animal y vegetal,
 * además de un indicador de construcción.
 */
public class AlmacenCentral {

    private int capacidadComidaAnimal;
    private int capacidadComidaVegetal;
    private boolean construido;

    /**
     * Constructor del almacén central.
     */
    public AlmacenCentral() {
        this.capacidadComidaAnimal = 0;
        this.capacidadComidaVegetal = 0;
        this.construido = false;
    }

    // Getters y Setters

    public int getCapacidadComidaAnimal() {
        return capacidadComidaAnimal;
    }

    public void setCapacidadComidaAnimal(int capacidadComidaAnimal) {
        this.capacidadComidaAnimal = capacidadComidaAnimal;
    }

    public int getCapacidadComidaVegetal() {
        return capacidadComidaVegetal;
    }

    public void setCapacidadComidaVegetal(int capacidadComidaVegetal) {
        this.capacidadComidaVegetal = capacidadComidaVegetal;
    }

    public boolean isConstruido() {
        return construido;
    }

    public void setConstruido(boolean construido) {
        this.construido = construido;
    }

    /**
     * Muestra el estado actual del almacén.
     */
    public void mostrarEstado() {
        System.out.println("Estado del Almacén Central:");
        System.out.println("Capacidad de Comida Animal: " + capacidadComidaAnimal);
        System.out.println("Capacidad de Comida Vegetal: " + capacidadComidaVegetal);
        System.out.println("Almacén construido: " + (construido ? "Sí" : "No"));
    }
}
