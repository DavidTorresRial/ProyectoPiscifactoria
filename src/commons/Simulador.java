package commons;

import tanque.Tanque;
import peces.Pez;
import peces.tipos.mar.Besugo;

public class Simulador {

     public static void main(String[] args) {
        // Crear un tanque con capacidad para 20 peces
        Tanque<Pez> tanque = new Tanque<>(20);

        // Agregar peces al tanque
        // Usando Pejerrey como ejemplo, puedes cambiar a otras especies como Besugo o Robalo 
        tanque.peces.add(new Besugo(true));  // Macho fértil
        tanque.peces.add(new Besugo(false)); // Hembra fértil

        // Alimentar y hacer crecer los peces para que sean fértiles
        for (Pez pez : tanque.peces) {
            pez.alimentar();
            while (!pez.isFertil()) {
                pez.grow();
            }
        }

        // Mostrar el estado inicial del tanque
        System.out.println("Estado inicial del tanque:");
        for (Pez pez : tanque.peces) {
            pez.showStatus();
        }

        // Ejecutar la reproducción
        tanque.reproduccion();

        // Mostrar el estado del tanque después de la reproducción
        System.out.println("\nEstado del tanque después de la reproducción:");
        for (Pez pez : tanque.peces) {
            pez.showStatus();
        }
    }
}
