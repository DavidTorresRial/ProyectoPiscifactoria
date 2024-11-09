package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Tilapia del Nilo. */
public class TilapiaDelNilo extends Pez implements Filtrador {

    /**
     * Crea una instancia de TilapiaDelNilo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TilapiaDelNilo(boolean sexo) {
        super(sexo, AlmacenPropiedades.TILAPIA_NILO);
    }
}