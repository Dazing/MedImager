/*
 * ImageViewFactory.java
 *
 * Created on October 16, 2002, 2:16 PM
 *
 * $Id: ImageViewFactory.java,v 1.1 2002/10/17 11:47:10 erichson Exp $
 *
 * $Log: ImageViewFactory.java,v $
 * Revision 1.1  2002/10/17 11:47:10  erichson
 * First check-in
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/** Factory class for ImageViews
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class ImageViewFactory implements ViewFactory {
    
    /** 
     * Creates a new instance of ImageViewFactory
     */
    public ImageViewFactory() {
    }
 
     /** 
      * Creates a new ImageViewFactory
      * @param dataSet The ExaminationDataSet to use to create the view
      * @return The new ImageView instance
      */
    public View createView(ExaminationDataSet dataSet) {
        return new ImageView(dataSet);
    }
    
}
