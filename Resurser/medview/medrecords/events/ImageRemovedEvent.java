//
//  ImageRemovedEvent.java
//
//  Created by Olof Torgersson on Fri Oct 24 2003.
//  $Id: ImageRemovedEvent.java,v 1.2 2003/11/11 14:28:18 oloft Exp $
//

package medview.medrecords.events;

import medview.medrecords.components.PhotoPanel;

public class ImageRemovedEvent extends java.util.EventObject {

    private final PhotoPanel photoPanel;
    private final String imagePath;

    public ImageRemovedEvent(PhotoPanel panel, String path) {
        super(panel);
        photoPanel = panel;
        imagePath = path;
    }

    public PhotoPanel getPhotoPanel() {
        return photoPanel;
    }

    public String getImagePath() {
        return imagePath;
    }

}
