package com.project.aicomics;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Panel> panels = new ArrayList<>();

    public List<Panel> getPanels() {
        return panels;
    }

    public void addPanel(Panel panel) {
      this.panels.add(panel);
    }
  
}
