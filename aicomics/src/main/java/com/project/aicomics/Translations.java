package com.project.aicomics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.project.aicomics.service.OpenAIService;

public class Translations {

    public enum Language {spanish, english};
    private OpenAIService ai = new OpenAIService();
    private String translatedFile;

    public Translations(Language originalLanguage, Language targetLanguage) {
        this.translatedFile = originalLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase() + ".tsv";
    }

    //TODO 
    /**
     * Gets the translation of a given vignette
     * Check language pair if it has the translation already
     * If pair not found, prompt to get it, then write to file
          * @throws IOException 
          */
    public String getTranslation(String textToBeTranslated, String targetLanguage) {
        String translatedText = ai.TranslateText(textToBeTranslated);
        // translatedText = Parsing.JSONParser(translatedText); JSON parsing is now built into OpenAIService Class
        File file = new File(translatedFile);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            //check if the translation already exists
            boolean existing = false;
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String existingText = sc.nextLine();
                if (sc.hasNextLine()) { //transaltion for existing text (if any)
                    String existingTranslation = sc.nextLine();
                    if (existingText.equals(textToBeTranslated) && existingTranslation.equals(translatedText)) {
                        existing = true;
                        return existingTranslation; 
                    }      
                }
            }
            //write to file if it doesnt exist already
            if (!existing) {
                try (FileWriter wr = new FileWriter(file, true)) {
                    wr.write(textToBeTranslated);
                    wr.write(System.lineSeparator());
                    wr.write(translatedText);
                    wr.write(System.lineSeparator());
                }
            }
            sc.close();
        } catch (IOException e) {System.out.println("Error creating file for translation storage, please contact devs: " + e);}
        
    return translatedText;
    }
}
