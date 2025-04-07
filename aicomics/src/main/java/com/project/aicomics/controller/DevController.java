package com.project.aicomics.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DevController {
    
    private final static List<String> states = new ArrayList<>();
    private final static List<Exception> errors = new ArrayList<>();

    @ModelAttribute
    @GetMapping("/dev")
    public String devPages(Model model) {
        return "dev";
    }

    public static void error(String status, Exception e) {
        states.add(status);
        errors.add(e);
    }
    
    public static void status(String status) {
        states.add(status);
    }

    

}
