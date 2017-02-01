/*
 * PlaquePanel.java
 *
 * Created on den 1 november 2002, 10:49
 *
 * $Id: PlaquePanelD.java,v 1.8 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: PlaquePanelD.java,v $
 * Revision 1.8  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.7  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.6.2.12  2005/04/26 12:29:07  erichson
 * Another leftover println
 *
 * Revision 1.6.2.11  2005/04/26 11:27:11  erichson
 * removed leftover println's
 *
 * Revision 1.6.2.10  2005/04/26 10:39:40  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.6.2.9  2005/04/25 10:47:38  erichson
 * Fixes bug: Backing from Furkaturer would move cursor to wrong row in previous PlaquHandler
 *
 * Revision 1.6.2.8  2005/04/24 10:36:48  erichson
 * debug println's disabled
 *
 * Revision 1.6.2.7  2005/04/20 15:03:08  erichson
 * updated moveToNextTable while working on backspace movement.
 *
 * Revision 1.6.2.6  2005/04/05 16:30:17  erichson
 * Major rewrite of event and focus handling, fixes most of the problems so far.
 *
 * Revision 1.6.2.5  2005/03/25 19:40:06  erichson
 * Removed old commented-out layout code.
 *
 * Revision 1.6.2.4  2005/03/25 19:37:18  erichson
 * Things are starting to look good now.
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
* "Main" panel of the plaqueinput (contains PlaqueTablePanelD) // NE
 *
 * @author  nader
 */

import medview.datahandling.examination.tree.*;
import medview.medrecords.tools.*;
import medview.medrecords.models.*;

import java.util.ArrayList;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;
import medview.medrecords.data.*;

/**
* I have absolutely no idea why this class implements MouseListener. See below // NE
 *
 * @see mouseClicked
 */
public class PlaquePanelD extends javax.swing.JPanel 
{
    private StartPlaque             mParent;
    private ThreeColTable           mChoosenTable;
    private PlaqueTablePanelD       mTablePanel;
    private PlaqueButtonPanelD      mButtonPanel;
    private JScrollPane             mScrollPane;
    private PlaqueCategoryModelD    mModel;
    private String                  mTeethLocation;
    private boolean                 mIsUnder;
    private boolean                 mVertical   = false;
	
    /** Creates a new instance of PlaquePanel */
	
    public PlaquePanelD(String pLocation, boolean isUnder, StartPlaque aPanel) 
    {
        super();
        mTeethLocation = pLocation;
        mIsUnder = isUnder;
        mParent = aPanel;
        initComponents();
        
        ThreeColTable startingTable = mTablePanel.getFirstTable();
        setChoosenTable(startingTable); // Select the first row, so that we know were we are originally.
        startingTable.changeSelection(0,0,false,false);
        
    }
    
    public void setVerticalLoop(boolean isVertical)
    {
        mVertical = isVertical;
    }
	
    private void initComponents()
    {
        setLayout(new BorderLayout());
		
        // set up tables
        mTablePanel = new PlaqueTablePanelD(this,mIsUnder);
		
		
        // set up buttons
        mButtonPanel = new PlaqueButtonPanelD(this);
		
        // add panels
        add(mTablePanel, BorderLayout.CENTER);
        add(mButtonPanel, BorderLayout.SOUTH);
    }
    
    /**
		* Moves the cursor to the next ThreColTable.
     * How "next cell" is determined depends on whether we have Vertical input or not - if it's horizontal, we keep the
     * current row, but if it is Vertical, we move to the top left cell of the next cell.
     */
    public void moveCursorToNextTable()
    {
        System.out.println("moveCursorToNextTable->changeSelect");
        if (mVertical)
            moveCursor(mChoosenTable.getRowCount() -1,mChoosenTable.getColumnCount() -1); // Moves to top left cell of next table
        else
			// not vertical. see javadoc comment above
            moveCursor(mChoosenTable.getSelectedRow(), mChoosenTable.getColumnCount() - 1); // Maintains current row
    }
    
    public ThreeColTable getChoosenTable()
    {
        return mChoosenTable;
    }
    
    public PlaqueTablePanelD getTablePanel()
    {
        return mTablePanel;
    }
    
    /**
		* "ChoosenTable" means the currently active ThreeColTable. // NE
     * --
     *
     * Calls from a Tabel with a comoBox When the List is selected
     *            
     */
    public void setChoosenTable(ThreeColTable pTable)
    {        
        
        ThreeColTable oldTable = mChoosenTable;
        
        if(mChoosenTable != pTable) // ChoosenTable is changing
        {
            
            /* if(mChoosenTable != null) // Check the previous chosentable
            {                
                System.out.println("Moving choosentable from " + mChoosenTable.getName());                
            }
            System.out.println("Moving choosenTable to " + pTable.getName());*/
            
            
            mChoosenTable = pTable;                        
            
            mTablePanel.setHandler(pTable.getHandler()); // Update PlaqueTablePanelD with new PlaqueHandler (row of plaque inputs) // NE            
			
            if (oldTable != null)                
            {                                
                CellEditor editor = oldTable.getSelectedCellEditor();
                if (editor != null)
                { 
					// System.out.println(">>>> " + mChoosenTable.getName());
					
					try {
						editor.stopCellEditing(); // Stops combobox from appearing if application loses focus
					}
					catch (Exception e) {
						// Problems occur for extracted teeth when loading from file, but they do not seem to be critical
						System.out.println("PlaquePanelD.setChoosenTable: " + e.getMessage());
					}
					
					//System.out.println("<<<< " + mChoosenTable.getName());
				}
				oldTable.clearSelection();
			} // end oldTable != null            
		} 
		
		/*
		 else 
		 {
			 System.out.println("PlaquePanelD.setChoosenTable: No change, Choosentable still " + mChoosenTable.getName()); // TODO: Remove debug
		 }                
		 */
		
		// Check if the celleditor is open in the new table
		if (mChoosenTable.isPopupVisible())                
		{            
			// request focus for new celleditor
			mChoosenTable.requestCellEditorFocus();
		}
		else
		{
			// popup not visible - requesting focus for the table
			requestFocus(); // either for this or mChoosenTable
		}            
		
		/**
			* changeSelection() / moveCursor() was here before, but should not be.
		 * (Should not be done by setChoosenTable, since table is chosen even if a value is selected)
		 * // NE        
		 */
	}
	
	
	/**
		* Keyboard handling (in StartPlaque) sends input here
	 */
	public void keyboardInput(int value)
	{
		fireButtonClick(value); // Will validate the value before allowing it.
	}
	
	/**
		* Called from PlaqueButtonPanelD when a button is clicked // NE
	 */
	public void  fireButtonClick(int pValue)
	{       
		// System.out.println("Firebuttonclick(" + pValue + ")"); // TODO: Remove
		if(mChoosenTable == null)
		{
			mChoosenTable = mTablePanel.getFirstTable();            
		}                                                        
		
		int aRow = mChoosenTable.getSelectedRow();
		int aCol = mChoosenTable.getSelectedColumn();
		
		// Check if it is a valid input (not exceeding the max input)        
		
		if (pValue <= mChoosenTable.getMaxInput())
		{
			if(aRow < 0) aRow = 0;
			if(aCol < 0) aCol = 0;
			Integer newValue = new Integer(pValue);                                        
			
			javax.swing.table.TableModel tableModel  = mChoosenTable.getModel();           
			
			if(tableModel.isCellEditable(aRow,aCol))
			{
				// Removes combobox remains after using keyboard input.                
				mChoosenTable.editingCanceled(new ChangeEvent(this)); // Remove any combobox so that it won't obscure the cell.        
				
				tableModel.setValueAt(newValue,aRow,aCol);
			}                                                         
			
			mChoosenTable.requestFocus(); // So that the arrow keys are kept up                        
		} 
		else // Not edit_ok
		{            
			mChoosenTable.editingCanceled(new ChangeEvent(this)); // Remove any combobox so that it won't obscure the cell.        
		}
	}
	
	/**
		* Move the cursor in the current choosen table to the next cell
	 */
	public void moveCursor()
	{
		if (mChoosenTable != null)
		{
			int aCol = mChoosenTable.getSelectedColumn();
			int aRow = mChoosenTable.getSelectedRow();
			
			if(aRow < 0) aRow = 0;
			if(aCol < 0) aCol = 0;
			
			moveCursor(aRow,aCol);
		} 
		else
		{
			System.out.println("Warning: Skipping move - mChoosenTable null"); 
		}
		
	}
	
	/**
		* Moves the selection horizontally (regardless of the name...) // NE
	 *
	 * @param pRow current row (to move from)
	 * @param pCol current column (to move from)
	 */ 
	private void moveCursorV(int pRow,int pCol)
	{
		if(pCol < mChoosenTable.getColumnCount() -1)
		{ 
			// Move to next column // NE
			pCol++;
		}
		else
		{ 
			// This was the last column - Move to next table // NE
			ByRef rfRow = new ByRef();
			rfRow.intVal = pRow; // current row
			
			if (mTablePanel != null) {
				setChoosenTable( mTablePanel.getNextTable(mChoosenTable,rfRow) ); // Gets next table, current row detemines whether to loop around in this plaquehandler or go to next
																				  // note that rfRow comes back changed!
				
				pCol = 0;
				pRow = rfRow.intVal;
			}
			else {
				System.out.println("mTablePanel null in moveCursorV");
			}
		}              
		
		//System.out.println("PlaquePanelD.moveCursorV->table.changeSelection");
		if(mChoosenTable.getModel().isCellEditable(pRow,pCol))
			mChoosenTable.changeSelection(pRow,pCol,false,false); // update jtable
		
		else // this cell was not editable, so skip it ( go to the next)
			moveCursorV(pRow,pCol);
	}
	
	
	/**
		* Move the position where next value is going to end up. // NE      
	 * 
	 *
	 * @param pRow current row (to move from)
	 * @param pCol current column (to move from)
	 */ 
	private void moveCursor(int pRow,int pCol)
	{
		
		if(! mVertical)
		{            
			moveCursorV(pRow,pCol); // Moves selection horizontally (regardless of the name...)
			return;
		}
		
		// Otherwise, move vertically...
		
		if(pRow < mChoosenTable.getRowCount() -1) // Ok to move to next row in this Table
		{
			pRow++;
		}
		else if(pCol < mChoosenTable.getColumnCount() -1) // Ok to move to new column 
		{
			pRow = 0;
			pCol++;
		}
		else // Not possible to move more within this ThreeColTable, move to next // NE
		{
			moveToNextTable(false);
			
			pRow = 0;
			pCol = 0;
		}                
		
		//System.out.println("PlaquePanelD.changeSel -> mChoosenTable.changeSel");
		if(mChoosenTable.getModel().isCellEditable(pRow,pCol))
			mChoosenTable.changeSelection(pRow,pCol,false,false);
		else 
			moveCursor(pRow,pCol); // Keep moving, until we find one that isCellEditable
	}
	
	private void moveToNextTable(boolean moveCursor)
	{                
		if(mChoosenTable != null)  
			mChoosenTable.clearSelection();
		
		ThreeColTable nextTable = mTablePanel.getNextTable(mChoosenTable);
		setChoosenTable(nextTable);
		
		if (moveCursor)
		{
			int pRow = 0;
			int pCol = 0;
			
			if(mChoosenTable.getModel().isCellEditable(pRow,pCol))
				mChoosenTable.changeSelection(pRow,pCol,false,false);
			else 
				moveCursor(pRow,pCol); // Keep moving, until we find one that isCellEditable            
		}
	}
	
	public void moveCursorBackward()
	{
		int aCol = mChoosenTable.getSelectedColumn();
		int aRow = mChoosenTable.getSelectedRow();
		
		if(aRow < 0) aRow = 0;
		if(aCol < 0) aCol = 0;
		
		moveCursorBackward(aRow,aCol);
	}
	
	public void moveCursorBackward(int pRow, int pCol)
	{
		/// System.out.println("--- MOVE BACKWARD --- Row: " + pRow + ", Col: " + pCol);
		ThreeColTable currentTable = getChoosenTable();        
		
		if (!mVertical) // backward horizontally
		{            
			if (pCol == 0) // leftmost column
			{                 
				// Go back to previous table!
				ByRef rfRow = new ByRef(); // I don't like this way of doing it, but i won't change it // NE
				rfRow.intVal = pRow; // current row
				moveCursorToPreviousTable(rfRow); // updates choosenTable
				
				pCol = getChoosenTable().getColumnCount() - 1; // Last column in new table
				pRow = rfRow.intVal; // ByRef comes back changed 
			}
			else // not leftmost column, move one column left
			{
				pCol--;
			}                        
		} 
		else 
			// backward vertically
		{ 
			
			if (pRow == 0) // we're at top row.
			{
				pCol--;                
				if (pCol < 0) 
				{
					moveCursorToPreviousTable(null); // don't update cursor yet. Changes choosenTable
					pCol = getChoosenTable().getColumnCount() - 1; // Last column in new table
				}
				pRow = getChoosenTable().getRowCount() - 1;
			}
			else // we're not at top row, just move up 1 step
			{
				pRow--;                
			}
		}
		
		// move done - check validity. if not valid, keep moving
		//System.out.println("--- Checking Row " + pRow + ", Col " + pCol);
		if(mChoosenTable.getModel().isCellEditable(pRow,pCol))
			mChoosenTable.changeSelection(pRow,pCol,false,false);
		else 
		{
			//System.out.println("--- Not editable --- ");
			moveCursorBackward(pRow,pCol); // Keep moving, until we find one that isCellEditable                
		}
		currentTable.editingCanceled(new ChangeEvent(this)); // Removes combobox remains
	}
	
	private void moveCursorToPreviousTable(ByRef pRow) // boolean moveCursor, boolean checkIsEditable)
	{
		// Go back to previous table!                
		ThreeColTable currentTable = getChoosenTable();
		currentTable.clearSelection();        
		
		PlaqueHandler oldHandler = mTablePanel.getHandler();                
		
		if (pRow == null) // top left
		{
			pRow = new ByRef();
			pRow.intVal = 0;
		}
		
		ThreeColTable prevTable = mTablePanel.getPrvTable(currentTable, pRow);  // Move to prev table based on row. Will switch plaque handlers if necesary, otherwise just loop around
																				// Note: pRow comes back changed                
		
		setChoosenTable(prevTable);
		currentTable = prevTable;                
		
		// currentTable.requestFocus();
	}
	
	/**
		* Set which input buttons (0-13) are enabled.
	 */
	public void setEnabledButtons(int enabledMax)
	{
		if (mButtonPanel != null)
			mButtonPanel.setEnabledButtons(enabledMax);        
	}
	
	
	public void setAllEnabled(int tabNr,boolean flag)
	{
		mTablePanel.setAllEnabled( tabNr,flag);
	}
	
	public MRTree buildTree(PlaqueContext pc)
	{
		if (mIsUnder) 
		{
			pc.setJaw(PlaqueContext.LOWER_JAW);
		}
		else 
		{
			pc.setJaw(PlaqueContext.UPPER_JAW);
		}
		MRTree aTree = mTablePanel.buildTree(mTeethLocation, pc);
		return aTree;
	}
	
	public void loadTree(Tree t, PlaqueContext pc)
	{
		if (mIsUnder) 
		{
			pc.setJaw(PlaqueContext.LOWER_JAW);
		}
		else 
		{
			pc.setJaw(PlaqueContext.UPPER_JAW);
		}
		mTablePanel.loadTree(t, mTeethLocation, pc);
	}
	
}
