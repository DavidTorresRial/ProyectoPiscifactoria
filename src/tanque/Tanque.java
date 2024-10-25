package tanque;

import java.util.ArrayList;

import peces.Pez;
import piscifactorias.Piscifactoria;

/**
 * La clase Tanque representa un contenedor para peces de un tipo específico. 
 * 
 * @param <T> Tipo de pez que extiende de la clase Pez.
 */
public class Tanque<T extends Pez> {

    /** Lista para almacenar los peces del tanque */
    public ArrayList<T> peces;

    /** Tipo de pez actual permitido en el tanque */
    private Class<?> tipoPezActual;

    /** Capacidad máxima de peces que puede almacenar el tanque */
    private int capacidadMaxima;

    /** Número identificador del tanque */
    private int numeroTanque;

    /**
     * Constructor que inicializa el tanque con una capacidad máxima.
     *
     * @param capacidadMaxima Capacidad máxima del tanque.
     */
    public Tanque(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.peces = new ArrayList<>();
        this.tipoPezActual = null;
    }

    /**
     * Muestra el estado general del tanque.
     */
    public void showStatus() {
        System.out.println("\n=============== Tanque " + numeroTanque + " ===============");

        System.out.println("Ocupación: " + peces.size() + " / " + capacidadMaxima + " (" + (peces.size() * 100 / capacidadMaxima) + "%)");
        System.out.println("Peces vivos: " + getVivos() + " / " + peces.size() + " (" + (getVivos() > 0 ? (getVivos() * 100 / peces.size()) : 0) + "%)");
        System.out.println("Peces alimentados: " + getAlimentados() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAlimentados() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Peces adultos: " + getAdultos() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAdultos() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Hembras / Machos: " + getHembras() + " / " + getMachos());
        System.out.println("Fértiles: " + getFertiles() + " / " + getVivos());
    }

    /**
     * Muestra el estado individual de cada pez en el tanque.
     */
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        for (T pez : peces) {
            pez.showStatus();
        }
    }

    /**
     * Muestra la capacidad actual del tanque en porcentaje.
     */
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría: " + Piscifactoria.getNombre() + " al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]");
    }

    /**
     * Simula el paso de un día en el tanque. Hace crecer a los peces y gestiona la reproducción.
     */
    public void nextDay() { 
        System.out.println("Avanzando al siguiente día para el Tanque " + numeroTanque + "...");
        for (T pez : peces) {
            pez.grow(); // Hace crecer cada pez
        }

        reproduccion(); // Reproduce los peces

        //venderPecesAdultos //TODO implementar metodo
    }

    /**
     * Método que gestiona la reproducción de los peces dentro del tanque.
     */
    public void reproduccion() {
        int huevosPorHembra = 0;

        if (!peces.isEmpty()) {
            huevosPorHembra = peces.get(0).getHuevos();
        }

        int nuevosMachos = 0;
        int nuevasHembras = 0;

        // Listas para almacenar los peces fértiles
        ArrayList<T> machosFertiles = new ArrayList<>();
        ArrayList<T> hembrasFertiles = new ArrayList<>();

        // Clasificar los peces fértiles
        for (T pez : peces) {
            if (pez.isFertil()) {
                if (pez.isSexo()) {
                    machosFertiles.add(pez);
                } else {
                    hembrasFertiles.add(pez);
                }
            }
        }

        if (machosFertiles.isEmpty() || hembrasFertiles.isEmpty()) {
            System.out.println("No hay suficientes peces fértiles de ambos sexos para reproducirse.");
            return;
        }

        // Reproducción
        for (T macho : machosFertiles) {
            if (hembrasFertiles.isEmpty()) {
                break;
            }

            for (T hembra : new ArrayList<>(hembrasFertiles)) {
                for (int i = 0; i < huevosPorHembra; i++) {
                    boolean nuevoSexo;
                    if (getHembras() <= getMachos()) {
                        nuevoSexo = false;
                    } else {
                        nuevoSexo = true;
                    }

                    T nuevoPez = (T) hembra.clonar(nuevoSexo);

                    if (peces.size() < capacidadMaxima) {
                        peces.add(nuevoPez);
                        if (nuevoSexo) {
                            nuevosMachos++;
                        } else {
                            nuevasHembras++;
                        }
                    } else {
                        System.out.println("No se puede agregar más peces. Tanque lleno.");
                        break;
                    }
                }
                hembra.setFertil(false);
                hembrasFertiles.remove(hembra);
            }
            macho.setFertil(false);
        }

        System.out.println("Se han creado " + nuevosMachos + " nuevos machos y " + nuevasHembras + " nuevas hembras.");
    }

    /**
     * Añade un pez al tanque si hay espacio y el tipo de pez es compatible con los ya presentes.
     *
     * @param pez El pez a añadir al tanque.
     * @return true si se añadió con éxito, false si el tanque está lleno o el tipo de pez no es compatible.
     */
    public boolean addPez(T pez) {
        if (peces.size() >= capacidadMaxima) {
            System.out.println("El tanque está lleno.");
            return false;
        }

        // Si el tanque está vacío, restablece el tipo de pez permitido
        if (peces.isEmpty()) {
            tipoPezActual = null;
        }

        if (tipoPezActual == null) {
            tipoPezActual = pez.getClass(); // Establece el tipo de pez actual
        }

        if (!tipoPezActual.equals(pez.getClass())) {
            System.out.println("Este tanque solo acepta peces del tipo: " + tipoPezActual.getSimpleName());
            return false;
        }

        peces.add(pez);
        return true;
    }

    // Getters y Setters

    /**
     * @return El número identificador del tanque.
     */
    public int getNumeroTanque() {
        return numeroTanque;
    }

    /**
     * @return El número de peces presentes en el tanque.
     */
    public int getNumPeces() {
        return peces.size();
    }

    /**
     * @return La lista de peces en el tanque.
     */
    public ArrayList<T> getPeces() {
        return peces;
    }

    /**
     * @return El número de hembras en el tanque.
     */
    public int getHembras() {
        // Contar hembras
        int hembras = 0;
        for (T pez : peces) {
            if (!pez.isSexo()) { // false es hembra
                hembras++;
            }
        }
        return hembras;
    }

    /**
     * @return El número de machos en el tanque.
     */
    public int getMachos() {
        // Contar machos
        int machos = 0;
        for (T pez : peces) {
            if (pez.isSexo()) { // true si es macho
                machos++;
            }
        }
        return machos;
    }

    /**
     * @return El número de peces fértiles en el tanque.
     */
    public int getFertiles() {
        // Contar peces fértiles
        int fertiles = 0;
        for (T pez : peces) {
            if (pez.isFertil()) {
                fertiles++;
            }
        }
        return fertiles;
    }

    /**
     * @return El número de peces vivos en el tanque.
     */
    public int getVivos() {
        // Contar peces vivos
        int pecesVivos = 0;
        for (T pez : peces) {
            if (pez.isVivo()) {
                pecesVivos++;
            }
        }
        return pecesVivos;
    }

    /**
     * @return El número de peces alimentados en el tanque.
     */
    public int getAlimentados() {
        // Contar peces alimentados
        int pecesAlimentados = 0;
        for (T pez : peces) {
            if (pez.isAlimentado()) {
                pecesAlimentados++;
            }
        }
        return pecesAlimentados;
    }

    /**
     * @return El número de peces adultos en el tanque.
     */
    public int getAdultos() {
        // Contar peces adultos
        int pecesAdultos = 0;
        for (T pez : peces) {
            if (pez.getEdad() >= pez.getDatos().getMadurez()) {
                pecesAdultos++;
            }
        }
        return pecesAdultos;
    }
}
