package peces.tipos.rio;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de río Pejerrey. */
public class Pejerrey extends Carnivoro {

    /**
     * Crea una instancia de Pejerrey con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Pejerrey(boolean sexo) {
        super(sexo, AlmacenPropiedades.PEJERREY);
    }

    /**
     * Crea una copia del Pejerrey con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de Pejerrey con el sexo especificado.
     */
    @Override
    public Pejerrey clonar(boolean nuevoSexo) {
        return new Pejerrey(nuevoSexo);
    }
}