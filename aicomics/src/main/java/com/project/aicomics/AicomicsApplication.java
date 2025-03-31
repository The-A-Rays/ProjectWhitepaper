package com.project.aicomics;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.service.OpenAIService;
import com.project.aicomics.vignette.VignetteManager;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);
		// Creating xml doc
		VignetteManager mng = VignetteManager.getInstance();
		Translations trans = new Translations(Translations.Language.english, Translations.Language.spanish);
		ArrayList<Figure> figures = new ArrayList<>();
		// figures.add(new Figure(true));
		// figures.add(new Figure(false));
		// ArrayList<Vignette> scenes = new ArrayList<>();
		// scenes.add(mng.generateVignette(trans, 0));
		// scenes.add(mng.generateVignette(trans, 1));
		// scenes.add(mng.generateVignette(trans, 2));
		// XMLManager.createComic("genericComic", "This is a generic comic!", figures, scenes);
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
