package commons;

import piscifactorias.*;
import piscifactorias.tipos.PiscifactoriaDeRio;
import tanque.Tanque;
import peces.Pez;
import peces.tipos.doble.SalmonAtlantico;
import peces.tipos.mar.Besugo; // Asegúrate de importar el tipo de pez que deseas usar
import peces.tipos.rio.Pejerrey;


public class Simulador {

     public static void main(String[] args) {
        // Crear un sistema de monedas (asumiendo que es necesario para la piscifactoría)
        SistemaMonedas sistemaMonedas = new SistemaMonedas(1000);

        // Crear una piscifactoría
        Piscifactoria piscifactoria = new PiscifactoriaDeRio("Piscifactoria de Ejemplo", sistemaMonedas) {
            // Implementación de métodos abstractos si es necesario
        };

        // Crear un tanque con capacidad para 20 peces
        Tanque<Pez> tanque = new Tanque<>(20);

        // Agregar el tanque a la piscifactoría
        piscifactoria.agregarTanque(tanque);

        // Agregar pez al tanque de piscifactoria
        piscifactoria.addPez(new Besugo(false));
        piscifactoria.addPez(new Pejerrey(false));
        piscifactoria.addPez(new SalmonAtlantico(false));

        // Mostrar el estado de la piscifactoría
        piscifactoria.showStatus();

        // Mostrar el estado de los tanques
        piscifactoria.showTankStatus();

        // Mostrar el estado de los peces en el primer tanque
        piscifactoria.showFishStatus(1);
        piscifactoria.showFishStatus(2);
    }
}

