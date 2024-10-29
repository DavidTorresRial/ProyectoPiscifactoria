package helpers;

import java.util.Scanner;

public class InputHelper {
    private Scanner scanner;

    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    // Método para leer una cadena de texto
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Método para leer un entero con validación
    public int readInt(String prompt) {
        int number = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                number = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
            }
        }
        return number;
    }

    // Método para leer un número decimal con validación
    public double readDouble(String prompt) {
        double number = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                number = Double.parseDouble(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número decimal.");
            }
        }
        return number;
    }
}