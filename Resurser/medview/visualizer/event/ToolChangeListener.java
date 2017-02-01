/*
 * ToolChangeListener.java
 *
 * Created on June 26, 2002, 3:38 PM
 *
 * $Id: ToolChangeListener.java,v 1.3 2002/10/30 15:06:35 zachrisg Exp $
 *
 * $Log: ToolChangeListener.java,v $
 * Revision 1.3  2002/10/30 15:06:35  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for ToolChangeEvents.
 *
 * @author  d97nix
 */
public interface ToolChangeListener extends java.util.EventListener {

    /**
     * Invoked when the active tool has changed.
     *
     * @param e An object that contains a reference to the object that initiated the event.
     */    
    public void toolChanged(ToolChangeEvent e);
    
}

