package piscifactorias.tipos;

import commons.SistemaMonedas;

import piscifactorias.Piscifactoria;
import tanque.Tanque;

public class PiscifactoriaDeMar extends Piscifactoria {

    public PiscifactoriaDeMar(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaAlmacenComida = 100;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        // Aquí se puede agregar un tanque si es necesario, o dejar que se agreguen desde la clase principal
        tanques.add(new Tanque(100, monedas));
    }
}
