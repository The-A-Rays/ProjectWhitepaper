package com.project.aicomics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.aicomics.vignette.Vignette;
import com.project.aicomics.vignette.VignetteFileReader;
import com.project.aicomics.vignette.VignetteManager;
import com.project.aicomics.vignette.VignetteSchema;
import com.project.aicomics.Translations.Language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VignetteManagerTests {

    private VignetteManager manager;
    List<String> list = Arrays.asList("catering", "to serve, a tray, a cocktail", "", "ordering a drink", "eating, drinking, sipping", "restaurant, food truck");

    private VignetteSchema schema = new VignetteSchema(list);

    @BeforeEach
    void setUp() throws IOException{
        // Create a mock translator
        Translations translator = new Translations(Language.english, Language.spanish); // Assume this is properly implemented
        List<VignetteSchema> schemas = VignetteFileReader.readSchemas("English.tsv", 3);
        manager = new VignetteManager(schemas, translator);
    }

    @Test
    void testSchemaIsPopulatedCorrectly() {
        assertFalse(schema.getLeftPose().isEmpty(), "Left Pose should not be empty");
        assertFalse(schema.getCombinedText().isEmpty(), "Combined Text should not be empty");
        assertTrue(schema.getLeftText().isEmpty(), "Left Text should be empty");
        assertFalse(schema.getRightPose().isEmpty(), "Right Pose should not be empty");
        assertFalse(schema.getBackground().isEmpty(), "Background should not be empty");
    }

    @Test
    void testVignetteManagerCreation() {

        // Check that the first vignette's attributes are correctly assigned
        Vignette vignette = manager.getVignette(0);
        assertNotNull(vignette, "Vignette should not be null");
        assertFalse(vignette.getTranslatedText().isEmpty(), "Translated Text should not be empty");
    }

    @Test
    void testVignetteTranslation() {
        Vignette vignette = manager.getVignette(2);
        // Simulate translation check (you can mock or assert a known translation)
        assertEquals("una bandeja", vignette.getTranslatedText(), "Translation should match expected");
    }

    @Test
    void testEmptySchemas() {
        List<VignetteSchema> emptySchemas = new ArrayList<>();
        VignetteManager emptyManager = new VignetteManager(emptySchemas, new Translations(Language.english, Language.spanish));

        assertNull(emptyManager.getVignette(0), "Vignette should be null when no schemas are available");
    }

}

