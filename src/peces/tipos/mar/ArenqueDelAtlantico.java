package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class ArenqueDelAtlantico extends Pez implements Filtrador {

    /**
     * Constructor que inicializa un ArenqueDelAtlantico con su sexo específico.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public ArenqueDelAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.ARENQUE_ATLANTICO);
    }
}
