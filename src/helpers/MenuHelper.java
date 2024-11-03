package helpers;

import java.util.HashMap;
import java.util.Map;

/** Clase que ayuda en la gestión y presentación de un menú de opciones */
public class MenuHelper {
    private Map<Integer, String> menuOptions;

    /** Crea una nueva instancia de MenuHelper con un mapa vacío de opciones */
    public MenuHelper() {
        this.menuOptions = new HashMap<>();
    }

    /**
     * Agrega una opción al menú.
     *
     * @param optionNumber el número de la opción
     * @param description  la descripción de la opción
     */
    public void addOption(int optionNumber, String description) {
        menuOptions.put(optionNumber, description);
    }

    /** Muestra el menú en la consola, excluyendo la opción 0 en la iteración principal */
    public void showMenu() {
        for (Map.Entry<Integer, String> entry : menuOptions.entrySet()) {
            if (entry.getKey() != 0) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }
        }
        if (menuOptions.containsKey(0)) {
            System.out.println("0. " + menuOptions.get(0));
        }
    }

    /** Limpia todas las opciones del menú */
    public void clearOptions() {
        menuOptions.clear();
    }
}
