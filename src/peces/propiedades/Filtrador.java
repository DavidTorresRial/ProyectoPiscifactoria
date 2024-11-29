package peces.propiedades;

import java.util.Random;

import commons.Simulador;
import peces.Pez;
import piscifactoria.Piscifactoria;
import propiedades.PecesDatos;

/** Clase que indica que un pez es un filtrador. */
public abstract class Filtrador extends Pez {

    Random rand = new Random();

    public Filtrador(boolean sexo, PecesDatos datos) {
        super(sexo, datos);
    }

    @Override
    public void alimentar() {
        if (rand.nextDouble() < 0.5) {
            if (Piscifactoria.cantidadComidaVegetal > 0) {
                Piscifactoria.cantidadComidaVegetal--;
                alimentado = true;
            } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaVegetal() > 0) {
                if (Simulador.almacenCentral.getCantidadComidaVegetal() >= 1) {
                    Simulador.almacenCentral.setCantidadComidaVegetal(Simulador.almacenCentral.getCantidadComidaVegetal() - 1);
                    Piscifactoria.cantidadComidaVegetal++;
                    alimentado = true;
                } else {
                    System.out.println("No hay suficiente comida vegetal.");
                }
            }
        } else {
            alimentado = true;
        }
    }
}
