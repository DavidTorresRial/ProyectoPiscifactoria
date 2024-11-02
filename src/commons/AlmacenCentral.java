package commons;

/**
 * Clase que representa un almacén central básico con atributos para comida animal y vegetal,
 * además de un indicador de construcción.
 */
public class AlmacenCentral {

    private int capacidadAlmacen;          // Capacidad total del almacén
    private int cantidadComidaAnimal;      // Cantidad actual de comida animal almacenada
    private int cantidadComidaVegetal;     // Cantidad actual de comida vegetal almacenada
    private boolean construido;            // Indica si el almacén ha sido construido

    /**
     * Constructor del almacén central.
     */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200; 
        this.cantidadComidaAnimal = 0;      // Inicialmente no hay comida almacenada
        this.cantidadComidaVegetal = 0;     // Inicialmente no hay comida almacenada
        this.construido = false;            // El almacén no está construido inicialmente
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
     * Método para añadir comida animal al almacén.
     * @param cantidad La cantidad de comida animal a añadir.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaAnimal(int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad a añadir debe ser positiva.");
            return false;
        }
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaAnimal = nuevaCantidad;
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida animal: excede la capacidad.");
            // Añadir solo lo que quepa
            int espacioLibre = capacidadAlmacen - cantidadComidaAnimal;
            cantidadComidaAnimal = capacidadAlmacen; // Llenar hasta la capacidad máxima
            return espacioLibre > 0; // Indica si se añadió algo
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     * @param cantidad La cantidad de comida vegetal a añadir.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaVegetal(int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad a añadir debe ser positiva.");
            return false;
        }
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaVegetal = nuevaCantidad;
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida vegetal: excede la capacidad.");
            // Añadir solo lo que quepa
            int espacioLibre = capacidadAlmacen - cantidadComidaVegetal;
            cantidadComidaVegetal = capacidadAlmacen; // Llenar hasta la capacidad máxima
            return espacioLibre > 0; // Indica si se añadió algo
        }
    }

    /**
     * Método para calcular el costo de añadir una cantidad de comida.
     * @param cantidad La cantidad de comida a añadir.
     * @return El costo total de añadir esa cantidad de comida.
     */
    public int calcularCosto(int cantidad) {
        int costo = cantidad; // Cada comida cuesta 1 moneda
        // Descuento por cada 25 añadidos
        if (cantidad >= 25) {
            int descuentos = cantidad / 25;
            costo -= descuentos * 5; // Descuento de 5 monedas por cada 25
        }
        return costo;
    }

    /**
     * Muestra el estado actual del almacén.
     */
    public void mostrarEstado() {
        System.out.println("\nEstado del Almacén Central:");
        System.out.println("Capacidad Total: " + capacidadAlmacen);
        // Calculamos el porcentaje de ocupación
        int porcentajeComidaAnimal = (cantidadComidaAnimal * 100) / capacidadAlmacen;
        int porcentajeComidaVegetal = (cantidadComidaVegetal * 100) / capacidadAlmacen;
        
        System.out.println("Cantidad de Comida Animal: " + cantidadComidaAnimal + " / " + capacidadAlmacen + ", " + porcentajeComidaAnimal + "%");
        System.out.println("Cantidad de Comida Vegetal: " + cantidadComidaVegetal + " / " + capacidadAlmacen + ", " + porcentajeComidaVegetal + "%");
        System.out.println("Almacén construido: " + (construido ? "Sí" : "No"));
    }

    /**
     * Método toString para representar el estado del almacén en formato de texto.
     * @return Una cadena que describe el estado actual del almacén central.
     */
    @Override
    public String toString() {
        int porcentajeComidaAnimal = (cantidadComidaAnimal * 100) / capacidadAlmacen;
        int porcentajeComidaVegetal = (cantidadComidaVegetal * 100) / capacidadAlmacen;

        return "AlmacenCentral {" +
               "Capacidad Total = " + capacidadAlmacen +
               ", Comida Animal = " + cantidadComidaAnimal + " / " + capacidadAlmacen + " (" + porcentajeComidaAnimal + "%)" +
               ", Comida Vegetal = " + cantidadComidaVegetal + " / " + capacidadAlmacen + " (" + porcentajeComidaVegetal + "%)" +
               ", Construido = " + (construido ? "Sí" : "No") +
               '}';
    }
}
