package peces.propiedades;

import java.util.Random;

import commons.Simulador;
import peces.Pez;
import propiedades.PecesDatos;

/** Clase que indica que un pez es carnívoro. */
public abstract class Carnivoro extends Pez {

    Random rand = new Random();

    public Carnivoro(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    @Override
    public void alimentar(int cantidadComidaAnimal, int cantidadComidaVegetal) {
        if (cantidadComidaAnimal > 0) {
            cantidadComidaAnimal--;
            alimentado = true;
        } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaAnimal() > 0) {
            if (Simulador.almacenCentral.getCantidadComidaAnimal() >= 1) {
                Simulador.almacenCentral.setCantidadComidaAnimal(Simulador.almacenCentral.getCantidadComidaAnimal() - 1);
                alimentado = true;
            } else {
                System.out.println("No hay suficiente comida animal.");
                alimentado = false;
            }
        }
    }
}
