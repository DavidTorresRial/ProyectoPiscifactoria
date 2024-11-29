package peces.tipos.rio;

import peces.propiedades.CarnivoroActivo;
import propiedades.AlmacenPropiedades;

/** Pez de río Perca Europea. */
public class PercaEuropea extends CarnivoroActivo {

    /**
     * Crea una instancia de PercaEuropea con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public PercaEuropea(boolean sexo) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }

    /**
     * Crea una copia del PercaEuropea con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de PercaEuropea con el sexo especificado.
     */
    @Override
    public PercaEuropea clonar(boolean nuevoSexo) {
        return new PercaEuropea(nuevoSexo);
    }
}