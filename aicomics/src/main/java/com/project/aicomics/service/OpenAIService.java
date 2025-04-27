package com.project.aicomics.service;

import java.io.FileOutputStream;
import java.io.IOException;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.aicomics.ConfigurationFile;
import com.project.aicomics.Parsing;
import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.Scene;
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
     * - {@link #generateAudioFile(String, String)} sends a request with a specified AI behavior and user prompt.
     * @param input text to get audio description of
     * @param fileName name fo the file to store the audio
     */
    public void generateAudioFile(String input, String fileName){
        String url = "https://api.openai.com/v1/audio/speech";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getAPIKey());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "tts-1");
        body.put("input", input);
        body.put("voice", "nova");
        body.put("response_format", "mp3");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);

        byte[] audioBytes = response.getBody();

        if (audioBytes != null) {
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                out.write(audioBytes);
                System.out.println("Saved audio to " + fileName);
            }
            catch (IOException e){
                System.out.println(e);
            }
        }
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
     * @param reader XML file to generate dialogue for
     * @return String response from ai (parsed from JSON format)
     */
    public List<String> GenerateDialogue(XMLFile reader){
        String behaviour = """
        You will receive dialogue from a comic that simply describes the panel in a scene. 
        The format will be a concatenated string were each piece of dialogue has a scene number and then the current dialogue. 
        The dialogue will be in chronologcial order relevant to the scene. 
        You should generate simple and short but realistic human dialogue to replace it, use the context of all the dialogue in each scene to understand the scene's narrative arc.
        There should be exactly one piece of dialogue to replace each piece of dialogue given. 
        Please include only dialogue formatted as a numbered list in chronological order with no quotations. 
        The dialogue should be relevant to the panel and scene. 
        If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO""";
        
        List<String> text = reader.getSourceText();
        // System.out.println(text);
        String prompt = "";
        for(String str : text){
            prompt = prompt + str;
        }
        String response = CallAPI(behaviour, prompt);
        List<String> strings;
        strings = Parsing.parseNumberedList(response);
        return strings;
    }

    public List<String> GenerateDialogue(Scene s) {
        String behaviour = """
        You will receive dialogue from a comic that simply describes the panel in a scene. 
        The format will be a concatenated string were each piece of dialogue has a scene number and then the current dialogue. 
        The dialogue will be in chronologcial order relevant to the scene. 
        You should generate simple and short but realistic human dialogue to replace it, use the context of all the dialogue in each scene to understand the scene's narrative arc.
        There should be exactly one piece of dialogue to replace each piece of dialogue given. 
        Please include only dialogue formatted as a numbered list in chronological order with no quotations. 
        The dialogue should be relevant to the panel and scene. 
        If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO""";
        
        List<String> text = s.getText();
        String prompt = "";
        for(String str : text){
            prompt = prompt + str;
        }
        String response = CallAPI(behaviour, prompt);
        List<String> strings;
        strings = Parsing.parseNumberedList(response);
        return strings;
    }

    /**
     *  - {@link #GenerateCaptions()}
     * @param reader XML file to generate captions for
     * @return List<String> containing captions in the same order as panels
     */
    public List<String> GenerateCaptions(XMLFile reader) {
        List<String> captions;
        String message = "";
        String behavior = """
                You will be given a set of scenes from a comic that contain information regarding each panel,
                 the characters in each panel and their position, along with any other available information.
                 The information will be in chronological order.
                 Using this information, please generate a very brief description that could be used by visually
                 impared people to allow them to better understand the comic and story.
                 You can ignore details like character appearance or location if it is irrellavent
                 Please order the captions in a matching order to the information given, and as a numbered list with no other text.
                 If the request cannot be fulfilled, add the following string to your response: 2W1VXBaWnPXICnxklKXAOw7TO""";
        for (Scene s : reader.getScenes()) {
            message += s.toString() + "\n";
        }
        captions = Parsing.parseNumberedList(this.CallAPI(behavior, message));
        return captions;
    }
}
