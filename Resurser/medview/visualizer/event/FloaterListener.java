/*
 * FloaterListener.java
 *
 * Created on November 8, 2002, 3:56 PM
 *
 * $Id: FloaterListener.java,v 1.1 2002/11/08 15:53:29 erichson Exp $
 *
 * $Log: FloaterListener.java,v $
 * Revision 1.1  2002/11/08 15:53:29  erichson
 * First check-in
 *
 */

package medview.visualizer.event;

/**
 * Listener interface for changes to Floater.
 * @see java.awt.event.WindowListener
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0 
 */
public interface FloaterListener {
        
    /**
     * Called when the user is closing a Floater
     */    
    public void floaterClosing(FloaterEvent fe);    
    
}
