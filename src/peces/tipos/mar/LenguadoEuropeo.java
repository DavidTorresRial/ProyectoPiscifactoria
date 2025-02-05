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
     * Constructor de la clase LenguadoEuropeo con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public LenguadoEuropeo(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.LENGUADO_EUROPEO);
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