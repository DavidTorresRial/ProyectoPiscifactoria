package commons;

public class Simulador {

    public static void main(String[] args) {
        AlmacenCentral almacen = new AlmacenCentral();
        
        almacen.agregarComidaVeg(150);
        almacen.agregarComidaAnimal(180);
        
        almacen.mostrarEstado();
        
        almacen.distribuirComida(50, 30, 2); // Distribuye comida para 2 piscifactorías.
        
        almacen.mostrarEstado();
        
        // Usando getters
        System.out.println("Cantidad de comida vegetal actual: " + almacen.getComidaVegetal());
        System.out.println("Cantidad de comida animal actual: " + almacen.getComidaAnimal());
        almacen.mejora();
        }
    }