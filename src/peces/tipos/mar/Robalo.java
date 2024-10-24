package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class Robalo extends Pez implements Carnivoro {

    public Robalo(boolean sexo) {
        super(sexo, AlmacenPropiedades.ROBALO);
    }
}
