package commons;

/** Representa una granja de langostinos, donde se crían y producen langostinos en tanques. */
public class GranjaLangostinos {

    /** Número de tanques en la granja. */
    private int numeroTanques;

    /** Producción actual de langostinos. */
    private int produccion;

    /** Cantidad de peces muertos disponibles para retroalimentación. */
    private int pecesMuertos;

    /** Costo de compra de la granja. */
    public static final int COSTO_COMPRA = 3000;

    /** Costo de mejora para añadir un tanque. */
    public static final int COSTO_MEJORA = 1500;

    /** Constructor que inicializa la granja sin tanques y sin producción. */
    public GranjaLangostinos() {
        this.numeroTanques = 0;
        this.produccion = 0;
        this.pecesMuertos = 0;
    }

    /** Agrega un langostino muerto a la granja, usado para retroalimentación. */
    public void agregarPezMuerto() {
        pecesMuertos++;
        System.out.println("Se ha agregado un pez muerto a la granja de langostinos. Total muertos: " + pecesMuertos);
    }

    /** Mejora la granja añadiendo un nuevo tanque si hay suficientes monedas disponibles. */
    public void mejorar() {
        if (Simulador.monedas.gastarMonedas(COSTO_MEJORA)) {
            numeroTanques++;
            System.out.println("\nMejorada la granja de langostinos: Añadido un nuevo tanque. Total: " + numeroTanques + " tanques.");
            Simulador.registro.registroMejoradaGranjaLangostinos(numeroTanques);
        } else {
            System.out.println("\nNo tienes suficientes monedas para mejorar la granja de langostinos.");
        }
    }

    /**
     * Devuelve una representación en cadena del estado de la granja de langostinos.
     * 
     * @return Una cadena que describe el estado actual de la granja.
     */
    @Override
    public String toString() {
        return "\nInformación de la Granja de Langostinos:" +
               "\n  Número de tanques        : " + numeroTanques +
               "\n  Producción actual        : " + produccion +
               "\n  Peces muertos            : " + pecesMuertos;
    }
}