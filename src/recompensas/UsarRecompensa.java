package recompensas;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class UsarRecompensa {

    public static void readFood(String fileName) {
        try {
            // Crear un lector SAXReader para leer el archivo XML
            File xmlFile = new File(fileName);
            SAXReader reader = new SAXReader();
            
            // Parsear el archivo XML
            Document document = reader.read(xmlFile);
            
            // Obtener el nodo raíz
            Element root = document.getRootElement();
            
            // Leer nodos "give"
            List<Element> giveList = root.elements("give");
            
            for (Element giveElement : giveList) {
                // Leer nodos "food" dentro de "give"
                List<Element> foodList = giveElement.elements("food");

                for (Element foodElement : foodList) {
                    String tipoDeComida = foodElement.attributeValue("type");
                    int cantidad = Integer.parseInt(foodElement.getText());

                    // Procesar la comida
                    processFood(tipoDeComida, cantidad);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readCoins(String fileName) {
        try {
            // Crear un lector SAXReader para leer el archivo XML
            File xmlFile = new File(fileName);
            SAXReader reader = new SAXReader();
            
            // Parsear el archivo XML
            Document document = reader.read(xmlFile);
            
            // Obtener el nodo raíz
            Element root = document.getRootElement();
            
            // Leer nodos "give"
            Element give = root.element("give");
            Element coins = give.element("coins");
            int cantidadMonedas = Integer.parseInt(coins.getText());

            System.out.println("Monedas: " + cantidadMonedas);
            
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readTank(String fileName) {
        try {
            // Crear un lector SAXReader para leer el archivo XML
            File xmlFile = new File(fileName);
            SAXReader reader = new SAXReader();
            
            // Parsear el archivo XML
            Document document = reader.read(xmlFile);
            
            // Obtener el nodo raíz
            Element root = document.getRootElement();
            
            // Leer nodos "give"
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processFood(String tipoDeComida, int cantidad) {
        // Método para procesar el tipo y la cantidad de comida
        // Puedes implementar lógica adicional aquí según sea necesario
        switch (tipoDeComida) {
            case "algae":
                System.out.println("vegetal: " + cantidad);
                break;
            case "animal":
                System.out.println("animal: " + cantidad);
                break;
            case "general":
                System.out.println("general: " + cantidad);
                break;
            default:
                // Lógica para tipos desconocidos
                break;
        }
    }

    public static void main(String[] args) {
        // Llama al método con el nombre del archivo XML
        readFood("rewards/algas_1.xml");
        readFood("rewards/pienso_1.xml");
        readFood("rewards/comida_1.xml");
        readCoins("rewards/monedas_1.xml");
    }
}
