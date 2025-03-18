package com.project.aicomics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OpenAIControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGenerateTextController() {

        String url = "/api/openai/generate?prompt=Tell%20me%20a%20joke";

        // Send the GET request
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertNotNull(response.getBody(), "Response should not be null");
        assertFalse(response.getBody().isEmpty(), "Response should not be empty");
    }
}
