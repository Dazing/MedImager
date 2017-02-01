/*
 * ViewFrameMenuItem.java
 *
 * Created on October 14, 2002, 4:23 PM
 *
 * $Id: ViewFrameMenuItem.java,v 1.1 2002/10/14 15:16:24 erichson Exp $
 *
 * $Log: ViewFrameMenuItem.java,v $
 * Revision 1.1  2002/10/14 15:16:24  erichson
 * First check-in
 *
 */

package medview.visualizer.gui;

import javax.swing.*;

/**
 * A JMenuItem which encapsulates a ViewFrame.
 * Used for the Window menu in {@link #ApplicationFrame ApplicationFrame}
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */

public class ViewFrameMenuItem extends JMenuItem {
    
    private ViewFrame viewFrame;
    
    /**
     * Create a new ViewFrameMenuItem
     * @param vf The viewframe to encapsulate
     */
    public ViewFrameMenuItem(ViewFrame vf) {
        super("  " + vf.getTitle());
        viewFrame = vf;
    }
    
    /**
     * Create a new ViewFrameMenuItem with a mnemonic
     * @param mnemonic The mnemonic character
     * @param vf The viewframe to encapsulate
     */
    public ViewFrameMenuItem(char mnemonic,ViewFrame vf) {
        super(mnemonic + " " + vf.getTitle());
        setMnemonic(mnemonic);
        viewFrame = vf;
    }
    
    /**
     * Gets the encapsulated viewframe
     * @return the encapsulated viewframe
     */
    public ViewFrame getViewFrame() {
        return viewFrame;
    }    

} // end class
