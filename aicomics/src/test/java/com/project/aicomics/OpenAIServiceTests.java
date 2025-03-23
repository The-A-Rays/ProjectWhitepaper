package com.project.aicomics;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import com.project.aicomics.service.OpenAIService;

@SpringBootTest
public class OpenAIServiceTests {

    @Autowired
    private OpenAIService openAIService;

    @Test
    void testCallAPI_Success() {

        // CallAPI takes both the requested behaviour and the users prompt as input 

        String userPrompt = "Tell me a joke";

        String response = openAIService.CallAPI("You are a helpful assistant", userPrompt);

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
    }

    @Test
    void testTranslateText_Success() throws Exception {

        // CallAPI takes both the requested behaviour and the users prompt as input 

        String userPrompt = "Hello";

        String response = openAIService.TranslateText(userPrompt);

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        // Extract translated text
        String translatedText = jsonNode.get("choices").get(0).get("message").get("content").asText();        
        System.out.println("Extracted Translated Text: " + translatedText);

        // Validate the translation (assuming English "Hello" translates to Spanish "Hola")
        assertEquals("Hola", translatedText, "Translation should be 'Hola'");
    }
}
