/*
 * Floater.java
 *
 * Created on July 23, 2002, 10:05 AM
 *
 * $Id: Floater.java,v 1.6 2003/07/02 00:44:36 erichson Exp $
 *
 * $Log: Floater.java,v $
 * Revision 1.6  2003/07/02 00:44:36  erichson
 * Removed setName() method.
 *
 * Revision 1.5  2003/07/02 00:41:15  erichson
 * Large rewrite! deprecated individual methods and added general methods createFloater().
 * Added type constants.
 *
 * Revision 1.4  2002/11/08 15:50:18  erichson
 * added addFloaterListener and removeFloaterListener
 *
 * Revision 1.3  2002/10/31 14:50:58  zachrisg
 * Minor changes needed for settings.
 *
 * Revision 1.2  2002/10/17 11:40:39  erichson
 * added isVisible() to the interface
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import javax.swing.*;

import medview.visualizer.event.FloaterListener;

/**
 * An interface for floater windows such as toolboxes.
 *
 * Must be interface since FreeFloater and InternalFloater cannot inherit from Floater, since
 * FreeFloater extends JFrame and InternalFloater extends JInternalFrame.
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface Floater { // must be interface - see above
               
    public static final int FLOATER_TYPE_MESSAGES = 1;
    public static final int FLOATER_TYPE_DATAGROUPS = 2;
    public static final int FLOATER_TYPE_QUERY = 3;
    public static final int FLOATER_TYPE_TOOLBOX = 4;    
    
    /** 
     * Sets the component of the floater.
     *
     * @param component The component.
     */
    public void setComponent(JComponent component);

    public void addFloaterListener(FloaterListener fl);
    
    public void removeFloaterListener(FloaterListener fl);
    
    /**
     * Returns the name of the floater.
     *
     * @return The name of the floater.
     * 
     */
    public String getName();
    
    /**
     * Returns true if the floater is visible.
     *
     * @return True if the floater is visible.
     */
    public boolean isVisible();
    
    /**
     * Sets whether the floater should be visible or not.
     *
     * @param visible True if the floater should be visible.
     */
    public void setVisible(boolean visible);
    
    /**
     * Close the floater and dispose all of its belongings.
     */
    public void closeFloater();
    
    /**
     * Returns the size of the floater.
     *
     * @return The floater's size.
     */
    public Dimension getSize();
    
    /**
     * Returns the location of the floater.
     *
     * @return The floater's location.
     */
    public Point getLocation();
    
    /**
     * Returns the location of the floater in the screen's coordinate space.
     *
     * @return The floater's location in the screen's coordinate space.
     */
    public Point getLocationOnScreen();
        
    /**
     * Sets the floater's location in its parent's coordinate space.
     *
     * @param p The new location.
     */
    public void setLocation(Point p);
    
    /**
     * Sets the floater's size.
     *
     * @param d The new size.
     */
    public void setSize(Dimension d);        
    
    /**
     * Packs the floater.
     */
    public void pack();
    
}
