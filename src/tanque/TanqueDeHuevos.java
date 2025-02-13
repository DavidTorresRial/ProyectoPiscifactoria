package tanque;

import java.util.ArrayList;
import java.util.List;
import peces.Pez;

/**
 * Representa un tanque de huevos donde se almacenan crías de peces 
 * cuando no hay espacio en otros tanques de la piscifactoría.
 */
public class TanqueDeHuevos {
    private List<Pez> crias;
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

    /**
     * Lista las especies de peces en el tanque de huevos.
     * 
     * @return Lista de nombres de especies en el tanque.
     */
    public List<String> listarEspecies() {
        List<String> especies = new ArrayList<>();
        for (Pez p : crias) {
            if (!especies.contains(p.getNombre())) {
                especies.add(p.getNombre());
            }
        }
        return especies;
    }

    /**
     * Lista la cantidad de peces por especie en el tanque de huevos.
     * 
     * @return Lista con las cantidades correspondientes a cada especie.
     */
    public List<Integer> listarCantidades() {
        List<String> especies = listarEspecies();
        List<Integer> cantidades = new ArrayList<>();

        for (String especie : especies) {
            int count = 0;
            for (Pez p : crias) {
                if (p.getNombre().equals(especie)) {
                    count++;
                }
            }
            cantidades.add(count);
        }
        return cantidades;
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
}
