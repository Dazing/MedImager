/*
 * $Id: BarChartHorizontalCategoryAxis.java,v 1.2 2004/07/22 14:42:51 erichson Exp $
 *
 * Created on den 21 juli 2004, 15:30
 *
 * $Log: BarChartHorizontalCategoryAxis.java,v $
 * Revision 1.2  2004/07/22 14:42:51  erichson
 * Removed the commented code.
 *
 * Revision 1.1  2004/07/22 14:40:53  erichson
 * First check-in. Contains some useless (commented out) code.
 *
 */

package medview.visualizer.jfreechartextensions;

import java.awt.*; // Graphics
import java.awt.geom.*; //  rectangle
import java.util.*;

import com.jrefinery.chart.*;
import com.jrefinery.data.*; // CategoryDataset

import medview.visualizer.gui.Chart;
import medview.visualizer.data.CategoryGraphDataSet;

/**
 * Extension of HorizontalCategoryAxis to limit the length of ticks
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class BarChartHorizontalCategoryAxis extends HorizontalCategoryAxis
{

    private int tickLengthLimit = Integer.MAX_VALUE;
    
    /** Creates a new instance of BarChartHorizontalCategoryAxis */
    public BarChartHorizontalCategoryAxis(String term, int a_tickLengthLimit) 
    {
        super(term);
        setTickLengthLimit(a_tickLengthLimit);
    }
        
    // We extend this to limit the length of the ticks temporarily, without
    // changing the actual data for other use.
   
   public void refreshTicks(Graphics2D g2,
                Rectangle2D plotArea,
                Rectangle2D dataArea)
   {
       CategoryPlot categoryPlot = (CategoryPlot)plot;
       CategoryGraphDataSet cDataSet = (CategoryGraphDataSet) categoryPlot.getCategoryDataset();
       
       int oldLimit = cDataSet.getCategoryLengthLimit();
       
       cDataSet.setCategoryLengthLimit(tickLengthLimit);                     
       super.refreshTicks(g2, plotArea, dataArea);
       cDataSet.setCategoryLengthLimit(oldLimit);             
   }
   
   public void setTickLengthLimit(int newLimit)
   {
      if (newLimit >= 0)
          tickLengthLimit = newLimit;
   }
}
