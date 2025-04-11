package com.project.aicomics;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.Translations.Language;
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

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);


		XMLGenerator generate = new XMLGenerator("specification_10Scenes.xml");
		DevController.status("Number of bubbles: " + generate.getAllText().size());
		DevController.setLongStatus(generate.getAllText().toString());
		List<String> dialogue = ai.GenerateDialogue(generate);
		DevController.status("Generated dialogue size: " + dialogue.size());
		DevController.setLongStatus(dialogue.toString());
		generate.generatePrint(Language.spanish, "newSpecs.xml");
	}
}