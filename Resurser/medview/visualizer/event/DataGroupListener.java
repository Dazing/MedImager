/*
 * DataGroupListener.java
 *
 * Created on July 19, 2002, 5:06 PM
 *
 * $Id: DataGroupListener.java,v 1.3 2002/10/30 15:06:31 zachrisg Exp $
 *
 * $Log: DataGroupListener.java,v $
 * Revision 1.3  2002/10/30 15:06:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An interface for listeners of data group additions and removals.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface DataGroupListener {
    
    /**
     * Called when a data group was added.
     *
     * @param event An event containing a reference to the added group.
     */    
    public void dataGroupAdded(DataGroupEvent event);
    
    /**
     * Called when a data group was removed.
     *
     * @param event An event containing a reference to the removed group.
     */    
    public void dataGroupRemoved(DataGroupEvent event);
    
}
