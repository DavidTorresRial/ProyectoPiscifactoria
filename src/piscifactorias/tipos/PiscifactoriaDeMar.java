package piscifactorias.tipos;

import commons.SistemaMonedas;
import piscifactorias.Piscifactoria;
import tanque.Tanque;

public class PiscifactoriaDeMar extends Piscifactoria {

    public PiscifactoriaDeMar(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        this.capacidadMaximaAlmacenComida = 100;
        this.comidaVegetalActual = 100; //TODO revisar (En el pdf la comida inicail de los de rio)
        this.comidaAnimalActual = 100;

        // Aquí se puede agregar un tanque si es necesario, o dejar que se agreguen desde la clase principal
        this.tanques.add(new Tanque(100, monedas));
    }
}
