package piscifactoria;

import commons.Simulador;
import tanque.Tanque;

public class PiscifactoriaDeMar extends Piscifactoria {

        private final int costoMejora = 200;
        private final int incrementoCapacidad = 100;
        private final int capacidadMaximaPermitida = 1000;

    public PiscifactoriaDeMar(String nombre) {
        super(nombre);
        tanques.add(new Tanque(tanques.size() + 1, 100));
        capacidadMaximaComida = 100;
    }

    @Override
    public void upgradeFood() {
        if (capacidadMaximaComida + incrementoCapacidad <= capacidadMaximaPermitida) {
            if (Simulador.monedas.gastarMonedas(costoMejora)) {
                capacidadMaximaComida += incrementoCapacidad;
                System.out.println("\nMejorada la piscifactoría " + nombre + " aumentando su capacidad de comida hasta un total de " + capacidadMaximaComida + " por " + costoMejora + " monedas.");
                Simulador.logger.log("Mejorado la piscifactoría " + nombre + " aumentando su capacidad de comida.");
            } else {
                System.out.println("No tienes suficientes monedas para mejorar el almacén de comida de la piscifactoría " + nombre + ".");
            }
        } else {
            System.out.println("La capacidad máxima del almacén ya ha sido alcanzada para la piscifactoría " + nombre + ".");
        } 
    }

    @Override
    public void addTanque() {
        int costoTanque = 150 * (tanques.size() + 1);
        if (Simulador.monedas.gastarMonedas(costoTanque)) {
            tanques.add(new Tanque(tanques.size() + 1, 100));
            System.out.println("\nComprado un tanque número " + tanques.size() + " de la piscifactoría " + nombre + ".");
            Simulador.logger.log("Añadiendo un tanque a la piscifactoría " + nombre + ".");
        } else {
            System.out.println("\nNo tienes suficientes monedas para agregar un tanque de río. Necesitas " + costoTanque + " monedas.");
        }
    }

}
