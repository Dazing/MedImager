/*
 * $Id: MRConst.java,v 1.7 2010/06/22 14:41:39 oloft Exp $
 *
 * Created on June 19, 2001, 1:01 AM
 *
 * $Log: MRConst.java,v $
 * Revision 1.7  2010/06/22 14:41:39  oloft
 * Added constants for line widths
 *
 * Revision 1.6  2005/07/18 13:29:11  erichson
 * Changed picDuration from string to int // NE
 *
 */

package medview.medrecords.data;

/**
 *
 * Defines a number of constant values for use in MedRecords.
 * @author oloft
 */

public class MRConst extends Object {
    
    public static final boolean debug = false;
    public static final int LIST_CURRENT = 1;
    public static final int LIST_NEXT = 2;
    public static final String configFileName = "medview.cfg";
    public static final int FIELD_LENGTH = 44; // Default length for text fields
    public static final boolean debugColor = false;
    
    public static final String MrConstDbForestDir = "Forest.forest";
    public static final String MrConstDbPicturesDir = "Pictures" + java.io.File.separator + "Pictures";
    public static final String MrConstDbBackGrounDir = "BackgroundData";
    public static final String MrConstDbMVDDir = "Mvd.mvd";
    public static final String MrConstImageDir = "Images";
    
    
    public static final int MrConstPicDuration = 365;
    public static final String MrConstPicNumber = "16";
    public static final String MrConstPicCatName = "Images";
    public static final String MrConstPicNodName = "Photo";
    public static final String MrConstPlaque = "true";
    public static final String MrConstKeepPrefix = "false";
    public static final String MrConstIsDocumentModeOn = "false";
    
    public static final String MrConstTemplate = "";
    public static final String MrConstPreviewTemplate = "";
    public static final String MrConstTraslator = "";
    public static final String MrConstValuesFile = "";
    public static final String MrConstTermsfile = "";

    public static final String MrConstLanguage = "English";
    
    public static final int LINE_WIDTH = 106; // Default width for lines in inputs and comments, based on nbr of characters in a not very wide window
	public static final int BOLD_LINE_WIDTH = 96; // Default width for bold lines in inputs and comments, based on nbr of characters in a not very wide window
}
