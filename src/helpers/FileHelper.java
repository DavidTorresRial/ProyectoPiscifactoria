package helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
                        Simulador.registro.registroLogError("No se pudo crear la carpeta: " + carpetaFile);
                    }
                }
            } catch (SecurityException se) {
                Simulador.registro.registroLogError("Permisos insuficientes para crear la carpeta: " + carpetaFile + " - " + se.getMessage());
            } catch (Exception e) {
                Simulador.registro.registroLogError("Error inesperado al intentar crear la carpeta: " + carpetaFile + " - " + e.getMessage());
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
            return directorio.list().length > 0;
            
        } else {
            Simulador.registro.registroLogError("El directorio no existe: " + rutaDirectorio);
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
                System.out.println("0. Iniciar una nueva partida.");
                int opcion = InputHelper.solicitarNumero(0, archivos.length);

                if (opcion != 0) {
                    String nombreArchivoSeleccionado = archivos[opcion - 1].getName();
                    int indicePunto = nombreArchivoSeleccionado.lastIndexOf(".");
                    if (indicePunto > 0) {
                        nombreArchivoSeleccionado = nombreArchivoSeleccionado.substring(0, indicePunto);
                    }
                    return nombreArchivoSeleccionado;

                } else {
                    return null;
                }

            } else {
                Simulador.registro.registroLogError("No hay archivos en el directorio: " + rutaDirectorio);
                return null;
            }
        } else {
            Simulador.registro.registroLogError("El directorio no existe o no es un directorio válido: "  + rutaDirectorio);
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
                Arrays.sort(archivos, Comparator.comparing(File::getName));

                List<String> nombresArchivos = new ArrayList<>();

                for (File archivo : archivos) {
                    if (archivo.isFile()) {
                        String nombreArchivo = archivo.getName();
                        nombresArchivos.add(nombreArchivo);
                    }
                }
                return nombresArchivos.toArray(new String[0]);
            } else {
                Simulador.registro.registroLogError("No hay archivos en el directorio: " + rutaDirectorio);
                return new String[0];
            }
        } else {
            Simulador.registro.registroLogError("El directorio no existe o no es un directorio válido: " + rutaDirectorio);
            return new String[0];
        }
    }

    /**
     * Obtiene los nombres de las recompensas desde los archivos XML en el directorio "rewards".
     * 
     * @return Un array de strings con los nombres de las recompensas extraídos de los archivos XML.
     */
    public static String[] getRewards() {
        List<String> rewardNames = new ArrayList<>();
        
        Boolean[] partesAlmacen = {false, false, false, false};

        Boolean[] partesPiscifactoriaRio = {false, false};
        Boolean[] partesPiscifactoriaMar = {false, false};
        
        String[] xmlOptions = FileHelper.obtenerArchivosEnDirectorio("rewards");

        for (String fileName : xmlOptions) {
            if (fileName.endsWith(".xml")) {
                try {
                    File xmlFile = new File("rewards", fileName);

                    SAXReader reader = new SAXReader();
                    Document document = reader.read(xmlFile);

                    Element nameElement = document.getRootElement().element("name");
                    Element buildingElement = document.getRootElement().element("give").element("building");

                    // Si no tiene la etiqueta <building>, lo agregamos como recompensa
                    if (nameElement != null && buildingElement == null) {
                        rewardNames.add(nameElement.getText());
                    } else {
                        // Procesar las partes para Almacén central
                        if (buildingElement != null && buildingElement.getText().contains("Almacen central")) {
                            Element partElement = document.getRootElement().element("give").element("part");

                            if (partElement != null) {
                                String partName = partElement.getText().trim();
                                switch (partName) {
                                    case "A":
                                        partesAlmacen[0] = true;
                                        break;
                                    case "B":
                                        partesAlmacen[1] = true;
                                        break;
                                    case "C":
                                        partesAlmacen[2] = true;
                                        break;
                                    case "D":
                                        partesAlmacen[3] = true;
                                        break;
                                    default:
                                }
                            }
                        }

                        // Procesar las partes para Piscifactoria de Mar
                        if (buildingElement != null && buildingElement.getText().contains("Piscifactoria de mar")) {
                            Element partElement = document.getRootElement().element("give").element("part");

                            if (partElement != null) {
                                String partName = partElement.getText().trim();
                                switch (partName) {
                                    case "A":
                                        partesPiscifactoriaMar[0] = true;
                                        break;
                                    case "B":
                                        partesPiscifactoriaMar[1] = true;
                                        break;
                                    default:
                                }
                            }
                        }

                        // Procesar las partes para Piscifactoria de Río
                        if (buildingElement != null && buildingElement.getText().contains("Piscifactoria de rio")) {
                            Element partElement = document.getRootElement().element("give").element("part");

                            if (partElement != null) {
                                String partName = partElement.getText().trim();
                                switch (partName) {
                                    case "A":
                                        partesPiscifactoriaRio[0] = true;
                                        break;
                                    case "B":
                                        partesPiscifactoriaRio[1] = true;
                                        break;
                                    default:
                                }
                            }
                        }

                        // Procesar las partes para Tanque de mar
                        if (buildingElement != null && buildingElement.getText().contains("Tanque de mar")) {
                            Element partElement = document.getRootElement().element("give").element("part");

                            if (partElement != null) {
                                rewardNames.add("Tanque de mar [A]");

                            }
                        }

                        // Procesar las partes para Tanque de rio
                        if (buildingElement != null && buildingElement.getText().contains("Tanque de rio")) {
                            Element partElement = document.getRootElement().element("give").element("part");

                            if (partElement != null) {
                                rewardNames.add("Tanque de rio [A]");
                            }
                        }
                    }
                } catch (Exception e) {
                    Simulador.registro.registroLogError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                }
            }
        }

        // Procesar y agregar el "Almacen central" a las recompensas
        String almacenParts = "";
        for (int i = 0; i < partesAlmacen.length; i++) {
            if (partesAlmacen[i]) {
                almacenParts += (char) ('A' + i);
            } else {
                almacenParts += "x";
            }
        }
        if (partesAlmacen[0] == true || partesAlmacen[1] == true || partesAlmacen[2] == true || partesAlmacen[3] == true) {
            rewardNames.add("Almacen central [" + almacenParts + "]");
        }


        // Procesar y agregar la "Piscifactoria de Mar" a las recompensas
        String piscifactoriaMarParts = "";
        for (int i = 0; i < partesPiscifactoriaMar.length; i++) {
            if (partesPiscifactoriaMar[i]) {
                piscifactoriaMarParts += (char) ('A' + i);
            } else {
                piscifactoriaMarParts += "x";
            }
        }
        if (partesPiscifactoriaMar[0] == true || partesPiscifactoriaMar[1] == true) {
            rewardNames.add("Piscifactoria de mar [" + piscifactoriaMarParts + "]");
        }
        

        // Procesar y agregar la "Piscifactoria de Río" a las recompensas
        String piscifactoriaRioParts = "";
        for (int i = 0; i < partesPiscifactoriaRio.length; i++) {
            if (partesPiscifactoriaRio[i]) {
                piscifactoriaRioParts += (char) ('A' + i);
            } else {
                piscifactoriaRioParts += "x";
            }
        }
        if (partesPiscifactoriaRio[0] == true || partesPiscifactoriaRio[1] == true) {
            rewardNames.add("Piscifactoria de rio [" + piscifactoriaRioParts + "]");
        }
        
        return rewardNames.toArray(new String[0]);
    }

    /**
     * Elimina todo el contenido después del primer corchete "[" de cada opción en el array.
     * 
     * @param opciones Array de strings que contienen las recompensas.
     * @return Un array de strings con las recompensas, sin los corchetes.
     */
    public static String[] getRewardsWithoutBrackets(String[] opciones) {
        List<String> rewardsWithoutBrackets = new ArrayList<>();
        
        for (String opcion : opciones) {
            int bracketIndex = opcion.indexOf("[");
            
            if (bracketIndex == -1) {
                rewardsWithoutBrackets.add(opcion);
            } else {
                String modifiedOption = opcion.substring(0, bracketIndex).trim();
                rewardsWithoutBrackets.add(modifiedOption);
            }
        }
        return rewardsWithoutBrackets.toArray(new String[0]);
    }
}