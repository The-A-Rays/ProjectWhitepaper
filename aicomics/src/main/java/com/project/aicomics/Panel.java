package com.project.aicomics;

import java.util.ArrayList;
import java.util.List;

public class Panel {

  private String position;
  private List<Figure> figures = new ArrayList<>();
  private List<Bubble> bubbles = new ArrayList<>();
  private String setting;

  public String getPosition() {
      return position;
  }

  public void setPosition(String position) {
      this.position = position;
  }

  public List<Figure> getFigures() {
      return figures;
  }

  public void setFigures(List<Figure> figures) {
      this.figures = figures;
  }

  public List<Bubble> getBubbles() {
      return bubbles;
  }

  public void setBubbles(List<Bubble> bubbles) {
      this.bubbles = bubbles;
  }

  public String getSetting() {
      return setting;
  }

  public void setSetting(String setting) {
      this.setting = setting;
  }
}
