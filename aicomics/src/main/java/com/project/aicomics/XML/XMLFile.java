package com.project.aicomics.XML;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.project.aicomics.vignette.Vignette;
import com.project.aicomics.vignette.VignetteFileReader;
import com.project.aicomics.vignette.VignetteManager;

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
     *
     * @return a scene containing two panels with only one speech bubble in each. "left" is for when there is one figure in the panel.
     */
  public Scene getLeftScene() throws IOException{
    Translations translator = new Translations(Language.english, Language.spanish);
    VignetteManager vm = new VignetteManager(VignetteFileReader.readSchemas("English.tsv", 40), translator);
    Random rand = new Random();
		Vignette vignette = vm.getVignette(rand.nextInt(39)); 

    Scene scene = new Scene();
    Panel panel = new Panel();

		List<Figure> figures = this.getFigures();

    Bubble leftBubble = new Bubble();
    
		leftBubble.setContent(vignette.getLeftText());
    
		leftBubble.setStatus("speech");

    panel.setSetting(vignette.getBackground());
		Figure figOne = figures.get(0);
		
    figOne.setPose(vignette.getLeftPose());
    
		Position left = new Position();
		
		left.setName("left");
		
		left.setFigure(figOne);

		left.setBubble(leftBubble);
		panel.addPosition(left);

		scene.addPanel(panel);

    // add a second identical panel except with the dialogue trnaslated then abstract these two methods

    Panel panelTwo = new Panel();
    Bubble leftBubbleTranslated = new Bubble();

    leftBubbleTranslated.setContent(vignette.getTranslatedText());
    panelTwo.setSetting(vignette.getBackground());
    System.out.println(vignette.getTranslatedText());
    Position leftTwo = new Position();

    leftTwo.setName("left");
		leftTwo.setFigure(figOne);
    leftTwo.setBubble(leftBubbleTranslated);

    panelTwo.addPosition(leftTwo);

    scene.addPanel(panelTwo);
    
    System.out.println(scene.toString());
    return scene;

  }

  /**
     *
     * @return a scene containing two panels with only one speech bubble in each. "whole" is for when there is two figures in the panel.
     */
  public Scene getWholeScene() throws IOException{
    Translations translator = new Translations(Language.english, Language.spanish);
    VignetteManager vm = new VignetteManager(VignetteFileReader.readSchemas("English.tsv", 40), translator);
    Random rand = new Random();
		Vignette vignette = vm.getVignette(rand.nextInt(39)); 

    Scene scene = new Scene();
    Panel panel = new Panel();

		List<Figure> figures = this.getFigures();

    Bubble bubble = new Bubble();
    
		bubble.setContent(vignette.getCombinedText());

		bubble.setStatus("speech");

    panel.setSetting(vignette.getBackground());
		Figure figOne = figures.get(0);
		Figure figTwo = figures.get(1);
    figOne.setPose(vignette.getLeftPose());
    figTwo.setPose(vignette.getRightPose());
		Position left = new Position();
    
		Position right = new Position();
		left.setName("left");
		right.setName("right");
		left.setFigure(figOne);
		right.setFigure(figTwo);
    left.setBubble(bubble);
		panel.addPosition(left);
		panel.addPosition(right);

		scene.addPanel(panel);

    // add a second identical panel except with the dialogue trnaslated then abstract these two methods

    Panel panelTwo = new Panel();
    panel.setSetting(vignette.getBackground());
    Bubble bubbleTranslated = new Bubble();

    bubbleTranslated.setContent(vignette.getTranslatedText());
    Position leftTwo = new Position();
    Position rightTwo = new Position();
    rightTwo.setName("right");
    rightTwo.setFigure(figTwo);

    leftTwo.setName("left");
		leftTwo.setFigure(figOne);
    leftTwo.setBubble(bubbleTranslated);

    panelTwo.addPosition(leftTwo);
		panelTwo.addPosition(rightTwo);
    scene.addPanel(panelTwo);

    System.out.println(scene.toString());
    return scene;

  }
}