package peces.tipos.doble;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Salmon Atlántico. */
public class SalmonAtlantico extends Carnivoro {

    /**
     * Crea una instancia de SalmonAtlantico con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_ATLANTICO);
    }

    /**
     * Crea una copia del SalmonAtlantico con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de SalmonAtlantico con el sexo especificado.
     */
    @Override
    public SalmonAtlantico clonar(boolean nuevoSexo) {
        return new SalmonAtlantico(nuevoSexo);
    }
}