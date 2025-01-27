package recompensas;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import commons.Simulador;

import helpers.FileHelper;
import helpers.InputHelper;

import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;

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
                                    Simulador.distribuirComida(cantidad , tipoDeComida);
                                }
                                break;
                            case "animal":
                                if (Simulador.almacenCentral != null) {
                                    Simulador.almacenCentral.añadirComidaAnimal(cantidad);
                                } else {
                                    Simulador.distribuirComida(cantidad , tipoDeComida);
                                }
                                break;
                            case "general":
                                if (Simulador.almacenCentral != null) {
                                    Simulador.almacenCentral.añadirComidaAnimal(cantidad / 2);
                                    Simulador.almacenCentral.añadirComidaVegetal(cantidad / 2);
                                } else {
                                    Simulador.distribuirComida(cantidad , tipoDeComida);
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
    
                Simulador.logger.logUsarRecompensa(name.getText());
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

                Simulador.logger.logUsarRecompensa(name.getText());
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
    public static boolean readTank(String fileName) {
        File xmlFile = new File("rewards/" + fileName);
        if (xmlFile.exists()) {
            try {
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
    
                Simulador.logger.logUsarRecompensa(name.getText());
                Simulador.transcriptor.transcribir("Recompensa " + name.getText() + " usada.");
            } catch (Exception e) {
                Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
            }
            return true;
        } else {
            return false;
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
                        
                        Simulador.logger.logUsarRecompensa(name.getText());
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

                        Simulador.logger.logUsarRecompensa(name.getText());
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

                        Simulador.logger.logUsarRecompensa(name.getText());
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
}