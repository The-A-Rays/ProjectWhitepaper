package com.project.aicomics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.aicomics.ConfigurationFile;
import com.project.aicomics.Parsing;

@Service
/**
 * Class to send manage HTTP requests to OpenAI's API
 * CallAPI() method calls API with ai behaviour and user prompt as input
 * TranslateText() calls the first method with the behaviour designated as Translator
 */
public class OpenAIService {
    private final ConfigurationFile config;
    private final RestTemplate restTemplate; // for sending HTTP requests

    public OpenAIService() {
        this.config = ConfigurationFile.getInstance();
        this.restTemplate = new RestTemplate();
    }

    /**
     * Method retrieves model, URL, and APIkey creates HTTPentity for POST request
     * @param behaviour String to define ai behaviour
     * @param message String representation of prompt for ai
     * @return String response from ai (parsed from JSON format)
     */
    public String CallAPI(String behaviour, String message){
        String apiUrl = config.getCompURL();
        String apiKey = config.getAPIKey();
        String model = config.getModel();

        // Create HTTP header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //specifies JSON format 
        headers.setBearerAuth(apiKey); //sets Bearer token for authentication

        // Build the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", behaviour)); //defines the ai behaviour
        messages.add(Map.of("role", "user", "content", message)); //this is the actual prompt

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 100); //limits ai response length

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers); //wraps the request body and header together

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class); //POST request
            return Parsing.JSONParser(response.getBody());
        } catch (Exception e) {
            return "Error calling OpenAI API: " + e.getMessage();
        }
    }

    /**
     * Method calls CallAPI() method with ai behaviour defined as a translator
     * @param sourceText String representation of prompt for ai
     * @return String response from ai (parsed from JSON format)
     */
    public String TranslateText(String sourceText){

        try {
            String behaviour = "You are a translator, translate input to Spanish. If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO";
            String response = CallAPI(behaviour, sourceText);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred\"}";
        }
    }
}
