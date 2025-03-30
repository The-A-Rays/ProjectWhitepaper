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
    // Using an array as the number of String variables has gotten rather large.
    // Comp URL, Embed URL, Model URL, Org key, API key, Model, Status
    private String[] fileInfo = {"https://api.openai.com/v1/chat/completions", "https://api.openai.com/v1/embeddings",
                                "https://api.openai.com/v1/models", "", "", "", "Config File Error: File not Initialized"};
    private Translations.Language language = Translations.Language.english; 

    /**
     * Constructor is private to enable only ever creating one object of this class
     * through getInstance()
     */
    private ConfigurationFile() {
        try {
            apiconfig = new File("apikey.txt");
            if (apiconfig.createNewFile()) {
                this.changeAPIConfig("Put org key here", "Put API key here", "put model here", Translations.Language.english);
                fileInfo[6] = ("API Configuration File not found. Program will not function normally. \n Please configure file and re-run program.");
            }
            System.out.println(apiconfig);
            try (Scanner read = new Scanner(apiconfig)) {
                fileInfo[0] = read.nextLine().substring(16);
                fileInfo[1] = read.nextLine().substring(15);
                fileInfo[2] = read.nextLine().substring(11);
                fileInfo[3] = read.nextLine().substring(8);
                fileInfo[4] = read.nextLine().substring(8);
                fileInfo[5] = read.nextLine().substring(6);
                language = Translations.Language.valueOf(Translations.Language.class, read.nextLine().substring(9));
            }
        } catch (IOException e) {
            fileInfo[6] = ("apikey file not generated. Please contact developer " + e);
            }
        catch (IllegalArgumentException e) {
            fileInfo[6] = ("Language read in is not a valid option. Please enter a valid language and restart.");
            throw e;
            }
    }

    /**
     * Writes new data to API config file
     */
    public final void changeAPIConfig(String newOrgKey, String newAPIKey, String newModel, Translations.Language language) {
        try (FileWriter wr = new FileWriter(apiconfig)) {
            wr.write("COMPLETIONS_URL " + fileInfo[0] + "\n");
            wr.write("EMBEDDINGS_URL "  + fileInfo[1] + "\n");
            wr.write("MODELS_URL "      + fileInfo[2] + "\n");
            wr.write("ORG_KEY "         + newOrgKey + "\n");
            wr.write("API_KEY "         + newAPIKey + "\n");
            wr.write("MODEL "           + newModel + "\n");
            wr.write("LANGUAGE "        + language + "\n");
            fileInfo[3] = newOrgKey;
            fileInfo[4] = newAPIKey;
            fileInfo[5] = newModel;
            this.language = language;
        } catch (IOException e) {
            fileInfo[6] = ("IO exception: Unable to write, please check API config file: " + e);
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
        return fileInfo[0];
    }

    public String getEmbedURL() {
        return fileInfo[1];
    }

    public String getModelURL() {
        return fileInfo[2];
    }

    public String getOrg() {
        return fileInfo[3];
    }

    public String getAPIKey() {
        return fileInfo[4];
    }

    public String getModel() {
        return fileInfo[5];
    }

    public String getStatus() {
        return fileInfo[6];
    }

    public Translations.Language getLanguage() {
        return language;
    }
}
