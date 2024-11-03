package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Robalo */
public class Robalo extends Pez implements Carnivoro {

    /**
     * Crea una instancia de Robalo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Robalo(boolean sexo) {
        super(sexo, AlmacenPropiedades.ROBALO);
    }
}