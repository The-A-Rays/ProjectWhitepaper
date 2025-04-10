package com.project.aicomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import static com.project.aicomics.Translations.Language.spanish;

import java.util.List;

import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.controller.DevController;
import com.project.aicomics.service.OpenAIService;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		DevController.status("Status test haha");
		DevController.error("error working", new Exception("this is an error"));
		SpringApplication.run(AicomicsApplication.class, args);


		XMLFile reader = new XMLFile();
		reader.readXML();
		ai.GenerateDialogue();
		// System.out.println(ai.GenerateDialogue());
		List<String> str = ai.GenerateDialogue();
		System.out.println(str);
	}
}
