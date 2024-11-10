package tanque;

import java.util.List;

import commons.Simulador;
import commons.SistemaMonedas;

import java.util.ArrayList;

import peces.Pez;

/** Representa un tanque para almacenar peces con capacidades de gestión y reproducción. */
public class Tanque {

    /** Lista para almacenar los peces en el tanque. */
    private List<Pez> peces = new ArrayList<>();

    /** Tipo de pez actual permitido en el tanque. */
    private Class<?> tipoPezActual;

    /** Número del tanque. */
    private int numeroTanque;

    /** Capacidad máxima del tanque. */
    private int capacidadMaxima;

    SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /**
     * Crea un tanque con el número y capacidad especificados.
     * 
     * @param numeroTanque    El identificador del tanque.
     * @param capacidadMaxima La capacidad máxima del tanque.
     */
    public Tanque(int numeroTanque, int capacidadMaxima) {
        this.numeroTanque = numeroTanque;
        this.capacidadMaxima = capacidadMaxima;
        this.tipoPezActual = null;
    }

    /** Muestra el estado actual del tanque. */
    public void showStatus() {
        System.out.println("\n=============== Tanque " + numeroTanque + " ===============");

        System.out.println("Ocupación: " + peces.size() + " / " + capacidadMaxima + " (" + (peces.size() * 100 / capacidadMaxima) + "%)");
        System.out.println("Peces vivos: " + getVivos() + " / " + peces.size() + " (" + (getVivos() > 0 ? (getVivos() * 100 / peces.size()) : 0) + "%)");
        System.out.println("Peces alimentados: " + getAlimentados() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAlimentados() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Peces adultos: " + getAdultos() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAdultos() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Hembras / Machos: " + getHembras() + " / " + getMachos());
        System.out.println("Fértiles: " + getFertiles() + " / " + getVivos());
    }

    /** Muestra el estado de todos los peces del tanque. */
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        if (peces.isEmpty()) {
            System.out.println("El tanque está vacío.");
        } else {
            peces.forEach(Pez::showStatus);
        }
    }

    /** Muestra la capacidad actual del tanque. */
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]");
    }

    /** Avanza un día en el tanque, haciendo crecer los peces y ejecutando la reproducción. */
    public void nextDay() {
        for (Pez pez : peces) {
            pez.grow();
        }

        reproduccion();

        if (peces.isEmpty()) {
            tipoPezActual = null;
        }
    }

    /** Método que maneja la reproducción de los peces en el tanque. */
    public void reproduccion() {
        int nuevosMachos = 0, nuevasHembras = 0;

        boolean hayMachoFertil = false;
        for (Pez pez : peces) {
            if (pez.isSexo() && pez.isFertil()) {
                hayMachoFertil = true;
                break;
            }
        }

        if (hayMachoFertil) {
            List<Pez> nuevosPeces = new ArrayList<>();
            for (Pez pez : peces) {
                if (pez.isFertil() && !pez.isSexo()) {
                    for (int i = 0; i < pez.getDatos().getHuevos(); i++) {
                        if (peces.size() < capacidadMaxima) {
                            boolean nuevoSexo = (getHembras() <= getMachos()) ? false : true;

                            Pez nuevoPez = (Pez) pez.clonar(nuevoSexo);
                            nuevosPeces.add(nuevoPez);

                            if (nuevoSexo) {
                                nuevosMachos++;
                                Simulador.estadisticas.registrarNacimiento(pez.getDatos().getNombre());
                            } else {
                                nuevasHembras++;
                                Simulador.estadisticas.registrarNacimiento(pez.getDatos().getNombre());
                            }
                        } else {
                            System.out.println("No hay espacio para añadir más peces. Capacidad máxima alcanzada.");
                            break;
                        }
                    }
                    pez.setFertil(false);
                }
            }
            peces.addAll(nuevosPeces);

            System.out.println(
                    "Se han creado " + nuevosMachos + " nuevos machos y " + nuevasHembras + " nuevas hembras.");
        } else {
            System.out.println("No hay machos fértiles en el tanque. No se realizará reproducción.");
        }
    }

    /**
     * Agrega un pez al tanque.
     *
     * @param pez pez a añadir.
     * @return true si el pez se añadió correctamente, false en caso contrario.
     */
    public boolean addFish(Pez pez) {
        if (peces.size() >= capacidadMaxima) {
            System.out.println("El tanque está lleno. Capacidad máxima alcanzada.");
            return false;
        }
        if (monedas.gastarMonedas(pez.getDatos().getCoste())) {
            if (tipoPezActual == null) {
                tipoPezActual = pez.getClass();
            }
            peces.add(pez);
            return true;
        }
        return false;
    }

    /**
     * Devuelve la lista de peces en el tanque.
     * 
     * @return lista de peces en el tanque.
     */
    public List<Pez> getPeces() {
        return peces;
    }

    /**
     * Devuelve el tipo de pez actual.
     * 
     * @return la clase del tipo de pez actual.
     */
    public Class<?> getTipoPezActual() {
        return tipoPezActual;
    }

    /**
     * Devuelve el número del tanque.
     *
     * @return número del tanque.
     */
    public int getNumeroTanque() {
        return numeroTanque;
    }

    /**
     * Devuelve la capacidad del tanque.
     * 
     * @return capacidad máxima del tanque.
     */
    public int getCapacidad() {
        return capacidadMaxima;
    }

    /**
     * Devuelve el número de peces del tanque.
     * 
     * @return número de peces en el tanque.
     */
    public int getNumPeces() {
        return peces.size();
    }

    /**
     * Cuenta y devuelve el número de machos en el tanque.
     *
     * @return número de machos en el tanque.
     */
    public int getMachos() {
        int machos = 0;
        for (Pez pez : peces) {
            if (pez.isSexo()) {
                machos++;
            }
        }
        return machos;
    }

    /**
     * Cuenta y devuelve el número de hembras en el tanque.
     * 
     * @return número de hembras en el tanque.
     */
    public int getHembras() {
        int hembras = 0;
        for (Pez pez : peces) {
            if (!pez.isSexo()) {
                hembras++;
            }
        }
        return hembras;
    }

    /**
     * Cuenta y devuelve el número de peces fértiles en el tanque.
     *
     * @return número de peces fértiles en el tanque.
     */
    public int getFertiles() {
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
        int pecesAdultos = 0;
        for (Pez pez : peces) {
            if (pez.getEdad() >= pez.getDatos().getMadurez()) {
                pecesAdultos++;
            }
        }
        return pecesAdultos;
    }

    /**
     * Devuelve una representación en cadena del estado del tanque, incluyendo su número, capacidad, 
     * número de peces, tipo de pez permitido, y estadísticas de los peces.
     * 
     * @return una cadena que representa el estado del tanque.
     */
    @Override
    public String toString() {
        return "\nTanque " + numeroTanque + ":\n" +
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