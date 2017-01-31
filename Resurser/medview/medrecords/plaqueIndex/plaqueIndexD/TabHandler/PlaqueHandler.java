/*
 * PlaqueHandler.java
 *
 * Created on den 9 december 2002, 14:23
 *
 * $Id: PlaqueHandler.java,v 1.9 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: PlaqueHandler.java,v $
 * Revision 1.9  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.8  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.7.2.9  2005/04/26 10:39:40  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.7.2.8  2005/04/24 10:36:07  erichson
 * Mini-update to javadoc
 *
 * Revision 1.7.2.7  2005/04/20 15:04:56  erichson
 * updated getPrvTable.
 *
 * Revision 1.7.2.6  2005/04/05 16:37:22  erichson
 * Added some javadoc.
 *
 * Revision 1.7.2.5  2005/04/05 16:33:03  erichson
 * Reduced name cell widths, to fit better in 1024x768
 *
 * Revision 1.7.2.4  2005/03/29 08:13:54  erichson
 * Updated prefSize calculation
 *
 * Revision 1.7.2.3  2005/03/25 19:35:13  erichson
 * adjusted preferredSize to make things take less space. Added a constant to adjust the spacing later.
 *
 * Revision 1.7.2.2  2005/03/25 13:46:48  erichson
 * Cleaned things up a bit.
 *
 * Revision 1.7.2.1  2005/03/24 15:23:45  erichson
 * removed isNumeric and preferredSize
 *
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

/**
 *
 * PlaqueHandler: Base class for plaque input rows, such as Plaque, Poche, Blödning, Furkaturer and Lösning. // NE
 *
 * @author  nader
 */

import medview.datahandling.examination.tree.*;
import medview.medrecords.tools.*;
import medview.medrecords.models.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;

import ise.java.awt.*;

public abstract class PlaqueHandler // extends javax.swing.JPanel
{        
    
    private final int maxInput; // Limits which buttons can be used. Usually 15 (poche or lösning) or 3 (the rest)
    
    public static final int HALF_TEETH_AMOUNT = PqDFunConst.NuOfTeeth / 2;
    
    /* If we want to make things a bit sparse later... */
    private static final int EXTRA_SPACE_BELOW_EACH_TABLEROW = 0;
    
    protected ArrayList  mModels = new ArrayList();
    protected ArrayList  mTables = new ArrayList(); // ArrayList of ThreeColTables

    protected int height_modification = 0;
    
    /* How many rows the tables are made up of */
    protected int         mRowNr;   

    protected Object[][]  mFstColData;
    protected Object[][]  mSecColData;

    protected String[]    mFstColName = {"State"};
    protected String[]    mSecColName = {"Teeth"};

    protected PlaquePanelD mParent;

    protected JPanel leftPanel, rightPanel, titlePanel;
    
    private int scrollPaneAdjustment;
    
    /* --- Constructors --- */     
    
    /**
     * Constructs PlaqueHandler with 0-3
     */
    public PlaqueHandler()
    {
        super();
        maxInput = 3;
    }
    
    public PlaqueHandler(int maxInput)
    {
        super();
        this.maxInput = maxInput;
    }
    
    public Component getLeftTeethComponent() { return leftPanel; }
    public Component getRightTeethComponent() { return rightPanel; }
    public Component getTitleComponent() { return titlePanel; }
    
    
    public int getRowCount()
    {
        return mRowNr;
    }
    
    protected void initComponents(PlaquePanelD pPanel)
    {
        //scrollPaneAdjustment = mRowNr * - ThreeColTable.getLastRowHeight();
        scrollPaneAdjustment = ThreeColTable.getLastRowHeight();
        final int noOfColumns = 2 + PqDFunConst.NuOfTeeth; // Teeth, two initial columns, and a separator
                
        mParent = pPanel;

        titlePanel = new JPanel(new GridLayout(1,2));
        leftPanel = new JPanel(new GridLayout(1,7));
        rightPanel = new JPanel(new GridLayout(1,7));

        /* Put the component together from title columns and tooth columns // NE */
        addTitleColumns(titlePanel);        
        
        addInputColumns(leftPanel,0,HALF_TEETH_AMOUNT); // Add the left set of teeth        
        addInputColumns(rightPanel,HALF_TEETH_AMOUNT,HALF_TEETH_AMOUNT); // Add the rest of the teeth        
                
    }
    
    /**
     * Update all teeth in a table to be enabled/disabled (flag) (aka extracted)
     */
    public void setAllEnabled(int tabNr, boolean flag)
    {        
        mParent.setAllEnabled(tabNr,flag);
    }

    /** 
     * Sets one of the Tables in this PlaqueHandler enabled or disabled (disabled = tooth extracted) // NE
     */ 
    public void makeTableEnabled(int tabNr,boolean flag)
    {
        ((ThreeColTable) mTables.get(tabNr)).makeEnabled(flag);
    }

    /**
     * Add input columns to a container
     */
    protected void addInputColumns(Container container, int firstColumn, int columnAmount)
    {
        boolean hasHeader = true;

      
        for (int j = firstColumn; j < firstColumn+columnAmount; j++)
        {
            // Prepare a model for the table of threecol-inputs
            
            ThreeColTabModel aMd = new ThreeColTabModel(maxInput,j,hasHeader); // was ThreeColTabModel aMd = new ThreeColTabModel(mIsNumeric,j,hasHeader);
            for (int i = 0; i < mRowNr; i++)
            {
                aMd.addRow();
            }

            ThreeColTable aTb = new ThreeColTable(aMd); // Creates the table from the model

            int rowHeight = aTb.getRowHeight();
            
            //aTb.addMouseListener(mParent);  // use for click on tables
            aTb.setHandler(this);
            
            mModels.add( aMd);
            mTables.add( aTb);

            
            /* Make scrollPane, and adjust the preferredSize */
            
            JScrollPane scrollPane = new JScrollPane(aTb);

            Dimension aDim = aTb.getPreferredSize();                                    
            
            /**
             * old height calculation // NE
             * 
             * int aH = aDim.height;
             * int aW = aDim.width;
             *
             * if(mRowNr == 2) aH = aH + 40;
             * scrollPane.setPreferredSize(new Dimension(aW/4,aH)); // + scrollPaneAdjustment ));            
             *
             */
            
            int aW = 3 * rowHeight; // 3 cells wide
            
            //int aH = (mRowNr + 1) * rowHeight + (1* mRowNr); // Add one row for the header

            int aH = mRowNr + height_modification + (int) (aDim.getHeight() +aTb.getTableHeader().getPreferredSize().height);
                                                
            aH += EXTRA_SPACE_BELOW_EACH_TABLEROW; // Zero for now, but we might want to change the constant later
            
            scrollPane.setPreferredSize(new Dimension(aW, aH));                                    
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            
            container.add(scrollPane); // add one ThreeColTable            
        }
    }

    /** Add titled columns, to the container */
    protected void addTitleColumns(Container container)
    {        
        final  JTable fstTab;
        
        if (mFstColData == null)
            fstTab = new JTable(new Vector(), new Vector(java.util.Arrays.asList(mFstColName)));
        else
            fstTab = new JTable(mFstColData, mFstColName);

        int rowHeight = fstTab.getRowHeight();
        
        fstTab.setRowHeight(2 * rowHeight); // First column has double-height cells

        fstTab.setEnabled(false);

        JScrollPane fstScrlPane1 = new JScrollPane(fstTab);
        fstScrlPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        fstScrlPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        container.add(fstScrlPane1);
     
        final JTable secTab;
        if (mSecColData == null)
            secTab = new JTable(new Vector(), new Vector(java.util.Arrays.asList(mSecColName)));
        else
            secTab = new JTable(mSecColData, mSecColName);

        secTab.setEnabled(false);

        JScrollPane secScrlPane1 = new JScrollPane(secTab);
        secScrlPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        secScrlPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        
        Dimension aDim = fstTab.getPreferredSize();

        // int aH = aDim.height;               
        
        int aW = aDim.width;        
        
        aW = (3*aW) / 4; // Reduce the width, since it's hard to fit all of it on 1024x768 otherwise
        
        // int aH = rowHeight * (mRowNr + 1); // add 1 for header
                
        int aH = mRowNr + height_modification + (int) (secTab.getPreferredSize().getHeight() + secTab.getTableHeader().getPreferredSize().getHeight());                
        
        Dimension dim = new Dimension(aW, aH); // used to be aH + scrollPaneAdjustment
        
        secScrlPane1.setPreferredSize(dim);
        
        aW = (int) secTab.getPreferredSize().getWidth();
        aW = (3*aW) / 4; // See above
        
        dim = new Dimension(aW,aH);
        
        fstScrlPane1.setPreferredSize(dim);

        container.add(secScrlPane1);        
    }

    public void setParent(PlaquePanelD pPanel)
    {
        mParent = pPanel;
    }

    public PlaquePanelD getParentPlaquePanel()
    {
        return mParent;
    }
    
    /**
     * Gets the first ThreeColTable in this PlaqueHandler // NE
     */
    public ThreeColTable getFirstTable()
    {
        return (ThreeColTable) mTables.get(0);
    }

    public ThreeColTable getLastTable()
    {
        int idx = mTables.size();

        if (idx > 0)
        {
            return (ThreeColTable) mTables.get(idx -1);
        }

        return null;
    }

    public ThreeColTable getTableAt(int i)
    {
        return (ThreeColTable) mTables.get(i);
    }

    public ThreeColTable getNextTable(ThreeColTable pTable)
    {
        int find = -1;

        for(int i = 0; i < mTables.size() ; i++)
        {
            if(pTable == mTables.get(i)) find = i;
        }

        find++;

        if (find >= mTables.size())
        {
            return null;
        }

        return (ThreeColTable) mTables.get(find);
    }

    /**
     * Gets preceding ThreeColTable // NE
     * If no preceding threecoltable exists, returns null.
     */ 
    public ThreeColTable getPrvTable(ThreeColTable pTable)
    {        
        // Check mTables for the current table, pTable
        for(int i = 0; i < mTables.size(); i++)
        {
            if(pTable == mTables.get(i))
            {
                if (i == 0) // pTable is first, so return null
                {
                    return null;
                }
                else
                    return (ThreeColTable) mTables.get(i-1);                
            }
        }
        
        // None found!
        return null;
    }

    public abstract MRTree buildTree(MRTree pRoot, PlaqueContext pc);
	
	public abstract void loadTree(Tree t, PlaqueContext pc);

    
    /**
     * Gets the max input (15 for poche and lösning, 3 for others)
     */
    public int getMaxInput()
    {
        return maxInput;    
    }
    
    /**
     * Support method for debugging
     */ 
    public String getPlaqueHandlerName()
    {
        String className = getClass().getName();
        int lastdotpos = className.lastIndexOf('.');
        if (lastdotpos > -1)
            className = className.substring(lastdotpos+1);
        return className;
    }
}
