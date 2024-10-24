package tanque;

import java.util.ArrayList;
import java.util.Random;

import peces.Pez;

public class Tanque<T extends Pez> {
    public ArrayList<T> peces; // Lista para almacenar los peces

    private int capacidadMaxima; // Capacidad máxima del tanque
    private int numeroTanque; // Número del tanque

    // Constructor
    public Tanque(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.peces = new ArrayList<>();
    }

    // Metodo para mostrar el estado del tanque
    public void showStatus() {
        System.out.println("\n=============== Tanque " + numeroTanque + " ===============");

        System.out.println("Ocupación: " + peces.size() + " / " + capacidadMaxima + " (" + (peces.size() * 100 / capacidadMaxima) + "%)");
        System.out.println("Peces vivos: " + getVivos() + " / " + peces.size() + " (" + (getVivos() > 0 ? (getVivos() * 100 / peces.size()) : 0) + "%)");
        System.out.println("Peces alimentados: " + getAlimentados() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAlimentados() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Peces adultos: " + getAdultos() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAdultos() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Hembras / Machos: " + getHembras() + " / " + getMachos());
        System.out.println("Fértiles: " + getFertiles() + " / " + getVivos());
    }

    // Muestra el estado de los peces
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        for (T pez : peces) {
            pez.showStatus();
        }
    }

    // Muestra la capacidad del tanque
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]"); // TODO añadir nombre de la piscifacoria
    }

    // Pasa un día en el tanque 
    public void nextDay() { // TODO
        System.out.println("Avanzando al siguiente día para el Tanque " + numeroTanque + "...");
        for (T pez : peces) {
            pez.grow(); // Hace crecer cada pez
        }

        reproduccion();

        // Vender los peces que han alcanzado la edad óptima
        //venderPecesAdultos
    }

    public void reproduccion() {
        Random rand = new Random();
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
                break; // Si no quedan hembras fértiles, salir del bucle de machos
            }

            for (T hembra : new ArrayList<>(hembrasFertiles)) { // Usar una copia para evitar ConcurrentModificationException
                for (int i = 0; i < huevosPorHembra; i++) {
                    boolean nuevoSexo = rand.nextBoolean();
                    T nuevoPez = (T) hembra.clonar(nuevoSexo); // Clonar el pez

                    if (peces.size() < capacidadMaxima) {
                        peces.add(nuevoPez); // Agregar el nuevo pez al tanque
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
                hembra.setFertil(false); // La hembra deja de ser fértil después de reproducirse
                hembrasFertiles.remove(hembra); // Remover la hembra de la lista de fértiles
            }
            macho.setFertil(false); // El macho deja de ser fértil después de reproducirse
        }

        System.out.println("Se han creado " + nuevosMachos + " nuevos machos y " + nuevasHembras + " nuevas hembras.");
    }


    //Getters y Setters
    
    // Devuelve el número del tanque
    public int getNumeroTanque() {
        return numeroTanque;
    }

    // Devuelve el numero de peces del tanque
    public int getNumPeces() {
        return peces.size();
    }

    // Devuelve los peces del tanque
    public ArrayList<T> getPeces() {
        return peces;
    }

    // Cuenta las hembras del tanque
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
    
    // Cuenta los machos del tanque
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

    // Cuenta los peces fertiles del tanque
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

    // Cuenta los peces vivos del tanque
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

    // Cuenta los peces vivos del tanque
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

    // Cuenta los peces adultos del tanque
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
