package com.project.aicomics;

public class Vignette {

    private String leftPose, combinedText, leftText, rightPose, background;


    public Vignette(String leftPose, String combinedText, String leftText, String rightPose, String background){
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.background = background;
    }

    public String getBackground() {
        return this.background;
    }
    public String getCombinedText() {
        return combinedText;
    }
    public String getLeftPose() {
        return leftPose;
    }
    public String getLeftText() {
        return leftText;
    }
    public String getRightPose() {
        return rightPose;
    }
}
