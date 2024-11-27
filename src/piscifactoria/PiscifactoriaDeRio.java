package piscifactoria;

import commons.Simulador;
import tanque.Tanque;

public class PiscifactoriaDeRio extends Piscifactoria {

    private int numTanques = 0;

    public PiscifactoriaDeRio(String nombre) {
        super(nombre);
        tanques.add(new Tanque(tanques.size() + 1, 25));
        capacidadMaximaComida = 25;
    }

    @Override
    public boolean upgradeFood() {
        int costoMejora = 50;
        int incrementoCapacidad = 25;
        int capacidadMaximaPermitida = 250;

        if (capacidadMaximaComida + incrementoCapacidad <= capacidadMaximaPermitida) {
            if (Simulador.monedas.gastarMonedas(costoMejora)) {
                capacidadMaximaComida += incrementoCapacidad;
                System.out.println("\nMejorada la piscifactoría " + nombre + " aumentando su capacidad de comida hasta un total de " + capacidadMaximaComida + " por " + costoMejora + " monedas.");
                return true;
            } else {
                System.out.println("No tienes suficientes monedas para mejorar el almacén de comida de la piscifactoría " + nombre + ".");
                return false;
            }
        } else {
            System.out.println("La capacidad máxima del almacén ya ha sido alcanzada para la piscifactoría " + nombre + ".");
            return false;
        }    
    }

    @Override
    public void addTanque() {
        int costoTanque = 150 * (tanques.size() + 1);
        if (Simulador.monedas.gastarMonedas(costoTanque)) {
            tanques.add(new Tanque(tanques.size() + 1, 25));
            numTanques++;
            System.out.println("\nComprado un tanque número " + numTanques + " de la piscifactoría " + nombre + ".");
        } else {
            System.out.println("\nNo tienes suficientes monedas para agregar un tanque de río. Necesitas " + costoTanque + " monedas.");
        }
    }    
}
