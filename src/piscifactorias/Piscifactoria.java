package piscifactorias;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
<<<<<<< HEAD
=======

import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;
>>>>>>> origin/DavidTrama

import peces.Pez;
<<<<<<< HEAD
import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;
import piscifactorias.tipos.PiscifactoriaDeMar;
import piscifactorias.tipos.PiscifactoriaDeRio;

public abstract class Piscifactoria {
    private String nombre;
    protected List<Tanque> tanques; // Lista de tanques con distintos tipos de peces
    private SistemaMonedas monedas;

=======
import tanque.Tanque;

import piscifactorias.tipos.PiscifactoriaDeMar;
import piscifactorias.tipos.PiscifactoriaDeRio;

import commons.SistemaMonedas;

/**
 * Clase abstracta que representa una piscifactoría que gestiona tanques de peces.
 */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría. */
    private String nombre;
    
    /** Lista de tanques con distintos tipos de peces. */
    protected List<Tanque> tanques;
    
    /** Sistema de monedas para gestionar el dinero ganado. */
    private SistemaMonedas monedas;
>>>>>>> origin/DavidTrama

    /** Capacidad máxima compartida para ambos tipos de comida. */
    protected int capacidadMaximaAlmacenComida;
    
    /** Cantidad actual de comida vegetal. */
    protected int comidaVegetalActual;

    /** Cantidad actual de comida animal. */
    protected int comidaAnimalActual;

    /** Número de tanques de río. */
    private int numeroTanquesDeRio;

    /** Número de tanques de mar. */
    private int numeroTanquesDeMar;


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

<<<<<<< HEAD
    // Método para agregar un tanque a la piscifactoría (genérico para diferentes tipos de peces)
=======
    /**
     * Agrega un tanque a la piscifactoría.
     *
     * @param tanque El tanque a agregar.
     */
>>>>>>> origin/DavidTrama
    public void agregarTanque(Tanque tanque) {
        this.tanques.add(tanque);
    }

<<<<<<< HEAD
=======
    /**
     * Devuelve la lista de tanques de la piscifactoría.
     *
     * @return La lista de tanques.
     */
>>>>>>> origin/DavidTrama
    public List<Tanque> getTanques() {
        return this.tanques;
    }
    
<<<<<<< HEAD
    public void addPez(Pez pez) {
        int tipoPez = pez.getTipo();
=======
    /**
     * Agrega un pez a la piscifactoría, verificando que sea del tipo adecuado.
     *
     * @param pez El pez a agregar.
     */
    public void addPez(Pez pez) {
        int tipoPez = pez.getDatos().getPiscifactoria().getValue();
>>>>>>> origin/DavidTrama
        boolean agregado = false;
    
        // Verifica si el pez puede ser añadido a esta piscifactoría
        if ((tipoPez == 0 && this instanceof PiscifactoriaDeRio) || 
            (tipoPez == 1 && this instanceof PiscifactoriaDeMar) || 
            tipoPez == 2) {
            
            // Intenta añadir el pez al primer tanque que lo acepte
            for (Tanque tanque : tanques) {
                // Usa el método addPez del tanque para verificar y añadir el pez
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
<<<<<<< HEAD

=======
>>>>>>> origin/DavidTrama

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
    
        String ocupacion = (capacidadTotal > 0) ? "Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + (totalPeces * 100 / capacidadTotal) + "%)" : "Ocupación: 0 / 0 (0%)";
        System.out.println(ocupacion);

        String pecesVivos = (totalPeces > 0) ? "Peces vivos: " + totalVivos + " / " + totalPeces + " (" + (totalVivos * 100 / totalPeces) + "%)" : "Peces vivos: 0 / 0 (0%)";
        System.out.println(pecesVivos);

        String pecesAlimentados = (totalVivos > 0) ? "Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + (totalAlimentados * 100 / totalVivos) + "%)" : "Peces alimentados: 0 / 0 (0%)";
        System.out.println(pecesAlimentados);

        String pecesAdultos = (totalVivos > 0) ? "Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + (totalAdultos * 100 / totalVivos) + "%)" : "Peces adultos: 0 / 0 (0%)";
        System.out.println(pecesAdultos);

        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));

        String fertiles = (totalVivos > 0) ? "Fértiles: " + totalFertiles + " / " + totalVivos : "Fértiles: 0 / 0 (0)";
        System.out.println(fertiles);
    
        showFood();
    }
    

    /**
     * Muestra el estado de cada tanque en la piscifactoría.
     */
    public void showTankStatus() {
        for (Tanque tanque : tanques) {
            tanque.showStatus(); // Mostrar el estado de cada tanque
        }
    }

    /**
     * Muestra la información de los peces de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showFishStatus(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1); // Los tanques están numerados desde 1
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
        System.out.println("Comida vegetal al " + (comidaVegetalActual * 100 / capacidadMaximaAlmacenComida) + "% de su capacidad. [" + comidaVegetalActual + "/" + capacidadMaximaAlmacenComida + "]");
        System.out.println("Comida animal al " + (comidaAnimalActual * 100 / capacidadMaximaAlmacenComida) + "% de su capacidad. [" + comidaAnimalActual + "/" + capacidadMaximaAlmacenComida + "]");
<<<<<<< HEAD
    }

    // Método que hace avanzar el ciclo de vida en la piscifactoría
    public void nextDay() {
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay(); 
        }
    }

    private void alimentarPeces(Tanque tanque) {
        Random rand = new Random();

        for (Pez pez : tanque.getPeces()) {
            if (!pez.isVivo()) {
                continue; // No alimentar peces muertos
            }

            boolean alimentado = false;

            if (pez instanceof Filtrador) {
                // Filtrador: 50% de probabilidad de no consumir comida
                if (rand.nextDouble() < 0.5) {
                    continue; // No consume comida
                }
                if (comidaVegetalActual > 0) {
                    comidaVegetalActual--;
                    alimentado = true;
                }
            } else if (pez instanceof Carnivoro) {
                // Carnívoro: Consume comida animal
                if (comidaAnimalActual > 0) {
                    comidaAnimalActual--;
                    alimentado = true;
                }
            }

            if (pez instanceof Activo) {
                // Activo: 50% de probabilidad de consumir el doble de comida
                if (rand.nextDouble() < 0.5) {
                    if (pez instanceof Carnivoro && comidaAnimalActual > 0) {
                        comidaAnimalActual--;
                    }
                }
            }

            if (alimentado) {
                pez.setAlimentar(); // Marcar el pez como alimentado
            }
        }

        System.out.println("Comida vegetal restante: " + comidaVegetalActual);
        System.out.println("Comida animal restante: " + comidaAnimalActual);
    }



    // Método que vende todos los peces adultos en la piscifactoría
    public void sellFish() {
        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;
    
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
    
        System.out.println("Total de peces vendidos: " + totalPecesVendidos);
        System.out.println("Total de monedas ganadas: " + totalMonedasGanadas);
    }

    // Método que mejora el almacén de comida
    public void upgradeFood(int incremento) {
        capacidadMaximaAlmacenComida += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + incremento + " hasta un total de " + capacidadMaximaAlmacenComida);
    }

    public int getTotalPeces() {
        int totalPeces = 0;
        for (Tanque tanque : tanques) {
            totalPeces += tanque.getNumPeces();
        }
        return totalPeces;
    }
    
    public int getTotalVivos() {
        int totalVivos = 0;
        for (Tanque tanque : tanques) {
            totalVivos += tanque.getVivos();
        }
        return totalVivos;
    }
    
    public int getTotalAlimentados() {
        int totalAlimentados = 0;
        for (Tanque tanque : tanques) {
            totalAlimentados += tanque.getAlimentados();
        }
        return totalAlimentados;
    }
    
    public int getTotalAdultos() {
        int totalAdultos = 0;
        for (Tanque tanque : tanques) {
            totalAdultos += tanque.getAdultos();
        }
        return totalAdultos;
    }
    
    public int getTotalHembras() {
        int totalHembras = 0;
        for (Tanque tanque : tanques) {
            totalHembras += tanque.getHembras();
        }
        return totalHembras;
    }
    
    public int getTotalFertiles() {
        int totalFertiles = 0;
        for (Tanque tanque : tanques) {
            totalFertiles += tanque.getFertiles();
        }
        return totalFertiles;
    }
    
    public int getCapacidadTotal() {
        int capacidadTotal = 0;
        // Calcula la capacidad total sumando las capacidades de todos los tanques
        for (Tanque tanque : tanques) {
            capacidadTotal += tanque.getCapacidad();
        }
        return capacidadTotal;
=======
>>>>>>> origin/DavidTrama
    }

    /**
     * Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y actualizando sus estados.
     */
    public void nextDay() {
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay(); 
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
                continue; // No alimentar peces muertos
            }

            boolean alimentado = false;

            if (pez instanceof Filtrador) {
                // Filtrador: 50% de probabilidad de no consumir comida
                if (rand.nextDouble() < 0.5) {
                    continue; // No consume comida
                }
                if (comidaVegetalActual > 0) {
                    comidaVegetalActual--;
                    alimentado = true;
                }
            } else if (pez instanceof Carnivoro) {
                // Carnívoro: Consume comida animal
                if (comidaAnimalActual > 0) {
                    comidaAnimalActual--;
                    alimentado = true;
                }
            }

            if (pez instanceof Activo) {
                // Activo: 50% de probabilidad de consumir el doble de comida
                if (rand.nextDouble() < 0.5) {
                    if (pez instanceof Carnivoro && comidaAnimalActual > 0) {
                        comidaAnimalActual--;
                    }
                }
            }

            if (alimentado) {
                pez.setAlimentado(true); // Marcar el pez como alimentado
            }
        }

        System.out.println("Comida vegetal restante: " + comidaVegetalActual);
        System.out.println("Comida animal restante: " + comidaAnimalActual);
    }

    /**
     * Vende los peces maduros y actualiza el sistema de monedas.
     */
    public void sellFish() {
        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;
    
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
    
        System.out.println("Total de peces vendidos: " + totalPecesVendidos);
        System.out.println("Total de monedas ganadas: " + totalMonedasGanadas);
    }

    /**
     * Mejora el almacén de comida aumentando su capacidad máxima.
     *
     * @param incremento El incremento de capacidad.
     */
    public void upgradeFood(int incremento) {
        capacidadMaximaAlmacenComida += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + incremento + " hasta un total de " + capacidadMaximaAlmacenComida);
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
<<<<<<< HEAD
     * @param nombre the nombre to set 
=======
     * Establece el nombre de la piscifactoría.
     *
     * @param nombre El nuevo nombre de la piscifactoría.
>>>>>>> origin/DavidTrama
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
    
<<<<<<< HEAD
=======
    /**
     * Devuelve la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @return La cantidad actual de comida vegetal.
     */
>>>>>>> origin/DavidTrama
    public int getComidaVegetalActual() {
        return comidaVegetalActual;
    }

<<<<<<< HEAD
=======
    /**
     * Establece la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @param comidaVegetalActual La nueva cantidad de comida vegetal.
     */
>>>>>>> origin/DavidTrama
    public void setComidaVegetalActual(int comidaVegetalActual) {
        this.comidaVegetalActual = comidaVegetalActual;
    }

<<<<<<< HEAD
=======
    /**
     * Establece la cantidad actual de comida animal en la piscifactoría.
     *
     * @param comidaAnimalActual La nueva cantidad de comida animal.
     */
>>>>>>> origin/DavidTrama
    public void setComidaAnimalActual(int comidaAnimalActual) {
        this.comidaAnimalActual = comidaAnimalActual;
    }

<<<<<<< HEAD
=======
    /**
     * Devuelve la cantidad actual de comida animal en la piscifactoría.
     *
     * @return La cantidad actual de comida animal.
     */
>>>>>>> origin/DavidTrama
    public int getComidaAnimalActual() {
        return comidaAnimalActual;
    }
}
