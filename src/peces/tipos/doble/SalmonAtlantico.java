package peces.tipos.doble;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Salmon Atlántico. */
public class SalmonAtlantico extends Pez implements Carnivoro {

    /**
     * Crea una instancia de SalmonAtlantico con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_ATLANTICO);
    }
}