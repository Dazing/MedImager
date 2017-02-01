/*
 * MedFormConnection.java
 *
 * Created on den 13 juni 2005, 14:52
 *
 * $Id: MedFormConnection.java,v 1.3 2006/06/02 11:04:38 oloft Exp $
 *
 * $Log: MedFormConnection.java,v $
 * Revision 1.3  2006/06/02 11:04:38  oloft
 * Added encoding
 *
 * Revision 1.2  2005/06/14 15:34:13  erichson
 * Fixed an error message.
 *
 * Revision 1.1  2005/06/14 15:18:04  erichson
 * First check-in.
 *
 */

package medview.medrecords.data;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Fetches Tree data from medForm via the WWW.
 *
 * @author Nils Erichson <erichson>
 */
public class MedFormConnection
{
    private String pathToMedForm; // path to getLatestTree.php
    private String user;
    private String mvd;
    
    /** Creates a new instance of TreeFetcher */
    public MedFormConnection(String medformPath, String user, String mvd)    
    {
        pathToMedForm = medformPath;
        this.user = user;
        this.mvd = mvd;
        
		System.out.println("MedFormConnection user: " + user);
    }
    /**
     * Gets the latest Tree for a patient
     */
    public String getLatestTree(String pid) throws MalformedURLException, IOException
    {
        String urlString = pathToMedForm + "/getlatesttree.php?user=" + user +
            "&mvd=" + mvd +
            "&pid=" + pid;
        
		System.out.println("getLatestTree: " + urlString);

        URL url = new URL(urlString);
        Object content = url.getContent();                
        
        /*if (content instanceof sun.net.www.content.text.PlainTextInputStream ) { }
        
        else if (content instanceof sun.net.www.protocol.http.HttpURLConnection$HttpInputStream) { }
        */
        
		// Use proper encoding here
        if (content instanceof InputStream)
        { 
            StringBuffer contentBuffer = new StringBuffer();
            
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) content, misc.foundation.io.IOConstants.ISO_LATIN_1_CHARACTER_ENCODING));
            while (br.ready())
            {
                contentBuffer.append( br.readLine() + "\n");
            }
            
            String contentString = contentBuffer.toString();
                                    
            if (contentString.startsWith("<treefile>"))
            {
                contentString = contentString.substring("<treefile>".length());
                return contentString;
            }                                            
                else
            {
                throw new IOException("MedForm returned: " + contentString);
            }                        
        
        }
        else 
        {
            throw new IOException("Unknown output type: " + content.getClass());
        }                
    }
    
    /**
     * Test method
     */
    public static void main(String[] args)
    {
        MedFormConnection fetcher = new MedFormConnection("http://khaom.odontologi.gu.se/nilstest/",
                  "marita", "boma");
        
        try
        {   
            System.out.println("Trying...");
            String content = fetcher.getLatestTree("JL00019510");
            System.out.println(content);
        } catch (MalformedURLException e)
        {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("IOException: " + e.getMessage());
        }        
    }
    
}
