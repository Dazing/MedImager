/*
 * PlaqueModel.java
 *
 * Created on den 6 november 2002, 14:02
 */

package medview.medrecords.plaqueIndex.plaqueIndexS;

/**
 *
 * @author  nader
 */

import medview.medrecords.models.*;
import medview.medrecords.data.*;

public class PlaqueCategoryModel extends CategoryModel {
    /** Creates a new instance of PlaqueModel */
    public PlaqueCategoryModel(String pName) {
        super(pName); // No presets
        buildModel();
    }
    private void buildModel(){
        String       aName;
        InputModel   aModel;
        
        for(int j = 0; j < PqConst.NuOfPlqTabCol ; j++){ 
            addInputModel("" + (18 - j));         
            addInputModel("" + (21 + j));    
            addInputModel("" + (48 - j));
            addInputModel("" + (31 + j));     
        }
    }
    private void addInputModel(String pIndex){ 
        PlaqueInputModel aModel;
        
        aModel = new PlaqueInputModel(pIndex,"m",getTitle());
        this.addInput(aModel);
        
        aModel = new PlaqueInputModel(pIndex,"b",getTitle());
        this.addInput(aModel);
        
        aModel = new PlaqueInputModel(pIndex,"d",getTitle());
        this.addInput(aModel);
        
        aModel = new PlaqueInputModel(pIndex,"l",getTitle());
        this.addInput(aModel);     
        
    }
}
