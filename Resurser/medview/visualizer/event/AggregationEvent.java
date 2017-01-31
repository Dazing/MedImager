/*
 * SelectionEvent.java
 *
 * Created on June 26, 2002, 3:42 PM
 *
 * $Id: AggregationEvent.java,v 1.1 2002/09/27 15:35:25 erichson Exp $ 
 *
 * $Log: AggregationEvent.java,v $
 * Revision 1.1  2002/09/27 15:35:25  erichson
 * First check-in
 *
 */

package medview.visualizer.event;

/**
 *
 * @author  d97nix
 * @version 1.0
 */

public class AggregationEvent extends java.util.EventObject {    
    
    /** Creates new AggregationEvent */
    public AggregationEvent(Object eventSource) {
        super(eventSource);
    }

    
    // inherited: getSource(), toString()

}
