package com.project.aicomics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.print.DocFlavor.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.XML.XMLGenerator;
import com.project.aicomics.controller.DevController;
import com.project.aicomics.service.OpenAIService;

@RestController
@SpringBootApplication
public class AicomicsApplication {

    private final DevController devController;

    AicomicsApplication(DevController devController) {
        this.devController = devController;
    }

	public static void main(String[] args) throws IOException {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);   

    XMLFile xml = new XMLFile("specification_10Scenes.xml");
		Audio audio = new Audio(xml);
		audio.generateAudioXML(Language.romanian);

		ai.generateAudioFile("Hola, ¿cómo estás?", "dialogue1.mp3");


		// XMLGenerator generate = new XMLGenerator("specification_10Scenes.xml");
		// DevController.status("Number of bubbles: " + generate.getAllText().size());
		// DevController.setLongStatus(generate.getAllText().toString());
		// List<String> dialogue = ai.GenerateDialogue(generate);
		// DevController.status("Generated dialogue size: " + dialogue.size());
		// DevController.setLongStatus(dialogue.toString());
		// generate.generatePrint(Language.spanish, "newSpecs.xml");
	}
}