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
    private File apiconfig;
    private String completionsURL = "https://api.openai.com/v1/chat/completions";
    private String embedURL = "https://api.openai.com/v1/embeddings";
    private String modelURL = "https://api.openai.com/v1/models";
    private String org;
    private String apiKey;
    private String model;
    private String language = "English";

    /**
     * Constructor is private to enable only ever creating one object of this class
     * through getInstance()
     */
    private ConfigurationFile() {
        try {
            apiconfig = new File("apikey.txt");
            if (apiconfig.createNewFile()) {
                this.changeAPIConfig("Put org key here", "Put API key here", "put model here");
                System.out.println("API Configuration File not found. Program will not function normally. \n Please configure file and re-run program. Program will now close.");
                throw new IllegalArgumentException();
            }
            System.out.println(apiconfig);
            try (Scanner read = new Scanner(apiconfig)) {
                completionsURL = read.nextLine().substring(16);
                embedURL = read.nextLine().substring(15);
                modelURL = read.nextLine().substring(11);
                org = read.nextLine().substring(8);
                apiKey = read.nextLine().substring(8);
                model = read.nextLine().substring(6);
                language = read.nextLine().substring(9);
            }
        } catch (IOException e) {System.out.println("apikey file not generated. Please contact developer " + e);}
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
            wr.write("LANGUAGE"         + language + "\n");
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
