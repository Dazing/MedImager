/*
 * BarChart.java
 *
 * Created on July 16, 2002
 *
 * $Id: BarChart.java,v 1.31 2004/07/22 14:52:50 erichson Exp $
 *
 * $Log: BarChart.java,v $
 * Revision 1.31  2004/07/22 14:52:50  erichson
 * Value length limiting, fixes Bugzilla #383
 *
 * Revision 1.30  2004/06/24 15:44:37  d97nix
 * Update: Now uses the value length-limiting of categorygraphdataset to avoid too big axes
 *
 * Revision 1.29  2002/11/13 14:06:53  zachrisg
 * Added support for session saving.
 *
 * Revision 1.28  2002/10/30 10:00:13  zachrisg
 * Some minor javadoc clean ups.
 *
 * Revision 1.27  2002/10/25 14:39:38  zachrisg
 * Added untested support for summary reports.
 *
 * Revision 1.26  2002/10/21 08:31:56  zachrisg
 * Changed a debug comment from scatterplot to barchart.
 *
 * Revision 1.25  2002/10/10 14:54:16  erichson
 * added setAggregation()
 *
 * Revision 1.24  2002/10/04 13:09:37  erichson
 * added isXLabelsVertical
 *
 * $Id: BarChart.java,v 1.31 2004/07/22 14:52:50 erichson Exp $
 *
 * $Log: BarChart.java,v $
 * Revision 1.31  2004/07/22 14:52:50  erichson
 * Value length limiting, fixes Bugzilla #383
 *
 * Revision 1.30  2004/06/24 15:44:37  d97nix
 * Update: Now uses the value length-limiting of categorygraphdataset to avoid too big axes
 *
 * Revision 1.29  2002/11/13 14:06:53  zachrisg
 * Added support for session saving.
 *
 * Revision 1.28  2002/10/30 10:00:13  zachrisg
 * Some minor javadoc clean ups.
 *
 * Revision 1.27  2002/10/25 14:39:38  zachrisg
 * Added untested support for summary reports.
 *
 * Revision 1.26  2002/10/21 08:31:56  zachrisg
 * Changed a debug comment from scatterplot to barchart.
 *
 * Revision 1.25  2002/10/10 14:54:16  erichson
 * added setAggregation()
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

import com.jrefinery.data.*;
import com.jrefinery.chart.tooltips.*;
import com.jrefinery.chart.entity.*;
import com.jrefinery.chart.*;

import medview.visualizer.data.*;
import medview.visualizer.jfreechartextensions.*;

/**
 * <code>Chart</code> subclass for a bar chart.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */

public class BarChart extends Chart implements DatasetChangeListener {
    
    /** Horizontal alignment. */
    public final static int ALIGNMENT_HORIZONTAL = 1;
    
    /** Vertical alignment. */
    public final static int ALIGNMENT_VERTICAL = 2;
    
    /** Whether or not the labels on the x-axis should be vertical. */
    private boolean verticalXLabels = false;
    
    /** The dataset. */
    private CategoryGraphDataSet cDataSet;
    
    /** The term to view */
    private String term;
    
    /** Whether the barchart is horizontal or not */
    private boolean horizontalBars;
    
    /** Whether or not the bars are stacked. */
    private boolean stackedBars;
    
    /** The actual plot. */
    private CategoryPlot plot = null;
    
    /** 
     * Creates new BarChart.
     *
     * @param dataSet The data set.
     * @param term The term to display.
     * @param horizontalBars True if the bars should be horizontal.
     * @param stackedBars True if the bars should be stacked.
     */
    public BarChart(CategoryGraphDataSet dataSet, String term, boolean horizontalBars, boolean stackedBars) {
        super();
        
        cDataSet = dataSet;
        cDataSet.addChangeListener(this);
        setValueLengthLimit(50);
        
        this.term = term;
        this.stackedBars = stackedBars;
        this.horizontalBars = horizontalBars;
        
        if (horizontalBars) {
            verticalXLabels = false;
        } else {
            verticalXLabels = true;
        }        
        
        generateChart();                
        validateChart();
    }

    public boolean isXLabelsVertical() {
        return verticalXLabels;
    }
    
    /**
     * Sets whether or not the bars should be stacked.
     *
     * @param stackedBars True if the bars should be stacked.
     */
    public void setStackedBars(boolean stackedBars) {
        if (this.stackedBars != stackedBars) {
            this.stackedBars = stackedBars;
            generateChart();
            validateChart();
        }
    }
    
    public void setAggregation(medview.datahandling.aggregation.Aggregation newAgg) {
        cDataSet.setAggregation(newAgg);
        generateChart();
        validateChart();
    }
    
    /**
     * Returns true if the bars are stacked.
     *
     * @return True if the bars are stacked.
     */
    public boolean isBarsStacked() {
        return stackedBars;
    }
    
    /**
     * Set the alignment of the bars of the plot (horizontal or vertical)
     * This method will re-generate the plot and adjust the alignment of the X axis labels.
     *
     * @param newAlignment the new bar alignment (BarChart.ALIGNMENT_HORIZONTAL or BarChart.ALIGNMENT_VERTICAL)
     */
    protected void setBarAlignment(int newAlignment) {        
        System.out.println("setBarAlignment called");
        
        switch (newAlignment) {
            case ALIGNMENT_HORIZONTAL:
                horizontalBars = true;
                verticalXLabels = false;
                generateChart();
                break;
            case ALIGNMENT_VERTICAL:
                horizontalBars = false;
                verticalXLabels = true;
                generateChart();
                break;
            default:
                System.err.println("Warning: unrecognized alignment ( " + newAlignment +") in setBarAlignment!");
                break;
        }
        validateChart();
    }

    /**
     * Generates the x-axis.
     */
    private void generateXAxis() {       
        if (isHorizontal()) {
            plot.setRangeAxis( (ValueAxis) createXAxis()); // x axis is range
        } else {
            plot.setDomainAxis( (CategoryAxis) createXAxis()); // x axis is domain            
        }
        invalidateChart();
    }

    /**
     * Generates the y-axis.
     */
    private void generateYAxis() {
        if (isHorizontal()) {
            plot.setDomainAxis( (CategoryAxis) createYAxis());
        } else {
            plot.setRangeAxis( (ValueAxis) createYAxis());
        }
        invalidateChart();
    }
    
    /**
     * Sets the x-axis label alignment.
     *
     * @param newAlignment The new alignment. Can be ALIGNMENT_HORIZONTAL or ALIGNMENT_VERTICAL.
     */
    protected void setXLabelAlignment(int newAlignment) {        
        switch (newAlignment) {
            case ALIGNMENT_HORIZONTAL:
                // System.out.println("Going to horizontal");
                verticalXLabels = false;
                generateXAxis();
                break;
            case ALIGNMENT_VERTICAL:
                // System.out.println("Going to vertical");
                verticalXLabels = true;
                generateXAxis();
                break;
            default:
                System.err.println("Warning: unrecognized alignment ( " + newAlignment +") in setXLabelAlignment!");
                break;
        }
        validateChart();
    }
    
    /**
     * Creates the x-axis.
     * 
     * @return The new x-axis.
     */
    private Axis createXAxis() {
        Axis xAxis;
        if (isHorizontal()) {
            xAxis = createNumberXAxis(); // horizontalnumberaxis (range)
            ((HorizontalNumberAxis) xAxis).setVerticalTickLabels(verticalXLabels);
        } else { // vertical
            xAxis = createCategoryXAxis(); // domain (category)
            ((HorizontalCategoryAxis) xAxis).setVerticalCategoryLabels(verticalXLabels);
        }
        return xAxis;
    }
    
    /**
     * Creates the y-axis.
     *
     * @return The new y-axis.
     */
    private Axis createYAxis() {
        Axis yAxis;
        if (isHorizontal()) {
            yAxis = createCategoryYAxis();
        } else {
            yAxis = createNumberYAxis();
        }
        return yAxis;
    }

    /** 
     * Generate (re-create) the plot.
     */ 
    public void generateChart() {
        
        CategoryItemRenderer renderer;
        
        Axis xAxis = createXAxis();
        Axis yAxis = createYAxis();
        
        if (isHorizontal()) { // Horizontal bars = values on x, domain on y
            ApplicationManager.debug("Making horizontal graph");
            if (stackedBars) {
                renderer = new StackedHorizontalBarRenderer(new StandardCategoryToolTipGenerator());
            } else {
                renderer = new HorizontalBarRenderer(new StandardCategoryToolTipGenerator());
            }
            // yAxis is domain
            plot = new HorizontalCategoryPlot(cDataSet,(CategoryAxis) yAxis, (ValueAxis) xAxis,renderer) {
                public Paint getSeriesPaint(int series) {
                    return cDataSet.getDataGroup(series).getColor();
                }
            };

        } else { // Vertical bars = values on y, domain on X
            ApplicationManager.debug("Making vertical graph");
            if (stackedBars) {
                renderer = new StackedVerticalBarRenderer(new StandardCategoryToolTipGenerator());
            } else {
                renderer = new VerticalBarRenderer(new StandardCategoryToolTipGenerator());
            }
            // xAxis is domain
            plot = new VerticalCategoryPlot(cDataSet,(CategoryAxis) xAxis, (ValueAxis) yAxis,renderer)
            {                
                public Paint getSeriesPaint(int series) {
                    return cDataSet.getDataGroup(series).getColor();
                }
            };            
        }
        plot.setItemGapsPercent(0.0);
        
        chart = new JFreeChart(null, // title
                                JFreeChart.DEFAULT_TITLE_FONT,
                                plot, 
                                false); // createlegend = false

        // make sure that the cached image gets regenerated
        invalidateChart();
    }
    
    /**
     * Creates a number x-axis.
     *
     * @return A number x-axis.
     */
    protected HorizontalNumberAxis createNumberXAxis() {
        HorizontalNumberAxis axis;
        System.out.println("isPercentValuesUsed: " + cDataSet.isPercentValuesUsed());        
        if (cDataSet.isPercentValuesUsed()) {
            axis = new HorizontalNumberAxis("Percent of examinations in data group (%)");
        } else {
            axis = new HorizontalNumberAxis("Amount");
            axis.setStandardTickUnits(TickUnits.createIntegerTickUnits());
        }
        return axis;
    }
    
    /**
     * Creates a number y-axis.
     *
     * @return A number y-axis.
     */
    protected VerticalNumberAxis createNumberYAxis() {
        VerticalNumberAxis axis;
        System.out.println("isPercentValuesUsed: " + cDataSet.isPercentValuesUsed());        
        if (cDataSet.isPercentValuesUsed()) {
            axis = new VerticalNumberAxis("Percent of examinations in data group (%)");
        } else {
            axis = new VerticalNumberAxis("Amount");
            axis.setStandardTickUnits(TickUnits.createIntegerTickUnits());
        }
        return axis;
    }
    
    /**
     * Creates a category x-axis.
     *
     * @return A category x-axis.
     */
    protected HorizontalCategoryAxis createCategoryXAxis() {
        HorizontalCategoryAxis axis = new BarChartHorizontalCategoryAxis(term, valueLengthLimit);        
        return axis;
    }
    
    /**
     * Creates a category y-axis.
     *
     * @return A category y-axis.
     */
    protected VerticalCategoryAxis createCategoryYAxis() {
        VerticalCategoryAxis axis = new BarChartVerticalCategoryAxis(term, valueLengthLimit);        
        return axis;
    }
    
    /**
     * Sets whether the labels on the x-axis should be vertical or not.
     *
     * @param vertical Vertical labels on the x-axis.
     */
    public void setVerticalXLabels(boolean vertical) {
        if (verticalXLabels != vertical) {
            
            // The label alignment has changed
            
            verticalXLabels = vertical;
            
            generateXAxis();
            
            validateChart();
        }
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
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
            for (int j = 0; j < elements.length; j++) {
                elements[j].setSelected(true);
            }
        }

        // update all selection stuff - views etc
        DataManager.getInstance().validateViews();
    }
    
    /**
     * Add the entities under the point to the selection (as opposed to pointSelect() which deselects all other entities).
     * If all the entities already are selected then deselect them.
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
        Vector selectedEntities = new Vector();
        boolean allSelected = true;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity)i.next();
            if (entity.getArea().contains(p)) {
                selectedEntities.add(entity);
                ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
                for (int j = 0; j < elements.length; j++) {
                    if (!elements[j].isSelected()) {
                        allSelected = false;
                    }
                }                
            }
        }
        
        
        // select the examinations connected to the entities below the point
        // or deselect them if all already were selected
        for (Iterator i = selectedEntities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
            for (int j = 0; j < elements.length; j++) {
                elements[j].setSelected(!allSelected);
            }
        }

        // update all selection stuff - views etc
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
            ChartEntity entity = (ChartEntity) i.next();
            if (s.contains(entity.getArea().getBounds())) {
                selectedEntities.add(entity);
            }
        }
        
        // deselect all elements
        DataManager.getInstance().deselectAllElements();
        
        // select the examinations
        for (Iterator i = selectedEntities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
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
     * If all the entities already are selected, then deselect them.
     *
     * @param s The shape used to select entities.
     */
    public void addShapeSelection(Shape s) {
        if (chart == null) {
            return;
        }
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        // Find out which entities was withing the shape.
        Vector selectedEntities = new Vector();
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            if (s.contains(entity.getArea().getBounds())) {
                selectedEntities.add(entity);
            }
        }
        
        // select the examinations
        for (Iterator i = selectedEntities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
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
            CategoryItemEntity entity = (CategoryItemEntity)i.next();
            if (entity.getArea().contains(p)) {
                ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
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
        
        ApplicationManager.debug("BarChart.startDrag(): drag started: event: " + e.toString());
        
        TransferHandler handler = getTransferHandler();
        handler.exportAsDrag(this,e,TransferHandler.COPY);
    }
    
    /** 
     * Returns true if an entity is under the point.
     *
     * @param point The point.
     * @return True if an entity is under the point.
     */
    public boolean entityUnderPoint(Point point) {
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        boolean entityUnderPoint = false;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            if (entity.getArea().contains(point)) {
                entityUnderPoint = true;
            }
        }
        return entityUnderPoint;        
    }
      
    /**
     * Returns true if the bars are horizontal.
     *
     * @return True if the bars are horizontal.
     */
    public boolean isHorizontal() {
        return horizontalBars;
    }
    
    /**
     * Returns true if the bars are vertical.
     *
     * @return True if the bars are vertical.
     */    
    private boolean isVertical() {
        return !horizontalBars;
    }
    
    /** 
     * Paint the selection.
     * Paints the selection marks on top of the unselected elements.
     *
     * @param g the graphics context to draw on.
     */
    protected void paintSelection(Graphics g) {
                
        final float[] dashPattern = {2,2,2,1,1,1};
        final Stroke dashedStroke = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1,dashPattern,0);
        final Stroke plainStroke = new BasicStroke(2);
        
        Graphics2D g2d = (Graphics2D) g;

        // set the clipping
        Shape originalClip = g2d.getClip();
        g2d.clip(renderingInfo.getDataArea());
        
        // draw the selection
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
            
            // ugly hack.. needs to be optimized
            // see if all elements in the category are selected
            boolean allSelected = true;

            if (elements.length < 1)
                allSelected = false;
            
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
     * Returns true if the chart is empty.
     *
     * @return True if the chart is empty.
     */
    protected boolean isEmpty() {
        return cDataSet.isEmpty();
    }
    
    /**
     * Sets the term to visualize.
     *
     * @param term The term to visualize.
     */
    public void setTerm(String term) {
        this.term = term;        
        cDataSet.setTerm(term);
        validateChart();
    }

    /**
     * Sets whether or not the values should be in percent of the amount of each data group.
     *
     * @param percentValues True if the values should be in percent.
     */
    public void setPercentValues(boolean percentValues) {
        cDataSet.setPercentValues(percentValues);
        validateChart();
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
     * Makes sure the chart is valid and repaints it if needed.
     */
    public void validateChart() {
        // regenerate the data set if needed
        cDataSet.validateDataSet();
        
        if (chartInvalid) {
            // regenerate the axes
            generateYAxis();
            generateXAxis();

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
        
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            CategoryItemEntity entity = (CategoryItemEntity) i.next();
            if (entity.getArea().contains(point)) {
                ExaminationDataElement[] elements = cDataSet.getElements(entity.getSeries(), entity.getCategory());
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
     * Sets the visible range.
     *
     * @param percentVisible The percent of the total range that should be visible.
     */
    public void setVisibleRange(double percentVisible) {
        cDataSet.setVisibleRange(percentVisible);
    }
    
    /**
     * Returns the visible range in percent of the total range.
     *
     * @return The percent of the total range that is visible.
     */
    public double getVisibleRange() {
        return cDataSet.getVisibleRange();
    }
    
    /**
     * Returns the term currently displayed in the barchart.
     *
     * @return The term currently displayed in the barchart.
     */
    public String getTerm() {
        return term;
    }
    
    /**
     * Returns true if percent values are used.
     *
     * @return True if percent values are used.
     */
    public boolean isPercentValuesUsed() {
        return cDataSet.isPercentValuesUsed();
    }     
}
