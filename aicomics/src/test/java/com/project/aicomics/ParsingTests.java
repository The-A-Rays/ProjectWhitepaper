package com.project.aicomics;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ParsingTests {
  
  @Test //test for when the string is formatted as expected
  void parseNumberedListTest1(){
    String initialResponse = "1. Why did the scarecrow win an award? 2. Because he was outstanding in his field!";

    ArrayList <String> responseAfterParsing = Parsing.parseNumberedList(initialResponse);     
    
    assertEquals(2, responseAfterParsing.size());
    assertEquals("Why did the scarecrow win an award?", responseAfterParsing.get(0));
    assertEquals("Because he was outstanding in his field", responseAfterParsing.get(1));
  }

  @Test //test for when the string is not formatted as expected - 1
  void parseNumberedListTest2(){
    String initialResponse = "Why did the scarecrow win an award? 2. Because he was outstanding in his field!         ";

    ArrayList <String> responseAfterParsing = Parsing.parseNumberedList(initialResponse);     
    
    assertEquals(1, responseAfterParsing.size());
    assertEquals("Because he was outstanding in his field", responseAfterParsing.get(0));
  }

  @Test //test for when the string is formatted as expected - 2
  void parseNumberedListTest3(){
    String initialResponse = "Why did the scarecrow win an award? Because he was outstanding in his field!";

    ArrayList <String> responseAfterParsing = Parsing.parseNumberedList(initialResponse);     
    
    assertEquals(0, responseAfterParsing.size());
  }

  @Test //test for when the string is empty
  void parseNumberedListTest4(){
    String initialResponse = "";

    ArrayList <String> responseAfterParsing = Parsing.parseNumberedList(initialResponse);     
    
    assertEquals(0, responseAfterParsing.size());
  }

  @Test //when the request is not denied
  void requestDeniedTest1(){
    String response = "No, I can't";
    Boolean perrmission = Parsing.requestDenied(response);
    assertFalse(perrmission);
  }

  @Test //when the request is denied
  void requestDeniedTest2(){
    String response = "No, I can't 2W1VXBaWnPXICnxklKXAOw7TO";
    Boolean perrmission = Parsing.requestDenied(response);
    assertTrue(perrmission);
  }

  @Test //example taken from the tell me a joke prompt
  void JSONParserTest(){
    String fullResponse = "{ \"id\": \"chatcmpl-BBT79HhtZVkj09emzBA8Do1IiCCMt\", \"object\": \"chat.completion\", \"created\": 1742073223, \"model\": \"gpt-4o-mini-2024-07-18\", \"choices\": [ { \"index\": 0, \"message\": { \"role\": \"assistant\", \"content\": \"1. Why don't skeletons fight each other? \\n" + //
            "2. They don't have the guts. \\n" + //
            "\", \"refusal\": null, \"annotations\": [] }, \"logprobs\": null, \"finish_reason\": \"stop\" } ], \"usage\": { \"prompt_tokens\": 96, \"completion_tokens\": 21, \"total_tokens\": 117, \"prompt_tokens_details\": { \"cached_tokens\": 0, \"audio_tokens\": 0 }, \"completion_tokens_details\": { \"reasoning_tokens\": 0, \"audio_tokens\": 0, \"accepted_prediction_tokens\": 0, \"rejected_prediction_tokens\": 0 } }, \"service_tier\": \"default\", \"system_fingerprint\": \"fp_06737a9306\" }";
    String content = Parsing.JSONParser(fullResponse);
    String expected = "1. Why don't scientists trust atoms? 2. Because they make up everything!";
    assertEquals(expected, content);
  }
  
}
