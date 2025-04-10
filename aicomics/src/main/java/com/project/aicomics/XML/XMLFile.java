package com.project.aicomics.XML;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.aicomics.Translations;
import com.project.aicomics.Translations.Language;
import com.project.aicomics.controller.DevController;

public class XMLFile {
  protected List<Scene> scenes;  
  protected List<Figure> figures;
  protected List<String> translatedText;

  public XMLFile(String fileName) {
    this.readXML(fileName);
  }

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
    return null;
}

  private void readXML(String file) {
    try (InputStream inputStream = XMLFile.class.getClassLoader().getResourceAsStream(file)) {
        if (inputStream == null) {
            throw new IOException("File not found: " + file);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        doc.getDocumentElement().normalize();

        NodeList figuersNodes = doc.getElementsByTagName("figures");
        for (int i = 0; i < figuersNodes.getLength(); i ++){
          Node node = figuersNodes.item(i);
          this.figures = parseFigures((Element)node);
        }
        this.scenes = parseScenes(doc);

    } catch (IOException e) {
      DevController.error("Fatal error processing XML file", e);
    } catch (ParserConfigurationException e) {
      DevController.error("Fatal error creating document factory or builder", e);
    } catch (SAXException e) {
      DevController.error("Fatal error putting inputStream into document builder", e);
    } 
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
     */ //extracts bubbles from different positions
     private static List<Bubble> parseBubbles(Element elem){
      List<Bubble> bubbles = new ArrayList<>();
      NodeList bubbleNodes = elem.getElementsByTagName("balloon");   

      for (int j = 0; j < bubbleNodes.getLength(); j++) { //get balloons
        Node bubbleN = bubbleNodes.item(j);
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
      NodeList sceneNodes = doc.getElementsByTagName("scene");
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

              //goes through child nodes of the panel node to find position/positions
              NodeList childNodes = panelElem.getChildNodes();
              for (int k = 0; k < childNodes.getLength(); k++) {
                Node child = childNodes.item(k);
                //filters out the children that are only elements EXCLUDING the damn new lines
                if (child.getNodeType() == Node.ELEMENT_NODE && !"#text".equals(child.getNodeName())) {
                 
                  Element positionElem = (Element) child;
                  switch (child.getNodeName()) {
                    case "left" -> {
                      Position position = new Position();
                        position.setName("left");
                        position.setFigure(parseFigures(positionElem).get(0));
                        position.setBubble(parseBubbles(positionElem).get(0));
                        panel.addPosition(position);
                        }
                    case "right" -> {
                      Position position = new Position();
                        position.setName("right");
                        position.setFigure(parseFigures(positionElem).get(0));
                        position.setBubble(parseBubbles(positionElem).get(0));
                        panel.addPosition(position);
                        }
                    case "middle" -> {
                      Position position = new Position();
                        position.setName("middle");
                        position.setFigure(parseFigures(positionElem).get(0));
                        position.setBubble(parseBubbles(positionElem).get(0));
                        panel.addPosition(position);
                        }
                    case "border" ->{
                      panel.setBorder(getTagVal("border", panelElem));
                    } 
                    case "below" -> {
                      panel.setTitleBelow(getTagVal("below", panelElem));
                    }
                    default -> {
                        }
                  }
                  
                }
              }
              panel.setSetting(getTagVal("setting", panelElem));
              scene.addPanel(panel); //adds each panel to the list of panels in the scene we're currently manipulating
            }
          }
        }
        scenes.add(scene);
      }
      return scenes;
  }

  /**
   * 
   * @return a list of Strings containing all the speech content from the XML plus the content translated
   */ 
  public List<String> getAllTranslatedText(Language lan) {
    List<String> spokenText = new ArrayList<>();
    Translations translate = new Translations(Language.english, lan);
    
    // Added so that if the method is called multiple times on the same file
    // it doesn't give unnecessary api requests.
    if (translatedText != null) return translatedText;

    if (scenes == null) {
        System.out.println("No scenes available.");
        return spokenText;
    }
    for (Scene scene : scenes) {
      for (Panel panel : scene.getPanels()) {
        for(Position pos: panel.getPosition()){
            spokenText.add(translate.getTranslation(pos.getBubble().getContent()));
        }
      }
    }
    translatedText = spokenText;
    return spokenText;
  }

  public List<String> getAllText() {
    List<String> spokenText = new ArrayList<>();

    if (scenes == null) {
        System.out.println("No scenes available.");
        return spokenText;
    }
    int i = 0;
    for (Scene scene : scenes) {
      for (Panel panel : scene.getPanels()) {
        for(Position pos: panel.getPosition()){
          spokenText.add( "Scene " + i + " :" + pos.getBubble().getContent() + ", ");
        }
      }
      i++;
    }
    return spokenText;
  }

  public List<Scene> getScenes() {
    return scenes;
  }

  public List<Figure> getFigures() {
    return figures;
  }
}