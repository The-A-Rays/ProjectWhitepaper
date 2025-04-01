package com.project.aicomics;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReadXMLFileTests {
    private static Document document;

    @BeforeAll
    public static void setUp() throws Exception {
        try (InputStream inputStream = ReadXMLFile.class.getClassLoader().getResourceAsStream("specification.xml")) {
            if (inputStream == null) {
                throw new IllegalStateException("File not found: specification.xml");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
        }
    }

    @Test
    public void figuresHaveValidAppearance() {
        NodeList figures = document.getElementsByTagName("figure");

        for (int i = 0; i < figures.getLength(); i++) {
            Element figure = (Element) figures.item(i);
            String appearance = figure.getElementsByTagName("appearance").item(0).getTextContent();
            assertTrue(appearance.equals("male") || appearance.equals("female"));
        }
    }

    @Test
    public void figuresHaveValidFacing() {
        NodeList figures = document.getElementsByTagName("figure");

        for (int i = 0; i < figures.getLength(); i++) {
            Element figure = (Element) figures.item(i);
            String facing = figure.getElementsByTagName("facing").item(0).getTextContent();
            assertTrue(facing.equals("left") || facing.equals("right"));
        }
    }

    @Test
    public void panelsHaveAtLeastOneFigure() {
        NodeList panels = document.getElementsByTagName("panel");

        for (int i = 0; i < panels.getLength(); i++) {
            Element panel = (Element) panels.item(i);
            NodeList figures = panel.getElementsByTagName("figure");
            assertTrue(figures.getLength() > 0); //ecah panel should have at least one figure
        }
    }

    @Test
    public void speechBubbleHasText() {
        NodeList balloons = document.getElementsByTagName("balloon");

        for (int i = 0; i < balloons.getLength(); i++) {
            Element balloon = (Element) balloons.item(i);
            assertTrue(balloon.hasAttribute("status")); //speech bubble needs to have a status 

            NodeList contentList = balloon.getElementsByTagName("content");
            assertTrue(contentList.getLength() > 0 && !contentList.item(0).getTextContent().isEmpty());//speech bubbles should not be empty; tehre need to be words inside
        }
    }

    @Test
    public void panelsHaveASetting() {
        NodeList panels = document.getElementsByTagName("panel");

        for (int i = 0; i < panels.getLength(); i++) {
            Element panel = (Element) panels.item(i);
            NodeList settingList = panel.getElementsByTagName("setting");

            if (settingList.getLength() > 0) {
                String setting = settingList.item(0).getTextContent();
                assertFalse(setting.isBlank());//there needs to be a settign for each panel
            }
        }
    }
  }
    

