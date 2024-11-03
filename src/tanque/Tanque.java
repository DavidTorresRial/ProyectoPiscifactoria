package tanque;

import java.util.ArrayList;

import peces.Pez;

public class Tanque {

    /** Lista para almacenar los peces en el tanque. */
    public ArrayList<Pez> peces;

    /** Tipo de pez actual permitido en el tanque. */
    private Class<?> tipoPezActual;

    /** Capacidad máxima del tanque. */
    private int capacidadMaxima;

    /** Número del tanque. */
    private int numeroTanque;

    /**
     * Constructor para crear un nuevo tanque.
     *
     * @param capacidadMaxima capacidad máxima del tanque
     * @param monedas         sistema de monedas para gestionar el dinero ganado
     */
    public Tanque(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.peces = new ArrayList<>();
        this.tipoPezActual = null;
    }

    /** Muestra el estado actual del tanque */
    public void showStatus() {
        System.out.println("\n=============== Tanque " + (numeroTanque + 1) + " ===============");

        System.out.println("Ocupación: " + peces.size() + " / " + getCapacidad() + " (" + (peces.size() * 100 / getCapacidad()) + "%)");
        System.out.println("Peces vivos: " + getVivos() + " / " + peces.size() + " (" + (getVivos() > 0 ? (getVivos() * 100 / peces.size()) : 0) + "%)");
        System.out.println("Peces alimentados: " + getAlimentados() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAlimentados() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Peces adultos: " + getAdultos() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAdultos() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Hembras / Machos: " + getHembras() + " / " + getMachos());
        System.out.println("Fértiles: " + getFertiles() + " / " + getVivos());
    }

    /** Muestra el estado de todos los peces del tanque */
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        if (peces.isEmpty()) {
            System.out.println("El tanque está vacío.");
        } else {
            peces.forEach(Pez::showStatus);
        }
    }

    /** Muestra la capacidad actual del tanque */
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]");
    }

    /**
     * Avanza un día en el tanque, haciendo crecer los peces y ejecutando la
     * reproducción
     */
    public void nextDay() {
        for (Pez pez : peces) {
            pez.grow();
        }

        reproduccion();

        if (peces.isEmpty()) {
            tipoPezActual = null;
        }
    }

    /** Método que maneja la reproducción de los peces en el tanque */
    public void reproduccion() {
        int huevosPorHembra = peces.isEmpty() ? 0 : peces.get(0).getDatos().getHuevos();
        ArrayList<Pez> machosFertiles = new ArrayList<>();
        ArrayList<Pez> hembrasFertiles = new ArrayList<>();

        for (Pez pez : peces) {
            if (pez.isFertil()) {
                if (pez.isSexo())
                    machosFertiles.add(pez);
                else
                    hembrasFertiles.add(pez);
            }
        }

        if (machosFertiles.isEmpty() || hembrasFertiles.isEmpty()) {
            System.out.println("No hay suficientes peces fértiles de ambos sexos para reproducirse.");
            return;
        }

        int nuevosMachos = 0, nuevasHembras = 0;
        for (Pez macho : machosFertiles) {
            for (Pez hembra : new ArrayList<>(hembrasFertiles)) {
                for (int i = 0; i < huevosPorHembra && peces.size() < capacidadMaxima; i++) {
                    Pez nuevoPez = (Pez) hembra.clonar(getHembras() <= getMachos());
                    peces.add(nuevoPez);
                    if (nuevoPez.isSexo())
                        nuevosMachos++;
                    else
                        nuevasHembras++;
                }
                hembra.setFertil(false);
                hembrasFertiles.remove(hembra);
            }
            macho.setFertil(false);
        }
        System.out.println("Nuevos peces: " + nuevosMachos + " machos, " + nuevasHembras + " hembras.");
    }

    /**
     * Agrega un pez al tanque.
     *
     * @param pez pez a añadir
     * @return true si el pez se añadió correctamente, false en caso contrario
     */
    public boolean addPez(Pez pez) {
        if (peces.size() >= capacidadMaxima) {
            System.out.println("El tanque está lleno. Capacidad máxima alcanzada.");
            return false;
        }

        if (tipoPezActual == null) {
            tipoPezActual = pez.getClass();

        } else if (!tipoPezActual.equals(pez.getClass())) {
            System.out.printf("Este tanque solo acepta peces del tipo: %s, pero se intentó añadir: %s\n", tipoPezActual.getSimpleName(), pez.getClass().getSimpleName());
            return false;
        }

        peces.add(pez);
        return true;
    }

    /**
     * Devuelve el tipo de pez actual.
     * 
     * @return la clase del tipo de pez actual.
     */
    public Class<?> getTipoPezActual() {
        return tipoPezActual;
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
     * Devuelve la capacidad del tanque.
     * 
     * @return capacidad máxima del tanque
     */
    public int getCapacidad() {
        return capacidadMaxima;
    }

    /**
     * Devuelve el número de peces del tanque.
     * 
     * @return número de peces en el tanque
     */
    public int getNumPeces() {
        return peces.size();
    }

    /**
     * Devuelve la lista de peces en el tanque.
     * 
     * @return lista de peces en el tanque
     */
    public ArrayList<Pez> getPeces() {
        return peces;
    }

    /**
     * Cuenta y devuelve el número de hembras en el tanque.
     * 
     * @return número de hembras en el tanque
     */
    public int getHembras() {
        // Contar hembras
        int hembras = 0;
        for (Pez pez : peces) {
            if (!pez.isSexo()) { // false es hembra
                hembras++;
            }
        }
        return hembras;
    }

    /**
     * Cuenta y devuelve el número de machos en el tanque.
     *
     * @return número de machos en el tanque.
     */
    public int getMachos() {
        // Contar machos
        int machos = 0;
        for (Pez pez : peces) {
            if (pez.isSexo()) { // true si es macho
                machos++;
            }
        }
        return machos;
    }

    /**
     * Cuenta y devuelve el número de peces fértiles en el tanque.
     *
     * @return número de peces fértiles en el tanque.
     */
    public int getFertiles() {
        // Contar peces fértiles
        int fertiles = 0;
        for (Pez pez : peces) {
            if (pez.isFertil()) {
                fertiles++;
            }
        }
        return fertiles;
    }

    /**
     * Cuenta y devuelve el número de peces vivos en el tanque.
     *
     * @return número de peces vivos en el tanque.
     */
    public int getVivos() {
        // Contar peces vivos
        int pecesVivos = 0;
        for (Pez pez : peces) {
            if (pez.isVivo()) {
                pecesVivos++;
            }
        }
        return pecesVivos;
    }

    /**
     * Cuenta y devuelve el número de peces alimentados en el tanque.
     *
     * @return número de peces alimentados en el tanque.
     */
    public int getAlimentados() {
        // Contar peces alimentados
        int pecesAlimentados = 0;
        for (Pez pez : peces) {
            if (pez.isAlimentado()) {
                pecesAlimentados++;
            }
        }
        return pecesAlimentados;
    }

    /**
     * Cuenta y devuelve el número de peces adultos en el tanque.
     *
     * @return número de peces adultos en el tanque.
     */
    public int getAdultos() {
        // Contar peces adultos
        int pecesAdultos = 0;
        for (Pez pez : peces) {
            if (pez.getEdad() >= pez.getDatos().getMadurez()) {
                pecesAdultos++;
            }
        }
        return pecesAdultos;
    }

    /**
     * Devuelve una representación en cadena del estado del tanque, incluyendo su número,
     * capacidad, número de peces, tipo de pez permitido, y estadísticas de los peces.
     * 
     * @return una cadena que representa el estado del tanque.
     */
    @Override
    public String toString() {
        return "Tanque " + numeroTanque + ":\n" +
                "Capacidad: " + capacidadMaxima + "\n" +
                "Peces en el tanque: " + peces.size() + "\n" +
                "Tipo de pez permitido: " + (tipoPezActual != null ? tipoPezActual.getSimpleName() : "Ninguno") + "\n" +
                "Peces vivos: " + getVivos() + "\n" +
                "Peces alimentados: " + getAlimentados() + "\n" +
                "Peces adultos: " + getAdultos() + "\n" +
                "Hembras: " + getHembras() + ", Machos: " + getMachos() + "\n" +
                "Peces fértiles: " + getFertiles() + "\n";
    }
}