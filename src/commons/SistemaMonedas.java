package commons;

public class SistemaMonedas {

    private int monedas;

    // Constructor que inicializa el saldo de monedas
    public SistemaMonedas(int saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.monedas = saldoInicial;
    }

    // Obtener la cantidad de monedas actual
    public int getMonedas() {
        return monedas;
    }

    // Método para ganar monedas, retorna true si la operación es exitosa
    public boolean ganarMonedas(int cantidad) {
        if (cantidad > 0) {
            monedas += cantidad;
            System.out.println("Has ganado " + cantidad + " monedas. Saldo actual: " + monedas + " monedas.");
            return true;  // Operación exitosa
        }
        System.out.println("No se pueden ganar monedas negativas o cero.");
        return false;
    }

    // Método para gastar monedas, retorna true si la operación es exitosa
    public boolean gastarMonedas(int costo) {
        System.out.println("Monedas disponibles: " + monedas + ". Intentando gastar: " + costo + " monedas.");
        if (costo > 0 && costo <= monedas) {
            monedas -= costo;
            System.out.println("Se han gastado " + costo + " monedas. Saldo restante: " + monedas + " monedas.");
            return true;  // Operación exitosa
        }
        System.out.println("No hay suficientes monedas para realizar esta acción.");
        return false;  // No se puede gastar más de lo que se tiene o una cantidad no válida
    }
}
