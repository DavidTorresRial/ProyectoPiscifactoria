package helpers;

import java.util.Scanner;

import commons.Simulador;

/** InputHelper facilita la lectura validada de cadenas y enteros. */
public class InputHelper {

    /** Objeto Scanner utilizado para la lectura de entradas del usuario. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee una cadena que no esté vacía y sin caracteres especiales.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Cadena ingresada válida.
     */
    public static String readString(String prompt) {
        boolean entradaValida = false;
        String string = "";

        while (!entradaValida) {
            System.out.print(prompt);
            string = scanner.nextLine();

            if (string.isEmpty()) {
                System.out.println("\nLa entrada no puede estar vacía. Intente nuevamente.");
                continue;
            }

            if (!string.matches("[a-zA-Z0-9\\s]*")) {
                System.out.println("\nEntrada no válida. Por favor, no ingrese caracteres especiales.");
                continue;
            }
            entradaValida = true;
        }
        return string;
    }

    /**
     * Lee un número entero que no esté vacío y sea válido.
     *
     * @param prompt Mensaje mostrado al usuario.
     * @return Número ingresado válido.
     */
    public static int readInt(String prompt) {
        boolean entradaValida = false;
        int numero = -1;

        while (!entradaValida) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (!input.isEmpty()) {
                if (input.length() <= 2) {
                    try {
                        numero = Integer.parseInt(input);
                        entradaValida = true;
                    } catch (NumberFormatException e) {
                        System.out.println("\nEntrada no válida. Por favor, ingrese un número válido.");
                    }
                } else {
                    System.out.println("\nLa entrada no puede tener más de dos dígitos.");
                }
            } else {
                System.out.println("\nLa entrada no puede estar vacía. Por favor, ingrese un número entero.");
            }
        }
        return numero;
    }

    /**
     * Solicita un número al usuario dentro de un rango específico.
     *
     * @param min    Valor mínimo permitido (incluido).
     * @param max    Valor máximo permitido (incluido).
     * @return Un número dentro del rango especificado.
     */
    public static int solicitarNumero(int min, int max) {
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

    /** Cierra el Scanner y libera los recursos. */
    public static void close() {
        try {
            if (scanner != null) {
                scanner.close();
            }
        } catch (IllegalStateException e) {
            Simulador.registro.registroLogError("El Scanner ya fue cerrado previamente: \n" + e);
        }
    }
}