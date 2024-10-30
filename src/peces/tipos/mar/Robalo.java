package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class Robalo extends Pez implements Carnivoro {

    /**
     * Constructor que inicializa un Robalo con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Robalo(boolean sexo) {
        super(sexo, AlmacenPropiedades.ROBALO);
    }
}
