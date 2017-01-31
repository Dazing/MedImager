/*
 * ScatterPlotXYItemRenderer.java
 *
 * Created on July 18, 2002, 12:19 PM
 *
 * $Id: ScatterPlotXYItemRenderer.java,v 1.8 2004/07/15 18:16:00 erichson Exp $
 *
 * $Log: ScatterPlotXYItemRenderer.java,v $
 * Revision 1.8  2004/07/15 18:16:00  erichson
 * Added comment about intersects(...)
 *
 * Revision 1.7  2002/10/30 15:56:34  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.geom.*;
import com.jrefinery.chart.*;
import com.jrefinery.chart.tooltips.*;
import com.jrefinery.data.*;
import medview.visualizer.data.*;

/**
 * A renderer for ExaminationDataElements in a scatterplot.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ScatterPlotXYItemRenderer extends StandardXYItemRenderer {
   
    /** The shape factory used to determine the shape of the items that are rendered. */
    private ShapeFactory shapeFactory = new DefaultShapeFactory();
    
    /** 
     * Creates a new instance of ScatterPlotXYItemRenderer.
     */
    public ScatterPlotXYItemRenderer() {
        super(StandardXYItemRenderer.SHAPES,new SymbolicXYToolTipGenerator());
    }

    /**
     * Returns the Paint to be used when rendering the item.
     * The paint depends on which series the item belongs to and whether or
     * not the ExaminationDataElement it represents is selected.
     *
     * @param plot The plot used for obtaining the item.
     * @param series The series the item belongs to.
     * @param item The index of the item.
     * @param x The x-position of the item.
     * @param y The y-position of the item.
     * @return The Paint to be used when rendering the item.
     */
    protected Paint getPaint(Plot plot, int series, int item, double x, double y) {
        XYGraphDataSet dataSet = (XYGraphDataSet) plot.getDataset();
        return dataSet.getDataGroup(item).getColor();
    }

    /**
     * Returns true if the shape is to be filled.
     * 
     * @param plot The plot used for obtaining the item.
     * @param series The series the item belongs to.
     * @param item The index of the item.
     * @param x The x-position of the item.
     * @param y The y-position of the item.
     * @return Always true since the shapes always should be filled.
     */
    protected boolean isShapeFilled(Plot plot, int series, int item, double x, double y) {
        return true;
    }

    /**
     * Returns the shape for an item.
     *
     * @param plot The plot used for obtaining the item.
     * @param series The series the item belongs to.
     * @param item The index of the item.
     * @param x The x-position of the item.
     * @param y The y-position of the item.
     * @param scale The scale factor.
     * @return A shape representing the item.
     */
    protected Shape getShape(Plot plot, int series, int item, double x, double y, double scale) {
        XYGraphDataSet dataSet = (XYGraphDataSet) plot.getDataset();
        
        XYGraphDataSet.GraphItem graphItem = dataSet.getGraphItem(item);
/*        
        // create a rectangle

        double totalSize = graphItem.getTotalSize();
        double totalWidth = Math.sqrt(totalSize);
        double widthPercentage = ((double)graphItem.getSize()) / (totalWidth * totalWidth);
        double posPercentage = ((double)graphItem.getPos()) / (totalWidth * totalWidth);
        double scaledTotalWidth = 5 + totalWidth * 2;
        
        double shapeX = x - scaledTotalWidth / 2 + scaledTotalWidth * posPercentage;
        double shapeY = y - scaledTotalWidth / 2;
        double shapeWidth = scaledTotalWidth * widthPercentage;
        double shapeHeight = scaledTotalWidth;
        return new Rectangle((int)shapeX, (int)shapeY, (int)shapeWidth, (int)shapeHeight);
 */
        double totalSize = graphItem.getTotalSize();
        double sizePercentage = graphItem.getSize() / totalSize;
        double posPercentage = graphItem.getPos() / totalSize;
        
        double shapeWidth = 5 + 3 * Math.sqrt(totalSize);
        double shapeHeight = shapeWidth;
        
        // Center the arc (it is painted to the right and below of the coordinate)
        double shapeX = x - shapeWidth / 2; 
        double shapeY = y - shapeWidth / 2;        
        
        Shape shape;
        if (sizePercentage == 1.0) {
            shape = new Ellipse2D.Double(shapeX, shapeY, shapeWidth, shapeHeight);
        } else {
            shape = new Arc2D.Double(shapeX, shapeY, shapeWidth, shapeHeight, posPercentage * 360, sizePercentage * 360, Arc2D.PIE) {
                // This is needed because of a bug in the intersects() of Arc2D.
                // Disabling this causes painting to become all wrong (arcs are not drawn unless they are full 360* - they are there
                // (can be selected and the selection shows) but painting doesn't work correctly.
                 public boolean intersects(double x, double y, double w, double h) {
                    return getBounds2D().intersects(x,y,w,h);
                }
            };
        }
        return shape; 
        
    }

    /**
     * Initialize the renderer.
     *
     * @param g2d The graphics device.
     * @param dataArea The data area.
     * @param plot The plot.
     * @param data The data set.
     * @param info The rendering info.
     */
    public void initialise(Graphics2D g2d, Rectangle2D dataArea, XYPlot plot, XYDataset data, ChartRenderingInfo info) {
        super.initialise(g2d, dataArea, plot, data, info);
    }
    
}
