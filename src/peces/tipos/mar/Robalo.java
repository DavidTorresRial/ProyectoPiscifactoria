package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Robalo. */
public class Robalo extends Pez implements Carnivoro {

    /**
     * Crea una instancia de Robalo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Robalo(boolean sexo) {
        super(sexo, AlmacenPropiedades.ROBALO);
    }

    /**
     * Crea una copia del Robalo con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de Robalo con el sexo especificado.
     */
    @Override
    public Robalo clonar(boolean nuevoSexo) {
        return new Robalo(nuevoSexo);
    }
}