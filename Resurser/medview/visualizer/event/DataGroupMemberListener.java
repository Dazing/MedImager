/*
 * DataGroupStateListener.java
 *
 * Created on July 22, 2002, 10:22 AM
 *
 * $Id: DataGroupMemberListener.java,v 1.2 2002/10/30 15:06:31 zachrisg Exp $
 *
 * $Log: DataGroupMemberListener.java,v $
 * Revision 1.2  2002/10/30 15:06:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener for changes in the state of a data group such a color or name changes.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface DataGroupMemberListener {
   
    /**
     * Called when the member count or selected member count of the data group has changed.
     *
     * @param event The event. 
     */
    public void dataGroupMemberCountChanged(DataGroupEvent event);
        
}
