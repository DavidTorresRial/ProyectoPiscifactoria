package commons;

// CLASE QUE REPRESENTA UN ALMACEN CENTRAL
public class AlmacenCentral {

    // ATRIBUTOS
    private int comidaVegetal;
    private int comidaAnimal;
    private int capacidadMaxVeg = 200;
    private int capacidadMaxAni = 200;
    private int nivelMejora = 1;
    private SistemaMonedas monedas;

    // CONSTRUCTOR
    public AlmacenCentral(SistemaMonedas sistemaMonedas) {
        this.comidaVegetal = 0;
        this.comidaAnimal = 0;
        this.monedas = sistemaMonedas;
    }

    // GETTERS Y SETTERS
    /**
     * @return int return the comidaVegetal
     */
    public int getComidaVegetal() {
        return comidaVegetal;
    }

    /**
     * @param comidaVegetal the comidaVegetal to set
     */
    public void setComidaVegetal(int comidaVegetal) {
        this.comidaVegetal = comidaVegetal;
    }

    /**
     * @return int return the comidaAnimal
     */
    public int getComidaAnimal() {
        return comidaAnimal;
    }

    /**
     * @param comidaAnimal the comidaAnimal to set
     */
    public void setComidaAnimal(int comidaAnimal) {
        this.comidaAnimal = comidaAnimal;
    }

    public int getNivelMejora() {
        return nivelMejora;
    }

    public void setNivelMejora(int nivelMejora) {
        this.nivelMejora = nivelMejora;
    }

    // METODOS DE LA CLASE

    public void agregarComidaVeg(int cantidad) { //TODO IMPLEMENTAR SISTEMA MONEDAS
        if (cantidad < 0 ) {
            System.out.println("No es posible añadir una cantidad de comida negativa");
            return;
        }
        comidaVegetal += cantidad;
        if (comidaVegetal > capacidadMaxVeg) {
            comidaVegetal = capacidadMaxVeg;
            System.out.println("Capacidad máxima alcanzada para la comida vegetal.");
        }
    }

    
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


     // Método para distribuir comida a las piscifactorías
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

    public void mostrarEstado() {
        System.out.println("Estado del Almacén Central:");
        System.out.println("Comida Vegetal: " + comidaVegetal + " / " + capacidadMaxVeg);
        System.out.println("Comida Animal: " + comidaAnimal + " / " + capacidadMaxAni);
    }


    
    public void mejora() {
        int costoMejora = 200; // Costo de la mejora
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
