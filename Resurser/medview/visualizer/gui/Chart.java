/*
 * Chart.java
 *
 * Created on July 8, 2002, 3:36 PM
 *
 * $Id: Chart.java,v 1.22 2004/07/22 14:54:16 erichson Exp $
 *
 * $Log: Chart.java,v $
 * Revision 1.22  2004/07/22 14:54:16  erichson
 * Added value length limiting (usable by any charts/plots)
 *
 * Revision 1.21  2002/11/28 14:44:37  zachrisg
 * Removed some debug output.
 *
 * Revision 1.20  2002/11/04 14:33:10  zachrisg
 * Removed bug that stopped double click from working correctly.
 *
 * Revision 1.19  2002/10/31 10:42:07  zachrisg
 * Fixed a bug that caused an exception if all elements were removed from a chart and
 * the user then clicked in the chart.
 *
 * Revision 1.18  2002/10/22 09:32:08  zachrisg
 * Cleaned up javadoc and added Id and Log tags.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import medview.visualizer.gui.*; // ToolHandler
import medview.visualizer.event.*; // ToolChangeListener
import medview.visualizer.data.*; // ToolBox

import com.jrefinery.chart.*;
import com.jrefinery.chart.entity.*; // StandardEntityCollection

/**
 * An abstract superclass for chart or plot components.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public abstract class Chart extends JComponent implements ToolHandler, ToolChangeListener {
    
    /** Lenght limit for painting values */          
    protected int valueLengthLimit = Integer.MAX_VALUE;
    
    /** The chart. */
    protected JFreeChart chart = null;    
    
    /** A cached image of the chart. */
    private BufferedImage cachedImage = null;   
    
    /** True if the cached image needs to be regenerated. */
    protected boolean cachedImageInvalid = true;

    /** True if the chart needs to be regenerated. */
    protected boolean chartInvalid = true;
    
    /** The active tool. */
    protected Tool tool;
    
    /** The information about the chart after the rendering. */
    protected ChartRenderingInfo renderingInfo;
    
    /** The temporary selection shape. */
    protected Shape selectionShape = null;
    
    /** 
     * Creates a new instance of Chart.
     */
    protected Chart() {
        int newTool = ApplicationManager.getInstance().getTool();
        setTool(newTool);
        renderingInfo = new ChartRenderingInfo(new StandardEntityCollection());
    }

    /**
     * Select the entity under the point.
     *
     * @param p The point to look for the entity.
     */
    public void pointSelect(Point p) {}
    
    /**
     * Select all entities within the shape.
     *
     * @param s The shape to select entities with.
     */
    public void shapeSelect(Shape s) {}
    
    /**
     * Sets the temporary selection shape used when doing a
     * rectangle selection or freehand selection.
     *
     * @param s The new temporary selection shape.
     */
    public void setSelectionShape(Shape s) {}
    
    /**
     * Cancels the selection. Used to remove the temporary selection shape.
     */
    public void selectionCanceled() {}
    
    /**
     * Start dragging the entities under the point.
     *
     * @startPoint The point where the drag started.
     */
    public void startDrag(InputEvent e, Point startPoint) {}
    
    /**
     * Do whatever needs to be done when the chart has been double clicked.
     * For example show the examination data.
     *
     * @param p The point where the double click was done.
     */
    public void doubleClick(Point p) {}

    /**
     * Do whatever needs to be done when the chart has been double clicked.
     * For example show a popup menu.
     *
     * @param p The point where the right click was done.
     */
    public void rightClick(Point p) {}
    
    /**
     * Returns true if the chart supports the specified tool.
     *
     * @param tool The tool to check if it is supported.
     */
    public abstract boolean supportsTool(int tool);    
    
    /** 
     * Add the entities under the point to the selection (as opposed to pointSelect() which deselects all other entities).
     *
     * @param p The point to look for the entities.
     */
    public void addPointSelection(Point p) {}
    
    /** 
     * Add the entities within the shape to the selection (as opposed to shapeSelect() which deselects all other entities).
     *
     * @param s The shape used to select entities.
     */
    public void addShapeSelection(Shape s) {}

    /**
     * Invoked when the active tool has changed.
     *
     * @param e An object that contains a reference to the object that initiated the event.
     */    
    public void toolChanged(ToolChangeEvent e) {
        ToolBox toolBox = (ToolBox)e.getSource();
        int newTool = toolBox.getTool();
        // remove all listeners
        tool.setComponent(null);
        tool.setToolHandler(null);
        // set the new tool
        setTool(newTool);
         
    }
    
    /**
     * Sets the active tool for this chart.
     *
     * @param newTool The tool.
     */
    private void setTool(int newTool) {
        switch (newTool) {
            case ToolBox.TOOL_POINTER:
                tool = new PointerTool(this,this);
                break;
            case ToolBox.TOOL_RECTANGLE_SELECT:
                tool = new RectangleSelectionTool(this,this);
                break;
            default:
                tool = new PointerTool(this,this);
                System.err.println("Chart.toolChanged(): Unknown tool!");
                break;
        }
    }
   
    /**
     * Marks the cached image as invalid so that it will get repainted at the next repaint.
     */
    public void invalidateChart() {
        cachedImageInvalid = true;
        chartInvalid = true;
    }

    /**
     * Makes sure the chart is up to date.
     */
    public abstract void validateChart();
    
    /**
     * Paints the chart on the graphics context.
     *
     * @param g The graphics context.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        if (chart == null)
            return;
       
        validateCachedImage();
        
        if (!cachedImageInvalid) {
            g2d.drawImage(cachedImage,0,0,null); // draw the cached image
        
            // If a selection shape exists, draw it
            if (selectionShape != null) {
                g2d.setColor(Color.black);
                g2d.draw(selectionShape);
            }            
        }
    }

    /**
     * Makes sure that the cached image is up to date.
     */
    public void validateCachedImage() {
        Dimension size = getSize();

        // check if chart has been resized
        if (cachedImage != null) {
            if ((cachedImage.getWidth() != size.width) || (cachedImage.getHeight() != size.height)) {
                cachedImage = null;
                cachedImageInvalid = true;
            }
        }
        
        // check if image is too small to be drawn or empty
        if ((size.width <= 0) || (size.height <= 0) || isEmpty() ) {
            cachedImage = null;
            cachedImageInvalid = true;
            renderingInfo.getEntityCollection().clear();
            return;
        }
        
        // if cachedImage is null create an empty image
        if (cachedImage == null) {
            cachedImage = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_RGB);
            cachedImageInvalid = true;
        }

        // regenerate the cached image if needed
        if (cachedImageInvalid) {
            Graphics2D cachedG2d = (Graphics2D)cachedImage.getGraphics();            
            chart.draw(cachedG2d,new Rectangle(0,0,size.width,size.height),renderingInfo);
            paintSelection(cachedG2d);
            cachedImageInvalid = false;
        }
    }
    
    /** 
     * Returns true if an entity is under the point.
     *
     * @return True if an entity is under the point.
     */
    public abstract boolean entityUnderPoint(Point point);

    /** 
     * Returns true if a selected entity is under the point.
     *
     * @param point The point.
     * @return True if a selected entity is under the point.
     */
    public abstract boolean selectedEntityUnderPoint(Point point);
   
    /**
     * Returns true if the chart is empty.
     *
     * @return True if the chart is empty.
     */
    protected abstract boolean isEmpty();
    
    /**
     * Paints the selection.
     *
     * @param g The graphics context to paint the selection on.
     */
    protected abstract void paintSelection(Graphics g);    
    
    /**
     * Shorten the strings if they exceed valueLengthLimit
     */                
    protected void limitStrings(String[] stringArray)
    {                                
        for (int v = 0; v < stringArray.length; v++)
        {
            stringArray[v] = limitString(stringArray[v], valueLengthLimit);            
        }
    }    
    
    public static String limitString(String string, int limit)
    {
        int toplimit = limit - 3;
        if (toplimit < 0)
            toplimit = 0;
        if (string.length() > limit)
                string = string.substring(0, (toplimit) +1) + "...";
        return string;
    }    
    
    /**
     * Sets the length limit for drawing values
     */
    protected void setValueLengthLimit(int newlimit)
    {
        valueLengthLimit = newlimit;
    }
}
