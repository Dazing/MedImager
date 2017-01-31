/*
 * FloaterComponent.java
 *
 * Created on den 2 juli 2003, 01:22
 *
 * $Id: FloaterComponent.java,v 1.1 2003/07/02 00:29:14 erichson Exp $
 *
 * $Log: FloaterComponent.java,v $
 * Revision 1.1  2003/07/02 00:29:14  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui;

import javax.swing.*;

/**
 * Base class for components that exist in Floaters
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public abstract class FloaterComponent extends JPanel {
    
    /** Creates a new instance of FloaterComponent */
    
    public abstract int getFloaterType();
    
}
