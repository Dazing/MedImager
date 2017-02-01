/*
 * ToolHandler.java
 *
 * Created on July 8, 2002, 3:42 PM
 *
 * $Id: ToolHandler.java,v 1.7 2002/10/30 15:56:38 zachrisg Exp $
 *
 * $Log: ToolHandler.java,v $
 * Revision 1.7  2002/10/30 15:56:38  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import medview.visualizer.data.*; // ToolBox

/**
 * An interface for handlers of Tools.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface ToolHandler {
    
    /**
     * Add the entities under the point to the selection (as opposed to pointSelect() which deselects all other entities).
     *
     * @param p The point to look for the entities.
     */
    public void addPointSelection(Point p);
    
    /**
     * Add the entities within the shape to the selection (as opposed to shapeSelect() which deselects all other entities).
     *
     * @param s The shape used to select entities.
     */
    public void addShapeSelection(Shape s);
    
    /**
     * Do whatever needs to be done when the chart has been double clicked.
     * For example show the examination data.
     *
     * @param p The point where the double click was done.
     */
    public void doubleClick(Point p);
    
    /**
     * Select the entities under the point.
     *
     * @param p The point to look for the entities.
     */
    public void pointSelect(Point p);

    /**
     * Do whatever needs to be done when the chart has been double clicked.
     * For example show a popup menu.
     *
     * @param p The point where the right click was done.
     */
    public void rightClick(Point p);
    
    /**
     * Cancels the selection. Used to remove the temporary selection shape.
     */
    public void selectionCanceled();
    
    /**
     * Sets the temporary selection shape used when doing a
     * rectangle selection or freehand selection.
     *
     * @param s The new temporary selection shape.
     */
    public void setSelectionShape(Shape s);
    
    /**
     * Select all entities within the shape.
     *
     * @param s The shape to select entities with.
     */
    public void shapeSelect(Shape s);
    
    /**
     * Start dragging the entities under the point.
     *
     * @param e The InputEvent that generated the drag start.
     * @startPoint The point where the drag started.
     */
    public void startDrag(InputEvent e, Point startPoint);
    
    /**
     * Returns true if the chart supports the specified tool.
     *
     * @param tool The tool to check if it is supported.
     */
    public boolean supportsTool(int tool);

    /**
     * Returns true if an entity is under the point.
     *
     * @param point The point.
     * @return True if an entity is under the point.
     */
    public boolean entityUnderPoint(Point point);
    
    /**
     * Returns true if a selected entity is under the point.
     *
     * @param point The point.
     * @return True if a selected entity is under the point.
     */
    public boolean selectedEntityUnderPoint(Point point);

}
