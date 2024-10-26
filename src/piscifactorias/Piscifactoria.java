package piscifactorias;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import tanque.Tanque;
import commons.SistemaMonedas;
import peces.Pez;
import peces.propiedades.Activo;
import peces.propiedades.Carnivoro;
import peces.propiedades.Filtrador;
import piscifactorias.tipos.PiscifactoriaDeMar;
import piscifactorias.tipos.PiscifactoriaDeRio;

public abstract class Piscifactoria {
    private String nombre;
    protected List<Tanque<? extends Pez>> tanques; // Lista de tanques con distintos tipos de peces
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
    public void agregarTanque(Tanque<? extends Pez> tanque) {
        this.tanques.add(tanque);
    }

    public List<Tanque<? extends Pez>> getTanques() {
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
            for (Tanque<? extends Pez> tanque : tanques) {
                // Usa el método addPez del tanque para verificar y añadir el pez
                if (((Tanque<Pez>) tanque).addPez(pez)) {
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
    
        int totalPeces = 0;
        int totalVivos = 0;
        int totalAlimentados = 0;
        int totalAdultos = 0;
        int totalHembras = 0;
        int totalFertiles = 0;
        int capacidadTotal = 0;
    
        for (Tanque<? extends Pez> tanque : tanques) {
            totalPeces += tanque.getNumPeces();
            totalVivos += tanque.getVivos();
            totalAlimentados += tanque.getAlimentados();
            totalAdultos += tanque.getAdultos();
            totalHembras += tanque.getHembras();
            totalFertiles += tanque.getFertiles();
        }
    
        // Muestra la ocupación y otros datos relevantes
        String ocupacion = (capacidadTotal > 0) ? String.format("Ocupación: %d / %d (%d%%)", totalPeces, capacidadTotal, (totalPeces * 100 / capacidadTotal)) : "Ocupación: 0 / 0 (0%)";
        String pecesVivos = (totalPeces > 0) ? String.format("Peces vivos: %d / %d (%d%%)", totalVivos, totalPeces, (totalVivos * 100 / totalPeces)) : "Peces vivos: 0 / 0 (0%)";
        String pecesAlimentados = (totalVivos > 0) ? String.format("Peces alimentados: %d / %d (%d%%)", totalAlimentados, totalVivos, (totalAlimentados * 100 / totalVivos)) : "Peces alimentados: 0 / 0 (0%)";
        String pecesAdultos = (totalVivos > 0) ? String.format("Peces adultos: %d / %d (%d%%)", totalAdultos, totalVivos, (totalAdultos * 100 / totalVivos)) : "Peces adultos: 0 / 0 (0%)";
    
        System.out.println(ocupacion);
        System.out.println(pecesVivos);
        System.out.println(pecesAlimentados);
        System.out.println(pecesAdultos);
        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));
        
        String fertiles = (totalVivos > 0) ? String.format("Fértiles: %d / %d", totalFertiles, totalVivos) : "Fértiles: 0 / 0 (0)";
        System.out.println(fertiles);
    
        showFood();
    }
    

    // Método que muestra el estado de cada tanque
    public void showTankStatus() {
        for (Tanque<? extends Pez> tanque : tanques) {
            tanque.showStatus(); // Mostrar el estado de cada tanque
        }
    }

    // Método que muestra la información de los peces de un tanque determinado
    public void showFishStatus(int numeroTanque) {
        Tanque<? extends Pez> tanque = tanques.get(numeroTanque - 1); // Los tanques están numerados desde 1
        tanque.showFishStatus();
    }

    // Método que muestra la ocupación de un tanque determinado
    public void showCapacity(int numeroTanque) {
        Tanque<? extends Pez> tanque = tanques.get(numeroTanque - 1);
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
        System.out.println("Avanzando al siguiente día en la piscifactoría " + nombre + "...");
        for (Tanque<? extends Pez> tanque : tanques) {
            alimentarPeces(tanque);
            tanque.nextDay(); 
        }
    }

    private void alimentarPeces(Tanque<?> tanque) {
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
        for (Tanque<? extends Pez> tanque : tanques) {
            Iterator<? extends Pez> iterator = tanque.getPeces().iterator();
            while (iterator.hasNext()) {
                Pez pez = iterator.next();
                if (pez.getEdad() >= pez.getDatos().getMadurez() && pez.isVivo()) {
                    monedas.ganarMonedas(pez.getDatos().getMonedas());
                    iterator.remove();
                }
            }
        }
    }

    // Método que mejora el almacén de comida
    public void upgradeFood(int incremento) {
        capacidadMaximaAlmacenComida += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + incremento + " hasta un total de " + capacidadMaximaAlmacenComida);
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
