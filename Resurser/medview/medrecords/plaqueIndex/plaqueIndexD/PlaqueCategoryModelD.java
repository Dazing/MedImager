/*
 * PlaqueModel.java
 *
 * Created on den 6 november 2002, 14:02
 *
 * $Id: PlaqueCategoryModelD.java,v 1.3 2004/01/04 09:28:58 oloft Exp $
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
 *
 * @author  nader
 */

import medview.medrecords.models.*;
import medview.medrecords.data.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;

public class PlaqueCategoryModelD extends CategoryModel {
    /** Creates a new instance of PlaqueModel */
    public PlaqueCategoryModelD(String pName) {
        super(pName); // No presets
        buildModel();
    }
    private void buildModel(){
        String       aName;
        InputModel   aModel;
        
        //for(int j = 0; j < PqConstD.NuOfPlqTabCol ; j++){ 
        for(int j = 0; j < 22 ; j++){ // old things
            addInputModel("" + (18 - j));         
            addInputModel("" + (21 + j));    
            addInputModel("" + (48 - j));
            addInputModel("" + (31 + j));     
        }
    }
    private void addInputModel(String pIndex){ 
        ThreeColInputModel aModel;
        
        aModel = new ThreeColInputModel(pIndex,"m",getTitle());
        this.addInput(aModel);
        
        aModel = new ThreeColInputModel(pIndex,"b",getTitle());
        this.addInput(aModel);
        
        aModel = new ThreeColInputModel(pIndex,"d",getTitle());
        this.addInput(aModel);
        
        aModel = new ThreeColInputModel(pIndex,"l",getTitle());
        this.addInput(aModel);     
        
    }
}
