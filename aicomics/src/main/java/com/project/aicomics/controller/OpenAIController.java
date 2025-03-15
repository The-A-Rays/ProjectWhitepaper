package com.project.aicomics.controller;

import com.project.aicomics.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    private final OpenAIService openAIService; //calls the openAI service

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/generate")
    public String generateText(@RequestParam String prompt) {
        return openAIService.generateText(prompt);
    }
}
