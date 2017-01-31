/*
 * StatisticsTableModel.java
 *
 * Created on October 7, 2002, 11:46 AM
 *
 * $Id: StatisticsTableModel.java,v 1.6 2002/10/11 06:57:02 zachrisg Exp $
 *
 * $Log: StatisticsTableModel.java,v $
 * Revision 1.6  2002/10/11 06:57:02  zachrisg
 * Added a little piece of javadoc.
 *
 */

package medview.visualizer.gui;

import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;

import medview.datahandling.aggregation.*;
import medview.visualizer.data.*;

import com.jrefinery.data.*;

/**
 * A tabel model for a statistics table.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class StatisticsTableModel extends AbstractTableModel implements DatasetChangeListener {
    
    /** The category data set. */
    private CategoryGraphDataSet cDataSet;
   
    /**
     * Creates a new instance of StatisticsTableModel.
     * 
     * @param dataSet The data set.
     */
    public StatisticsTableModel(CategoryGraphDataSet dataSet) {
        cDataSet = dataSet;
        cDataSet.addChangeListener(this);
    }
    
    /**
     * Returns the value of the specified position.
     *
     * @param row The row.
     * @param column The column.
     * @return The value at the specified position.
     */
    public Object getValueAt(int row, int column) {
/*        if (row == 0) {
            if (column == 0) {
                // the term value column
                return cDataSet.getTerm();
            } else {
                int series = (column - 1) / 2;
                if ((column - 1) % 2 == 0) {
                    // amount column
                    return cDataSet.getSeriesName(series);
                } else {
                    // percent column
                    return cDataSet.getSeriesName(series) + " (%)";
                }
            }
        }
*/
        if (column == 0) {
            // the term value column
            if (row == getRowCount() - 1) {
                // the totals row
                return DataManager.getInstance().getTotalsString();
            } else {
                // a normal row
                return cDataSet.getCategories().get(row);
            }
        }

        // a normal column
        int series = (column - 1) / 2;
        if (row == getRowCount() - 1) {
            // the totals row
            if ((column - 1) % 2 == 0) {
                // an amount column
                return cDataSet.getExaminationCount(cDataSet.getDataGroup(series)).toString();
            } else {
                // a percent column, return an empty string
                return new String();
            }
        } else {
            // a normal row
            if ((column - 1) % 2 == 0) {
                // an amount column
                return cDataSet.getValue(series, cDataSet.getCategories().get(row)).toString();
            } else {
                // a percent column
                float amount = cDataSet.getValue(series, cDataSet.getCategories().get(row)).floatValue();
                float totalAmount = cDataSet.getExaminationCount(cDataSet.getDataGroup(series)).floatValue();
                float percent = 100.0f * amount / totalAmount;
                return "" + percent;
            }
        }
    }
    
    /**
     * Returns the number of rows in the table.
     *
     * @return The number of rows in the table.
     */
    public int getRowCount() {
        int categoryCount = cDataSet.getCategoryCount();
        if (categoryCount == 0) {
            return 0;
        } else {
            // add one row for the examinations count
            return categoryCount + 1;
        }
    }
    
    /**
     * Returns the name of the specified column.
     *
     * @param column The column.
     * @return The name of the column.
     */
    public String getColumnName(int column) {
//        return new String(" ");
        if (column == 0) {
            // the term value column
            return cDataSet.getTerm();
        } else {
            int series = (column - 1) / 2;
            if ((column - 1) % 2 == 0) {
                // amount column
                return cDataSet.getSeriesName(series);
            } else {
                // percent column
                return cDataSet.getSeriesName(series) + " (%)";
            }
        }
    }
    
    /**
     * Returns the number of columns in the table.
     * 
     * @return The number of columns in the table.
     */
    public int getColumnCount() {
        int seriesCount = cDataSet.getSeriesCount();
        if (seriesCount == 0) {
            return 0;
        } else {
            // one count and one percent column per data group and one value column
            return seriesCount * 2 + 1;
        }
    }
    
    /**
     * Called when the category graph dataset has changed.
     * 
     * @param event The event.
     */    
    public void datasetChanged(DatasetChangeEvent event) {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));       
    }

    /**
     * Sets the aggregation.
     *
     * @param agg The new aggregation.
     */
    public void setAggregation(Aggregation agg) {
        cDataSet.setAggregation(agg);
    }
    
}
