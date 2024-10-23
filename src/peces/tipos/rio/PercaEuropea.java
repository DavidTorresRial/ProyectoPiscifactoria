package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class PercaEuropea extends Pez implements Carnivoro, Activo {

    public PercaEuropea(boolean sexo) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }
}
