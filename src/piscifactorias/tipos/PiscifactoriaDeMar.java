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

        tanques.add(new Tanque(100));
    } 
    //TODO añadir meto de añadir tanques
}
