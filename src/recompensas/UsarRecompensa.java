package recompensas;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import commons.Simulador;
import helpers.FileHelper;

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

                String rewardName = (name != null) ? name.getText() : "desconocida";
                
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
    
                Simulador.logger.log("Recompensa " + rewardName + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + rewardName + " usada.");
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

                String rewardName = (name != null) ? name.getText() : "desconocida";
                
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

                Simulador.logger.log("Recompensa " + rewardName + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + rewardName + " usada.");
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

                String rewardName = (name != null) ? name.getText() : "desconocida";
                
                Element give = root.element("give");
                Element building = give.element("building");
                String code = building.attributeValue("code");
                int tipo = Integer.parseInt(code);
    
                if (tipo == 2) {
                    // Crear tanque de rio
                } else {
                    // Crear tanque de mar
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
    
                Simulador.logger.log("Recompensa " + rewardName + " usada.");
                Simulador.transcriptor.transcribir("Recompensa " + rewardName + " usada.");
            } catch (Exception e) {
                Simulador.logger.logError("Error al procesar la recompensa del archivo: " + fileName + " Detalles: " + e.getMessage());
            }
        }
    }











    /**
     * Obtiene los nombres de las recompensas desde los archivos XML en el directorio "rewards". // TODO dejar de ultimo (falta añadir el metodo de canjear pisc y almacen) 
     * 
     * @return Un array de strings con los nombres de las recompensas extraídos de los archivos XML.
     */
    public static String[] getRewards() {
        List<String> rewardNames = new ArrayList<>();
        
        String[] xmlOptions = FileHelper.obtenerArchivosEnDirectorio("rewards");
        
        for (String fileName : xmlOptions) {
            if (fileName.endsWith(".xml")) {
                try {
                    File xmlFile = new File("rewards", fileName);
            
                    SAXReader reader = new SAXReader();
                    
                    Document document = reader.read(xmlFile);
            
                    Element nameElement = document.getRootElement().element("name");
            
                    if (nameElement != null) {
                        rewardNames.add(nameElement.getText());
                    } else {
                        Simulador.logger.logError("El archivo '" + fileName + "' no contiene la etiqueta <name>.");
                    }
            
                } catch (Exception e) {
                    Simulador.logger.logError("Error al procesar el archivo XML '" + fileName + "': " + e.getMessage());
                }
            }
        }
        return rewardNames.toArray(new String[0]);
    }
}
