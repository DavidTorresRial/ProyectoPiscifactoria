package peces.tipos.doble;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class TruchaArcoiris extends Pez implements Carnivoro {

    public TruchaArcoiris(boolean sexo) {
        super(sexo, AlmacenPropiedades.TRUCHA_ARCOIRIS);
    }
}
