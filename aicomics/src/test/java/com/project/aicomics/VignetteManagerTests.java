package com.project.aicomics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.aicomics.vignette.VignetteManager;
import com.project.aicomics.vignette.VignetteSchema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VignetteManagerTests {

    private VignetteManager manager;
    List<String> list = Arrays.asList("catering", "to serve, a tray, a cocktail", "", "ordering a drink", "eating, drinking, sipping", "restaurant, food truck");

    private VignetteSchema schema = new VignetteSchema(list);

    @Test
    void testSchemaIsPopulatedCorrectly() {
        assertFalse(schema.getLeftPose().isEmpty(), "Left Pose should not be empty");
        assertFalse(schema.getCombinedText().isEmpty(), "Combined Text should not be empty");
        assertTrue(schema.getLeftText().isEmpty(), "Left Text should be empty");
        assertFalse(schema.getRightPose().isEmpty(), "Right Pose should not be empty");
        assertFalse(schema.getBackground().isEmpty(), "Background should not be empty");
    }

    // @Test
    // void testHandlesEmptyTSVGracefully() {
    //     VignetteManager emptyManager = new VignetteManager(); // Load an empty TSV
    //     VignetteSchema emptySchema = emptyManager.schema;

    //     assertTrue(emptySchema.getLeftPose().isEmpty(), "Expected empty leftPose list");
    //     assertTrue(emptySchema.getBackground().isEmpty(), "Expected empty background list");
    // }
}
