package com.project.aicomics;

/*  TODO
 * Converts the new classes and vignettes into an XML file
 * I have considered using his XML formatting as a lost cause. I do not like it and it's horribly inefficient
 * So. For our formatting we will be using this method:
 * <comic> is the root node, like his, and holds <figures> and <panels>
 * <figures> holds information about the figures that is consistent across the comic (so not pose)\
 * <panels> holds <panel> children, however many panels there are.
 * <panel> holds all the information needed for that vignette:
 *      - characters, pose, background, 
 * Of note: His comics are long, very long, and he implies they are meant to have a "story"
 * This story is incoherant to me, I can't see it at all and I dont think im alone in that.
 * Also, these are meant to be webcomics, they should be kept short, that is why I'm going with the
 * single comic in an xml doc version, notably I expect no more than 5 panels
 */
public class XMLManager {
    
}
