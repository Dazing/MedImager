/*
 * ThreeColBolValue.java
 *
 * Created on den 10 december 2002, 17:29
 *
 * $Id: ThreeColBolValue.java,v 1.3 2004/01/04 14:40:59 oloft Exp $
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;

import medview.medrecords.data.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;

/**
 *
 * @author  nader
 */
public class ThreeColBolValue extends ThreeColValue {
    
    /** Creates a new instance of ThreeColBolValue */
    
    public ThreeColBolValue() {
        mValues         = new Object[3] ;
        
        for(int i = 0; i < mValues.length ; i++){ 
            mValues[i] = new Boolean(false);
        }
    }
    
    
}
