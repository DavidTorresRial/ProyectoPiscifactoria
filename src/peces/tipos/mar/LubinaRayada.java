package peces.tipos.mar;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Lubina Rayada. */
public class LubinaRayada extends Pez implements Carnivoro {

    /**
     * Crea una instancia de LubinaRayada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public LubinaRayada(boolean sexo) {
        super(sexo, AlmacenPropiedades.LUBINA_RAYADA);
    }

    /**
     * Crea una copia del LubinaRayada con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de LubinaRayada con el sexo especificado.
     */
    @Override
    public LubinaRayada clonar(boolean nuevoSexo) {
        return new LubinaRayada(nuevoSexo);
    }
}