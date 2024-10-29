package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class SalmonChinook extends Pez implements Carnivoro {

    public SalmonChinook(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_CHINOOK);
    }
}
