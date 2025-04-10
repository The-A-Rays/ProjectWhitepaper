package com.project.aicomics;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.XML.XMLGenerator;
import com.project.aicomics.service.OpenAIService;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);


		XMLFile generate = new XMLGenerator("specification_10Scenes.xml");
		// System.out.println(ai.GenerateDialogue());
		List<String> str = ai.GenerateDialogue(generate);
		System.out.println(str);
	}
}
