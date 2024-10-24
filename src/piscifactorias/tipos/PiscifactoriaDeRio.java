package piscifactorias.tipos;

import commons.SistemaMonedas;
import peces.Pez;
import piscifactorias.Piscifactoria;
//import tanque.Tanque;
import tanque.Tanque;

public class PiscifactoriaDeRio<T extends Pez> extends Piscifactoria {

    public PiscifactoriaDeRio(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        this.capacidadMaximaAlmacenComida = 25;
        this.comidaVegetalActual = 25;
        this.comidaAnimalActual = 25;

        // Aquí se puede agregar un tanque si es necesario, o dejar que se agreguen desde la clase principal
        this.tanques.add(new Tanque<>(25));
    }

    // Otros métodos específicos para piscifactorías de río si los necesitas
}
