package com.project.aicomics;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.project.aicomics.service.OpenAIService;
import com.project.aicomics.vignette.Vignette;
import com.project.aicomics.vignette.VignetteFileReader;
import com.project.aicomics.vignette.VignetteManager;
import com.project.aicomics.vignette.VignetteSchema;
import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.*;;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		// OpenAIService ai = new OpenAIService();
		SpringApplication.run(AicomicsApplication.class, args);

		try {
            Translations translator = new Translations(Language.english, Language.spanish); // Assume this is properly implemented

            // Read schemas from file
            List<VignetteSchema> schemas = VignetteFileReader.readSchemas("English.tsv", 3);

            // Create VignetteManager
            VignetteManager manager = new VignetteManager(schemas, translator);

            Vignette vignette = manager.getVignette(2);
            if (vignette != null) {
                System.out.println("Generated Vignette: " + vignette.getTranslatedText());
            }

        } catch (Exception e) {
            e.printStackTrace();
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
