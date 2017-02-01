/*
 * TableHandlerFurkaturer.java
 *
 * Created on den 11 december 2002, 12:21
 *
 * $Id: TableHandlerFurkaturer.java,v 1.9 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: TableHandlerFurkaturer.java,v $
 * Revision 1.9  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.8  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.7.2.10  2005/04/26 10:39:41  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.7.2.9  2005/04/20 15:08:21  erichson
 * small javadoc notes
 *
 * Revision 1.7.2.8  2005/04/20 15:06:12  erichson
 * Documented getNextTable, added getPrvTable and getLastTable (overrides methods in PlaqueHandler)
 *
 * Revision 1.7.2.7  2005/03/29 10:50:29  erichson
 * Updated insets (fixed mis-aligning of the 2 tables)
 *
 * Revision 1.7.2.6  2005/03/25 13:39:05  erichson
 * preferredSize update.
 *
 * Revision 1.7.2.5  2005/03/24 19:26:18  erichson
 * Uses FurkaturTableFactory, TableHandlerFurkaturerUnder can now inherit this class to avoid code duplication.
 *
 * Revision 1.7.2.4  2005/03/24 17:19:26  erichson
 * Cleaned up.
 *
 * Revision 1.7.2.3  2005/03/24 17:16:16  erichson
 * Fixed the layout.
 *
 * Revision 1.7.2.2  2005/03/24 15:27:42  erichson
 * removed unnecessary parameter to initcomponents
 *
 * Revision 1.7.2.1  2005/03/24 15:25:09  erichson
 * updated to new plaquehandler, removed isNumeric, made some cleanup.
 * Checking in to prepare for reworking.
 *
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;
import medview.datahandling.examination.tree.*;


/**
 *
 * @author  nader
 */
public class TableHandlerFurkaturer extends PlaqueHandler
    implements FurkaturTableFactory
{

    protected ArrayList  mModelsU = new ArrayList();
    protected ArrayList  mTablesU = new ArrayList();
        
    protected FurkaturTableFactory tableFactory;
    
    protected TableHandlerFurkaturer(PlaquePanelD pPanel, boolean doInitComponents)
    {
        super(); // Defaults to 0-3 input                
        
        // height_modification = -16;
        
        mRowNr = 1;       

        mParent = pPanel;
                
        if (doInitComponents)
        {   
            tableFactory = this;
            initComponents();
        }
    }

    public TableHandlerFurkaturer(PlaquePanelD pPanel)
    {
        this(pPanel, true); // Do initcomponents
    }
    
    protected void initComponents()
    {
        Object[][] data = {{"Furkaturer"}};

        mFstColData = data;

        Object[][] data2 = {{"Facialt"}, {"Lingualt"}};

        mSecColData =data2;
        
        titlePanel = new JPanel(new GridLayout(1,2));
        leftPanel = new JPanel(new GridLayout(1,PlaqueHandler.HALF_TEETH_AMOUNT));
        rightPanel = new JPanel(new GridLayout(1,PlaqueHandler.HALF_TEETH_AMOUNT));                
        
        addTitleColumns(titlePanel);
        addInputColumns(leftPanel,0,PlaqueHandler.HALF_TEETH_AMOUNT);
        addInputColumns(rightPanel,PlaqueHandler.HALF_TEETH_AMOUNT,PlaqueHandler.HALF_TEETH_AMOUNT);
        
    }
 
    protected void addInputColumns(Container cont, int firstTooth, int toothAmount)
    {                
        for (int j = firstTooth; j < firstTooth + toothAmount; j++)
        {
                        
            JTable overTable = tableFactory.getOverTable(j);
            JTable underTable = tableFactory.getUnderTable(j);
            
            JScrollPane overScroll = new NoBorderScrollPane(overTable);
        
            JScrollPane underScroll = new NoBorderScrollPane(underTable);
            
            overScroll.setMinimumSize(new Dimension(10,32));
            overScroll.setPreferredSize(new Dimension(10,32));
            overScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            
            underScroll.setMinimumSize(new Dimension(10,16));
            underScroll.setPreferredSize(new Dimension(10,16));
            underScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                        
            JPanel layoutPanel = new JPanel(new GridBagLayout()); 
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight=2;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.NORTH;            
            
            /* Add a top inset to get spacing between over and under tables. */
            gbc.insets = new Insets(1, // top
                                    0, // left
                                    0, // bottom 
                                    0); //right
            
            layoutPanel.add(overScroll, gbc);

            
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridheight=1;
            gbc.weightx = 1;
            gbc.weighty = 0;            
            gbc.fill = GridBagConstraints.BOTH;            

            layoutPanel.add(underScroll, gbc);
            
            gbc.insets = new Insets(0,0,0,0); // default
            
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.gridheight = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.BOTH;
            
            layoutPanel.add(Box.createGlue(), gbc);                                   
            
            cont.add(new JScrollPane(layoutPanel));
                        
        }
    }

    public JTable getOverTable(int j)
    {        
        ThreeColTabModel aMd = new ThreeColTabModel(getMaxInput(),j,true,true,null); // isNumeric was true

        int tabNr = PqDFunConst.calculateTabNr(j);

        aMd.addRow();

        ThreeColTable aTb = new ThreeColTable(aMd);

        aTb.setHandler(this);

        if (tabNr <=3)
        {
            aTb.permDisableAllColumns();
        }
        else
        {
            aTb.permDisableColumn(0);
            aTb.permDisableColumn(2);
        }

        mModels.add(aMd);
        mTables.add(aTb);

        //aTb.addMouseListener(mParent);

        return aTb;
	}

    public JTable getUnderTable(int j)
    {
        String [] header ={"","",""};

        ThreeColTabModel aMd = new ThreeColTabModel(getMaxInput(),j,true,false,header); // isNumeric was true

        int tabNr = PqDFunConst.calculateTabNr(j);

        aMd.addRow();

        ThreeColTable aTb = new ThreeColTable(aMd);

        aTb.setHandler(this);

        if(tabNr >= 6)
        {
            aTb.permDisableColumn(1);
        }
        else if (tabNr >= 4)
        {
            aTb.permDisableColumn(0);
            aTb.permDisableColumn(2);
        }
        else
        {
            aTb.permDisableAllColumns();
        }

        mModelsU.add(aMd);
        mTablesU.add(aTb);

        //aTb.addMouseListener(mParent);

        return aTb;
    }

    public void makeTableEnabled(int tabNr,boolean flag)
    {
        ((ThreeColTable)mTables.get(tabNr)).makeEnabled(flag);
        ((ThreeColTable)mTablesU.get(tabNr)).makeEnabled(flag);
    }

    /**
     * Get next table, with support for över/underkäke. Return null if it is the last table in Underkäke. // NE
     *
     * (Overrides method in PlaqueHandler)
     */
    public ThreeColTable getNextTable(ThreeColTable pTable)
    {
        int find = mTables.indexOf(pTable);

        if(find > -1) // we found current table in Överkäke // NE
        {
            find++; // number of next table

            if(find < mTables.size()) 
            {
                    return (ThreeColTable) mTables.get(find); // Get next in Överkäke
            }
            else
            {
                    return (ThreeColTable) mTablesU.get(0); // This was last in Överkäke, Goto first in Underkäke
            }
        }
        else // check Underkäke tables
        {
            find = mTablesU.indexOf(pTable);

            if(find > -1) // We found current table in Underkäke // NE
            {
                find++;

                if(find < mTablesU.size()) // Get next in underkäke
                {
                        return (ThreeColTable) mTablesU.get(find);
                }
                else // Last in Underkäke - return null (no next table)
                {
                        return null;
                }
            }
            else
            {
                    return null; // Nothing found!
            }
        }
    }

    /**
     * Get prev table, with support for över/underkäke. Return null if it's the first table in Överkäke. // NE
     *
     * (Overrides method in PlaqueHandler)
     */
    public ThreeColTable getPrvTable(ThreeColTable pTable)
    {
        int find = mTables.indexOf(pTable);

        if(find >= 0) // we found current table in Överkäke // NE
        {
            find--; // number of prev table

            if(find >= 0) // In överkäke, but not the first one 
            {
                    return (ThreeColTable) mTables.get(find); // Get previous in Överkäke
            }
            else // The first table in Överkäke
            {
                    return null; // There is no previous for the first table in Överkäke
            }
        }
        else // check Underkäke tables
        {
            find = mTablesU.indexOf(pTable);

            if(find >= 0) // We found current table in Underkäke // NE
            {
                find--; // number of prev table

                if(find >= 0) // Not the first in underkäke, get previous
                {
                        return (ThreeColTable) mTablesU.get(find);
                }
                else // The first table in Underkäke - return last table in Överkäke
                {
                        return (ThreeColTable) mTables.get(mTables.size() -1); // Last table in Överkäke
                }
            }
            else
            {
                    return null; // Nothing found!
            }
        }
    }
    
    
    
    
    public MRTree buildTree(MRTree pRoot, PlaqueContext pc)
    {
        PlaqueContext tmpContext;

        MRTree[] secCol  = new MRTree[2];

        PlaqueContext[] plContexts = new PlaqueContext[2];

        MRTree fstCol1 = pRoot.addNode((String)mFstColData[0][0]);

        secCol[0] = fstCol1.addNode((String)mSecColData[0][0]);

        // Furcations, Facial
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.FURCATIONS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);

        plContexts[0] = tmpContext;

        secCol[1] = fstCol1.addNode((String)mSecColData[1][0]);

        // Furcations, Lingual
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.FURCATIONS_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);

        plContexts[1] = tmpContext;

        MRTree        aTree   = secCol[0];

        PlaqueContext aContext = plContexts[0];

        for(int i = 0; i < mTables.size(); i++)
        {
            ThreeColTable aTab    =(ThreeColTable) mTables.get(i);

            aTab.buildTree(aTree,0, aContext, i);
        }

        aTree   = secCol[1];

        aContext = plContexts[1];

        for(int i = 0; i < mTablesU.size(); i++)
        {
            ThreeColTable aTab    =(ThreeColTable) mTablesU.get(i);

            aTab.buildTree(aTree,0, aContext, i);
        }

        return pRoot;
    }
    
	// Mirror of buildTree
	public void loadTree(Tree t, PlaqueContext pc)
	{
        PlaqueContext tmpContext;
        PlaqueContext[] plContexts = new PlaqueContext[2];
		
        // Furcations, Facial
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.FURCATIONS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[0] = tmpContext;
		
		// Furcations, Lingual
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.FURCATIONS_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[1] = tmpContext;
				
        PlaqueContext aContext = plContexts[0];
		
        for(int i = 0; i < mTables.size(); i++)
        {
            ThreeColTable aTab    =(ThreeColTable) mTables.get(i);
			
            aTab.loadTree(t, 0, aContext, i);
        }
		
        aContext = plContexts[1];
		
        for(int i = 0; i < mTablesU.size(); i++)
        {
            ThreeColTable aTab    =(ThreeColTable) mTablesU.get(i);
			
            aTab.loadTree(t, 0, aContext, i);
        }
    }
	
    /**
     * Gets the last table.
     *
     * note: we override this PlaqueHandler method, so that the last table is picked from underkäke, instead of överkäke (which is mTables) // NE
     */
    public ThreeColTable getLastTable()
    {
        int idx = mTablesU.size();

        if (idx > 0)
        {
            return (ThreeColTable) mTablesU.get(idx -1);
        }

        return null;
    }
    
    /**
     * Custom class to kill the borders in scrollpanes, since double scrollpane borders mis-align the tables.
     * See "a note about JTable" here: 
     *     http://core.netbeans.org/windowsystem/MigrationGuide.html
     */
    private class NoBorderScrollPane extends JScrollPane
    {
        public NoBorderScrollPane(JTable t) { super(t); }
        
        // kill setBorder.
        public void setBorder(javax.swing.border.Border b) { } 
    }
}
