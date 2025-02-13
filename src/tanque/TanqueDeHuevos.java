package tanque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.Simulador;
import peces.Pez;

/**
 * Representa un tanque de huevos donde se almacenan crías de peces 
 * cuando no hay espacio en otros tanques de la piscifactoría.
 */
public class TanqueDeHuevos {
    private static List<Pez> crias;
    private final int capacidad = 25;
    public static final int COSTO_TANQUE_HUEVOS = 1500;

    /**
     * Crea un tanque de huevos vacío.
     */
    public TanqueDeHuevos() {
        crias = new ArrayList<>();
    }

    /**
     * Agrega una cría al tanque si hay espacio disponible.
     * 
     * @param cria Pez que se desea agregar al tanque.
     * @return true si la cría fue agregada, false si el tanque está lleno.
     */
    public boolean agregarCria(Pez cria) {
        if (crias.size() < capacidad) {
            crias.add(cria);
            return true;
        }
        return false;
    }

    public Map<String, Integer> contarEspecies() {
        Map<String, Integer> especies = new HashMap<>();
        for (Pez p : crias) {
            // Si la especie ya está en el mapa, incrementa el contador.
            if (especies.containsKey(p.getNombre())) {
                especies.put(p.getNombre(), especies.get(p.getNombre()) + 1);
            } else {
                // Si no existe, la agrega con valor 1.
                especies.put(p.getNombre(), 1);
            }
        }
        return especies;
    }
    

    /**
     * Vacía el tanque de huevos eliminando todas las crías almacenadas.
     */
    public void vaciarTanque() {
        crias.clear();
    }

    /**
     * Obtiene la lista de crías almacenadas en el tanque.
     * 
     * @return Lista de peces crías en el tanque.
     */
    public List<Pez> getCrias() {
        return crias;
    }
    
    public boolean addFish(Pez pez) {

        if (crias.size() < capacidad) {
            if (Simulador.monedas.gastarMonedas(pez.getDatos().getCoste())) {
                if (crias.isEmpty() || crias.get(0).getNombre().equals(pez.getNombre())) {
                    crias.add(pez);
                    return true;
                } else {
                    System.out.println("\nTipo de pez incompatible. Solo se pueden agregar peces de tipo: "
                            + crias.get(0).getNombre());
                    return false;
                }
            } else {
                System.out.println("\nNecesitas " + pez.getDatos().getCoste() + " monedas para comprar un "
                        + pez.getNombre() + ".");
                return false;
            }
        } else {
            System.out.println("\nEl tanque está lleno. Capacidad máxima alcanzada.");
            return false;
        }
    }
}
