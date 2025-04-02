package com.project.aicomics;

import java.util.List;

public class Panel {

  private List<Position> position;
  private String setting;
  private String titleBelow;
  private String border;

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
