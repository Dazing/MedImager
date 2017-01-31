/*
 * DataGroupStateListener.java
 *
 * Created on July 22, 2002, 10:22 AM
 *
 * $Id: DataGroupStateListener.java,v 1.4 2002/10/30 15:06:31 zachrisg Exp $
 *
 * $Log: DataGroupStateListener.java,v $
 * Revision 1.4  2002/10/30 15:06:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener for changes in the state of a data group such a color or name changes.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface DataGroupStateListener {
   
    /**
     * Called when the color of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupColorChanged(DataGroupEvent event);
    
    /**
     * Called when the name of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupNameChanged(DataGroupEvent event);
}
