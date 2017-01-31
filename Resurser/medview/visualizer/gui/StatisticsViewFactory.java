/*
 * StatisticsViewFactory.java
 *
 * Created on October 3, 2002, 3:56 PM
 *
 * $Id: StatisticsViewFactory.java,v 1.2 2002/10/30 15:56:34 zachrisg Exp $
 *
 * $Log: StatisticsViewFactory.java,v $
 * Revision 1.2  2002/10/30 15:56:34  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A ViewFactory implementation for StatisticsViews.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class StatisticsViewFactory implements ViewFactory {
    
    /** The term. */
    private String term;
    
    /** 
     * Creates a new instance of StatisticsViewFactory. 
     * 
     * @param term The term.
     */
    public StatisticsViewFactory(String term) {
        this.term = term;
    }
    
    /**
     * Creates a StatisticsView from an ExaminationDataSet.
     *
     * @param dataSet The data set.
     * @return A StatisticsView created from the data set.
     */
    public View createView(ExaminationDataSet dataSet) {
        return new StatisticsView(dataSet, term);
    }
    
    /**
     * Returns the term.
     *
     * @return The term.
     */
    public String getTerm() {
        return term;
    }
}
