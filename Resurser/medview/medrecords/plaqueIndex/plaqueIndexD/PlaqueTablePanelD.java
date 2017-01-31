/*
 * PlaqueTablePanel.java
 *
 * Created on den 30 oktober 2002, 14:01
 *
 * $Id: PlaqueTablePanelD.java,v 1.11 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: PlaqueTablePanelD.java,v $
 * Revision 1.11  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.10  2005/04/28 13:43:08  erichson
 * Added instructions for the keyboard input.
 *
 * Revision 1.9  2005/04/28 12:56:29  erichson
 * Added scrollPane to avoid problems on lower resolutions.
 *
 * Revision 1.8  2005/04/28 12:42:50  erichson
 * Small layout fix for lambdalayout version
 *
 * Revision 1.7  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.6.2.7  2005/04/26 10:39:40  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.6.2.6  2005/04/24 10:27:39  erichson
 * Minimal javadoc addition
 *
 * Revision 1.6.2.5  2005/04/20 15:04:06  erichson
 * Next/Previous table handling.
 *
 * Revision 1.6.2.4  2005/03/29 08:10:42  erichson
 * removed leftover println
 *
 * Revision 1.6.2.3  2005/03/25 19:45:15  erichson
 * Cleaned out the old, commented-out layout code. (Long gridbaglayout thing)
 *
 * Revision 1.6.2.2  2005/03/25 19:41:00  erichson
 * Things are starting to look good now.
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
 *
 * @author  nader
 */

import medview.datahandling.examination.tree.*;
import medview.medrecords.tools.*;
import medview.medrecords.models.*;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;


public class PlaqueTablePanelD extends javax.swing.JPanel
{

    /* CONSTANTS */

    private static final int PLAQUE_HANDLERS_AMOUNT = 5;
    
    /**
     * Whether to use lambdaLayout or not. If not, a GridBagLayout is used.    
     */
    private static final boolean USE_LAMBDALAYOUT = false; // GridBagLayout actually looks nicer // NE
    
    // Preferred size of the separator between the two groups of teeth (left and right)
    private static final Dimension SEPARATOR_PREFERREDSIZE = new Dimension(32,10);
    
    // MEMBERS
    
    private PlaquePanelD        mParent; // Reference to parent panel // NE
    private Vector              mTableHandlers;    
    private PlaqueHandler       mCurrentHandler;
    private PlaqueHandler       mPoche;
    private PlaqueHandler       mLosning;
    private PlaqueHandler       mFurkatur;
    private boolean             mIsUnder;


    // CONSTRUCTORS

    public PlaqueTablePanelD(PlaquePanelD pPanel,boolean isUnder)
    {
        super();
        
        mIsUnder = isUnder;

        mParent = pPanel;

        initComponents();
        
        setHandler(mPoche);   //setPocheHandler();
    }

     private void initComponents()
    {
        mTableHandlers = new Vector();
        
        this.setLayout(new BorderLayout());

        
        // plaque

        PlaqueHandler aPH = new TableHandlerPlaque(mParent);
        addTableHandler(aPH);        

        // poche
        
        mPoche = new TableHandlerPoche(mParent);                  
        addTableHandler(mPoche);
        
        // blodning
        
        aPH = new TableHandlerBlodning(mParent);        
        addTableHandler(aPH);       

        // furkaturer
       
        if(mIsUnder) 
        {
            aPH = new TableHandlerFurkaturerUnder(mParent);
        }
        else
        {
            aPH = new TableHandlerFurkaturer(mParent);
        }

        mFurkatur = aPH;
        addTableHandler(mFurkatur);
              
        // losning      

        mLosning = new TableHandlerLosning(mParent);       
        addTableHandler(mLosning);
       
        // build the gui

        JPanel backPanel  = new JPanel();               
        
        if (USE_LAMBDALAYOUT)
        {
            backPanel.setLayout(new ise.java.awt.LambdaLayout());

            // Lay out each row in title, left and right components
            int y = 0;
            for (int i = 0; i < mTableHandlers.size(); i++)
            {
                
                PlaqueHandler pH = (PlaqueHandler) mTableHandlers.get(i);                                
                
                // Height. Add 1 for the header
                int h = pH.getRowCount() + 1;                
                
                Component titleC = pH.getTitleComponent();
                Component leftC = pH.getLeftTeethComponent();
                JLabel separatorC = new JLabel(" ");
                separatorC.setPreferredSize(SEPARATOR_PREFERREDSIZE);
                Component rightC = pH.getRightTeethComponent();                
                
                backPanel.add(titleC, "0," + y + ",6," + h + ",,wh,0");
                backPanel.add(leftC, "6," + y + ",12," + h + ",,wh,0");
                backPanel.add(separatorC, "18," + y + ",1," + h + ",,h,0"); // if wh, it will grow strangely when resizing...?
                backPanel.add(rightC, "19," + y + ",14," + h + ",,wh,0");
                
                y += h; // Adjust y position
            }
        } 
        else 
        {
            // use gridbaglayout
            backPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            
            gbc.gridheight = 1;
            gbc.fill = gbc.BOTH;
            
            for (int y = 0; y < mTableHandlers.size(); y++)
            {
                PlaqueHandler pH = (PlaqueHandler) mTableHandlers.get(y);                
                gbc.gridy = y;
                gbc.weightx = 1;
                gbc.weighty = 0;
                
                gbc.gridx = 0;
                gbc.gridwidth = 6;
                backPanel.add(pH.getTitleComponent(), gbc);
                
                gbc.gridx = 6;
                gbc.gridwidth= 12;
                backPanel.add(pH.getLeftTeethComponent(),gbc);
                
                // The filler space
                gbc.gridx = 18;
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.fill = gbc.NONE;
                JLabel separatorC = new JLabel(" ");
                                
                separatorC.setPreferredSize(SEPARATOR_PREFERREDSIZE);
                backPanel.add(separatorC, gbc);
                
                gbc.fill = gbc.BOTH;
                
                gbc.weightx = 1;
                gbc.gridx = 19;
                gbc.gridwidth = 12;
                backPanel.add(pH.getRightTeethComponent(),gbc);
                
                gbc.gridx = 31;
                gbc.gridwidth = gbc.REMAINDER;
                gbc.weightx = 0;
                gbc.weighty = 0;
                backPanel.add(new JLabel(), gbc);                
                
            }
        }
                
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText("Keyboard controls: Number keys input values. SHIFT+<0-5> inputs numbers 10 to 15 where applicable.\n" +
                         "ENTER and BACKSPACE move the cursor forward or backward respectively, without editing.\n" +
                         "DELETE key removes input from the current cell, and moves the cursor forward.");
        
        // Put it all together in a panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(backPanel,BorderLayout.NORTH); // Put the plaquetables on top               
        centerPanel.add(textPane, BorderLayout.CENTER);  
        
        // Put the panel in a scrollpane to avoid chaos in too low resolutions
        JScrollPane pane = new JScrollPane(centerPanel);
        this.add(pane,BorderLayout.CENTER);
    }

    private void addTableHandler(PlaqueHandler pH)
    {
        mTableHandlers.add(pH);
    }
      
    /**
     * Gets the first ThreeColTable in this PlaquePanelD (gets it from PlaqueTablePanelD) // NE
     */
    public ThreeColTable getFirstTable()
    {        
        return ((PlaqueHandler) mTableHandlers.get(0)).getFirstTable();
    }

    public ThreeColTable getLastTable(){                
        return ((PlaqueHandler) mTableHandlers.get(mTableHandlers.size()-1)).getLastTable();
    }

    public ThreeColTable getTableAt(int i){
        return mCurrentHandler.getTableAt(i);
    }

     /**
     * Gets previous ThreeColTable. If there is no previous threeColTable, switches to previous plaquehandler.
     */      
    public ThreeColTable getPrvTable(ThreeColTable pTable)
    {
        ThreeColTable aTab = mCurrentHandler.getPrvTable(pTable);
        if (aTab == null) // no previous table in this handler
        {
            gotoPrevPlaqueHandler();
            return mCurrentHandler.getLastTable();
        }
        return aTab;
    }

    
    /**
     * Gets next ThreeColTable. If there is no next threecoltable, switches to next plaquehandler.
     */
    public ThreeColTable getNextTable(ThreeColTable pTable)
    {
        ThreeColTable aTab = mCurrentHandler.getNextTable(pTable);
        if(aTab == null) // We've run out of tables. Move to the next plaque handler (...the first table)
        {
            gotoNextPlaqueHandler();
            return mCurrentHandler.getFirstTable();
        }
        return aTab;
    }

    
    
    /**
     * Gets next threeColTable, based on the active row. This is because, if we are on the final row, we should switch plaquehandler.
     */
    public ThreeColTable getNextTable(ThreeColTable pTable,ByRef pRow)
    {
        ThreeColTable aTab = mCurrentHandler.getNextTable(pTable); // Get next ThreeColTable from current PlaqueHandler. will not switch plaquehandler
        if(aTab == null) // No next table - we are in final table // NE
        {
            if(pRow.intVal >= pTable.getRowCount() -1) // If we are on the last row (or have passed it(?))
            {
                gotoNextPlaqueHandler(); // switch plaque handlers
                pRow.intVal = 0; // NOTE! ByRef passes changed value back! // NE
                return mCurrentHandler.getFirstTable();
            }
            else // we're not on the final row. Don't switch plaque handlers, just loop around.
            {
                pRow.intVal = pRow.intVal + 1;
                return mCurrentHandler.getFirstTable();
            }
        }
        return aTab;
    }

    /**
     * Gets next threeColTable, based on the active row. This is because, if we are on the final row, we should switch plaquehandler.
     */
    public ThreeColTable getPrvTable(ThreeColTable pTable,ByRef pRow)
    {
        ThreeColTable aTab = mCurrentHandler.getPrvTable(pTable); // Get next ThreeColTable from current PlaqueHandler. Will not switch plaqueHandler
        if(aTab == null) // This is the first table in this plaquehandler // NE
        {
            if(pRow.intVal == 0) // We are on the first row - switch plaque handlers
            {
                gotoPrevPlaqueHandler();
                ThreeColTable lastTable = mCurrentHandler.getLastTable();
                pRow.intVal = lastTable.getRowCount() - 1; // NOTE ByRef - gets passed back. // NE
                return lastTable;
            }
            else // we're not on the first row. don't switch plaque handlers, just loop around.
            {
                pRow.intVal = pRow.intVal - 1;
                return mCurrentHandler.getLastTable();
            }
        }
        return aTab;
    }
    

    /**
     * Moves currentHandler to next PlaqueHandler and then calls setHandler // NE
     */
    private void gotoNextPlaqueHandler()
    {
        int next = 0;
        for (int i = 0; i < PLAQUE_HANDLERS_AMOUNT; i++)
        {
            if (mCurrentHandler == mTableHandlers.get(i))
                next = i+1;
        }
        if (next >= PLAQUE_HANDLERS_AMOUNT)
            next = 0;
        
        setHandler( (PlaqueHandler) mTableHandlers.get(next));                
    }
    
    /**
     * Moves currentHandler to previous PlaqueHandler and then calls setHandler // NE
     */
    private void gotoPrevPlaqueHandler()
    {
        int prev = PLAQUE_HANDLERS_AMOUNT - 1;
        
        for (int i = 0; i < PLAQUE_HANDLERS_AMOUNT; i++)
        {
            if (mCurrentHandler == mTableHandlers.get(i))
            {
                prev = i - 1;
            }
        }
        
        if (prev < 0)
            prev = PLAQUE_HANDLERS_AMOUNT - 1; // last
        
        setHandler( (PlaqueHandler) mTableHandlers.get(prev));        
    }
    
    // Called from PlaquePanelD.setChoosenTable // NE    
    public void setHandler(PlaqueHandler pHandler){//,ThreeColTable pTab){        
        mCurrentHandler = pHandler;        
        mParent.setEnabledButtons(pHandler.getMaxInput());        
    }

    public PlaqueHandler getHandler()
    {
        return mCurrentHandler;
    }
    
    public MRTree buildTree(String pRoot, PlaqueContext pc)
    {
        MRTree aTree = new MRTree(pRoot,MRTree.TYPE_BRANCH);
        for(int i = 0; i < mTableHandlers.size(); i++){
            PlaqueHandler tPH = (PlaqueHandler) mTableHandlers.get(i);
            tPH.buildTree(aTree, pc.copy());
        }
        return aTree;
    }

	public void loadTree(Tree t, String pRoot, PlaqueContext pc)
    {
        for(int i = 0; i < mTableHandlers.size(); i++){
            PlaqueHandler tPH = (PlaqueHandler) mTableHandlers.get(i);
            tPH.loadTree(t, pc.copy());
        }
    }
	
    /**
     * Sets the state of all cells to enabled/disabled (disabled = extracted) // NE
     */
    public void setAllEnabled(int tabNr,boolean flag)
    {
        for(int i = 0; i < mTableHandlers.size(); i++)
        {
            PlaqueHandler tPH = (PlaqueHandler) mTableHandlers.get(i);
            tPH.makeTableEnabled(tabNr,flag);
        }
    }
}
