package com.project.aicomics.XML;

import java.util.ArrayList;
import java.util.List;

public class Position {
  private String name;
  private List<Figure> figures = new ArrayList<>();
  private List<Bubble> bubbles = new ArrayList<>();

  @Override
  public String toString() {
      return "Position{" +"name='" + getName() + ", figures=" + getFigures() +", bubbles=" + getBubbles() + "}";
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
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
  
}
