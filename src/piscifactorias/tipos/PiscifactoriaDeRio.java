package piscifactorias.tipos;

import commons.SistemaMonedas;

import piscifactorias.Piscifactoria;
import tanque.Tanque;

public class PiscifactoriaDeRio extends Piscifactoria {

    public PiscifactoriaDeRio(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaAlmacenComida = 25;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        tanques.add(new Tanque(25));
        //TODO añadir meto de añadir tanques
    }
}
