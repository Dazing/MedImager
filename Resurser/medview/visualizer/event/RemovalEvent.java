/*
 * RemovalEvent.java
 *
 * Created on August 26, 2002, 4:26 PM
 *
 * $Id: RemovalEvent.java,v 1.2 2002/10/30 15:06:32 zachrisg Exp $
 *
 * $Log: RemovalEvent.java,v $
 * Revision 1.2  2002/10/30 15:06:32  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event sent when an object has been removed.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class RemovalEvent {
    
    /** The source of the event. */
    private Object source;

    /** The removed object. */
    private Object removedObject;
    
    /** 
     * Creates a new instance of RemovalEvent.
     *
     * @param source The source of the event.
     * @param removedObject The removed object.
     */
    public RemovalEvent(Object source, Object removedObject) {
        this.source = source;
        this.removedObject = removedObject;
    }
    
    /**
     * Returns the source of the event.
     *
     * @return The source of the event.
     */
    public Object getSource() {
        return source;
    }
    
    /**
     * Returns the removed object.
     *
     * @return The removed object.
     */
    public Object getRemovedObject() {
        return removedObject;
    }
    
}
