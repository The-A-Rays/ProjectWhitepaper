package com.project.aicomics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parsing {
  
    public static ArrayList<String> parseNumberedList(String numberedList){
        ArrayList<String> parsedList = new ArrayList<>();             
                    
        Pattern p = Pattern.compile("(\\d{1,2}\\.\\s)([\\w\\W]+?)(?=\\d{1,2}\\.\\s|$)");
        Matcher m = p.matcher(numberedList);

        while (m.find()){
            parsedList.add(m.group(2).trim());
        }
       return parsedList;
    }

    public static boolean requestDenied (String response){
      return response.contains("2W1VXBaWnPXICnxklKXAOw7TO");
    }

    //returns only the content needed from the original response
    public static String JSONParser(String fullResponse){
        ObjectMapper obj = new ObjectMapper();
        try {
            JsonNode node = obj.readTree(fullResponse);
            return (node.findPath("content").asText());
        } catch (JsonProcessingException ex) {
            return "error parsing the content of the response." + ex.getOriginalMessage();
        }
    }
}

