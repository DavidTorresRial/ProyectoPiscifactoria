package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de mar Arenque del Atlántico. */
public class ArenqueDelAtlantico extends Pez implements Filtrador {

    /**
     * Crea una instancia de ArenqueDelAtlantico con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public ArenqueDelAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.ARENQUE_ATLANTICO);
    }

    /**
     * Crea una copia del ArenqueDelAtlantico con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de ArenqueDelAtlantico con el sexo especificado.
     */
    @Override
    public ArenqueDelAtlantico clonar(boolean nuevoSexo) {
        return new ArenqueDelAtlantico(nuevoSexo);
    }
}