package com.project.aicomics.XML;
// package com.project.aicomics;

// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.List;

// import org.jdom2.Document;
// import org.jdom2.Element;
// import org.jdom2.output.Format;
// import org.jdom2.output.XMLOutputter;

// import com.project.aicomics.vignette.Vignette;



// /*
//  * Converts the new classes and vignettes into an XML file
//  * I have considered using his XML formatting as a lost cause. I do not like it and it's horribly inefficient
//  * So. For our formatting we will be using this method:
//  * <comic> is the root node, like his, and holds <rubric>, <figures>, and <panels>
//  * <rubric> holds information about the comic in general, like title
//  * <figures> holds information about the figures that is consistent across the comic (so not pose)\
//  * <panels> holds <panel> children, however many panels there are.
//  * <panel> holds all the information needed for that vignette:
//  *      - characters, pose, background, 
//  * Of note: His comics are long, very long, and he implies they are meant to have a "story"
//  * This story is incoherant to me, I can't see it at all and I dont think im alone in that.
//  * Also, these are meant to be webcomics, they should be kept short, that is why I'm going with the
//  * single comic in an xml doc version, notably I expect no more than 5 panels
//  */
// public class XMLManager {

//     /**
//      * A static method to create an XML document formatted as a comic
//      * @param fileName name of the XML file to be created in aicomics\\src\\main\\resources
//      * @param title The string for the title of the webcomic at the top
//      * @param figures A list containing all figures in the comic
//      * @param scenes A list of scenes in the comic.
//      */
//     public static void createComic(String fileName, String title, List<Figure> figuresList, List<Vignette> scenesList) {
//         if (figuresList.size() > 2 || figuresList.size() < 1) throw new IllegalArgumentException("figuresList must be 1 or 2 figures");
//         if (scenesList.size() < 1) throw new IllegalArgumentException("Must have at least 1 scene");
//         Document doc = new Document();

//         Element comic = new Element("comic");
//         doc.setRootElement(comic);

//         Element rubric = new Element("rubric");
//         Element comicTitle = new Element("title");
//         comicTitle.setText(title);
//         rubric.addContent(comicTitle);

//         Element figures = new Element("figures");
//         for (Figure fig : figuresList) {
//             Element f = new Element("figure");
//             if (fig.getAppearence()) {
//                 f.addContent(new Element("skin").setText("male"));
//                 f.addContent(new Element("beard").setText(fig.getBeardColor().toString()));
//             }else {
//                 f.addContent(new Element("skin").setText("female"));
//                 f.addContent(new Element("beard").setText("none"));
//             }
//             f.addContent(new Element("skin").setText(fig.getSkinColor().toString()));
//             f.addContent(new Element("hair").setText(fig.getHairColor().toString()));
//             f.addContent(new Element("lips").setText(fig.getLipColor().toString()));
//             f.addContent(new Element("hairLength").setText(Integer.toString(fig.getHairLength())));
//             figures.addContent(f);
//         }


//         Element panels = new Element("panels");
//         for (Vignette scene : scenesList) {
//             Element p = new Element("panel");
//             p.addContent(new Element("leftPose").setText(scene.getLeftPose()));
//             p.addContent(new Element("combinedText").setText(scene.getCombinedText()));
//             p.addContent(new Element("translatedText").setText(scene.getTranslatedText()));
//             p.addContent(new Element("leftText").setText(scene.getLeftText()));
//             p.addContent(new Element("rightPose").setText(scene.getRightPose()));
//             p.addContent(new Element("background").setText(scene.getBackground()));
//             panels.addContent(p);
//         }

//         comic.addContent(rubric);
//         comic.addContent(figures);
//         comic.addContent(panels);
        
//         // Writes doc out into the resources file (so it cant be read by a user)
//         XMLOutputter out = new XMLOutputter();
//         out.setFormat(Format.getPrettyFormat());
//         try {
//             out.output(doc, new FileWriter(new File("aicomics\\src\\main\\resources\\".concat(fileName))));
//         } catch (IOException e) {e.getStackTrace();}
//     }
    
    
// }
