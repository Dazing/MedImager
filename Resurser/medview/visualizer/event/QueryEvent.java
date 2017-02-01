/*
 * QueryEvent.java
 *
 * Created on October 11, 2002, 11:02 AM
 *
 * $Id: QueryEvent.java,v 1.1 2002/10/11 09:06:47 zachrisg Exp $
 *
 * $Log: QueryEvent.java,v $
 * Revision 1.1  2002/10/11 09:06:47  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

/**
 * An event for queries.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class QueryEvent {
    
    /** The source of the event. */
    private Object source;
    
    /** 
     * Creates a new instance of QueryEvent.
     *
     * @param source The source of the event.
     */
    public QueryEvent(Object source) {
        this.source = source;
    }
    
    /**
     * Returns the source of the event.
     *
     * @return The source of the event.
     */
    public Object getSource() {
        return source;
    }
    
}
