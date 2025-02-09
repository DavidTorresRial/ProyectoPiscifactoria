package peces.tipos.rio;

import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

/** Pez de río Carpa Plateada. */
public class CarpaPlateada extends Filtrador {

    /**
     * Crea una instancia de CarpaPlateada con el sexo especificado.
     * 
     * @param sexo El sexo del pez: true para macho, false para hembra.
     */
    public CarpaPlateada(boolean sexo) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
    }

    /**
     * Constructor de la clase CarpaPlateada con valores personalizados.
     *
     * @param sexo        El sexo del pez, true para macho, false para hembra.
     * @param edad        La edad en días del pez.
     * @param vivo        Estado de vida del pez, true si está vivo, false si está muerto.
     * @param fertil      Estado de fertilidad, true si es fértil, false si no.
     * @param ciclo       Ciclo reproductivo, determina el tiempo hasta la reproducción.
     * @param alimentado  Si ha sido alimentado, true si lo ha sido, false si no.
     * @param datos       Contiene las propiedades del pez.
     */
    public CarpaPlateada(boolean sexo, int edad, boolean vivo, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, edad, vivo, fertil, ciclo, alimentado, AlmacenPropiedades.CARPA_PLATEADA);
    }

    /**
     * Crea una copia del CarpaPlateada con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de CarpaPlateada con el sexo especificado.
     */
    @Override
    public CarpaPlateada clonar(boolean nuevoSexo) {
        return new CarpaPlateada(nuevoSexo);
    }
}