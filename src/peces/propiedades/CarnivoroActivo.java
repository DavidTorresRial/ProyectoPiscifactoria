package peces.propiedades;

import java.util.Random;

import peces.Pez;
import propiedades.PecesDatos;
import commons.Simulador;

/** Clase abstracta que representa a un pez Activo. */
public abstract class CarnivoroActivo extends Pez {

    /** Generador de aleatorios. */
    Random rand = new Random();
    
    /**
     * Constructor de la clase CarnivoroActivo.
     *
     * @param sexo  Sexo del pez (true para macho, false para hembra).
     * @param datos Datos específicos del pez.
     */
    public CarnivoroActivo(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    /**
     * Alimenta al pez, consumiendo más comida si es activo.
     *
     * @param cantidadComidaAnimal Cantidad local de comida animal disponible.
     * @param cantidadComidaVegetal Cantidad local de comida vegetal (no usada).
     * @return El número de unidades de comida animal a consumir.
     */
    @Override
    public int alimentar(int cantidadComidaAnimal, int cantidadComidaVegetal) {

        if (rand.nextDouble() < 0.5) {
            if (cantidadComidaAnimal > 1) {
                alimentado = true;
                return 2;
                
            } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaAnimal() > 1) {
                Simulador.almacenCentral.setCantidadComidaAnimal(Simulador.almacenCentral.getCantidadComidaAnimal() - 2);
                alimentado = true;
                return 0;

            } else {
                alimentado = false;
                return 0;
            }
        } else {
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
}
