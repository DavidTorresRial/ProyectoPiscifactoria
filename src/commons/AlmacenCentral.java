package commons;

import helpers.Logger;
import java.util.List;

import piscifactorias.Piscifactoria;

/** Representa un almacén central con capacidad para almacenar comida animal y vegetal. */
public class AlmacenCentral {

    /** Capacidad total del almacén. */
    private int capacidadAlmacen;

    /** Cantidad de comida animal almacenada. */
    private int cantidadComidaAnimal;

    /** Cantidad de comida vegetal almacenada. */
    private int cantidadComidaVegetal;

    /** Costo de mejora fijo para el almacén central. */
    private final int costoMejora = 200;

    /** Constructor del almacén central. */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200;
        this.cantidadComidaAnimal = 0;
        this.cantidadComidaVegetal = 0;
        Logger.getInstance("almacen_central.log").log("Almacén central creado con capacidad inicial: " + capacidadAlmacen);
    }

    /**
     * Aumenta la capacidad del almacén central en 50 unidades si hay monedas suficientes.
     */
    public void aumentarCapacidad() {
        if (Simulador.monedas.gastarMonedas(costoMejora)) {
            capacidadAlmacen += 50;
            Logger.getInstance("almacen_central.log").log("Capacidad del almacén aumentada en 50 unidades. Nueva capacidad: " + capacidadAlmacen);
            System.out.println("\nCapacidad del almacén central mejorada en 50 unidades hasta " + capacidadAlmacen);
        } else {
            Logger.getErrorLogger().logError("No se pudo aumentar la capacidad del almacén. Monedas insuficientes.");
            System.out.println("Necesitas " + costoMejora + " monedas para aumentar la capacidad.");
        }
    }

    /**
     * Método para añadir comida animal al almacén.
     */
    public void añadirComidaAnimal(int cantidad) {
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaAnimal = nuevaCantidad;
            Logger.getInstance("almacen_central.log").log("Añadidas " + cantidad + " unidades de comida animal. Total actual: " + cantidadComidaAnimal);
        } else {
            Logger.getErrorLogger().logError("No se pudo añadir comida animal: cantidad excede la capacidad (" + cantidad + ").");
            System.out.println("No se puede añadir la cantidad de comida animal: excede la capacidad.");
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     */
    public void añadirComidaVegetal(int cantidad) {
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadAlmacen) {
            cantidadComidaVegetal = nuevaCantidad;
            Logger.getInstance("almacen_central.log").log("Añadidas " + cantidad + " unidades de comida vegetal. Total actual: " + cantidadComidaVegetal);
        } else {
            Logger.getErrorLogger().logError("No se pudo añadir comida vegetal: cantidad excede la capacidad (" + cantidad + ").");
            System.out.println("No se puede añadir la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    /** Muestra el estado actual del almacén. */
    public void mostrarEstado() {
        Logger.getInstance("almacen_central.log").log("Estado del almacén solicitado.");
        System.out.println("\nEstado del Almacén Central:");
        System.out.println("Comida vegetal al " + (cantidadComidaVegetal * 100 / capacidadAlmacen) + 
                "% de su capacidad. [" + cantidadComidaVegetal + "/" + capacidadAlmacen + "]");
        System.out.println("Comida animal al " + (cantidadComidaAnimal * 100 / capacidadAlmacen)+ 
                "% de su capacidad. [" + cantidadComidaAnimal + "/" + capacidadAlmacen + "]");
    }

    /**
     * Distribuye equitativamente la comida animal y vegetal entre las piscifactorías disponibles.
     */
    public void distribuirComida(List<Piscifactoria> piscifactorias) {
        Logger.getInstance("almacen_central.log").log("Distribución de comida iniciada.");
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
                    Logger.getInstance("almacen_central.log").log(comidaMaxima + " unidades de comida animal distribuidas a " + piscifactoria.getNombre());
                }

                if (piscifactoria.getComidaVegetalActual() < piscifactoria.getCapacidadMaximaComida()) {
                    int espacioDisponibleVegetal = piscifactoria.getCapacidadMaximaComida() - piscifactoria.getComidaVegetalActual();
                    int comidaMaxima = Math.min(espacioDisponibleVegetal, comidaVegetalPorPiscifactoria);
                    piscifactoria.añadirComidaVegetal(comidaMaxima);
                    cantidadComidaVegetal -= comidaMaxima;
                    necesitanComidaVegetal--;
                    Logger.getInstance("almacen_central.log").log(comidaMaxima + " unidades de comida vegetal distribuidas a " + piscifactoria.getNombre());
                }
            }
        } while (necesitanComidaAnimal == 0 && necesitanComidaVegetal == 0);
    }

    // Métodos getter y setter (sin cambios)

    public int getCapacidadAlmacen() {
        return capacidadAlmacen;
    }

    public int getCantidadComidaAnimal() {
        return cantidadComidaAnimal;
    }

    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
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
        if (cantidadComidaVegetal <= capacidadAlmacen) {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        } else {
            System.out.println("No se puede establecer la cantidad de comida vegetal: excede la capacidad.");
        }
    }

    @Override
    public String toString() {
        return "Información del Almacén Central:" +
                "\n  Capacidad Total          : " + capacidadAlmacen +
                "\n  Comida Animal            : " + cantidadComidaAnimal + " (" + ((cantidadComidaAnimal * 100.0) / capacidadAlmacen) + "% de la capacidad)" +
                "\n  Comida Vegetal           : " + cantidadComidaVegetal + " (" + ((cantidadComidaVegetal * 100.0) / capacidadAlmacen) + "% de la capacidad)";
    }
}
