package piscifactoria;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;

import peces.Pez;
import tanque.Tanque;

import commons.Simulador;

/** Clase abstracta que representa una piscifactoría que gestiona tanques de peces. */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría. */
    protected String nombre;

    /** Lista de tanques con distintos tipos de peces. */
    protected List<Tanque> tanques = new ArrayList<>();
    
    /** Número máximo de tanques permitidos en la piscifactoría. */
    protected final int numeroMaximoTanques = 10;
    
    /** Cantidad actual de comida animal. */
    private int cantidadComidaAnimal;

    /** Cantidad actual de comida vegetal. */
    private int cantidadComidaVegetal;

    /** Capacidad máxima para ambos tipos de comida. */
    protected int capacidadMaximaComida;

    /**
     * Constructor para crear una nueva piscifactoría.
     *
     * @param nombre  El nombre de la piscifactoría.
     */
    public Piscifactoria(String nombre) {        
        this.nombre = nombre;
    }

    /** Muestra toda la información de la piscifactoría. */
    public void showStatus() {
        System.out.println("\n=============== " + nombre + " ===============");
        
        int totalPeces = getTotalPeces();
        int capacidadTotal = getCapacidadTotal();
        int totalVivos = getTotalVivos();
        int totalAlimentados = getTotalAlimentados();
        int totalAdultos = getTotalAdultos();
        int totalHembras = getTotalHembras();
        int totalMachos = getTotalMachos();
        int totalFertiles = getTotalFertiles();
        int porcentajeOcupacion = (capacidadTotal > 0) ? (totalPeces * 100) / capacidadTotal : 0;
        int porcentajeVivos = (totalPeces > 0) ? (totalVivos * 100) / totalPeces : 0;
        int porcentajeAlimentados = (totalVivos > 0) ? (totalAlimentados * 100) / totalVivos : 0;
        int porcentajeAdultos = (totalVivos > 0) ? (totalAdultos * 100) / totalVivos : 0;
    
        System.out.println("Tanques: " + tanques.size());
        System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + porcentajeOcupacion + "%)");
        System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + porcentajeVivos + "%)");
        System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + porcentajeAdultos + "%)");
        System.out.println("Hembras / Machos: " + totalHembras + " / " + totalMachos);
        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);
    
        showFood();
    }

    /** Muestra el estado de cada tanque en la piscifactoría. */
    public void showTankStatus() {
        tanques.forEach(Tanque::showStatus);
    }

    /**
     * Muestra la información de los peces de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showFishStatus(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showFishStatus();
    }

    /**
     * Muestra la ocupación de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showCapacity(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showCapacity();
    }

    /** Muestra el estado actual del depósito de comida de la piscifactoría. */
    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");

        System.out.println("Comida vegetal al " + (cantidadComidaVegetal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaVegetal + "/" + capacidadMaximaComida + "]");
        System.out.println("Comida animal al " + (cantidadComidaAnimal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaAnimal + "/" + capacidadMaximaComida + "]");
    }

    /** Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y actualizando sus estados. */
    public int[] nextDay() {
        int pecesVendidos = 0;
        int monedasGanadas = 0;
    
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            int[] resultadoTanque = tanque.nextDay();
            pecesVendidos += resultadoTanque[0];
            monedasGanadas += resultadoTanque[1];
        }
    
        return new int[]{pecesVendidos, monedasGanadas};
    }
    
    /** Mejora el almacén de comida aumentando su capacidad máxima. */
    public abstract void upgradeFood();

    /** Agrega un tanque, verifica monedas y el límite de tanques. */
    public abstract void addTanque();

    /**
     * Alimenta a los peces en un tanque específico.
     *
     * @param tanque El tanque del que se alimentarán los peces.
     */
    private void alimentarPeces(Tanque tanque) { // TODO alimentar en la clase pez haciendo una jerarquia de clases que extiendan de sus propiedades
        Random rand = new Random();

        for (Pez pez : tanque.getPeces()) {
            if (!pez.isVivo()) {
                continue;
            }

            if (pez instanceof Filtrador) {
                // Filtrador: 50% de probabilidad de no consumir comida
                if (rand.nextDouble() < 0.5) {
                    pez.setAlimentado(true);
                    continue; // No consume comida
                }
                // Intentar alimentar al filtrador
                if (cantidadComidaVegetal > 0) {
                    cantidadComidaVegetal--;
                    pez.setAlimentado(true);
                } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaVegetal() > 0) {
                    // Extraer comida vegetal del almacén
                    if (Simulador.almacenCentral.getCantidadComidaVegetal() >= 1) {
                        Simulador.almacenCentral.setCantidadComidaVegetal(Simulador.almacenCentral.getCantidadComidaVegetal() - 1);
                        cantidadComidaVegetal++;
                        pez.setAlimentado(true);
                    } else {
                        System.out.println("No hay suficiente comida vegetal.");
                    }
                }
            } else if (pez instanceof Carnivoro) {
                // Carnívoro: Consume comida animal
                if (cantidadComidaAnimal > 0) {
                    cantidadComidaAnimal--;
                    pez.setAlimentado(true);
                } else if (Simulador.almacenCentral != null && Simulador.almacenCentral.getCantidadComidaAnimal() > 0) {
                    // Extraer comida animal del almacén
                    if (Simulador.almacenCentral.getCantidadComidaAnimal() >= 1) {
                        Simulador.almacenCentral.setCantidadComidaAnimal(Simulador.almacenCentral.getCantidadComidaAnimal() - 1);
                        cantidadComidaAnimal++;
                        pez.setAlimentado(true);
                    } else {
                        System.out.println("No hay suficiente comida animal.");
                    }
                }
            }

            // Activo: 50% de probabilidad de consumir el doble de comida
            if (pez instanceof Activo) {
                if (pez.isAlimentado()) {
                    // Verificar si puede consumir el doble
                    if (rand.nextDouble() < 0.5) {
                        if (pez instanceof Carnivoro && cantidadComidaAnimal > 0) {
                            cantidadComidaAnimal--; // Consume una unidad adicional si es posible
                        } else if (pez instanceof Filtrador && cantidadComidaVegetal > 0) {
                            cantidadComidaVegetal--; // Consume una unidad adicional si es posible
                        }
                    }
                }
            }
        }
    }

    /**
     * Método para añadir comida animal al almacén.
     * 
     * @param cantidad La cantidad de comida animal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaAnimal(int cantidad) {
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaAnimal = nuevaCantidad;
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida animal: excede la capacidad.");
            return false;
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     * 
     * @param cantidad La cantidad de comida vegetal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaVegetal(int cantidad) {
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaVegetal = nuevaCantidad;          
            return true;
        } else {
            System.out.println("No se puede añadir la cantidad de comida vegetal: excede la capacidad.");
            return false;
        }
    }

    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
        this.cantidadComidaAnimal = cantidadComidaAnimal;
    }

    public void setCantidadComidaVegetal(int cantidadComidaVegetal) {
        this.cantidadComidaVegetal = cantidadComidaVegetal;
    }

    /**
     * Devuelve el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la lista de tanques de la piscifactoría.
     *
     * @return La lista de tanques.
     */
    public List<Tanque> getTanques() {
        return this.tanques;
    }

    /**
     * Obtiene el número máximo de tanques que puede tener la piscifactoría.
     *
     * @return El número máximo de tanques permitidos en la piscifactoría.
     */
    public int getNumeroMaximoTanques() {
        return numeroMaximoTanques;
    }

    /**
     * Obtiene la capacidad máxima de comida.
     * 
     * @return La capacidad máxima de comida.
     */
    public int getCapacidadMaximaComida() {
        return capacidadMaximaComida;
    }

    /**
     * Establece un incremento a la capacidad máxima de comida del depósito.
     * 
     * @param num El valor que se sumará a la capacidad máxima de comida.
     * @return La nueva capacidad máxima de comida después de la suma.
     */
    public int setCapacidadMaximaComida(int num) {
        return capacidadMaximaComida = num;
    }

    /**
     * Devuelve la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @return La cantidad actual de comida vegetal.
     */
    public int getComidaVegetalActual() {
        return cantidadComidaVegetal;
    }

    /**
     * Devuelve la cantidad actual de comida animal en la piscifactoría.
     *
     * @return La cantidad actual de comida animal.
     */
    public int getComidaAnimalActual() {
        return cantidadComidaAnimal;
    }

    /**
     * Devuelve el total de peces en la piscifactoría.
     *
     * @return El total de peces.
     */
    public int getTotalPeces() {
        int totalPeces = 0;
        for (Tanque tanque : tanques) {
            totalPeces += tanque.getPeces().size();
        }
        return totalPeces;
    }

    /**
     * Devuelve la capacidad total de todos los tanques de la piscifactoría.
     *
     * @return La capacidad total de los tanques.
     */
    public int getCapacidadTotal() {
        int capacidadTotal = 0;
        for (Tanque tanque : tanques) {
            capacidadTotal += tanque.getCapacidad();
        }
        return capacidadTotal;
    }

    /**
     * Devuelve el total de peces vivos en la piscifactoría.
     *
     * @return El total de peces vivos.
     */
    public int getTotalVivos() {
        int totalVivos = 0;
        for (Tanque tanque : tanques) {
            totalVivos += tanque.getVivos();
        }
        return totalVivos;
    }

    /**
     * Devuelve el total de peces alimentados en la piscifactoría.
     *
     * @return El total de peces alimentados.
     */
    public int getTotalAlimentados() {
        int totalAlimentados = 0;
        for (Tanque tanque : tanques) {
            totalAlimentados += tanque.getAlimentados();
        }
        return totalAlimentados;
    }

    /**
     * Devuelve el total de peces adultos en la piscifactoría.
     *
     * @return El total de peces adultos.
     */
    public int getTotalAdultos() {
        int totalAdultos = 0;
        for (Tanque tanque : tanques) {
            totalAdultos += tanque.getAdultos();
        }
        return totalAdultos;
    }

    /**
     * Devuelve el total de machos en la piscifactoría.
     *
     * @return El total de machos.
     */
    public int getTotalMachos() {
        int totalMachos = 0;
        for (Tanque tanque : tanques) {
            totalMachos += tanque.getMachos();
        }
        return totalMachos;
    }

    /**
     * Devuelve el total de hembras en la piscifactoría.
     *
     * @return El total de hembras.
     */
    public int getTotalHembras() {
        int totalHembras = 0;
        for (Tanque tanque : tanques) {
            totalHembras += tanque.getHembras();
        }
        return totalHembras;
    }

    /**
     * Devuelve el total de peces feudiles en la piscifactoría.
     *
     * @return El total de peces feudiles.
     */
    public int getTotalFertiles() {
        int totalFertiles = 0;
        for (Tanque tanque : tanques) {
            totalFertiles += tanque.getFertiles();
        }
        return totalFertiles;
    }

    /**
     * Devuelve una representación en cadena del estado de la piscifactoría.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "\nInformación de la Piscifactoría: " + nombre +
                "\n  Número de Tanques    : " + tanques.size() +
                "\n  Total de Peces       : " + getTotalPeces() + " (Ocupación: " + ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) +
                "\n  Peces Vivos          : " + getTotalVivos() +
                "\n  Peces Alimentados    : " + getTotalAlimentados() +
                "\n  Peces Adultos        : " + getTotalAdultos() +
                "\n  Hembras              : " + getTotalHembras() +
                "\n  Machos               : " + getTotalMachos() +
                "\n  Peces Fértiles       : " + getTotalFertiles() +
                "\n  Comida Vegetal       : " + getComidaVegetalActual() + " / " + getCapacidadTotal() +
                "\n  Comida Animal        : " + getComidaAnimalActual() + " / " + getCapacidadTotal();
    }
}
