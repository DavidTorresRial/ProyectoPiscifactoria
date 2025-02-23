package tanque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import commons.Simulador;
import peces.Pez;
import piscifactoria.Piscifactoria;

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

        int ocupacion = peces.size();
        int vivos = getVivos();
        int alimentados = getAlimentados();
        int adultos = getMaduros();
        int hembras = getHembras();
        int machos = getMachos();
        int fertiles = getFertiles();

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

    /**
     * Muestra la capacidad actual del tanque.
     * 
     * @param piscifactoria la piscifactoría a la que pertenece el tanque.
     */
    public void showCapacity(Piscifactoria piscifactoria) {
        int porcentajeCapacidad = (peces.size() * 100) / capacidadMaxima;
        String mensaje = String.format(
                "Tanque %d de la %s al %d%% de capacidad. [%d/%d]",
                numeroTanque, piscifactoria.getNombre(), porcentajeCapacidad, peces.size(), capacidadMaxima);
        System.out.println(mensaje);
    }

    /**
     * Avanza un día en el tanque, haciendo crecer los peces y ejecutando la reproducción.
     * 
     * @return Un arreglo con el número de peces vendidos y las monedas ganadas.
     */
    public int[] nextDay(boolean comprado) {
        for (Pez pez : peces) {
            pez.grow();
        }
        reproduccion(comprado);
        return sellFish();
    }

    /** Método que maneja la reproducción de los peces en el tanque. */
    public void reproduccion(boolean comprado) {
        int huevosSobrantes = 0;

        boolean hayMachoFertil = false;
        for (Pez pez : peces) {
            if (pez.isSexo() && pez.isFertil()) {
                hayMachoFertil = true;
                break;
            }
        }

        if (hayMachoFertil) {
            List<Pez> hembrasFertiles = new ArrayList<>();
            for (Pez pez : peces) {
                if (pez.isFertil() && pez.isVivo() && !pez.isSexo()) {
                    hembrasFertiles.add(pez);
                }
            }

            if (!hembrasFertiles.isEmpty()) {
                boolean capacidadLlena = false;
                for (int h = 0; h < hembrasFertiles.size(); h++) {
                    if (capacidadLlena) {
                        for (int k = h; k < hembrasFertiles.size(); k++) {
                            huevosSobrantes += hembrasFertiles.get(k).getDatos().getHuevos();
                        }
                        break;
                    }

                    Pez hembra = hembrasFertiles.get(h);
                    int totalHuevos = hembra.getDatos().getHuevos();

                    for (int i = 0; i < totalHuevos; i++) {
                        if (peces.size() < capacidadMaxima) {
                            boolean nuevoSexo = (getHembras() <= getMachos()) ? false : true;
                            Pez nuevoPez = (Pez) hembra.clonar(nuevoSexo);
                            peces.add(nuevoPez);
                            Simulador.estadisticas.registrarNacimiento(hembra.getDatos().getNombre());
                        } else {
                            huevosSobrantes += (totalHuevos - i);
                            capacidadLlena = true;
                            break;
                        }
                    }
                    hembra.setFertil(false);
                }
            }
            if (huevosSobrantes > 0 && comprado) {
                TanqueHuevos.añadirCria(peces.get(0), huevosSobrantes);
            }
        }
    }

    /**
     * Agrega un pez al tanque.
     *
     * @param pez pez a añadir.
     * @return true si el pez se añadió correctamente, false en caso contrario.
     */
    public boolean addFish(Pez pez) {

        if (peces.size() < capacidadMaxima) {
            if (Simulador.monedas.gastarMonedas(pez.getDatos().getCoste())) {
                if (peces.isEmpty() || peces.get(0).getNombre().equals(pez.getNombre())) {
                    peces.add(pez);
                    return true;
                } else {
                    System.out.println("\nTipo de pez incompatible. Solo se pueden agregar peces de tipo: "
                            + peces.get(0).getNombre());
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

    /**
     * Vende los peces maduros, actualiza el sistema de monedas y registra la venta.
     * 
     * @return Un arreglo con el número de peces vendidos y las monedas ganadas.
     */
    public int[] sellFish() {
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
        return new int[] { pecesVendidos, monedasGanadas };
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
                "\n  Peces Adultos       : " + getMaduros() +
                "\n  Hembras             : " + getHembras() +
                "\n  Machos              : " + getMachos() +
                "\n  Peces Fértiles      : " + getFertiles();
    }
}