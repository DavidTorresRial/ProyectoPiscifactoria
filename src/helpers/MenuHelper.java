package helpers;

public class MenuHelper {

    /** Constructor de MenuHelper */
    public MenuHelper() {
    }

    /**
     * Muestra un menú y lee la opción seleccionada por el usuario.
     *
     * @param opciones Array de opciones disponibles (sin incluir la opción de cancelar).
     * @return La opción seleccionada por el usuario (0 para cancelar).
     */
    public int mostrarMenu(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
        System.out.println("0. Cancelar");
    
        return opciones.length;
    }
    

    /**
     * Muestra un menú y lee la opción seleccionada por el usuario, sin incluir la opción 0 de cancelar.
     *
     * @param opciones Array de opciones disponibles (sin incluir la opción de cancelar).
     * @return La opción seleccionada por el usuario.
     */
    public void mostrarMenuSinCancelar(String[] opciones) {
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ". " + opciones[i]);
        }
    }
}
