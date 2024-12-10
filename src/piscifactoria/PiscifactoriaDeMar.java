package piscifactoria;

import commons.Simulador;
import tanque.Tanque;

/** Representa una piscifactoría de peces de mar. */
public class PiscifactoriaDeMar extends Piscifactoria {

    /** Costo en monedas para mejorar la capacidad de comida. */
    private final int COSTO_MEJORA = 200;

    /** Incremento de capacidad de comida en cada mejora. */
    private final int INCREMENTO_CAPACIDAD = 100;

    /** Capacidad máxima de comida permitida. */
    private final int CAPACIDAD_MAXIMA_PERMITIDA = 1000;

    /**
     * Crea una nueva piscifactoría de mar con un tanque inicial.
     *
     * @param nombre Nombre de la piscifactoría.
     */
    public PiscifactoriaDeMar(String nombre) {
        super(nombre);
        tanques.add(new Tanque(tanques.size() + 1, 100));
        capacidadMaximaComida = 100;
    }

    /** Mejora la capacidad de comida de la piscifactoría si hay monedas suficientes y no se supera la capacidad máxima. */
    @Override
    public void upgradeFood() {
        if (capacidadMaximaComida + INCREMENTO_CAPACIDAD <= CAPACIDAD_MAXIMA_PERMITIDA) {
            if (Simulador.monedas.gastarMonedas(COSTO_MEJORA)) {
                capacidadMaximaComida += INCREMENTO_CAPACIDAD;
                System.out.println("\nMejorada la piscifactoría " + nombre + " aumentando su capacidad de comida hasta un total de " + capacidadMaximaComida + " por " + COSTO_MEJORA + " monedas.");
                Simulador.logger.log("Mejorada la piscifactoría " + nombre + " aumentando su capacidad de comida.");
                Simulador.transcriptor.transcribir("Mejorada la piscifactoría " + nombre + " aumentando su capacidad de comida hasta un total de " + capacidadMaximaComida + " por " + COSTO_MEJORA + " monedas.");
            } else {
                System.out.println("No tienes suficientes monedas para mejorar el almacén de comida de la piscifactoría " + nombre + ".");
            }
        } else {
            System.out.println("La capacidad máxima del almacén ya ha sido alcanzada para la piscifactoría " + nombre + ".");
        } 
    }

    /** Agrega un nuevo tanque a la piscifactoría aumentando el costo según la cantidad de tanques actuales. */
    @Override
    public void addTanque() {
        if (tanques.size() < numeroMaximoTanques) {
            int costoTanque = 150 * (tanques.size() + 1);
            if (Simulador.monedas.gastarMonedas(costoTanque)) {
                tanques.add(new Tanque(tanques.size() + 1, 100));
                System.out.println("\nComprado un tanque número " + tanques.size() + " de la piscifactoría " + nombre + ".");
                Simulador.logger.log("Comprado un tanque para la piscifactoría " + nombre + ".");
                Simulador.transcriptor.transcribir("Comprado un tanque número " + tanques.size() + " de la piscifactoría " + nombre + ".");
            } else {
                System.out.println("\nNo tienes suficientes monedas para agregar un tanque de río. Necesitas " + costoTanque + " monedas.");
            }
        } else {
            System.out.println("\nCapacidad máxima alcanzada: no se pueden añadir más tanques a \"" + nombre + "\".");
        }
    }
}