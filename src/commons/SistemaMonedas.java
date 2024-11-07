package commons;

/** Representa un sistema de gestión de monedas. */
public class SistemaMonedas {

    /** Cantidad de monedas disponibles en el sistema. */
    private int monedas;

    /** Instancia única de SistemaMonedas (Singleton) */
    private static SistemaMonedas instanciaUnica;

    /**
     * Constructor privado que inicializa el saldo de monedas con 100.
     * El saldo inicial siempre será 100, sin opción a modificarlo desde fuera.
     */
    private SistemaMonedas() {
        this.monedas = 100;  // Siempre empieza con 100 monedas
    }

    /**
     * Obtiene la instancia única del sistema de monedas (Singleton).
     * Si la instancia aún no ha sido creada, la crea con 100 monedas.
     * 
     * @return La instancia única de SistemaMonedas.
     */
    public static SistemaMonedas getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new SistemaMonedas();
        }
        return instanciaUnica;
    }

    /**
     * Obtiene la cantidad actual de monedas.
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
            return true;
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
            return true;
        }
        System.out.println("\nNo hay suficientes monedas para realizar esta acción.");
        return false;
    }
}
