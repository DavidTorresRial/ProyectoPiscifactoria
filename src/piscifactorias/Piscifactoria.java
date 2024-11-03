package piscifactorias;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;

import peces.Pez;
import tanque.Tanque;

import piscifactorias.tipos.PiscifactoriaDeMar;
import piscifactorias.tipos.PiscifactoriaDeRio;
import commons.AlmacenCentral;
import commons.SistemaMonedas;

/** Clase abstracta que representa una piscifactoría que gestiona tanques de peces */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría */
    private String nombre;

    /** Lista de tanques con distintos tipos de peces */
    protected List<Tanque> tanques;

    /** Sistema de monedas para gestionar el dinero ganado */
    private SistemaMonedas monedas;

    /** Capacidad máxima compartida para ambos tipos de comida */
    protected int capacidadMaximaComidaPiscifactoria;

    /** Cantidad actual de comida vegetal */
    protected int comidaVegetalActual;

    /** Cantidad actual de comida animal */
    protected int comidaAnimalActual;

    /** Número de tanques de río */
    private int numeroTanquesDeRio;

    /** Número de tanques de mar */
    private int numeroTanquesDeMar;

    /** Almacén central para gestionar el almacenamiento de comida */
    private AlmacenCentral almacenCentral;

    /**
     * Constructor para crear una nueva piscifactoría.
     *
     * @param nombre  El nombre de la piscifactoría.
     * @param monedas El sistema de monedas para gestionar el dinero ganado.
     */
    public Piscifactoria(String nombre, SistemaMonedas monedas) {
        this.nombre = nombre;
        this.tanques = new ArrayList<>();
        this.monedas = monedas;
    }

    /**
     * Agrega un tanque a la piscifactoría.
     *
     * @param tanque El tanque a agregar.
     */
    public void agregarTanque(Tanque tanque) {
        this.tanques.add(tanque);
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
     * Agrega un pez a la piscifactoría, verificando que sea del tipo adecuado.
     *
     * @param pez El pez a agregar.
     */
    public void addPez(Pez pez) {
        int tipoPez = pez.getDatos().getPiscifactoria().getValue();
        boolean agregado = false;

        // Verifica si el pez puede ser añadido a esta piscifactoría
        if ((tipoPez == 0 && this instanceof PiscifactoriaDeRio) ||
                (tipoPez == 1 && this instanceof PiscifactoriaDeMar) ||
                tipoPez == 2) {
            for (Tanque tanque : tanques) {
                if (((Tanque) tanque).addPez(pez)) {
                    agregado = true;
                    break;
                }
            }
        }

        if (!agregado) {
            System.out.println("No se puede añadir el pez de tipo " + tipoPez + " a esta piscifactoría.");
        }
    }

    /**
     * Muestra toda la información de la piscifactoría.
     */
    public void showStatus() {
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
        System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");

        int porcentajeAdultos = (totalVivos > 0) ? (totalAdultos * 100) / totalVivos : 0;
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + porcentajeAdultos + "%)");

        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));

        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);

        showFood();
    }

    /**
     * Muestra el estado de cada tanque en la piscifactoría.
     */
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

    /**
     * Muestra el estado del almacén de comida de la piscifactoría.
     */
    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");
        System.out.println("Comida vegetal al " + (comidaVegetalActual * 100 / capacidadMaximaComidaPiscifactoria) + "% de su capacidad. [" + comidaVegetalActual + "/" + capacidadMaximaComidaPiscifactoria + "]");
        System.out.println("Comida animal al " + (comidaAnimalActual * 100 / capacidadMaximaComidaPiscifactoria) + "% de su capacidad. [" + comidaAnimalActual + "/" + capacidadMaximaComidaPiscifactoria + "]");
    }

    /**
     * Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y
     * actualizando sus estados.
     */
    public void nextDay() {
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay();
            sellFish();
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
                } else if (almacenCentral.isConstruido() && almacenCentral.getCantidadComidaVegetal() > 0) {
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
                } else if (almacenCentral.isConstruido() && almacenCentral.getCantidadComidaAnimal() > 0) {
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

    /**
     * Vende los peces maduros y actualiza el sistema de monedas.
     */
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
        System.out.println("Piscifactoría " + nombrePiscifactoria + ": " + totalPecesVendidos + " peces vendidos por " + totalMonedasGanadas + " monedas.");
    }

    /**
     * Mejora el almacén de comida aumentando su capacidad máxima.
     *
     * @param incremento El incremento de capacidad.
     */
    public void upgradeFood(int incremento) {
        capacidadMaximaComidaPiscifactoria += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + incremento + " hasta un total de " + capacidadMaximaComidaPiscifactoria);
    }

    public boolean verificarTanqueYPiscifactoria(Tanque tanqueBuscado) {
        boolean tanqueEncontrado = false;

        for (Tanque tanque : tanques) {
            if (tanque == tanqueBuscado) {
                tanqueEncontrado = true;
                break;
            }
        }

        if (!tanqueEncontrado) {
            System.out.println("El tanque no pertenece a esta piscifactoría.");
            return false;
        }

        if (this instanceof PiscifactoriaDeRio) {
            System.out.println("El tanque pertenece a una piscifactoría de tipo río.");
            return true; // Retorna true si es de tipo río
        } else if (this instanceof PiscifactoriaDeMar) {
            System.out.println("El tanque pertenece a una piscifactoría de tipo mar.");
            return false; // Retorna false si es de tipo mar
        } else {
            System.out.println("La piscifactoría no es de tipo río ni mar.");
            return false; // En caso de error, retorna false por defecto
        }
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
        // Calcula la capacidad total sumando las capacidades de todos los tanques
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

    /**
     * Establece el nombre de la piscifactoría.
     *
     * @param nombre El nuevo nombre de la piscifactoría.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el número de tanques de río en la piscifactoría.
     *
     * @param numeroTanquesDeRio El nuevo número de tanques de río.
     */
    public void setNumeroTanquesDeRio(int numeroTanquesDeRio) {
        this.numeroTanquesDeRio = numeroTanquesDeRio;
    }

    /**
     * Establece el número de tanques de mar en la piscifactoría.
     *
     * @param numeroTanquesDeMar El nuevo número de tanques de mar.
     */
    public void setNumeroTanquesDeMar(int numeroTanquesDeMar) {
        this.numeroTanquesDeMar = numeroTanquesDeMar;
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
     * Establece la cantidad actual de comida animal en la piscifactoría.
     *
     * @param comidaAnimalActual La nueva cantidad de comida animal.
     */
    public void setComidaAnimalActual(int comidaAnimalActual) {
        this.comidaAnimalActual = comidaAnimalActual;
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
     * Devuelve una representación en cadena del estado de la piscifactoría incluyendo su nombre,
     * el número de tanques, y estadísticas de los peces.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "Piscifactoría: " + nombre + "\n" +
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
