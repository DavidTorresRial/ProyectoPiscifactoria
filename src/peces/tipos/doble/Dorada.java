package peces.tipos.doble;

import peces.propiedades.Omnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Dorada. */
public class Dorada extends Omnivoro {

    /**
     * Crea una instancia de Dorada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Dorada(boolean sexo) {
        super(sexo, AlmacenPropiedades.DORADA);
    }

    /**
     * Constructor de la clase Dorada con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public Dorada(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.DORADA);
    }

    /**
     * Crea una copia del Dorada con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de Dorada con el sexo especificado.
     */
    @Override
    public Dorada clonar(boolean nuevoSexo) {
        return new Dorada(nuevoSexo);
    }
}

