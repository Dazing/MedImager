/*
 * ThreeColTable.java
 *
 * Created on den 28 november 2002, 17:53
 *
 * $Id: ThreeColTable.java,v 1.10 2006/09/13 22:00:08 oloft Exp $
 *
 * $Log: ThreeColTable.java,v $
 * Revision 1.10  2006/09/13 22:00:08  oloft
 * Added Open functionality
 *
 * Revision 1.9  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.8.2.10  2005/04/26 11:47:06  erichson
 * Shift+number key to input 10-15
 *
 * Revision 1.8.2.9  2005/04/26 11:25:17  erichson
 * Overridden the normal keyboard handling in JComboBox (typing 1 when 1 was selected would give 10, etc)
 *
 * Revision 1.8.2.8  2005/04/26 10:39:41  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.8.2.7  2005/04/25 11:17:03  erichson
 * Enter key now moves forward.
 *
 * Revision 1.8.2.6  2005/04/24 10:35:23  erichson
 * * Fixed valueChanged so that cursor is only moved when value is changed in active table (prevents the bug where cursor would move a lot if you extracted a tooth)
 * * changed order of moving cursor and setting the table extracted (fixes a nullPointerException problem)
 * * Changed so that extracted teeth are not left out of the built tree (their terms would not appear before) - now saved as LExtracted instead
 *
 * Revision 1.8.2.5  2005/04/05 16:29:28  erichson
 * Major rewrite of event and focus handling, fixes most of the problems so far.
 *
 * Revision 1.8.2.4  2005/03/25 13:41:14  erichson
 * Fix to avoid choosenTable going back after making the final input in a ThreeColTable with a combobox
 *
 * Revision 1.8.2.3  2005/03/24 15:18:52  erichson
 * - CustomCellRenderer reworked
 * - Cleanup of "disable" code - property is now known as permDisable to avoid confusion
 *
 * Revision 1.8.2.2  2005/03/04 08:46:11  erichson
 * selections/setChoosenTable finally debugged (itemStateChanged vs actionPerformed etc)
 *
 * Revision 1.8.2.1  2005/02/24 12:11:39  erichson
 * Highlighting
 *
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;

/**
*
 * @author  nader
 */
import java.util.*;

import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.*;

import medview.medrecords.models.*;
import medview.datahandling.examination.tree.*;
import medview.medrecords.tools.*;
import medview.medrecords.components.*;

import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;

import misc.gui.components.*;

public class ThreeColTable extends JTable implements ItemListener
{
    private ThreeColTabModel mModel;
	
    private PlaqueHandler    mHandler;
	
    private Color            mColor;
	
    private static int rowHeight;
    
    private MyCellEditor MyCellEditor;
    
    private MouseAdapter comboBoxMouseListener = new MouseAdapter() 
    {
        public void mousePressed(MouseEvent me)
	{
            // System.out.println("Mouse pressed on combobox");                                                
            
            Point p = me.getPoint();
            int col = columnAtPoint(p);
            int row = rowAtPoint(p);
            
            //System.out.println("mousepressed on row " + row + ", col " + col);
            //System.out.println("Editable: " +  ThreeColTable.this.isCellEditable(row,col));
            
            if (ThreeColTable.this.isCellEditable(row,col))
				mHandler.getParentPlaquePanel().setChoosenTable(ThreeColTable.this);                        
	}
    };
    
    
    public ThreeColTable(ThreeColTabModel aMd)
    {
        super(aMd);
		
        mModel = aMd;
		
        setRowSelectionAllowed(false);
		
        setColumnSelectionAllowed(false);
		
        if(aMd.hasheader())
        {
            makeHeader();
        }
        else
        {
            setTableHeader(null);
        }
		
        setCellSelectionEnabled(false);
		
        makeLists();
		
        rowHeight = getRowHeight();
        
        
        // Lyssna efter ändringar (inmatade värden)
        mModel.addTableModelListener(new TableModelListener()
									 {
            public void tableChanged(TableModelEvent e)
		{                                
                
                int row = e.getFirstRow();
                int col = e.getColumn();                                                                
                
                if ((row == getSelectedRow()) && (col == getSelectedColumn()))
                {
                    // The currently selected cell was changed
					
                    PlaquePanelD parentPanel = mHandler.getParentPlaquePanel();
                    if (parentPanel != null)
                        parentPanel.moveCursor();                    
                }                
		}
									 });
        
        addMouseListener(comboBoxMouseListener);
		
    }
	
    private void makeHeader()
    {
        JTableHeader    aHeader = this.getTableHeader();
		
        aHeader.addMouseListener(new java.awt.event.MouseAdapter()
								 {
			public void mouseClicked(java.awt.event.MouseEvent e)
		{
				tableHeaderClicked();
		}
								 });
    }
	
    
    public int getMaxInput()
    {
        return mModel.getMaxInput();
    }
    
    private void makeLists()
    {
        for (int i = 0; i < mModel.getColumnCount(); i++)
        {
            TableColumn aCol = this.getColumnModel().getColumn(i);
			
            aCol.setPreferredWidth(30);
			
            setupCellRenderer(aCol,i);
        }
        setupCellEditor();
    }
	
    private void setupCellEditor()
    {
        MyComboBox comboBox = new MyComboBox(); // JComboBox with overridden key handling
        comboBox.setMaximumRowCount(17);        
        
        // Add the numbers to the combobox (up to maxinput)
        for (int i = -1; i <= mModel.getMaxInput(); i++)
        {
            comboBox.addItem(new Integer(i));
        }
		
        comboBox.addPopupMenuListener(new MyPopupMenuListener());        
        comboBox.addItemListener(this); // Listens for item selections
		
        /*
         * Keyboard handling is different - normal JCombobox keyboard handling is disabled, see MyComboBox for details // NE
         */
        
        comboBox.addKeyListener(new KeyAdapter()
								{
            public void keyTyped(KeyEvent ke)
		{
                ke.consume(); // Normal key handling always closes the combo box
                
                PlaquePanelD parentPanel = mHandler.getParentPlaquePanel();
                
                char pressedKey = ke.getKeyChar();                                
                //System.out.println("pressedKey = " + pressedKey);
                
                int intValue = -1;
                
                if (pressedKey == '0')
                    intValue = 0;
                else if ((pressedKey >= '1') && (pressedKey <= '9'))
                    intValue = (pressedKey - '1') +1;                
                else
                    switch(pressedKey) // Shift + number keys
                    {
                        case '!':
                            intValue = 11;
                            break;
                        case '"':
                            intValue = 12;
                            break;
                        case '#':
                            intValue = 13;
                            break;
                        case '¤':
                            intValue = 14;
                            break;
                        case '%':
                            intValue = 15;
                            break;
                        case '=':
                            intValue = 10;
                            break;
                    }                                                
						
						//System.out.println("intvalue = " + intValue);
						
						if ((intValue != -1) && (intValue <= getMaxInput()))
							parentPanel.keyboardInput(intValue);                
		}
								});
        
        // Set MyCellEditor as the default editor everywhere in this table
        cellEditor = new MyCellEditor(comboBox);
        setCellEditor(cellEditor);  
        setDefaultEditor(getColumnClass(0),cellEditor);
        
    }    
    
    /**
		* Disables the normal keyboard-selection in open combobox. Fixes the problem where pressing '1' when the
     * selection is already 1, would yield 10, 11 etc.
     */
    private class MyComboBox extends JComboBox
    {
        public boolean selectWithKeyChar(char keyChar) 
	{
            return false;
	}
    }
    
    private void setupCellRenderer(TableColumn aColumn,int colNr)
    {        
        aColumn.setCellRenderer(new CustomTableCellRenderer());        
    }        
    
    static int popupcount = 0;
    private class MyPopupMenuListener implements PopupMenuListener
    {
        
        int number;
        public MyPopupMenuListener()
        {            
            number = popupcount;
            popupcount++;
        }
        
        public void popupMenuCanceled(PopupMenuEvent e) { }        
		
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
        {
            //System.out.println(number + "Will invisible");            
            //getSelectedCellEditor().stopCellEditing();            
            cellEditor.cancelCellEditing(); 
        }
		
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }            
		
    }
    
    /**
		* Called when clicked upon header.
     */
    private void tableHeaderClicked()
    {
        boolean b = this.isEnabled();
		
        if(b)
        {
            int ans = Ut.yesNoQuestion("Do you want to mark tooth " + getColumnName(1) + " as extracted?"); 
			
            if(ans == Ut.Yes)
            {
                int toothToExtractNr = mModel.getTabelNumber();                
                
                // Check if the current cursor is in the row that was marked extracted. If so, move it to the next table.
                
                if (isChoosenTable())
                {                                            
                    mHandler.getParentPlaquePanel().moveCursorToNextTable(); // Moves the selection to the next table
                }                
                
                setToothExtracted(toothToExtractNr,false);
            }
        }
		
        else
        {
            setToothExtracted(mModel.getTabelNumber(),true);
        }
    }
	
    /**
		* Set a tooth to extracted or not
     */
    private void setToothExtracted(int toothNr, boolean extracted)
    {
        mHandler.setAllEnabled(toothNr, extracted);
    }
    
    public boolean isChoosenTable()
    {
        return (mHandler.getParentPlaquePanel().getChoosenTable() == this);
    }
	
    /**
		* Enable/disable all columns in this ThreeColTable (for Extracted teeth)
     */
    public void makeEnabled(boolean flag)
    {
        for(int i = 0; i < mModel.getColumnCount(); i++)
        {
            mModel.setColEditable(i,flag);
        }
		
        if (flag)
        {
            this.setBackground(Color.white);
        }
		
        else
        {
            this.setBackground(mColor);
        }
		
        this.setEnabled(flag);
    }
	
    /**
		* Permanently disable ALL columns in this table.
     * @see permDisableColumn(int)
     */
    public void permDisableAllColumns()
    {
        for(int i = 0; i < mModel.getColumnCount(); i++)
        {
            TableColumn aCol = this.getColumnModel().getColumn(i);
			
            permDisableColumn(i);
        }
		
        this.setCellSelectionEnabled(false);
    }
	
    
    /**
		* Permanently disables a column, so that it is "blacked out" and can't be used.
     * This is used for TableHandlerLosning, TableHandlerFurkaturer and TableHandlerFurkaturerUnder. 
     */
    public void permDisableColumn(int colNr)
    {
        TableColumn aColumn = this.getColumnModel().getColumn(colNr);
		
        mModel.permDisableColumn(colNr);
		
        /* Renderer update code used to be here, now removed since it's not necessary, 
			as CustomCellRenderer checks for permDisable property // NE */
        
    }
	
    /**
		* Custom celleditor to customize focus handling and make sure the old editor is removed 
     **/
    public class MyCellEditor extends DefaultCellEditor 
        implements FocusListener, CellEditorListener
    {
        private JComboBox comboBox;
        private final static String enterActionName = "ENTER_ACTION";
        
        /**
			* Enter key action for Cell Editor. The default action is to move vertically, so we want to override it with our MoveCursor.
         */
        private final AbstractAction enterAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
		{
                mHandler.getParentPlaquePanel().moveCursor(); 
		}
        };
        
        public MyCellEditor(JComboBox box)
        {
            super(box);
            comboBox = box;
            comboBox.addFocusListener(this);            
            addCellEditorListener(this);
            
            // Override enter key action
            
            KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);            
            ThreeColTable.this.getInputMap().put(enterKeyStroke, enterActionName);                        
            ThreeColTable.this.getActionMap().put(enterActionName, enterAction);
        }
        
        public boolean isPopupVisible()
        {
            return comboBox.isPopupVisible();            
        }
        
        /**
			* Make the combobox request focus.
         */
        public void requestComboBoxFocus()
        {            
            comboBox.requestFocus();                         
        }
        
        public void showPopup()
        {
            comboBox.showPopup();
        }
        
        public void focusGained(FocusEvent fe)
        {
            // System.out.println("Focusgained by celleditor in " + getName()); //TODO: Remove
        }
		
        public void focusLost(FocusEvent fe)
        {            
            // System.out.println("Focus lost by celleditor in " + getName() + ", stopping editing."); // TODO: Remove
            cancelCellEditing(); // Cancel, not stop (stop fires changedevent, which will move the focus)        
								 //repaint();
        }
        
        public void editingCanceled(ChangeEvent e)
        {
            //System.out.println("Editing cancelled in celleditor in " + getName()); // TODO: Debug
            //comboBox.hidePopup();
        }
        
        public void editingStopped(ChangeEvent e)
        {
            //System.out.println("Editing stopped in celleditor in " + getName()); // TODO: Debug
            //comboBox.hidePopup();
        }
        
    }
    
    public CellEditor getSelectedCellEditor()
    {
        int row = getSelectedRow();
        int col = getSelectedColumn();
        
        try {
            return getCellEditor(getSelectedRow(),getSelectedColumn());
        } catch (ArrayIndexOutOfBoundsException e)
        {
            return null;
        }
    }
    
    public void requestCellEditorFocus()
    {
        MyCellEditor editor = (MyCellEditor) getSelectedCellEditor();
        if (editor != null)
            editor.requestComboBoxFocus();
    }
    
    public boolean isPopupVisible()
    {
        CellEditor ce = getSelectedCellEditor();
        if (ce instanceof MyCellEditor)
        {
            MyCellEditor editor = (MyCellEditor) ce;
            if (editor != null)
                return editor.isPopupVisible();
            else
                return false;
        }    
        else
            //System.out.println("WARNING: isPopupVisible(): cellEditor instanceof " + ce.getClass());
            return false;
    }
    
    public void setHandler(PlaqueHandler pHandler)
    {
        mHandler = pHandler;
		
        mColor = pHandler.getParentPlaquePanel().getBackground(); // was mColor = pHandler.getBackground();
    }
	
    public PlaqueHandler getHandler()
    {
        return mHandler;
    }
	
    /**
		* Called when an item is selected in the combobox. 
     */
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            // the below code is not necessary since actionlistener does this // NE
            
            /*
             mHandler.getParentPlaquePanel().setChoosenTable(this); // Selects the new highlight                                     
             System.out.println("combobox contents changed ->moveCursor");
             mHandler.getParentPlaquePanel().moveCursor();        
             */
        }
    }
	
    public void buildTree(MRTree aTree,int pRow, PlaqueContext pc, int toothIndex)
    {
        for(int i = 0; i < mModel.getColumnCount(); i++)
        {
            String aHdName   = mModel.getColumnName(i);
			
            //System.out.println("debug: ahdname = " + aHdName + ", facetype = " + PlaqueContext.faceTypeForString(aHdName));
            pc.setFace(PlaqueContext.faceTypeForString(aHdName));
			
            pc.setTooth(toothIndex);
			
            //if(mModel.isCellEditable(pRow,i)) // previous version - extracted tooth didn't even get terms!
            if (!mModel.isPermDisabled(pRow, i)) // Only save if it isn't perm-disabled
            {
                Object aVal      = mModel.getValueAt(pRow,i);
				
                String strVal    = null;
				
                if (aVal instanceof Integer)
                {
					if (((Integer)aVal).intValue() == -1)
					{
						strVal = "Unchecked";
					}
					else
					{
						strVal = aVal.toString();
					}
                }
                else if (aVal instanceof Character)
                {
					if ( Character.toUpperCase(((Character) aVal).charValue()) == 'X')
						strVal = "Extracted";
					else
						strVal = aVal.toString();
                }
                else
                {
					System.err.println("ThreeColTable: warning: unknown type value detected");
					
					strVal       = aVal .toString();
                }
				
                // <=========================================
				
                MRTree colTree   = aTree.addNode(pc.representedTermName());
				
                if(strVal != null && strVal.length() > 0)
                {
                    colTree.addLeaf(strVal);
                }
                else
                    System.out.println("buildTree: Skipping threeColTable " + getName()); // TODO: remove this - Debug
            }
        }
    }
    
	public void loadTree(Tree t,int pRow, PlaqueContext pc, int toothIndex)
    {
        for(int i = 0; i < mModel.getColumnCount(); i++)
        {
            String aHdName   = mModel.getColumnName(i);
			
            //System.out.println("debug: ahdname = " + aHdName + ", facetype = " + PlaqueContext.faceTypeForString(aHdName));
            pc.setFace(PlaqueContext.faceTypeForString(aHdName));
			
            pc.setTooth(toothIndex);
			
            //if(mModel.isCellEditable(pRow,i)) // previous version - extracted tooth didn't even get terms!
            if (!mModel.isPermDisabled(pRow, i)) // Only save if it isn't perm-disabled
            {
				String term = pc.representedTermName();
								
				Tree aNode = t.getNode(term);
				
				if(aNode != null)
				{
					// Should really be only one value but..., i.e., there's no error handling
					for (Enumeration aLeaf = aNode.getChildrenEnumeration(); aLeaf.hasMoreElements(); ) 
					{
						Object valObj = null;
						Tree   theLeaf     = (Tree) aLeaf.nextElement();
						String valString      = theLeaf.getValue();
						
						//System.out.println("term: " + term + ", value: " + valString);
						//vic.putPreset(theVal);
						
						if (valString.equals("Extracted")) {
							valObj = new Character('X');
						}
						else if (valString.equals("Unchecked")) {
							valObj = new Integer(-1);
						}
						else {
							try {
								int numVal = Integer.parseInt(valString);
								valObj = new Integer(numVal);
							}
							catch (NumberFormatException ne) {
								// Simply insert the string from the tree?
								valObj = valString;
							}
						}
						mModel.setValueAt(valObj, pRow, i);
					}
				}        
				
            }
        }
    }
	
    /**
		* Custom renderer for cells in ThreeColTable.
     * 
     */
	private class CustomTableCellRenderer extends DefaultTableCellRenderer
	{                                                   
		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column)
	{                    
			String name; 
			if (table instanceof ThreeColTable)
				name = ((ThreeColTable) table).getName();
			else
				name = "BUG_unknown_tooth"; // Fallback. TODO: Fix this
			
			if ((value instanceof Integer) && (((Integer)value).intValue() == -1)) // -1 is "missing value", so draw empty cell
				value = "";
			
			Component c;
			c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);                        
			if (mModel.isCellPermDisabled(row,column)) // For example in Furkaturer
				c.setBackground(Color.BLACK);
			else if (!mModel.isCellEditable(row,column)) // Extracted teeth are not editable
				c.setBackground(Color.LIGHT_GRAY);
			else if ((row == getSelectedRow()) && (column == getSelectedColumn()))
				c.setBackground(Color.YELLOW);
			else
				c.setBackground(Color.WHITE);
			return c;
	}		
	}                
	
	public static int getLastRowHeight() 
	{
		return rowHeight;
	}                
	
	/* Debug method */
	public String getName()
	{
		String className = mHandler.getPlaqueHandlerName();
		
		return className + "-" + mModel.getColumnName(1);
	}
	
	/** 
		* Debug method by nils. Should be removed later.
		*
		* Just overrides changeSelection in JTable so that we can monitor when
		* selections are done.
		*
		* Comment this out before release
		*/
	/*public void changeSelection(int a, int b, boolean c, boolean d)
	{
		super.changeSelection(a,b,c,d);
		System.out.println("changeSel detector: Selection changed to row " + a + ", col " + b);
	}*/
}
