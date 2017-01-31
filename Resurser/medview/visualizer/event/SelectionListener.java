/*
 * SelectionListener.java
 *
 * Created on June 26, 2002, 3:38 PM
 *
 * $Id: SelectionListener.java,v 1.3 2002/10/30 15:06:33 zachrisg Exp $
 *
 * $Log: SelectionListener.java,v $
 * Revision 1.3  2002/10/30 15:06:33  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for selection changes.
 *
 * @author  d97nix
 */
public interface SelectionListener extends java.util.EventListener {

    /**
     * Called when the selection has changed.
     *
     * @param event The object representing the event.
     */
    public void selectionChanged(SelectionEvent event);
    
}

