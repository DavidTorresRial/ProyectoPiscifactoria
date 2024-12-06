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

    /**
     * Comprueba si un directorio contiene archivos o subdirectorios.
     *
     * @param rutaDirectorio Ruta del directorio a comprobar.
     * @return true si el directorio contiene archivos o subdirectorios, false en caso contrario.
     */
    public static boolean hayContenidoEnDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);

        if (directorio.exists()) {
            if (directorio.list().length > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            Simulador.logger.logError("El directorio no existe: " + rutaDirectorio);
            return false;
        }
    }

    public static String mostrarMenuConArchivos(String directorio) {
        File folder = new File(directorio);

        if (folder.exists() && folder.isDirectory()) {
            File[] archivos = folder.listFiles();

            if (archivos != null && archivos.length > 0) {
                System.out.println("\nSelecciona una partida: ");

                for (int i = 0; i < archivos.length; i++) {
                    if (archivos[i].isFile()) {
                        
                        String nombreArchivo = archivos[i].getName();
                        int indicePunto = nombreArchivo.lastIndexOf(".");
                        if (indicePunto > 0) {
                            nombreArchivo = nombreArchivo.substring(0, indicePunto); 
                        }
                        System.out.println((i + 1) + ". " + nombreArchivo);
                    }
                }

                // Usamos InputHelper para obtener la opción seleccionada por el usuario
                int opcion = InputHelper.solicitarNumero(1, archivos.length); // Usamos InputHelper para obtener una opción válida

                // Mostrar el archivo seleccionado sin su extensión
                String nombreArchivoSeleccionado = archivos[opcion - 1].getName();
                int indicePunto = nombreArchivoSeleccionado.lastIndexOf(".");
                if (indicePunto > 0) {
                    nombreArchivoSeleccionado = nombreArchivoSeleccionado.substring(0, indicePunto);
                }

                // Devolvemos el nombre del archivo sin la extensión
                return nombreArchivoSeleccionado;
            } else {
                Simulador.logger.logError("No hay archivos en el directorio: " + directorio);
                return null;
            }
        } else {
            Simulador.logger.logError("El directorio no existe o no es un directorio válido: "  + directorio);
            return null;
        }
    }
}