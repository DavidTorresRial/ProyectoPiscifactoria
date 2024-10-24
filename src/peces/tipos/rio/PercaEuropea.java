package peces.tipos.rio;

import java.util.Random;

import peces.Pez;
import peces.propiedades.Activo;
import propiedades.AlmacenPropiedades;

public class PercaEuropea extends Pez implements Activo {

    public PercaEuropea(boolean sexo) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }

    @Override
    public boolean comerCarne(int comidaAnimalDisponible) {
        if (comidaAnimalDisponible > 0) {
            System.out.println("El pez carnívoro activo ha comido 1 unidad de comida animal.");
            return true; // Consumió comida
        }
        System.out.println("No hay suficiente comida animal.");
        return false; // No consumió comida
    }

    @Override
    public boolean comerCarneActivo(int comidaAnimalDisponible) {
        Random random = new Random();
        // 50% de probabilidades de comer el doble de comida
        if (random.nextDouble() < 0.5 && comidaAnimalDisponible >= 2) {
            System.out.println("El pez carnívoro activo ha comido 2 unidades de comida animal.");
            return true; // Consumió 2 unidades de comida
        }
        return comerCarne(comidaAnimalDisponible); // Si no come 2 unidades, come 1
    }
}
