/*
 * TitleChangeListener.java
 *
 * Created on August 7, 2002, 2:08 PM
 *
 * $Id: TitleChangeListener.java,v 1.2 2002/10/30 15:06:35 zachrisg Exp $
 *
 * $Log: TitleChangeListener.java,v $
 * Revision 1.2  2002/10/30 15:06:35  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for TitleChangeEvents.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface TitleChangeListener {
    
    /**
     * Called when the title of a View has been changed.
     *
     * @param event The object containing information about the event.
     */
    public void titleChanged(TitleChangeEvent event);
        
}
