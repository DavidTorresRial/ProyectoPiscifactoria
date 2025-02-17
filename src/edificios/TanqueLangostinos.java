package edificios;

import java.util.Random;

/**
 * Representa un tanque de langostinos.
 * Cada tanque produce entre 100 y 200 unidades tras 3 días de espera.
 */
public class TanqueLangostinos {

    /** Días de penalización por falta de alimento (máx. 3 días). */
    private int diasPenalizacion = 3;

    /** Raciones disponibles en el tanque. */
    private int racionesLocal = 0;

    /** Generador de números aleatorios. */
    private Random random = new Random();

    /** Referencia a la granja para consumir raciones de retroalimentación. */
    private GranjaLangostinos granja;

    /**
     * Crea un tanque de langostinos asociado a una granja.
     * @param granja La granja a la que pertenece este tanque.
     */
    public TanqueLangostinos(GranjaLangostinos granja) {
        this.granja = granja;
    }

    /**
     * Constructor que inicializa un tanque de langostinos con los valores especificados.
     *
     * @param granja la granja a la que pertenece el tanque.
     * @param racionesLocal las raciones locales disponibles en el tanque.
     * @param diasPenalizacion los días de penalización por falta de alimento.
     */
    public TanqueLangostinos(int racionesLocal, int diasPenalizacion) {
        this.racionesLocal = racionesLocal;
        this.diasPenalizacion = diasPenalizacion;
    }

    /**
     * Simula un día en el tanque: consume alimento y produce si es posible.
     * @return la cantidad producida en el día o 0 si no produce.
     */
    public int simularDia() {
        boolean alimentado = false;
        if (granja.consumirRacionRetroalimentacion()) {
            alimentado = true;
        } else if (racionesLocal > 0) {
            racionesLocal--;
            alimentado = true;
        }
        
        if (alimentado) {
            if (diasPenalizacion > 0) {
                diasPenalizacion--;
                return 0;
            }
            return random.nextInt(101) + 100;
        } else {
            if (diasPenalizacion < 3) {
                diasPenalizacion++;
            }
            return 0;
        }
    }
    
    /** Incrementa en 1 las raciones locales disponibles. */
    public void recargarRacion() {
        racionesLocal++;
    }
    
    /** 
     * Devuelve las raciones locales (comida) disponibles en el tanque.
     * @return raciones locales.
     */
    public int getRacionesLocal() {
        return racionesLocal;
    }
    
    /** 
     * Devuelve los días de penalización (descanso) actuales.
     * @return días de penalización.
     */
    public int getDiasPenalizacion() {
        return diasPenalizacion;
    }

    /**
     * Asigna la granja a este tanque.
     *
     * @param granja la granja a asignar
     */
    public void setGranja(GranjaLangostinos granja) {
        this.granja = granja;
    }

    /**
     * Devuelve el estado del tanque de langostinos.
     *
     * @return una cadena con el estado del tanque.
     */
    @Override
    public String toString() {
        return "\nInformación del Tanque de Langostinos:" +
            "\n  Raciones locales         : " + racionesLocal +
            "\n  Días de penalización     : " + diasPenalizacion;
    }
}