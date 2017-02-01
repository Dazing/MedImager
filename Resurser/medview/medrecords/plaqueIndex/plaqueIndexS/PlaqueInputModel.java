/*
 * PlaqueInputModel.java
 *
 * Created on den 6 november 2002, 15:56
 *
 * $Log: PlaqueInputModel.java,v $
 * Revision 1.3  2007/10/17 15:17:23  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.2  2003/11/11 14:49:53  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.1  2003/08/07 00:21:48  erichson
 * now extends AbstractInputModel
 *
 */
package medview.medrecords.plaqueIndex.plaqueIndexS;

/**
 *
 * @author  nader
 */

import medview.medrecords.models.*;

public class PlaqueInputModel extends AbstractInputModel{
    
    private String mTeeth;
    private String mStatus;
    
    /** Creates a new instance of PlaqueInputModel */
    public PlaqueInputModel(String pTeeth,String pStatus,String initialDescription, String initialComment) {
        super(pTeeth + "_" + pStatus,initialDescription,initialComment, null); // No presets
        mStatus = pStatus;
        mTeeth  = pTeeth;
        
    }
    public PlaqueInputModel(String initialName,String initialDescription, String initialComment) {
        super(initialName,initialDescription,initialComment,null); // No presets
    }
    public String getStatus(){
        return mStatus;
       /* String aName    = this.getName();
        int    indx     = aName.indexOf("_") + 1; 
        if(indx == 0) return null;
        return aName.substring(indx); */
    }
    
    public String getTooth(){
        return mTeeth;
       /* String aName    = this.getName();
        int    indx     = aName.indexOf("_"); 
        if(indx <= 0) return null;
        return aName.substring(0,indx);    */
    }
}
