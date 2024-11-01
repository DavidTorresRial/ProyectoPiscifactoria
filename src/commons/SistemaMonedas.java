package commons;

/** Representa un sistema de gestión de monedas. */
public class SistemaMonedas {

    /** Cantidad de monedas disponibles en el sistema. */
    private int monedas;

    /**
     * Constructor que inicializa el saldo de monedas.
     * 
     * @param saldoInicial El saldo inicial de monedas. Debe ser un valor nonegativo.
     * @throws IllegalArgumentException si el saldo inicial es negativo.
     */
    public SistemaMonedas(int saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("\nEl saldo inicial no puede ser negativo.");
        }
        this.monedas = saldoInicial;
    }

    /**
     * Devuelve la cantidad de monedas actual.
     * 
     * @return El número de monedas disponibles.
     */
    public int getMonedas() {
        return monedas;
    }

    /**
     * Método para incrementar la cantidad de monedas.
     * 
     * @param cantidad La cantidad de monedas a ganar. Debe ser mayor a 0.
     * @return true si la operación fue exitosa, false si la cantidad es inválida.
     */
    public boolean ganarMonedas(int cantidad) {
        if (cantidad > 0) {
            monedas += cantidad;
            System.out.println("\nHas ganado " + cantidad + " monedas. Saldo actual: " + monedas + " monedas.");
            return true; // Operación exitosa
        }
        System.out.println("\nNo se pueden ganar monedas negativas o cero.");
        return false;
    }

    /**
     * Método para gastar monedas.
     * 
     * @param costo El número de monedas a gastar. Debe ser mayor a 0 y menor o igual al saldo disponible.
     * @return true si la operación fue exitosa, false si no hay suficientes monedas o el costo es inválido.
     */
    public boolean gastarMonedas(int costo) {
        if (costo > 0 && costo <= monedas) {
            monedas -= costo;
            System.out.println("\nSe han gastado " + costo + " monedas. Saldo restante: " + monedas + " monedas.");
            return true; // Operación exitosa
        }
        System.out.println("\nNo hay suficientes monedas para realizar esta acción.");
        return false; // No se puede gastar más de lo que se tiene o una cantidad no válida
    }
}
