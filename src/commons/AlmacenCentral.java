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
    private final int COSTO_MEJORA = 200;

    /** Incremento fijo para la mejora del almacén central. */
    private static final int INCREMENTO_MEJORA_ALMACEN = 50;

    /** Constructor del almacén central. */
    public AlmacenCentral() {
        this.capacidadAlmacen = 200;
        this.cantidadComidaAnimal = 0;
        this.cantidadComidaVegetal = 0;
    }

    /**
     * Constructor del almacén central con valores personalizados.
     *
     * @param capacidadAlmacen la capacidad total del almacén.
     * @param cantidadComidaAnimal la cantidad de comida animal en el almacén.
     * @param cantidadComidaVegetal la cantidad de comida vegetal en el almacén.
     */
    public AlmacenCentral(int capacidadAlmacen, int cantidadComidaAnimal, int cantidadComidaVegetal) {
        this.capacidadAlmacen = capacidadAlmacen;
        this.cantidadComidaAnimal = cantidadComidaAnimal;
        this.cantidadComidaVegetal = cantidadComidaVegetal;
    }

    /**
     * Aumenta la capacidad del almacén central en 50 unidades si hay monedas suficientes.
     * 
     * @return true si se aumentó la capacidad exitosamente, false en caso contrario.
     */
    public void aumentarCapacidad() {
        if (Simulador.monedas.gastarMonedas(COSTO_MEJORA)) {
            capacidadAlmacen += INCREMENTO_MEJORA_ALMACEN;
            System.out.println("\nCapacidad del almacén central mejorada en 50 unidades hasta " + capacidadAlmacen);
            Simulador.registro.registroMejorarAlmacenCentral(INCREMENTO_MEJORA_ALMACEN, cantidadComidaAnimal, COSTO_MEJORA);
        } else {
            System.out.println("\nNecesitas " + COSTO_MEJORA + " monedas para aumentar la capacidad.");
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
            System.out.println("\nNo se puede añadir la cantidad de comida animal: excede la capacidad.");
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
            System.out.println("\nNo se puede añadir la cantidad de comida vegetal: excede la capacidad.");
        }
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
        } while (necesitanComidaAnimal != 0 && necesitanComidaVegetal != 0);
    }

    /**
     * Obtiene la capacidad total del almacén.
     * 
     * @return Capacidad actual del almacén.
     */
    public int getCapacidadAlmacen() {
        return capacidadAlmacen;
    }

    /**
     * Establece la capacidad total del almacén.
     * 
     * @param capacidadAlmacen Nueva capacidad del almacén. Debe ser positiva.
     */
    public void setCapacidadAlmacen(int capacidadAlmacen) {
        if (capacidadAlmacen > 0) {
            this.capacidadAlmacen = capacidadAlmacen;
        } else {
            System.out.println("La capacidad del almacén debe ser positiva.");
        }
    }

    /**
     * Obtiene la cantidad de comida animal almacenada.
     * 
     * @return Cantidad de comida animal.
     */
    public int getCantidadComidaAnimal() {
        return cantidadComidaAnimal;
    }

    /**
     * Establece la cantidad de comida animal almacenada.
     * 
     * @param cantidadComidaAnimal Nueva cantidad de comida animal. Debe ser no negativa y no exceder la capacidad del almacén.
     */
    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
        if (cantidadComidaAnimal >= 0 && cantidadComidaAnimal <= capacidadAlmacen) {
            this.cantidadComidaAnimal = cantidadComidaAnimal;
        } else {
            System.out.println("Cantidad de comida animal inválida: debe estar entre 0 y " + capacidadAlmacen);
        }
    }

    /**
     * Obtiene la cantidad de comida vegetal almacenada.
     * 
     * @return Cantidad de comida vegetal.
     */
    public int getCantidadComidaVegetal() {
        return cantidadComidaVegetal;
    }

    /**
     * Establece la cantidad de comida vegetal almacenada.
     * 
     * @param cantidadComidaVegetal Nueva cantidad de comida vegetal. Debe ser no negativa y no exceder la capacidad del almacén.
     */
    public void setCantidadComidaVegetal(int cantidadComidaVegetal) {
        if (cantidadComidaVegetal >= 0 && cantidadComidaVegetal <= capacidadAlmacen) {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        } else {
            System.out.println("Cantidad de comida vegetal inválida: debe estar entre 0 y " + capacidadAlmacen);
        }
    }

    /**
     * Devuelve una representacion en cadena del estado del almacen central.
     * 
     * @return una cadena que describe el estado del almacen central.
     */
    @Override
    public String toString() {
        return "\nInformación del Almacén Central:" +
                "\n  Capacidad Total          : " + capacidadAlmacen +
                "\n  Comida Animal            : " + cantidadComidaAnimal + " (" + (cantidadComidaAnimal  > 0 ? ((cantidadComidaAnimal * 100.0) / capacidadAlmacen) : "0") + "% de la capacidad)" +
                "\n  Comida Vegetal           : " + cantidadComidaVegetal + " (" + (cantidadComidaVegetal > 0 ? ((cantidadComidaVegetal * 100.0) / capacidadAlmacen) : "0") + "% de la capacidad)";
    }
}
