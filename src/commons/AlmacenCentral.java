package commons;

/**
 * Clase que representa un almacén central para la distribución de alimentos en una piscifactoría.
 */
public class AlmacenCentral {

    // ATRIBUTOS
    private boolean comprado = false;
    
    private int comidaVegetal;
    private int comidaAnimal;
    private int capacidadMaxVeg = 200;
    private int capacidadMaxAni = 200;
    private int nivelMejora = 1;
    private SistemaMonedas monedas;
    private static final int COSTO_CREACION = 2000;
    private static final int COSTO_MEJORA = 200;
    private static final int MEJORA_CAPACIDAD = 50;

    private enum TipoComida { VEGETAL, ANIMAL }

    /**
     * Constructor privado para crear una instancia de AlmacenCentral.
     *
     * @param sistemaMonedas Sistema de monedas asociado para gestionar los costos.
     */
    private AlmacenCentral(SistemaMonedas sistemaMonedas) {
        this.comidaVegetal = 0;
        this.comidaAnimal = 0;
        this.monedas = sistemaMonedas;
    }

    /**
     * Método estático para crear un nuevo almacén central. 
     * Requiere 2000 monedas para crearse.
     *
     * @param sistemaMonedas Sistema de monedas usado para el costo de creación.
     * @return una instancia de AlmacenCentral si hay monedas suficientes, null en caso contrario.
     */
    public static AlmacenCentral crearAlmacen(SistemaMonedas sistemaMonedas) {
        if (sistemaMonedas.gastarMonedas(COSTO_CREACION)) {
            System.out.println("Almacén central creado exitosamente por " + COSTO_CREACION + " monedas.");
            return new AlmacenCentral(sistemaMonedas);
        } else {
            System.out.println("No hay suficientes monedas para crear el almacén central.");
            return null;
        }
    }

    /**
     * Agrega una cantidad especificada de comida al almacén.
     *
     * @param tipo Tipo de comida (vegetal o animal).
     * @param cantidad Cantidad de comida a agregar.
     */
    public void agregarComida(TipoComida tipo, int cantidad) {
        if (cantidad < 0) {
            System.out.println("No se puede añadir una cantidad negativa.");
            return;
        }

        if (tipo == TipoComida.VEGETAL) {
            comidaVegetal = Math.min(comidaVegetal + cantidad, capacidadMaxVeg);
            System.out.println("Añadida " + cantidad + " de comida vegetal.");
        } else if (tipo == TipoComida.ANIMAL) {
            comidaAnimal = Math.min(comidaAnimal + cantidad, capacidadMaxAni);
            System.out.println("Añadida " + cantidad + " de comida animal.");
        }
    }

    /**
     * Distribuye una cantidad especificada de comida vegetal y animal entre varias piscifactorías.
     * Si no hay suficiente comida, se informa de la falta de recursos.
     *
     * @param cantidadVegetal Cantidad de comida vegetal a distribuir por piscifactoría.
     * @param cantidadAnimal Cantidad de comida animal a distribuir por piscifactoría.
     * @param numPiscifactorías Número de piscifactorías.
     */
    public void distribuirComida(int cantidadVegetal, int cantidadAnimal, int numPiscifactorías) {
        if (numPiscifactorías < 1) {
            System.out.println("No hay piscifactorías disponibles para distribuir comida.");
            return;
        }

        if (cantidadVegetal * numPiscifactorías <= comidaVegetal) {
            comidaVegetal -= cantidadVegetal * numPiscifactorías;
            System.out.println("Distribuidos " + cantidadVegetal * numPiscifactorías + " de comida vegetal.");
        } else {
            System.out.println("No hay suficiente comida vegetal para distribuir.");
        }

        if (cantidadAnimal * numPiscifactorías <= comidaAnimal) {
            comidaAnimal -= cantidadAnimal * numPiscifactorías;
            System.out.println("Distribuidos " + cantidadAnimal * numPiscifactorías + " de comida animal.");
        } else {
            System.out.println("No hay suficiente comida animal para distribuir.");
        }
    }

    /**
     * Muestra el estado actual del almacén, incluyendo cantidades de comida y capacidades máximas.
     */
    public void mostrarEstado() {
        System.out.println("Estado del Almacén Central:");
    
        // Muestra el estado de la comida vegetal
        System.out.println("Comida Vegetal: " + comidaVegetal + " / " + capacidadMaxVeg + " (" + (capacidadMaxVeg > 0 ? (comidaVegetal * 100 / capacidadMaxVeg) : 0) + "%)");
    
        // Muestra el estado de la comida animal
        System.out.println("Comida Animal: " + comidaAnimal + " / " + capacidadMaxAni + " (" + (capacidadMaxAni > 0 ? (comidaAnimal * 100 / capacidadMaxAni) : 0) + "%)");
    }

    /**
     * Mejora la capacidad máxima del almacén en ambos tipos de comida.
     * Requiere 200 monedas para incrementar la capacidad en 50 unidades.
     */
    public void mejora() {
        if (monedas.gastarMonedas(COSTO_MEJORA)) {
            capacidadMaxAni += MEJORA_CAPACIDAD;
            capacidadMaxVeg += MEJORA_CAPACIDAD;
            nivelMejora++;
            System.out.println("Nivel del almacén mejorado: Nivel " + nivelMejora);
        } else {
            System.out.println("No hay suficientes monedas para mejorar.");
        }
    }

    // GETTERS Y SETTERS

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public int getComidaVegetal() {
        return comidaVegetal;
    }

    

    public void setComidaVegetal(int comidaVegetal) {
        if (comidaVegetal < 0) {
            System.out.println("No se puede establecer una cantidad negativa de comida vegetal.");
        } else {
            this.comidaVegetal = Math.min(comidaVegetal, capacidadMaxVeg);
        }
    }

    public int getComidaAnimal() {
        return comidaAnimal;
    }

    public void setComidaAnimal(int comidaAnimal) {
        if (comidaAnimal < 0) {
            System.out.println("No se puede establecer una cantidad negativa de comida animal.");
        } else {
            this.comidaAnimal = Math.min(comidaAnimal, capacidadMaxAni);
        }
    }

    public int getCapacidadMaxVeg() {
        return capacidadMaxVeg;
    }

    public void setCapacidadMaxVeg(int capacidadMaxVeg) {
        this.capacidadMaxVeg = capacidadMaxVeg;
    }

    public int getCapacidadMaxAni() {
        return capacidadMaxAni;
    }

    public void setCapacidadMaxAni(int capacidadMaxAni) {
        this.capacidadMaxAni = capacidadMaxAni;
    }

    public int getNivelMejora() {
        return nivelMejora;
    }

    public void setNivelMejora(int nivelMejora) {
        this.nivelMejora = nivelMejora;
    }
}
