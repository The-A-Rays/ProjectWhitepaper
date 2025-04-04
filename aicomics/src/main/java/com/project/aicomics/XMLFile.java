package com.project.aicomics;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.aicomics.service.OpenAIService;

public class XMLFile {
  private List<Scene> scenes;  
  private List<Figure> figures;

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
  try (InputStream inputStream = XMLFile.class.getClassLoader().getResourceAsStream("specification.xml")) {
      if (inputStream == null) {
          throw new IOException("File not found: specification.xml");
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(inputStream);
      doc.getDocumentElement().normalize();

      this.figures = parseFigures(doc);
      this.scenes = parseScenes(doc);
      System.out.println("Scenes parsed: " + (scenes == null ? "null" : scenes.size()));

  } catch (Exception e) {
      System.out.println("Something went wrong when processing the file.");
      e.printStackTrace();
  }
}

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
        System.out.println("scene"+i);
        Scene scene = new Scene();
        Node sceneN = sceneNodes.item(i);
        System.out.println("is it scene elem: "+(sceneN.getNodeType() == Node.ELEMENT_NODE));
        if (sceneN.getNodeType() == Node.ELEMENT_NODE){
          NodeList panelNodes = ((Element)sceneN).getElementsByTagName("panel");//list containing all panles from one scene
          
          //goes trhough each panel of a specific scene and creates a panel object with the retrieved values from the .xml file
          for (int j = 0; j < panelNodes.getLength(); j++){
            Node panelN = panelNodes.item(j);
            System.out.println("pan=" + j);
            if (panelN.getNodeType() == Node.ELEMENT_NODE){
              Panel panel = new Panel();
              Element panelElem = (Element) panelN;

              //goes through child nodes of the panel node to find position/positions
              NodeList childNodes = panelElem.getChildNodes();
              System.out.println(childNodes.toString());
              for (int k = 0; k < childNodes.getLength(); k++) {
                System.out.println("child="+k);
                Node child = childNodes.item(k);
                System.out.println(child.getNodeName());
                System.out.println("is child node elem: "+ (child.getNodeType() == Node.ELEMENT_NODE));
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                  Position position = new Position();
                  Element positionElem = (Element) child;
                  switch (child.getNodeName()) {
                    case "left" -> {
                        position.setName("left");
                        position.setFigures(parseFigures(positionElem));
                        position.setBubbles(parseBubbles(positionElem));
                        }
                    case "right" -> {
                        position.setName("right");
                        position.setFigures(parseFigures(positionElem));
                        position.setBubbles(parseBubbles(positionElem));
                        }
                    case "middle" -> {
                        position.setName("middle");
                        position.setFigures(parseFigures(positionElem));
                        position.setBubbles(parseBubbles(positionElem));
                        }
                    case "border" -> panel.setBorder(getTagVal("border", panelElem));
                    case "below" -> panel.setTitleBelow(getTagVal("below", panelElem));
                    default -> {
                        }
                  }
                  panel.addPosition(position);
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

  
  public List<String> getAllTranslatedText() {
    OpenAIService ai = new OpenAIService();
    List<String> spokenText = new ArrayList<>();
    
    if (scenes == null) {
        System.out.println("No scenes available.");
        return spokenText;
    }
    for (Scene scene : scenes) {
      for (Panel panel : scene.getPanels()) {
        for(Position pos: panel.getPosition()){
          for(Bubble bubble : pos.getBubbles()) {
            spokenText.add(bubble.getContent());
            spokenText.add(ai.TranslateText(bubble.getContent()));
        }
        }
      }
    }
    return spokenText;
  }

  /**
   * Prints the XMLFile object into a new XML file with the
   * translated scenes next to the original.
   */
  public void translationPrint() {
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {builder = fac.newDocumentBuilder();}
    catch (ParserConfigurationException e) {
      System.out.println("Error creating new document builder: " + e.getStackTrace());
      return;
    }
    Document doc = builder.newDocument();
    List<String> trans = getAllTranslatedText();
    // Create base file formatting
    Element comic = doc.createElement("comic");
    
    // Insert Figures
    Element xmlFigures = doc.createElement("figures");
    comic.appendChild(xmlFigures);
    for (Figure f : figures) {
      addFigure(f, xmlFigures, doc);
    }
    // Insert Scenes
    /* This code is hard to read, but to put it simply it addes the scene little by little
     * Most of it is creating elements and appending them to their parents
     * The only content is the figures and balloons in position
     * and the setting, below, and border in panel
     * Everything else is formatting
     */
    Element xmlScenes = doc.createElement("scenes");
    comic.appendChild(xmlScenes);
    for (Scene s : scenes) {
      Element scene = doc.createElement("scene");
      xmlScenes.appendChild(scene);
      for (Panel p : s.getPanels()) {
        Element panel = doc.createElement("panel");
        scene.appendChild(panel);
        for (Position pos : p.getPosition()) {
          System.out.println(pos.getName());
          Element position = doc.createElement(pos.getName().trim());
          panel.appendChild(position);
          for (Figure f : pos.getFigures()) {
            addFigure(f, position, doc);
          }
          for (Bubble b: pos.getBubbles()) {
            Element balloon = doc.createElement("balloon");
            position.appendChild(balloon);
            balloon.setAttribute("status", b.getStatus().trim());
            Element content = doc.createElement("content");
            balloon.appendChild(content);
            content.setTextContent(b.getContent().trim());
          }
        }
        // Adding setting, below, and border
        if (!p.getSetting().equals("")) {
          Element setting = doc.createElement("setting");
          panel.appendChild(setting);
          setting.setTextContent(p.getSetting());
        }
        if (!p.getTitleBelow().equals("")) {
          Element below = doc.createElement("below");
          panel.appendChild(below);
          below.setTextContent(p.getTitleBelow());
        }
        if (!p.getBorder().equals("")) {
          Element border = doc.createElement("border");
          panel.appendChild(border);
          border.setTextContent(p.getBorder());
        }
      }
      System.out.println("shit ran");
      int i = 1;
      Element tScene = doc.createElement("scene");
      xmlScenes.appendChild(tScene);
      for (Panel p : s.getPanels()) {
        Element panel = doc.createElement("panel");
        scene.appendChild(panel);
        for (Position pos : p.getPosition()) {
          Element position = doc.createElement(pos.getName());
          panel.appendChild(position);
          for (Figure f : pos.getFigures()) {
            addFigure(f, position, doc);
          }
          for (Bubble b: pos.getBubbles()) {
            Element balloon = doc.createElement("balloon");
            position.appendChild(balloon);
            balloon.setAttribute("status", b.getStatus());
            Element content = doc.createElement("content");
            balloon.appendChild(content);
            content.setTextContent(trans.get(i));
            i += 2;
          }
        }
        // Adding setting, below, and border
        if (!p.getSetting().equals("")) {
          Element setting = doc.createElement("setting");
          panel.appendChild(setting);
          setting.setTextContent(p.getSetting());
        }
        if (!p.getTitleBelow().equals("")) {
          Element below = doc.createElement("below");
          panel.appendChild(below);
          below.setTextContent(p.getTitleBelow());
        }
        if (!p.getBorder().equals("")) {
          Element border = doc.createElement("border");
          panel.appendChild(border);
          border.setTextContent(p.getBorder());
        }
      }
      doc.appendChild(comic);
      System.out.println("Ran");
      System.out.println(doc);
      try {writeXML(doc);}
      catch (TransformerException te) {System.out.println("Error writing to file: " + te.getStackTrace());}

    }
    
  }

  /**
   * Private method to abstract adding a figure to a parent element, as it is used repeatedly
   * @param f Figure passed for basic information about the figure
   * @param parent Parent element needs to be passed in order to append the child to it
   * @param doc In order to create the figure element the document is needed
   */
  private void addFigure(Figure f, Element parent, Document doc) {
    Element figure = doc.createElement("figure");
    parent.appendChild(figure);
    String[] attributeNames = {"id", "name", "appearance", "pose", 
                               "facing", "skin", "hair", "lips"};
    String[] atrs = f.getAttributes();
    for (int i = 0; i < atrs.length; i++) {
      String atr = atrs[i];
      if (atr.equals("")) continue;             // Skip if the attribute is empty
      Element child = doc.createElement(attributeNames[i]);
      child.setTextContent(atr);
      figure.appendChild(child);
    }
  }

  private void writeXML(Document doc) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource();

    StreamResult result = new StreamResult(new File("src\\main\\resources\\translatedSpecifications.xml"));

    transformer.transform(source, result);

  }
}