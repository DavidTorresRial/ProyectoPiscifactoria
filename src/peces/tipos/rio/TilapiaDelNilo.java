package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class TilapiaDelNilo extends Pez implements Filtrador {

    public TilapiaDelNilo(boolean sexo) {
        super(sexo, AlmacenPropiedades.TILAPIA_NILO);
    }
}
