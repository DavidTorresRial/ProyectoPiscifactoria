package piscifactorias.tipos;

import commons.SistemaMonedas;
import piscifactorias.Piscifactoria;
import tanque.Tanque;

public class PiscifactoriaDeMar extends Piscifactoria {

    // Capacidad máxima del array de tanques
    private static final int CAPACIDAD_MAXIMA_TANQUES = 10;

    // Contador de tanques
    private int contadorTanquesMar;

    public PiscifactoriaDeMar(String nombre, SistemaMonedas monedas) {
        super(nombre, monedas);
        capacidadMaximaComidaPiscifactoria = 100;
        comidaVegetalActual = 0;
        comidaAnimalActual = 0;

        // Inicializa con un tanque de capacidad 100
        tanques.add(new Tanque(100));
        contadorTanquesMar = 1; // Inicia con 1 tanque
    }

    /**
     * Añade un nuevo tanque con capacidad fija de 100 unidades a la piscifactoría, si hay espacio disponible.
     * @return true si el tanque se añadió correctamente, false si no hay espacio.
     */
    public boolean añadirTanque() {
        // Verifica si hay espacio en el array de tanques
        if (tanques.size() < CAPACIDAD_MAXIMA_TANQUES) {
            tanques.add(new Tanque(100));
            contadorTanquesMar++; // Incrementa el contador de tanques
            System.out.println("Tanque añadido con capacidad de 100 unidades. Total de tanques: " + contadorTanquesMar);
            return true;
        } else {
            System.out.println("Error: No hay espacio disponible para más tanques.");
            return false;
        }
    }

    // Método para obtener el contador de tanques
    public int getTanquesMar() {
        return contadorTanquesMar;
    }
}
