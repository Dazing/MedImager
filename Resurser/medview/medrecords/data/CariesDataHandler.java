// $Id: CariesDataHandler.java,v 1.7 2010/05/06 19:14:44 oloft Exp $

package medview.medrecords.data;

import medview.medrecords.components.ValueTabbedPane;
import medview.medrecords.components.TabPanel;
import medview.medrecords.components.inputs.ValueInputComponent;
import medview.datahandling.MedViewDataSettingsHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.*;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;


/**
    A singleton class to manage getting caries data from an external database
    All informaton needed to connect and get the data from the database is
    found in an xml-file. This xml-file is encrypted using DES encryption.
    The file is decrypted (in memory) in the readXMLDBConfig method. There
    is also a method saveXMLDocument that can save the xml-file encrypted
    or as plain text.

    * @author  Andreas Argirakis
 */
public class CariesDataHandler
{
    private java.sql.Connection  con = null;
    public static final String KARIES = "Karies";
    public static final int KARIES_TERMS = 40;
    private String url;
    private String serverName;
    private String portNumber;
    private String databaseName;
    private String userName;
    private String password;
    private String sql;
    private static CariesDataHandler instance = null;
    // Informs the driver to use server a side-cursor,
    // which permits more than one active statement
    // on a connection.
    private final String selectMethod = "cursor";

    protected CariesDataHandler()
    {
    }
    public static CariesDataHandler getInstance()
    {
        if(instance == null)
        {
            instance = new CariesDataHandler();
        }
        return instance;
    }
    /**
    * Returns the complete url to use to connect to the database
     * @return complete url to connect to database
    */
    private String getConnectionUrl()
    {
        return url+serverName+":"+portNumber+";databaseName="+databaseName+";selectMethod="+selectMethod+";";
    }

    /**
    * Connects to a database and returns the connection
    * @return a connection to the caries database
    */
    private java.sql.Connection getConnection()
    {
        try{
           Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
           con = java.sql.DriverManager.getConnection(getConnectionUrl(),userName,password);
           if(con!=null) System.out.println("Caries DB Connection Successful!");
        }catch(Exception e){
           //System.out.println("Error Trace in getConnection() : " + e.getMessage());
        }
        return con;
    }
    /**
    * Parses the database settings, connects to the database and returns
    * an array of strings of all the values of the database columns
     * @param pCode person identification number in form xxxxxxxxxx
     * @return array of strings of caries values
     * @throws SQLException if can't connect to database
     * @throws IOException if can't find caries file
    */
    private String[] getCariesfromDB(String pCode) throws SQLException,IOException,ParserConfigurationException,SAXException
    {
        String xmlFile = PreferencesModel.instance().getCariesFileLocation();

        boolean readOK = false;

        readOK = readXMLDBConfig(new File(xmlFile));

        if(!readOK)
        {
            System.err.println("Error creating encryption scheme");
            throw new IOException("Error creating encryption scheme");
        }
        String[] cariesValues = new String[KARIES_TERMS];


        //replace $PNUMBER$ in the SQL query with the real person number
        sql = sql.replace("$PNUMBER$",pCode);

        con= this.getConnection();
        if(con!=null)
        {
            Statement stmt;
            ResultSet rset;
            stmt = con.createStatement ();
            rset = stmt.executeQuery(sql);

            while (rset.next())
            {
                for(int i = 0; i<KARIES_TERMS; i++)
                {
                    cariesValues[i] = rset.getString(i+1);
                }
            }

            rset.close();
            stmt.close();
            closeConnection();
        }
        else //con = null
        {
            System.err.println("Can't connect to external database!");
            throw new SQLException("Can't connect to external database!");
        }

        return cariesValues;
    }
    /**
    * Close the database connection
    */
    private void closeConnection()
    {
        try
        {
           if(con!=null)
           {
                con.close();
           }
           con=null;
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }

    /**
    * Populate the caries tab with data from an external database
    * @param pCode person identifaction number in the form xxxxxxxxxx
    * @param valueTabbedPane ValueTappedPane containing the tabs
    * @throws SQLException if can't connect to database
    * @throws IOException if can't parse or find caries file
    */
    public void getCariesData(String pCode, ValueTabbedPane valueTabbedPane) throws SQLException, IOException,ParserConfigurationException,SAXException
    {
        //get caries data from database
        LinkedHashMap tab = valueTabbedPane.getTabsTable();
        TabPanel cariesTab = (TabPanel)tab.get(KARIES);

        if(cariesTab!=null)
        {
            ValueInputComponent[] comp = cariesTab.getInputComponents();

            String[] cariesValues =  getCariesfromDB(pCode);
            //String[] cariesValues =  {"1","2","3","4","5","6","7","8","9"};
            for(int j = 0;j<cariesValues.length;j++)
            {
                comp[j].putPreset(cariesValues[j]);
            }
        }
        else
        {
            System.out.println("Can't find " + KARIES + " tab!");
        }
    }


    /**
    * Parses the database settings file and sets the member variables
    * used to get the data
    * @param xmlFile An encrypted xmlfile
    * @return false if there was an error creating the encryption
    * @throws IOException if can't parse or find the file
    * @throws ParserConfigurationException
    * @throws SAXException
    */
    public boolean readXMLDBConfig(File xmlFile) throws IOException,
                                                ParserConfigurationException,SAXException
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc;


        DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();

        FileInputStream fis = new FileInputStream(xmlFile);

        //for encrypting the file
        CipherInputStream cis;
        Cipher cipher;
        DESKeySpec desKeySpec;
        SecretKeyFactory keyFactory;
        SecretKey secretKey;
        byte[] desKeyData = { (byte)0xb6, (byte)0xc5, (byte)0x63, (byte)0x34,
                              (byte)0x05, (byte)0x56, (byte)0xd7, (byte)0x02,
                              (byte)0xf5, (byte)0xfe, (byte)0x26, (byte)0xb6,
                              (byte)0x9a, (byte)0x36, (byte)0xd7, (byte)0x1f};

        try
        {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desKeySpec = new DESKeySpec(desKeyData);
            keyFactory = SecretKeyFactory.getInstance("DES");
            secretKey = keyFactory.generateSecret(desKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }
        catch(Exception e)
        {
            System.out.println("Error occured: " + e.getMessage());
            return false;
        }

        cis = new CipherInputStream(fis,cipher);


        //temporary testing: saving the encrypted file to disk --------------
        /*FileOutputStream fos;
        byte[] b = new byte[8];
        fos = new FileOutputStream("b.txt");
        int o = cis.read(b);
        while (o != -1)
        {
            fos.write(b, 0, o);
            o = cis.read(b);
        }*/
        // ------------------------------
        


        doc = builder.parse(cis);




        NodeList dbInfoNodeList = doc.getElementsByTagName("DBINFO");

        //System.out.println("Got " + categoryNodeList.getLength() + " categories");

        for (int i=0; i < dbInfoNodeList.getLength(); i++)
        {
            Node catNode = dbInfoNodeList.item(i); // Get category no. i
            //System.out.println(catNode.getNodeName());

            // Read its attributes

            NamedNodeMap attributes = catNode.getAttributes();
            //System.out.println("Got " + attributes.getLength() + " attribs");
            for (int c = 0; c < attributes.getLength(); c++)
            {
                Node child = attributes.item(c);
                String attributeName = child.getNodeName();
                //System.out.println(child.getNodeName());

                        String attributeValue = child.getNodeValue();
                        if (attributeValue == null)
                            attributeValue = "";

                        if (attributeName.equalsIgnoreCase("url"))
                        {
                            url = attributeValue;
                        }
                        else if (attributeName.equalsIgnoreCase("serverName"))
                        {
                            serverName = attributeValue;
                        }
                        else if (attributeName.equalsIgnoreCase("portnumber"))
                        {
                            portNumber = attributeValue;
                        }
                        else if (attributeName.equalsIgnoreCase("databaseName"))
                        {
                            databaseName = attributeValue;
                        }
                        else if (attributeName.equalsIgnoreCase("userName"))
                        {
                            userName = attributeValue;
                        }
                        else if (attributeName.equalsIgnoreCase("password"))
                        {
                            password = attributeValue;
                        }
                        //System.out.println(attributeValue);

            }
        }
        NodeList SQLNodeList = doc.getElementsByTagName("SQL");
        for (int i=0; i < SQLNodeList.getLength(); i++)
        {
            Node sqlNode = SQLNodeList.item(i); // Get category no. i
            sql = sqlNode.getFirstChild().getNodeValue();
        }


        //saveXMLDocument("testar.xml",doc, true);
        return true;

    }

/** Saves XML Document into XML file.
* @param fileName XML file name
* @param doc XML document to save
* @param encrypt true encrypts the file
* @return <B>true</B> if method success <B>false</B> otherwise
*/
public boolean saveXMLDocument(String fileName, Document doc, boolean encrypt)
{
    System.out.println("Saving XML file... " + fileName);
    // open output stream where XML Document will be saved
    File xmlOutputFile = new File(fileName);
    FileOutputStream fos;

    //for encrypting the file
    CipherOutputStream cos;
    Cipher cipher;
    Transformer transformer;
    DESKeySpec desKeySpec;
    SecretKeyFactory keyFactory;
    SecretKey secretKey;
    byte[] desKeyData = { (byte)0xb6, (byte)0xc5, (byte)0x63, (byte)0x34,
                          (byte)0x05, (byte)0x56, (byte)0xd7, (byte)0x02,
                          (byte)0xf5, (byte)0xfe, (byte)0x26, (byte)0xb6,
                          (byte)0x9a, (byte)0x36, (byte)0xd7, (byte)0x1f};

    try
    {
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desKeySpec = new DESKeySpec(desKeyData);
        keyFactory = SecretKeyFactory.getInstance("DES");
        secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    }
    catch(Exception e)
    {
        System.out.println("Error occured: " + e.getMessage());
        return false;
    }


    try
    {
        fos = new FileOutputStream(xmlOutputFile);
        cos = new CipherOutputStream(fos,cipher);
    }
    catch (FileNotFoundException e)
    {
        System.out.println("Error occured: " + e.getMessage());
        return false;
    }
    // Use a Transformer for output
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try
    {
        transformer = transformerFactory.newTransformer();
    }
    catch (TransformerConfigurationException e)
    {
        System.out.println("Transformer configuration error: " + e.getMessage());
        return false;
    }

    DOMSource source = new DOMSource(doc);
    StreamResult result;
    if(encrypt)
    {
        result = new StreamResult(cos);
    }
    else
    {
        result = new StreamResult(fos);
    }


    // transform source into result will do save
    try
    {
        transformer.transform(source, result);
        result.getOutputStream().close();    //important to close, otherwise the whole file
                                             // might not be saved if encryption is used

    }
    catch (Exception e)
    {
        System.out.println("Error: " + e.getMessage());
    }

    System.out.println("XML file saved.");

    return true;
}

}
