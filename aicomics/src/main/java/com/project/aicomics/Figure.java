 package com.project.aicomics;

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
        return "Figure{" + "id='" + getId() + ", name='" + getName() + ", appearance='" + getAppearance() + ", pose='" + getPose() + ", facing='" + getFacing() + ", skin='" + getSkin() + ", hair='" + getHair() + ", lips='" + getLips() + "}";
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

//     // predefined colors
//     public enum Colors{
//         LIGHT_SKIN(new Color(255,229,204)),
//         DARK_BROWN(new Color(51,25,0)),
//         BROWN(new Color(102,51,0)),
//         BLACK(Color.BLACK),
//         OLIVE(new Color(204, 204, 0)),
//         PINK(new Color(255, 0, 127)),
//         RED(Color.RED),
//         BLUE(new Color(51, 153, 255)),
//         PURPLE(new Color(254, 153, 255)),
//         BLONDE(new Color(255, 255, 153)),
//         GRAY(new Color(192, 192, 192));

//         private final Color color;
//         Colors(Color color){
//             this.color = color;
//         }
//     }


//     private Colors skinColor;
//     private Colors hairColor;
//     private Colors beardColor;
//     private Colors lips;
//     /**Boolean, 0 Female, 1 Male */
//     private Boolean appearance;
//     /**int, 0 Long, 1 Short, 2 Bald */
//     private int hairLength;


//     //constructor with default colors
//     public Figure(boolean male) {
//         this.appearance = male;
//         this.skinColor = Colors.LIGHT_SKIN;
                
//         if (appearance) { //male
//             this.beardColor = Colors.BLACK;
//             this.hairLength = 1; //short hair
//             this.hairColor = Colors.BLACK; 
//             this.lips = Colors.PINK;
//         } else { //female
//             this.beardColor = null; //no beard
//             this.hairLength = 0; //long hair
//             this.hairColor = Colors.BLONDE; 
//             this.lips = Colors.RED;
//         }
//     }


//     //setters and getters
//     public void setSkinColor(Colors color) {
//         this.skinColor = color;
//     }

//     public void setHairColor(Colors color) {
//         this.hairColor = color;
//     }

//     public void setBeardColor(Colors color) {
//         this.beardColor = color;
//     }

//     public void setHairLength(int length) {
//         this.hairLength = length;
//     }

//     public void setlips(Colors color) {
//         this.hairColor = color;
//     }

//     public Colors getSkinColor() {
//         return skinColor;
//     }

//     public Colors getHairColor() {
//         return hairColor;
//     }

//     public Colors getBeardColor() {
//         return beardColor;
//     }

//     public int getHairLength() {
//         return hairLength;
//     }

//     public Colors getLipColor() {
//         return lips;
//     }

//     public Colors setlips() {
//         return lips;
//     }

//     public boolean getAppearence(){
//         return appearance;
//     }

//     //method to print all available colors
//     public static List<Colors> getAvailableColors() {
//         return Arrays.asList(Colors.values());
//     }



// }
