package com.project.aicomics.XML;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.controller.DevController;
import com.project.aicomics.service.OpenAIService;

public class XMLGenerator extends XMLFile{
    OpenAIService ai = new OpenAIService();
    
    public XMLGenerator(String fileName) {
        super(fileName);
    }

    /**
     *  - {@link #generateDialogue} generates and inserts dialogue into the corresponding xml file
     */
    public void generateDialogue() {
        List<String> dialogue = ai.GenerateDialogue(this);
        int bubbleIndex = 0;
        for (Scene s : scenes) {
            for (Panel p : s.getPanels()) {
                for (Position pos : p.getPosition()) {
                    if (pos.getBubble() != null && bubbleIndex < dialogue.size()) {
                        pos.getBubble().setContent(dialogue.get(bubbleIndex));
                        bubbleIndex++;
                    }
                }
            }
        }

    }

    /**
     * - {@link #generateCaptions()} generates and inserts captions into the corresponding xml file
     */
    public void generateCaptions() {
        List<String> captions = ai.GenerateCaptions(this);
        int captionIndex = 0;
        for (Scene s : scenes) {
            for (Panel p : s.getPanels()) {
                if(captionIndex < captions.size()){
                    p.setTitleBelow(captions.get(captionIndex));
                    captionIndex++;
                }
            }
        }
    }

    public void generateAudio() {

    }

    /**
     * - {@link #generatePrint()} Generates dialogue, captions, and translations and prints them to a new xml file
     * @param language Language to be translated to when printed
     * @param fileName String to be used as the fileName
     */
    public void generatePrint(Language language, String fileName) {
        generateDialogue();
        generateCaptions();
        generateAudio();
        Print(language, fileName);
    }


    /**
   * Prints the XMLFile object into a new XML file with the
   * translated scenes next to the original.
   */
    public void Print(Language language, String fileName) {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {builder = fac.newDocumentBuilder();}
        catch (ParserConfigurationException e) {
        DevController.error("Fatal error creating document builder", e);
        return;
        }
        Document doc = builder.newDocument();
        List<String> trans = getAllTranslatedText(language);
        // Create base file formatting
        Element comic = doc.createElement("comic");
        doc.appendChild(comic);
        // Insert Figures
        Element xmlFigures = doc.createElement("figures");
        comic.appendChild(xmlFigures);
        for (Figure f : figures) {
        addFigure(f, xmlFigures, doc);
        }
        // Insert Scenes
        /* This code is hard to read, but to put it simply it addes the scene little by little
        * Most of it is creating elements and appending them to their parents
        * The only content is the figures and balloons in position
        * and the setting, below, and border in panel
        * Everything else is formatting
        */
        int i = 0;
        Element xmlScenes = doc.createElement("scenes");
        comic.appendChild(xmlScenes);
        for (Scene s : scenes) {
        Element scene = doc.createElement("scene");
        xmlScenes.appendChild(scene);
        //each Panel object will have 2 panel elements(the oroginal one and the translatted one)
        for (Panel p : s.getPanels()) {
            //english panel first, then insert translated one
            Element panel = doc.createElement("panel");
            scene.appendChild(panel);
            for (Position pos : p.getPosition()) {
                Element position = doc.createElement(pos.getName().trim());
                panel.appendChild(position);

                addFigure(pos.getFigure(), position, doc);
                if(pos.getBubble() != null){
                    Bubble b = pos.getBubble();
                    Element balloon = doc.createElement("balloon");
                    position.appendChild(balloon);
                    balloon.setAttribute("status", b.getStatus().trim());
                    Element content = doc.createElement("content");
                    balloon.appendChild(content);
                    content.setTextContent(b.getContent().trim());
                }
                
            }
            //add translated panel
            Element translatedPanel = doc.createElement("panel");
            scene.appendChild(translatedPanel);
            for (Position pos : p.getPosition()) {
                Element position = doc.createElement(pos.getName().trim());
                translatedPanel.appendChild(position);
                
                addFigure(pos.getFigure(), position, doc);

                if(pos.getBubble() != null){
                    Bubble b = pos.getBubble();
                    Element balloon = doc.createElement("balloon");
                    position.appendChild(balloon);
                    balloon.setAttribute("status", b.getStatus().trim());
                    Element content = doc.createElement("content");
                    balloon.appendChild(content);
                    content.setTextContent(trans.get(i));
                    i = i+1;
                }

            }

            // Adding setting, below, and border
            if (p.getSetting() != null) {
            Element setting = doc.createElement("setting");
            panel.appendChild(setting);
            setting.setTextContent(p.getSetting());
            }
            if (p.getTitleBelow() != null) {
            Element below = doc.createElement("below");
            panel.appendChild(below);
            below.setTextContent(p.getTitleBelow());
            }
            if (p.getBorder() != null) {
            Element border = doc.createElement("border");
            panel.appendChild(border);
            border.setTextContent(p.getBorder());
            }
        }
            try {writeXML(doc, fileName);}
            catch (TransformerException te) {
            DevController.error("Error writing to new XML file", te);
            return;
            } 
        }
        DevController.status("Done creating XML file");
    }

    /**
     * Private method to abstract adding a figure to a parent element, as it is used repeatedly
     * @param f Figure passed for basic information about the figure
     * @param parent Parent element needs to be passed in order to append the child to it
     * @param doc In order to create the figure element the document is needed
     */
    private void addFigure(Figure f, Element parent, Document doc) {
        Element figure = doc.createElement("figure");
        parent.appendChild(figure);
        String[] attributeNames = {"id", "name", "appearance", "skin",
                                "hair", "lips", "pose", "facing"};
        String[] atrs = f.getAttributes();
        for (int i = 0; i < atrs.length; i++) {
        String atr = atrs[i];
        if (atr == null) continue;             // Skip if the attribute is empty
        Element child = doc.createElement(attributeNames[i]);
        child.setTextContent(atr);
        figure.appendChild(child);
        }
    }

    /**
     * Creates a new XML formatted file in src\main\resources
     * @param doc Document containing the elements to be put in file
     * @param fileName String filename or folder location ! MUST BE IN src\main\resources !
     *  ! DO NOT INCLUDE '.xml' IT SHOULD JUST BE THE NAME OF THE FILE !
     * @throws TransformerException
     */
    public static void writeXML(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (!fileName.endsWith(".xml")) fileName += ".xml";

        // pretty print XML
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName + ".xml"));

        transformer.transform(source, result);
    }

}
