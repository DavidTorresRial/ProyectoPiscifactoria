package helpers;

import java.util.HashMap;
import java.util.Map;

public class MenuHelper {
    private Map<Integer, String> menuOptions;
    private InputHelper inputHelper;

    public MenuHelper() {
        this.menuOptions = new HashMap<>();
        this.inputHelper = new InputHelper();
    }

    // Método para agregar opciones al menú
    public void addOption(int optionNumber, String description) {
        menuOptions.put(optionNumber, description);
    }

    // Método para mostrar el menú
    public void showMenu() {
        for (Map.Entry<Integer, String> entry : menuOptions.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    // Método para manejar la selección de opciones usando InputHelper
    public int getSelection() {
        int selection = -1;
        while (selection == -1) {
            selection = inputHelper.readInt("Ingrese su opción: ");
            if (!menuOptions.containsKey(selection)) {
                System.out.println("Opción no válida. Intente de nuevo.");
                selection = -1;
            }
        }
        return selection;
    }

    public void clearOptions() {
        menuOptions.clear();
    }

    // Método para ejecutar una acción basada en la selección
    public void executeOption(int optionNumber) {
        System.out.println("Ejecutando la opción: " + optionNumber);
    }
}