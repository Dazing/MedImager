/*
 * ViewFrame.java
 *
 * Created on June 26, 2002, 2:22 PM
 *
 * $Id: GenericFrame.java,v 1.2 2002/10/14 15:13:33 erichson Exp $
 * 
 * $Log: GenericFrame.java,v $
 * Revision 1.2  2002/10/14 15:13:33  erichson
 * Added toFront(), toBack(), requestFocus() // NE
 *
 *
 */

package medview.visualizer.gui;
import java.awt.*;

/**
 * An interface for a frame 
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 * @version 1.0
 */
public interface GenericFrame {

    
    /**
     * Sets whether the frame should be visible or not.
     * @param visible 
     */
    public void setVisible(boolean visible);
    
    /**
     * Close the frame and dispose all of its belongings.
     */
    public void closeFrame();
    
    /**
     * Returns the size of the frame.
     * @return The frame's size.
     */
    public Dimension getSize();
    
    /**
     * Returns the location of the frame.
     * @return The frame's location.
     */
    public Point getLocation();
    
    /**
     * Returns the location of the frame in the screen's coordinate space.
     * @return The frame's location in the screen's coordinate space.
     */
    public Point getLocationOnScreen();
        
    /**
     * Sets the frame's location in its parent's coordinate space.
     * @param p The new location.
     */
    public void setLocation(Point p);
    
    /**
     * Sets the frame's size.
     * @param d The new size.
     */
    public void setSize(Dimension d);
    
    /**
     * Sets the frame's title
     * @param newTitle the frame's new title
     */
    public void setTitle(String newTitle);
    
    /**
     * Bring the frame to the front
     * @see java.awt.Window#toFront()
     */
    public void toFront();
        
    /**
     * Bring the frame to the back
     * @see java.awt.Window#toBack()
     */ 
    public void toBack();
    
    /** 
     * Request focus for this frame
     * @see java.awt.Component#requestFocus()
     */
    public void requestFocus();
    
    /** 
     * Returns the frame's current title 
     */
    public String getTitle();
    /*
    public void addFrameListener(FrameListener fl);
    
    public void removeFrameListener(FrameListener fl);
     */
}

