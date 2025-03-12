package com.project.aicomics;

import java.io.*;
import java.util.Scanner;


public class ConfigurationFile {
    
    
    public static String getAPIKey() {
        try {
            File apikey = new File("src\\main\\resources\\apikey.txt");
            String key;
            try (Scanner read = new Scanner(apikey)) {
                for(int i = 0; i < 6; i++) {read.nextLine();}
                key = read.nextLine();
            }
            return key.substring(8, key.length()-1);

        } catch(FileNotFoundException e) {
            System.out.println("File not found, please check config file");
        }
        return "";
    }

    public static String getCompletionsURL() {

    }

    public static String getModel() {

    }
}
