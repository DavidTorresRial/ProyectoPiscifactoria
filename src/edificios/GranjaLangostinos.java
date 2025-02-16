package edificios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import commons.Simulador;

/** Representa una granja de langostinos. */
public class GranjaLangostinos {
    
    /** Raciones disponibles por retroalimentación (1 ración = 50 unidades). */
    private int racionesRetroalimentacion;

    /** Lista de tanques de langostinos. */
    private List<TanqueLangostinos> tanques;

    /** Costo para añadir un tanque. */
    private final int COSTO_MEJORA = 1500;

    /** Crea una granja con un tanque inicial y sin producción. */
    public GranjaLangostinos() {
        this.racionesRetroalimentacion = 0;
        this.tanques = new ArrayList<>();
        tanques.add(new TanqueLangostinos());
    }

    public GranjaLangostinos(int racionesRetroalimentacion, List<TanqueLangostinos> tanques) {
        this.racionesRetroalimentacion = racionesRetroalimentacion;
        this.tanques = tanques;
    }

    /** Registra la muerte de un pez y suma una ración. */
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
    
    /** Reparte alimento a los tanques desde el almacén central. */
    private void recargarTanques() {
        for (int ronda = 0; ronda < 3; ronda++) {
            for (TanqueLangostinos tanque : tanques) {
                if (tanque.racionesLocal < 3) {
                    if (Simulador.almacenCentral.getCantidadComidaVegetal() >= 50) {
                        tanque.racionesLocal++;
                        int comidaDisponible = Simulador.almacenCentral.getCantidadComidaVegetal();
                        Simulador.almacenCentral.setCantidadComidaVegetal(comidaDisponible - 50);
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
        int produccion = 0;

        recargarTanques();
        for (TanqueLangostinos tanque : tanques) {
            int prodDia = tanque.simularDia();
            produccion += prodDia;
        }
        if (produccion > 0) {
            System.out.println("\nGranja de Langostinos produce " + produccion + " de comida animal.");
            Simulador.almacenCentral.añadirComidaAnimal(produccion);
        }
    }

    /** Devuelve el estado de la granja. */
    @Override
    public String toString() {
        return "\nGranja de Langostinos:" +
               "\n  Tanques: " + tanques.size() +
               "\n  Raciones retroalimentación: " + racionesRetroalimentacion;
    }

    /**
     * Representa un tanque de langostinos.
     * Cada tanque produce entre 100 y 200 unidades tras 3 días de espera.
     */
    public class TanqueLangostinos {

        /** Días para iniciar la producción. */
        private int diasParaProduccion = 3;

        /** Días de penalización por falta de alimento (máx. 3 días). */
        private int diasPenalizacion = 0;

        /** Raciones disponibles en el tanque. */
        private int racionesLocal = 0;

        /** Generador de números aleatorios. */
        private Random random = new Random();

        public TanqueLangostinos() {
        }

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
        
        /** Devuelve las raciones locales (comida) disponibles en el tanque. */
        public int getRacionesLocal() {
            return racionesLocal;
        }
        
        /** Devuelve los días de penalización (descanso) actuales. */
        public int getDiasPenalizacion() {
            return diasPenalizacion;
        }
    }

    public int getRacionesRetroalimentacion() {
        return racionesRetroalimentacion;
    }

    public List<TanqueLangostinos> getTanques() {
        return tanques;
    }
}