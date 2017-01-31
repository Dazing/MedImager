/*
 * ScatterPlotViewFactory.java
 *
 * Created on July 25, 2002, 4:22 PM
 *
 * $Id: ScatterPlotViewFactory.java,v 1.5 2002/10/30 15:56:33 zachrisg Exp $
 *
 * $Log: ScatterPlotViewFactory.java,v $
 * Revision 1.5  2002/10/30 15:56:33  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A ViewFactory implementation for creating ScatterPlotViews.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ScatterPlotViewFactory implements ViewFactory {
    
    /** The term on the x-axis. */
    private String xTerm;
    
    /** The term on the y-axis. */
    private String yTerm;
    
    /** Values in percent. */
    private boolean percent;
    
    /**
     * Creates a new instance of ScatterPlotViewFactory.
     * 
     * @param xAxisTerm The term on the x-axis.
     * @param yAxisTerm The term on the y-axis.
     * @param percentValues True if values should be in percent.
     */
    public ScatterPlotViewFactory(String xAxisTerm, String yAxisTerm, boolean percentValues) {
        xTerm = xAxisTerm;
        yTerm = yAxisTerm;
        percent = percentValues;
    }
    
    /**
     * Creates a ScatterPlotView from an ExaminationDataSet.
     *
     * @param dataSet The data set.
     * @return The created ScatterPlotView.
     */
    public View createView(ExaminationDataSet dataSet) {
        return new ScatterPlotView(dataSet, xTerm, yTerm, percent);
    }
    
    /**
     * Returns the x-axis term.
     *
     * @return The x-axis term.
     */
    public String getXAxisTerm() {
        return xTerm;
    }
    
    /**
     * Returns the y-axis term.
     *
     * @return The y-axis term.
     */
    public String getYAxisTerm() {
        return yTerm;
    }
    
    /**
     * Returns true if values should be in percent.
     *
     * @return True if values should be in percent.
     */
    public boolean isPercentValuesUsed() {
        return percent;
    }
}
