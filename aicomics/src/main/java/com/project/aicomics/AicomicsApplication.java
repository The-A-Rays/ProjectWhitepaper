package com.project.aicomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.XMLGenerator;
import com.project.aicomics.service.OpenAIService;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);


		XMLGenerator generate = new XMLGenerator("specification_10Scenes.xml");
		// System.out.println(ai.GenerateDialogue());
		generate.generatePrint(Language.spanish);
	}
}
