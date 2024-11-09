package commons;

/** Representa un almacén central con capacidad para almacenar comida animal y vegetal. */
public class AlmacenCentral {

    /** Capacidad total del almacén. */
    private int capacidadAlmacen;

    /** Cantidad de comida animal almacenada. */
    private int cantidadComidaAnimal;

    /** Cantidad de comida vegetal almacenada. */
    private int cantidadComidaVegetal;

    /** Costo de mejora fijo para el almacen central. */
    private final int costoMejora = 200;

    /** Constructor del almacén central. */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200;
        this.cantidadComidaAnimal = 0;
        this.cantidadComidaVegetal = 0;
    }

    /**
     * Aumenta la capacidad del almacén central en 50 unidades si hay monedas suficientes.
     * 
     * @return true si se aumentó la capacidad exitosamente, false en caso contrario.
     */
    public boolean aumentarCapacidad() {
        if (Simulador.monedas.gastarMonedas(costoMejora)) {
            capacidadAlmacen += 50;
            System.out.println("\nCapacidad del almacén central mejorada en 50 unidades hasta " + capacidadAlmacen);
            return true;
        }
        return false;
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
            int espacioLibre = capacidadAlmacen - cantidadComidaAnimal;
            cantidadComidaAnimal = capacidadAlmacen;
            return espacioLibre > 0;
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
            cantidadComidaVegetal = capacidadAlmacen;
            return espacioLibre > 0;
        }
    }

    /**
     * Método para calcular el costo de añadir una cantidad de comida.
     * 
     * @param cantidad La cantidad de comida a añadir. Debe ser positiva.
     * @return El costo total de añadir esa cantidad de comida.
     */
    public int calcularCosto(int cantidad) {
        int costo = cantidad;
        if (cantidad >= 25) {
            int descuentos = cantidad / 25;
            costo -= descuentos * 5;
        }
        return costo;
    }

    /** Muestra el estado actual del almacén. */
    public void mostrarEstado() {
        System.out.println("Estado del Almacén Central:");

        double porcentajeComidaAnimal = (cantidadComidaAnimal * 100.0) / capacidadAlmacen;
        double porcentajeComidaVegetal = (cantidadComidaVegetal * 100.0) / capacidadAlmacen;

        System.out.println("Cantidad de Comida Animal: " + cantidadComidaAnimal + " (" + porcentajeComidaAnimal + "% de la capacidad)");
        System.out.println("Cantidad de Comida Vegetal: " + cantidadComidaVegetal + " (" + porcentajeComidaVegetal + "% de la capacidad)");
    }

    /**
     * Devuelve la capacidad total del almacén.
     * 
     * @return La capacidad del almacén.
     */
    public int getCapacidadAlmacen() {
        return capacidadAlmacen;
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
        if (cantidadComidaVegetal <= capacidadAlmacen) {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    /**
     * Devuelve una representacion en cadena del estado del almacen central.
     * 
     * @return una cadena que describe el estado del almacen central.
     */
    @Override
    public String toString() {
        return "Almacen Central:\n" +
                "Capacidad Total: " + capacidadAlmacen + "\n" +
                "Cantidad de Comida Animal: " + cantidadComidaAnimal + " (" +
                ((cantidadComidaAnimal * 100.0) / capacidadAlmacen) + "% de la capacidad)\n" +
                "Cantidad de Comida Vegetal: " + cantidadComidaVegetal + " (" +
                ((cantidadComidaVegetal * 100.0) / capacidadAlmacen) + "% de la capacidad)";
    }
}
