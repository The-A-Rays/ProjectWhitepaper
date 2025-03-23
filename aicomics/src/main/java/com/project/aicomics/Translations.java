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
         public String getTranslation(Vignette input, String targetLanguage) throws IOException {
        String textToBeTranslated = input.getCombinedText() + input.getLeftText();
        String translatedString = ai.TranslateText(textToBeTranslated);

        File file = new File(translatedFile);

        //add string to newly created file
        if (file.createNewFile()){
            FileWriter wr = new FileWriter(file);
            wr.write(textToBeTranslated);
            wr.write(System.lineSeparator());
            wr.write(translatedString);
            wr.write(System.lineSeparator());
        } else { //if file already exists, check weather it already has the translation
            boolean exists = false;
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                if (sc.nextLine().equals(translatedString)){
                    exists = true;
                    break;
                }
            } //if not, add it
            if (!exists){
                //appends
                FileWriter wr = new FileWriter(file, true);
                wr.write(textToBeTranslated);
                wr.write(System.lineSeparator());
                wr.write(translatedString);
                wr.write(System.lineSeparator());
            }
        }
        return null;
    }
}
