package helpers;

import java.util.Scanner;

/**
 * InputHelper facilita la lectura validada de cadenas, enteros y decimales,
 * asegurando que la entrada no esté vacía y cumpla con el formato esperado.
 */
public class InputHelper {

    /** Objeto Scanner utilizado para la lectura de entradas del usuario. */
    private Scanner scanner;

    /**
     * Inicializa el Scanner para la lectura de datos.
     */
    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lee una cadena que no esté vacía y sin caracteres especiales.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Cadena ingresada sin caracteres especiales.
     */
    public String readString(String prompt) {
        while (true) { // Bucle infinito hasta que se obtenga una entrada válida
            System.out.print(prompt);
            String input = scanner.nextLine();

            // Validar que no esté vacía y no contenga caracteres especiales
            if (input.isEmpty()) {
                System.out.println("La entrada no puede estar vacía. Intente nuevamente.");
            } else if (!input.matches("[a-zA-Z0-9\\s]*")) { // Solo letras, números y espacios
                System.out.println("Entrada no válida. Por favor, no ingrese caracteres especiales.");
            } else {
                return input; // Entrada válida, retornamos el valor
            }
        }
    }

    /**
     * Lee un número entero que no esté vacío y sea válido.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Número entero ingresado.
     */
    public int readInt(String prompt) {
        int number = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("La entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            try {
                number = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
            }
        }
        return number;
    }

    /**
     * Lee un número decimal que no esté vacío y sea válido.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Número decimal ingresado.
     */
    public double readDouble(String prompt) {
        double number = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("La entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            try {
                number = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número decimal.");
            }
        }
        return number;
    }

    /**
     * Cierra el Scanner y libera los recursos.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Método toString para representar el estado del InputHelper.
     * @return Una cadena que describe el estado del InputHelper.
     */
    @Override
    public String toString() {
        return "InputHelper {scanner activo = " + (scanner != null) + "}";
    }
}
