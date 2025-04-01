package com.project.aicomics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.aicomics.service.OpenAIService;


public class ReadXMLFile {
  private List<Figure> figures;
  private List<Scene> scenes;  
  
  /**
   * gets content from a given tag in an element
   * @param tag the tag whose content we wanat to retrive
   * @param element element in which the tag is found
   * @return the content of the tag or an empty string if there's nothing
   */
  public static String getTagVal(String tag, Element element) {
    NodeList nodeList = element.getElementsByTagName(tag);
    if (nodeList.getLength() > 0) {
        Node node = nodeList.item(0);
        return node.getTextContent();
    }
    return "";
}

  public void readXML() {
    try (InputStream inputStream = ReadXMLFile.class.getClassLoader().getResourceAsStream("specification.xml")) {
        if (inputStream == null) {
            throw new IOException("File not found: specification.xml");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        doc.getDocumentElement().normalize();

        this.figures = parseFigures(doc);
        this.scenes = parseScenes(doc);

    } catch (Exception e) {
        System.out.println("Something went wrong when processing the file.");
        e.printStackTrace();
    }
  }

  //overloading so it accepts both documents and elements
    private static List<Figure> parseFigures(Document doc){
      return parseFigures(doc.getDocumentElement());
    }

    /**
     * 
     * @param elem the element from which we want to extract the figures
     * @return a list containing all the figures in the passed element
     */
    private static List<Figure> parseFigures(Element elem){
      List<Figure> figures = new ArrayList<>();
      NodeList figureNodes = elem.getElementsByTagName("figure"); //list containing each figure element

      for (int i = 0; i < figureNodes.getLength(); i++) {
         Node node = figureNodes.item(i);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
            Figure figure = new Figure((Element) node);
            figures.add(figure); 
          }
      }
      return figures;
    }

    /**
     * 
     * @param elem the element from which we want to extract the speeches from
     * @return a list containing all the speech bubbles' content
     */
    private static List<Bubble> parseBubbles(Element elem){
      List<Bubble> bubbles = new ArrayList<>();
      NodeList bubblesNodes = elem.getElementsByTagName("balloon"); //list containing each speech bubble

      for (int i = 0; i < bubblesNodes.getLength(); i++) {
         Node bubbleN = bubblesNodes.item(i);
          if (bubbleN.getNodeType() == Node.ELEMENT_NODE) {
            Bubble bubble = new Bubble();
            Element bubbleElem = (Element) bubbleN;
            bubble.setContent(getTagVal("content", bubbleElem)); //gets content
            bubble.setStatus(bubbleElem.getAttribute("status")); //gets status
            bubbles.add(bubble); 
          }
      }
      return bubbles;
    }

    /**
     * 
     * @param doc the XML Document object containing the comic structure
     * @return a list of all the scenes in the file
     */
    private static List<Scene> parseScenes(Document doc) {
        List<Scene> scenes = new ArrayList<>();
        NodeList sceneNodes = doc.getElementsByTagName("scenes").item(0).getChildNodes(); //method on individual node, hence item(0) since getElem.. returns a NodeList, in our case with only one node
        
        //itaretaes through all "scene" elements in "scenes" and looks for panels in each one
        for (int i = 0; i < sceneNodes.getLength(); i++){
          Scene scene = new Scene();
          Node sceneN = sceneNodes.item(i);
          if (sceneN.getNodeType() == Node.ELEMENT_NODE){
            NodeList panelNodes = ((Element)sceneN).getElementsByTagName("panel");//list containing all panles from one scene
            
            //goes trhough each panel of a specific scene and creates a panel object with the retrieved values from the .xml file
            for (int j = 0; j < panelNodes.getLength(); j++){
              Node panelN = panelNodes.item(j);
              if (panelN.getNodeType() == Node.ELEMENT_NODE){
                Panel panel = new Panel();
                Element panelElem = (Element) panelN;

                switch (panelElem.getParentNode().getNodeName()) {
                    case "left":
                        panel.setPosition("left");
                        break;
                    case "right":
                        panel.setPosition("right");
                        break;
                    case "middle":
                        panel.setPosition("middle");
                        break;
                    default:
                        break;
                }
                panel.setFigures(parseFigures(panelElem));
                panel.setBubbles(parseBubbles(panelElem));
                panel.setSetting(getTagVal("setting", panelElem));

                scene.addPanel(panel); //adds each panel to the list of panels in the scene we're currently manipulating
              }
            }
          }
          scenes.add(scene);
        }
        return scenes;
    }


    public List<String> getAllTranslatedText() {
      OpenAIService ai = new OpenAIService();
      List<String> spokenText = new ArrayList<>();
      
      if (scenes == null) {
          System.out.println("No scenes available.");
          return spokenText;
      }
  
      for (Scene scene : scenes) {
          for (Panel panel : scene.getPanels()) {
              for (Bubble bubble : panel.getBubbles()) {
                  spokenText.add(bubble.getContent());
                  spokenText.add(ai.TranslateText(bubble.getContent())); // not working
              }
          }
      }
  
      return spokenText;
    }
  
  }
  