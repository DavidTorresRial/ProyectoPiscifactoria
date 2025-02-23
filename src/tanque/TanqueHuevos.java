package tanque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.Simulador;
import peces.Pez;

/** Representa un tanque para almacenar peces con capacidades de gestión y reproducción. */
public class TanqueHuevos {

    /** Lista para almacenar los peces en el tanque. */
    private static List<Pez> huevos = new ArrayList<>();

    /** Número máximo de crias. */
    private static int NUMERO_MAXIMO_CRIAS = 25;

    /**
     * Añade crias al tanque de huevos.
     * 
     * @param pez el pez a añadir.
     * @param cantidadHuevos cantidad de peces a añadir.
     */
    public static void añadirCria(Pez pez, int cantidadHuevos) {
        for (int i = 0; i < cantidadHuevos; i++) {
            if (huevos.size() < NUMERO_MAXIMO_CRIAS) {
                huevos.add(pez.clonar(false));
            } else {
                System.out.println("\nNo tienes espacio para añadir mas huevos en este tanque de huevos.");
            }
        }
        System.out.println("\nHas añadido " + cantidadHuevos + " huevo(s) de " + pez.getNombre() + " al tanque de huevos.");
    }

    /**
     * Agrega las crias a un tanque disponible.
     * 
     * @param tanques el array de tanques.
     */
    public void redistribuirCrias(List<Tanque> tanques) {
        for (Tanque tanque : tanques) {
            if (tanque.getPeces().size() < tanque.getCapacidad() && tanque.getCapacidad() > 0) {
                for (Pez pez : huevos) {
                    if (pez.getNombre().equals(tanque.getPeces().get(0).getNombre())) {
                        tanque.addFish(pez);
                    }
                }
            }
        }
    }

    /**
     * Muestra por consola la lista de peces en el tanque de huevos,
     * clasificandolos por especies.
     */
    public void listarPeces() {
            Map<String, Integer> conteoEspecies = new HashMap<>();
    
            for (Pez pez : huevos) {
                conteoEspecies.put(pez.getNombre(), conteoEspecies.getOrDefault(pez.getNombre(), 0) + 1);
            }
    
            System.out.println("\nEspecies en los tanques de huevos:");
            for (Map.Entry<String, Integer> entry : conteoEspecies.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
    }
 
    /**
     * Devuelve la lista de peces en el tanque de huevos.
     * 
     * @return la lista de peces en el tanque de huevos.
     */
    public List<Pez> getHuevos() {
        return huevos;
    }
}