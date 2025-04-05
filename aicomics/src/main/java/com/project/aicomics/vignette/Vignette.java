package com.project.aicomics.vignette;

/**
 * Represents a single vignette (scene) with text, poses, and background.
 * 
 * A vignette may have:
 * - Either `leftText` (individual speech) or `combinedText` (shared speech).
 * - A translated text version (`translatedText`).
 * - Poses for left and right characters.
 * - A background image reference.
 */

public class Vignette {

    private final String leftText, combinedText, translatedText, leftPose, rightPose, background;

    /**
     * Creates a Vignette object with specified text, translations, poses, and background.
     *
     * @param leftText Text spoken by the left character (if applicable).
     * @param combinedText Shared speech between both characters (if applicable).
     * @param translatedText Translated version of the speech text.
     * @param leftPose Pose of the left character.
     * @param rightPose Pose of the right character.
     * @param background Background image reference.
     */
    public Vignette(String leftText, String combinedText, String translatedText, String leftPose, String rightPose, String background){
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.translatedText = translatedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.background = background;
    }

    /**
     * @return Text spoken by the left character (if applicable).
     */
    public String getLeftText() {
        return leftText;
    }

    /**
     * @return Shared text when both characters speak together.
     */
    public String getCombinedText() {
        return combinedText;
    }

    /**
     * @return Translated version of the spoken text.
     */
    public String getTranslatedText() {
        return translatedText;
    }

    /**
     * @return Pose of the left character.
     */
    public String getLeftPose() {
        return leftPose;
    }

    /**
     * @return Pose of the right character.
     */
    public String getRightPose() {
        return rightPose;
    }

    /**
     * @return Background image reference for the vignette.
     */
    public String getBackground() {
        return background;
    }

}
