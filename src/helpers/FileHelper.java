package helpers;

import java.io.*;

import commons.Simulador;

/** Clase para manejar archivos y carpetas. */
public class FileHelper {

    /**
     * Crea múltiples carpetas si no existen.
     * 
     * @param carpetas Array de nombres de carpetas a crear.
     */
    public static void crearCarpetas(String[] carpetas) {
        for (String carpeta : carpetas) {
            File carpetaFile = new File(carpeta.trim());
            try {
                if (!carpetaFile.exists()) {
                    if (!carpetaFile.mkdirs()) {
                        Simulador.logger.logError("Error al crear la carpeta: " + carpeta.trim());
                    }
                }
            } catch (Exception e) {
                Simulador.logger.logError("Ocurrió un error inesperado al intentar crear la carpeta '" + carpeta.trim() + "': " + e.getMessage());
            }
        }
    }
}