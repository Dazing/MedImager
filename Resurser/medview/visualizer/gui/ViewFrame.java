/*
 * ViewFrame.java
 *
 * Created on June 26, 2002, 2:22 PM
 *
 * $Id: ViewFrame.java,v 1.5 2002/10/30 15:56:40 zachrisg Exp $
 *
 * $Log: ViewFrame.java,v $
 * Revision 1.5  2002/10/30 15:56:40  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;

/**
 * An interface for a frame that can contain a View.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface ViewFrame extends GenericFrame {

    /**
     * Returns the View contained in the frame.
     *
     * @return The View contained in the frame.
     */
    public View getView();
    
    /**
     * Sets the View of the frame.
     *
     * @param view The View to be contained in the frame.
     */
    public void setView(View view);        
    
}

