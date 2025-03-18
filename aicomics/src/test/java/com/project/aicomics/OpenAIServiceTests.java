package com.project.aicomics;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.aicomics.service.OpenAIService;

@SpringBootTest
public class OpenAIServiceTests {

    @Autowired
    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
    }

    // @Test
    // void testGenerateText_Success() {

    //     String userPrompt = "Tell me a joke";

    //     // Call the real aPI
    //     String response = openAIService.generateText(userPrompt);

    //     assertNotNull(response, "Response should not be null");
    //     assertFalse(response.isEmpty(), "Response should not be empty");
    // }
}
