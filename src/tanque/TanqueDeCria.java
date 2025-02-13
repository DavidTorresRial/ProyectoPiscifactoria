package tanque;

import peces.Pez;

/**
 * Representa un tanque de cría donde se mantiene una pareja de peces
 * para su reproducción continua sin que mueran ni sean vendidos.
 */
public class TanqueDeCria {
    private String especie;
    private Pez macho;
    private Pez hembra;
    private int cicloReproduccion;
    public static final int COSTO_TANQUE_CRIAS = 500;

    /**
     * Crea un tanque de cría para una especie específica.
     * Inicialmente, el tanque está vacío.
     * 
     * @param especie Nombre de la especie que se criará en el tanque.
     */
    public TanqueDeCria(String especie) {
        this.especie = especie;
        this.macho = null;
        this.hembra = null;
        this.cicloReproduccion = 0;
    }

    /**
     * Verifica si el tanque está vacío, es decir, si no tiene pareja de peces.
     * 
     * @return true si el tanque está vacío, false si contiene una pareja.
     */
    public boolean estaVacio() {
        return (macho == null && hembra == null);
    }

    /**
     * Permite comprar una pareja de peces de la misma especie para el tanque de cría.
     * 
     * @param macho Pez macho a añadir.
     * @param hembra Pez hembra a añadir.
     * @return true si la compra fue exitosa, false si el tanque ya tenía peces o si las especies no coinciden.
     */
    public boolean comprarPareja(Pez macho, Pez hembra) {
        if (estaVacio() && macho.getNombre().equals(hembra.getNombre())) {
            this.macho = macho;
            this.hembra = hembra;
            this.cicloReproduccion = macho.getDatos().getCiclo();
            return true;
        }
        return false;
    }

    /**
     * Avanza un día en el tanque de cría. Si los peces han sido alimentados,
     * el ciclo de reproducción avanza y, si llega a 0, los peces se reproducen.
     * 
     * @param seAlimentaron Indica si los peces fueron alimentados en el día.
     */
    public void nextDay(boolean seAlimentaron) {
        if (!estaVacio() && seAlimentaron) {
            cicloReproduccion--;
            if (cicloReproduccion <= 0) {
                reproducir();
                cicloReproduccion = macho.getDatos().getCiclo();
            }
        }
    }

    /**
     * Lógica de reproducción: genera el doble de crías y las transfiere a un tanque disponible.
     */
    private void reproducir() {
        int numHuevos = macho.getDatos().getHuevos();
        int totalCria = 2 * numHuevos;
        System.out.println("Tanque de cría de " + especie + " ha reproducido " + totalCria + " crías.");
    }

    /**
     * Devuelve el estado del tanque de cría, indicando la especie y el ciclo de reproducción restante.
     * 
     * @return Estado del tanque en forma de cadena de texto.
     */
    public String estado() {
        if (estaVacio()) {
            return "Tanque de cría vacío.";
        } else {
            return "Tanque de cría de " + especie + " - Ciclo restante: " + cicloReproduccion;
        }
    }

    /**
     * Vacía el tanque eliminando la pareja de peces.
     */
    public void vaciarTanque() {
        this.macho = null;
        this.hembra = null;
    }
}
