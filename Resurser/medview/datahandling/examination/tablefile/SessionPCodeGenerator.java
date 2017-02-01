/*
 * SessionPcodeGenerator.java
 *
 * Created on den 19 maj 2005, 11:51
 *
 * $Id: SessionPCodeGenerator.java,v 1.5 2007/01/05 10:39:02 oloft Exp $
 *
 * $Log: SessionPCodeGenerator.java,v $
 * Revision 1.5  2007/01/05 10:39:02  oloft
 * Added missing method
 *
 * Revision 1.4  2007/01/03 14:28:55  erichson
 * Listens to MVDH for user id updates.
 *
 * Revision 1.3  2005/06/03 16:05:53  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.2  2005/05/20 11:13:16  erichson
 * Removed debugging messages
 *
 * Revision 1.1  2005/05/20 08:14:25  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import medview.datahandling.*;

import java.util.*;
import javax.swing.event.*;

import misc.foundation.*;

/**
 *
 * P-code from pid generation, same as LocalSwedishPCodeGenerator but without the sync-to-disk caching. I.E. the p-codes generated
 * are for the session only and not persistent.
 *
 * Note: Based on LocalSwedishPCodeGenerator Revision 1.6  2005/02/17 10:23:09
 *
 * @author Nils, copy-pasted from LocalSwedishPCodeGenerator
 */
public class SessionPCodeGenerator 
    extends LocalSwedishPCodeGenerator  
    implements MedViewDataListener
{
    /** Creates a new instance of SessionPcodeGenerator */

    /* Fields */
    
    private HashMap pidToPCodeMap = new HashMap();

    
    /* Constructors */
    public SessionPCodeGenerator(PCodeNumberGenerator numberGenerator,
                                 boolean acceptMVDHUserIdUpdates)
    {
        super();
        
        // Get the pcode prefix from MVDH
        
        String pcodePrefix = MedViewDataHandler.instance().getUserID();
        
        if ((pcodePrefix == null) || (pcodePrefix.equals("")))
        {
            pcodePrefix = medview.visualizer.data.ApplicationManager.DEFAULT_MEDVIEW_USER_ID;
        }
        
        setGeneratedPCodePrefix(pcodePrefix);

        setPCodeNumberGenerator(numberGenerator);                
        
        if (acceptMVDHUserIdUpdates)
        {
            // Listen to MedViewDataHandler for changes to the prefix.
            MedViewDataHandler.instance().addMedViewDataListener(this);
        }
    }
    
    public SessionPCodeGenerator(String pcodePrefix,
                                 PCodeNumberGenerator numberGenerator,
                                 boolean acceptMVDHUserIdUpdates)
    {
	this(numberGenerator,acceptMVDHUserIdUpdates);
        
        setGeneratedPCodePrefix(pcodePrefix);       
    }

    
    /* Methods */
    
    public String obtainPCode(String pid, boolean consumeNr, ProgressNotifiable not) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
    {
            //System.out.println(DEBUG_PREFIX + "obtainPCode(" + pid + ", " + consumeNr + ", " + not + ") called"); // DEBUG

            // check that the pid is not already in pcode format

            if (newPCodeFormatRawPIDValidator.validates(pid))
            {
                    //System.out.println(DEBUG_PREFIX + pid + " already in new pcode-format"); // DEBUG

                    return pid; // pid is in new pcode format already
            }
            else if (oldPCodeFormatRawPIDValidator.validates(pid))
            {
                    //System.out.println(DEBUG_PREFIX + pid + " already in old pcode-format"); // DEBUG

                    return pid; // pid is in old pcode format already
            }

            // check that prefix and number generator location is set and valid

            if (!numberGenerator.isNumberGeneratorLocationSet())
            {
                    //System.out.println(DEBUG_PREFIX + "number generator location not set"); // DEBUG

                    throw new CouldNotGeneratePCodeException("Number Generator location not set!");
            }
            else if (!isGeneratedPCodePrefixSet())
            {
                    //System.out.println(DEBUG_PREFIX + "generated pcode prefix not set"); // DEBUG

                    throw new CouldNotGeneratePCodeException("Prefix in generated pcodes not set!");
            }

            // start processing (but only if the pid is in swedish pid form)

            if (swedishPNRPIDValidator.validates(pid))
            {
                    //System.out.println(DEBUG_PREFIX + pid + " recognized as swedish pnr pid"); // DEBUG

                    // normalize the pid (ex 197704032222 -> 19770403-2222)

                    pid = swedishPNRPIDValidator.normalizePID(pid);

                    //System.out.println(DEBUG_PREFIX + "pid after normalization = " + pid); // DEBUG

                    // obtain and return from cache if the pid has a mapping there already

                    if (pidToPCodeMap.containsKey(pid))
                    {

                        //System.out.println(DEBUG_PREFIX + "cache contained " + pid); // DEBUG
                        return (String) pidToPCodeMap.get(pid);
                    }

                    else
                    {
                            //System.out.println(DEBUG_PREFIX + "cache did not contain " + pid); // DEBUG
                    }

                    // do the actual generation of a new pcode based on the pid

                    //System.out.println(DEBUG_PREFIX + "performing generation for " + pid); // DEBUG

                    String generatedPCode = getGeneratedPCodePrefix(); // append prefix

                    int number = -1;

                    try
                    {
                            number = numberGenerator.getNextNumber(consumeNr); // get running number (löpnummer) from gen
                    }
                    catch (CouldNotObtainNumberException exc)
                    {
                            exc.printStackTrace();

                            throw new CouldNotGeneratePCodeException("Could not generate running number!");
                    }

                    String nrS = number + "";

                    while (nrS.length() != 6)
                    {
                            nrS = "0" + nrS; // prepend 0's
                    }

                    generatedPCode = generatedPCode + nrS;

                    if (pid.substring(0,2).equalsIgnoreCase("19")) // convert to pcode format year
                    {
                            generatedPCode = generatedPCode + "9";
                    }
                    else
                    {
                            generatedPCode = generatedPCode + "0";
                    }

                    generatedPCode = generatedPCode + pid.substring(2,4); // append year to generated pcode

                    String lIBNS = pid.substring(11,12); // start processing of gender of pnr

                    String[] even = new String[] {"0", "2", "4", "6", "8"};

                    int gender = 1; // 1 = male, 0 = female

                    for (int ctr=0; ctr<even.length; ctr++)
                    {
                            if (lIBNS.equalsIgnoreCase(even[ctr])) // check if control number is even (f)
                            {
                                    gender = 0;

                                    break;
                            }
                    }

                    generatedPCode = generatedPCode + gender; // add gender number to end

                    pidToPCodeMap.put(pid,generatedPCode); // Store it

                    return generatedPCode; // return the generated pcode
            }
            else
            {
                    throw new InvalidRawPIDException("Could not recognize pid " + pid);
            }
    }
        
    
    /* Listen to MedViewDataHandler for events.
     * We're only interested in when the user id changes, though.
     */
    public void examinationAdded(MedViewDataEvent e) {}   
    public void examinationDataHandlerChanged(MedViewDataEvent e) {}    
    public void examinationDataLocationChanged(MedViewDataEvent e) {}    
    public void examinationDataLocationIDChanged(MedViewDataEvent e) {}        
    public void examinationUpdated(MedViewDataEvent e) {}
	public void examinationRemoved(MedViewDataEvent e) {};
	public void templateAndTranslatorDataHandlerChanged(MedViewDataEvent e) {}    
    public void termAdded(MedViewDataEvent e) {}    
    public void termDataHandlerChanged(MedViewDataEvent e) {}    
    public void termLocationChanged(MedViewDataEvent e) {}    
    public void termRemoved(MedViewDataEvent e) {}    
    public void userIDChanged(MedViewDataEvent e) 
    {
        // Update the medview user id.
        setGeneratedPCodePrefix(e.getUserID());
    }    
    public void userNameChanged(MedViewDataEvent e) {}    
    public void valueAdded(MedViewDataEvent e) {}    
    public void valueRemoved(MedViewDataEvent e) {}
    
}
