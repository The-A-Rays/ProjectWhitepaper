package com.project.aicomics.XML;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.project.aicomics.Audio;
import com.project.aicomics.Translations.Language;
import com.project.aicomics.controller.DevController;
import com.project.aicomics.service.OpenAIService;

public final class XMLGenerator extends XMLFile{
    OpenAIService ai = new OpenAIService();
    Audio aud = new Audio(this);
    
    public XMLGenerator(String fileName, Language language) {
        super(fileName);
        generateCaptions();
        try {
            aud.generateAudioXML(language);
        } catch (IOException e) {
            DevController.error("Fatal Error: Unable to create audio files for the XML", e);
        }
    }

    /**
     *  - {@link #generateDialogue} generates and inserts dialogue into the corresponding xml file
     */
    public static void generateDialogue(Scene s) {
        OpenAIService ai = new OpenAIService();
        List<String> dialogue = ai.GenerateDialogue(s);
        int bubbleIndex = 0;
        for (Panel p : s.getPanels()) {
            for (Position pos : p.getPosition()) {
                if (pos.getBubble() != null && bubbleIndex < dialogue.size()) {
                    pos.getBubble().setContent(dialogue.get(bubbleIndex));
                    bubbleIndex++;
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

    /**
   * Prints the XMLFile object into a new XML file with the
   * translated scenes next to the original.
   */
    public void Print(Language language, String fileName) {
        Document doc;
        try {doc = createDocument();}
        catch (ParserConfigurationException e) {
            DevController.error("Fatal error creating document builder", e);
            return;
        }
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
        int i = 0;              // Used to keep track of which text we are on for trans.get(i)
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
                    Element audio = doc.createElement("audio");
                    balloon.appendChild(audio);
                    audio.setTextContent(aud.getAudioFileName(b.getContent()));
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
                    Element audio = doc.createElement("audio");
                    balloon.appendChild(audio);
                    audio.setTextContent(aud.getAudioFileName(trans.get(i)));
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
        System.out.println("Done creating XML file");
    }

}
