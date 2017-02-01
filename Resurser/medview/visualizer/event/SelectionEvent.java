/*
 * SelectionEvent.java
 *
 * Created on June 26, 2002, 3:42 PM
 *
 * $Id: SelectionEvent.java,v 1.3 2002/10/30 15:06:33 zachrisg Exp $
 *
 * $Log: SelectionEvent.java,v $
 * Revision 1.3  2002/10/30 15:06:33  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event for selection changes.
 *
 * @author  d97nix
 */

public class SelectionEvent extends java.util.EventObject {    
    
    /** True if Control was pressed when the selection change occurred. */
    private boolean controlDown = false;
    
    /** True if Shift was pressed when the selection change occurred. */
    private boolean shiftDown = false;
    
    /** 
     * Creates new SelectionEvent.
     *
     * @param eventSource The source of the event.
     */
    public SelectionEvent(Object eventSource) {
        super(eventSource);
    }

    /**
     * Create a new SelectionEvent storing modifier information.
     *
     * @param eventSource The source of the event.
     * @param controlDown True if Control was pressed when the selection change occurred.
     * @param shiftDown True if Shift was pressed when the selection change occurred.
     */
    public SelectionEvent(Object eventSource, boolean controlDown, boolean shiftDown) {
        super(eventSource);
        this.controlDown = controlDown;
        this.shiftDown = shiftDown;
    }

    /**
     * Returns true if Control was pressed when the selection change occurred.
     *
     * @return True if Control was pressed when the selection change occurred.
     */
     public boolean isControlDown() {
         return controlDown;
     }

     /**
     * Returns true if Shift was pressed when the selection change occurred.
      *
     * @return True if Shift was pressed when the selection change occurred.
     */
     public boolean isShiftDown() {
         return shiftDown;
     }

}
