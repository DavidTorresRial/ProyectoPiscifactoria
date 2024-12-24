package peces.propiedades;

import java.util.Random;

import peces.Pez;
import propiedades.PecesDatos;
import commons.Simulador;

/** Clase abstracta que representa a un pez Filtrador. */
public abstract class Filtrador extends Pez {

    /** Generador de aleatorios. */
    Random rand = new Random();

    /**
     * Constructor de la clase Filtrador.
     *
     * @param sexo  Sexo del pez (true para macho, false para hembra).
     * @param datos Datos específicos del pez.
     */
    public Filtrador(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    /**
     * Constructor de la clase Filtrador con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public Filtrador(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado, PecesDatos datos) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, datos);
    }

    /**
     * Alimenta al pez usando comida vegetal local o del almacén central.
     *
     * @param cantidadComidaAnimal Cantidad local de comida animal (no usada).
     * @param cantidadComidaVegetal Cantidad local de comida vegetal disponible.
     * @return El número de unidades de comida vegetal a consumir.
     */
    @Override
    public int alimentar(int cantidadComidaAnimal, int cantidadComidaVegetal) {

        if (rand.nextDouble() < 0.5) {
            if (cantidadComidaVegetal > 0) {
                alimentado = true;
                return 1;

            } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaVegetal() > 0) {
                Simulador.almacenCentral.setCantidadComidaVegetal(Simulador.almacenCentral.getCantidadComidaVegetal() - 1);
                alimentado = true;
                return 0;

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
