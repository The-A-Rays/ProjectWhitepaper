package com.project.aicomics.service;

import com.project.aicomics.ConfigurationFile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    private final ConfigurationFile config;
    private final RestTemplate restTemplate;

    public OpenAIService() {
        this.config = ConfigurationFile.getInstance(); // Access ConfigFile CLass
        this.restTemplate = new RestTemplate();
    }

    public String generateText(String userMessage) {
        String apiUrl = config.getCompURL();
        String apiKey = config.getAPIKey();
        String model = config.getModel();

        // set a header for API KEY
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //specifies JSON format 
        headers.setBearerAuth(apiKey);

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a helpful assistant.")); //defines the ai behaviour
        messages.add(Map.of("role", "user", "content", userMessage)); //this is the actual prompt

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers); //wraps the request body and headerr together

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class); //send request
            return response.getBody(); //gets the JSON response
        } catch (Exception e) {
            return "Error calling OpenAI API: " + e.getMessage();
        }
    }

    
}
