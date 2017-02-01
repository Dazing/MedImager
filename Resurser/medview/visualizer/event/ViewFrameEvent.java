/*
 * FrameEvent.java
 *
 * Created on August 23, 2002, 2:06 PM
 *
 * $Id: ViewFrameEvent.java,v 1.1 2002/11/25 14:06:06 erichson Exp $
 *
 * $Log: ViewFrameEvent.java,v $
 * Revision 1.1  2002/11/25 14:06:06  erichson
 * First check-in. This class is not finished, and not used right now.
 *
 */

package medview.visualizer.event;

/**
 *
 * @author  erichson
 */
import medview.visualizer.gui.ViewFrame;
import java.awt.*; // AWTEvent 

public class ViewFrameEvent extends java.util.EventObject {
    
    static final int FRAME_ACTIVATED = 1;
    static final int FRAME_CLOSED = 2;
    static final int FRAME_CLOSING = 3;
    static final int FRAME_DEACTIVATED = 4;
    static final int FRAME_DEICONIFIED = 5;
    static final int FRAME_ICONIFIED = 6;
    static final int FRAME_OPENED = 7;

    static final int FRAME_FIRST = 1;
    static final int FRAME_LAST = 7;
        
    private int eventId;
    
    
    /** Creates a new instance of FrameEvent */
    public ViewFrameEvent(ViewFrame source, int id) {
        super(source);
        eventId = id;        
    }        
    
    public ViewFrame getViewFrame() {
        return (ViewFrame) super.getSource();
    }
            
}
