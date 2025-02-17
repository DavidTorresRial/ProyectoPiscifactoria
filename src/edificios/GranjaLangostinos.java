package edificios;

import java.util.ArrayList;
import java.util.List;
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
        tanques.add(new TanqueLangostinos(this));
    }

    /**
     * Constructor que inicializa la granja con las raciones y la lista de tanques especificadas.
     *
     * @param racionesRetroalimentacion el número de raciones de retroalimentación
     * @param tanques la lista de tanques de langostinos
     */
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
            tanques.add(new TanqueLangostinos(this));
            System.out.println("Granja de langostinos mejorada: Nuevo tanque añadido. Total: " + tanques.size() + " tanques.");
            Simulador.instance.registro.registroMejoradaGranjaLangostinos(tanques.size());
        } else {
            System.out.println("\nNo tienes suficientes monedas para mejorar la granja de langostinos.");
        }
    }
    
    /** Reparte alimento a los tanques desde el almacén central. */
    private void recargarTanques(AlmacenCentral almacenCentral) {
        for (int ronda = 0; ronda < 3; ronda++) {
            for (TanqueLangostinos tanque : tanques) {
                if (tanque.getRacionesLocal() < 3) {
                    int comidaDisponible = almacenCentral.getCantidadComidaVegetal();
                    if (comidaDisponible >= 50) {
                        tanque.recargarRacion();
                        almacenCentral.setCantidadComidaVegetal(comidaDisponible - 50);
                    }
                }
            }
        }
    }

    /** Recarga tanques y produce alimento según disponibilidad. */
    public void nextDay(AlmacenCentral almacenCentral) {
        int produccion = 0;
    
        for (TanqueLangostinos tanque : tanques) {
            int prodDia = tanque.simularDia();
            produccion += prodDia;
        }
        if (produccion > 0) {
            System.out.println("\nGranja de Langostinos produce " + produccion + " de comida animal.");
            almacenCentral.añadirComidaAnimal(produccion);
        }
        recargarTanques(almacenCentral);
    }

    /**
     * Devuelve el número de raciones de retroalimentación.
     * @return Número de raciones de retroalimentación.
     */
    public int getRacionesRetroalimentacion() {
        return racionesRetroalimentacion;
    }
    
    /**
     * Consume una ración de retroalimentación si hay disponible.
     * 
     * @return true si se consumió una ración, false si no hay disponibles.
     */
    public boolean consumirRacionRetroalimentacion() {
        if (racionesRetroalimentacion > 0) {
            racionesRetroalimentacion--;
            return true;
        }
        return false;
    }

    /**
     * Devuelve la lista de tanques de langostinos.
     * @return Lista de tanques de langostinos.
     */
    public List<TanqueLangostinos> getTanques() {
        return tanques;
    }

    /**
     * Devuelve el estado de la granja de langostinos.
     *
     * @return una cadena con el estado de la granja.
     */
    @Override
    public String toString() {
        return "\nInformación de la Granja de Langostinos:" +
            "\n  Número de tanques               : " + tanques.size() +
            "\n  Raciones de retroalimentación   : " + racionesRetroalimentacion;
    }
}