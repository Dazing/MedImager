/*
 * FreeFloater.java
 *
 * Created on July 23, 2002, 10:22 AM
 *
 * $Id: FreeFloater.java,v 1.8 2002/11/08 15:51:44 erichson Exp $
 * 
 * $Log: FreeFloater.java,v $
 * Revision 1.8  2002/11/08 15:51:44  erichson
 * added FloaterListener methods, changed default close operation to DO_NOTHING, put setVisible(false) in floaterClosing method
 *
 * Revision 1.7  2002/10/31 14:50:57  zachrisg
 * Minor changes needed for settings.
 *
 * Revision 1.6  2002/10/17 14:40:07  zachrisg
 * Changed floater constructors to take "resizable" as a parameter.
 *
 * Revision 1.5  2002/10/17 11:41:19  erichson
 * set Focusable to false, windowClosing now calls closeFrame()
 *
 */

package medview.visualizer.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import medview.visualizer.event.*;

/**
 * A floater factory for floaters using JFrame. 
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class FreeFloater extends JFrame implements Floater {
    
    /** The name of the floater. */
    private String name;
 
    /** The component listener. */
    private ComponentListener componentListener = null;
    
    /** A reference to the floater. */
    private FreeFloater selfReference;
    
    /** Set of FloaterListeners */
    private HashSet floaterListeners = new HashSet();
    
    /** 
     * Creates a new instance of FreeFloater.
     *
     * @param name The name of the floater.
     * @param title The title of the floater window.
     * @param location The initial location of the floater.
     * @param resizable True if the floater should be resizable.
     */
    public FreeFloater(String name, String title, Point location, boolean resizable) {
        super(title);
        this.setResizable(resizable);
        this.name = name;
        selfReference = this;
        setLocation(location);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                floaterClosing();
            }});
        setFocusableWindowState(false); // 
        setFocusable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    
    public void addFloaterListener(FloaterListener fl) {
        floaterListeners.add(fl);
    }
    
    public void removeFloaterListener(FloaterListener fl) {
        floaterListeners.remove(fl);
    }
    
    
    /** 
     * Close the floater and dispose all of its belongings.
     */
    public void closeFloater() {
        // remove component listeners
        if (componentListener != null) {
            Component[] components = this.getContentPane().getComponents();
            for (int i = 0; i < components.length; i++) {
                components[i].removeComponentListener(componentListener);
            }
        }
        setVisible(false);
        dispose();
    }

    /**
     * Called when the floater is closed by the user.
     * This should hide the floater but not dispose it
     */
    private void floaterClosing() {
        setVisible(false);
        for (Iterator it = floaterListeners.iterator(); it.hasNext();) {
            ((FloaterListener) it.next()).floaterClosing(new FloaterEvent(this));
        }
    }
    
    /**
     * Returns the name of the floater.
     *
     * @return The name of the floater.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the floater.
     *
     * @param name The new name of the floater.
     */
    public void setName(String name) {
        this.name = name;
    }    
    
    /**
     * Sets the component of the floater.
     *
     * @param component The component.
     */
    public void setComponent(JComponent component) {
        this.getContentPane().removeAll();
        this.getContentPane().add(component);
        component.addComponentListener(componentListener = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (!selfReference.isResizable()) {
                    pack();
                }
            }});
    }
    
}
