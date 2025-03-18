package com.project.aicomics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.project.aicomics.service.OpenAIService;

public class Translations {

    private enum languages {Spanish, English};
    private OpenAIService ai = new OpenAIService();
    private FileOutputStream vignettes;

    public Translations() {
        try {vignettes = new FileOutputStream("English.tsv");
        } catch (FileNotFoundException e) {}
    }

    //TODO 
    /**
     * Gets the translation of a given vignette
     * Check language pair if it has the translation already
     * If pair not found prompt to get it, then write to file
     */
    public getTranslation() {

    }


}
