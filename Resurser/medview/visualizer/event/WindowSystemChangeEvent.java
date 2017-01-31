/*
 * WindowSystemChangeEvent.java
 *
 * Created on July 4, 2002, 2:45 PM
 *
 * $Id: WindowSystemChangeEvent.java,v 1.3 2002/10/30 15:06:37 zachrisg Exp $
 *
 * $Log: WindowSystemChangeEvent.java,v $
 * Revision 1.3  2002/10/30 15:06:37  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;
import java.util.*;

/** 
 * An event sent to WindowSystemChangeListeners when
 * the window system is changed from MDI to free windows or
 * the other way around.
 *
 * @author Göran Zachrisson
 */
public class WindowSystemChangeEvent extends java.util.EventObject {
    
    /** The new window system */
    private int windowSystem;
    
    /**
     * Creates a new instance of WindowSystemChangeEvent.
     *
     * @param eventSource The source that generated the event.
     * @oaran windowSystem The new window system.
     */
    public WindowSystemChangeEvent(Object eventSource, int windowSystem) {
        super(eventSource);
        this.windowSystem = windowSystem;
    }
    
    /**
     * Returns the new window system.
     *
     * @return The new window system. Either ApplicationManager.WINDOWSYSTEM_MDI or ApplicationManager.WINDOWSYSTEM_FREE.
     */
    public int getWindowSystem() {
        return windowSystem;
    }
    
}
