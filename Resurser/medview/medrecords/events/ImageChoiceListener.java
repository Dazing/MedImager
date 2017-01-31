/*
 * ImageChoiceListener.java
 *
 * Created on den 14 augusti 2003, 23:19
 * 
 * $Id: ImageChoiceListener.java,v 1.2 2003/11/11 14:28:18 oloft Exp $
 *
 * $Log: ImageChoiceListener.java,v $
 * Revision 1.2  2003/11/11 14:28:18  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.1  2003/08/16 14:44:10  erichson
 * New events for MedRecords package.
 *
 */

package medview.medrecords.events;

/**
 * Listener for events fired when an image is chosen
 * @author  nix
 */
public interface ImageChoiceListener {
    
    /** Creates a new instance of ImageChoiceListener */
    public void imageChosen(ImageChoiceEvent ice);        
}
