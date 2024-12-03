package commons;

import java.util.List;

import piscifactoria.Piscifactoria;

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
    public void aumentarCapacidad() {
        if (Simulador.monedas.gastarMonedas(costoMejora)) {
            capacidadAlmacen += 50;
            System.out.println("\nCapacidad del almacén central mejorada en 50 unidades hasta " + capacidadAlmacen);
            Simulador.logger.log("Mejorando el almacen central, aumentando su capacidad de comida.");
        } else {
            System.out.println("Necesitas " + costoMejora + " monedas para aumentar la capacidad.");
        }
    }

    /**
     * Método para añadir comida animal al almacén.
     * 
     * @param cantidad La cantidad de comida animal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public void añadirComidaAnimal(int cantidad) {
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaAnimal = nuevaCantidad;
        } else {
            System.out.println("No se puede añadir la cantidad de comida animal: excede la capacidad.");
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     * 
     * @param cantidad La cantidad de comida vegetal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public void añadirComidaVegetal(int cantidad) {
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaVegetal = nuevaCantidad;
        } else {
            System.out.println("No se puede añadir la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    /** Muestra el estado actual del almacén. */
    public void mostrarEstado() {
        System.out.println("\nEstado del Almacén Central:");

        System.out.println("Comida vegetal al " + (cantidadComidaVegetal * 100 / capacidadAlmacen) + 
                "% de su capacidad. [" + cantidadComidaVegetal + "/" + capacidadAlmacen + "]");
        System.out.println("Comida animal al " + (cantidadComidaAnimal * 100 / capacidadAlmacen)+ 
                "% de su capacidad. [" + cantidadComidaAnimal + "/" + capacidadAlmacen + "]");
    }

    /**
     * Distribuye equitativamente la comida animal y vegetal entre las piscifactorías disponibles.
     * 
     * @param piscifactorias la lista de piscifactorías a las que se les distribuirá comida.
     */
    public void distribuirComida(List<Piscifactoria> piscifactorias) {
        int necesitanComidaAnimal = 0, necesitanComidaVegetal = 0;

        do {
            for (Piscifactoria piscifactoria : piscifactorias) {
                if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                    necesitanComidaAnimal++;
                }
                if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                    necesitanComidaVegetal++;
                }
            }

            int comidaAnimalPorPiscifactoria = (necesitanComidaAnimal > 0) ? cantidadComidaAnimal / necesitanComidaAnimal : 0;
            int comidaVegetalPorPiscifactoria = (necesitanComidaVegetal > 0) ? cantidadComidaVegetal / necesitanComidaVegetal : 0;

            for (Piscifactoria piscifactoria : piscifactorias) {
                if (piscifactoria.getComidaAnimalActual() < piscifactoria.getCapacidadMaximaComida()) {
                    int espacioDisponibleAnimal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaAnimalActual();
                    int comidaMaxima = Math.min(espacioDisponibleAnimal, comidaAnimalPorPiscifactoria);
                    piscifactoria.añadirComidaAnimal(comidaMaxima);
                    cantidadComidaAnimal -= comidaMaxima;
                    necesitanComidaAnimal--;
                }

                if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                    int espacioDisponibleVegetal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaVegetalActual();
                    int comidaMaxima = Math.min(espacioDisponibleVegetal, comidaVegetalPorPiscifactoria);
                    piscifactoria.añadirComidaVegetal(comidaMaxima);
                    cantidadComidaVegetal -= comidaMaxima;
                    necesitanComidaVegetal--;
                }
            }
        } while (necesitanComidaAnimal == 0 && necesitanComidaVegetal == 0);
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
        return "Información del Almacén Central:" +
                "\n  Capacidad Total          : " + capacidadAlmacen +
                "\n  Comida Animal            : " + cantidadComidaAnimal + " (" + ((cantidadComidaAnimal * 100.0) / capacidadAlmacen) + "% de la capacidad)" +
                "\n  Comida Vegetal           : " + cantidadComidaVegetal + " (" + ((cantidadComidaVegetal * 100.0) / capacidadAlmacen) + "% de la capacidad)";
    }
}
