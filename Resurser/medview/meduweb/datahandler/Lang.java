package medview.meduweb.datahandler;

import java.util.HashMap;

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
 *This class handles the language independence for the mEduWeb 
 *system. It contains methods for retrieving text in the language
 *given in the session object from the XML-file containing language
 *specific texts for the specific page.
 *@author Frans, Tove and Fredrik
 *@version 2.0 
 */

public class Lang {
    
    private static Document document = null;
    private static LangDoc ld = new LangDoc();
	/** The HashMap that holds all Documents **/
	private static HashMap documents = new HashMap();

    //Absolute adress to the xml language file. 
    //private static String file = "Z:\\jsp_work_dir\\Language.xml";
    private static String file = null;

	/** Where the languagefiles are storde on disk. **/
	private static String dir = System.getProperty("language.dir");

/**
     *This method returns a string in the language specified in the request object.
     *@param a containing the first part of the path to follow in the xml-file.
     *@param b containing the second part of the path to follow in the xml-file.
     *@param c containing the third part of the path to follow in the xml-file.
     *@param d containing the fourth part of the path to follow in the xml-file.
     *@param e containing the fifth part of the path to follow in the xml-file.
     *@return the string found at the given path in the xml-file.
     */
    public static String lang(String a, String b, String c, String d, String e){
	String[] args = {a, b, c, d, e};
	return listLang(a,args);
    }
/**
     *This method returns a string in the language specified in the request object.
     *@param a containing the first part of the path to follow in the xml-file.
     *@param b containing the second part of the path to follow in the xml-file.
     *@param c containing the third part of the path to follow in the xml-file.
     *@param d containing the fourth part of the path to follow in the xml-file.
     *@return the string found at the given path in the xml-file.
     */
    public static String lang(String a, String b, String c, String d){
	String[] args = {a, b, c, d};
	return listLang(a,args);
    }

    /**
     *This method returns a string in the language specified in the request object.
     *@param a containing the first part of the path to follow in the xml-file.
     *@param b containing the second part of the path to follow in the xml-file.
     *@param c containing the third part of the path to follow in the xml-file. 
     *@return the string found at the given path in the xml-file.
     */
    public static String lang(String a, String b, String c){
	String[] args = {a, b, c};
	return listLang(a,args);
    }

    /**
     *This method returns a string in the language specified in the request object.
     *@param a containing the first part of the path to follow in the xml-file.
     *@param b containing the second part of the path to follow in the xml-file. 
     *@return the string found at the given path in the xml-file.
     */
    public static String lang(String a, String b){
	String[] args = {a, b};
	return listLang(a,args);
    }

    /**
     *This method returns a string in the language specified in the request object.
     *@param a containing the path to follow in the xml-file. 
     *@return the string found at the given path in the xml-file.
     */
    public static String lang(String a){
	String[] args = {a};
	return listLang(a,args);
    }

    /**
     *This method returns a string in the language specified in the request object.
     *@param args containing a list of the parts of the path to follow in the xml-file. 
     *@return the string found at the given path in the xml-file.
     */
    private static String listLang(String page, String[] args) {
	synchronized(ld) {
		if(!documents.containsKey(page)) {
			System.out.print("Making XML doc (" + page + ")...");
			document = ld.getDoc(dir + page + ".xml");
			documents.put(page,document);
			System.out.println("done!");

		} else {
			document = (Document)documents.get(page);
		}
	}
	//retrieves the string from the document object.
	String str = getString(args);
	
	return str;
    }
 
    /**
     *This method finds a string from the document object located at the path given.
     *@param args containing a list of the parts of the path to follow in the document object. 
     *@return the string found at the given path in the document object.
     */
    private static String getString(String[] args)
    {
       
	//While there are arguments(parts of the path) left 
	//in the args list, move down the document tree.
	NodeList listOfElements = null;
        String result = null;
	try {
	
        Element firstElement = (Element)document.getElementsByTagName(args[0]).item(0);
	int i = 1;
	while (i < args.length) {
	    listOfElements = firstElement.getElementsByTagName( args[i] );
	    firstElement = (Element)listOfElements.item(0);
	    i=i+1;
	}

	//When at the last node (the leaf), get the childnodes.
	//This list can contain unwanted data, which is ignored.
	NodeList list = firstElement.getChildNodes();
	for (i = 0 ; i < list.getLength() ; i ++ ){
	    String value = 
		((Node)list.item( i )).getNodeValue().trim();
	    if( value.equals("") || value.equals("\r") ){
		continue; //keep iterating
	    }
	    else{
		result = value;
		break; //found the firstName!
	    }
	}
	} catch (NullPointerException npe) {
	    result = "error in string!";
	}
	return result;
    }
}
