package com.project.aicomics.vignette;

// import java.util.ArrayList;

// this class represents a singular vignette object
// There should either be leftText or combined text based on whether there is one or two posed characters in the vignette
// I've included a translated text field too.
public class Vignette {

    private final String leftText, combinedText, translatedText, leftPose, rightPose, background;

    public Vignette(String leftText, String combinedText, String translatedText, String leftPose, String rightPose, String background){
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.translatedText = translatedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.background = background;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getCombinedText() {
        return combinedText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getLeftPose() {
        return leftPose;
    }

    public String getRightPose() {
        return rightPose;
    }

    public String getBackground() {
        return background;
    }
}
