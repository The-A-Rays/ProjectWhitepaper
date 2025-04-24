package com.project.aicomics;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

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
		ConfigurationFile config = ConfigurationFile.getInstance(); 

    	// XMLFile xml = new XMLFile("specification_10Scenes.xml");

    XMLFile xml = new XMLFile("specification.xml");
		xml.readXML("specification.xml");
		xml.getRandScene();
		// Audio audio = new Audio(xml);
		// audio.generateAudioXML(Language.romanian);

		// ai.generateAudioFile("Hola, ¿cómo estás?", "dialogue1.mp3");


		XMLGenerator generate = new XMLGenerator("specification_10Scenes.xml");
		generate.generatePrint(config.getLanguage(), "newSpecs");
	}
}