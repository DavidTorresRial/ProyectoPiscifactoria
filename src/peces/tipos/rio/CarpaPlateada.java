package peces.tipos.rio;

import java.util.Random;

import peces.Pez;
import peces.propiedades.Filtrador;
import propiedades.AlmacenPropiedades;

public class CarpaPlateada extends Pez implements Filtrador {

    public CarpaPlateada(boolean sexo) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
    }

    @Override
    public boolean comerVegetal(int comidaVegetalDisponible) {
        Random random = new Random();
        // 50% de probabilidad de no consumir comida
        if (random.nextDouble() >= 0.5) {
            if (comidaVegetalDisponible > 0) {
                System.out.println("El pez filtrador ha comido 1 unidad de comida vegetal.");
                return true; // Consumió comida
            }
            System.out.println("No hay suficiente comida vegetal para el pez filtrador.");
        } else {
            System.out.println("El pez filtrador no ha consumido comida.");
        }
        return false; // No consumió comida
    }
}
