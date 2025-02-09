package peces.tipos.mar;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Lubina Rayada. */
public class LubinaRayada extends Carnivoro {

    /**
     * Crea una instancia de LubinaRayada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public LubinaRayada(boolean sexo) {
        super(sexo, AlmacenPropiedades.LUBINA_RAYADA);
    }

    /**
     * Constructor de la clase LubinaRayada con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public LubinaRayada(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.LUBINA_RAYADA);
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