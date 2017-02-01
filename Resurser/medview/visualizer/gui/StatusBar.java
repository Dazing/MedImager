/*
 * StatusBar.java
 *
 * Created on July 11, 2002, 11:31 AM
 *
 * $Id: StatusBar.java,v 1.4 2002/12/03 14:48:09 erichson Exp $
 *
 * $Log: StatusBar.java,v $
 * Revision 1.4  2002/12/03 14:48:09  erichson
 * Now non-floatable as default
 *
 * Revision 1.3  2002/10/30 15:56:35  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import javax.swing.*;
import javax.swing.border.*;

/**
 * A status bar, appropriate for adding to bottoms of views (Borderlayout.south).
 *
 * @author  Nils Erichson
 */
public class StatusBar extends JToolBar {
    
    /** 
     * Creates a new instance of StatusBar.
     */
    public StatusBar() {
        super();
        Border statusBarBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        
        //Border statusBarBorder = BorderFactory.createLineBorder(Color.black);
        //Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        //Border loweredBevel = BorderFactory.createLoweredBevelBorder();
        //Border statusBarBorder = BorderFactory.createCompoundBorder(raisedBevel,loweredBevel);
        
        this.setBorder(statusBarBorder);
        setFloatable(false);
    }
    
}
