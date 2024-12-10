package peces.tipos.doble;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Trucha Arcoiris. */
public class TruchaArcoiris extends Carnivoro {

    /**
     * Crea una instancia de TruchaArcoiris con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TruchaArcoiris(boolean sexo) {
        super(sexo, AlmacenPropiedades.TRUCHA_ARCOIRIS);
    }

    /**
     * Crea una copia del TruchaArcoiris con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de TruchaArcoiris con el sexo especificado.
     */
    @Override
    public TruchaArcoiris clonar(boolean nuevoSexo) {
        return new TruchaArcoiris(nuevoSexo);
    }
}