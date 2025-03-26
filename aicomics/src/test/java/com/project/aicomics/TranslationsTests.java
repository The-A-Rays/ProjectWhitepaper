package com.project.aicomics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static com.project.aicomics.Translations.Language.english;
import static com.project.aicomics.Translations.Language.spanish;

public class TranslationsTests {
    
    private Translations translations = new Translations(english, spanish);
    private static final String testFile = "english-spanish.tsv";

    //test to ensure translated pairs are done and stored properly
    @Test
    void testTranslation() throws IOException {
      //parameters for vignette object
      ArrayList<String> leftPose = new ArrayList<>(List.of("catering"));
      ArrayList<String> combinedText = new ArrayList<>(List.of("to serve, a tray, a cocktail, ordering a drink"));
      ArrayList<String> leftText = new ArrayList<>(List.of("a tray"));
      ArrayList<String> rightPose = new ArrayList<>(List.of("eating, drinking, sipping, slurping"));
      ArrayList<String> background = new ArrayList<>(List.of("restaurant, food truck"));
        
      VignetteSchema input = new VignetteSchema(leftPose, combinedText, leftText, rightPose, background);
    
      String translated = translations.getTranslation(input.getLeftText(), "spanish");

      File file = new File(testFile);
      assertTrue(file.exists(), "The translation file should exist by now from the getTranslation() method.");

      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          assertEquals(input.getLeftText(), reader.readLine(), "a tray");
          assertEquals(translated, reader.readLine(), "una bandeja");
      }
  }

  @Test
  void testTranslation2() throws IOException {
    //parameters for vignette object
    ArrayList<String> leftPose = new ArrayList<>(List.of("catering"));
    ArrayList<String> combinedText = new ArrayList<>(List.of("to serve, a tray, a cocktail, ordering a drink"));
    ArrayList<String> leftText = new ArrayList<>(List.of("a politician"));
    ArrayList<String> rightPose = new ArrayList<>(List.of("eating, drinking, sipping, slurping"));
    ArrayList<String> background = new ArrayList<>(List.of("restaurant, food truck"));
      
    VignetteSchema input = new VignetteSchema(leftPose, combinedText, leftText, rightPose, background);
  
    String translated = translations.getTranslation(input.getLeftText(), "spanish");

    File file = new File(testFile);
    assertTrue(file.exists(), "The translation file should exist exist by now from the getTranslation() method.");

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        reader.readLine(); //skip tray line
        reader.readLine(); //skip translation of tray
        assertEquals(input.getLeftText(), reader.readLine(), "a politician");
        assertEquals(translated, reader.readLine(), "un político");
    }
}

    
}

  

