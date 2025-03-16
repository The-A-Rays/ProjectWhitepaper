package com.project.aicomics;

import com.project.aicomics.ConfigurationFile;
import com.project.aicomics.service.OpenAIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpenAIServiceTests {

    @Autowired
    private OpenAIService openAIService;

    private ConfigurationFile config;

    @BeforeEach
    void setUp() {
        config = ConfigurationFile.getInstance();
    }

    @Test
    void testGenerateText_Success() {

        String userPrompt = "Tell me a joke";

        // Call the real aPI
        String response = openAIService.generateText(userPrompt);

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
    }
}
