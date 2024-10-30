package piscifactorias.tipos;

import commons.SistemaMonedas;
import piscifactorias.Piscifactoria;
//import tanque.Tanque;
import tanque.Tanque;

public class PiscifactoriaDeRio extends Piscifactoria {

    public PiscifactoriaDeRio(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaAlmacenComida = 25;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        // Aquí se puede agregar un tanque si es necesario, o dejar que se agreguen desde la clase principal
<<<<<<< HEAD
        this.tanques.add(new Tanque(25, monedas));
=======
        tanques.add(new Tanque(25, monedas));
>>>>>>> origin/DavidTrama
    }
}
