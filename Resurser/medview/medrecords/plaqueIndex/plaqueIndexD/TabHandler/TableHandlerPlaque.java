/*
 * TwoLineTable.java
 *
 * Created on den 11 december 2002, 11:29
 *
 * $Id: TableHandlerPlaque.java,v 1.8 2006/09/13 22:00:08 oloft Exp $
 *
 * $Log: TableHandlerPlaque.java,v $
 * Revision 1.8  2006/09/13 22:00:08  oloft
 * Added Open functionality
 *
 * Revision 1.7  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.6.2.1  2005/03/25 13:50:58  erichson
 * removed old preferredSize juggling.
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

import javax.swing.*;
import java.awt.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;
import medview.datahandling.examination.tree.*;
/**
*
 * @author  nader
 */
public class TableHandlerPlaque extends PlaqueHandler {
    public TableHandlerPlaque(PlaquePanelD pPanel) {
        super();
        mRowNr = 2;
		
        //mIsNumeric = false;
		
        initComponents(pPanel);
	}
	
	protected void initComponents(PlaquePanelD pPanel) {
		Object[][] data = {{"Plaque"}};
		
		mFstColData = data;
		
		Object[][] data2 = {{"Facialt"}, {"Lingualt"}};
		
		mSecColData =data2;
		
		super.initComponents(pPanel);
		
	}
	
	public MRTree buildTree(MRTree pRoot, PlaqueContext pc)	{
		PlaqueContext tmpContext;
		
		MRTree[] secCol  = new MRTree[2];
		
		PlaqueContext[] plContexts = new PlaqueContext[2];
		
		MRTree plaqueTree = pRoot.addNode((String)mFstColData[0][0]);
		
		secCol[0] = plaqueTree.addNode((String)mSecColData[0][0]);
		
		tmpContext = pc.copy();
		
		tmpContext.setPart(PlaqueContext.PLAQUE_PART);
		
		tmpContext.setRow(PlaqueContext.FACIAL_ROW);
		
		plContexts[0] = tmpContext;
		
		secCol[1] = plaqueTree.addNode((String)mSecColData[1][0]);
		
		tmpContext = pc.copy();
		
		tmpContext.setPart(PlaqueContext.PLAQUE_PART);
		
		tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
		
		plContexts[1] = tmpContext;
		
		for(int i = 0; i < mTables.size(); i++)
		{
			for(int j = 0; j < secCol.length; j++)
			{
				MRTree        aTree   = secCol[j];
				
				PlaqueContext aContext = plContexts[j];
				
				ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
				
				aTab.buildTree(aTree,j,aContext,i);
			}
		}
		
		return pRoot;
	}
	
	public void loadTree(Tree t, PlaqueContext pc) {
		PlaqueContext tmpContext;
		PlaqueContext[] plContexts = new PlaqueContext[2];
		
		tmpContext = pc.copy();
		tmpContext.setPart(PlaqueContext.PLAQUE_PART);
		tmpContext.setRow(PlaqueContext.FACIAL_ROW);
		plContexts[0] = tmpContext;
		
		tmpContext = pc.copy();
		tmpContext.setPart(PlaqueContext.PLAQUE_PART);
		tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
		plContexts[1] = tmpContext;
		
		for(int i = 0; i < mTables.size(); i++) {
			for(int j = 0; j < plContexts.length; j++) {
				PlaqueContext aContext = plContexts[j];
				ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
				
				aTab.loadTree(t, j, aContext, i);
			}
		}
	}
	
}
