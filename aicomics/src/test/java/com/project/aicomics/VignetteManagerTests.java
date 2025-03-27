package com.project.aicomics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VignetteManagerTests {

    private VignetteManager manager;
    private VignetteSchema schema;

    @BeforeEach
    void setUp() {
        manager = new VignetteManager();
        schema = manager.schema;
    }

    @Test
    void testSchemaIsPopulatedCorrectly() {
        assertFalse(schema.getLeftPose().isEmpty(), "Left Pose should not be empty");
        assertFalse(schema.getCombinedText().isEmpty(), "Combined Text should not be empty");
        assertTrue(schema.getLeftText().isEmpty(), "Left Text should be empty");
        assertFalse(schema.getRightPose().isEmpty(), "Right Pose should not be empty");
        assertFalse(schema.getBackground().isEmpty(), "Background should not be empty");
    }

    // @Test
    // void testCommaSeparatedValuesAreSplitCorrectly() {
    //     List<String> leftPoses = schema.getLeftPose();
    //     List<String> combinedText = schema.getCombinedText();
    //     List<String> rightPoses = schema.getRightPose();
    //     List<String> backgrounds = schema.getBackground();

    //     // Check leftPose
    //     assertEquals(1, leftPoses.size());
    //     assertTrue(leftPoses.contains("attracted"));

    //     // Check combinedText
    //     assertEquals(2, combinedText.size());
    //     assertTrue(combinedText.contains("to fall in love"));
    //     assertTrue(combinedText.contains("love"));

    //     // Check rightPose
    //     assertEquals(2, rightPoses.size());
    //     assertTrue(rightPoses.contains("nude"));
    //     assertTrue(rightPoses.contains("posing"));

    //     // Check background
    //     assertEquals(3, backgrounds.size());
    //     assertTrue(backgrounds.contains("bedroom"));
    //     assertTrue(backgrounds.contains("red carpet event"));
    //     assertTrue(backgrounds.contains("locker room"));
    // }

    // @Test
    // void testHandlesEmptyTSVGracefully() {
    //     VignetteManager emptyManager = new VignetteManager(); // Load an empty TSV
    //     VignetteSchema emptySchema = emptyManager.schema;

    //     assertTrue(emptySchema.getLeftPose().isEmpty(), "Expected empty leftPose list");
    //     assertTrue(emptySchema.getBackground().isEmpty(), "Expected empty background list");
    // }
}
