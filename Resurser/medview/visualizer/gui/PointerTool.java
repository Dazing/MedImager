/*
 * PointerTool.java
 *
 * Created on July 9, 2002, 4:10 PM
 *
 * $Id: PointerTool.java,v 1.9 2002/10/30 15:56:32 zachrisg Exp $
 *
 * $Log: PointerTool.java,v $
 * Revision 1.9  2002/10/30 15:56:32  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import medview.visualizer.data.*; // ToolBox

/**
 * A tool for selecting and dragging entities.
 *
 * @author  G�ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class PointerTool extends Tool {
    
    /** The start point of a drag operation. */
    private Point startPoint = null;
    
    /** Set to true while a rectangle selection is performed. */
    private boolean rectangleSelect = false;
    
    /** Set to true when the selection operation is completed. */
    private boolean selectionDone = false;
    
    /** 
     * Creates a new instance of PointerTool. 
     *
     * @param toolHandler The ToolHandler that handles the calls generated by the Tool.
     * @param component The JComponent that the Tool will observe for user actions.
     */
    public PointerTool(ToolHandler toolHandler, JComponent component) {
        super(toolHandler, component);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component. 
     *
     * @param e The event object.
     */
    public void mouseClicked(MouseEvent e) {        
        ApplicationManager.debug("PointerTool.mouseClicked()");
        
        int modifiers = e.getModifiers();
        int clickCount = e.getClickCount();
        Point point = e.getPoint();
        
        if (super.toolHandler == null) {
            return;
        } else if (!super.toolHandler.supportsTool(ToolBox.TOOL_POINTER)) {
            return;
        }
        
        if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
            if (clickCount == 2) {
                super.toolHandler.doubleClick(point);
            }
        }
    }
    
    /**    
     * Invoked when a mouse button has been pressed on a component. 
     *
     * @param e The event object.
     */
    public void mousePressed(MouseEvent e) {
        int modifiers = e.getModifiers();
        Point point = e.getPoint();

        rectangleSelect = false;
        
        if (super.toolHandler == null) {
            return;
        } else if (!super.toolHandler.supportsTool(ToolBox.TOOL_POINTER)) {
            return;
        }
                
        if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
            ApplicationManager.debug("PointerTool.mousePressed(): button1 pressed");
            startPoint = point;
            
            if (super.toolHandler.selectedEntityUnderPoint(startPoint)) {
                if (e.isControlDown()) {
                    super.toolHandler.addPointSelection(startPoint);
                    selectionDone = true;
                } else {
                    selectionDone = false;
                }
            } else if (super.toolHandler.entityUnderPoint(startPoint)) {
                if (e.isControlDown()) {
                    super.toolHandler.addPointSelection(startPoint);
                } else {
                    super.toolHandler.pointSelect(startPoint);
                }
                selectionDone = true;
            } else {
                selectionDone = false;
            }
        }    
    }
    
    /**
     * Invoked when a mouse button is pressed on a component and then dragged. MOUSE_DRAGGED events will continue to be delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position is within the bounds of the component). 
     * 
     * Due to platform-dependent Drag&Drop implementations, MOUSE_DRAGGED events may not be delivered during a native Drag&Drop operation. 
     *
     * @param e The event object.
     */
    public void mouseDragged(MouseEvent e) {
        int modifiers = e.getModifiers();
        Point point = e.getPoint();
        
        if (super.toolHandler == null) {
            return;
        } else if (!super.toolHandler.supportsTool(ToolBox.TOOL_POINTER)) {
            return;
        }
                
        if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
            if (startPoint != null) {
                if (!rectangleSelect) { // The folowing is in a new if clause for performance reasons.
                    if (super.toolHandler.selectedEntityUnderPoint(startPoint)) {
                        super.toolHandler.startDrag(e,startPoint);
                        rectangleSelect = false;
                        selectionDone = true;
                        // make sure startDrag() isn't called more than once
                        startPoint = null;
                    } else {
                        if (!super.toolHandler.supportsTool(ToolBox.TOOL_RECTANGLE_SELECT)) {
                            return;
                        }
                        if (selectionDone) {
                            return; // this shouldn't happen
                        }
                        
                        rectangleSelect = true;
                        int x = Math.min(startPoint.x, point.x);
                        int y = Math.min(startPoint.y, point.y);
                        int width = Math.max(startPoint.x, point.x) - x;
                        int height = Math.max(startPoint.y, point.y) - y;
            
                        Rectangle r = new Rectangle(x,y,width,height);
            
                        super.toolHandler.setSelectionShape(r);
                    }
                } else {
                    int x = Math.min(startPoint.x, point.x);
                    int y = Math.min(startPoint.y, point.y);
                    int width = Math.max(startPoint.x, point.x) - x;
                    int height = Math.max(startPoint.y, point.y) - y;
            
                    Rectangle r = new Rectangle(x,y,width,height);
            
                    super.toolHandler.setSelectionShape(r);
                }
            }
        }        
    }
    
    /** 
     * Invoked when a mouse button has been released on a component.
     *
     * @param e The event object.
     */
    public void mouseReleased(MouseEvent e) {
        int modifiers = e.getModifiers();
        Point point = e.getPoint();
        
        if (super.toolHandler == null) {
            return;
        } else if (!super.toolHandler.supportsTool(ToolBox.TOOL_RECTANGLE_SELECT)) {
            return;
        }
         
        
        if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
            if (rectangleSelect) {
                int x = Math.min(startPoint.x, point.x);
                int y = Math.min(startPoint.y, point.y);
                int width = Math.max(startPoint.x, point.x) - x;
                int height = Math.max(startPoint.y, point.y) - y;
            
                Rectangle r = new Rectangle(x,y,width,height);
            
                if (e.isControlDown()) {
                    super.toolHandler.addShapeSelection(r);
                } else {
                    super.toolHandler.shapeSelect(r);
                }
            } else if (!selectionDone) {
                super.toolHandler.pointSelect(startPoint);
            }
        }
        rectangleSelect = false;
        startPoint = null;
        selectionDone = true;
    }
       
}
