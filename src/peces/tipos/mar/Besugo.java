package peces.tipos.mar;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez de mar Besugo. */
public class Besugo extends Carnivoro {

    /**
     * Crea una instancia de Besugo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public Besugo(boolean sexo) {
        super(sexo, AlmacenPropiedades.BESUGO);
    }

    /**
     * Constructor de la clase Besugo con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public Besugo(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.BESUGO);
    }

    /**
     * Crea una copia del Besugo con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de Besugo con el sexo especificado.
     */
    @Override
    public Besugo clonar(boolean nuevoSexo) {
        return new Besugo(nuevoSexo);
    }
}