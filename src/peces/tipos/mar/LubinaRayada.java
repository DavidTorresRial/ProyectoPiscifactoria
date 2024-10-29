package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class LubinaRayada extends Pez implements Carnivoro {

    public LubinaRayada(boolean sexo) {
        super(sexo, AlmacenPropiedades.LUBINA_RAYADA);
    }
}
