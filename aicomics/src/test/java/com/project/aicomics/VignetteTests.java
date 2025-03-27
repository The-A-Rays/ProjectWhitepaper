package com.project.aicomics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class VignetteTests {

    // private ArrayList<String> leftPose = new ArrayList<String>();
    // private ArrayList<String> combinedText = new ArrayList<String>();
    // private ArrayList<String> leftText = new ArrayList<String>();
    // private ArrayList<String> rightPose = new ArrayList<String>();
    // private ArrayList<String> background = new ArrayList<String>();

    VignetteSchema vignette = new VignetteSchema();

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
        vignette.add("background", "theatre");
        vignette.add("combinedText", "to act");
        vignette.add("leftPose", "acting");
        vignette.add("leftText", "to act");
        vignette.add("rightPose", "watching");
        assertEquals("theatre", vignette.getBackground(), "background should be 'theatre'");
        assertEquals("to act", vignette.getCombinedText(), "background should be 'to act'");
        assertEquals("acting", vignette.getLeftPose(), "background should be 'acting'");
        assertEquals("to act", vignette.getLeftText(), "background should be 'to act'");
        assertEquals("watching", vignette.getRightPose(), "background should be 'watching'");
    }

    @Test
    void testGetBackground() {
        vignette.add("background", "film set");
        String result = vignette.getBackground();
        assertTrue("film set" == result || "theatre" == result);
    }

    @Test
    void testGetCombinedText() {
        vignette.add("combinedText", "to perform");
        String result = vignette.getCombinedText();
        assertTrue("to act" == result || "to perform" == result);
    }

    @Test
    void testGetLeftPose() {
        vignette.add("leftPose", "performing");
        String result = vignette.getLeftPose();
        assertTrue("acting" == result || "performing" == result);    
    }

    @Test
    void testGetLeftText() {
        vignette.add("leftText", "to perform");
        String result = vignette.getLeftText();
        assertTrue("to act" == result || "to perform" == result);
    }

    @Test
    void testGetRightPose() {
        vignette.add("rightPose", "clapping");
        String result = vignette.getRightPose();
        assertTrue("clapping" == result || "watching" == result);
    }
}
