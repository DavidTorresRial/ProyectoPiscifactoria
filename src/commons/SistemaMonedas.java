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
            return true;  // Operación exitosa
        }
        return false;  // No se pueden ganar cantidades negativas o cero
    }

    // Método para gastar monedas, retorna true si la operación es exitosa
    public boolean gastarMonedas(int costo) {
        if (costo > 0 && costo <= monedas) {
            monedas -= costo;
            return true;  // Operación exitosa
        }
        return false;  // No se puede gastar más de lo que se tiene o una cantidad no válida
    }
}
