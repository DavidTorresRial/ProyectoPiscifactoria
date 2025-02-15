package edificios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import commons.Simulador;

/** Representa una granja de langostinos. */
public class GranjaLangostinos {

    /** Producción total (pienso generado). */
    private int produccion;
    
    /** Raciones disponibles por retroalimentación (1 ración = 50 unidades). */
    private int racionesRetroalimentacion;

    /** Lista de tanques de langostinos. */
    private List<TanqueLangostinos> tanques;

    /** Costo de compra de la granja. */
    public static final int COSTO_COMPRA = 3000;

    /** Costo para añadir un tanque. */
    public static final int COSTO_MEJORA = 1500;

    /** Crea una granja con un tanque inicial y sin producción. */
    public GranjaLangostinos() {
        this.produccion = 0;
        this.racionesRetroalimentacion = 0;
        this.tanques = new ArrayList<>();
        tanques.add(new TanqueLangostinos());
    }

    public GranjaLangostinos(int produccion, int racionesRetroalimentacion, List<TanqueLangostinos> tanques) {
        this.produccion = produccion;
        this.racionesRetroalimentacion =racionesRetroalimentacion;
        this.tanques = tanques;
    }

    /** 
     * Registra la muerte de un pez y suma una ración.
     */
    public void agregarPezMuerto() {
        racionesRetroalimentacion++;
        System.out.println("Se ha agregado un pez muerto. Raciones disponibles: " + racionesRetroalimentacion);
    }

    /** Añade un tanque si hay suficientes monedas. */
    public void mejorar() {
        if (Simulador.monedas.gastarMonedas(COSTO_MEJORA)) {
            TanqueLangostinos nuevoTanque = new TanqueLangostinos();
            tanques.add(nuevoTanque);
            System.out.println("Granja de langostinos mejorada: Nuevo tanque añadido. Total: " + tanques.size() + " tanques.");
            Simulador.registro.registroMejoradaGranjaLangostinos(tanques.size());
        } else {
            System.out.println("\nNo tienes suficientes monedas para mejorar la granja de langostinos.");
        }
    }
    
    /**
     * Reparte alimento a los tanques desde el almacén central.
     * Cada tanque recibe raciones de 50 unidades hasta tener 3.
     */
    private void recargarTanques() {
        boolean recargoPosible = true;
        while (recargoPosible) {
            recargoPosible = false;
            for (TanqueLangostinos tanque : tanques) {
                if (tanque.racionesLocal < 3) {
                    if (Simulador.almacenCentral.getCantidadComidaAnimal() >= 50) {
                        int nuevaCantidad = Simulador.almacenCentral.getCantidadComidaAnimal() - 50;
                        Simulador.almacenCentral.setCantidadComidaAnimal(nuevaCantidad);
                        tanque.racionesLocal++;
                        recargoPosible = true;
                    }
                }
            }
        }
    }
    
    /**
     * Simula un día en la granja:
     * recarga tanques y produce alimento según disponibilidad.
     */
    public void simularDia() {
        recargarTanques();
        for (TanqueLangostinos tanque : tanques) {
            int prodDia = tanque.simularDia();
            produccion += prodDia;
        }
        if (produccion > 0) {
            System.out.println("Granja de Fitoplancton produce " + produccion + " de comida vegetal.");
        }
        if (Simulador.almacenCentral != null) {
            Simulador.almacenCentral.añadirComidaAnimal(produccion);
        }
    }

    /** Devuelve el estado de la granja. */
    @Override
    public String toString() {
        return "\nGranja de Langostinos:" +
               "\n  Tanques: " + tanques.size() +
               "\n  Producción: " + produccion +
               "\n  Raciones retroalimentación: " + racionesRetroalimentacion;
    }

    /**
     * Representa un tanque de langostinos.
     * Cada tanque produce entre 100 y 200 unidades tras 3 días de espera.
     */
    private class TanqueLangostinos {

        /** Días para iniciar la producción. */
        private int diasParaProduccion = 3;

        /** Días de penalización por falta de alimento (máx. 3 días). */
        private int diasPenalizacion = 0;

        /** Raciones disponibles en el tanque. */
        private int racionesLocal = 3;

        /** Generador de números aleatorios. */
        private Random random = new Random();

        /**
         * Simula un día en el tanque: consume alimento y produce si es posible.
         * @return la cantidad producida en el día o 0 si no produce.
         */
        public int simularDia() {
            boolean alimentado = false;
            if (racionesRetroalimentacion > 0) {
                racionesRetroalimentacion--;
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
                if (diasParaProduccion > 0) {
                    diasParaProduccion--;
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
    }

    public int getProduccion() { //TODO Vaya horas para tener que pensar...
        return produccion;
    }

    public void setProduccion(int produccion) {
        this.produccion = produccion;
    }

    public int getRacionesRetroalimentacion() {
        return racionesRetroalimentacion;
    }

    public void setRacionesRetroalimentacion(int racionesRetroalimentacion) {
        this.racionesRetroalimentacion = racionesRetroalimentacion;
    }

    public List<TanqueLangostinos> getTanques() {
        return tanques;
    }

    public void setTanques(List<TanqueLangostinos> tanques) {
        this.tanques = tanques;
    }
}