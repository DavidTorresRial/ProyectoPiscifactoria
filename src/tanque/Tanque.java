package tanque;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import commons.Simulador;

import peces.Pez;

/** Representa un tanque para almacenar peces con capacidades de gestión y reproducción. */
public class Tanque {

    /** Lista para almacenar los peces en el tanque. */
    private List<Pez> peces = new ArrayList<>();

    /** Número del tanque. */
    private final int numeroTanque;

    /** Capacidad máxima del tanque. */
    private final int capacidadMaxima;

    /**
     * Crea un tanque con el número y capacidad especificados.
     * 
     * @param numeroTanque    El identificador del tanque.
     * @param capacidadMaxima La capacidad máxima del tanque.
     */
    public Tanque(int numeroTanque, int capacidadMaxima) {
        this.numeroTanque = numeroTanque;
        this.capacidadMaxima = capacidadMaxima;
    }

    /** Muestra el estado actual del tanque. */
    public void showStatus() {
        System.out.println("\n=============== Tanque " + numeroTanque + " ===============");

        int vivos = getVivos();
        int alimentados = getAlimentados();
        int adultos = getAdultos();
        int hembras = getHembras();
        int machos = getMachos();
        int fertiles = getFertiles();
        int ocupacion = peces.size();
        int porcentajeOcupacion = (ocupacion * 100 / capacidadMaxima);
        int porcentajeVivos = (ocupacion > 0 ? (vivos * 100 / ocupacion) : 0);
        int porcentajeAlimentados = (vivos > 0 ? (alimentados * 100 / vivos) : 0);
        int porcentajeAdultos = (vivos > 0 ? (adultos * 100 / vivos) : 0);

        System.out.println("Ocupación: " + ocupacion + " / " + capacidadMaxima + " (" + porcentajeOcupacion + "%)");
        System.out.println("Peces vivos: " + vivos + " / " + ocupacion + " (" + porcentajeVivos + "%)");
        System.out.println("Peces alimentados: " + alimentados + " / " + vivos + " (" + porcentajeAlimentados + "%)");
        System.out.println("Peces adultos: " + adultos + " / " + vivos + " (" + porcentajeAdultos + "%)");
        System.out.println("Hembras / Machos: " + hembras + " / " + machos);
        System.out.println("Fértiles: " + fertiles + " / " + vivos);
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
        sellFish();
    }

    /** Método que maneja la reproducción de los peces en el tanque. */
    public void reproduccion() {
        int nuevosMachos = 0, nuevasHembras = 0;

        boolean hayMachoFertil = false;
        for (Pez pez : peces) {
            if (pez.isSexo() && pez.isFertil()) {
                hayMachoFertil = true;
            }
        }

        if (hayMachoFertil) {
            List<Pez> hembrasFertiles = new ArrayList<>();

            for (Pez pez : peces) {
                if (pez.isFertil() && pez.isVivo()) {
                    if (!pez.isSexo()) {
                        hembrasFertiles.add(pez);
                    }
                }
            }

            if (!hembrasFertiles.isEmpty()) {
                for (Pez hembra : hembrasFertiles) {
                    for (int i = 0; i < hembra.getDatos().getHuevos(); i++) {
                        if (peces.size() < capacidadMaxima) {
                            boolean nuevoSexo = (getHembras() <= getMachos()) ? false : true;

                            Pez nuevoPez = (Pez) hembra.clonar(nuevoSexo);
                            peces.add(nuevoPez);

                            if (nuevoSexo) {
                                nuevosMachos++;
                                Simulador.estadisticas.registrarNacimiento(hembra.getDatos().getNombre());
                            } else {
                                nuevasHembras++;
                                Simulador.estadisticas.registrarNacimiento(hembra.getDatos().getNombre());
                            }
                        } else {
                            System.out.println("No hay espacio para añadir más peces. Capacidad máxima alcanzada.");
                            break;
                        }
                    }
                    hembra.setFertil(false);
                }
            }
            System.out.println("\nSe han creado " + nuevosMachos + " nuevos machos y " + nuevasHembras + " nuevas hembras.");
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
        if (Simulador.monedas.gastarMonedas(pez.getDatos().getCoste())) {
            if (peces.isEmpty() || peces.get(0).getNombre().equals(pez.getNombre())) {
                peces.add(pez);
                return true;
            }
        }
        return false;
    }

    /** Vende los peces maduros y actualiza el sistema de monedas. */
    public void sellFish() {
        int pecesVendidos = 0, monedasGanadas = 0;

        Iterator<Pez> iterator = peces.iterator();
        while (iterator.hasNext()) {
            Pez pez = iterator.next();
            if (pez.getEdad() >= pez.getDatos().getOptimo() && pez.isVivo()) {
                Simulador.monedas.ganarMonedas(pez.getDatos().getMonedas());
                Simulador.estadisticas.registrarVenta(pez.getNombre(), pez.getDatos().getMonedas());

                monedasGanadas += pez.getDatos().getMonedas();
                pecesVendidos++;
                iterator.remove();
            }
        }
        if (pecesVendidos > 0) {
            System.out.println(pecesVendidos + " peces vendidos por " + monedasGanadas + " monedas.");
        } else {
            System.out.println("\nNo hay peces adultos para vender.");
        }
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
            if (pez.isMaduro()) {
                pecesAdultos++;
            }
        }
        return pecesAdultos;
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
     * Cuenta y devuelve el número de peces maduros en el tanque.
     *
     * @return número de peces maduros en el tanque.
     */
    public int getMaduros() {
        int maduros = 0;
        for (Pez pez : peces) {
            if (pez.isMaduro()) {
                maduros++;
            }
        }
        return maduros;
    }

    /**
     * Devuelve una representación en cadena del estado del tanque.
     * 
     * @return una cadena que representa el estado del tanque.
     */
    @Override
    public String toString() {
        return "\nInformación del Tanque: " + numeroTanque +
                "\n  Capacidad Máxima    : " + capacidadMaxima +
                "\n  Peces en el Tanque  : " + peces.size() +
                "\n  Tipo de Pez         : " + (!peces.isEmpty() ? peces.get(0).getNombre() : "Ninguno") +
                "\n  Peces Vivos         : " + getVivos() +
                "\n  Peces Alimentados   : " + getAlimentados() +
                "\n  Peces Adultos       : " + getAdultos() +
                "\n  Hembras             : " + getHembras() +
                "\n  Machos              : " + getMachos() +
                "\n  Peces Fértiles      : " + getFertiles();
    }
}