/*
 * BarChartViewFactory.java
 *
 * Created on September 16, 2002, 2:24 PM
 *
 * $Id: BarChartViewFactory.java,v 1.7 2002/10/30 15:56:31 zachrisg Exp $
 *
 * $Log: BarChartViewFactory.java,v $
 * Revision 1.7  2002/10/30 15:56:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A ViewFactory for barcharts.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class BarChartViewFactory  implements ViewFactory {
    
    /** The term to visualize. */
    private String term;
    
    /** True if the bars should be horizontal. */
    private boolean horizontal;
    
    /** True if the bars should be stacked. */
    private boolean stacked;
    
    /** True if the values should be in percent. */
    private boolean percent;
    
    /** 
     * Creates a new instance of BarChartViewFactory.
     *
     * @param term The term to visualize.
     * @param horizontalBars True if the bars should be horizontal.
     * @param stackedBars True if the bars should be stacked.
     * @param percentValues True if the values should be in percent.
     */
    public BarChartViewFactory(String term, boolean horizontalBars, boolean stackedBars, boolean percentValues) {
        this.term = term;
        horizontal = horizontalBars;
        stacked = stackedBars;
        percent = percentValues;
    }

    /**
     * Creates a BarChartView from a ExaminationDataSet.
     *
     * @param dataSet The ExaminationDataSet.
     * @return The BarChartView created from the data set.
     */
    public View createView(ExaminationDataSet dataSet) {
        return new BarChartView(dataSet, horizontal, stacked, percent, term);
    }
    
    
    /**
     * Returns the term.
     *
     * @return The term.
     */
    public String getTerm() {
        return term;
    }
    
    /**
     * Returns true if the bars are horizontal.
     *
     * @return True if the bars are horizontal.
     */
    public boolean isHorizontalBarsUsed() {
        return horizontal;
    }
    
    /**
     * Returns true if stacked bars are used.
     *
     * @return True if stacked bars are used.
     */
    public boolean isStackedBarsUsed() {
        return stacked;
    }
    
    /**
     * Returns true if percent values are used.
     *
     * @return True if percent values are used.
     */
    public boolean isPercentValuesUsed() {
        return percent;
    }
    
}
