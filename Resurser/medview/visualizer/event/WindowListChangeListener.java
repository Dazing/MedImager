/*
 * WindowListChangeListener.java
 *
 * Created on July 4, 2002, 2:47 PM
 *
 * $Id: WindowListChangeListener.java,v 1.2 2002/10/30 15:06:36 zachrisg Exp $
 *
 * $Log: WindowListChangeListener.java,v $
 * Revision 1.2  2002/10/30 15:06:36  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/** 
 * A listener interface for WindowListChangeEvents.
 *
 * @author Nils Erichson
 */
public interface WindowListChangeListener {
    
    /**
     * Called when the list of windows has changed.
     *
     * @param e The object describing the event.
     */
    public void windowListChanged(WindowListChangeEvent e);
    
}
