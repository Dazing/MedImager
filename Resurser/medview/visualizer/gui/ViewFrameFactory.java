/*
 * ViewFrameFactory.java
 *
 * Created on July 1, 2002, 3:05 PM
 *
 * $Id: ViewFrameFactory.java,v 1.3 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: ViewFrameFactory.java,v $
 * Revision 1.3  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.2  2002/10/30 15:56:41  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import javax.swing.*;

/**
 * A factory class for creating ViewFrames.
 *
 * @author  Göran Zachrisson
 */
public class ViewFrameFactory {
    
    /** 
     * Private constructor. No need to create an instance of it. 
     */
    private ViewFrameFactory() {
    }
    
    /**
     * Creates a ViewFrame using a JFrame from a View.
     *
     * @param view The View that the ViewFrame should contain.
     * @return A ViewFrame.
     */
    public static ViewFrame createViewJFrame(View view) {
        return new ViewJFrame(view);
    }
    
    /**
     * Creates a ViewFrame using a JInternalFrame from a View.
     *
     * @param view The View that the ViewFrame should contain.
     * @return A ViewFrame.
     */    
    public static ViewFrame createViewJInternalFrame(View view) {
        return new ViewJInternalFrame(view);
    }
}
