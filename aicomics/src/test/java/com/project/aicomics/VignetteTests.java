package com.project.aicomics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VignetteTests {

    Vignette vignette = new Vignette("jumping", "to jump, to fall", "to jump", "falling", "trampoline");

    @Test
    void testGetBackground() {
        assertEquals("trampoline", vignette.getBackground(), "background should be 'trampoline'");
    }

    @Test
    void testGetCombinedText() {
        assertEquals("to jump, to fall", vignette.getCombinedText(), "combined text should be 'to jump, to fall'");
    }

    @Test
    void testGetLeftPose() {
        assertEquals("jumping", vignette.getLeftPose(), "combined text should be 'jumping'");
    }

    @Test
    void testGetLeftText() {
        assertEquals("to jump", vignette.getLeftText(), "combined text should be 'to jump'");
    }

    @Test
    void testGetRightPose() {
        assertEquals("falling", vignette.getRightPose(), "combined text should be 'falling'");
    }
}
