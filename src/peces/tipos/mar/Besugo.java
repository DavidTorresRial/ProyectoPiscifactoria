package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class Besugo extends Pez implements Carnivoro {

    public Besugo(boolean sexo) {
        super(sexo, AlmacenPropiedades.BESUGO);
    }
}
