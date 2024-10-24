package commons;


import peces.tipos.rio.Pejerrey;
import tanque.Tanque;
import piscifactorias.tipos.PiscifactoriaDeRio;


public class Simulador {

    public static void main(String[] args) {
        // Creamos una instancia del sistema de monedas (simulación)
        SistemaMonedas sistemaMonedas = new SistemaMonedas(1000);

        // Creamos una piscifactoría para Pejerreyes
        PiscifactoriaDeRio piscifactoria = new PiscifactoriaDeRio("Piscifactoria de Pejerreyes", sistemaMonedas);

        // Creamos tanques y les asignamos capacidad
        Tanque<Pejerrey> tanque1 = new Tanque<>(50);  // Capacidad para 50 peces

        // Agregamos los tanques a la piscifactoría
        piscifactoria.agregarTanque(tanque1);

        // Añadimos peces a los tanques
        tanque1.peces.add(new Pejerrey(true));  // Añadimos un Pejerrey macho
        tanque1.peces.add(new Pejerrey(false)); // Añadimos un Pejerrey hembra

        // Mostramos el estado inicial de la piscifactoría
        piscifactoria.showStatus();

        // Avanzamos un día en la piscifactoría (esto también avanzará un día en todos los tanques)
        piscifactoria.nextDay();

        // Volvemos a mostrar el estado de la piscifactoría después de avanzar un día
        piscifactoria.showStatus();

        // Muestra el estado de un tanque específico
        piscifactoria.showFishStatus(1); // Mostrar el estado de los peces del tanque 1
        piscifactoria.showFishStatus(2); // Mostrar el estado de los peces del tanque 2
    }
}
