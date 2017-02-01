/*
 * ScatterPlot.java
 *
 * Created on July 2, 2002, 9:14 AM
 *
 * $Id: ScatterPlot.java,v 1.42 2004/07/22 14:55:28 erichson Exp $
 *
 * $Log: ScatterPlot.java,v $
 * Revision 1.42  2004/07/22 14:55:28  erichson
 * Reworked value length limiting so that the actual data is not modified, only the plot
 *
 * Revision 1.41  2004/07/15 18:19:24  erichson
 * Cleaned range handling, added grid lines, some JFreeChart documentation
 *
 * Revision 1.40  2004/06/24 20:25:59  erichson
 * added setValueLengthLimit call
 *
 * Revision 1.39  2002/11/13 14:34:01  zachrisg
 * Added support for session loading/saving.
 *
 * Revision 1.38  2002/10/30 09:57:23  zachrisg
 * Added double-click support to the chart.
 *
 * Revision 1.37  2002/10/10 14:53:27  erichson
 * Added setAggregation(), regenerateChart()
 *
 */

package medview.visualizer.gui;

import medview.datahandling.aggregation.*;
import medview.visualizer.data.*;

import com.jrefinery.data.*;
import com.jrefinery.chart.tooltips.*;
import com.jrefinery.chart.entity.*;
import com.jrefinery.chart.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * A component for displaying a scatterplot.
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 * @version 
 */
public class ScatterPlot extends Chart implements DatasetChangeListener, ChangeListener {     
    
    /**
     * Default max length of values on axes
     */
    private static final int DEFAULT_VALUE_LENGTH_LIMIT = 50;     
    
    /** Whether or not the labels on the x-axis should be vertical. */
    private boolean verticalXLabels = true;
    
    /** The dataset. */
    private XYGraphDataSet xyDataSet;
    
    /** The term on the x-axis. */
    private String xTerm;
    
    /** The term on the y-axis. */
    private String yTerm;
   
    /** The horizontal percent of the total range that is visible. */
    private double horizontalPercentVisible = 100.0;
    
    /** The vertical percent of the total range that is visible. */
    private double verticalPercentVisible = 100.0;
    
    /** The horizontal range model. */
    private BoundedRangeModel horizontalRangeModel;
    
    /** The vertical range model. */
    private BoundedRangeModel verticalRangeModel;
    
    /** 
     * Creates a new ScatterPlot.
     *
     * @param xyDataSet The data set.
     * @param xTerm The x-axis term.
     * @param yTerm The y-axis term.
     */
    public ScatterPlot(XYGraphDataSet xyDataSet, String xTerm, String yTerm) {
        super();
        setValueLengthLimit(DEFAULT_VALUE_LENGTH_LIMIT); // Limit drawn length of values on axes
        generateChart(xyDataSet,xTerm,yTerm);
    }

    /**
     * Generates the Chart from an XYGraphDataSet and the x- and y-terms.
     *
     * @param xyDataSet The new dataset.
     * @param xTerm The term on the x-axis.
     * @param yTerm The term on the y-axis.
     */
    protected void generateChart(XYGraphDataSet xyDataSet, String xTerm, String yTerm) {
        this.xyDataSet = xyDataSet;
        xyDataSet.addChangeListener(this);
        this.xTerm = xTerm;
        this.yTerm = yTerm;
        
        horizontalRangeModel = new DefaultBoundedRangeModel();
        verticalRangeModel = new DefaultBoundedRangeModel();
        updateRangeModels();
               
        // add change listeners to the range models
        horizontalRangeModel.addChangeListener(this);
        verticalRangeModel.addChangeListener(this);
  
        // create the axes
        ValueAxis xAxis = createXAxis();
        ValueAxis yAxis = createYAxis();
            
        // create the plot
        XYPlot plot = new XYPlot(xyDataSet,xAxis,yAxis);
        
        // add a custom renderer
        XYItemRenderer renderer = new ScatterPlotXYItemRenderer();
        plot.setRenderer(renderer);
        
        // create the chart
        chart = new JFreeChart(null, // Chart title
                                JFreeChart.DEFAULT_TITLE_FONT, // Title font
                                plot, 
                                false); // Create legend?

        // make sure that the cached image gets regenerated
        invalidateChart();
    }
    
    /**
     * Sets whether the labels on the x-axis should be vertical or not.
     *
     * @param vertical Vertical labels on the x-axis.
     */
    public void setVerticalXLabels(boolean vertical) {
        if (verticalXLabels != vertical) {
            verticalXLabels = vertical;
            chart.getXYPlot().setDomainAxis(createXAxis());
            // make sure that the plot gets regenerated
            regenerateChart();
        }
    }
    
    private void regenerateChart() {
        invalidateChart();
        validateChart();
    }
    
    /**
     * Returns true if the labels on the x-axis are vertical.
     *
     * @return True if the labels on the x-axis are vertical.
     */
    public boolean isVerticalXLabelsUsed() {
        return verticalXLabels;
    }
    
    /**
     * Creates the x-axis.
     *
     * @return A new x-axis.
     */
    private ValueAxis createXAxis() {
        String[] symbolicValues = ((XisSymbolic)xyDataSet).getXSymbolicValues();                        
        // Limit the length on the axis
        limitStrings(symbolicValues);
        
        boolean autoRange = false;
     
        double lowerBound = horizontalRangeModel.getValue();
        double upperBound = lowerBound + horizontalRangeModel.getExtent();

        // check if autorange should be used (if the range is only one item)
        if ((upperBound - lowerBound) <= 1) {
            autoRange = true;
            lowerBound = ValueAxis.DEFAULT_LOWER_BOUND;
            upperBound = ValueAxis.DEFAULT_UPPER_BOUND;
        }
        
        // create the axis
        HorizontalSymbolicAxis xAxis = new HorizontalSymbolicAxis(
             xTerm, // label
             symbolicValues,
             Axis.DEFAULT_AXIS_LABEL_FONT,
             Axis.DEFAULT_AXIS_LABEL_PAINT,
             Axis.DEFAULT_AXIS_LABEL_INSETS,
             true, // tick labels visible
             Axis.DEFAULT_TICK_LABEL_FONT,
             Axis.DEFAULT_TICK_LABEL_PAINT,
             Axis.DEFAULT_TICK_LABEL_INSETS,
             verticalXLabels,  // tick labels drawn vertically
             true, // tick marks visible
             Axis.DEFAULT_TICK_STROKE,
             autoRange,  // true, // auto range selection
             new Integer(0), //ValueAxis.DEFAULT_AUTO_RANGE_MINIMUM_SIZE,
             false, // auto range includes zero
             false, // auto range sticky zero
             lowerBound,//ValueAxis.DEFAULT_LOWER_BOUND,
             upperBound, //ValueAxis.DEFAULT_UPPER_BOUND,
             false, // inverted
             false, // auto tick unit
             new SymbolicTickUnit(NumberAxis.DEFAULT_TICK_UNIT.getSize(), symbolicValues),
             true, // grid lines visible
             ValueAxis.DEFAULT_GRID_LINE_STROKE,
             ValueAxis.DEFAULT_GRID_LINE_PAINT,
             true, // ValueAxis.DEFAULT_CROSSHAIR_VISIBLE,
             0.0,  // crosshair value
             ValueAxis.DEFAULT_CROSSHAIR_STROKE,
             ValueAxis.DEFAULT_CROSSHAIR_PAINT,
             false, // smbolic grid line
             ValueAxis.DEFAULT_GRID_LINE_PAINT); // color of dark aprt of symbolic grid line. MUST NOT BE NULL or null XORColor is thrown
        return xAxis;
    }
    
    /**
     * Creates the y-axis.
     *
     * @return The new y-axis.
     */
    private ValueAxis createYAxis() {
        String[] symbolicValues = ((YisSymbolic)xyDataSet).getYSymbolicValues();
        
        // Limit the length of values on the axis
        limitStrings(symbolicValues);        
        
        boolean autoRange = false;               
        
        double lowerBound = verticalRangeModel.getValue();
        double upperBound = lowerBound + verticalRangeModel.getExtent();
        
        // check if autorange should be used (if the range is only one item)
        if ((upperBound - lowerBound) <= 1) {
            autoRange = true;
            lowerBound = ValueAxis.DEFAULT_LOWER_BOUND;
            upperBound = ValueAxis.DEFAULT_UPPER_BOUND;
        }
        
        // create the axis
        VerticalSymbolicAxis yAxis = new VerticalSymbolicAxis(
             yTerm,
             symbolicValues,
             Axis.DEFAULT_AXIS_LABEL_FONT,
             Axis.DEFAULT_AXIS_LABEL_PAINT,
             Axis.DEFAULT_AXIS_LABEL_INSETS,
             true, // label drawn vertical
             true, // tick labels visible
             Axis.DEFAULT_TICK_LABEL_FONT,
             Axis.DEFAULT_TICK_LABEL_PAINT,
             Axis.DEFAULT_TICK_LABEL_INSETS,
             // false,  // tick labels drawn vertically
             true, // tick marks visible
             Axis.DEFAULT_TICK_STROKE,
             autoRange, // true, // auto range selection
             ValueAxis.DEFAULT_AUTO_RANGE_MINIMUM_SIZE,
             false, // auto range includes zero
             false, // auto range sticky zero
             lowerBound, // ValueAxis.DEFAULT_LOWER_BOUND,
             upperBound, // ValueAxis.DEFAULT_UPPER_BOUND,
             true, // inverted
             false, // auto tick unit
             new SymbolicTickUnit(NumberAxis.DEFAULT_TICK_UNIT.getSize(), symbolicValues),
             true, // grid lines visible
             ValueAxis.DEFAULT_GRID_LINE_STROKE,
             ValueAxis.DEFAULT_GRID_LINE_PAINT,
             ValueAxis.DEFAULT_CROSSHAIR_VISIBLE,
             0.0,  // crosshair value
             ValueAxis.DEFAULT_CROSSHAIR_STROKE,
             ValueAxis.DEFAULT_CROSSHAIR_PAINT,
             false, // symbolic grid line
             ValueAxis.DEFAULT_GRID_LINE_PAINT);
        return yAxis;
    }    

    /**
     * Select the entity under the point.
     *
     * @param p The point to look for the entity.
     */
    public void pointSelect(Point p) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was below the point.
        // I don't use entities.getEntity(double x, double y) because that method will only return one ChartEntity. 
        Vector selectedEntities = new Vector();
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            ChartEntity entity = (ChartEntity)i.next();
            if (entity.getArea().contains(p)) {
                selectedEntities.add(entity);
            }
        }

        // deselect all elements
        DataManager.getInstance().deselectAllElements();

        // select the examinations connected to the entities below the point
        for (Iterator i = selectedEntities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
            for (int j = 0; j < elements.length; j++) {
                elements[j].setSelected(true);
            }
        }

        // update all selection stuff - views etc
        DataManager.getInstance().validateViews();
    }

    /** 
     * Add the entities under the point to the selection (as opposed to pointSelect() which
     * deselects all other entities).
     *
     * @param p The point to look for the entities.
     */
    public void addPointSelection(Point p) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was below the point.
        // I don't use entities.getEntity(double x, double y) because that method will only return one ChartEntity. 
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            if (entity.getArea().contains(p)) {
                ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
                boolean allSelected = true;
                for (int j = 0; j < elements.length; j++) {
                    if (!elements[j].isSelected()) {
                        allSelected = false;
                    }
                }
                for (int j = 0; j < elements.length; j++) {
                    elements[j].setSelected(!allSelected);
                }
            }
        }

        // repaint the views that need repainting etc. update selection-related stuff
        DataManager.getInstance().validateViews();
    }   

    /**
     * Select all entities within the shape.
     *
     * @param s The shape to select entities with.
     */
    public void shapeSelect(Shape s) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was withing the shape.
        Vector selectedEntities = new Vector();
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            ChartEntity entity = (ChartEntity)i.next();
            if (s.contains(entity.getArea().getBounds())) {
                selectedEntities.add(entity);
            }
        }

        // deselect all elements
        DataManager.getInstance().deselectAllElements();

        // select the examinations connected to the entities below the point
        for (Iterator i = selectedEntities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
            for (int j = 0; j < elements.length; j++) {
                elements[j].setSelected(true);
            }
        }

        // remove the selection shape
        selectionShape = null;

        // repaint the views that need repainting
        DataManager.getInstance().validateViews();

        // repaint this chart to make sure that the selection shape is removed
        repaint();
    }
    
    /**
     * Add the entities within the shape to the selection (as opposed to shapeSelect() which deselects all other entities).
     *
     * @param s The shape used to select entities.
     */
    public void addShapeSelection(Shape s) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was withing the shape.
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            if (s.contains(entity.getArea().getBounds())) {
                ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
                boolean allSelected = true;
                for (int j = 0; j < elements.length; j++) {
                    if (!elements[j].isSelected()) {
                        allSelected = false;
                    }
                }
                for (int j = 0; j < elements.length; j++) {
                    elements[j].setSelected(!allSelected);
                }
            }
        }

        // remove the selection shape
        selectionShape = null;

        // repaint the views that need repainting
        DataManager.getInstance().validateViews();

        // repaint this chart to make sure that the selection shape is removed
        repaint();
    }
    
    /**
     * Sets the temporary selection shape used when doing a
     * rectangle selection or freehand selection.
     *
     * @param s The new temporary selection shape.
     */
    public void setSelectionShape(Shape s) {
        selectionShape = s;
        repaint();
    }

    /**
     * Do whatever needs to be done when the chart has been double clicked.
     * For example show the summary report.
     *
     * @param p The point where the double click was done.
     */
    public void doubleClick(Point p) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was below the point.
        // I don't use entities.getEntity(double x, double y) because that method will only return one ChartEntity.
        Vector selectedElements = new Vector();
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            if (entity.getArea().contains(p)) {
                ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
                for (int j = 0; j < elements.length; j++) {
                    selectedElements.add(elements[j]);
                }                
            }
        }

        ApplicationFrame.getInstance().showSummaryDialog((ExaminationDataElement[]) selectedElements.toArray(new ExaminationDataElement[selectedElements.size()]));        
    }
        
    /**
     * Returns true if the chart supports the specified tool.
     *
     * @param tool The tool to check if it is supported.
     */
    public boolean supportsTool(int tool) {
        if ((tool == ToolBox.TOOL_POINTER) ||
            (tool == ToolBox.TOOL_RECTANGLE_SELECT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Start dragging the entities under the point.
     *
     * @param e The event that caused the drag.
     * @param startPoint The point where the drag started.
     */
    public void startDrag(InputEvent e, Point startPoint) {
        ApplicationManager.debug("ScatterPlot.startDrag(): drag started: event: " + e.toString());

        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out if a selected entity was under the drag start point.
        // I don't use entities.getEntity(double x, double y) because that method will only return one ChartEntity. 
        boolean selectedEntityUnderPoint = false;
        boolean entityUnderPoint = false;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity)i.next();
            if (entity.getArea().contains(startPoint)) {
                ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
                entityUnderPoint = true;
                for (int j = 0; j < elements.length; j++) {
                    if (elements[j].isSelected()) {
                        selectedEntityUnderPoint = true;
                    }
                }
            }
        }
        
        if (!selectedEntityUnderPoint) {
            pointSelect(startPoint);
        }
        
        if (entityUnderPoint) {
            TransferHandler handler = getTransferHandler();
            handler.exportAsDrag(this,e,TransferHandler.COPY);
        }
    }

    /** 
     * Returns true if an entity is under the point.
     *
     * @return True if an entity is under the point.
     */
    public boolean entityUnderPoint(Point point) {
        EntityCollection entities = renderingInfo.getEntityCollection();

        boolean entityUnderPoint = false;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity) i.next();
            if (entity.getArea().contains(point)) {
                entityUnderPoint = true;
            }
        }    
        return entityUnderPoint;
    }
    
    /**
     * Paints the selection.
     *
     * @param g The graphics object to paint the selection on.
     */    
    protected void paintSelection(Graphics g) {
        final float[] dashPattern = {2,2,2,1,1,1};
        final Stroke dashedStroke = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1,dashPattern,0);
        final Stroke plainStroke = new BasicStroke(2);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // set up the clipping
        Shape originalClip = g2d.getClip();
        g2d.clip(renderingInfo.getDataArea());
        
        // draw the selected entities
        EntityCollection entities = renderingInfo.getEntityCollection();        
        
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity) i.next();
            ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
            boolean allSelected = true;
            for (int j = 0; j < elements.length; j++) {
                if (!elements[j].isSelected()) {
                    allSelected = false;
                }
            }
            if (allSelected) {
                Shape shape = entity.getArea();
                g2d.setColor(Color.yellow);
                g2d.setStroke(plainStroke);
                g2d.draw(shape);
                g2d.setColor(Color.red);
                g2d.setStroke(dashedStroke);
                g2d.draw(shape);
            }
        }
        
        // restore the clipping
        g2d.setClip(originalClip);
    }
    
    /**
     * Sets the terms on the x- and y-axis.
     *
     * @param xTerm The term on the x-axis.
     * @param yTerm The term on the y-axis.
     */
    public void setTerms(String xTerm, String yTerm) {
        this.xTerm = xTerm;
        this.yTerm = yTerm;
        
        xyDataSet.setTerms(xTerm, yTerm);
        
        validateChart();
    }

    /**
     * Sets whether or not the values should be in percent of the amount of each data group.
     *
     * @param percentValues True if the values should be in percent.
     */
    public void setPercentValues(boolean percentValues) {
        xyDataSet.setPercentValues(percentValues);
        validateChart();
    }
    
    /** 
     * Returns true if the chart is empty.
     *
     * @return True if the chart is empty.
     */
    protected boolean isEmpty() {
        return xyDataSet.isEmpty();
    }
    
    /**
     * Called when the data set has changed.
     *
     * @param event The event.
     */
    public void datasetChanged(DatasetChangeEvent event) {
        invalidateChart(); // not repaint, since this gets called for every data item that changes....                
    }    

    /**
     * Makes sure the chart is valid and repaint it.
     */
    public void validateChart() {
        // regenerate the data set if needed
        xyDataSet.validateDataSet();
        
        if (chartInvalid) {
            // update the range models
            horizontalRangeModel.removeChangeListener(this);
            verticalRangeModel.removeChangeListener(this);
            updateRangeModels();
            horizontalRangeModel.addChangeListener(this);
            verticalRangeModel.addChangeListener(this);
            
            XYPlot xyPlot = chart.getXYPlot();

            // regenerate the axes
            xyPlot.setDomainAxis(createXAxis());
            xyPlot.setRangeAxis(createYAxis());

            chartInvalid = false;
            cachedImageInvalid = true;
            
            // make sure that the chart gets repainted
            repaint();
        }
    }

    /** 
     * Returns true if a selected entity is under the point.
     *
     * @param point The point.
     * @return True if a selected entity is under the point.
     */
    public boolean selectedEntityUnderPoint(Point point) {
        EntityCollection entities = renderingInfo.getEntityCollection();

        boolean selectedEntityUnderPoint = false;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            XYItemEntity entity = (XYItemEntity) i.next();
            if (entity.getArea().contains(point)) {
                ExaminationDataElement[] elements = xyDataSet.getElements(entity.getSeries(), entity.getItem());
                for (int j = 0; j < elements.length; j++) {
                    if (elements[j].isSelected()) {
                        return true;
                    }
                }
            }
        }    
        return false;
    }
    
    /**
     * Returns the number of x-axis values.
     *
     * @return The number of x-axis values.
     */
    public int getXValuesCount() {
        return xyDataSet.getXSymbolicValues().length;
    }
    
    /**
     * Returns the number of y-axis values.
     *
     * @return The number of y-axis values.
     */
    public int getYValuesCount() {
        return xyDataSet.getYSymbolicValues().length;
    }

    /**
     * Invoked when either of the rangemodels has changed.
     *
     * @param event The event.
     */
    public void stateChanged(ChangeEvent event) {
        Object source = event.getSource();
        invalidateChart();
        validateChart();
    }
    
    /**
     * Returns the horizontal range model.
     *
     * @return The horizontal range model.
     */
    public BoundedRangeModel getHorizontalRangeModel() {
        return horizontalRangeModel;
    }
    
    /**
     * Returns the vertical range model.
     *
     * @return The vertical range model.
     */
    public BoundedRangeModel getVerticalRangeModel() {
        return verticalRangeModel;
    }
    
    /**
     * Updates the range models according to the data set.
     */
    private void updateRangeModels() {
        // update the horizontal range model
        int hMin = 0;
        int hMax = Math.max(this.getXValuesCount() - 1 , 0);
        int hExtent = (int)(horizontalPercentVisible / 100.0 * (double) hMax);        
        int hValue = Math.min(hMax - hExtent, horizontalRangeModel.getValue());
        horizontalRangeModel.setRangeProperties(hValue, hExtent, hMin, hMax, horizontalRangeModel.getValueIsAdjusting());
        
        // update the vertical range model
        int vMin = 0;
        int vMax = Math.max(this.getYValuesCount() - 1, 0);
        int vExtent = (int)(verticalPercentVisible / 100.0 * (double) vMax);        
        int vValue = Math.min(vMax - vExtent, verticalRangeModel.getValue());
        verticalRangeModel.setRangeProperties(vValue, vExtent, vMin, vMax, verticalRangeModel.getValueIsAdjusting());
    }
    
    /**
     * Sets the percent of the total horizontal range that should be visible.
     * 
     * @param visiblePercent The visible percent of the total range.
     */
    public void setHorizontalVisibleRange(double visiblePercent) {
        if (visiblePercent > 100.0) {
            horizontalPercentVisible = 100.0;
        } else if (visiblePercent <= 0.0) {
            horizontalPercentVisible = 1.0;
        } else {
            horizontalPercentVisible = visiblePercent;
        }
        updateRangeModels();
    }
    
    /**
     * Sets the percent of the total vertical range that should be visible.
     * 
     * @param visiblePercent The visible percent of the total range.
     */
    public void setVerticalVisibleRange(double visiblePercent) {
        if (visiblePercent > 100.0) {
            verticalPercentVisible = 100.0;
        } else if (visiblePercent <= 0.0) {
            verticalPercentVisible = 1.0;
        } else {
            verticalPercentVisible = visiblePercent;
        }
        updateRangeModels();
    }
    
    /**
     * Returns the visible percent of the total horizontal range.
     *
     * @return The visible percent of the total horizontal range.
     */
    public double getHorizontalVisibleRange() {
        return horizontalPercentVisible;
    }
    
    /**
     * Returns the visible percent of the total vertical range.
     *
     * @return The visible percent of the total vertical range.
     */
    public double getVerticalVisibleRange() {
        return verticalPercentVisible;
    }
    
    /**
     * Change to a new aggregation.
     *
     * @param agg the new aggregation
     */
    public void setAggregation(Aggregation agg) {
        xyDataSet.setAggregation(agg);
        regenerateChart();
    }
    
    /**
     * Returns the x-axis term.
     *
     * @return The x-axis term.
     */
    public String getXTerm() {
        return xTerm;
    }
    
    /**
     * Returns the y-axis term.
     *
     * @return The y-axis term.
     */
    public String getYTerm() {
        return yTerm;
    }
    
    /**
     * Returns true if percent values are used.
     *
     * @return True if percent values are used.
     */
    public boolean isPercentValuesUsed() {
        return xyDataSet.isPercentValuesUsed();
    }    
}
