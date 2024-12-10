package peces.propiedades;

import peces.Pez;
import propiedades.PecesDatos;
import commons.Simulador;

/** Clase abstracta que representa a un pez Carnívoro. */
public abstract class Carnivoro extends Pez {

    /**
     * Constructor de la clase Carnivoro.
     *
     * @param sexo  Sexo del pez (true para macho, false para hembra).
     * @param datos Datos específicos del pez.
     */
    public Carnivoro(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    /**
     * Alimenta al pez usando comida animal local o del almacén central.
     *
     * @param cantidadComidaAnimal Cantidad local de comida animal disponible.
     * @param cantidadComidaVegetal Cantidad local de comida vegetal (no usada).
     * @return El número de unidades de comida animal a consumir.
     */
    @Override
    public int alimentar(int cantidadComidaAnimal, int cantidadComidaVegetal) {

        if (cantidadComidaAnimal > 0) {
            alimentado = true;
            return 1;

        } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaAnimal() > 0) {
            Simulador.almacenCentral.setCantidadComidaAnimal(Simulador.almacenCentral.getCantidadComidaAnimal() - 1);
            alimentado = true;
            return 0;

        } else {
            alimentado = false;
            return 0;
        }
    }
}
