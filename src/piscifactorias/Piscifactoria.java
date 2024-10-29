package piscifactorias;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import commons.SistemaMonedas;
import peces.Pez;
import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;
import piscifactorias.tipos.PiscifactoriaDeMar;
import piscifactorias.tipos.PiscifactoriaDeRio;
import tanque.Tanque;

public abstract class Piscifactoria {
    private String nombre;
    protected List<Tanque> tanques; // Lista de tanques con distintos tipos de peces
    private SistemaMonedas monedas;


    protected int capacidadMaximaAlmacenComida; // Capacidad máxima compartida para ambos tipos de comida
    protected int comidaVegetalActual; // Cantidad actual de comida vegetal
    protected int comidaAnimalActual; // Cantidad actual de comida animal

    private int numeroTanquesDeRio; // Número de tanques de rio
    private int numeroTanquesDeMar; // Número de tanques de mar

    // Constructor
    public Piscifactoria(String nombre, SistemaMonedas monedas) {
        this.nombre = nombre;
        this.tanques = new ArrayList<>();
        this.monedas = monedas;
    }

    // Método para agregar un tanque a la piscifactoría (genérico para diferentes tipos de peces)
    public void agregarTanque(Tanque tanque) {
        this.tanques.add(tanque);
    }

    public List<Tanque> getTanques() {
        return this.tanques;
    }
    
    public void addPez(Pez pez) {
        int tipoPez = pez.getTipo();
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


    // Método que muestra toda la información de la piscifactoría
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
    

    // Método que muestra el estado de cada tanque
    public void showTankStatus() {
        for (Tanque tanque : tanques) {
            tanque.showStatus(); // Mostrar el estado de cada tanque
        }
    }

    // Método que muestra la información de los peces de un tanque determinado
    public void showFishStatus(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1); // Los tanques están numerados desde 1
        tanque.showFishStatus();
    }

    // Método que muestra la ocupación de un tanque determinado
    public void showCapacity(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showCapacity();
    }

    // Método que muestra el estado del almacén de comida
    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");
        System.out.println("Comida vegetal al " + (comidaVegetalActual * 100 / capacidadMaximaAlmacenComida) + "% de su capacidad. [" + comidaVegetalActual + "/" + capacidadMaximaAlmacenComida + "]");
        System.out.println("Comida animal al " + (comidaAnimalActual * 100 / capacidadMaximaAlmacenComida) + "% de su capacidad. [" + comidaAnimalActual + "/" + capacidadMaximaAlmacenComida + "]");
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
    }

    /**
     * @return String return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setNumeroTanquesDeRio(int numeroTanquesDeRio) {
        this.numeroTanquesDeRio = numeroTanquesDeRio;
    }

    public void setNumeroTanquesDeMar(int numeroTanquesDeMar) {
        this.numeroTanquesDeMar = numeroTanquesDeMar;
    }

    public int getNumeroTanquesDeRio() {
        return numeroTanquesDeRio;
    }

    public int getNumeroTanquesDeMar() {
        return numeroTanquesDeMar;
    }
    
    public int getComidaVegetalActual() {
        return comidaVegetalActual;
    }

    public void setComidaVegetalActual(int comidaVegetalActual) {
        this.comidaVegetalActual = comidaVegetalActual;
    }

    public void setComidaAnimalActual(int comidaAnimalActual) {
        this.comidaAnimalActual = comidaAnimalActual;
    }

    public int getComidaAnimalActual() {
        return comidaAnimalActual;
    }
}
