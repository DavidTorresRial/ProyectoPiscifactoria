package peces.tipos.doble;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class SalmonAtlantico extends Pez implements Carnivoro {

    /**
     * Constructor que inicializa un SalmonAtlantico con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_ATLANTICO);
    }
}
