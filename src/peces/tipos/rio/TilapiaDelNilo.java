package peces.tipos.rio;

import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Tilapia del Nilo. */
public class TilapiaDelNilo extends Filtrador {

    /**
     * Crea una instancia de TilapiaDelNilo con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TilapiaDelNilo(boolean sexo) {
        super(sexo, AlmacenPropiedades.TILAPIA_NILO);
    }

    /**
     * Constructor de la clase TilapiaDelNilo con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public TilapiaDelNilo(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.TILAPIA_NILO);
    }

    /**
     * Crea una copia del TilapiaDelNilo con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de TilapiaDelNilo con el sexo especificado.
     */
    @Override
    public TilapiaDelNilo clonar(boolean nuevoSexo) {
        return new TilapiaDelNilo(nuevoSexo);
    }
}