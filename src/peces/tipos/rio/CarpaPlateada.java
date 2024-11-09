package peces.tipos.rio;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Carpa Plateada. */
public class CarpaPlateada extends Pez implements Filtrador {

    /**
     * Crea una instancia de CarpaPlateada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public CarpaPlateada(boolean sexo) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
    }
}