package piscifactorias.tipos;

import commons.SistemaMonedas;

import piscifactorias.Piscifactoria;
import tanque.Tanque;

public class PiscifactoriaDeRio extends Piscifactoria {

    // Capacidad máxima del array de tanques
    private static final int CAPACIDAD_MAXIMA_TANQUES = 10;

    public PiscifactoriaDeRio(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaAlmacenComida = 25;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        // Inicializa con un tanque de capacidad 25
        tanques.add(new Tanque(25));
    }

    /**
     * Añade un nuevo tanque con capacidad fija de 25 unidades a la piscifactoría, si hay espacio disponible.
     * @return true si el tanque se añadió correctamente, false si no hay espacio.
     */
    public boolean añadirTanque() {
        // Verifica si hay espacio en el array de tanques
        if (tanques.size() < CAPACIDAD_MAXIMA_TANQUES) {
            tanques.add(new Tanque(25));
            System.out.println("Tanque añadido con capacidad de 25 unidades.");
            return true;
        } else {
            System.out.println("Error: No hay espacio disponible para más tanques.");
            return false;
        }
    }
}
