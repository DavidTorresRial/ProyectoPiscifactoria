package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Activo;
import propiedades.AlmacenPropiedades;

/** Pez de río Perca Europea. */
public class PercaEuropea extends Pez implements Activo {

    /**
     * Crea una instancia de PercaEuropea con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public PercaEuropea(boolean sexo) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }
}