package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class TilapiaDelNilo extends Pez implements Filtrador {

    /**
     * Constructor que inicializa una TilapiaDelNilo con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TilapiaDelNilo(boolean sexo) {
        super(sexo, AlmacenPropiedades.TILAPIA_NILO);
    }
}
