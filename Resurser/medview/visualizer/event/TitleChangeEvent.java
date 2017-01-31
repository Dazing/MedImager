/*
 * TitleChangeEvent.java
 *
 * Created on August 7, 2002, 2:09 PM
 *
 * $Id: TitleChangeEvent.java,v 1.2 2002/10/30 15:06:34 zachrisg Exp $
 *
 * $Log: TitleChangeEvent.java,v $
 * Revision 1.2  2002/10/30 15:06:34  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event for title changes.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class TitleChangeEvent {
    
    /** The new title. */
    private String title;
    
    /** 
     * Creates a new instance of TitleChangeEvent.
     *
     * @param title The new title. 
     */
    public TitleChangeEvent(String title) {
        this.title = title;
    }

    /**
     * Returns the new title.
     *
     * @return The new title.
     */
    public String getTitle() {
        return title;
    }
    
}
