/*
 * RemovalListener.java
 *
 * Created on August 26, 2002, 4:31 PM
 *
 * $Id: RemovalListener.java,v 1.2 2002/10/30 15:06:33 zachrisg Exp $
 *
 * $Log: RemovalListener.java,v $
 * Revision 1.2  2002/10/30 15:06:33  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for object removals.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface RemovalListener {
    
    /**
     * Called when an object is removed.
     *
     * @param event The event containing a reference to the source and the removed object.
     */
    public void objectRemoved(RemovalEvent event);
    
}
