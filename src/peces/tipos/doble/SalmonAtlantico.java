package peces.tipos.doble;

import peces.Pez;
import peces.propiedades.Carnivoro;
import propiedades.AlmacenPropiedades;

public class SalmonAtlantico extends Pez implements Carnivoro {

    public SalmonAtlantico(boolean sexo) {
        super(sexo, AlmacenPropiedades.SALMON_ATLANTICO);
    }

    @Override
    public boolean comerCarne(int comidaAnimalDisponible) {
        if (comidaAnimalDisponible > 0) {
            System.out.println("El pez carnívoro ha comido 1 unidad de comida animal.");
            return true; // Consumió comida
        }
        System.out.println("No hay suficiente comida animal para el pez carnívoro.");
        return false; // No consumió comida
    }
}
