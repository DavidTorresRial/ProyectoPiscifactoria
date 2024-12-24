package peces.tipos.mar;

import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de mar Arenque del Atlántico. */
public class ArenqueDelAtlantico extends Filtrador {

    /**
     * Crea una instancia de ArenqueDelAtlantico con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public ArenqueDelAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.ARENQUE_ATLANTICO);
    }

    /**
     * Constructor de la clase ArenqueDelAtlantico con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public ArenqueDelAtlantico(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.ARENQUE_ATLANTICO);
    }

    /**
     * Crea una copia del ArenqueDelAtlantico con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de ArenqueDelAtlantico con el sexo especificado.
     */
    @Override
    public ArenqueDelAtlantico clonar(boolean nuevoSexo) {
        return new ArenqueDelAtlantico(nuevoSexo);
    }
}