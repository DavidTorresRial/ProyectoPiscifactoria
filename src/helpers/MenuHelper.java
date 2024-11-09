package helpers;

/** MenuHelper facilita creación de menús en el sistema. */
public class MenuHelper {

    /** Constructor de MenuHelper. */
    public MenuHelper() {
    }

    /**
     * Muestra un menú dadas unas opciones + opción 0.Cancelar.
     *
     * @param opciones Array de opciones disponibles (sin incluir la opción de cancelar).
     */
    public void mostrarMenuCancelar(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
        System.out.println("0. Cancelar");
    }
    

    /**
     * Muestra un menú dadas unas opciones.
     *
     * @param opciones Array de opciones disponibles (sin incluir la opción de cancelar).
     */
    public void mostrarMenu(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
    }
}
