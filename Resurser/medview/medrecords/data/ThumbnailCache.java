/*
 * $Id: ThumbnailCache.java,v 1.5 2003/12/22 10:00:39 oloft Exp $
 *
 * Created on August 11, 2001, 5:10 PM
 */

package medview.medrecords.data;

import java.util.*;
import java.awt.Image;
import java.io.File;

//import medview.data.*;

/**
 * Global cache for thumbnail images.
 *
 * @author  nils
 * @version 
 */
public class ThumbnailCache extends Object {

    private static ThumbnailCache instance = new ThumbnailCache();
    private Hashtable table;
    
    public static final int THUMBNAIL_WIDTH = 110;
    public static final int THUMBNAIL_HEIGHT = 80;
    
    public static final int BUTTON_WIDTH  = 120;
    public static final int BUTTON_HEIGHT = 120; 
    
    
    /** Creates new ImageCache */
    private ThumbnailCache() {
        //int size = Config.getMaxThumbnailCount();
        table = new Hashtable();
    }
  
    public static ThumbnailCache getInstance() {
        return instance;
    }
    
        
    /*
     * Get an image, via the cache. If the image doesn't exist in the cache, it is loaded, resized and cached.
     */
    public Image getImage(String path) {
    
        if (table.containsKey(path)) {           
            Object value = table.get(path);
            if ( value instanceof Image) {
                return (Image) value;
            }
        }
        
        File f = new File(path);        
        if (f.canRead()) {

            Image img = java.awt.Toolkit.getDefaultToolkit().getImage(f.getPath());           
            Image thumbnail = img.getScaledInstance(THUMBNAIL_WIDTH,THUMBNAIL_HEIGHT,Image.SCALE_FAST);
    
            table.put(path,thumbnail);
                
            return thumbnail;
        }
        return null;
    }
}
