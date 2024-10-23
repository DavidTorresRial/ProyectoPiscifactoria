package piscifactorias;

import java.util.ArrayList;
import java.util.List;

import tanque.Tanque;
import commons.SistemaMonedas;
import peces.Pez;

public abstract class Piscifactoria<T extends Pez> {
    private String nombre;
    protected List<Tanque<T>> tanques; // Lista de tanques

    protected int capacidadMaximaAlmacenComida; // Capacidad máxima compartida para ambos tipos de comida
    protected int comidaVegetalActual; // Cantidad actual de comida vegetal
    protected int comidaAnimalActual; // Cantidad actual de comida animal

    public Piscifactoria(String nombre, SistemaMonedas monedas) {
        this.nombre = nombre;
        this.tanques = new ArrayList<>();
    }

    // Método para agregar un tanque a la piscifactoría
    public void agregarTanque(Tanque<T> tanque) {
        this.tanques.add(tanque);
    }

    public List<Tanque<T>> getTanques() {
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

        for (Tanque<T> tanque : tanques) {
            totalPeces += tanque.getNumPeces(); 
            totalVivos += tanque.getVivos();
            totalAlimentados += tanque.getAlimentados();
            totalAdultos += tanque.getAdultos();
            totalHembras += tanque.getHembras();
            totalFertiles += tanque.getFertiles();
            //capacidadTotal += tanque.getCapacidadMaxima(); // TDOO: Revisar
        }

        // Muestra la ocupación y otros datos relevantes
        System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + 
                           (totalPeces * 100 / capacidadTotal) + "%)");
        System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + 
                           (totalPeces > 0 ? (totalVivos * 100 / totalPeces) : 0) + "%)"); // TODO comprobar si los peces es cero porque salta excepcion al dividir
        System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + 
                           (totalVivos > 0 ? (totalAlimentados * 100 / totalVivos) : 0) + "%)");
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + 
                           (totalVivos > 0 ? (totalAdultos * 100 / totalVivos) : 0) + "%)");
        System.out.println("Hembras / Machos: " + totalHembras + " / " + (totalVivos - totalHembras));
        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);
        showFood();
    }

    // Método que muestra el estado de cada tanque
    public void showTankStatus() {
        for (Tanque<T> tanque : tanques) {
            tanque.showStatus(); // Mostrar el estado de cada tanque
        }
    }

    // Método que muestra la información de los peces de un tanque determinado
    public void showFishStatus(int numeroTanque) {
        Tanque<T> tanque = tanques.get(numeroTanque - 1); // Asumimos que los tanques están numerados desde 1
        tanque.showFishStatus();
    }

    // Método que muestra la ocupación de un tanque determinado
    public void showCapacity(int numeroTanque) {
        Tanque<T> tanque = tanques.get(numeroTanque - 1);
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
        for (Tanque<T> tanque : tanques) {
            tanque.nextDay();
            alimentarPeces(tanque);
        }
    }

    // Método para alimentar los peces en un tanque
    private void alimentarPeces(Tanque<T> tanque) {
        int pecesVivos = tanque.getVivos(); // Obtener el número de peces vivos en el tanque

        int comidaNecesaria = pecesVivos;

        if (comidaVegetalActual >= comidaNecesaria && comidaAnimalActual >= comidaNecesaria) {
            System.out.println("Alimentando " + pecesVivos + " peces en el tanque " + tanque.getNumeroTanque());
            comidaVegetalActual -= comidaNecesaria / 2;
            comidaAnimalActual -= comidaNecesaria / 2;

            // Alimentar solo a los peces vivos
            for (T pez : tanque.getPeces()) { // Acceso directo a la lista de peces
                if (pez.isVivo()) { // Verificamos que el pez esté vivo
                    pez.alimentar(); // Marcar el pez como alimentado
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
        for (Tanque<T> tanque : tanques) {
            //tanque.venderPecesAdultos(); // Revisar
        }
    }

    // Método que mejora el almacén de comida
    public void upgradeFood(int incremento) {
        capacidadMaximaAlmacenComida += incremento;
        System.out.println("Almacén de comida de la piscifactoría " + nombre + " mejorado. Su capacidad ha aumentado en " + 
                           incremento + " hasta un total de " + capacidadMaximaAlmacenComida);
    }
}
