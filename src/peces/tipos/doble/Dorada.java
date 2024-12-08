package peces.tipos.doble;

import peces.propiedades.Omnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Dorada. */
public class Dorada extends Omnivoro {

    /**
     * Crea una instancia de Dorada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Dorada(boolean sexo) {
        super(sexo, AlmacenPropiedades.DORADA);
    }

    /**
     * Crea una copia del Dorada con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de Dorada con el sexo especificado.
     */
    @Override
    public Dorada clonar(boolean nuevoSexo) {
        return new Dorada(nuevoSexo);
    }
}

