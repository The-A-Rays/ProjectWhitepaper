package com.project.aicomics.XML;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
  protected List<String> allText;

  public XMLFile(String fileName) {
    if (!fileName.endsWith(".xml")) fileName += ".xml";
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

  public final void readXML(String file) {
    try (InputStream inputStream = XMLFile.class.getClassLoader().getResourceAsStream(file)) {
        if (inputStream == null) {
            throw new IOException("File not found: " + file);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        doc.getDocumentElement().normalize();

        NodeList figuersNodes = doc.getElementsByTagName("figures");
        this.figures = parseFigures((Element)figuersNodes.item(0));
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
        figures.add(new Figure((Element) node)); 
      }
  }
  return figures;
}

    /**
     * 
     * @param elem the element from which we want to extract the speeches from
     * @return a list containing all the speech bubbles' content
     */ //extracts bubbles from different positions
     private static Bubble parseBubbles(Element elem){
      Bubble bubble = new Bubble();
      NodeList bubbleNodes = elem.getElementsByTagName("balloon"); 
      Node bubbleN = bubbleNodes.item(0);
      if (bubbleN == null) return null;
      if (bubbleN.getNodeType() == Node.ELEMENT_NODE) {
        Element bubbleElem = (Element) bubbleN;
        bubble.setContent(getTagVal("content", bubbleElem)); //gets content
        bubble.setStatus(bubbleElem.getAttribute("status")); //gets status
      }
      return bubble;
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
                        position.setBubble(parseBubbles(positionElem));
                        panel.addPosition(position);
                        }
                    case "right" -> {
                      Position position = new Position();
                        position.setName("right");
                        position.setFigure(parseFigures(positionElem).get(0));
                        position.setBubble(parseBubbles(positionElem));
                        panel.addPosition(position);
                        }
                    case "middle" -> {
                      Position position = new Position();
                        position.setName("middle");
                        position.setFigure(parseFigures(positionElem).get(0));
                        position.setBubble(parseBubbles(positionElem));
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
            if(pos.getBubble() != null && pos.getBubble().getContent() != null){
              spokenText.add(translate.getTranslation(pos.getBubble().getContent()));
            }
        }
      }
    }
    translatedText = spokenText;
    return spokenText;
  }

  public List<String> getSourceText() {
    List<String> sourceText = new ArrayList<>();

    if (scenes == null) {
      System.out.println("No scenes available.");
      return sourceText;
    }
    for (Scene scene : scenes) {
      for (Panel panel : scene.getPanels()) {
        for(Position pos: panel.getPosition()){
          if (pos.getBubble() == null) continue;
          sourceText.add(pos.getBubble().getContent());
        }
      }
    }
    return sourceText;
  }

  public List<String> getAllText(Language lan) {
    List<String> spokenText = new ArrayList<>();
    Translations translate = new Translations(Language.english, lan);
    if (allText != null) return allText;

    if (scenes == null) {
        System.out.println("No scenes available.");
        return spokenText;
    }
    for (Scene scene : scenes) {
      for (Panel panel : scene.getPanels()) {
        for(Position pos: panel.getPosition()){
          if (pos.getBubble() == null) continue;
          spokenText.add(pos.getBubble().getContent());
          spokenText.add(translate.getTranslation(pos.getBubble().getContent()));
        }
      }
    }
    allText = spokenText;
    return spokenText;
  }

  public List<Scene> getScenes() {
    return scenes;
  }

  public List<Figure> getFigures() {
    return figures;
  }

  public Scene getRandScene(){
    Scene randScene = null;
    Random rand = new Random();
    int val = rand.nextInt(scenes.size());
    for (int i = 0; i < scenes.size(); i++){
      if (i == val){
        randScene = this.scenes.get(i);
      }
    }
    // System.out.println("random = " + val);
    // System.out.println(randScene.toString());
    return randScene;
  }

  /**
   * Creates a new XML formatted file in src\main\resources
   * @param doc Document containing the elements to be put in file
   * @param fileName String filename or folder location ! MUST BE IN src\main\resources !
   *  ! DO NOT INCLUDE '.xml' IT SHOULD JUST BE THE NAME OF THE FILE !
   * @throws TransformerException
   */
  public static void writeXML(Document doc, String fileName) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    if (!fileName.endsWith(".xml")) fileName += ".xml";

    // pretty print XML
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(fileName));

    transformer.transform(source, result);
  }

  public static Document createDocument() throws ParserConfigurationException{
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    builder = fac.newDocumentBuilder();
		return builder.newDocument();
  }

  /**
     * Converts a Scene object into an Element to be added to a document
     * @param doc Document to create the Element with
     * @param s Scene to be converted to Element
     */
    public static Element convertScene(Document doc, Scene s) {
      Element scene = doc.createElement("scene");
      for (Panel p : s.getPanels()) {
        //english panel first, then insert translated one
        Element panel = doc.createElement("panel");
        scene.appendChild(panel);
        for (Position pos : p.getPosition()) {
            Element position = doc.createElement(pos.getName().trim());
            panel.appendChild(position);
            addFigure(pos.getFigure(), position, doc);
            if(pos.getBubble() != null){
                Bubble b = pos.getBubble();
                Element balloon = doc.createElement("balloon");
                position.appendChild(balloon);
                balloon.setAttribute("status", b.getStatus());
                Element content = doc.createElement("content");
                balloon.appendChild(content);
                content.setTextContent(b.getContent().trim());
            }
            
        }
      }
      return scene;
  }

  

  /**
     * Method to abstract adding a figure to a parent element, as it is used repeatedly
     * @param f Figure passed for basic information about the figure
     * @param parent Parent element needs to be passed in order to append the child to it
     * @param doc In order to create the figure element the document is needed
     */
    public static void addFigure(Figure f, Element parent, Document doc) {
      Element figure = doc.createElement("figure");
      parent.appendChild(figure);
      String[] attributeNames = {"id", "name", "appearance", "skin",
                              "hair", "lips", "pose", "facing"};
      String[] atrs = f.getAttributes();
      for (int i = 0; i < atrs.length; i++) {
      String atr = atrs[i];
      if (atr == null) continue;             // Skip if the attribute is empty
      Element child = doc.createElement(attributeNames[i]);
      child.setTextContent(atr);
      figure.appendChild(child);
      }
  }

    /**
     * Method to abstract adding a figure to a parent element, as it is used repeatedly
     * @param f Figure passed for basic information about the figure
     * @param parent Parent element needs to be passed in order to append the child to it
     * @param doc In order to create the figure element the document is needed
     * @param excludeExtras If true, the method will exclude the "pose" and "facing" attributes
     */
    public static void addFigure(Figure f, Element parent, Document doc, Boolean excludeExtras) {
      Element figure = doc.createElement("figure");
      parent.appendChild(figure);
      String[] attributeNames = {"id", "name", "appearance", "skin",
                              "hair", "lips", "pose", "facing"};
      String[] atrs = f.getAttributes();
      int size;
      if (excludeExtras) size = atrs.length - 2;
      else size = atrs.length;
      for (int i = 0; i < size; i++) {
          String atr = atrs[i];
          if (atr == null) continue;             // Skip if the attribute is empty
          Element child = doc.createElement(attributeNames[i]);
          child.setTextContent(atr);
          figure.appendChild(child);
      }
  }
}