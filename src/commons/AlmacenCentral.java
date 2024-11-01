package commons;

/**
 * Clase que representa un almacén central básico con atributos para comida animal y vegetal,
 * además de un indicador de construcción.
 */
public class AlmacenCentral {

    private int capacidadAlmacen;          // Capacidad total del almacén
    private int cantidadComidaAnimal;      // Cantidad actual de comida animal almacenada
    private int cantidadComidaVegetal;     // Cantidad actual de comida vegetal almacenada
    private boolean construido;             // Indica si el almacén ha sido construido

    /**
     * Constructor del almacén central.
     */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200; 
        this.cantidadComidaAnimal = 0;      // Inicialmente no hay comida almacenada
        this.cantidadComidaVegetal = 0;     // Inicialmente no hay comida almacenada
        this.construido = false;             // El almacén no está construido inicialmente
    }

    // Getters y Setters

    public int getCapacidadAlmacen() {
        return capacidadAlmacen;
    }

    public void setCapacidadAlmacen(int capacidadAlmacen) {
        this.capacidadAlmacen = capacidadAlmacen;
    }

    public int getCantidadComidaAnimal() {
        return cantidadComidaAnimal;
    }

    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
        // Asegurarse de que la cantidad no exceda la capacidad
        if (cantidadComidaAnimal <= capacidadAlmacen) {
            this.cantidadComidaAnimal = cantidadComidaAnimal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida animal: excede la capacidad.");
        }
    }

    public int getCantidadComidaVegetal() {
        return cantidadComidaVegetal;
    }

    public void setCantidadComidaVegetal(int cantidadComidaVegetal) {
        // Asegurarse de que la cantidad no exceda la capacidad
        if (cantidadComidaVegetal <= capacidadAlmacen) {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    public boolean isConstruido() {
        return construido;
    }

    /**
     * Método para construir el almacén.
     * @return true si la construcción fue exitosa, false si ya está construido.
     */
    public boolean construir() {
        if (!construido) {
            construido = true;
            System.out.println("Almacén central construido.");
            return true;
        } else {
            System.out.println("El almacén central ya está construido.");
            return false;
        }
    }

    /**
     * Aumenta la capacidad del almacén central en una cantidad específica.
     * @param aumento La cantidad en la que se desea aumentar la capacidad.
     */
    public void aumentarCapacidad(int aumento) {
        if (aumento > 0) {
            capacidadAlmacen += aumento;
            System.out.println("Capacidad aumentada en " + aumento + " unidades. Nueva capacidad: " + capacidadAlmacen);
        } else {
            System.out.println("El aumento debe ser positivo.");
        }
    }

    /**
     * Muestra el estado actual del almacén.
     */
    public void mostrarEstado() {
        System.out.println("\nEstado del Almacén Central:");
        System.out.println("Capacidad Total: " + capacidadAlmacen);
        System.out.println("Cantidad de Comida Animal: " + cantidadComidaAnimal);
        System.out.println("Cantidad de Comida Vegetal: " + cantidadComidaVegetal);
        System.out.println("Almacén construido: " + (construido ? "Sí" : "No"));
    }
}
