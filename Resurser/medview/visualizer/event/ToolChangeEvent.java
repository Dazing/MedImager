/*
 * ToolChangeEvent.java
 *
 * Created on June 26, 2002, 3:42 PM
 *
 * $Id: ToolChangeEvent.java,v 1.2 2002/10/30 15:06:35 zachrisg Exp $
 *
 * $Log: ToolChangeEvent.java,v $
 * Revision 1.2  2002/10/30 15:06:35  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event for tool changes.
 *
 * @author  d97nix
 */
public class ToolChangeEvent extends java.util.EventObject {        
    
    /** 
     * Creates new ToolChangeEvent. 
     *
     * @param eventSource The source of the event.
     */
    public ToolChangeEvent(Object eventSource) {
        super(eventSource);
        
    }

}
