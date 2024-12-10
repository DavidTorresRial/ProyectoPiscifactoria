package peces.tipos.rio;

import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Carpa Plateada. */
public class CarpaPlateada extends Filtrador {

    /**
     * Crea una instancia de CarpaPlateada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public CarpaPlateada(boolean sexo) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
    }

    /**
     * Crea una copia del CarpaPlateada con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de CarpaPlateada con el sexo especificado.
     */
    @Override
    public CarpaPlateada clonar(boolean nuevoSexo) {
        return new CarpaPlateada(nuevoSexo);
    }
}