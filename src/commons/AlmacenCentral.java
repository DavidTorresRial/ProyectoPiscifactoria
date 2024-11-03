package commons;

/**
 * Clase que representa un almacén central básico con capacidad para almacenar
 * comida animal y vegetal.
 * Permite construir el almacén, aumentar su capacidad y gestionar la comida
 * almacenada.
 */
public class AlmacenCentral {

    /** Capacidad total del almacén. */
    private int capacidadAlmacen;

    /** Cantidad actual de comida animal almacenada. */
    private int cantidadComidaAnimal;

    /** Cantidad actual de comida vegetal almacenada. */
    private int cantidadComidaVegetal;

    /** Indica si el almacén ha sido construido. */
    private boolean construido;

    /** Constructor del almacén central. */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200;
        this.cantidadComidaAnimal = 0;
        this.cantidadComidaVegetal = 0;
        this.construido = false;
    }

    /**
     * Método para construir el almacén.
     * 
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
     * 
     * @param aumento La cantidad en la que se desea aumentar la capacidad. Debe ser
     *                positiva.
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
     * 
     * @param cantidad La cantidad de comida animal a añadir. Debe ser positiva.
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
     * 
     * @param cantidad La cantidad de comida vegetal a añadir. Debe ser positiva.
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
     * 
     * @param cantidad La cantidad de comida a añadir. Debe ser positiva.
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
        System.out.println("Estado del Almacén Central:");
        System.out.println("Capacidad Total: " + capacidadAlmacen);

        // Calcular el porcentaje de comida animal y vegetal
        double porcentajeComidaAnimal = (cantidadComidaAnimal * 100.0) / capacidadAlmacen;
        double porcentajeComidaVegetal = (cantidadComidaVegetal * 100.0) / capacidadAlmacen;

        // Mostrar la cantidad y el porcentaje
        System.out.println("Cantidad de Comida Animal: " + cantidadComidaAnimal + " (" + porcentajeComidaAnimal
                + "% de la capacidad)");
        System.out.println("Cantidad de Comida Vegetal: " + cantidadComidaVegetal + " (" + porcentajeComidaVegetal
                + "% de la capacidad)");
    }

    // Getters y Setters

    /**
     * Devuelve la capacidad total del almacén.
     * 
     * @return La capacidad del almacén.
     */
    public int getCapacidadAlmacen() {
        return capacidadAlmacen;
    }

    /**
     * Establece la capacidad del almacén.
     * 
     * @param capacidadAlmacen Nueva capacidad del almacén.
     */
    public void setCapacidadAlmacen(int capacidadAlmacen) {
        this.capacidadAlmacen = capacidadAlmacen;
    }

    /**
     * Devuelve la cantidad de comida animal actualmente almacenada.
     * 
     * @return La cantidad de comida animal.
     */
    public int getCantidadComidaAnimal() {
        return cantidadComidaAnimal;
    }

    /**
     * Establece la cantidad de comida animal en el almacén.
     * 
     * @param cantidadComidaAnimal Nueva cantidad de comida animal.
     */
    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
        // Asegurarse de que la cantidad no exceda la capacidad
        if (cantidadComidaAnimal <= capacidadAlmacen) {
            this.cantidadComidaAnimal = cantidadComidaAnimal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida animal: excede la capacidad.");
        }
    }

    /**
     * Devuelve la cantidad de comida vegetal actualmente almacenada.
     * 
     * @return La cantidad de comida vegetal.
     */
    public int getCantidadComidaVegetal() {
        return cantidadComidaVegetal;
    }

    /**
     * Establece la cantidad de comida vegetal en el almacén.
     * 
     * @param cantidadComidaVegetal Nueva cantidad de comida vegetal.
     */
    public void setCantidadComidaVegetal(int cantidadComidaVegetal) {
        // Asegurarse de que la cantidad no exceda la capacidad
        if (cantidadComidaVegetal <= capacidadAlmacen) {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    /**
     * Indica si el almacén ha sido construido.
     * 
     * @return true si el almacén está construido, false en caso contrario.
     */
    public boolean isConstruido() {
        return construido;
    }
}
