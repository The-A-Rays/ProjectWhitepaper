package com.project.aicomics;

import java.util.ArrayList;
import java.util.Random;

public class VignetteSchema {

    private ArrayList<String> leftPose = new ArrayList<String>();
    private ArrayList<String> combinedText = new ArrayList<String>();
    private ArrayList<String> leftText = new ArrayList<String>();
    private ArrayList<String> rightPose = new ArrayList<String>();
    private ArrayList<String> background = new ArrayList<String>(); 

    public VignetteSchema(){} //initialises with empy arraylists

    // public VignetteSchema(ArrayList<String> leftPose, ArrayList<String> combinedText, ArrayList<String> leftText, ArrayList<String> rightPose, ArrayList<String> background){
    //     this.leftPose = leftPose;
    //     this.combinedText = combinedText;
    //     this.leftText = leftText;
    //     this.rightPose = rightPose;
    //     this.background = background;
    // }

    Random rand = new Random();

    // public void add(ArrayList<String> list, String string){
    //     list.add(string);
    // }
    public void add(String type, String value) {
        switch(type) {
            case "leftPose":
                leftPose.add(value);
                break;
            case "combinedText":
                combinedText.add(value);
                break;
            case "leftText":
                leftText.add(value);
                break;
            case "rightPose":
                rightPose.add(value);
                break;
            case "background":
                background.add(value);
                break;
            default:
                System.out.println("Unknown type: " + type);
                break;
        }
    }
    
    public String getVignetteField(ArrayList<String> list){
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
