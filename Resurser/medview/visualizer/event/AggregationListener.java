/*
 * SelectionListener.java
 *
 * Created on September June 27, 2002, 4:05 PM
 *
 * $Id: AggregationListener.java,v 1.1 2002/09/27 15:36:42 erichson Exp $
 *
 * $Log: AggregationListener.java,v $
 * Revision 1.1  2002/09/27 15:36:42  erichson
 * First check-in
 *
 */

package medview.visualizer.event;

/**
 * Aggregation listener interface
 * @author  d97nix
 * @version 1.0
 */
public interface AggregationListener extends java.util.EventListener {

    /**
     * Called when the aggregation has changed.
     * @param event The object representing the event.
     */
    public void aggregationChanged(AggregationEvent event);
}

