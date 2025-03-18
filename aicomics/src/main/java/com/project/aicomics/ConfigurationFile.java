package com.project.aicomics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * Singleton class used to access the configuration file data. 
 * It's a singleton as we should only ever access this once per running attempt
 */
public class ConfigurationFile {

    private static ConfigurationFile INSTANCE = null;
    private File apiconfig = new File("src\\main\\resources\\apikey.txt");
    private String completionsURL;
    private String embedURL;
    private String modelURL;
    private String org;
    private String apiKey;
    private String model;

    /**
     * Constructor is private to enable only ever creating one object of this class
     * through getInstance()
     */
    private ConfigurationFile() {
        File apikey; 
        try {
            apikey = new File(getClass().getProtectionDomain().getCodeSource().getLocation() + "apikey.txt");
            apikey.createNewFile();
            try (Scanner read = new Scanner(apikey)) {
                completionsURL = read.nextLine().substring(16);
                embedURL = read.nextLine().substring(15);
                modelURL = read.nextLine().substring(11);
                org = read.nextLine().substring(8);
                apiKey = read.nextLine().substring(8);
                model = read.nextLine().substring(6);
            }
        } catch (Exception e) {System.out.println("apikey file not generated. Please contact developer" + e);}
    }

    /**
     * Writes new data to API config file
     */
    public void changeAPIConfig(String newOrgKey, String newAPIKey, String newModel) {
        try (FileWriter wr = new FileWriter(apiconfig)) {
            wr.write("COMPLETIONS_URL " + completionsURL + "\n");
            wr.write("EMBEDDINGS_URL "  + embedURL + "\n");
            wr.write("MODELS_URL "      + modelURL + "\n");
            wr.write("ORG_KEY "         + newOrgKey + "\n");
            wr.write("API_KEY "         + newAPIKey + "\n");
            wr.write("MODEL "           + newModel + "\n");
            org = newOrgKey;
            apiKey = newAPIKey;
            model = newModel;
        } catch (IOException e) {
            System.out.println("IO exception: Unable to write, please check API config file: " + e);
        }
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
