package helpers;

import java.util.Scanner;

/** InputHelper facilita la lectura validada de cadenas y enteros */
public class InputHelper {

    /** Objeto Scanner utilizado para la lectura de entradas del usuario. */
    private Scanner scanner;

    /** Inicializa el Scanner para la lectura de datos */
    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lee una cadena que no esté vacía y sin caracteres especiales.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Cadena ingresada valida.
     */
    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("\nLa entrada no puede estar vacía. Intente nuevamente.");
            } else if (!input.matches("[a-zA-Z0-9\\s]*")) {
                System.out.println("\nEntrada no válida. Por favor, no ingrese caracteres especiales.");
            } else {
                return input;
            }
        }
    }

    /**
     * Lee un número entero que no esté vacío y sea válido.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Número ingresado valido.
     */
    public int readInt(String prompt) {
        int number = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("\nLa entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            try {
                number = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada no válida. Por favor, ingrese un número entero.");
            }
        }
        return number;
    }

    /** Cierra el Scanner y libera los recursos */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
