/*
 * PqConst.java
 *
 * Created on den 8 november 2002, 15:46
 *
 * $Id: PqDFunConst.java,v 1.3 2006/03/15 17:01:52 erichson Exp $
 *
 * $Log: PqDFunConst.java,v $
 * Revision 1.3  2006/03/15 17:01:52  erichson
 * Minor change to support Fixing columns (inverting d..m on right side) in ThreeColTable // Nils
 *
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
 *
 * @author  nader
 */
public class PqDFunConst {
    
    //public static final int NuOfTeeth = 32;
    // public static final int NuOfPlqTabCol = 22;
    public static final int NuOfTeeth = 14;
    public static final int PlqIdxMin = 0;
    public static final int PlqIdxMax = 15;
    
    public static final int tnr = NuOfTeeth / 2;
    
    public static int calculateTabNr(int pNr)
    {        
        return (pNr < tnr ? tnr - pNr: pNr - (tnr -1));
    }
    
    public static boolean isLeftSideOfMouth(int pNr)
    {        
        return (pNr < tnr);
    }               
    
    
    public static PqDFunConst getInstance() {
        if (mInstance == null){
            mInstance = new PqDFunConst();
        }
        return mInstance;
    }
                
    
    private static PqDFunConst mInstance;
    private PqDFunConst() {
    }
    
    
    
    
    
}
