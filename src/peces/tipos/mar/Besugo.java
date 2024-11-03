package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Besugo */
public class Besugo extends Pez implements Carnivoro {

    /**
     * Crea una instancia de Besugo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Besugo(boolean sexo) {
        super(sexo, AlmacenPropiedades.BESUGO);
    }
}