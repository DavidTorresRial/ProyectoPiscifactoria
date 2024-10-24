package commons;

public class Simulador {

    public static void main(String[] args) {
        SistemaMonedas sistemaMonedas = new SistemaMonedas(1000);
        AlmacenCentral almacen = new AlmacenCentral(sistemaMonedas);

        
        almacen.agregarComidaVeg(150);
        almacen.agregarComidaAnimal(180);
        
        almacen.mostrarEstado();
        
        almacen.distribuirComida(50, 30, 2); // Distribuye comida para 2 piscifactorías.
        
       
        
        // Usando getters
        System.out.println("Cantidad de comida vegetal actual: " + almacen.getComidaVegetal());
        System.out.println("Cantidad de comida animal actual: " + almacen.getComidaAnimal());
        almacen.mejora();
        almacen.mostrarEstado();
        }
    }