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
    private static String ls1 = "";
    private static String ls2 = "";

    private DevController() {}

    @ModelAttribute
    @GetMapping("/dev")
    public String devPages(Model model) {
        model.addAttribute("status", states);
        model.addAttribute("errors", errors);
        model.addAttribute("longstatus1", ls1);
        model.addAttribute("longstatus2", ls2);
        return "dev";
    }

    /**
     * Addes a new status and error to the developer frontend
     * @param status String that explains what the error is and where it occurred
     * @param e The exception that is thrown or caught
     */
    public static void error(String status, Exception e) {
        states.add(status);
        errors.add(e);
    }
    
    /**
     * Adds a new status to the developer frontend to mark when an operation is completed or in progress
     * @param status String that explains what has occurred and the status of it.
     */
    public static void status(String status) {
        states.add(status);
    }

    /**
     * Sets up to two long statuses for comparing strings. Will work with first two then show an error at the third
     * @param status Status to assign to a long status variable.
     */
    public static void setLongStatus(String status) {
        if (ls1.equals("")) ls1 = status;
        else if (ls2.equals("")) ls2 = status;
        else error("Setting more than two long statuses", new IllegalArgumentException("Too many long statuses for dev page to handle"));
    }



}
