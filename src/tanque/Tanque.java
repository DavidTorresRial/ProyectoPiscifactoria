package tanque;

import java.util.ArrayList;
import java.util.Iterator;

import commons.SistemaMonedas;
import peces.Pez;
import piscifactorias.Piscifactoria;

/**
 * Clase que representa un tanque donde se pueden almacenar peces.
 *
 * @param <T> tipo de pez que extiende la clase Pez
 */
public class Tanque<T extends Pez> {
    /** Lista para almacenar los peces en el tanque. */
    public ArrayList<T> peces;
    
    /** Tipo de pez actual permitido en el tanque. */
    private Class<?> tipoPezActual;

    /** Capacidad máxima del tanque. */
    private int capacidadMaxima;
    
    /** Número del tanque. */
    private int numeroTanque;
    
    /** Sistema de monedas asociado al tanque. */
    private SistemaMonedas monedas;

    /** Piscifactoria a la que pertenece el tanque. */
    Piscifactoria piscifactoria;

    /**
     * Constructor para crear un nuevo tanque.
     *
     * @param capacidadMaxima capacidad máxima del tanque
     * @param monedas sistema de monedas para gestionar el dinero ganado
     */
    public Tanque(int capacidadMaxima, SistemaMonedas monedas) {
        this.capacidadMaxima = capacidadMaxima;
        this.peces = new ArrayList<>();
        this.tipoPezActual = null;
        this.monedas = monedas;
    }

    /**
     * Muestra el estado actual del tanque, incluyendo ocupación y detalles sobre los peces.
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
     * Muestra el estado de todos los peces en el tanque.
     */
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        for (T pez : peces) {
            pez.showStatus();
        }
    }

    /**
     * Muestra la capacidad actual del tanque.
     */
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría: " + piscifactoria.getNombre() + " al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]");
    }

    /**
     * Avanza un día en el tanque, haciendo crecer los peces y ejecutando la reproducción.
     */
    public void nextDay() { 
        System.out.println("Avanzando al siguiente día para el Tanque " + numeroTanque + "...");
        for (T pez : peces) {
            pez.grow(); // Hace crecer cada pez
        }

        reproduccion();
        sellFish();    
    }

    /**
     * Método que maneja la reproducción de los peces en el tanque.
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
     * Agrega un pez al tanque.
     *
     * @param pez pez a añadir
     * @return true si el pez se añadió correctamente, false en caso contrario
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

    /**
     * Vende los peces que han alcanzado la edad óptima y actualiza el sistema de monedas.
     */
    public void sellFish() {
        Iterator<T> iterator = peces.iterator();

        while (iterator.hasNext()) {
            T pez = iterator.next();
            if (pez.getEdad() >= pez.getDatos().getOptimo()) {
                monedas.ganarMonedas(pez.getDatos().getMonedas());
                iterator.remove(); // Eliminar el pez del tanque
            }
        }
    }

    // Getters y Setters

    /**
     * Devuelve el número del tanque.
     *
     * @return número del tanque
     */
    public int getNumeroTanque() {
        return numeroTanque;
    }

    /**
     * Devuelve el número de peces en el tanque.
     *
     * @return número de peces
     */
    public int getNumPeces() {
        return peces.size();
    }

    /**
     * Devuelve la lista de peces en el tanque.
     *
     * @return lista de peces
     */
    public ArrayList<T> getPeces() {
        return peces;
    }

    /**
     * Cuenta las hembras en el tanque.
     *
     * @return número de hembras
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
     * Cuenta los machos en el tanque.
     *
     * @return número de machos
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
     * Cuenta los peces fértiles en el tanque.
     *
     * @return número de peces fértiles
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
     * Cuenta los peces vivos en el tanque.
     *
     * @return número de peces vivos
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
     * Cuenta los peces alimentados en el tanque.
     *
     * @return número de peces alimentados
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
     * Cuenta los peces adultos en el tanque.
     *
     * @return número de peces adultos
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
