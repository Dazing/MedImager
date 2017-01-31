
package medview.medrecords.plaqueIndex.plaqueIndexS;
//import medview.medrecords.data.*;
/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * @author Nader Nazari
 * @version
 */
import medview.medrecords.data.*;

public class PlaqueIndex {
    
    private   Object[]  mValues;
    private   int       mToothSerial;
    private   boolean   mHasStartCol;
    

   public PlaqueIndex(int pSerial) {
        mHasStartCol    = false;
        mValues         = new Object[PqConst.NuOfPlqTabCol] ;
        mToothSerial    = pSerial;
        
        for(int i = 0; i < mValues.length ; i++){
           // mValues[i] = new Integer(3);
            mValues[i] = new String("-");
        }
    }
    public PlaqueIndex(int pSerial,String pRowName) {
        mHasStartCol    = true;
        mValues         = new Object[PqConst.NuOfPlqTabCol + 1];
        mToothSerial    = pSerial;     
        
        mValues[0]   = new String(pRowName);
        for(int i = 1; i < mValues.length ; i++){
           // mValues[i] = new Integer(3);
            mValues[i] = new String("-");
        }
    }
    
    public Object getCell(int aCol){
        return mValues[aCol] ;
    }
    
    public void setCell(int aCol, Object aValue){
        mValues [aCol] = aValue;
    }
    
    
}




   /* public PlaqueIndex() {
                mValues[0] = new String("The Title");
                mValues[1] = new Integer(40);
                mValues[2] = new Integer(0);
                mValues[3] = new Boolean(false);
    }  
  */
    
    