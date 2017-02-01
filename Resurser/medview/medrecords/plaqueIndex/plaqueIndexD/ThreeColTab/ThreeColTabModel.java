/*
 * ThreeColTabModel.java
 *
 * Created on den 28 november 2002, 17:53
 *
 * $Id: ThreeColTabModel.java,v 1.8 2006/03/15 17:01:27 erichson Exp $
 *
 * $Log: ThreeColTabModel.java,v $
 * Revision 1.8  2006/03/15 17:01:27  erichson
 * Fixing columns (inverting d..m on right side) // Nils
 *
 * Revision 1.7  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.6.2.6  2005/04/24 10:32:43  erichson
 * +split up isCellEditable so that we can use the help functions (isPermDisabled etc) as well
 * + Extracted teeth now stored as 'X'
 * + javadoc
 *
 * Revision 1.6.2.5  2005/04/05 14:31:29  erichson
 * Removed commented-out constructor.
 *
 * Revision 1.6.2.4  2005/03/25 13:40:03  erichson
 * just comments and formatting
 *
 * Revision 1.6.2.3  2005/03/24 15:20:17  erichson
 * Changed "disable" property to "permDisabled" to avoid confusion with component disabled property.
 *
 * Revision 1.6.2.2  2005/03/04 08:33:58  erichson
 * Previous comment for 1.6.2.1 was wrong
 *
 * Revision 1.6.2.1  2005/03/04 08:31:28  erichson
 * Reworked model to use an integer for max input instead of isNumeric, isBoolean booleans.
 * Constructors updated as well.
 * 
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;


import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import medview.medrecords.components.*;
import medview.medrecords.data.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;

import misc.gui.components.*;

/**
 *
 * @author  nader
 */
public class ThreeColTabModel extends AbstractTableModel
{
    
    // CONSTANTS
    
    private static final int DEFAULT_MAX_INPUT = 3;
    
    // MEMBERS
    
    private  int        mMaxInput;
    
    private  String[]   mColumnNames;

    private  int        mTabNumber;

    /*private  boolean    mIsNumeric;

    private  boolean    mIsBoolean;*/

    private  boolean    mSmalList;

    private  boolean    mHasHeader;

    private  boolean[]  mCellEdit = {true,true,true}; // Extracted or not

    private  ArrayList  mValues;

    /**
     * ArrayList containing boolean[] for which cells are disabled. // NE
     */
    private  ArrayList  mPrmntDisableCells;


	// CONSTRUCTORS
    
        
    public ThreeColTabModel(int pMaxInput,int pTabNr,boolean pSmalList,boolean hasHeader, String[] titles)
    {                
        /*mIsNumeric      = isNumeric;

        mIsBoolean      = !isNumeric;*/

        mMaxInput       = pMaxInput;
        
        mTabNumber      = pTabNr;

        mSmalList       = pSmalList;

        mHasHeader      = hasHeader;

        if(titles == null)
        {
            String[] cols = new String[3];
            
            if (PqDFunConst.isLeftSideOfMouth(mTabNumber))
            {
                cols[0] = "d";
                cols[1] = "";
                cols[2] = "m";
            }
            else
            {
                cols[0] = "m";
                cols[1] = "";
                cols[2] = "d";
            }
                        
            buildTableHeader( cols);
        }
        else
        {
            buildTableHeader(titles);
        }

        mValues         = new ArrayList();

        mPrmntDisableCells = new ArrayList();
    }
            

    public ThreeColTabModel(int pMaxInput,int pTabNr,boolean hasHeader, String[] titles)
    {
        this( pMaxInput,pTabNr,false,hasHeader,titles);
    }

    public ThreeColTabModel(int pMaxInput,int pTabNr,boolean hasHeader)
    {
        this( pMaxInput,pTabNr,hasHeader,null);
    }

    public ThreeColTabModel(int pTabNr,boolean hasHeader,boolean pSmalList)
    {
        this(DEFAULT_MAX_INPUT, pTabNr,pSmalList,hasHeader,null); // defaulted to numeric true
    }

    public ThreeColTabModel(int pTabNr,String[] pColNames)
    {
        this(1, pTabNr,true,pColNames); // Defaulted to numeric false
    }


	// METHODS

    private void buildTableHeader(String[] pColNames)
    {
        mColumnNames    = new String[3];

        String vTxt     = null;

        int hedNr       = PqDFunConst.calculateTabNr(mTabNumber);

        for(int i = 0; i < mColumnNames.length; i++)
        {
            if(i % 3 == 0)
            {
                vTxt = pColNames[0];
            }
            else if(i % 3 == 1)
            {
                vTxt = hedNr + pColNames[1];
            }
            else if(i % 3 == 2)
            {
                vTxt = pColNames[2];
            }

            mColumnNames[i] = vTxt;
        }
    }

    public ArrayList getValues()
    {
        return mValues;
    }

    /*
    private boolean isNumeric()
    {
        return mIsNumeric;
    }
    */
    
    public boolean isSmalList()
    {
        return mSmalList;
    }

    /*
    private boolean isBoolean()
    {
        return mIsBoolean;
    }*/
    
    public int getMaxInput()
    {
        return mMaxInput;
    }

    public boolean hasheader()
    {
        return mHasHeader;
    }

    public void addRow()
    {
        Object aRow;

        aRow =  new ThreeColValue();

        mValues.add(aRow);

        int sZ = mValues.size();

        boolean[] tmpArr = {false,false,false};

        mPrmntDisableCells.add(tmpArr);

        fireTableRowsInserted(sZ,sZ);
    }

    public void removeRow(int aRow)
    {
        if(aRow < mValues.size() && aRow > -1)
        {
            mValues.remove(aRow);

            fireTableRowsDeleted(aRow,aRow);
        }
    }

    public String getColumnName(int col)
    {
        if (mColumnNames != null)
        {
            return mColumnNames[col];
        }

        return null;
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then a table catch (contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    /**
     * If a cell can be edited - i e not extracted (gray) or permanently disabled (black)
     */
    public boolean isCellEditable(int row, int col)
    {                
        return (!isToothExtracted(col) && !isPermDisabled(row,col));
    }

    public boolean isToothExtracted(int col)
    {
        return !mCellEdit[col];
    }
    
    public boolean isPermDisabled(int row, int col)
    {
        boolean[] tmpArr =  (boolean[])mPrmntDisableCells.get(row);
        return tmpArr[col];
    }
    
    /*
     * Make a column disable or enable (extracted)
     */
    public void setColEditable(int col, boolean pEditable ) {

        for(int i = 0 ; i < getRowCount(); i++)
        {
            if (pEditable) // re-enabling // NE
                setValueAt(new Integer(-1), i, col);	// Fredrik
            else // disabling // NE                
                setValueAt(new Character('X'), i, col);
        }

        mCellEdit[col] = pEditable;
    }

    /*
     * Make respective col or row or cell Permenently disable
     *
     * Also empties the contents of the columns // NE
     */
    public void permDisableColumn(int colNr)
    {
        for (int i = 0; i < mPrmntDisableCells.size(); i++)
        {
            boolean[] tmpArr =  (boolean[])mPrmntDisableCells.get(i);

            tmpArr[colNr] = true;

            // Empty the contents of the column // NE
            setValueAt(new Integer(-1),i,colNr);
        }
    }

    /**
     * Is cell permanently disabled?
     */ 
    public boolean isCellPermDisabled(int row,int col)
    {
        boolean[] tmpArr =  (boolean[])mPrmntDisableCells.get(row);

        return tmpArr[col];
    }

    public int getTabelNumber()
    {
        return mTabNumber;
    }

    public void setValueAt(Object value, int row, int col)
    {
        if (row >= mValues.size())
        {
        	return;
        }

        ThreeColValue aRow = (ThreeColValue)mValues.get(row);

        if(aRow != null)
        {
            aRow.setCell(col,value);

            fireTableCellUpdated(row, col);
        }
    }

    public int getColumnCount()
    {
        return 3;
    }

    public Object getValueAt(int row, int col)
    {
        ThreeColValue aRow = (ThreeColValue)mValues.get(row);

        if(aRow != null) return aRow.getCell(col);

        return null;
    }

    public ThreeColValue getValue()
    {
        ThreeColValue aRow = (ThreeColValue)mValues.get(0);

        if(aRow != null) return aRow;

        return null;
    }

    public int getRowCount()
    {
        return mValues.size();
    }
        
}
