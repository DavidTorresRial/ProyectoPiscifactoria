package peces.tipos.doble;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Trucha Arcoiris. */
public class TruchaArcoiris extends Carnivoro {

    /**
     * Crea una instancia de TruchaArcoiris con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public TruchaArcoiris(boolean sexo) {
        super(sexo, AlmacenPropiedades.TRUCHA_ARCOIRIS);
    }

    /**
     * Constructor de la clase TruchaArcoiris con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public TruchaArcoiris(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.TRUCHA_ARCOIRIS);
    }

    /**
     * Crea una copia del TruchaArcoiris con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de TruchaArcoiris con el sexo especificado.
     */
    @Override
    public TruchaArcoiris clonar(boolean nuevoSexo) {
        return new TruchaArcoiris(nuevoSexo);
    }
}