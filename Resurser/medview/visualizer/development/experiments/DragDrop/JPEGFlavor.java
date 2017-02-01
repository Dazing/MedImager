package medview.visualizer.development.experiments.DragDrop;
/*
 * JPEGflavor.java
 *
 * Created on June 19, 2002, 11:24 AM
 */

/**
 *
 * @author  d97nix
 * @version 
 */

import java.awt.datatransfer.*;

public class JPEGFlavor extends DataFlavor{

    private static JPEGFlavor instance;
    
    /** Creates new JPEGflavor */
    private JPEGFlavor() {
        super("image/jpeg","JPEG image");
    }

    public static JPEGFlavor getInstance() {
        if (instance == null)
            instance = new JPEGFlavor();
        return instance;
    }
    
}
