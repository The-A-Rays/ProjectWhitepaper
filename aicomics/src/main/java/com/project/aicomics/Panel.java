package com.project.aicomics;

import java.util.ArrayList;
import java.util.List;

public class Panel {

  private List<String> position;
  private List<Figure> figures = new ArrayList<>();
  private List<Bubble> bubbles = new ArrayList<>();
  private String setting;
  private String titleBelow;
  private String titleAbove;
  private String border;

    public String getTitleBelow() {
        return titleBelow;
    }

    public void setTitleBelow(String titleBelow) {
        this.titleBelow = titleBelow;
    }

    public String getTitleAbove() {
        return titleAbove;
    }

    public void setTitleAbove(String titleAbove) {
        this.titleAbove = titleAbove;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

  public List<String> getPosition() {
      return position;
  }

  public void addPosition(String position) {
      this.position.add(position);
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
