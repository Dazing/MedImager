/*
 * DataGroupChangeListener.java
 *
 * Created on July 24, 2002, 3:58 PM
 */

package medview.visualizer.event;

/**
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface DataGroupChangeListener {
    
    /**
     * Called when an element has changed the data group that it belongs to.
     * @param event The object representing the event.
     */
    void dataGroupChanged(DataGroupEvent event);
    
}
