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

@Service
public class OpenAIService {
    private final ConfigurationFile config;
    private final RestTemplate restTemplate;

    public OpenAIService() {
        this.config = ConfigurationFile.getInstance(); // Access ConfigFile CLass
        this.restTemplate = new RestTemplate();
    }

    public String CallAPI(String behaviour, String message){
        String apiUrl = config.getCompURL();
        String apiKey = config.getAPIKey();
        String model = config.getModel();

        // set a header for API KEY
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //specifies JSON format 
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", behaviour)); //defines the ai behaviour
        messages.add(Map.of("role", "user", "content", message)); //this is the actual prompt

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

    public String TranslateText(String sourceText){

        try {
            String behaviour = "You are a translator, translate input to Spanish. If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO";
            String response = CallAPI(behaviour, sourceText);
    
            if (!response.startsWith("{")) { // Not JSON? Log and return a default error.
                System.err.println("API Error: " + response);
                return "{\"error\": \"Invalid API response\"}";
            }
    
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred\"}";
        }
    }
}
