package com.project.aicomics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// This class represents what the vignette options are, that we read in from a single line of the English.tsv file (or user input in the future)

public class VignetteSchema {

    private List<String> leftPose = new ArrayList<>();
    private List<String> combinedText = new ArrayList<>();
    private List<String> leftText = new ArrayList<>();
    private List<String> rightPose = new ArrayList<>();
    private List<String> background = new ArrayList<>(); 
    Random rand = new Random();

    public VignetteSchema(){} //initialises with empy arraylists

    /**
     * Initializes vignette with Lists of the field
     * @param fields must be csv format !!! Can only handle fields with all 5 options !!! 
     * 
     */
    public VignetteSchema(List<String> fields){
        leftPose = Arrays.asList(fields.get(0).split(","));
        combinedText = Arrays.asList(fields.get(1).split(","));
        leftText = Arrays.asList(fields.get(2).split(","));
        rightPose = Arrays.asList(fields.get(3).split(","));
        background = Arrays.asList(fields.get(0).split(","));
    }

    /**
     * Old method to import strings into the lists, didn;t work as inteded so constructor is used now.
     * @param type 
     * @param value
     */
    // public void add(String type, String value) {
    //     switch(type) {
    //         case "leftPose":
    //             leftPose.add(value);
    //             break;
    //         case "c21111 ombinedText":
    //             combinedText.add(value);
    //             break;
    //         case "leftText":
    //             leftText.add(value);
    //             break;
    //         case "rightPose":
    //             rightPose.add(value);
    //             break;
    //         case "background":
    //             background.add(value);
    //             break;
    //         default:
    //             System.out.println("Unknown type: " + type);
    //             break;
    //     }
    // }
    
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

    public String getBackground() {
        return getVignetteField(background);
    }
    public String getCombinedText() {
        return getVignetteField(combinedText);
    }
    public String getLeftPose() {
        return getVignetteField(leftPose);
    }
    public String getLeftText() {
        return getVignetteField(leftText);
    }
    public String getRightPose() {
        return getVignetteField(rightPose);
    }
}
