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

/** Clase que gestiona la creación de recompensas en formato XML. */
public class CrearRecompensa {

    /** Directorio donde se almacenan los archivos de recompensas. */
    private static final String REWARDS_DIRECTORY = "rewards/";

    /**
     * Crea una recompensa de pienso basada en el tipo especificado.
     * 
     * @param type el nivel de la recompensa (1-5).
     */
    public static void createPiensoReward(int type) {
        String fileName = REWARDS_DIRECTORY + "pienso_" + type + ".xml";
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
                root.addElement("origin").addText(Simulador.nombreEntidad);
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(String.valueOf(rarity));

                Element give = root.addElement("give");
                give.addElement("food")
                    .addAttribute("type", "animal")
                    .addText(String.valueOf(foodAmount));

                root.addElement("quantity").addText("1");
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una recompensa de algas basada en el tipo especificado.
     * 
     * @param type el nivel de la recompensa (1-5).
     */
    public static void createAlgasReward(int type) {
        String fileName = REWARDS_DIRECTORY + "algas_" + type + ".xml";
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

            } else {
                document = DocumentHelper.createDocument();
                Element root = document.addElement("reward");

                root.addElement("name").addText(name);
                root.addElement("origin").addText(Simulador.nombreEntidad);
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

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una recompensa de comida multipropósito basada en el tipo especificado.
     * 
     * @param type el nivel de la recompensa (1-5).
     */
    public static void createComidaReward(int type) {
        String fileName = REWARDS_DIRECTORY + "comida_" + type + ".xml";
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
                root.addElement("origin").addText(Simulador.nombreEntidad);
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

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una recompensa de monedas basada en el tipo especificado.
     * 
     * @param type el nivel de la recompensa (1-5).
     */
    public static void createMonedasReward(int type) {
        String fileName = REWARDS_DIRECTORY + "monedas_" + type + ".xml";
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
                root.addElement("origin").addText(Simulador.nombreEntidad);
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

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Crea una recompensa para un tanque de piscifactoría.
     * 
     * @param type el tipo de tanque (1-5).
     * @param part la parte del tanque (A o B).
     */
    public static void createTanqueReward(int type, String part) {
        String tipo = getTypeAmount(type) == 'r' ? "rio" : "mar";
        String fileName = REWARDS_DIRECTORY + "tanque_" + getTypeAmount(type) + ".xml";
        String name = "Tanque de " + tipo;
        String description = "Materiales para la construcción, de forma gratuita, de un tanque de una piscifactoría de " + tipo + ".";

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
                root.addElement("origin").addText(Simulador.nombreEntidad);
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(getTypeAmount(type) == 'r' ? "2" : "3");

                Element give = root.addElement("give");
                give.addElement("building")
                    .addAttribute("code", getTypeAmount(type) == 'r' ? "2" : "3")
                    .addText("Tanque de " + tipo);
                give.addElement("part")
                    .addText(part);
                give.addElement("total")
                    .addText("A");

                root.addElement("quantity").addText("1");
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una recompensa para una piscifactoría.
     * 
     * @param type el tipo de piscifactoría (1-5).
     * @param part la parte de la piscifactoría (A o B).
     */
    public static void createPiscifactoriaReward(int type, String part) {
        String tipo = getTypeAmount(type) == 'r' ? "rio" : "mar";
        String fileName = REWARDS_DIRECTORY + "piscifactoria_" + getTypeAmount(type) + "_" +  part.toLowerCase() + ".xml";
        String name = "Piscifactoria de " + tipo + " [" + part + "]";
        String description = "Materiales para la construcción de una piscifactoría de " + tipo + ". Con la parte A y B, puedes obtenerla de forma gratuita.";

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
                root.addElement("origin").addText(Simulador.nombreEntidad);
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(getTypeAmount(type) == 'r' ? "3" : "4");

                Element give = root.addElement("give");
                give.addElement("building")
                    .addAttribute("code", getTypeAmount(type) == 'r' ? "0" : "1")
                    .addText("Piscifactoria de " + tipo);
                give.addElement("part")
                    .addText(part);
                give.addElement("total")
                    .addText("AB");

                root.addElement("quantity").addText("1");
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una recompensa para un almacén central.
     * 
     * @param part la parte del almacén (A, B, C o D).
     */
    public static void createAlmacenReward(String part) {
        String fileName = REWARDS_DIRECTORY + "almacen_" +  part.toLowerCase() + ".xml";
        String name = "Almacen central " + " [" + part + "]";
        String description = "Materiales para la construcción de un almacén central. Con la parte A, B, C y D, puedes obtenerlo de forma gratuita.";

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
                root.addElement("origin").addText(Simulador.nombreEntidad);
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText("3");

                Element give = root.addElement("give");
                give.addElement("building")
                    .addAttribute("code", "4")
                    .addText("Almacen central");
                give.addElement("part")
                    .addText(part);
                give.addElement("total")
                    .addText("ABCD");

                root.addElement("quantity").addText("1");
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();

            Simulador.logger.log("Recompensa creada.");
            Simulador.transcriptor.transcribir("Recompensa " + name + " creada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convierte un número entero a su representación en números romanos.
     * 
     * @param number el número a convertir (1-5).
     * @return la representación en números romanos.
     */
    private static String romanize(int number) {
        String[] romans = {"I", "II", "III", "IV", "V"};
        return romans[number - 1];
    }

    /**
     * Obtiene la cantidad de comida vegetal o animal según el tipo.
     * 
     * @param type el nivel de la recompensa (1-5).
     * @return la cantidad de comida asignada.
     */
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

    /**
     * Obtiene la cantidad de comida multipropósito según el tipo.
     * 
     * @param type el nivel de la recompensa (1-5).
     * @return la cantidad de comida asignada.
     */
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

    /**
     * Obtiene la cantidad de monedas según el tipo.
     * 
     * @param type el nivel de la recompensa (1-5).
     * @return la cantidad de monedas asignada.
     */
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
    
    /**
     * Obtiene el tipo de recompensa ('r' para río, 'm' para mar).
     * 
     * @param type el nivel de la recompensa (1-5).
     * @return el tipo de recompensa ('r' o 'm').
     */
    private static char getTypeAmount(int type) {
        switch (type) {
            case 1: return 'r';
            case 2: return 'm';
            default: throw new IllegalArgumentException("Tipo inválido: " + type);
        }
    }
}