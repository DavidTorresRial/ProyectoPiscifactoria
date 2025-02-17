package recompensas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import commons.Simulador;

/** Clase responsable de la creación y gestión de recompensas en formato XML. */
public class CrearRecompensa {

    /** Directorio donde se almacenan los archivos de recompensas. */
    private static final String REWARDS_DIRECTORY = "rewards/";

    /**
     * Crea o actualiza un archivo de recompensa con los datos proporcionados.
     *
     * @param fileName    Nombre del archivo XML donde se guardará la recompensa.
     * @param name        Nombre de la recompensa.
     * @param description Descripción de la recompensa.
     * @param rarity      Rareza de la recompensa (del 1 al 5).
     * @param addGive     Acción que define los elementos de la etiqueta <give>.
     */
    private static void createOrUpdateReward(String fileName, String name, String description, String rarity, Consumer<Element> addGive) {
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
                root.addElement("origin").addText(Simulador.instance.getNombreEntidad());
                root.addElement("desc").addText(description);
                root.addElement("rarity").addText(rarity);
                
                Element give = root.addElement("give");
                addGive.accept(give);
                
                root.addElement("quantity").addText("1");
            }
            
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();
            
            Simulador.instance.registro.registroCrearRecompensa(name);
        } catch (IOException | DocumentException e) {
            Simulador.instance.registro.registroLogError("Error al crear la recompensa '" + name + "' en el archivo '" + fileName + "': " + e.getMessage());
        }
    }

    // MÉTODOS DE CREACIÓN DE RECOMPENSAS

    /**
     * Crea una recompensa de pienso para peces carnívoros y omnívoros.
     *
     * @param type Nivel del pienso (1-5).
     */
    public static void createPiensoReward(int type) {
        String fileName = REWARDS_DIRECTORY + "pienso_" + type + ".xml";
        String name = "Pienso " + romanize(type);
        String rarity = String.valueOf(type - 1);
        int foodAmount = getFoodAmount(type);
        String description = foodAmount + " unidades de pienso hecho a partir de peces, moluscos y otros seres marinos para alimentar a peces carnívoros y omnívoros.";

        createOrUpdateReward(fileName, name, description, rarity, give -> 
            give.addElement("food")
                .addAttribute("type", "animal")
                .addText(String.valueOf(foodAmount))
        );
    }

    /**
     * Crea una recompensa de algas para peces filtradores.
     *
     * @param type Nivel de las algas (1-5).
     */
    public static void createAlgasReward(int type) {
        String fileName = REWARDS_DIRECTORY + "algas_" + type + ".xml";
        String name = "Algas " + romanize(type);
        String rarity = String.valueOf(type - 1);
        int foodAmount = getFoodAmount(type);
        String description = foodAmount + " cápsulas de algas para alimentar peces filtradores y omnívoros.";

        createOrUpdateReward(fileName, name, description, rarity, give ->
            give.addElement("food")
                .addAttribute("type", "algae")
                .addText(String.valueOf(foodAmount))
        );
    }

    /**
     * Crea una recompensa de comida general apta para todos los peces.
     *
     * @param type Nivel de la comida (1-5).
     */
    public static void createComidaReward(int type) {
        String fileName = REWARDS_DIRECTORY + "comida_" + type + ".xml";
        String name = "Comida general " + romanize(type);
        String rarity = String.valueOf(type - 1);
        int foodAmount = getSharedFoodAmount(type);
        String description = foodAmount + " unidades de pienso multipropósito para todo tipo de peces.";

        createOrUpdateReward(fileName, name, description, rarity, give ->
            give.addElement("food")
                .addAttribute("type", "general")
                .addText(String.valueOf(foodAmount))
        );
    }

    /**
     * Crea una recompensa de monedas.
     *
     * @param type Nivel de las monedas (1-5).
     */
    public static void createMonedasReward(int type) {
        String fileName = REWARDS_DIRECTORY + "monedas_" + type + ".xml";
        String name = "Monedas " + romanize(type);
        String rarity = String.valueOf(type - 1);
        int coinsAmount = getCoinsAmount(type);
        String description = coinsAmount + " monedas";

        createOrUpdateReward(fileName, name, description, rarity, give ->
            give.addElement("coins")
                .addText(String.valueOf(coinsAmount))
        );
    }

    /**
     * Crea una recompensa para la construcción de un tanque de piscifactoría.
     *
     * @param type Tipo de tanque (1 para río, 2 para mar).
     */
    public static void createTanqueReward(int type) {
        char t = getTypeAmount(type);
        String tipo = (t == 'r' ? "rio" : "mar");
        String fileName = REWARDS_DIRECTORY + "tanque_" + t + ".xml";
        String name = "Tanque de " + tipo;
        String rarity = (t == 'r' ? "2" : "3");
        String description = "Materiales para la construcción, de forma gratuita, de un tanque de una piscifactoría de " + tipo + ".";

        createOrUpdateReward(fileName, name, description, rarity, give -> {
            give.addElement("building")
                .addAttribute("code", (t == 'r' ? "2" : "3"))
                .addText("Tanque de " + tipo);
            give.addElement("part").addText("A");
            give.addElement("total").addText("A");
        });
    }

    /**
     * Crea una recompensa para la construcción de una piscifactoría.
     *
     * @param type Tipo de piscifactoría (1 para río, 2 para mar).
     * @param part Parte específica (A o B).
     */
    public static void createPiscifactoriaReward(int type, String part) {
        char t = getTypeAmount(type);
        String fileName = REWARDS_DIRECTORY + "pisci_" + t + "_" + part.toLowerCase() + ".xml";
        String tipo = (t == 'r' ? "rio" : "mar");
        String name = "Piscifactoria de " + tipo + " [" + part + "]";
        String rarity = (t == 'r' ? "3" : "4");
        String description = "Materiales para la construcción de una piscifactoría de " + tipo + ". Con la parte A y B, puedes obtenerla de forma gratuita.";

        createOrUpdateReward(fileName, name, description, rarity, give -> {
            give.addElement("building")
                .addAttribute("code", (t == 'r' ? "0" : "1"))
                .addText("Piscifactoria de " + tipo);
            give.addElement("part").addText(part);
            give.addElement("total").addText("AB");
        });
    }

    /**
     * Crea una recompensa para la construcción de un almacén central.
     *
     * @param part Parte específica (A, B, C o D).
     */
    public static void createAlmacenReward(String part) {
        String fileName = REWARDS_DIRECTORY + "almacen_" + part.toLowerCase() + ".xml";
        String name = "Almacen central [" + part + "]";
        String rarity = "3";
        String description = "Materiales para la construcción de un almacén central. Con la parte A, B, C y D, puedes obtenerlo de forma gratuita.";

        createOrUpdateReward(fileName, name, description, rarity, give -> {
            give.addElement("building")
                .addAttribute("code", "4")
                .addText("Almacen central");
            give.addElement("part").addText(part);
            give.addElement("total").addText("ABCD");
        });
    }

    /**
     * Crea una recompensa para una granja de fitoplancton.
     *
     * @param part Parte específica (A, B, C o D).
     */
    public static void createGranjaFitoplanctonReward(String part) {
        String fileName = REWARDS_DIRECTORY + "fitoplancton_" + part.toLowerCase() + ".xml";
        String name = "Granja de fitoplancton [" + part + "]";
        String rarity = "3";
        String description = "Materiales para la construcción de una granja de fitoplancton. Con la parte A, B, C y D, puedes obtenerla de forma gratuita.";

        createOrUpdateReward(fileName, name, description, rarity, give -> {
            give.addElement("building")
                .addAttribute("code", "5")
                .addText("Granja de fitoplancton");
            give.addElement("part").addText(part);
            give.addElement("total").addText("ABCD");
        });
    }

    /**
     * Crea una recompensa para una granja de langostinos.
     *
     * @param part Parte específica (A, B, C o D).
     */
    public static void createGranjaLangostinosReward(String part) {
        String fileName = REWARDS_DIRECTORY + "langostinos_" + part.toLowerCase() + ".xml";
        String name = "Granja de langostinos [" + part + "]";
        String rarity = "3";
        String description = "Materiales para la construcción de una granja de langostinos. Con la parte A, B, C y D, puedes obtenerla de forma gratuita.";

        createOrUpdateReward(fileName, name, description, rarity, give -> {
            give.addElement("building")
                .addAttribute("code", "6")
                .addText("Granja de langostinos");
            give.addElement("part").addText(part);
            give.addElement("total").addText("ABCD");
        });
    }

    /**
     * Convierte un número entero entre 1 y 5 a su representación en números romanos.
     *
     * @param number Número a convertir.
     * @return Representación en números romanos (I, II, III, IV o V).
     */
    private static String romanize(int number) {
        String[] romans = {"I", "II", "III", "IV", "V"};
        return romans[number - 1];
    }

    /**
     * Calcula la cantidad de comida específica según el nivel.
     *
     * @param type Nivel de recompensa (1-5).
     * @return Cantidad de comida asignada.
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
     * Calcula la cantidad de comida multipropósito según el nivel.
     *
     * @param type Nivel de recompensa (1-5).
     * @return Cantidad de comida multipropósito.
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
     * Calcula la cantidad de monedas según el nivel.
     *
     * @param type Nivel de recompensa (1-5).
     * @return Cantidad de monedas.
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
     * Determina el tipo de piscifactoría asociado al nivel proporcionado.
     *
     * @param type Nivel de recompensa (1 o 2).
     * @return 'r' para río o 'm' para mar.
     */
    private static char getTypeAmount(int type) {
        switch (type) {
            case 1: return 'r';
            case 2: return 'm';
            default: throw new IllegalArgumentException("Tipo inválido: " + type);
        }
    }
}