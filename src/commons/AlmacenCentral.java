package commons;

/**
 * Clase que representa un almacén central para la distribución de alimentos en una piscifactoría.
<<<<<<< HEAD
 * Este almacén permite almacenar y distribuir comida de tipo vegetal y animal,
 * así como mejorar su capacidad mediante el gasto de monedas.
=======
>>>>>>> origin/DavidTrama
 */
public class AlmacenCentral {

    // ATRIBUTOS
<<<<<<< HEAD
=======
    private boolean comprado = false;
    
>>>>>>> origin/DavidTrama
    private int comidaVegetal;
    private int comidaAnimal;
    private int capacidadMaxVeg = 200;
    private int capacidadMaxAni = 200;
    private int nivelMejora = 1;
    private SistemaMonedas monedas;
    private static final int COSTO_CREACION = 2000;
<<<<<<< HEAD

    /**
     * Constructor privado para crear una instancia de AlmacenCentral. 
     * Solo accesible desde el método {@link #crearAlmacen(SistemaMonedas)}.
=======
    private static final int COSTO_MEJORA = 200;
    private static final int MEJORA_CAPACIDAD = 50;

    private enum TipoComida { VEGETAL, ANIMAL }

    /**
     * Constructor privado para crear una instancia de AlmacenCentral.
>>>>>>> origin/DavidTrama
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

<<<<<<< HEAD
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
=======
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
>>>>>>> origin/DavidTrama
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
<<<<<<< HEAD
        int totalNecesitadoVegetal = cantidadVegetal * numPiscifactorías;
        int totalNecesitadoAnimal = cantidadAnimal * numPiscifactorías;

        if (totalNecesitadoVegetal <= comidaVegetal) {
            comidaVegetal -= totalNecesitadoVegetal;
            System.out.println("Distribuidos " + totalNecesitadoVegetal + " de comida vegetal.");
=======
        if (numPiscifactorías < 1) {
            System.out.println("No hay piscifactorías disponibles para distribuir comida.");
            return;
        }

        if (cantidadVegetal * numPiscifactorías <= comidaVegetal) {
            comidaVegetal -= cantidadVegetal * numPiscifactorías;
            System.out.println("Distribuidos " + cantidadVegetal * numPiscifactorías + " de comida vegetal.");
>>>>>>> origin/DavidTrama
        } else {
            System.out.println("No hay suficiente comida vegetal para distribuir.");
        }

<<<<<<< HEAD
        if (totalNecesitadoAnimal <= comidaAnimal) {
            comidaAnimal -= totalNecesitadoAnimal;
            System.out.println("Distribuidos " + totalNecesitadoAnimal + " de comida animal.");
=======
        if (cantidadAnimal * numPiscifactorías <= comidaAnimal) {
            comidaAnimal -= cantidadAnimal * numPiscifactorías;
            System.out.println("Distribuidos " + cantidadAnimal * numPiscifactorías + " de comida animal.");
>>>>>>> origin/DavidTrama
        } else {
            System.out.println("No hay suficiente comida animal para distribuir.");
        }
    }

    /**
<<<<<<< HEAD
     * Realiza la distribución diaria de comida entre las piscifactorías.
     *
     * @param cantidadVegetal Cantidad de comida vegetal a distribuir por piscifactoría.
     * @param cantidadAnimal Cantidad de comida animal a distribuir por piscifactoría.
     * @param numPiscifactorías Número de piscifactorías.
     */
    public void distribuirDiariamente(int cantidadVegetal, int cantidadAnimal, int numPiscifactorías) {
        System.out.println("Distribución diaria de comida:");
        
        // Asegurarse de que haya al menos una piscifactoría
        if (numPiscifactorías < 1) {
            System.out.println("No hay piscifactorías disponibles para distribuir comida.");
            return;
        }
    
        // Intentar distribuir la comida
        distribuirComida(cantidadVegetal, cantidadAnimal, numPiscifactorías);
    }
    
    /**
=======
>>>>>>> origin/DavidTrama
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
<<<<<<< HEAD
        int costoMejora = 200;
        if (monedas.gastarMonedas(costoMejora)) {
            capacidadMaxAni += 50;
            capacidadMaxVeg += 50;
=======
        if (monedas.gastarMonedas(COSTO_MEJORA)) {
            capacidadMaxAni += MEJORA_CAPACIDAD;
            capacidadMaxVeg += MEJORA_CAPACIDAD;
>>>>>>> origin/DavidTrama
            nivelMejora++;
            System.out.println("Nivel del almacén mejorado: Nivel " + nivelMejora);
        } else {
            System.out.println("No hay suficientes monedas para mejorar.");
        }
    }
<<<<<<< HEAD
=======

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
>>>>>>> origin/DavidTrama
}
