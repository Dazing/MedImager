/*
 * QueryChangeListener.java
 *
 * Created on October 11, 2002, 11:07 AM
 *
 * $Id: QueryChangeListener.java,v 1.1 2002/10/11 09:09:22 zachrisg Exp $
 *
 * $Log: QueryChangeListener.java,v $
 * Revision 1.1  2002/10/11 09:09:22  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for query change events.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface QueryChangeListener {
    
    /**
     * Called when the query has changed.
     *
     * @param event The event object describing the event.
     */
    public void queryChanged(QueryEvent event);
    
}
