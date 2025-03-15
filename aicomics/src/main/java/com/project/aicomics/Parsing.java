package com.project.aicomics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {
  
    public ArrayList<String> parseNumberedList(String numberedList){
        ArrayList<String> parsedList = new ArrayList<>();             
                    
        Pattern p = Pattern.compile("(\\d{1,2}\\.\\s)([\\w\\W]+?)(?=\\n\\d{1,2}\\.\\s|$)");
        Matcher m = p.matcher(numberedList);

        while (m.find()){
            parsedList.add(m.group(2));
        }
       for (int i = 0; i < parsedList.size(); i++){
            parsedList.add(parsedList.get(i));
          //  System.out.println(parsedList.get(i));
       }
       return parsedList;
      }

  
    public boolean requestDenied (String response){
      return response.contains("2W1VXBaWnPXICnxklKXAOw7TO");
    }
}

