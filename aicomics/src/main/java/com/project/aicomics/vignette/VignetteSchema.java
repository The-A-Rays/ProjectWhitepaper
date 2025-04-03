package com.project.aicomics.vignette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents a set of vignette options parsed from a single line of the English.tsv file.
 * 
 * Each vignette consists of multiple possible values for:
 * - Left character pose
 * - Combined text (if both characters speak together)
 * - Left character text (if speaking separately)
 * - Right character pose
 * - Background image
 * 
 * The class supports random selection of values when multiple options are available.
 */

public class VignetteSchema {

    private List<String> leftPose = new ArrayList<>();
    private List<String> combinedText = new ArrayList<>();
    private List<String> leftText = new ArrayList<>();
    private List<String> rightPose = new ArrayList<>();
    private List<String> background = new ArrayList<>(); 
    Random rand = new Random();

    public VignetteSchema(){} //initialises with empy arraylists

    /**
     * Initializes a VignetteSchema with fields split from a CSV-like format.
     * 
     * @param fields List of strings, where each element represents a comma-separated set of options 
     *               for one of the five vignette fields (leftPose, combinedText, leftText, rightPose, background).
     *               Must contain exactly 5 elements.
     */

    public VignetteSchema(List<String> fields){
        if(!(fields == null)){
            leftPose = Arrays.asList(fields.get(0).split(","));
            combinedText = Arrays.asList(fields.get(1).split(","));
            leftText = Arrays.asList(fields.get(2).split(","));
            rightPose = Arrays.asList(fields.get(3).split(","));
            background = Arrays.asList(fields.get(4).split(","));
        }
    }

    /**
     * Returns a random value from a list of vignette options.
     * 
     * @param list List of possible options for a vignette attribute.
     * @return A randomly selected option from the list, or `null` if the list is empty.
     */
    public String getVignetteField(List<String> list){
        if(list == null || list.isEmpty()) {
            return null;
        }

        if(list.size() == 1) {
            return list.get(0);
        }
        else {
            return list.get(rand.nextInt(list.size()));
        }
    }

/**
 * @return A random background option from the available choices.
 */
public String getBackground() {
    return getVignetteField(background);
}

/**
 * @return A random combined text option (when both characters speak together).
 */
public String getCombinedText() {
    return getVignetteField(combinedText);
}

/**
 * @return A random pose for the left character.
 */
public String getLeftPose() {
    return getVignetteField(leftPose);
}

/**
 * @return A random text spoken by the left character.
 */
public String getLeftText() {
    return getVignetteField(leftText);
}

/**
 * @return A random pose for the right character.
 */
public String getRightPose() {
    return getVignetteField(rightPose);
}

}
