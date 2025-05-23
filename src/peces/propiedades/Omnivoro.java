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
     * Constructor de la clase Omnivoro con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public Omnivoro(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado, PecesDatos datos) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, datos);
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
    
            } else if (Simulador.instance.almacenCentral != null && (Simulador.instance.almacenCentral.getCantidadComidaAnimal() > 0 || Simulador.instance.almacenCentral.getCantidadComidaVegetal() > 0)) {
                if (Simulador.instance.almacenCentral.getCantidadComidaAnimal() >= Simulador.instance.almacenCentral.getCantidadComidaVegetal()) {
                    Simulador.instance.almacenCentral.setCantidadComidaAnimal(Simulador.instance.almacenCentral.getCantidadComidaAnimal() - 1);
                    alimentado = true;
                    return 0;

                } else {
                    Simulador.instance.almacenCentral.setCantidadComidaVegetal(Simulador.instance.almacenCentral.getCantidadComidaVegetal() - 1);
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