package com.project.aicomics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



/*
 * Converts the new classes and vignettes into an XML file
 * I have considered using his XML formatting as a lost cause. I do not like it and it's horribly inefficient
 * So. For our formatting we will be using this method:
 * <comic> is the root node, like his, and holds <rubric>, <figures>, and <panels>
 * <rubric> holds information about the comic in general, like title
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

    /**
     * A static method to create an XML document formatted as a comic
     * @param fileName name of the XML file to be created in aicomics\\src\\main\\resources
     * @param title The string for the title of the webcomic at the top
     * @param figures A list containing all figures in the comic
     * @param scenes A list of scenes in the comic.
     */
    public static void createComic(String fileName, String title, List<Figure> figuresList, List<Vignette> scenesList) {
        if (figuresList.size() > 2 || figuresList.size() < 1) throw new IllegalArgumentException("figuresList must be 1 or 2 figures");
        if (scenesList.size() < 1) throw new IllegalArgumentException("Must have at least 1 scene");
        XmlMapper xmlmap = new XmlMapper();
        Document doc = new Document();

        Element comic = new Element("comic");
        doc.setRootElement(comic);

        Element rubric = new Element("rubric");
        Element comicTitle = new Element("title");
        comicTitle.setText(title);
        rubric.addContent(comicTitle);

        Element figures = new Element("figures");
        try {
            for (Figure fig : figuresList) {
                String xml = xmlmap.writeValueAsString(fig);
                figures.setText(xml);
            }
        } catch (JsonProcessingException e) {e.getStackTrace();}


        Element panels = new Element("panels");
        try {
            for (Vignette scene : scenesList) {
                String xml = xmlmap.writeValueAsString(scene);
                panels.addContent(xml);
            }
        } catch (JsonProcessingException e) {e.getStackTrace();}

        comic.addContent(rubric);
        comic.addContent(figures);
        comic.addContent(panels);
        
        // Writes doc out into the resources file (so it cant be read by a user)
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        try {
            out.output(doc, new FileWriter(new File("aicomics\\src\\main\\resources\\".concat(fileName))));
        } catch (IOException e) {e.getStackTrace();}
    }
    
    
}
