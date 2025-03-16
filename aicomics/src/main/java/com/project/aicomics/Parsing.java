package com.project.aicomics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parsing {
  
    /**
     * Uses a regex to parse a numbered list string into a list
     * @param numberedList String containing list to be parsed
     * @return ArrayList<String> List object version of the string
     */
    public static ArrayList<String> parseNumberedList(String numberedList){
        ArrayList<String> parsedList = new ArrayList<>();             
                    
        Pattern p = Pattern.compile("(\\d{1,2}\\.\\s)([\\w\\W]+?)(?=\\d{1,2}\\.\\s|$)");
        Matcher m = p.matcher(numberedList);

        while (m.find()){
            parsedList.add(m.group(2).trim());
        }
       return parsedList;
    }

    /**
     * @param response String to be parsed
     * @return Boolean value whether the response includes the
     * safeword string "2W1VXBaWnPXICnxklKXAOw7TO"
     */
    public static boolean requestDenied (String response){
      return response.contains("2W1VXBaWnPXICnxklKXAOw7TO");
    }

    /**
     * @param fullResponse JSON in String form to be parsed
     * @return String content of JSON
     */
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

