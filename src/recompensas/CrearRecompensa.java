package recompensas;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;

import commons.Simulador;

public class CrearRecompensa {

    public static void createAlgasReward(int type) {
        String fileName = "algas_" + type + ".xml";
        String name = "Algas " + romanize(type);
        int rarity = type - 1;
        int foodAmount = getFoodAmount(type);
        String description = foodAmount + " cápsulas de algas para alimentar peces filtradores y omnívoros.";

        try {
            File file = new File(fileName);
            Document document;

            if (file.exists()) {
                SAXReader reader = new SAXReader();
                document = reader.read(file);

                Element root = document.getRootElement();
                Element quantityElement = root.element("quantity");
                if (quantityElement != null) {
                    int currentQuantity = Integer.parseInt(quantityElement.getText());
                    quantityElement.setText(String.valueOf(currentQuantity + 1));
                } else {
                    root.addElement("quantity").addText("1");
                }

                System.out.println("El archivo " + fileName + " existe. Se aumentó el valor de <quantity>.");

            } else {
                document = DocumentHelper.createDocument();
                Element root = document.addElement("reward");

                root.addElement("name").addText(name);
                root.addElement("origin").addText(Simulador.getNombreEntidad());
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(String.valueOf(rarity));

                Element give = root.addElement("give");
                give.addElement("food")
                    .addAttribute("type", "algae")
                    .addText(String.valueOf(foodAmount));

                root.addElement("quantity").addText("1");
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPiensoReward(int type) {
        String fileName = "pienso_" + type + ".xml";
        String name = "Pienso " + romanize(type);
        int rarity = type - 1;
        int foodAmount = getFoodAmount(type);
        String description = foodAmount + " unidades de pienso hecho a partir de peces, moluscos y otros seres marinos para alimentar a peces carnívoros y omnívoros.";

        try {
            File file = new File(fileName);
            Document document;

            if (file.exists()) {
                SAXReader reader = new SAXReader();
                document = reader.read(file);

                Element root = document.getRootElement();
                Element quantityElement = root.element("quantity");
                if (quantityElement != null) {
                    int currentQuantity = Integer.parseInt(quantityElement.getText());
                    quantityElement.setText(String.valueOf(currentQuantity + 1));
                } else {
                    root.addElement("quantity").addText("1");
                }

            } else {
                document = DocumentHelper.createDocument();
                Element root = document.addElement("reward");

                root.addElement("name").addText(name);
                root.addElement("origin").addText(Simulador.getNombreEntidad());
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(String.valueOf(rarity));

                Element give = root.addElement("give");
                give.addElement("food")
                    .addAttribute("type", "animal")
                    .addText(String.valueOf(foodAmount));

                root.addElement("quantity").addText("1");
            }

            // Guardar el documento actualizado o nuevo
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMonedasReward(int type) {
        String fileName = "monedas_" + type + ".xml";
        String name = "Monedas " + romanize(type);
        int rarity = type - 1;
        int coinsAmount = getCoinsAmount(type);
        String description = coinsAmount + " monedas";

        try {
            File file = new File(fileName);
            Document document;

            if (file.exists()) {
                SAXReader reader = new SAXReader();
                document = reader.read(file);

                Element root = document.getRootElement();
                Element quantityElement = root.element("quantity");
                if (quantityElement != null) {
                    int currentQuantity = Integer.parseInt(quantityElement.getText());
                    quantityElement.setText(String.valueOf(currentQuantity + 1));
                } else {
                    root.addElement("quantity").addText("1");
                }

            } else {
                document = DocumentHelper.createDocument();
                Element root = document.addElement("reward");

                root.addElement("name").addText(name);
                root.addElement("origin").addText(Simulador.getNombreEntidad());
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(String.valueOf(rarity));

                Element give = root.addElement("give");
                give.addElement("coins")
                    .addText(String.valueOf(coinsAmount));

                root.addElement("quantity").addText("1");
            }

            // Guardar el documento actualizado o nuevo
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createComidaReward(int type) {
        String fileName = "comida_" + type + ".xml";
        String name = "Comida " + romanize(type);
        int rarity = type - 1;
        int foodAmount = getSharedFoodAmount(type);
        String description = foodAmount + " unidades de pienso multipropósito para todo tipo de peces.";

        try {
            File file = new File(fileName);
            Document document;

            if (file.exists()) {
                SAXReader reader = new SAXReader();
                document = reader.read(file);

                Element root = document.getRootElement();
                Element quantityElement = root.element("quantity");
                if (quantityElement != null) {
                    int currentQuantity = Integer.parseInt(quantityElement.getText());
                    quantityElement.setText(String.valueOf(currentQuantity + 1));
                } else {
                    root.addElement("quantity").addText("1");
                }

            } else {
                document = DocumentHelper.createDocument();
                Element root = document.addElement("reward");

                root.addElement("name").addText(name);
                root.addElement("origin").addText(Simulador.getNombreEntidad());
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(String.valueOf(rarity));

                Element give = root.addElement("give");
                give.addElement("food")
                    .addAttribute("type", "general")
                    .addText(String.valueOf(foodAmount));

                root.addElement("quantity").addText("1");
            }

            // Guardar el documento actualizado o nuevo
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para convertir números a números romanos
    private static String romanize(int number) {
        String[] romans = {"I", "II", "III", "IV", "V"};
        return romans[number - 1];
    }

    // Método auxiliar para definir la cantidad de comida según el tipo
    private static int getFoodAmount(int type) {
        switch (type) {
            case 1: return 100;
            case 2: return 200;
            case 3: return 500;
            case 4: return 1000;
            case 5: return 2000;
            default: throw new IllegalArgumentException("Tipo inválido: " + type);
        }
    }

    private static int getSharedFoodAmount(int type) {
        switch (type) {
            case 1: return 50;
            case 2: return 100;
            case 3: return 250;
            case 4: return 500;
            case 5: return 1000;
            default: throw new IllegalArgumentException("Tipo inválido: " + type);
        }
    }

    private static int getCoinsAmount(int type) {
        switch (type) {
            case 1: return 100;
            case 2: return 300;
            case 3: return 500;
            case 4: return 750;
            case 5: return 1000;
            default: throw new IllegalArgumentException("Tipo inválido: " + type);
        }
    }   
}