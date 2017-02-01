package medview.meduweb.datahandler;

import org.apache.xml.utils.*;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.*;

/**
 *This class contains a method for parsing an xml-file into a  
 *document object.
 *@author Frans and Tove
 *@version 1.00001
 */

public class LangDoc {

    public LangDoc() { 
    }

    /**
     *This method parses an xml-file and returns a document object.
     *@param filename the xml-file to parse.
     *@return a document object.
     */
    public Document getDoc(String filename) {
	
	Document document = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	try {
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    document = builder.parse( new File(filename) );
	    
	} catch (SAXException sxe) {
	    // Error generated during parsing
	    Exception  x = sxe;
	    if (sxe.getException() != null)
		x = sxe.getException();
	    x.printStackTrace();
	    
	} catch (ParserConfigurationException pce) {
	    // Parser with specified options can't be built
	    pce.printStackTrace();
	    
	} catch (IOException ioe) {
	    // I/O error
	    ioe.printStackTrace();
	    
	}// main
	
	return document;
    }
}
