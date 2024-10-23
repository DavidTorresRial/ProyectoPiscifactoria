package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class ArenqueDelAtlantico extends Pez implements Filtrador {

    public ArenqueDelAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.ARENQUE_ATLANTICO);
    }
}
