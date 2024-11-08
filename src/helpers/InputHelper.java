package helpers;

import java.util.Scanner;

/** InputHelper facilita la lectura validada de cadenas y enteros */
public class InputHelper {

    /** Objeto Scanner utilizado para la lectura de entradas del usuario. */
    private static Scanner scanner;

    /** Inicializa el Scanner para la lectura de datos (singleton). */
    private InputHelper() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
    }

    /**
     * Devuelve la instancia única de InputHelper (Singleton).
     */
    public static InputHelper getInstance() {
        if (scanner == null) {
            new InputHelper();
        }
        return new InputHelper();
    }

    /**
     * Lee una cadena que no esté vacía y sin caracteres especiales.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Cadena ingresada válida.
     */
    public String readString(String prompt) {
        boolean entradaValida = false;
        String input = "";

        while (!entradaValida) {
            System.out.print(prompt);
            input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("\nLa entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            if (!input.matches("[a-zA-Z0-9\\s]*")) {
                System.out.println("\nEntrada no válida. Por favor, no ingrese caracteres especiales.");
                continue;
            }

            entradaValida = true;
        }

        return input;
    }

    /**
     * Lee un número entero que no esté vacío y sea válido.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Número ingresado válido.
     */
    public int readInt(String prompt) {
        boolean entradaValida = false;
        int number = -1;

        while (!entradaValida) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("\nLa entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            try {
                number = Integer.parseInt(input);
                entradaValida = true;
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada no válida. Por favor, ingrese un número entero.");
            }
        }

        return number;
    }

    /**
     * Solicita un número al usuario dentro de un rango específico.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @param min    Valor mínimo permitido (incluido).
     * @param max    Valor máximo permitido (incluido).
     * @return Un número dentro del rango especificado.
     */
    public int solicitarNumero(int min, int max) {
        boolean entradaValida = false;
        int numero = 0;
    
        while (!entradaValida) {
            numero = readInt("Ingresa un número: ");
    
            if (numero >= min && numero <= max) {
                entradaValida = true;
            } else {
                System.out.println("\nEl número debe estar entre " + min + " y " + max + ". Intente nuevamente.");
            }
        }
        return numero;
    }
    

    /** Cierra el Scanner y libera los recursos */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
