package tanque;

import java.util.ArrayList;
import java.util.Iterator;

import commons.SistemaMonedas;
import peces.Pez;

public class Tanque {
    public ArrayList<Pez> peces; // Lista para almacenar los peces
    private Class<?> tipoPezActual; // Tipo de pez actual en el tanque

    private int capacidadMaxima; // Capacidad máxima del tanque
    private int numeroTanque; // Número del tanque
    private SistemaMonedas monedas;

    // Constructor
    public Tanque(int capacidadMaxima, SistemaMonedas monedas) {
        this.capacidadMaxima = capacidadMaxima;
        this.peces = new ArrayList<>();
        this.tipoPezActual = null;
        this.monedas = monedas;
    }

    // Metodo para mostrar el estado del tanque
    public void showStatus() {
        System.out.println("\n=============== Tanque " + numeroTanque + " ===============");

        System.out.println("Ocupación: " + peces.size() + " / " + getCapacidad() + " (" + (peces.size() * 100 / getCapacidad()) + "%)");
        System.out.println("Peces vivos: " + getVivos() + " / " + peces.size() + " (" + (getVivos() > 0 ? (getVivos() * 100 / peces.size()) : 0) + "%)");
        System.out.println("Peces alimentados: " + getAlimentados() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAlimentados() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Peces adultos: " + getAdultos() + " / " + getVivos() + " (" + (getVivos() > 0 ? (getAdultos() * 100 / getVivos()) : 0) + "%)");
        System.out.println("Hembras / Machos: " + getHembras() + " / " + getMachos());
        System.out.println("Fértiles: " + getFertiles() + " / " + getVivos());
    }

    // Muestra el estado de los peces
    public void showFishStatus() {
        System.out.println("--------------- Peces en el Tanque " + numeroTanque + " ---------------");
        for (Pez pez : peces) {
            pez.showStatus();
        }
    }

    // Muestra la capacidad del tanque
    public void showCapacity() {
        System.out.println("Tanque " + numeroTanque + " de la piscifactoría al " + (peces.size() * 100 / capacidadMaxima) + "% de capacidad. [" + peces.size() + "/" + capacidadMaxima + "]"); // TODO añadir nombre de la piscifacoria
    }

    // Pasa un día en el tanque 
    public void nextDay() { 
        for (Pez pez : peces) {
            pez.grow(); // Hace crecer cada pez
        }

        reproduccion();
        sellFish();    
    }

    // Método de reproducción
    public void reproduccion() {
        int huevosPorHembra = 0;

        if (!peces.isEmpty()) {
            huevosPorHembra = peces.get(0).getHuevos();
        }

        int nuevosMachos = 0;
        int nuevasHembras = 0;

        // Listas para almacenar los peces fértiles
        ArrayList<Pez> machosFertiles = new ArrayList<>();
        ArrayList<Pez> hembrasFertiles = new ArrayList<>();

        // Clasificar los peces fértiles
        for (Pez pez : peces) {
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
        for (Pez macho : machosFertiles) {
            if (hembrasFertiles.isEmpty()) {
                break; 
            }

            for (Pez hembra : new ArrayList<>(hembrasFertiles)) { 
                for (int i = 0; i < huevosPorHembra; i++) {
                    boolean nuevoSexo;
                    if (getHembras() <= getMachos()) {
                        nuevoSexo = false; 
                    } else {
                        nuevoSexo = true; 
                    }

                    Pez nuevoPez = (Pez) hembra.clonar(nuevoSexo); 

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

    public boolean addPez(Pez pez) {
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

     // Método getter para obtener el tipo de pez actual
     public Class<?> getTipoPezActual() {
        return tipoPezActual;
    }

    public void sellFish() {
        Iterator<Pez> iterator = peces.iterator();
        int pecesVendidos = 0;
        int monedasGanadas = 0;
    
        while (iterator.hasNext()) {
            Pez pez = iterator.next();
            if (pez.getEdad() >= pez.getDatos().getOptimo()) {
                int monedasPez = pez.getDatos().getMonedas();
                monedas.ganarMonedas(monedasPez);
                monedasGanadas += monedasPez;
                iterator.remove(); // Eliminar el pez del tanque
                pecesVendidos++;
            }
        }
    
        System.out.println("Peces vendidos: " + pecesVendidos);
        System.out.println("Monedas ganadas: " + monedasGanadas);
    }


    //Getters y Setters
    
    // Devuelve el número del tanque
    public int getNumeroTanque() {
        return numeroTanque;
    }

    // Devuelve la capacidad máxima del tanque
    public int getCapacidad() {
        return capacidadMaxima;
    }

    // Devuelve el numero de peces del tanque
    public int getNumPeces() {
        return peces.size();
    }

    // Devuelve los peces del tanque
    public ArrayList<Pez> getPeces() {
        return peces;
    }

    // Cuenta las hembras del tanque
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
    
    // Cuenta los machos del tanque
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

    // Cuenta los peces fertiles del tanque
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

    // Cuenta los peces vivos del tanque
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

    // Cuenta los peces vivos del tanque
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

    // Cuenta los peces adultos del tanque
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
}
