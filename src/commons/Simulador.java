package commons;

import peces.Pez;
import peces.tipos.rio.TilapiaDelNilo;
import tanque.Tanque;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class Simulador {
    public static void main(String[] args) {
        // Crear un tanque con capacidad para 5 peces
        Tanque<Pez> tanque = new Tanque<>(5);
        
        // Agregar peces al tanque
        System.out.println("Agregando peces al tanque:");
        tanque.addPez(new TilapiaDelNilo(true));  // Macho
        tanque.addPez(new TilapiaDelNilo(false)); // Hembra
        tanque.addPez(new TilapiaDelNilo(true));  // Macho
        tanque.addPez(new TilapiaDelNilo(false)); // Hembra
        
        // Intentar agregar un pez adicional (no debería ser posible)
        System.out.println("Intentando agregar un pez adicional:");
        if (!tanque.addPez(new TilapiaDelNilo(true))) {
            System.out.println("No se pudo agregar el pez, tanque lleno.");
        }
        
        // Mostrar estado del tanque y de los peces
        tanque.showStatus();
        tanque.showFishStatus();
        
        // Simulación de alimentación y crecimiento durante 5 días
        for (int dia = 1; dia <= 5; dia++) {
            System.out.println("\nDía " + dia + ":");
            tanque.nextDay(2, 4);  // 2 unidades de comida animal, 4 unidades de comida vegetal
            tanque.showStatus();
            tanque.showFishStatus();
        }
        
        // Mostrar capacidad del tanque
        tanque.showCapacity();
    }
}
