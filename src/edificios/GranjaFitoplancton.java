package edificios;

import commons.Simulador;

/** Representa una granja de fitoplancton que produce comida vegetal en ciclos de días. */
public class GranjaFitoplancton {
    
    /** Número de tanques en la granja. */
    private int numeroTanques;

    /** Ciclo actual de producción. */
    private int ciclo;

    /** Producción de comida vegetal por tanque. */
    public static final int PRODUCCION_POR_TANQUE = 500;

    /** Costo de mejora para añadir un tanque. */
    public static final int COSTO_MEJORA = 2500;

    /** Duración del ciclo de producción en días. */
    public static final int CICLO_DIAS = 5;

    /** Constructor que inicializa la granja con un tanque y ciclo en 0. */
    public GranjaFitoplancton() {
        this.numeroTanques = 1;
        this.ciclo = 0;
    }

    /**
     * Crea una granja con un número específico de tanques y un ciclo inicial.
     * 
     * @param numeroTanques Cantidad inicial de tanques.
     * @param ciclo Ciclo de producción inicial.
     */
    public GranjaFitoplancton(int numeroTanques, int ciclo) {
        this.numeroTanques = numeroTanques;
        this.ciclo = ciclo;
    }

    /**
     * Actualiza el ciclo de producción de la granja y añade comida vegetal al almacén si se cumple el ciclo.
     * 
     * @param almacen Almacén central donde se almacena la producción de comida vegetal.
     */
    public void nextDay(AlmacenCentral almacen) {
        ciclo++;
        if (ciclo > CICLO_DIAS) {
            int produccionFitoplancton = numeroTanques * PRODUCCION_POR_TANQUE;
            System.out.println("\nGranja de Fitoplancton produce " + produccionFitoplancton + " de comida vegetal.");
            almacen.añadirComidaVegetal(produccionFitoplancton);
            ciclo = 0;
        }
    }

    /** Mejora la granja añadiendo un nuevo tanque si hay suficientes monedas disponibles. */
    public void mejorar() {
        if (Simulador.monedas.gastarMonedas(COSTO_MEJORA)) {
            numeroTanques++;
            ciclo = 0;
            System.out.println("\nGranja de fitoplancton mejorada: Nuevo tanque añadido. Total: " + numeroTanques + " tanques.");
            Simulador.registro.registroMejoradaGranjaFitoplancton(numeroTanques);
        } else {
            System.out.println("\nNo tienes suficientes monedas para comprar la granja de fitoplancton.");
        }
    }

    /**
     * Devuelve el número de tanques de la granja.
     * 
     * @return Número de tanques actuales.
     */
    public int getNumeroTanques() {
        return numeroTanques;
    }

    /**
     * Devuelve el ciclo actual de producción.
     * 
     * @return Días transcurridos en el ciclo actual.
     */
    public int getCiclo() {
        return ciclo;
    }

    /**
     * Devuelve una representación en cadena del estado de la granja de fitoplancton.
     * 
     * @return Una cadena que describe el estado actual de la granja.
     */
    @Override
    public String toString() {
        return "\nInformación de la Granja de Fitoplancton:" +
            "\n  Número de tanques        : " + numeroTanques +
            "\n  Producción por tanque    : " + PRODUCCION_POR_TANQUE +
            "\n  Producción total         : " + (numeroTanques * PRODUCCION_POR_TANQUE) +
            "\n  Ciclo actual             : " + ciclo + " / " + CICLO_DIAS + " días";
    }
}