package peces.propiedades;

import java.util.Random;

//import commons.Simulador;
import peces.Pez;
//import piscifactoria.Piscifactoria;
import propiedades.PecesDatos;

/** Clase que indica que un pez es un activo y carnívoro. */ // TODO CAmbiar a ActivoCarnivoro y juntar las logicas
public abstract class Activo extends Pez {

    Random rand = new Random();

    public Activo(boolean sexo, PecesDatos datos) { // TODO El único pez que hay activo es PercaEuropea no meter en el tanque porque no va a comer hazta hacer el metodo alimentar bine
        super(sexo, datos);
    }

    @Override
    public void alimentar() {
        if (alimentado) {
            if (rand.nextDouble() < 0.5) {
                /**
                 * if (pez instanceof Carnivoro && cantidadComidaAnimal > 0) {
                    cantidadComidaAnimal--; // Consume una unidad adicional si es posible
                } else if (pez instanceof Filtrador && cantidadComidaVegetal > 0) {
                    cantidadComidaVegetal--; // Consume una unidad adicional si es posible
                }
                 */
            }
        }
    }
}
