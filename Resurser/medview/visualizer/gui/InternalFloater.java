/*
 * InternalFloater.java
 *
 * Created on July 23, 2002, 10:24 AM
 *
 * $Id: InternalFloater.java,v 1.12 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: InternalFloater.java,v $
 * Revision 1.12  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.11  2002/11/27 13:47:07  zachrisg
 * Added medview icon to the internal floater frame.
 *
 * Revision 1.10  2002/11/08 15:52:22  erichson
 * added FloaterListener methods, changed default close operation to DO_NOTHING, put setVisible(false) in floaterClosing method
 *
 * Revision 1.9  2002/10/31 14:50:57  zachrisg
 * Minor changes needed for settings.
 *
 * Revision 1.8  2002/10/17 14:40:07  zachrisg
 * Changed floater constructors to take "resizable" as a parameter.
 *
 * Revision 1.7  2002/10/17 11:39:40  erichson
 * set Focusable to false, floaterClosing now calls closeFloater()
 *
 * Revision 1.6  2002/10/17 11:34:28  zachrisg
 * Added possibility to resize floater and removed automatic packing of floater.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.visualizer.data.*; // ApplicationManager
import medview.visualizer.event.*; // FloaterListener

/**
 * A Floater implementation for floaters in JInternalFrames.
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class InternalFloater extends JInternalFrame implements Floater {
    
    /** Whether InternalFloaters should be closeable or not */
    private final static boolean closeable = true;
    
    private HashSet floaterListeners = new HashSet();
    
    /** The name of the floater. */
    private String name;
 
    /** The component listener. */
    private ComponentListener componentListener = null;
    
    /** A reference to the floater. */
    private InternalFloater selfReference;
    
    /** 
     * Creates a new instance of InternalFloater.
     *
     * @param name The name of the floater.
     * @param title The title of the floater window.
     * @param location The initial location of the floater.
     * @param resizable True if the floater should be resizable.
     */
    public InternalFloater(String name, String title, Point location, boolean resizable) {        
        super(title, resizable, closeable);
        
        try {
            setFrameIcon(ApplicationManager.getInstance().loadVisualizerIcon("medview16.gif"));
        } catch (IOException exc) {
            System.err.println("InternalFloater could not load icon.");
        }
            
        selfReference = this;
        this.name = name;
        setLocation(location);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                floaterClosing();
            }});
        this.setLayer(JLayeredPane.PALETTE_LAYER); // Make datagroup always appear above views
        setFocusable(false);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        
        
        
        addComponentListener(new ComponentAdapter(){
 
        public void componentMoved(ComponentEvent evt)
        {
            //we must go through all opened frames to see if this frame is the
            //one farthest out, if it is we must shrink the desktop
            ViewFrame[] viewFrames = ApplicationManager.getInstance().getViewFrames();
            int x = 0;
            int y = 0;
            for(ViewFrame view : viewFrames)
            {
                x = Math.max(x,view.getLocation().x+view.getSize().width);
                y = Math.max(y,view.getLocation().y+view.getSize().height);
            }

            for(Floater floater : ApplicationManager.getInstance().getToolFloaters())
            {
                x = Math.max(x,floater.getLocation().x+floater.getSize().width);
                y = Math.max(y,floater.getLocation().y+floater.getSize().height);
            }

            ApplicationManager.getInstance().getDesktop().setPreferredSize(new Dimension(x,y));
            ApplicationManager.getInstance().getDesktop().updateUI();
            }
    });
   
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
     * Called when the floater is closing.
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
