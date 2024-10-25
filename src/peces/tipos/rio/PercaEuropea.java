package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Activo;
import propiedades.AlmacenPropiedades;

public class PercaEuropea extends Pez implements Activo {

    /**
     * Constructor que inicializa una PercaEuropea con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public PercaEuropea(boolean sexo) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }
}
