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
public class Piscifactoria {

    /** El nombre de la piscifactoría. */
    private String nombre;

    /** Indica si la piscifactoria es de rio o no. */
    private boolean esDeRio;

    /** Lista de tanques con distintos tipos de peces. */
    private List<Tanque> tanques = new ArrayList<>();

    /** Capacidad máxima para ambos tipos de comida. */
    private int capacidadMaximaComida;

    /** Cantidad actual de comida animal. */
    private int cantidadComidaAnimal;

    /** Cantidad actual de comida vegetal. */
    private int cantidadComidaVegetal;

    /** Número máximo de tanques permitidos en la piscifactoría. */
    private final int numeroMaximoTanques = 10;

    /** Número de tanques de río. */
    private int numeroTanquesDeRio = 0;

    /** Número de tanques de mar. */
    private int numeroTanquesDeMar = 0;

    /**
     * Constructor para crear una nueva piscifactoría.
     *
     * @param nombre  El nombre de la piscifactoría.
     * @param monedas El sistema de monedas para gestionar el dinero ganado.
     */
    public Piscifactoria(String nombre, boolean esDeRio) {
        this.nombre = nombre;
        this.esDeRio = esDeRio;
        if (esDeRio) {
            tanques.add(new Tanque(tanques.size() + 1, 25));
            numeroTanquesDeRio++;
            capacidadMaximaComida = 25;
        } else {
            tanques.add(new Tanque(tanques.size() + 1, 100));
            numeroTanquesDeMar++;
            capacidadMaximaComida = 100;
        }
    }

    /** Muestra toda la información de la piscifactoría. */
    public void showStatus() {
        System.out.println("\n=============== " + nombre + " ===============");
        System.out.println("Tanques: " + tanques.size());

        System.out.println("Ocupación: " + getTotalPeces() + " / " + getCapacidadTotal() + " (" + ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) + "%)");
        System.out.println("Peces vivos: " + getTotalVivos() + " / " + getTotalPeces() + " (" + ((getTotalPeces() > 0) ? (getTotalVivos() * 100) / getTotalPeces() : 0) + "%)");
        System.out.println("Peces alimentados: " + getTotalAlimentados() + " / " + getTotalVivos() + " (" + ((getTotalVivos() > 0) ? (getTotalAlimentados() * 100) / getTotalVivos() : 0) + "%)");
        System.out.println("Peces adultos: " + getTotalAdultos() + " / " + getTotalVivos() + " (" + ((getTotalVivos() > 0) ? (getTotalAdultos() * 100) / getTotalVivos() : 0) + "%)");
        System.out.println("Hembras / Machos: " + getTotalHembras() + " / " + getTotalMachos());
        System.out.println("Fértiles: " + getTotalFertiles() + " / " + getTotalVivos());

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
    public void nextDay() {
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay();
        }
    }

    /**
     * Mejora el almacén de comida aumentando su capacidad máxima según el tipo de piscifactoría.
     *
     * @return true si la mejora fue exitosa, false si no se pudo realizar.
     */
    public boolean upgradeFood() {
        int costoMejora, incrementoCapacidad, capacidadMaximaPermitida;

        if (esDeRio) {
            costoMejora = 50;
            incrementoCapacidad = 25;
            capacidadMaximaPermitida = 250;
        } else {
            costoMejora = 200;
            incrementoCapacidad = 100;
            capacidadMaximaPermitida = 1000;
        }

        if (capacidadMaximaComida + incrementoCapacidad <= capacidadMaximaPermitida) {
            if (Simulador.monedas.gastarMonedas(costoMejora)) {
                capacidadMaximaComida += incrementoCapacidad;
                System.out.println("Almacén de comida de la piscifactoría " + nombre
                        + " mejorado. Su capacidad ha aumentado en " + incrementoCapacidad + " hasta un total de "
                        + capacidadMaximaComida + ".");
                return true;
            } else {
                System.out.println("No tienes suficientes monedas para mejorar el almacén de comida de la piscifactoría " + nombre + ".");
                return false;
            }
        } else {
            System.out.println("La capacidad máxima del almacén ya ha sido alcanzada para la piscifactoría " + nombre + ".");
            return false;
        }
    }

    /** Agrega un tanque según el tipo (río o mar), verifica monedas y el límite de tanques. */
    public void addTanque() {
        if (tanques.size() < numeroMaximoTanques) {
            if (esDeRio) {
                int costoTanque = 150 * (tanques.size() + 1);
                if (Simulador.monedas.getMonedas() >= costoTanque) {
                    tanques.add(new Tanque(tanques.size() + 1, 25));
                    numeroTanquesDeRio++;
                    Simulador.monedas.gastarMonedas(costoTanque);
                    System.out.println("\nTanque de río agregado exitosamente. " + costoTanque + " monedas han sido descontadas.");
                } else {
                    System.out.println("\nNo tienes suficientes monedas para agregar un tanque de río. Necesitas " + costoTanque + " monedas.");
                }
            } else {
                int costoTanque = 600 * (tanques.size() + 1);
                if (Simulador.monedas.getMonedas() >= costoTanque) {
                    tanques.add(new Tanque(tanques.size() + 1, 100));
                    numeroTanquesDeMar++;
                    Simulador.monedas.gastarMonedas(costoTanque);
                    System.out.println("\nTanque de mar agregado exitosamente. " + costoTanque + " monedas han sido descontadas.");
                } else {
                    System.out.println("\nNo tienes suficientes monedas para agregar un tanque de mar. Necesitas " + costoTanque + " monedas.");
                }
            }
        } else {
            System.out.println("\nNo se pueden agregar más tanques. Se ha alcanzado el límite máximo de tanques.");
        }
    }

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
        System.out.println("Comida vegetal restante: " + cantidadComidaVegetal);
        System.out.println("Comida animal restante: " + cantidadComidaAnimal);
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

    /**
     * Devuelve el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Verifica si la piscifactoría es de río.
     *
     * @return true si la piscifactoría es de río, false en caso contrario.
     */
    public boolean esDeRio() {
        return esDeRio;
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
        return capacidadMaximaComida += num;
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
     * Devuelve el número de tanques de río en la piscifactoría.
     *
     * @return El número de tanques de río.
     */
    public int getNumeroTanquesDeRio() {
        return numeroTanquesDeRio;
    }

    /**
     * Devuelve el número de tanques de mar en la piscifactoría.
     *
     * @return El número de tanques de mar.
     */
    public int getNumeroTanquesDeMar() {
        return numeroTanquesDeMar;
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
        return "Información de la Piscifactoría: " + nombre +
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
