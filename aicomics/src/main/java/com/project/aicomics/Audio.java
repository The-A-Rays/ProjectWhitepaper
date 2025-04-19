package com.project.aicomics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.aicomics.Translations.Language;
import com.project.aicomics.XML.XMLFile;
import com.project.aicomics.service.OpenAIService;

public class Audio {

  private OpenAIService ai = new OpenAIService();
  private Map<String, String> textToAudio;
  private final File directory = new File("src/main/resources/audio");
  private XMLFile xml;

  public Audio(XMLFile xml){
    if (!directory.exists())
      directory.mkdirs();
    this.xml = xml;
    this.textToAudio = new HashMap<>();
  }


  public void generateAudioXML(Language lan) throws IOException{
    List<String> allText = xml.getAllTranslatedText(lan);
    
    //cretae new directory for each language audios
    File languageDir = new File(directory, lan.toString().toLowerCase());
    if (!languageDir.exists()) languageDir.mkdirs();

    for(String t : allText){
      if (t != null && !t.isBlank()){
        //if text is already in the file, dont do anything
        if (textToAudio.containsKey(t)) continue;
        //else create audio file and add it and the text to the map
        String safeText = t.replaceAll("[^a-zA-Z0-9]", "_"); //for saftey in case the name contains a special char
        String audioFileName = "audio_" + safeText + "_" + lan + ".mp3";
        File audioFile = new File (languageDir, audioFileName);

        if (!audioFile.exists()){
          ai.generateAudioFile(t, audioFile.getPath().toString());
          textToAudio.put(t, audioFile.getPath().toString());
        } else {
          textToAudio.put(t, audioFile.getPath().toString());
        }
        textToAudio.toString();
      }
    }
   // saveIndex(new File("src/main/resources/audio/audios.txt")); //??
  }

  public String getAudioFileName(String text){
    return textToAudio.get(text);
  }


  private void saveIndex(File f) throws IOException{
    try(FileWriter wr = new FileWriter(f)){
      for(Map.Entry<String, String> entry : textToAudio.entrySet()){
        wr.write(entry.getKey() + "=" + entry.getValue() + "\n");
      }
    }
  }

  private void loadIndex(File f) throws IOException{
    textToAudio = new HashMap<>();
    if (f.exists()){
      try(BufferedReader r = new BufferedReader(new FileReader(f))){
        String line;
        while ((line = r.readLine()) != null){
          if (line.isEmpty() || line.isBlank() || !line.contains("=")) continue;
          String key = line.split("=", 2)[0];
          String val = line.split("=", 2)[1];
          textToAudio.put(key,val);
        }
      }
    } else return;
  }
}
