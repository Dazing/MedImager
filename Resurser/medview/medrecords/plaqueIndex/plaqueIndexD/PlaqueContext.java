/*
 *  PlaqueContext.java
 *
 *  Created by Olof Torgersson on Mon Jan 05 2004.
 *
 *  $Id: PlaqueContext.java,v 1.2 2005/04/26 12:57:12 erichson Exp $.
 *
 *  $Log: PlaqueContext.java,v $
 *  Revision 1.2  2005/04/26 12:57:12  erichson
 *  Merging COPENHAGEN_DEVELOPMENT branch.
 *
 *  Revision 1.1.2.2  2005/04/25 10:37:24  erichson
 *  Removed a debug output i had left in
 *
 *  Revision 1.1.2.1  2005/04/24 10:29:45  erichson
 *  Simple fix for faceTypeForString since it would always return FACE_NONE when used in plaqueIndex // NE
 *
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
* Stores information needed to be able to write term names.
* Should be in the models of Plaque Index instead but this method
* made it possible to change term names without any major modifications to the 
* rest of the code.
*
*/

public class PlaqueContext implements Cloneable {
    
    public static final int NOT_SET = 0;
    
    // Default NOT_SET
    public static final int UPPER_JAW = 10;
    public static final int LOWER_JAW = 11;
    
    // Default NOT_SET
    public static final int PLAQUE_PART = 20;
    public static final int POCKET_PART = 21;
    public static final int ATTACH_LOSS_PART = 22;
    public static final int BLEEDING_PART = 23;
    public static final int PUS_PART = 24;
    public static final int FURCATIONS_PART = 25;
    public static final int MOBILITY_PART = 26;
    
    // Default NOT_SET
    public static final int FACIAL_ROW = 40;
    public static final int LINGUAL_ROW = 41;
    public static final int SINGLE_ROW = 42;
    
    // 5d, 5f etc
    public static final int FACE_D = 50;
    public static final int FACE_NONE = 51;
    public static final int FACE_M = 52;
    public static final int FACE_DF = 53;
    public static final int FACE_T = 54;
    public static final int FACE_MF = 55;
    public static final int FACE_DP = 56;
    public static final int FACE_F = 57;
    public static final int FACE_MP = 58;
    
    // Default NOT_SET
    public static final int LEFT_SIDE = 60;
    public static final int RIGHT_SIDE = 61;

    private int jaw;
    private int part;
    private int row;
    private int face;
    private int side;
    private int number;
    
    public PlaqueContext() {
        jaw = NOT_SET;
        part = NOT_SET;
        row = NOT_SET;
        face = NOT_SET;
        side = NOT_SET;
        number = NOT_SET;
    }
    
    /**
     * Converts "face and row" (d, m
     */
    public static int faceTypeForString(String s) {
        if (s.length() > 0) 
        {            
            //String fs = s.substring(1); // Why?? // NE
            String fs = s;                                              
            
            if (fs.equals("d")) {
                return FACE_D;
            }
            else if (fs.equals("")) {
                return FACE_NONE;
            }
            else if (fs.equals("m")) {
                return FACE_M;
            }
            else if (fs.equals("df")) {
                return FACE_DF;
            }
            else if (fs.equals("t")) {
                return FACE_T;
            }
            else if(fs.equals("mf")) {
                return FACE_MF;
            }
            else if (fs.equals("dp")) {
                return FACE_DP;
            }
            else if (fs.equals("f")) {
                return FACE_F;
            }
            else if(fs.equals("mp")) {
                return FACE_MP;
            }
            
        }
        return 0;
    }
    
    public int getJaw() {
        return jaw;
    }
    
    public void setJaw(int j) {
        jaw = j;
    }
    
    public int getPart() {
        return part;
    }
    
    public void setPart(int p) {
        part = p;
    }
    
    public int getRow() {
        return row;
    }
    
    public void setRow(int r) {
        row = r;
    }
    
    public int getFace() {
        return face;
    }
    
    public void setFace(int f) {
        face = f;
    }
    
    public int getSide() {
        return side;
    }
    
    public void setSide(int s) {
        side = s;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int n) {
        number = n;
    }
    
    public void setTooth(int n) {
        if (n < 7) {
            setSide(LEFT_SIDE);
        }
        else {
            setSide(RIGHT_SIDE);
        }
        switch (n) {
            case 0:
                number = 7;
                break;
            case 1:
                number = 6;
                break;
            case 2:
                number = 5;
                break;
            case 3:
                number = 4;
                break;
            case 4:
                number = 3;
                break;
            case 5:
                number = 2;
                break;
            case 6:
                number = 1;
                break;
            case 7:
                number = 1;
                break;
            case 8:
                number = 2;
                break;
            case 9:
                number = 3;
                break;
            case 10:
                number = 4;
                break;
            case 11:
                number = 5;
                break;
            case 12:
                number = 6;
                break;
            case 13:
                number = 7;
                break;
            default:
                number = 0;
        }
    }
    
    public PlaqueContext copy() {
        try {
            PlaqueContext cp = (PlaqueContext)this.clone();
            return cp;
        }
        catch (Exception e) {
            e.printStackTrace();    // Should not happen
            return null;
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public String toString() {
        return "" + jaw + ":" + part + ":" + row + ":" + face + ":" + side + ":" + number;
    }
    
    public String representedTermName() {
        return termNameMJ();
    }
    
    
    // Term name according to Mats, 04 01 03
    private String  termNameMJ() {
        String faceName;
                    
        faceName = "FACE";

        return getPartName()    // Plaque/Pocket/Attach-loss etc
          + getToothName()      // Tooth notation: 7+, -6 etc
          + getFaceName()       // which part of the tooth: d, m, df etc
          + getRowMarker();     // f if facial, p if lingual
    }
    
    private String getPartName() {
        String partName;

        switch (part) {
            case PLAQUE_PART:
                partName = "Plaque";
                break;
            case POCKET_PART:
                partName = "Pocket";
                break;
            case ATTACH_LOSS_PART:
                partName = "Attach-loss";
                break;
            case BLEEDING_PART:
                partName = "Bleeding";
                break;
            case PUS_PART:
                partName = "Pus";
                break;
            case FURCATIONS_PART:
                partName = "Furcations";
                break;
            case MOBILITY_PART:
                partName = "Mobility";
                break;
            default:
                partName = "Error";
        }
        return partName;
    }
    
    /**
     * Gets tooth name. 
     * Upper jaw: 7+ for left side, +7 for right side
     * Lower jaw: 7- for left side, -7 for right side // NE
     */
    private String getToothName() 
    {
        String toothName;
        
        String toothSign = (jaw == UPPER_JAW) ? "+" : "-";
        
        if (side == LEFT_SIDE) {
            toothName = number + toothSign;
        }
        else {
            toothName = toothSign + number;
        }
        return toothName;
    }
    
    private String getFaceName() {
        switch (face) {
            case FACE_D:
                return "d";
            case FACE_NONE:
                return "";
            case FACE_M:
                return "m";
            case FACE_DF:
                return "df";
            case FACE_T:
                return "t";
            case FACE_MF:
                return "mf";
            case FACE_DP:
                return "dp";
            case FACE_F:
                return "f";
            case FACE_MP:
                return "mp";
        }
        return "";
    }
    
    private String getRowMarker() {
        switch (row) {
            case FACIAL_ROW:
                return "f";
            case LINGUAL_ROW:
                return "p";
        }
        return "";
    }
    
}
