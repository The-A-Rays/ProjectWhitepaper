package com.project.aicomics.XML;

import java.util.ArrayList;
import java.util.List;

public class Panel {

  private final List<Position> position = new ArrayList<>();
  private String setting;
  private String titleBelow;
  private String border;
  
  @Override
    public String toString() {
        String panel = "";
        panel += "Setting: " + setting + "\n";
        for (Position p : position) {
            panel += p.toString() + "\n";
        }
        panel += "Below: " + titleBelow +"\n";
        panel += "Border: " + border + "\n";
        return panel;
    }

    public String getTitleBelow() {
        return titleBelow;
    }

    public void setTitleBelow(String titleBelow) {
        this.titleBelow = titleBelow;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

  public List<Position> getPosition() {
      return position;
  }

  public void addPosition(Position position) {
      this.position.add(position);
  }

  public String getSetting() {
      return setting;
  }

  public void setSetting(String setting) {
      this.setting = setting;
  }
}
