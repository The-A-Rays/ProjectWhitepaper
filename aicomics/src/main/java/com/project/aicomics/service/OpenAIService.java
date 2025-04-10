package com.project.aicomics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.aicomics.ConfigurationFile;
import com.project.aicomics.Parsing;
import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.controller.DevController;

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
     * - {@link #CallAPI(String, String)} sends a request with a specified AI behavior and user prompt.
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
        requestBody.put("max_tokens", 1000); //limits ai response length

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers); //wraps the request body and header together

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class); //POST request
            return Parsing.JSONParser(response.getBody());
        } catch (RestClientException e) {
            DevController.error("Fatal error getting request from Spring", e);
            return "Error calling OpenAI API: " + e.getMessage();
        }
    }

    /**
     * - {@link #TranslateText(String)} translates text to Spanish using OpenAI.
     * @param sourceText String representation of prompt for ai
     * @return String response from ai (parsed from JSON format)
     */
    public String TranslateText(String sourceText, Language lan){
        String behaviour = "You are a translator, translate input to " + lan +". If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO";
        String response = CallAPI(behaviour, sourceText);
        return response;
    }
    /**
     * - {@link #GenerateDialogue()} generates dialogue for a given XML using OpenAI. Should 
     * @param sourceText String representation of prompt for ai
     * @return String response from ai (parsed from JSON format)
     */
    public List<String> GenerateDialogue(XMLFile reader){
        String behaviour = """
        You will receive dialogue from a comic that simply describes the panel in a scene. 
        The format will be a concatenated string were each piece of dialogue has a scene number and then the current dialogue. 
        The dialogue will be in chronologcial order relevant to the scene. 
        You should generate simple and short but realistic human dialogue to replace it, use the context of all the dialogue in each scene to understand the scene's narrative arc. 
        Each piece of dialogue should be separated by a comma. 
        The dialogue should be relevant to the panel and scene. 
        If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO""";
        
        List<String> text = reader.getAllText();
        String prompt = "";
        for(String str : text){
            prompt = prompt + str;
        }
        String response = CallAPI(behaviour, prompt);
        List<String> strings = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\*(.*?)\\*").matcher(response); // LLM was returning dialogue between * symbols so i used this regex pattern
        while (matcher.find()) {
            strings.add(matcher.group(1));
        }
        // String[] dialogue = response.split("(?=Scene)");
        // List<String> dialogues = Arrays.asList(dialogue);
        return strings;
    }

    public List<String> GenerateCaptions(XMLFile reader) {
        List<String> captions = new ArrayList<>();
        
        return captions;
    }
}
