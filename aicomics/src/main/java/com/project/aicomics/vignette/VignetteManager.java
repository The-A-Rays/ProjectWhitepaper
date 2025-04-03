package com.project.aicomics.vignette;

import java.util.ArrayList;
import java.util.List;

import com.project.aicomics.Translations;

// This class should take a list of VignetteSchemas and turn it into a list of Vignettes
// I think this makes more sense for now, and having the file reading separate helps make the logic more understandable

public class VignetteManager {

    private List<Vignette> vignettes;

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
    public void createVignettes(List<VignetteSchema> schemas, Translations translator) {
        if (schemas.isEmpty()) {
            System.out.println("No schemas loaded.");
            return;
        }

        for(VignetteSchema schema : schemas){
            String translatedText;
            if(schema.getLeftText().isEmpty() || schema.getLeftText() == null){
                translatedText = translator.getTranslation(schema.getCombinedText(), "spanish");
            }
            else {
                translatedText = translator.getTranslation(schema.getLeftText(), "spanish");
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
}
