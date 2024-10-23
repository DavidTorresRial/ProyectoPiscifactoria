package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class LenguadoEuropeo extends Pez implements Carnivoro {

    public LenguadoEuropeo(boolean sexo) {
        super(sexo, AlmacenPropiedades.LENGUADO_EUROPEO);
    }
}
