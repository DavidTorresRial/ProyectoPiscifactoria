package recompensas;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import commons.Simulador;
import helpers.FileHelper;
import helpers.InputHelper;

import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/** Clase para gestionar recompensas desde archivos XML. */
public class UsarRecompensa {

    /**
     * Procesa una recompensa de tipo "comida" desde un archivo XML.
     * 
     * @param fileName Nombre del archivo XML que contiene la recompensa.
     */
    public static void readFood(String fileName) {
        File xmlFile = new File("rewards/" + fileName);
        if (xmlFile.exists()) {
            try {
                SAXReader reader = new SAXReader();
                
                Document document = reader.read(xmlFile);
                
                Element root = document.getRootElement();

                Element name = root.element("name");
                
                List<Element> giveList = root.elements("give");
                
                for (Element giveElement : giveList) {

                    List<Element> foodList = giveElement.elements("food");
    
                    for (Element foodElement : foodList) {
                        String tipoDeComida = foodElement.attributeValue("type");
                        int cantidad = Integer.parseInt(foodElement.getText());
    
                        switch (tipoDeComida) {
                            case "algae":
                                if (Simulador.almacenCentral != null) {
                                    Simulador.almacenCentral.añadirComidaVegetal(cantidad);
                                } else {
                                    Simulador.distribuirComida(cantidad , "algae");
                                }
                                break;
                            case "animal":
                                if (Simulador.almacenCentral != null) {
                                    Simulador.almacenCentral.añadirComidaAnimal(cantidad);
                                } else {
                                    Simulador.distribuirComida(cantidad , "animal");
                                }
                                break;
                            case "general":
                                if (Simulador.almacenCentral != null) {
                                    Simulador.almacenCentral.añadirComidaAnimal(cantidad / 2);
                                    Simulador.almacenCentral.añadirComidaVegetal(cantidad / 2);
                                } else {
                                    Simulador.distribuirComida(cantidad , "general");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
    
                Element quantityElement = root.element("quantity");
                int quantity = Integer.parseInt(quantityElement.getText());
    
                quantity--;
    
                quantityElement.setText(String.valueOf(quantity));
                    
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                writer.write(document);
                writer.close();
    
                if (quantity <= 0) {
                    xmlFile.delete();
                }
    
                Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
            } catch (Exception e) {
                Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
            }
        }
    }

    /**
     * Procesa una recompensa de tipo "monedas" desde un archivo XML.
     * 
     * @param fileName Nombre del archivo XML que contiene la recompensa.
     */
    public static void readCoins(String fileName) {
        File xmlFile = new File("rewards/" + fileName);
        if (xmlFile.exists()) {
            try {
                SAXReader reader = new SAXReader();
                
                Document document = reader.read(xmlFile);
                
                Element root = document.getRootElement();

                Element name = root.element("name");
                
                Element give = root.element("give");
                Element coins = give.element("coins");
                int cantidadMonedas = Integer.parseInt(coins.getText());
    
                Simulador.monedas.ganarMonedas(cantidadMonedas);
                
                Element quantityElement = root.element("quantity");
                int quantity = Integer.parseInt(quantityElement.getText());
    
                quantity--;
    
                quantityElement.setText(String.valueOf(quantity));
                    
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                writer.write(document);
                writer.close();
    
                if (quantity <= 0) {
                    xmlFile.delete();
                }

                Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
            } catch (Exception e) {
                Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
            }
        }
    }

    /**
     * Procesa una recompensa de tipo "tanque" desde un archivo XML.
     * 
     * @param fileName Nombre del archivo XML que contiene la recompensa.
     */
    public static void readTank(String fileName) {
        File xmlFile = new File("rewards/" + fileName);
        if (xmlFile.exists()) {
            try {
                SAXReader reader = new SAXReader();
                
                Document document = reader.read(xmlFile);
                
                Element root = document.getRootElement();

                Element name = root.element("name");
                
                Element give = root.element("give");
                Element building = give.element("building");
                String code = building.attributeValue("code");
                int tipo = Integer.parseInt(code);
    
                if (tipo == 2) {
                    // Crear tanque de rio // TODO 
                } else {
                    // Crear tanque de mar // TODO 
                }
    
                Element quantityElement = root.element("quantity");
                int quantity = Integer.parseInt(quantityElement.getText());
    
                quantity--;
    
                quantityElement.setText(String.valueOf(quantity));
                    
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                writer.write(document);
                writer.close();
    
                if (quantity <= 0) {
                    xmlFile.delete();
                }
    
                Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
            } catch (Exception e) {
                Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica si se han reunido todas las partes necesarias para una piscifactoria.
     * 
     * @param piscifactoriaRio Indica si se está verificando una piscifactoria de río (true) o de mar (false).
     * @return La piscifactoria creada si todas las partes están reunidas.
     */
    public static Piscifactoria readPiscifactoria(boolean piscifactoriaRio) {
        String partesRio = "";
        String partesMar = "";
        String total = "";
    
        String[] xmlOptions = FileHelper.obtenerArchivosEnDirectorio("rewards");
    
        for (String fileName : xmlOptions) {
            if (fileName.startsWith("piscifactoria_r") && fileName.endsWith(".xml")) {
                try {
                    File xmlFile = new File("rewards", fileName);

                    SAXReader reader = new SAXReader();

                    Document document = reader.read(xmlFile);
    
                    Element root = document.getRootElement();
    
                    Element nameElement = root.element("name");
                    if (nameElement != null) {
                        String name = nameElement.getText();
    
                        if (name.contains("Piscifactoria")) {
                            Element giveElement = root.element("give");
                            if (giveElement != null) {
                                String part = giveElement.elementText("part");
                                total = giveElement.elementText("total");
    
                                if (piscifactoriaRio && name.contains("de rio")) {
                                    if (!partesRio.contains(part)) {
                                        partesRio += part;
                                    }
                                } else if (!piscifactoriaRio && name.contains("de mar")) {
                                    if (!partesMar.contains(part)) {
                                        partesMar += part;
                                    }
                                }
                            }
                        }
                    } else {
                        Simulador.logger.logError("El archivo '" + fileName + "' no contiene la etiqueta <name>.");
                    }
                } catch (Exception e) {
                    Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                }
            } else {
                if (fileName.startsWith("piscifactoria_m") && fileName.endsWith(".xml")) {
                    try {
                        File xmlFile = new File("rewards", fileName);
    
                        SAXReader reader = new SAXReader();
    
                        Document document = reader.read(xmlFile);
        
                        Element root = document.getRootElement();
        
                        Element nameElement = root.element("name");
                        if (nameElement != null) {
                            String name = nameElement.getText();
        
                            if (name.contains("Piscifactoria")) {
                                Element giveElement = root.element("give");
                                if (giveElement != null) {
                                    String part = giveElement.elementText("part");
                                    total = giveElement.elementText("total");
        
                                    if (piscifactoriaRio && name.contains("de rio")) {
                                        if (!partesRio.contains(part)) {
                                            partesRio += part;
                                        }
                                    } else if (!piscifactoriaRio && name.contains("de mar")) {
                                        if (!partesMar.contains(part)) {
                                            partesMar += part;
                                        }
                                    }
                                }
                            }
                        } else {
                            Simulador.logger.logError("El archivo '" + fileName + "' no contiene la etiqueta <name>.");
                        }
                    } catch (Exception e) {
                        Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                    }
                }
            }
        }
    
        if (partesRio.length() == total.length() && piscifactoriaRio) {
            for (String fileName : xmlOptions) {
                if (fileName.startsWith("piscifactoria_r") && fileName.endsWith(".xml")) {
                    try {
                        File xmlFile = new File("rewards", fileName);

                        SAXReader reader = new SAXReader();
                
                        Document document = reader.read(xmlFile);
                
                        Element root = document.getRootElement();

                        Element name = root.element("name");

                        Element quantityElement = root.element("quantity");
                        int quantity = Integer.parseInt(quantityElement.getText());
            
                        quantity--;
            
                        quantityElement.setText(String.valueOf(quantity));
                            
                        OutputFormat format = OutputFormat.createPrettyPrint();
                        XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                        writer.write(document);
                        writer.close();
            
                        if (quantity <= 0) {
                            xmlFile.delete();
                        }
                        
                        Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                        Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
                    } catch (Exception e) {
                        Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                    }
                }
            }
            String nombre = InputHelper.readString("Ingrese el nombre para la piscifactoria de rio: ");
            return new PiscifactoriaDeRio(nombre);

        } else if (partesMar.length() == total.length() && !piscifactoriaRio) {
            for (String fileName : xmlOptions) {
                if (fileName.startsWith("piscifactoria_m") && fileName.endsWith(".xml")) {
                    try {
                        File xmlFile = new File("rewards", fileName);

                        SAXReader reader = new SAXReader();
                
                        Document document = reader.read(xmlFile);
                
                        Element root = document.getRootElement();

                        Element name = root.element("name");

                        Element quantityElement = root.element("quantity");
                        int quantity = Integer.parseInt(quantityElement.getText());
            
                        quantity--;
            
                        quantityElement.setText(String.valueOf(quantity));
                            
                        OutputFormat format = OutputFormat.createPrettyPrint();
                        XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                        writer.write(document);
                        writer.close();
            
                        if (quantity <= 0) {
                            xmlFile.delete();
                        }

                        Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                        Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
                    } catch (Exception e) {
                        Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());

                    }
                }
            }
            String nombre = InputHelper.readString("Ingrese el nombre para la piscifactoria de mar: ");
            return new PiscifactoriaDeMar(nombre);
        }
        return null;
    }

    /**
     * Verifica si se han reunido todas las partes necesarias para un Almacen Central.
     * 
     * @return true si todas las partes del Almacén Central están completas, 
     *         false si faltan partes para completar el Almacén Central.
     */
    public static boolean readAlmacenCentral() {
        String partesAlmacenCentral = "";
        String total = "";
    
        String[] xmlOptions = FileHelper.obtenerArchivosEnDirectorio("rewards");
    
        for (String fileName : xmlOptions) {
            if (fileName.startsWith("almacen") && fileName.endsWith(".xml")) {
                try {
                    File xmlFile = new File("rewards", fileName);
    
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(xmlFile);
    
                    Element root = document.getRootElement();
    
                    Element nameElement = root.element("name");
                    if (nameElement != null) {
                        String name = nameElement.getText();
    
                        if (name.contains("Almacen central")) {
                            Element giveElement = root.element("give");
                            if (giveElement != null) {
                                String part = giveElement.elementText("part");
                                total = giveElement.elementText("total");
    
                                if (part != null && !partesAlmacenCentral.contains(part)) {
                                    partesAlmacenCentral += part;
                                }
                            }
                        }
                    } else {
                        Simulador.logger.logError("El archivo '" + fileName + "' no contiene la etiqueta <name>.");
                    }
                } catch (Exception e) {
                    Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                }
            }
        }
    
        if (partesAlmacenCentral.length() == total.length()) {
            for (String fileName : xmlOptions) {
                if (fileName.startsWith("almacen") && fileName.endsWith(".xml")) {
                    try {
                        File xmlFile = new File("rewards", fileName);

                        SAXReader reader = new SAXReader();
                
                        Document document = reader.read(xmlFile);
                
                        Element root = document.getRootElement();

                        Element name = root.element("name");

                        Element quantityElement = root.element("quantity");
                        int quantity = Integer.parseInt(quantityElement.getText());
            
                        quantity--;
            
                        quantityElement.setText(String.valueOf(quantity));
                            
                        OutputFormat format = OutputFormat.createPrettyPrint();
                        XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
                        writer.write(document);
                        writer.close();
            
                        if (quantity <= 0) {
                            xmlFile.delete();
                        }

                        Simulador.logger.log("Recompensa " + name.getText() + " usada.");
                        Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
                    } catch (Exception e) {
                        Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
                    }
                }
            }
            return true;
        } else {
            return false;
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
                                        break;
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
                                        break;
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
                                        break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
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