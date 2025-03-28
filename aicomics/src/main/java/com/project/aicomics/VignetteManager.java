package com.project.aicomics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;

// This class should read in a schema from the file, and create a single vignette object including the translated text

public class VignetteManager {

    private ArrayList<VignetteSchema> schemas = new ArrayList<>();
    private Translations translator;

    public VignetteManager(Translations translator) {
        this.translator = translator;
        loadTSV("English.tsv");
    }

    private void loadTSV(String fileName) {
        try (InputStream inputStream = VignetteManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null && i < 1) {
                    String[] fields = line.split("\t");
                    if (fields.length >= 5) {
                        VignetteSchema schema = new VignetteSchema();
                        schema.add("leftPose", fields[0]);
                        schema.add("combinedText", fields[1]);
                        schema.add("leftText", fields[2]);
                        schema.add("rightPose", fields[3]);
                        schema.add("background", fields[4]);
                        schemas.add(schema);
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vignette generateVignette() {
        if (schemas.isEmpty()) {
            System.out.println("No schemas loaded.");
            return null;
        }

        // For now just pick the first schema (later you can pick random if you want)
        VignetteSchema schema = schemas.get(0);

        // Apply translations
        
        String translatedText = "";
        if(schema.getLeftText().isEmpty() || schema.getLeftText() == null){
            try {
                translatedText = translator.getTranslation(schema.getCombinedText(), "spanish");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                translatedText = translator.getTranslation(schema.getLeftText(), "spanish");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
