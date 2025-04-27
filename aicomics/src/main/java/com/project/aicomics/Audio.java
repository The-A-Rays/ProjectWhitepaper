package com.project.aicomics;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.service.OpenAIService;

public class Audio {

  private final OpenAIService ai = new OpenAIService();
  private final Map<String, String> textToAudio;
  private final XMLFile xml;

  public Audio(XMLFile xml){
    this.xml = xml;
    this.textToAudio = new HashMap<>();
  }


  public void generateAudioXML(Language lan) throws IOException{
    List<String> allText = xml.getAllText(lan);

    for(String t : allText){
      if (t != null && !t.isBlank()){
        //if text is already in the file, dont do anything
        if (textToAudio.containsKey(t)) continue;
        //else create audio file and add it and the text to the map
        String safeText = t.replaceAll("[^a-zA-Z0-9]", "_"); //for saftey in case the name contains a special char
        String audioFileName = "audio_" + safeText + ".mp3";
        File audioFile = new File (audioFileName);

        if (!audioFile.exists()){
          ai.generateAudioFile(t, audioFile.getPath());
          textToAudio.put(t, audioFile.getPath());
        } else {
          textToAudio.put(t, audioFile.getPath());
        }
        textToAudio.toString();
      }
    }
  }

  public String getAudioFileName(String text){
    return textToAudio.get(text);
  }

}
