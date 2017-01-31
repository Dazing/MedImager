/*
 * TableHandlerLosning.java
 *
 * Created on den 11 december 2002, 14:40
 *
 * $Id: TableHandlerLosning.java,v 1.7 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: TableHandlerLosning.java,v $
 * Revision 1.7  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.6  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.5.2.5  2005/04/26 10:39:41  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.5.2.4  2005/04/05 14:32:14  erichson
 * Changed acceptable values to 0-3
 *
 * Revision 1.5.2.3  2005/03/25 13:48:00  erichson
 * Layout update, better preferredSize calculation
 *
 * Revision 1.5.2.2  2005/03/24 15:21:42  erichson
 * Reworked to conform to new PlaqueHandler.
 *
 * Revision 1.5.2.1  2005/03/04 08:30:48  erichson
 * isNumeric removed, super call added
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
public class TableHandlerLosning extends PlaqueHandler 
{
    
    /** Creates a new instance of TableHandlerLosning */
    public TableHandlerLosning(PlaquePanelD pPanel) {
        super(3); // Lösning accepts 0-3 // NE
        mRowNr = 1;
        //mIsNumeric = true;
        initComponents(pPanel);
        /*Dimension aD = this.getPreferredSize();
        aD.setSize(aD.getWidth(),aD.getHeight() + 20);
        this.setPreferredSize(aD);*/
    }
    
    protected void addInputColumns(Container cont, int startTooth, int teethAmount)
    {
        
        for (int j = startTooth; j < (startTooth + teethAmount); j++)
        {
            ThreeColTabModel aMd = new ThreeColTabModel(j,
                                                        false, // hasHeader
                                                        true); // isSmalllist
            
            aMd.addRow();
            
            ThreeColTable aTb = new ThreeColTable(aMd);
            //aTb.addMouseListener(mParent);
            
            mModels.add(aMd);
            mTables.add(aTb);
            aTb.setHandler(this);
                      
            aTb.permDisableColumn(0);
            aTb.permDisableColumn(2);
            
            JScrollPane scrollPane = new JScrollPane(aTb);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            Dimension aDim = aTb.getPreferredSize();
            // int aH = (mRowNr == 2 ? aDim.height / 2 : aDim.height );
            int aH = aDim.height;
            int aW = aDim.width;
            
            aH = aTb.getRowHeight() * 2; // Add one for the header
            
            //aTb.setPreferredSize(new Dimension(90,aH));
            scrollPane.setPreferredSize(new Dimension(aW/4,aH));
            
            cont.add(scrollPane);                        
        }
        
    }
    
    protected void addTitleColumns(Container cont)
    {
        
        final  JTable fstTab = new JTable(1,1);//mFstColData, null);
        fstTab.setValueAt("Loesning",0,0);
        fstTab.setEnabled(false);
        fstTab.setTableHeader(null);
        JScrollPane fstScrlPane1 = new JScrollPane(fstTab);
        
        
        final  JTable secTab = new JTable(1,1);//mSecColData, null);
        secTab.setValueAt("",0,0);
        secTab.setEnabled(false);
        secTab.setTableHeader(null);
        JScrollPane secScrlPane1 = new JScrollPane(secTab);
        
        
        Dimension aDim = secTab.getPreferredSize();
        //int aH = aDim.height * 2;
        int aW = aDim.width / 2;
        
        int rowHeight = fstTab.getRowHeight();
        int aH = rowHeight * (mRowNr +1); // Add one for header
        
        
        secScrlPane1.setPreferredSize(new Dimension(aW,aH));
        fstScrlPane1.setPreferredSize(new Dimension(aW,aH));
        
        cont.add(fstScrlPane1);
        cont.add(secScrlPane1);
        
    }
    
    public MRTree buildTree(MRTree pRoot, PlaqueContext pc){

        MRTree fstCol1 = pRoot.addNode("Loesning");
        PlaqueContext aContext = pc.copy();
        aContext.setPart(PlaqueContext.MOBILITY_PART);
        aContext.setRow(PlaqueContext.SINGLE_ROW);

        for(int i = 0; i < mTables.size(); i++){
            ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
            aTab.buildTree(fstCol1,0, aContext, i);
        }
        
        return pRoot;
    }
	
	public void loadTree(Tree t, PlaqueContext pc){
        PlaqueContext aContext = pc.copy();
        aContext.setPart(PlaqueContext.MOBILITY_PART);
        aContext.setRow(PlaqueContext.SINGLE_ROW);
		
        for(int i = 0; i < mTables.size(); i++){
            ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
            aTab.loadTree(t, 0, aContext, i);
        }
    }

}
