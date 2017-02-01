/*
 * ThreeColInputModel.java
 *
 * Created on den 13 januari 2003, 12:21
 *
 * $Log: ThreeColInputModel.java,v $
 * Revision 1.4  2007/10/17 15:17:37  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.3  2003/11/11 14:49:52  oloft
 * Switching main-branch
 *
 * Revision 1.2.2.1  2003/08/07 00:21:04  erichson
 * now extends AbstractInputModel
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;


import medview.medrecords.models.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;

/**
 *
 * @author  nader
 */
public class ThreeColInputModel extends AbstractInputModel {

    private String mTeeth;
    private String mStatus;

    /** Creates a new instance of ThreeColInputModel */
    public ThreeColInputModel(String pTeeth, String pStatus, String initialDescription, String initialComment) {
        super(pTeeth + "_" + pStatus,initialDescription,initialComment, null); // No presets
        mStatus = pStatus;
        mTeeth  = pTeeth;
    }

    public ThreeColInputModel(String initialName, String initialDescription, String initialComment) {
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


