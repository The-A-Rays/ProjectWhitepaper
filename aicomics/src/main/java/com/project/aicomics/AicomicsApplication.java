package com.project.aicomics;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.service.OpenAIService;
import com.project.aicomics.XML.*;;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);

		ReadXMLFile reader = new ReadXMLFile();
		reader.readXML();
		List<String> spokenLines = reader.getAllTranslatedText();

		for (String line : spokenLines) {
			System.out.println(line);
		}

	}

	// Major steps for this Sprint:
	//  Theme being conjugations in the target language: I learn, You Learn, We learn
	// Load/Parse XML with DOM parser
	// extract all spoken text from XML
	// obtain trnaslations of this text
	// Modify XML to interweave the translations
	// Save XML lesson to a new file

	// XML Blueprint Class - use DOM parser to load an XML "specification" or blueprint

}
