/*
 * FloaterEvent.java
 *
 * Created on November 8, 2002, 12:38 PM
 *
 * $Id: FloaterEvent.java,v 1.1 2002/11/08 15:50:39 erichson Exp $
 *
 * $Log: FloaterEvent.java,v $
 * Revision 1.1  2002/11/08 15:50:39  erichson
 * First check-in
 *
 */

package medview.visualizer.event;

import medview.visualizer.gui.Floater;

/**
 * Event class for changes to Floaters
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class FloaterEvent {
    
    private final Floater sourceFloater;
    
    /** Called when the user is closing a floater */
    public FloaterEvent(Floater source) {
        sourceFloater = source;
    }
    
    public Floater getSource() {
        return sourceFloater;
    }
    
    
}
