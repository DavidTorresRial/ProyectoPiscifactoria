package peces.tipos.doble;

import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

/** Pez doble Salmon Atlántico. */
public class SalmonAtlantico extends Carnivoro {

    /**
     * Crea una instancia de SalmonAtlantico con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public SalmonAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_ATLANTICO);
    }

    /**
     * Constructor de la clase SalmonAtlantico con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public SalmonAtlantico(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.SALMON_ATLANTICO);
    }

    /**
     * Crea una copia del SalmonAtlantico con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de SalmonAtlantico con el sexo especificado.
     */
    @Override
    public SalmonAtlantico clonar(boolean nuevoSexo) {
        return new SalmonAtlantico(nuevoSexo);
    }
}