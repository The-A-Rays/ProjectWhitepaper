package com.project.aicomics.XML;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Panel> panels = new ArrayList<>();

    @Override
public String toString() {
    return "Scene{" + "panels=" + getPanels() +"}";
}

    public List<Panel> getPanels() {
        return panels;
    }

    public void addPanel(Panel panel) {
      this.panels.add(panel);
    }
  
}
