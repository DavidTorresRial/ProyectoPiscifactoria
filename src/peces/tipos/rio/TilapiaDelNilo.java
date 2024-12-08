package peces.tipos.rio;

import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Tilapia del Nilo. */
public class TilapiaDelNilo extends Filtrador {

    /**
     * Crea una instancia de TilapiaDelNilo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TilapiaDelNilo(boolean sexo) {
        super(sexo, AlmacenPropiedades.TILAPIA_NILO);
    }

    /**
     * Crea una copia del TilapiaDelNilo con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de TilapiaDelNilo con el sexo especificado.
     */
    @Override
    public TilapiaDelNilo clonar(boolean nuevoSexo) {
        return new TilapiaDelNilo(nuevoSexo);
    }
}