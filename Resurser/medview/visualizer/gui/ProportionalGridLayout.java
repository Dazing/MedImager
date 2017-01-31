/*
 * ProportionalGridLayout.java
 *
 * Created on October 4, 2002, 2:54 PM
 *
 * $Id: ProportionalGridLayout.java,v 1.3 2002/10/30 15:56:33 zachrisg Exp $
 *
 * $Log: ProportionalGridLayout.java,v $
 * Revision 1.3  2002/10/30 15:56:33  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A layoutmanager that is very similar to GridLayout with the
 * difference that using ProportionalGridLayout the individual columns and
 * rows have individual widths and heights.
 *
 * The width of a column is determined by the widest preferred width of all
 * the components in that column, and the same goes for the height of a row.
 * 
 * If, however, the parent container is bigger than the preferred size of the
 * layoutmanager, then the columns and rows will increase in size proportional
 * to their preferred sizes. 
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ProportionalGridLayout implements LayoutManager {
    
    /** The number of rows. */
    protected int rows;
    
    /** The number of columns. */
    protected int columns;
    
    /**
     * Creates a new instance of ProportionalGridLayout.
     *
     * @param rows The number of rows in the grid.
     * @param columns The number of columns in the grid.
     */
    public ProportionalGridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * This method does nothing for this layoutmanager.
     * 
     * @param name A name.
     * @param comp A component.
     */
    public void addLayoutComponent(String name, Component comp) {
        // do nothing
    }
    
    /**
     * Layouts a container.
     *
     * @param parent The container.
     */    
    public void layoutContainer(Container parent) {
        Component[] components = parent.getComponents();

        Dimension minimumSize = minimumLayoutSize(parent);
        Dimension preferredSize = preferredLayoutSize(parent);
        Dimension actualSize = parent.getSize();
        
        if ((actualSize.width == 0) || (actualSize.height == 0)) {
            return;
        }            
        
        // generate column data
        int[] preferredColumnWidths = new int[columns];
        int[] minimumColumnWidths = new int[columns];
        int[] actualColumnWidths = new int[columns];
        for (int c = 0; c < columns; c++) {
            preferredColumnWidths[c] = calculatePreferredColumnWidth(parent, c);
            minimumColumnWidths[c] = calculateMinimumColumnWidth(parent, c);
            actualColumnWidths[c] = minimumColumnWidths[c];
        }
            
        double widthScale = (double) actualSize.width / (double) preferredSize.width;
        
        if (actualSize.width < minimumSize.width) {             
            // actual width is less than minimum width
            // what to do? do nothing, let them have their minimum widths.
        } else if (actualSize.width < preferredSize.width) {    
            
            // actual width is wider than minimum width but less than the preferred width
            double widthDiff = preferredSize.width - actualSize.width;
            double sumWidthDiff = 0;
            for (int c = 0; c < columns; c++) {
                sumWidthDiff += preferredColumnWidths[c] - minimumColumnWidths[c];
            }
            for (int c = 0; c < columns; c++) {
                double widthDiffProportion = (double)(preferredColumnWidths[c] - minimumColumnWidths[c]) /  sumWidthDiff;
                actualColumnWidths[c] += (int) (widthDiff * widthDiffProportion);
            }            
        } else {                                                
            
            // actual width is as width as the preferred width of wider
            for (int c = 0; c < columns; c++) {
                actualColumnWidths[c] = (int)( (double)preferredColumnWidths[c] * widthScale);
            }
        }
        
        // calculate the column offsets
        int[] columnOffsets = new int[columns];
        int columnOffset = 0;
        for (int c = 0; c < columns; c++) {
            columnOffsets[c] = columnOffset;
            columnOffset += actualColumnWidths[c];
        }

        // generate row data
        int[] preferredRowHeights = new int[rows];
        int[] minimumRowHeights = new int[rows];
        int[] actualRowHeights = new int[rows];
        for (int r = 0; r < rows; r++) {
            preferredRowHeights[r] = calculatePreferredRowHeight(parent, r);
            minimumRowHeights[r] = calculateMinimumRowHeight(parent, r);
            actualRowHeights[r] = minimumRowHeights[r];
        }
            
        double heightScale = (double) actualSize.height / (double) preferredSize.height;
        
        if (actualSize.height < minimumSize.height) {             
            // actual height is lower than minimum height
            // what to do? do nothing, let them have their minimum heights.
        } else if (actualSize.height < preferredSize.height) {    
            
            // actual height is higher than minimum height but lower than the preferred height
            double heightDiff = preferredSize.height - actualSize.height;
            double sumHeightDiff = 0;
            for (int r = 0; r < rows; r++) {
                sumHeightDiff += preferredRowHeights[r] - minimumRowHeights[r];
            }
            for (int r = 0; r < rows; r++) {
                double heightDiffProportion = (double)(preferredRowHeights[r] - minimumRowHeights[r]) /  sumHeightDiff;
                actualRowHeights[r] += (int) (heightDiff * heightDiffProportion);
            }            
        } else {                                                
            
            // actual height is as high as the preferred height of higher
            for (int r = 0; r < rows; r++) {
                actualRowHeights[r] = (int)( (double)preferredRowHeights[r] * heightScale);
            }
        }
        
        // calculate the row offsets
        int[] rowOffsets = new int[rows];
        int rowOffset = 0;
        for (int r = 0; r < rows; r++) {
            rowOffsets[r] = rowOffset;
            rowOffset += actualRowHeights[r];
        }

        // layout the components
theLoop:for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int index = r * columns + c;
                if (index >= components.length) {
                    break theLoop;
                }
                components[index].setSize(actualColumnWidths[c], actualRowHeights[r]);
                components[index].setLocation(columnOffsets[c], rowOffsets[r]);
            }
        }        
    }
    
    /**
     * Calculates the preferred column width.
     *
     * @param parent The container.
     * @param column The column.
     * @return The preferred column width.
     */
    protected int calculatePreferredColumnWidth(Container parent, int column) {
        Component[] components = parent.getComponents();
        
        int preferredColumnWidth = 0;
rowLoop:for (int r = 0; r < rows; r++) {
            int index = r * columns + column;
            if (index >= components.length) {
                break rowLoop;
            }
            int preferredComponentWidth = components[index].getPreferredSize().width;
            preferredColumnWidth = Math.max(preferredColumnWidth, preferredComponentWidth);
        }
        return preferredColumnWidth;
    }

    /**
     * Calculates the preferred row height.
     *
     * @param parent The container.
     * @param row The row.
     * @return The preferred row height.
     */
    protected int calculatePreferredRowHeight(Container parent, int row) {
        Component[] components = parent.getComponents();

        int preferredRowHeight = 0;
colLoop:for (int c = 0; c < columns; c++) {
            int index = row * columns + c;
            if (index >= components.length) {
                break colLoop;
            }
            int preferredComponentHeight = components[index].getPreferredSize().height;
            preferredRowHeight = Math.max(preferredRowHeight, preferredComponentHeight);
        }
        return preferredRowHeight;
    }

    /**
     * Calculates the minimum column width.
     *
     * @param parent The container.
     * @param column The column.
     * @return The minimum column width.
     */
    protected int calculateMinimumColumnWidth(Container parent, int column) {
        Component[] components = parent.getComponents();
        
        int minColumnWidth = 0;
rowLoop:for (int r = 0; r < rows; r++) {
            int index = r * columns + column;
            if (index >= components.length) {
                break rowLoop;
            }
            int minComponentWidth = components[index].getMinimumSize().width;
            minColumnWidth = Math.max(minColumnWidth, minComponentWidth);
        }
        return minColumnWidth;
    }    
    
    /**
     * Calculates the minimum row height.
     *
     * @param parent The container.
     * @param row The row.
     * @return The minimum row height.
     */
    protected int calculateMinimumRowHeight(Container parent, int row) {
        Component[] components = parent.getComponents();

        int minRowHeight = 0;
colLoop:for (int c = 0; c < columns; c++) {
            int index = row * columns + c;
            if (index >= components.length) {
                break colLoop;
            }
            int minComponentHeight = components[index].getMinimumSize().height;
            minRowHeight = Math.max(minRowHeight, minComponentHeight);
        }
        return minRowHeight;
    }
    
    /**
     * Returns the minimum layout size.
     *
     * @param parent The parent container.
     * @return The minimum layout size.
     */
    public Dimension minimumLayoutSize(Container parent) {        
        // find out the minimum width
        int minWidth = 0;
        for (int c = 0; c < columns; c++) {
            int columnMinWidth = calculateMinimumColumnWidth(parent, c);
            minWidth += columnMinWidth;
        }
        
        // find out the minimum height
        int minHeight = 0;
        for (int r = 0; r < rows; r++) {
            int rowMinHeight = calculateMinimumRowHeight(parent, r);
            minHeight += rowMinHeight;
        }        
        return new Dimension(minWidth, minHeight);
    }
    
    /**
     * Returns the preferred layout size.
     *
     * @param parent The parent container.
     * @return The preferred layout size.
     */
    public Dimension preferredLayoutSize(Container parent) {
        // find out the preferred width
        int preferredWidth = 0;
        for (int c = 0; c < columns; c++) {
            int preferredColumnWidth = calculatePreferredColumnWidth(parent, c);
            preferredWidth += preferredColumnWidth;
        }
        
        // find out the preferred height
        int preferredHeight = 0;
        for (int r = 0; r < rows; r++) {
            int preferredRowHeight = calculatePreferredRowHeight(parent, r);
            preferredHeight += preferredRowHeight;
        }        
        return new Dimension(preferredWidth, preferredHeight);
    }
    
    /**
     * This method does nothing for this layoutmanager.
     *
     * @param comp A component.
     */
    public void removeLayoutComponent(Component comp) {
        // do nothing
    }
    
}
