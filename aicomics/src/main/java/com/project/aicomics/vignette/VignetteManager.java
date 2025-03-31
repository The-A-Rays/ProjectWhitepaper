package com.project.aicomics.vignette;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.project.aicomics.Translations;

// This class should read in a schema from the file, and create a single vignette object including the translated text

public class VignetteManager {

    private static VignetteManager INSTANCE;
    private ArrayList<VignetteSchema> schemas = new ArrayList<>();

    private VignetteManager() {
    }

    public static synchronized VignetteManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VignetteManager();
            INSTANCE.loadTSV();
        }
        return INSTANCE;
    }

    /**
     * Creates a vignette schema from the english.tsv file.
     * !!! Currently only reads in three lines, will need to be changed later when how we choose which vignette is made clear !!!
     */
    private void loadTSV() {
        String fileName = "English.tsv";
        try (InputStream inputStream = VignetteManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null && i < 3) {
                    String[] fields = line.split("\t");
                    if (fields.length == 5) {
                        VignetteSchema schema = new VignetteSchema(Arrays.asList(fields));
                        schemas.add(schema);
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a vignette based on the loaded schemas
     * @param translator A Translations Object to translate the text into the selected language
     * @param vignetteNumber int for which vignette to pick from the tsv
     * @return A fully selected vignette
     */
    public Vignette generateVignette(Translations translator, int vignetteNumber) {
        if (schemas.isEmpty()) {
            System.out.println("No schemas loaded.");
            return null;
        }

        VignetteSchema schema = schemas.get(vignetteNumber);

        // Apply translations
        
        String translatedText;
        if(schema.getLeftText().isEmpty() || schema.getLeftText() == null){
            translatedText = translator.getTranslation(schema.getCombinedText(), "spanish");
        }
        else {
            translatedText = translator.getTranslation(schema.getLeftText(), "spanish");
        }

        return new Vignette(
            schema.getLeftText(),
            schema.getCombinedText(),
            translatedText,
            schema.getLeftPose(),
            schema.getRightPose(),
            schema.getBackground()
        );
    }
}
