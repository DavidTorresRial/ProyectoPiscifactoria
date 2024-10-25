package commons;

/**
 * Clase que representa un almacén central para la distribución de alimentos en una piscifactoría.
 * Este almacén permite almacenar y distribuir comida de tipo vegetal y animal,
 * así como mejorar su capacidad mediante el gasto de monedas.
 */
public class AlmacenCentral {

    // ATRIBUTOS
    private int comidaVegetal;
    private int comidaAnimal;
    private int capacidadMaxVeg = 200;
    private int capacidadMaxAni = 200;
    private int nivelMejora = 1;
    private SistemaMonedas monedas;
    private static final int COSTO_CREACION = 2000;

    /**
     * Constructor privado para crear una instancia de AlmacenCentral. 
     * Solo accesible desde el método {@link #crearAlmacen(SistemaMonedas)}.
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

    // GETTERS Y SETTERS

    /**
     * Obtiene la cantidad de comida vegetal almacenada.
     * @return cantidad de comida vegetal.
     */
    public int getComidaVegetal() {
        return comidaVegetal;
    }

    /**
     * Establece la cantidad de comida vegetal almacenada.
     * @param comidaVegetal la cantidad de comida vegetal.
     */
    public void setComidaVegetal(int comidaVegetal) {
        this.comidaVegetal = comidaVegetal;
    }

    /**
     * Obtiene la cantidad de comida animal almacenada.
     * @return cantidad de comida animal.
     */
    public int getComidaAnimal() {
        return comidaAnimal;
    }

    /**
     * Establece la cantidad de comida animal almacenada.
     * @param comidaAnimal la cantidad de comida animal.
     */
    public void setComidaAnimal(int comidaAnimal) {
        this.comidaAnimal = comidaAnimal;
    }

    /**
     * Obtiene el nivel de mejora actual del almacén.
     * @return el nivel de mejora.
     */
    public int getNivelMejora() {
        return nivelMejora;
    }

    /**
     * Establece el nivel de mejora del almacén.
     * @param nivelMejora el nivel de mejora.
     */
    public void setNivelMejora(int nivelMejora) {
        this.nivelMejora = nivelMejora;
    }

    /**
     * Obtiene la capacidad máxima de comida vegetal.
     * @return capacidad máxima de comida vegetal.
     */
    public int getCapacidadMaxVeg() {
        return capacidadMaxVeg;
    }

    /**
     * Obtiene la capacidad máxima de comida animal.
     * @return capacidad máxima de comida animal.
     */
    public int getCapacidadMaxAni() {
        return capacidadMaxAni;
    }

    // MÉTODOS DE LA CLASE

    /**
     * Agrega una cantidad especificada de comida vegetal al almacén.
     * Si la cantidad total excede la capacidad máxima, se ajusta al máximo.
     *
     * @param cantidad Cantidad de comida vegetal a agregar.
     */
    public void agregarComidaVeg(int cantidad) {
        if (cantidad < 0) {
            System.out.println("No es posible añadir una cantidad de comida negativa.");
            return;
        }
        comidaVegetal += cantidad;
        if (comidaVegetal > capacidadMaxVeg) {
            comidaVegetal = capacidadMaxVeg;
            System.out.println("Capacidad máxima alcanzada para la comida vegetal.");
        }
    }

    /**
     * Agrega una cantidad especificada de comida animal al almacén.
     * Si la cantidad total excede la capacidad máxima, se ajusta al máximo.
     *
     * @param cantidad Cantidad de comida animal a agregar.
     */
    public void agregarComidaAnimal(int cantidad) {
        if (cantidad < 0) {
            System.out.println("No se puede agregar una cantidad negativa.");
            return;
        }

        comidaAnimal += cantidad;
        if (comidaAnimal > capacidadMaxAni) {
            comidaAnimal = capacidadMaxAni;
            System.out.println("Capacidad máxima alcanzada para la comida animal.");
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
        int totalNecesitadoVegetal = cantidadVegetal * numPiscifactorías;
        int totalNecesitadoAnimal = cantidadAnimal * numPiscifactorías;

        if (totalNecesitadoVegetal <= comidaVegetal) {
            comidaVegetal -= totalNecesitadoVegetal;
            System.out.println("Distribuidos " + totalNecesitadoVegetal + " de comida vegetal.");
        } else {
            System.out.println("No hay suficiente comida vegetal para distribuir.");
        }

        if (totalNecesitadoAnimal <= comidaAnimal) {
            comidaAnimal -= totalNecesitadoAnimal;
            System.out.println("Distribuidos " + totalNecesitadoAnimal + " de comida animal.");
        } else {
            System.out.println("No hay suficiente comida animal para distribuir.");
        }
    }

    /**
     * Realiza la distribución diaria de comida entre las piscifactorías.
     *
     * @param cantidadVegetal Cantidad de comida vegetal a distribuir por piscifactoría.
     * @param cantidadAnimal Cantidad de comida animal a distribuir por piscifactoría.
     * @param numPiscifactorías Número de piscifactorías.
     */
    public void distribuirDiariamente(int cantidadVegetal, int cantidadAnimal, int numPiscifactorías) {
        System.out.println("Distribución diaria de comida:");
        distribuirComida(cantidadVegetal, cantidadAnimal, numPiscifactorías);
    }

    /**
     * Muestra el estado actual del almacén, incluyendo cantidades de comida y capacidades máximas.
     */
    public void mostrarEstado() {
        System.out.println("Estado del Almacén Central:");
        System.out.println("Comida Vegetal: " + comidaVegetal + " / " + capacidadMaxVeg);
        System.out.println("Comida Animal: " + comidaAnimal + " / " + capacidadMaxAni);
    }

    /**
     * Mejora la capacidad máxima del almacén en ambos tipos de comida.
     * Requiere 200 monedas para incrementar la capacidad en 50 unidades.
     */
    public void mejora() {
        int costoMejora = 200;
        if (monedas.gastarMonedas(costoMejora)) {
            capacidadMaxAni += 50;
            capacidadMaxVeg += 50;
            nivelMejora++;
            System.out.println("Nivel del almacén mejorado: Nivel " + nivelMejora);
        } else {
            System.out.println("No hay suficientes monedas para mejorar.");
        }
    }
}
