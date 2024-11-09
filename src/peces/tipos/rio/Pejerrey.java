package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de río Pejerrey. */
public class Pejerrey extends Pez implements Carnivoro {

    /**
     * Crea una instancia de Pejerrey con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Pejerrey(boolean sexo) {
        super(sexo, AlmacenPropiedades.PEJERREY);
    }
}