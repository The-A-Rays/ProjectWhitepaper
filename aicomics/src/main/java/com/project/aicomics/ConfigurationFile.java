package com.project.aicomics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * Singleton class used to access the configuration file data. 
 * It's a singleton as we should only ever access this once per running attempt
 */
public class ConfigurationFile {

    private static ConfigurationFile INSTANCE = null;
    String completionsURL;
    String embedURL;
    String modelURL;
    String org;
    String apiKey;
    String model;

    private ConfigurationFile() {
        File apiconfig = new File("src\\main\\resources\\apikey.txt");
        try (Scanner read = new Scanner(apiconfig)) {
            completionsURL = read.nextLine().substring(16);
            embedURL = read.nextLine().substring(15);
            modelURL = read.nextLine().substring(11);
            org = read.nextLine().substring(8);
            apiKey = read.nextLine().substring(8);
            model = read.nextLine().substring(6);
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception: Please check API config file");
        }

        // Very simple checks to ensure the URLs are placed in the correct position
        if (completionsURL.charAt(0) != 'h') System.out.println("completionsURL not read correctly. Check API config file");
        if (embedURL.charAt(0) != 'h') System.out.println("embed URL not read correctly. Check API config file");
        if (modelURL.charAt(0) != 'h') System.out.println("model URL not read correctly. Check API config file");
    }
    
    /**
     * @return Created or returns the single ConfigurationFile instance
     */
    public static synchronized ConfigurationFile getInstance() {
        if (INSTANCE == null) {INSTANCE = new ConfigurationFile();}
        return INSTANCE;
    }

    public String getCompURL() {
        return completionsURL;
    }

    public String getEmbedURL() {
        return embedURL;
    }

    public String getModelURL() {
        return modelURL;
    }

    public String getOrg() {
        return org;
    }

    public String getAPIKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }
}
