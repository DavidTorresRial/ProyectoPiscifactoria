package helpers;

import java.util.HashMap;
import java.util.Map;

public class MenuHelper {
    private Map<Integer, String> menuOptions;

    public MenuHelper() {
        this.menuOptions = new HashMap<>();
    }

    // Método para agregar opciones al menú
    public void addOption(int optionNumber, String description) {
        menuOptions.put(optionNumber, description);
    }

    // Método para mostrar el menú
    public void showMenu() {
        // Mostrar todas las opciones excepto la 0
        for (Map.Entry<Integer, String> entry : menuOptions.entrySet()) {
            if (entry.getKey() != 0) {  // Excluir la opción 0 en esta iteración
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }
        }
        // Mostrar la opción 0 al final
        if (menuOptions.containsKey(0)) {
            System.out.println("0. " + menuOptions.get(0));
        }
    }

    public void clearOptions() {
        menuOptions.clear();
    }

    // Método para ejecutar una acción basada en la selección
    public void executeOption(int optionNumber) {
        System.out.println("Ejecutando la opción: " + optionNumber);
    }
}
