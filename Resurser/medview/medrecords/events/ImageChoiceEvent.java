/*
 * ImageChoiceEvent.java
 *
 * Created on den 14 augusti 2003, 23:16
 *
 * $Id: ImageChoiceEvent.java,v 1.3 2003/11/11 14:30:05 oloft Exp $
 *
 */

package medview.medrecords.events;

import medview.medrecords.components.inputs.PictureChooserInput;

/**
 *
 * @author  nix
 */
public class ImageChoiceEvent {
    
    private final String imagePath;
    private final PictureChooserInput pictureChooser;
    
    /** Creates a new instance of ImageChoiceEvent */
    public ImageChoiceEvent(PictureChooserInput input, String imagePath) {
        this.pictureChooser = input;
        this.imagePath = imagePath;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public PictureChooserInput getPictureChooser() {
        return pictureChooser;
    }
    
}
