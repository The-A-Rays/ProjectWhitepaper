package com.project.aicomics.XML;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Panel> panels = new ArrayList<>();

    @Override
public String toString() {
    String representation = "";
    for (int i = 0; i < panels.size(); i++) {
        representation += ("Panel # " + i + ": \n");
        representation += (panels.get(i).toString());
    }
    return representation;
}

    public List<Panel> getPanels() {
        return panels;
    }

    public void addPanel(Panel panel) {
      this.panels.add(panel);
    }
  
}
