package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class Besugo extends Pez implements Carnivoro {

    /**
     * Constructor que inicializa un Besugo con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Besugo(boolean sexo) {
        super(sexo, AlmacenPropiedades.BESUGO);
    }
}
