package com.project.aicomics.XML;

public class Position {
  private String name;
  private Figure figure;
  private Bubble bubble;

  @Override
  public String toString() {
    String pos = "";
    pos += name + ": \n";
    pos += "Figure: " + figure.toString() + "\n";
    if (bubble != null) pos += "Speech bubble:" + bubble.toString();
    return pos;
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public Figure getFigure() {
    return figure;
}

public void setFigure(Figure f) {
    figure = f;
}

public Bubble getBubble() {
    return bubble;
}

public void setBubble(Bubble b) {
    bubble = b;
}
  
}
