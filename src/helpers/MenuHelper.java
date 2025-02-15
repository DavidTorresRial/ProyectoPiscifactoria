package helpers;

import java.util.List;

/** MenuHelper facilita creación de menús en el sistema. */
public class MenuHelper {

    /**
     * Muestra un menú dadas unas opciones + opción 0.Cancelar.
     *
     * @param opciones Array de opciones disponibles.
     */
    public static void mostrarMenuCancelar(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
        System.out.println("0. Cancelar");
    }

    /**
     * Muestra un menú dadas unas opciones.
     *
     * @param opciones Array de opciones disponibles.
     */
    public static void mostrarMenu(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
    }

    /**
     * Muestra un menú dadas unas opciones + opción 0. Cancelar.
     *
     * @param opciones Lista de opciones disponibles.
     */
    public static void mostrarMenuCancelar(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        System.out.println("0. Cancelar");
    }

    /**
     * Muestra un menú dadas unas opciones.
     *
     * @param opciones Lista de opciones disponibles.
     */
    public static void mostrarMenu(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
    }
}