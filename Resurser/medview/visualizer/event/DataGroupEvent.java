/*
 * DataGroupEvent.java
 *
 * Created on July 19, 2002, 5:08 PM
 *
 * $Id: DataGroupEvent.java,v 1.3 2002/10/30 15:06:30 zachrisg Exp $
 *
 * $Log: DataGroupEvent.java,v $
 * Revision 1.3  2002/10/30 15:06:30  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

import medview.visualizer.data.*; // DataGroup

/**
 * An event class for changes concerning data groups.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupEvent {
    
    /** The data group that caused the event. */
    private DataGroup group;
    
    /** 
     * Creates a new instance of DataGroupEvent. 
     *
     * @param dataGroup The dataGroup that caused the event.
     */
    public DataGroupEvent(DataGroup dataGroup) {
        group = dataGroup;
    }
    
    /**
     * Returns the data group that caused the event.
     *
     * @return The data group that caused the event.
     */
    public DataGroup getDataGroup() {
        return group;
    }
    
}
