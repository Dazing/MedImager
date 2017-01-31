/*
 * ChartButtonFactory.java
 *
 * Created on September 18, 2002, 11:27 AM
 *
 * $Id: ChartButtonFactory.java,v 1.9 2002/11/26 13:26:10 erichson Exp $
 *
 * $Log: ChartButtonFactory.java,v $
 * Revision 1.9  2002/11/26 13:26:10  erichson
 * changed loadIcon calls to loadVisualizerIcon
 *
 * Revision 1.8  2002/11/06 09:44:36  zachrisg
 * Changed the summary icon.
 *
 * Revision 1.7  2002/11/06 09:26:05  zachrisg
 * Added support for the summary view.
 *
 * Revision 1.6  2002/10/30 10:32:53  zachrisg
 * Added tooltips to buttons and removed text.
 *
 * Revision 1.5  2002/10/17 11:42:51  erichson
 * added createChartButton (ImageView)
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A factory class for the chart buttons on the main toolbar.
 *
 * @author G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ChartButtonFactory {
    
    /** Creates a new instance of ChartButtonFactory */
    protected ChartButtonFactory() {
    }
    
    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(ScatterPlotViewFactory viewFactory) {
        ChartButton chartButton;
        // the scatterplot button
        try {
//            chartButton = new ChartButton("X:" + viewFactory.getXAxisTerm() + ", Y:" + viewFactory.getYAxisTerm(), ApplicationManager.getInstance().loadVisualizerIcon("scatterPlot24.png"), viewFactory);
            chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("scatterPlot24.png"), viewFactory);
        } catch (java.io.IOException e) {            
            chartButton = new ChartButton("ScatterPlot X:" + viewFactory.getXAxisTerm() + ", Y:" + viewFactory.getYAxisTerm(), viewFactory);
        }
        chartButton.setToolTipText("Drag groups or examinations here for a scatterplot.");
        return chartButton;
    }

    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(BarChartViewFactory viewFactory) {
        ChartButton chartButton;
        // the scatterplot button
        if (viewFactory.isHorizontalBarsUsed()) {
            try {
//                chartButton = new ChartButton("TERM:" + viewFactory.getTerm(), ApplicationManager.getInstance().loadVisualizerIcon("horizontalBarChart24.png"), viewFactory);
                chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("horizontalBarChart24.png"), viewFactory);
            } catch (java.io.IOException e) {            
                chartButton = new ChartButton("Horizontal BarChart TERM:" + viewFactory.getTerm(), viewFactory);
            }
        } else {
            try {
//                chartButton = new ChartButton("TERM:" + viewFactory.getTerm(), ApplicationManager.getInstance().loadVisualizerIcon("verticalBarChart24.png"), viewFactory);
                chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("verticalBarChart24.png"), viewFactory);
            } catch (java.io.IOException e) {            
                chartButton = new ChartButton("Vertical BarChart TERM:" + viewFactory.getTerm(), viewFactory);
            }            
        }
        chartButton.setToolTipText("Drag groups or examinations here for a barchart.");
        return chartButton;
    }

    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(TableViewFactory viewFactory) {
        ChartButton chartButton;
        try {
//            chartButton = new ChartButton("Table", ApplicationManager.getInstance().loadVisualizerIcon("table24.png"), viewFactory);
            chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("table24.png"), viewFactory);
        } catch (java.io.IOException e) {
            chartButton = new ChartButton("Table", viewFactory);
        }            
        chartButton.setToolTipText("Drag groups or examinations here for a table.");
        return chartButton;
    }

    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(StatisticsViewFactory viewFactory) {
        ChartButton chartButton;
        try {
//            chartButton = new ChartButton("TERM:" + viewFactory.getTerm(), ApplicationManager.getInstance().loadVisualizerIcon("sigma24.png"), viewFactory);
            chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("sigma24.png"), viewFactory);
        } catch (java.io.IOException e) {
            chartButton = new ChartButton("Frequency TERM:" + viewFactory.getTerm(), viewFactory);
        }            
        chartButton.setToolTipText("Drag groups or examinations here for a frequency report.");
        return chartButton;
    }
    
    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(ImageViewFactory viewFactory) {
        ChartButton chartButton;        
        try {
//            chartButton = new ChartButton("Image view", ApplicationManager.getInstance().loadVisualizerIcon("picture24.png"),viewFactory);
            chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("picture24.png"),viewFactory);
        } catch (java.io.IOException e) {
            chartButton = new ChartButton("Image view",viewFactory);
        }
        chartButton.setToolTipText("Drag groups or examinations here to view the images.");
        return chartButton;
    }
    
    /**
     * Creates a new ChartButton.
     *
     * @param viewFactory The ViewFactory to create the button from.
     * @return A new ChartButton.
     */
    public static ChartButton createChartButton(SummaryViewFactory viewFactory) {
        ChartButton chartButton;        
        try {
            chartButton = new ChartButton("", ApplicationManager.getInstance().loadVisualizerIcon("paper24.png"),viewFactory);
        } catch (java.io.IOException e) {
            chartButton = new ChartButton("Summary view",viewFactory);
        }
        chartButton.setToolTipText("Drag groups or examinations here to view a summary report.");
        return chartButton;            
    }
}
