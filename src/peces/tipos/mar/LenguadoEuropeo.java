package peces.tipos.mar;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Lenguado Europeo. */
public class LenguadoEuropeo extends Carnivoro {

    /**
     * Crea una instancia de LenguadoEuropeo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public LenguadoEuropeo(boolean sexo) {
        super(sexo, AlmacenPropiedades.LENGUADO_EUROPEO);
    }

    /**
     * Crea una copia del LenguadoEuropeo con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de LenguadoEuropeo con el sexo especificado.
     */
    @Override
    public LenguadoEuropeo clonar(boolean nuevoSexo) {
        return new LenguadoEuropeo(nuevoSexo);
    }
}