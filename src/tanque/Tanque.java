package tanque;

import java.util.ArrayList;

import peces.Pez;

public class Tanque<T extends Pez> { // TODO revisar si el tanque tienen que venir ya definido con un tipo de pez que se puede meter 
    public ArrayList<T> peces; // Lista para almacenar los peces

    private int capacidadMaxima; // Capacidad máxima del tanque
    private int numeroTanque; // Número del tanque
    private static int contadorTanques = 0;

    // Constructor
    public Tanque(int capacidadMaxima) {
        contadorTanques++; // Incrementa el contador estático
        this.numeroTanque = contadorTanques; // TODO hacer el contador de tanques mejor en piscifactoría 
        
        

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
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]");
    }

    // Pasa un día en el tanque 
    public void nextDay() { // TODO
        System.out.println("Avanzando al siguiente día para el Tanque " + numeroTanque + "...");
        for (T pez : peces) {
            pez.grow(); // Hace crecer cada pez
        }

        // Realiza el proceso de reproducción
        //realizarReproduccion

        // Vender los peces que han alcanzado la edad óptima
        //venderPecesAdultos
    }

    // Metodo para agregar un pez al tanque
    public boolean agregarPez(T pez) { 
        if (peces.size() < capacidadMaxima) { // TODO la capadidad maxima de peces está limitada por Piscifactoria y no por Tanque¿?¿?
            peces.add(pez);
            return true;
        } else {
            System.out.println("No se puede agregar el pez. Tanque lleno.");
            return false;
        }
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
