package commons;

import commons.AlmacenCentral;
import commons.SistemaMonedas;
import peces.Pez;
import piscifactorias.Piscifactoria;
import java.util.Scanner;

public class Simulador {

    public static void main(String[] args) {
        SistemaMonedas sistemaMonedas = new SistemaMonedas(1000);
        AlmacenCentral almacen = new AlmacenCentral(sistemaMonedas);
        Piscifactoria piscifactoria = new Piscifactoria(almacen);
        // Suponiendo que tienes una clase Piscifactoria

        addFood(piscifactoria, sistemaMonedas, almacen);
    }

    public static void addFood(Piscifactoria piscifactoria, SistemaMonedas sistemaMonedas, AlmacenCentral almacen) {
        Scanner scanner = new Scanner(System.in);

        // Selección del tipo de comida
        System.out.println("Selecciona el tipo de comida a añadir:");
        System.out.println("1. Comida vegetal");
        System.out.println("2. Comida animal");
        int tipoComida = scanner.nextInt();

        if (tipoComida != 1 && tipoComida != 2) {
            System.out.println("Opción no válida.");
            return;
        }

        // Selección de la cantidad de comida
        System.out.println("Selecciona la cantidad de comida a añadir:");
        System.out.println("1. 5 unidades");
        System.out.println("2. 10 unidades");
        System.out.println("3. 25 unidades");
        System.out.println("4. Llenar depósito");
        int cantidadSeleccionada = scanner.nextInt();
        int cantidad = 0;

        switch (cantidadSeleccionada) {
            case 1:
                cantidad = 5;
                break;
            case 2:
                cantidad = 10;
                break;
            case 3:
                cantidad = 25;
                break;
            case 4:
                if (tipoComida == 1) {
                    cantidad = almacen.getCapacidadMaxVeg() - almacen.getComidaVegetal();
                } else {
                    cantidad = almacen.getCapacidadMaxAni() - almacen.getComidaAnimal();
                }
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        // Calcular el costo con el descuento de 5 monedas por cada 25 unidades
        int costo = calcularCosto(cantidad);

        // Verificar si hay monedas suficientes
        if (!sistemaMonedas.gastarMonedas(costo)) {
            System.out.println("No tienes suficientes monedas.");
            return;
        }

        // Añadir comida según el tipo seleccionado
        if (tipoComida == 1) {
            if (cantidad > almacen.getCapacidadMaxVeg() - almacen.getComidaVegetal()) {
                cantidad = almacen.getCapacidadMaxVeg() - almacen.getComidaVegetal(); // Ajustar si excede
            }
            almacen.agregarComidaVeg(cantidad);
            System.out.println("Añadida " + cantidad + " de comida vegetal.");
        } else {
            if (cantidad > almacen.getCapacidadMaxAni() - almacen.getComidaAnimal()) {
                cantidad = almacen.getCapacidadMaxAni() - almacen.getComidaAnimal(); // Ajustar si excede
            }
            almacen.agregarComidaAnimal(cantidad);
            System.out.println("Añadida " + cantidad + " de comida animal.");
        }

        // Mostrar estado del almacén
        almacen.mostrarEstado();
    }

    // Método que calcula el costo de la comida según las reglas establecidas
    public static int calcularCosto(int cantidad) {
        int costo = 0;
        int bloquesDe25 = cantidad / 25;
        int resto = cantidad % 25;

        // Descuento de 5 monedas por cada bloque de 25 unidades
        costo += (bloquesDe25 * 25) - (bloquesDe25 * 5);
        costo += resto; // Agregar el costo por el resto
        return costo;
    }
}