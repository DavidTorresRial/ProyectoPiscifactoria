package piscifactorias.tipos;

import commons.SistemaMonedas;
import piscifactorias.Piscifactoria;
import tanque.Tanque;

/** Clase que representa una piscifactoría de río que gestiona tanques de peces */
public class PiscifactoriaDeRio extends Piscifactoria {

    /** Capacidad máxima de tanques */
    private static final int CAPACIDAD_MAXIMA_TANQUES = 10;

    /** Contador de tanques en la piscifactoría */
    private int contadorTanquesRio;

    /**
     * Constructor de la piscifactoría de río.
     *
     * @param nombre el nombre de la piscifactoría
     * @param monedas el sistema de monedas asociado
     */
    public PiscifactoriaDeRio(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaComidaPiscifactoria = 25;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        tanques.add(new Tanque(tanques.size() + 1, 25));
        contadorTanquesRio = 1;
    }

    /**
     * Añade un tanque de 25 unidades.
     *
     * @return true si se añadió el tanque, false si no hay espacio.
     */
    public boolean añadirTanque() {
        if (tanques.size() < CAPACIDAD_MAXIMA_TANQUES) {
            tanques.add(new Tanque(tanques.size() + 1, 25));
            contadorTanquesRio++;
            System.out.println("Tanque añadido. Total: " + contadorTanquesRio);
            return true;
        } else {
            System.out.println("Error: No hay espacio disponible para más tanques.");
            return false;
        }
    }

    /**
     * Obtiene el número de tanques en la piscifactoría.
     *
     * @return el número de tanques.
     */
    public int getTanquesRio() {
        return contadorTanquesRio;
    }
}
