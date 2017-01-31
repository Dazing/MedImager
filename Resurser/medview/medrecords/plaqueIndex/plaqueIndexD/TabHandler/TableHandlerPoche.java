/*
 * TableHandlerPoche.java
 *
 * Created on den 11 december 2002, 11:53
 *
 * $Id: TableHandlerPoche.java,v 1.7 2006/09/13 22:00:08 oloft Exp $
 *
 * $Log: TableHandlerPoche.java,v $
 * Revision 1.7  2006/09/13 22:00:08  oloft
 * Added Open functionality
 *
 * Revision 1.6  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.5.2.1  2005/03/04 08:32:14  erichson
 * isNumeric removed, super() call added
 *
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
public class TableHandlerPoche  extends PlaqueHandler {
    
    /** Creates a new instance of TableHandlerPoche */
    public TableHandlerPoche(PlaquePanelD pPanel) {
        super(15); // Poche accepts 0-15;
        mRowNr = 4;
        //mIsNumeric = true;
        initComponents(pPanel);
    }
    protected void initComponents(PlaquePanelD pPanel) {
        
        
        Object[][] data = {{"Poche"},{"Faestetab"}};
        mFstColData = data;
        
        
        Object[][] data2 = {{"Facialt"}, {"Lingualt"},{"Facialt"}, {"Lingualt"}};
        mSecColData =data2;
        
        super.initComponents(pPanel);
    }
    
    public MRTree buildTree(MRTree pRoot, PlaqueContext pc){
        PlaqueContext tmpContext;
        MRTree[] secCol  = new MRTree[4];
        PlaqueContext[] plContexts = new PlaqueContext[4];
		
        MRTree pocketTree = pRoot.addNode((String)mFstColData[0][0]);
		
        secCol[0] = pocketTree.addNode((String)mSecColData[0][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.POCKET_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[0] = tmpContext;
        
        secCol[1] = pocketTree.addNode((String)mSecColData[1][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.POCKET_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[1] = tmpContext;
        
        MRTree lossTree = pRoot.addNode((String)mFstColData[1][0]);
		
        secCol[2]= lossTree.addNode((String)mSecColData[2][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.ATTACH_LOSS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[2] = tmpContext;
        
        secCol[3] = lossTree.addNode((String)mSecColData[3][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.ATTACH_LOSS_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[3] = tmpContext;
        
        for(int i = 0; i < mTables.size(); i++){
            for(int j = 0; j < secCol.length; j++){
                MRTree        aTree   = secCol[j];
                PlaqueContext aContext = plContexts[j];
				
                ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
                aTab.buildTree(aTree,j, aContext, i);
            }
        }
        return pRoot;
    }
    
	// Mirrors buildTree
 	public void loadTree(Tree t, PlaqueContext pc){
        PlaqueContext tmpContext;
        PlaqueContext[] plContexts = new PlaqueContext[4];
		
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.POCKET_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[0] = tmpContext;
        
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.POCKET_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[1] = tmpContext;
        
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.ATTACH_LOSS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[2] = tmpContext;
        
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.ATTACH_LOSS_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[3] = tmpContext;
        
        for(int i = 0; i < mTables.size(); i++){
            for(int j = 0; j < plContexts.length; j++){
                PlaqueContext aContext = plContexts[j];
				
                ThreeColTable aTab    = (ThreeColTable)mTables.get(i);
                aTab.loadTree(t, j, aContext, i);
            }
        }
	}



}
