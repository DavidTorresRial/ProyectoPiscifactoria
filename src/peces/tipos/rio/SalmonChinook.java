package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de río Salmon Chinook. */
public class SalmonChinook extends Pez implements Carnivoro {

    /**
     * Crea una instancia de SalmonChinook con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonChinook(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_CHINOOK);
    }

    /**
     * Crea una copia del SalmonChinook con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de SalmonChinook con el sexo especificado.
     */
    @Override
    public SalmonChinook clonar(boolean nuevoSexo) {
        return new SalmonChinook(nuevoSexo);
    }
}