package piscifactoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;

import peces.Pez;
import tanque.Tanque;

import commons.AlmacenCentral;
import commons.Simulador;
import commons.SistemaMonedas;

/** Clase abstracta que representa una piscifactoría que gestiona tanques de peces. */
public class Piscifactoria {

    /** El nombre de la piscifactoría. */
    private String nombre;

    /** Lista de tanques con distintos tipos de peces. */
    private List<Tanque> tanques = new ArrayList<>();

    /** Sistema de monedas para gestionar el dinero ganado */
    private SistemaMonedas monedas = SistemaMonedas.getInstancia();

    /** Capacidad máxima compartida para ambos tipos de comida. */
    private int capacidadMaximaComidaPiscifactoria;

    /** Cantidad actual de comida vegetal. */
    private int comidaVegetalActual;

    /** Cantidad actual de comida animal. */
    private int comidaAnimalActual;

    private final int numeroMaximoTanquesPiscifactoria = 10;

    /** Número de tanques de río. */
    private int numeroTanquesDeRio = 0;

    /** Número de tanques de mar. */
    private int numeroTanquesDeMar = 0;

    /** Almacén central para gestionar el almacenamiento de comida. */
    private AlmacenCentral almacenCentral;

    /** Indica si la piscifactoria es de rio o no. */
    private boolean esDeRio;

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
            capacidadMaximaComidaPiscifactoria = 25;
        } else {
            tanques.add(new Tanque(tanques.size() + 1, 100));
            numeroTanquesDeMar++;
            capacidadMaximaComidaPiscifactoria = 100;
        }
    }

    /** Agrega un tanque según el tipo (río o mar), verifica monedas y el límite de tanques. */
    public void addTanque() {
        if (tanques.size() < numeroMaximoTanquesPiscifactoria) {
            if (esDeRio) {
                int costoTanque = 150 * (tanques.size() + 1);
                if (monedas.getMonedas() >= costoTanque) {
                    tanques.add(new Tanque(tanques.size() + 1, 25));
                    numeroTanquesDeRio++;
                    monedas.gastarMonedas(costoTanque);
                    System.out.println("\nTanque de río agregado exitosamente. " + costoTanque + " monedas han sido descontadas.");
                } else {
                    System.out.println("\nNo tienes suficientes monedas para agregar un tanque de río. Necesitas " + costoTanque + " monedas.");
                }
            } else {
                int costoTanque = 600 * (tanques.size() + 1);
                if (monedas.getMonedas() >= costoTanque) {
                    tanques.add(new Tanque(tanques.size() + 1, 100));
                    numeroTanquesDeMar++;
                    monedas.gastarMonedas(costoTanque);
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
     * Devuelve la lista de tanques de la piscifactoría.
     *
     * @return La lista de tanques.
     */
    public List<Tanque> getTanques() {
        return this.tanques;
    }

    /** Muestra toda la información de la piscifactoría. */
    public void showStatus() { // TODO David
        System.out.println("\n=============== " + nombre + " ===============");
        System.out.println("Tanques: " + tanques.size());

        int totalPeces = getTotalPeces();
        int totalVivos = getTotalVivos();
        int totalAlimentados = getTotalAlimentados();
        int totalAdultos = getTotalAdultos();
        int totalHembras = getTotalHembras();
        int totalFertiles = getTotalFertiles();
        int capacidadTotal = getCapacidadTotal();

        int porcentajeOcupacion = (capacidadTotal > 0) ? (totalPeces * 100) / capacidadTotal : 0;
        System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + porcentajeOcupacion + "%)");

        int porcentajeVivos = (totalPeces > 0) ? (totalVivos * 100) / totalPeces : 0;
        System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + porcentajeVivos + "%)");

        int porcentajeAlimentados = (totalVivos > 0) ? (totalAlimentados * 100) / totalVivos : 0;
        System.out.println(
                "Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");

        int porcentajeAdultos = (totalVivos > 0) ? (totalAdultos * 100) / totalVivos : 0;
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + porcentajeAdultos + "%)");

        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));

        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);

        showFood();
    }

    /** Muestra el estado de cada tanque en la piscifactoría. */
    public void showTankStatus() {
        for (Tanque tanque : tanques) {
            tanque.showStatus();
        }
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

    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");

        System.out.println("Comida vegetal al " + (comidaVegetalActual * 100 / capacidadMaximaComidaPiscifactoria) + "% de su capacidad. [" + comidaVegetalActual + "/" + capacidadMaximaComidaPiscifactoria + "]");
        System.out.println("Comida animal al " + (comidaAnimalActual * 100 / capacidadMaximaComidaPiscifactoria) + "% de su capacidad. [" + comidaAnimalActual + "/" + capacidadMaximaComidaPiscifactoria + "]");

    }

    /** Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y actualizando sus estados. */
    public void nextDay() {
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay();
            //sellFish(); // TODO revisar 
        }
    }

    /**
     * Alimenta a los peces en un tanque específico.
     *
     * @param tanque El tanque del que se alimentarán los peces.
     */
    private void alimentarPeces(Tanque tanque) {
        Random rand = new Random();

        for (Pez pez : tanque.getPeces()) {
            if (!pez.isVivo()) {
                continue;
            }

            boolean alimentado = false;

            if (pez instanceof Filtrador) {
                // Filtrador: 50% de probabilidad de no consumir comida
                if (rand.nextDouble() < 0.5) {
                    continue; // No consume comida
                }
                // Intentar alimentar al filtrador
                if (comidaVegetalActual > 0) {
                    comidaVegetalActual--;
                    alimentado = true;
                } else if (Simulador.almacenCentral != null && almacenCentral.getCantidadComidaVegetal() > 0) {
                    // Extraer comida vegetal del almacén
                    if (almacenCentral.getCantidadComidaVegetal() >= 1) {
                        almacenCentral.setCantidadComidaVegetal(almacenCentral.getCantidadComidaVegetal() - 1);
                        comidaVegetalActual++;
                        alimentado = true;
                    } else {
                        System.out.println("No hay suficiente comida vegetal.");
                    }
                }
            } else if (pez instanceof Carnivoro) {
                // Carnívoro: Consume comida animal
                if (comidaAnimalActual > 0) {
                    comidaAnimalActual--;
                    alimentado = true;
                } else if (Simulador.almacenCentral != null && almacenCentral.getCantidadComidaAnimal() > 0) {
                    // Extraer comida animal del almacén
                    if (almacenCentral.getCantidadComidaAnimal() >= 1) {
                        almacenCentral.setCantidadComidaAnimal(almacenCentral.getCantidadComidaAnimal() - 1);
                        comidaAnimalActual++;
                        alimentado = true;
                    } else {
                        System.out.println("No hay suficiente comida animal.");
                    }
                }
            }

            // Activo: 50% de probabilidad de consumir el doble de comida
            if (pez instanceof Activo) {
                if (alimentado) {
                    // Verificar si puede consumir el doble
                    if (rand.nextDouble() < 0.5) {
                        if (pez instanceof Carnivoro && comidaAnimalActual > 0) {
                            comidaAnimalActual--; // Consume una unidad adicional si es posible
                        } else if (pez instanceof Filtrador && comidaVegetalActual > 0) {
                            comidaVegetalActual--; // Consume una unidad adicional si es posible
                        }
                    }
                }
            }

            if (alimentado) {
                pez.setAlimentado(true);
            }
        }
        System.out.println("Comida vegetal restante: " + comidaVegetalActual);
        System.out.println("Comida animal restante: " + comidaAnimalActual);
    }

    /** Vende los peces maduros y actualiza el sistema de monedas. */
    public void sellFish() {
        int totalPecesVendidos = 0, totalMonedasGanadas = 0;

        String nombrePiscifactoria = this.getNombre();

        for (Tanque tanque : tanques) {
            Iterator<? extends Pez> iterator = tanque.getPeces().iterator();
            while (iterator.hasNext()) {
                Pez pez = iterator.next();
                if (pez.getEdad() >= pez.getDatos().getMadurez() && pez.isVivo()) {
                    int monedasPez = pez.getDatos().getMonedas();
                    monedas.ganarMonedas(monedasPez);
                    totalMonedasGanadas += monedasPez;
                    iterator.remove();
                    totalPecesVendidos++;
                }
            }
        }
        System.out.println("Piscifactoría " + nombrePiscifactoria + ": " + totalPecesVendidos + " peces vendidos por "
                + totalMonedasGanadas + " monedas.");
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

        if (capacidadMaximaComidaPiscifactoria + incrementoCapacidad <= capacidadMaximaPermitida) {
            if (monedas.gastarMonedas(costoMejora)) {
                capacidadMaximaComidaPiscifactoria += incrementoCapacidad;
                System.out.println("Almacén de comida de la piscifactoría " + nombre
                        + " mejorado. Su capacidad ha aumentado en " + incrementoCapacidad + " hasta un total de "
                        + capacidadMaximaComidaPiscifactoria + ".");
                return true;
            } else {
                System.out.println("No tienes suficientes monedas para mejorar el almacén de comida de la piscifactoría " + nombre + ".");
            }
        } else {
            System.out.println("La capacidad máxima del almacén ya ha sido alcanzada para la piscifactoría " + nombre + ".");
        }

        return false;
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
     * Obtiene el número máximo de tanques que puede tener la piscifactoría.
     *
     * @return El número máximo de tanques permitidos en la piscifactoría.
     */
    public int getNumeroMaximoTanquesPiscifactoria() {
        return numeroMaximoTanquesPiscifactoria;
    }

    /**
     * Devuelve el total de peces en la piscifactoría.
     *
     * @return El total de peces.
     */
    public int getTotalPeces() {
        int totalPeces = 0;
        for (Tanque tanque : tanques) {
            totalPeces += tanque.getNumPeces();
        }
        return totalPeces;
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
     * Devuelve el total de machos en la piscifactoría.
     *
     * @return El total de machos.
     */
    public int getTotalMachos() {
        int totalMachos = 0;
        for (Tanque tanque : tanques) {
            totalMachos += tanque.getHembras();
        }
        return totalMachos;
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
     * Devuelve el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaximaComidaPiscifactoria() {
        return capacidadMaximaComidaPiscifactoria;
    }

    /**
     * Establece la capacidad máxima de comida en la piscifactoría.
     *
     * @param capacidadMaximaComidaPiscifactoria La nueva capacidad máxima de
     *                                           comida.
     * @return
     */
    public int setCapacidadMaximaComidaPiscifactoria(int num) {
        return capacidadMaximaComidaPiscifactoria += num;
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
     * Devuelve la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @return La cantidad actual de comida vegetal.
     */
    public int getComidaVegetalActual() {
        return comidaVegetalActual;
    }

    /**
     * Establece la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @param comidaVegetalActual La nueva cantidad de comida vegetal.
     */
    public void setComidaVegetalActual(int comidaVegetalActual) {
        this.comidaVegetalActual = comidaVegetalActual;
    }

    /**
     * Devuelve la cantidad actual de comida animal en la piscifactoría.
     *
     * @return La cantidad actual de comida animal.
     */
    public int getComidaAnimalActual() {
        return comidaAnimalActual;
    }

    /**
     * Establece la cantidad actual de comida animal en la piscifactoría.
     *
     * @param comidaAnimalActual La nueva cantidad de comida animal.
     */
    public void setComidaAnimalActual(int comidaAnimalActual) {
        this.comidaAnimalActual = comidaAnimalActual;
    }

    /**
     * Devuelve una representación en cadena del estado de la piscifactoría
     * incluyendo su nombre,
     * el número de tanques, y estadísticas de los peces.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "\nPiscifactoría: " + nombre + "\n" +
                "Número de tanques: " + tanques.size() + "\n" +
                "Total de peces: " + getTotalPeces() + " (Ocupación: " +
                ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) + "%)\n" +
                "Peces vivos: " + getTotalVivos() + "\n" +
                "Peces alimentados: " + getTotalAlimentados() + "\n" +
                "Peces adultos: " + getTotalAdultos() + "\n" +
                "Hembras: " + getTotalHembras() + ", Machos: " +
                (getTotalVivos() - getTotalHembras()) + "\n" +
                "Peces fértiles: " + getTotalFertiles() + "\n" +
                "Comida vegetal: " + getComidaVegetalActual() + " / " +
                getCapacidadTotal() + "\n" +
                "Comida animal: " + getComidaAnimalActual() + " / " +
                getCapacidadTotal() + "\n";
    }
}
