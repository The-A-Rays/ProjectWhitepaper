 package com.project.aicomics.XML;

import org.w3c.dom.Element;

public class Figure {
     private String id, name, appearance, pose, facing, skin, hair, lips;
  
  public Figure(Element element) {
      this.id = XMLFile.getTagVal("id", element);
      this.name = XMLFile.getTagVal("name", element);
      this.appearance = XMLFile.getTagVal("appearance", element);
      this.pose = XMLFile.getTagVal("pose", element);
      this.facing = XMLFile.getTagVal("facing", element);
      this.skin = XMLFile.getTagVal("skin", element);
      this.hair = XMLFile.getTagVal("hair", element);
      this.lips = XMLFile.getTagVal("lips", element);
  }

  @Override
    public String toString() {
        return "Figure{" + "id= " + getId() + ", name= " + getName() + ", appearance= " + getAppearance() + ", pose= " + getPose() + ", facing= " + getFacing() + ", skin= " + getSkin() + ", hair= " + getHair() + ", lips= " + getLips() + "}";
    }


    public String[] getAttributes() {
        String[] attr = new String[8];
        attr[0] = id;
        attr[1] = name;
        attr[2] = appearance;
        attr[3] = skin;
        attr[4] = hair;
        attr[5] = lips;
        attr[6] = pose;
        attr[7] = facing;
        return attr;
    }

    @Override
    public boolean equals(Object obj) {
        Figure f;
        if (obj.getClass() == this.getClass()) {
            f = (Figure) obj;
        } else return false;
        String[] otherAtr = f.getAttributes();
        String[] atr = getAttributes();
        for (int i = 0; i < atr.length - 2; i++) {
            if (atr[i] == null && otherAtr[i] == null) {} 
            else if (atr[i] == null && otherAtr[i] != null) return false;
            else if (atr[i] != null && otherAtr[i] == null) return false;
            else if (!atr[i].equals(otherAtr[i])) return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getPose() {
        return pose;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getLips() {
        return lips;
    }

    public void setLips(String lips) {
        this.lips = lips;
    }
  
 }
