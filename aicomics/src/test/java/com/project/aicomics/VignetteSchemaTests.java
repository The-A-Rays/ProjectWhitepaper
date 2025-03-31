package com.project.aicomics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.project.aicomics.vignette.VignetteSchema;

public class VignetteSchemaTests {

    VignetteSchema vignette = new VignetteSchema(null);
    List<String> list = Arrays.asList("catering", "to serve", "ordering a drink", "eating", "restaurant");
    List<String> listTwo = Arrays.asList("catering", "to serve, a tray, a cocktail", "ordering a drink", "eating, drinking, sipping", "restaurant, food truck");




    @Test
    void testEmpty() {
        assertEquals(null, vignette.getBackground(), "should return null");
        assertEquals(null, vignette.getCombinedText(), "should return null");
        assertEquals(null, vignette.getLeftPose(), "should return null");
        assertEquals(null, vignette.getLeftText(), "should return null");
        assertEquals(null, vignette.getRightPose(), "should return null");
    }

    @Test
    void testOneStringLists() {
        vignette = new VignetteSchema(list);

        assertEquals("catering", vignette.getLeftPose(), "leftPose should be 'catering'");
        assertEquals("to serve", vignette.getCombinedText(), "combinedText should be 'to serve'");
        assertEquals("ordering a drink", vignette.getLeftText(), "LeftText should be 'ordering a drink'");
        assertEquals("eating", vignette.getRightPose(), "rightPose should be 'eating'");
        assertEquals("restaurant", vignette.getBackground(), "background should be 'restaurant'");
    }

    @Test
    void testMultipleStringLists() {
        vignette = new VignetteSchema(listTwo);
        String leftPoses = vignette.getLeftPose();
        String combinedText = vignette.getCombinedText();
        String leftText = vignette.getLeftText();
        String rightPoses = vignette.getRightPose();
        String backgrounds = vignette.getBackground();

        assertTrue(leftPoses.contains("catering"));
        assertTrue(combinedText.contains("to serve") || combinedText.contains("a tray") || combinedText.contains("a cocktail"));
        assertTrue(leftText.contains("ordering a"));
        assertTrue(rightPoses.contains("eating") || rightPoses.contains("drinking") || rightPoses.contains("sipping"));
        assertTrue(backgrounds.contains("restaurant") || backgrounds.contains("food truck"));


        
        // assertTrue("film set" == result || "theatre" == result);
    }

   
}
