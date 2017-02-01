/*
 * TableHandlerBlodning.java
 *
 * Created on den 11 december 2002, 12:08
 *
 * $Id: TableHandlerBlodning.java,v 1.7 2006/09/13 22:00:07 oloft Exp $
 *
 * $Log: TableHandlerBlodning.java,v $
 * Revision 1.7  2006/09/13 22:00:07  oloft
 * Added Open functionality
 *
 * Revision 1.6  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.5.2.1  2005/03/04 08:29:59  erichson
 * isNumeric replaced by super call
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

/**
 *
 * @author  nader
 */
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;
import medview.datahandling.examination.tree.*;

public class TableHandlerBlodning extends PlaqueHandler {
    
    /** Creates a new instance of TableHandlerBlodning */
    public TableHandlerBlodning(PlaquePanelD pPanel) 
    {
        super(1); // Blödning is only true or false
        mRowNr = 4;
        //mIsNumeric = false;
        initComponents( pPanel);
    }
    protected void initComponents(PlaquePanelD pPanel) {
        
        Object[][] data = {{"Blödning"},{"Pus"}};
        mFstColData = data;
        
        Object[][] data2 = {{"Facialt"}, {"Lingualt"},{"Facialt"}, {"Lingualt"}};
        mSecColData =data2;
        
        super.initComponents(pPanel);
    }
    
    public MRTree buildTree(MRTree pRoot, PlaqueContext pc){
        PlaqueContext tmpContext;
        MRTree[] secCol  = new MRTree[4];
        PlaqueContext[] plContexts = new PlaqueContext[4];
        
        MRTree bleedTree = pRoot.addNode((String)mFstColData[0][0]);
        
        secCol[0] = bleedTree.addNode((String)mSecColData[0][0]); // Facialt
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.BLEEDING_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[0] = tmpContext;
        
        secCol[1] = bleedTree.addNode((String)mSecColData[1][0]); // Lingualt
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.BLEEDING_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[1] = tmpContext;
        
        MRTree pusTree = pRoot.addNode((String)mFstColData[1][0]);
        
        secCol[2]= pusTree.addNode((String)mSecColData[2][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.PUS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[2] = tmpContext;
        
        
        secCol[3] = pusTree.addNode((String)mSecColData[3][0]);
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.PUS_PART);
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
        
		// Facialt
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.BLEEDING_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[0] = tmpContext;
        
         // Lingualt
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.BLEEDING_PART);
        tmpContext.setRow(PlaqueContext.LINGUAL_ROW);
        plContexts[1] = tmpContext;
        
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.PUS_PART);
        tmpContext.setRow(PlaqueContext.FACIAL_ROW);
        plContexts[2] = tmpContext;
        
        tmpContext = pc.copy();
        tmpContext.setPart(PlaqueContext.PUS_PART);
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
