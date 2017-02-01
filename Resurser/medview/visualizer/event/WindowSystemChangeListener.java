/*
 * WindowSystemChangeListener.java
 *
 * Created on July 4, 2002, 2:47 PM
 *
 * $Id: WindowSystemChangeListener.java,v 1.2 2002/10/30 15:06:37 zachrisg Exp $
 *
 * $Log: WindowSystemChangeListener.java,v $
 * Revision 1.2  2002/10/30 15:06:37  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/** 
 * A listener interface that listens for changes in the
 * window system. When the window system changes from MDI
 * to free windows or the other way around the
 * windowSystemChanged() method is called.
 *
 * @author Göran Zachrisson
 */
public interface WindowSystemChangeListener {
    
    /** 
     * Called when the window system has changed from MDI to
     * free windows or the other way around.
     *
     * @param e An event object containing the source of the event.
     */    
    public void windowSystemChanged(WindowSystemChangeEvent e);
    
}
