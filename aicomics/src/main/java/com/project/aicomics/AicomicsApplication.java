package com.project.aicomics;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.Figure;
import com.project.aicomics.XML.Scene;
import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.XML.XMLGenerator;
import com.project.aicomics.controller.DevController;
import com.project.aicomics.vignette.VignetteFileReader;
import com.project.aicomics.vignette.VignetteManager;

@RestController
@SpringBootApplication
public class AicomicsApplication {

    private final DevController devController;

    AicomicsApplication(DevController devController) {
        this.devController = devController;
    }

	// Main method, this is the method that linkes it all together and is able to interpret the instructions in config
	public static void main(String[] args) throws IOException {
		// Base objects needed for program running
		SpringApplication.run(AicomicsApplication.class, args);
		ConfigurationFile config = ConfigurationFile.getInstance(); 

		// Objects for main functionality
		XMLFile story = new XMLFile("specification_10Scenes.xml");
		XMLFile conjugation = new XMLFile("specification.xml");
		XMLGenerator finalXML;
		Translations translator = new Translations(Language.english, config.getLanguage());
		VignetteManager vocab = new VignetteManager(VignetteFileReader.readSchemas("English.tsv", 50), translator);

		// XML Objects
		Document doc;
		try {doc = XMLFile.createDocument();}
		catch (ParserConfigurationException e) {
			DevController.error("Fatal error creating document builder", e);
            return;
		}
		Element comic = doc.createElement("comic");
		doc.appendChild(comic);
		Element figures = doc.createElement("figures");
		comic.appendChild(figures);
		Element scenes = doc.createElement("scenes");
		comic.appendChild(scenes);

		// Functionality
		for (String instruction : config.getLessonSchema()) {
			switch(instruction) {
				case "story" -> {
							Scene s = story.getRandScene();
							XMLGenerator.generateDialogue(s);
							scenes.appendChild(XMLFile.convertScene(doc, s));
                        }
				case "conjugation" -> {
							scenes.appendChild(XMLFile.convertScene(doc, conjugation.getRandScene()));
                        }
				case "left" -> {
							scenes.appendChild(XMLFile.convertScene(doc, vocab.getLeftScene()));
                        }
				case "whole" -> {
							scenes.appendChild(XMLFile.convertScene(doc, vocab.getWholeScene()));
                        }
				default -> {
							System.out.println("Incorrect command, please check config file");
							throw new IllegalArgumentException("Command not understood");
                        }
			}
		}

		// Get figures from the elements already added and add them to the xml doc
		ArrayList<Figure> figureList = new ArrayList<>();
		NodeList newFigures = doc.getElementsByTagName("figure");
		for (int i = 0; i < newFigures.getLength(); i++) {
			Node node = newFigures.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Figure figure = new Figure((Element) node);
				if (!figureList.contains(figure)) {
					figureList.add(figure);
				}
			}
		}

		for (Figure f : figureList) {
			XMLGenerator.addFigure(f, figures, doc, true);
		}

		try {XMLFile.writeXML(doc, "aicomics/src/main/resources/temp.xml");}
		catch (TransformerException e) {
			System.out.println("Unable to print document: " + e);
		}
		// There is a race condition happening between writeXML writing the file and XMLGenerator<init>
		// reading the file. I have tried other solutions but am now attempting to just make XMLGenerator wait its turn manually.
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {System.out.println("Failure making program wait");}
		finalXML = new XMLGenerator("temp.xml", config.getLanguage());
		finalXML.Print(config.getLanguage(), "final.xml");
	}
}