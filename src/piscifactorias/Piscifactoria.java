package piscifactorias;

import java.util.ArrayList;
import java.util.List;

import tanque.Tanque;
import commons.SistemaMonedas;
import peces.Pez;

public abstract class Piscifactoria {
    private String nombre;
    protected List<Tanque<? extends Pez>> tanques; // Lista de tanques con distintos tipos de peces


    protected int capacidadMaximaAlmacenComida; // Capacidad máxima compartida para ambos tipos de comida
    protected int comidaVegetalActual; // Cantidad actual de comida vegetal
    protected int comidaAnimalActual; // Cantidad actual de comida animal

    private int numeroTanquesDeRio; // Número de tanques de rio
    private int numeroTanquesDeMar; // Número de tanques de mar

    // Constructor
    public Piscifactoria(String nombre, SistemaMonedas monedas) {
        this.nombre = nombre;
        this.tanques = new ArrayList<>();
    }

    // Método para agregar un tanque a la piscifactoría (genérico para diferentes tipos de peces)
    public void agregarTanque(Tanque<? extends Pez> tanque) {
        this.tanques.add(tanque);
    }

    public List<Tanque<? extends Pez>> getTanques() {
        return this.tanques;
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
            //capacidadTotal += tanque.getCapacidadMaxima(); // Ahora también se suma la capacidad total
        }
    
        // Muestra la ocupación y otros datos relevantes
        if (capacidadTotal > 0) {
            System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + 
                               (totalPeces * 100 / capacidadTotal) + "%)");
        } else {
            System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (0%)");
        }
    
        if (totalPeces > 0) {
            System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + 
                               (totalVivos * 100 / totalPeces) + "%)");
        } else {
            System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (0%)");
        }
    
        if (totalVivos > 0) {
            System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + 
                               (totalAlimentados * 100 / totalVivos) + "%)");
        } else {
            System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (0%)");
        }
    
        if (totalVivos > 0) {
            System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + 
                               (totalAdultos * 100 / totalVivos) + "%)");
        } else {
            System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (0%)");
        }
    
        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));
        
        if (totalVivos > 0) {
            System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);
        } else {
            System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos + " (0)");
        }
    
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
        System.out.println("Comida vegetal al " + (comidaVegetalActual * 100 / capacidadMaximaAlmacenComida) + 
                           "% de su capacidad. [" + comidaVegetalActual + "/" + capacidadMaximaAlmacenComida + "]");
        System.out.println("Comida animal al " + (comidaAnimalActual * 100 / capacidadMaximaAlmacenComida) + 
                           "% de su capacidad. [" + comidaAnimalActual + "/" + capacidadMaximaAlmacenComida + "]");
    }

    // Método que hace avanzar el ciclo de vida en la piscifactoría
    public void nextDay() {
        System.out.println("Avanzando al siguiente día en la piscifactoría " + nombre + "...");
        for (Tanque<? extends Pez> tanque : tanques) {
            tanque.nextDay(comidaAnimalActual, comidaVegetalActual); //TODO hacer uso de getters y setters de la comida
            alimentarPeces(tanque);
        }
    }

    // Método para alimentar los peces en un tanque
    private void alimentarPeces(Tanque<? extends Pez> tanque) {
        int pecesVivos = tanque.getVivos(); // Obtener el número de peces vivos en el tanque
        int comidaNecesaria = pecesVivos;

        if (comidaVegetalActual >= comidaNecesaria && comidaAnimalActual >= comidaNecesaria) {
            System.out.println("Alimentando " + pecesVivos + " peces en el tanque " + tanque.getNumeroTanque());
            comidaVegetalActual -= comidaNecesaria / 2;
            comidaAnimalActual -= comidaNecesaria / 2;

            // Alimentar solo a los peces vivos
            for (Pez pez : tanque.getPeces()) { // Acceso directo a la lista de peces
                if (pez.isVivo()) { // Verificamos que el pez esté vivo
                    pez.setAlimentado(); // Marcar el pez como alimentado
                }
            }
        } else {
            System.out.println("No hay suficiente comida para alimentar a todos los peces.");
            System.out.println("Comida vegetal disponible: " + comidaVegetalActual);
            System.out.println("Comida animal disponible: " + comidaAnimalActual);
        }
    }

    // Método que vende todos los peces adultos en la piscifactoría
    public void sellFish() {
        System.out.println("Vendiendo peces adultos en la piscifactoría " + nombre + "...");
        for (Tanque<? extends Pez> tanque : tanques) {
            //tanque.venderPecesAdultos(); //TODO implementar metodo
        }
    }

    // Método que mejora el almacén de comida
    public void upgradeFood(int incremento) {
        capacidadMaximaAlmacenComida += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + 
                           incremento + " hasta un total de " + capacidadMaximaAlmacenComida);
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
