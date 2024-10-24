package commons;

import peces.Pez;
import peces.tipos.rio.SalmonChinook;
import tanque.Tanque;

public class Simulador {
    public static void main(String[] args) {
        // Crear un tanque con capacidad para 5 peces
        Tanque<Pez> tanque = new Tanque<>(5);

        int comidaAnimalActual = 100;
        int comidaVegetalActual = 100;

        // Agregar peces al tanque
        System.out.println("Agregando peces al tanque:");
        tanque.addPez(new SalmonChinook(true)); // Macho
        tanque.addPez(new SalmonChinook(false)); // Hembra


        // Mostrar estado del tanque y de los peces
        tanque.showStatus();
        tanque.showFishStatus();

        // Simulación de alimentación y crecimiento durante 5 días

        System.out.println("\nDía 1");
        tanque.nextDay(comidaAnimalActual, comidaVegetalActual);
        tanque.showStatus();
        tanque.showFishStatus();

        // Mostrar capacidad del tanque
        tanque.showCapacity();


        System.out.println(comidaAnimalActual);
        System.out.println(comidaVegetalActual);
    }
}
