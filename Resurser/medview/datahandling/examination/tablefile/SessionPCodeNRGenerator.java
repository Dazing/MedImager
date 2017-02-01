/*
 * VisualizerSessionPCodeNRGenerator.java
 *
 * Created on den 5 april 2004, 12:33
 *
 * $Id: SessionPCodeNRGenerator.java,v 1.2 2007/01/03 14:12:15 erichson Exp $
 *
 * $Log: SessionPCodeNRGenerator.java,v $
 * Revision 1.2  2007/01/03 14:12:15  erichson
 * Cosmetic update.
 *
 * Revision 1.1  2005/05/20 08:15:20  erichson
 * Moved from medview.datahandling.examination.tablefile;
 *
 * Revision 1.2  2004/11/10 13:05:43  erichson
 * Updated to PCodeNumberGenerator interface
 *
 * Revision 1.1  2004/04/12 20:14:36  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

/**
 * Simple NRGenerator for use during a Visualizer session. Does absolutely no session-to-session
 * saving since that is not useful for Visualizer right now. Later we will most likely want to
 * move to a centralized authority.
 *
 * Note: Formerly medview.visualizer.data.VisualizerSessionPCodeNRGenerator
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class SessionPCodeNRGenerator implements medview.datahandling.PCodeNumberGenerator 
{
    
    private int lopnummer;
    private String numberGeneratorLocation = "VISUALIZER"; // Does not matter
    
    /** Creates a new instance of VisualizerSessionPCodeNRGenerator */
    public SessionPCodeNRGenerator() 
    {
        lopnummer = 1;
    }
    
    /**
     * Generates the next counter number to
     * use for the next generated pcode.
     */
    public int getNextNumber()
        throws medview.datahandling.CouldNotObtainNumberException
    {    
        return getNextNumber(true); // As in LockFilePCodeNRGenerator        
    }
    
    /**
     * Generates the next counter number to
     * use for the next generated pcode. This
     * version of the method allows you to
     * specify whether or not a number will be
     * consumed by the call. If not, the next
     * call will return the same number.
     */
    public int getNextNumber(boolean consumeNr) 
        throws medview.datahandling.CouldNotObtainNumberException
    {
        int nr = lopnummer;
        if (consumeNr) {
            lopnummer++;
        }
        return nr;
    }               
    
    /* The following three are not really meaningful for the Visualizer */
    public String getNumberGeneratorLocation() 
    {
        return numberGeneratorLocation;
    }
    
    public boolean isNumberGeneratorLocationSet()
    {
        return (numberGeneratorLocation != null);
    }
    
    public void setNumberGeneratorLocation(String loc) 
    {
        numberGeneratorLocation = loc;
    }
    
}
