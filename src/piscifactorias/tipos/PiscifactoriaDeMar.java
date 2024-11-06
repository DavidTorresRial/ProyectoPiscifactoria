package piscifactorias.tipos;

import commons.SistemaMonedas;
import piscifactorias.Piscifactoria;
import tanque.Tanque;

/** Clase que representa una piscifactoría de mar que gestiona tanques de peces */
public class PiscifactoriaDeMar extends Piscifactoria {

    /** Capacidad máxima de tanques */
    private static final int CAPACIDAD_MAXIMA_TANQUES = 10;

    /** Contador de tanques en la piscifactoría */
    private int contadorTanquesMar;

    /**
     * Constructor de la piscifactoría de mar.
     *
     * @param nombre el nombre de la piscifactoría
     * @param monedas el sistema de monedas asociado
     */
    public PiscifactoriaDeMar(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaComidaPiscifactoria = 100;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        tanques.add(new Tanque(tanques.size() + 1, 100));
        contadorTanquesMar = 1;
    }

    /**
     * Añade un tanque de 100 unidades.
     *
     * @return true si se añadió el tanque, false si no hay espacio.
     */
    public boolean añadirTanque() {
        if (tanques.size() < CAPACIDAD_MAXIMA_TANQUES) {
            tanques.add(new Tanque(tanques.size() + 1, 100));
            contadorTanquesMar++;
            System.out.println("Tanque añadido. Total: " + contadorTanquesMar);
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
    public int getTanquesMar() {
        return contadorTanquesMar;
    }
}
