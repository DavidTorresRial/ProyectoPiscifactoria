package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                    carpetaFile.mkdirs();
                }
            } catch (Exception e) {
                Simulador.logger.logError("Ocurrió un error inesperado al intentar crear la carpeta '" + carpeta.trim() + "': " + e.getMessage());
            }
        }
    }

    /**
     * Comprueba si un directorio contiene archivos o subdirectorios.
     *
     * @param rutaDirectorio Ruta del directorio.
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

    /**
     * Muestra un menú con los nombres de los archivos en un directorio y permite al usuario seleccionar uno.
     *
     * @param rutaDirectorio Ruta del directorio.
     * @return El nombre del archivo seleccionado.
     */
    public static String mostrarMenuConArchivos(String rutaDirectorio) {
        File folder = new File(rutaDirectorio);

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
                int opcion = InputHelper.solicitarNumero(1, archivos.length);

                String nombreArchivoSeleccionado = archivos[opcion - 1].getName();
                int indicePunto = nombreArchivoSeleccionado.lastIndexOf(".");
                if (indicePunto > 0) {
                    nombreArchivoSeleccionado = nombreArchivoSeleccionado.substring(0, indicePunto);
                }
                return nombreArchivoSeleccionado;

            } else {
                Simulador.logger.logError("No hay archivos en el directorio: " + rutaDirectorio);
                return null;
            }
        } else {
            Simulador.logger.logError("El directorio no existe o no es un directorio válido: "  + rutaDirectorio);
            return null;
        }
    }

    /**
     * Obtiene los nombres de los archivos en un directorio.
     * 
     * @param rutaDirectorio Ruta del directorio.
     * @return Un arreglo con los nombres de los archivos.
     */
    public static String[] obtenerArchivosEnDirectorio(String rutaDirectorio) {
        File folder = new File(rutaDirectorio);
    
        if (folder.exists() && folder.isDirectory()) {
            File[] archivos = folder.listFiles();
    
            if (archivos != null && archivos.length > 0) {

                List<String> nombresArchivos = new ArrayList<>();
    
                for (File archivo : archivos) {
                    if (archivo.isFile()) {
                        String nombreArchivo = archivo.getName();
                        nombresArchivos.add(nombreArchivo);
                    }
                }
                return nombresArchivos.toArray(new String[0]);

            } else {
                Simulador.logger.logError("No hay archivos en el directorio: " + rutaDirectorio);
                return new String[0];
            }
        } else {
            Simulador.logger.logError("El directorio no existe o no es un directorio válido: " + rutaDirectorio);
            return new String[0];
        }
    }
}