package com.project.aicomics.vignette;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.project.aicomics.Translations;
import com.project.aicomics.XML.Bubble;
import com.project.aicomics.XML.Figure;
import com.project.aicomics.XML.Panel;
import com.project.aicomics.XML.Position;
import com.project.aicomics.XML.Scene;
import com.project.aicomics.XML.XMLFile;

// This class should take a list of VignetteSchemas and turn it into a list of Vignettes
// I think this makes more sense for now, and having the file reading separate helps make the logic more understandable

public class VignetteManager {

    private final List<Vignette> vignettes;

    /**
     * Constructor: Accepts a list of VignetteSchema objects and creates Vignettes from them.
     * @param schemas List of VignetteSchema objects to be converted into Vignettes.
     * @param translator Translations object for translating vignette text.
     */
    public VignetteManager(List<VignetteSchema> schemas, Translations translator) {
        this.vignettes = new ArrayList<>();
        createVignettes(schemas, translator);
    }


    /**
     * Generates a vignette list based on the loaded schemas
     * @param translator A Translations Object to translate the text into the selected language
     * @param shcemas the input list from which to create the Vignette objects
     * @return A fully selected vignette
     */
    public final void createVignettes(List<VignetteSchema> schemas, Translations translator ) {
        if (schemas.isEmpty()) {
            System.out.println("No schemas loaded.");
            return;
        }

        for(VignetteSchema schema : schemas){
            String translatedText;
            if(schema.getLeftText().isEmpty() || schema.getLeftText() == null){
                translatedText = translator.getTranslation(schema.getCombinedText());
            }
            else {
                translatedText = translator.getTranslation(schema.getLeftText());
            }
            this.vignettes.add(new Vignette(
                schema.getLeftText(),
                schema.getCombinedText(),
                translatedText,
                schema.getLeftPose(),
                schema.getRightPose(),
                schema.getBackground()
            ));
        }
    }
    /**
     * Retrieves a vignette by index.
     * @param index Index of the vignette.
     * @return Vignette object if available, else null.
     */
    public Vignette getVignette(int index) {
        if (index < 0 || index >= vignettes.size()) {
            return null;
        }
        return vignettes.get(index);
    }

    /**
     *
     * @return a scene containing two panels with only one speech bubble in each. "left" is for when there is one figure in the panel.
     */
    public Scene getLeftScene() throws IOException{
        XMLFile xml = new XMLFile("specification.xml");
        
        Random rand = new Random();
        Vignette vignette = new Vignette("", "", "", "", "", "");
        while(vignette.getLeftText().isEmpty() || vignette.getLeftText() == null){
            vignette = this.getVignette(rand.nextInt(49)); 
        }

        Scene scene = new Scene();
        Panel panel = new Panel();

        List<Figure> figures = xml.getFigures();

        Bubble leftBubble = new Bubble();
        
        leftBubble.setContent(vignette.getLeftText());
        
        leftBubble.setStatus("speech");

        panel.setSetting(vignette.getBackground());
        Figure figOne = figures.get(0);
            
        figOne.setPose(vignette.getLeftPose());
        
        Position left = new Position();
            
        left.setName("left");
            
        left.setFigure(figOne);

        left.setBubble(leftBubble);
        panel.addPosition(left);

        scene.addPanel(panel);

        // add a second identical panel except with the dialogue trnaslated then abstract these two methods
        // Commented out as the main function automatically translates everything for simplicity - Ryan
        // Panel panelTwo = new Panel();
        // Bubble leftBubbleTranslated = new Bubble();

        // leftBubbleTranslated.setContent(vignette.getTranslatedText());
        // panelTwo.setSetting(vignette.getBackground());
        // System.out.println(vignette.getTranslatedText());
        // Position leftTwo = new Position();

        // leftTwo.setName("left");
        // leftTwo.setFigure(figOne);
        // leftTwo.setBubble(leftBubbleTranslated);

        // panelTwo.addPosition(leftTwo);

        // scene.addPanel(panelTwo);
        
        return scene;
    }

  /**
     *
     * @return a scene containing two panels with only one speech bubble in each. "whole" is for when there is two figures in the panel.
     */
    public Scene getWholeScene() throws IOException{
        XMLFile xml = new XMLFile("specification.xml");
        
        Random rand = new Random();
        Vignette vignette = this.getVignette(rand.nextInt(39)); 

        Scene scene = new Scene();
        Panel panel = new Panel();

        List<Figure> figures = xml.getFigures();

        Bubble bubble = new Bubble();
        
        bubble.setContent(vignette.getCombinedText());

        bubble.setStatus("speech");

        panel.setSetting(vignette.getBackground());
        Figure figOne = figures.get(0);
        Figure figTwo = figures.get(1);
        figOne.setPose(vignette.getLeftPose());
        figTwo.setPose(vignette.getRightPose());
        Position left = new Position();
        
        Position right = new Position();
        left.setName("left");
        right.setName("right");
        left.setFigure(figOne);
        right.setFigure(figTwo);
        left.setBubble(bubble);
        panel.addPosition(left);
        panel.addPosition(right);

        scene.addPanel(panel);

        // add a second identical panel except with the dialogue trnaslated then abstract these two methods
        // Commented out as the main function automatically translates everything for simplicity - Ryan
        // Panel panelTwo = new Panel();
        // panelTwo.setSetting(vignette.getBackground());
        // Bubble bubbleTranslated = new Bubble();

        // bubbleTranslated.setContent(vignette.getTranslatedText());
        // Position leftTwo = new Position();
        // Position rightTwo = new Position();
        // rightTwo.setName("right");
        // rightTwo.setFigure(figTwo);

        // leftTwo.setName("left");
        // leftTwo.setFigure(figOne);
        // leftTwo.setBubble(bubbleTranslated);

        // panelTwo.addPosition(leftTwo);
        // panelTwo.addPosition(rightTwo);
        // scene.addPanel(panelTwo);
        return scene;
  }
}
