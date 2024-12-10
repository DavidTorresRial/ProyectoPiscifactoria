package peces.propiedades;

import java.util.Random;

import commons.Simulador;
import peces.Pez;
import propiedades.PecesDatos;

/** Clase abstracta que representa a un pez Omnivoro. */
public abstract class Omnivoro extends Pez{

    /** Generador de aleatorios. */
    Random rand = new Random();

    /**
     * Constructor de la clase Omnivoro.
     *
     * @param sexo  Sexo del pez (true para macho, false para hembra).
     * @param datos Datos específicos del pez.
     */
    public Omnivoro(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    /**
     * Alimenta al pez con comida animal o vegetal, ya sea local o del almacén central.
     *
     * @param cantidadComidaAnimal Cantidad local de comida animal disponible.
     * @param cantidadComidaVegetal Cantidad local de comida vegetal disponible.
     * @return El número de unidades de comida animal o vegetal a consumir.
     */
    @Override
    public int alimentar(int cantidadComidaAnimal, int cantidadComidaVegetal) {

        if (rand.nextDouble() > 0.25) {
            if (cantidadComidaAnimal > 0 || cantidadComidaVegetal > 0) {
                alimentado = true;
                return 1;
    
            } else if (Simulador.almacenCentral != null && (Simulador.almacenCentral.getCantidadComidaAnimal() > 0 || Simulador.almacenCentral.getCantidadComidaVegetal() > 0)) {
                if (Simulador.almacenCentral.getCantidadComidaAnimal() >= Simulador.almacenCentral.getCantidadComidaVegetal()) {
                    Simulador.almacenCentral.setCantidadComidaAnimal(Simulador.almacenCentral.getCantidadComidaAnimal() - 1);
                    alimentado = true;
                    return 0;

                } else {
                    Simulador.almacenCentral.setCantidadComidaVegetal(Simulador.almacenCentral.getCantidadComidaVegetal() - 1);
                    alimentado = true;
                    return 0;
                }
            } else {
                alimentado = false;
                return 0;
            }
        } else {
            alimentado = true;
            return 0;
        }
    }
}
