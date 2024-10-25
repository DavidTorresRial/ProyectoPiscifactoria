package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class SalmonChinook extends Pez implements Carnivoro {

    /**
     * Constructor que inicializa un SalmonChinook con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonChinook(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_CHINOOK);
    }
}
