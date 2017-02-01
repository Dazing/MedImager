/*
 * WindowSystemChangeEvent.java
 *
 * Created on July 4, 2002, 2:45 PM
 *
 * $Id: WindowListChangeEvent.java,v 1.2 2002/10/30 15:06:36 zachrisg Exp $
 *
 * $Log: WindowListChangeEvent.java,v $
 * Revision 1.2  2002/10/30 15:06:36  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;
import java.util.*;

/** 
 * Event sent when the list of active windows changes.
 *
 * @author Nils Erichson
 */
public class WindowListChangeEvent extends java.util.EventObject {
    
    /** 
     * Creates a new instance of WindowSystemChangeEvent.
     *
     * @param eventSource The source that generated the event.
     */
    public WindowListChangeEvent(Object eventSource) {
        super(eventSource);        
    }       
    
}
