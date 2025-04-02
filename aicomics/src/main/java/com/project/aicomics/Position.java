package com.project.aicomics;

import java.util.ArrayList;
import java.util.List;

public class Position {
  private List<String> name;
  private List<Figure> figures = new ArrayList<>();
  private List<Bubble> bubbles = new ArrayList<>();

  public List<String> getName(){
    return name;
  }

  public void setName(String name){
    this.name.add(name);
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
