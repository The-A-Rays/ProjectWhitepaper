package com.project.aicomics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class VignetteManager {

    VignetteSchema schema = new VignetteSchema();
    

    public VignetteManager() {
        String fileName = "English.tsv";  // The file name you want to read
        String[] firstRow = readFirstTSVLine(fileName); // Read the first line of the TSV file

        if (firstRow != null) {
            // Populate schema using the first row
            addToSchema("leftPose", firstRow[0]);
            addToSchema("combinedText", firstRow[1]);
            addToSchema("leftText", firstRow[2]);
            addToSchema("rightPose", firstRow[3]);
            addToSchema("background", firstRow[4]);
        } else {
            System.out.println("Error: Invalid or empty row in TSV.");
        }
        // Vignette vignette = new Vignette(null, null, null, null, null, null);
    }

    private void addToSchema(String type, String field){
        String[] values = field.split(","); // Split by comma
        // ArrayList<String> resultList = new ArrayList<>();
        for (String value : values) {
            schema.add(type, value.trim()); // Trim spaces and add to the list
        }
    }

    public static String[] readFirstTSVLine(String fileName) {
        try (InputStream inputStream = VignetteManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine(); // Read the first line
                if (line != null) {
                    return line.split("\t"); // Split by tab and return as array
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if the file is empty or an error occurs
    }

    
}
