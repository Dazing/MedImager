/*
 * GraphXMLParser.java
 *
 * Created on den 7 juni 2005, 12:01
 *
 * $Id: GraphXMLParser.java,v 1.6 2005/07/06 11:38:02 erichson Exp $
 *
 * $Log: GraphXMLParser.java,v $
 * Revision 1.6  2005/07/06 11:38:02  erichson
 * Moved static helper methods to separate class (XMLHelper)
 *
 * Revision 1.5  2005/06/09 15:08:28  erichson
 * Re-commit (previous one got messed up)
 *
 * Revision 1.3  2005/06/08 09:48:29  erichson
 * Added parsing of the "Attributes" array for proper ordering of attributes.
 *
 * Revision 1.2  2005/06/07 17:59:56  erichson
 * cosmetic fix
 *
 * Revision 1.1  2005/06/07 17:54:23  erichson
 * First check-in.
 *
 */

package medview.medrecords.data.graph;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.*;

import medview.medrecords.data.InvalidTemplateException;

/**
 * Parses a graph description from an XML file into a GraphInfo object.
 *
 * @author Nils Erichson
 */
public class GraphXMLParser extends misc.foundation.util.xml.XMLHelper
{
    
    private GraphSet graphSet;    
    private DocumentBuilder builder;
    private String fileLocation;
    
    
    /** Creates a new instance of GraphXMLParser */
    public GraphXMLParser() throws ParserConfigurationException, SAXException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        builder = dbf.newDocumentBuilder();                
    }    
    
    public GraphSet parse(File file) throws ParserConfigurationException, IOException, SAXException, InvalidTemplateException
    {
        fileLocation = file.getPath();
        
        graphSet = new GraphSet();
                
        Document doc = builder.parse(file);
        
        // Start at <plist> tag        
        NodeList nList = doc.getElementsByTagName("plist");
        
        if (nList.getLength() < 0)
        {
            throw new InvalidTemplateException(fileLocation,"Couldn't find <plist> tag.");
        }
        
        Node plistNode = nList.item(0);
        
        // Get Child, should be <dict>
        Node dictNode = getChildNamed(plistNode,"dict");
                        
        NodeList mainChildren = dictNode.getChildNodes();
        
        String key = null;
        for (int i = 0; i < mainChildren.getLength(); i++)
        {
            Node thisNode = mainChildren.item(i);
            String nodeName = thisNode.getNodeName();
            
            // If it's a key, store it
            if (nodeName.equalsIgnoreCase("key"))
            {
                key = getChildValue(thisNode);
            }
            
            else if ((nodeName.equalsIgnoreCase("array") && (key != null)))
            {
                if (key.equalsIgnoreCase("AvailableGraphs"))
                {
                    processAvailableGraphsArrayNode(thisNode);
                }
            }
            
            else if (nodeName.equalsIgnoreCase("dict"))
                processGraphDictNode(key, thisNode);
        }
            
        return graphSet;
    }
    
    /**
     * Process the <array> node with the AvailableGraphs <string>'s
     */
    private void processAvailableGraphsArrayNode(Node arrayNode)
    {
        NodeList arrayChildren = arrayNode.getChildNodes();
        for (int i = 0; i < arrayChildren.getLength(); i++)
        {
            // Only process <string> nodes
            Node node = arrayChildren.item(i);
            if (node.getNodeName().equalsIgnoreCase("string"))
            {            
                String name = getChildValue(node);
                // System.out.println("got AvailableGraph " + name + " from nodeName " + node.getNodeName());
                graphSet.put(name, null); // Just store the key for now
            }
        }
    }
    
    /**
     * Process a <dict> node describing a graph
     */
    private void processGraphDictNode(String graphName, Node dictNode) throws InvalidTemplateException
    {
        if (!graphSet.graphExists(graphName))
            return;
        
        GraphInfo thisGraph = new GraphInfo();
        
        NodeList dictChildren = dictNode.getChildNodes();
        
        String key = null;
        for (int i = 0; i < dictChildren.getLength(); i++)
        {
            Node thisChild = dictChildren.item(i);
            String childName = dictChildren.item(i).getNodeName();
            
            if (childName.equalsIgnoreCase("key"))
                key = getChildValue(thisChild);
            
            else if (childName.equalsIgnoreCase("dict"))
            {
                if("AttributeLabels".equalsIgnoreCase(key))
                {
                    processAttributeLabelsDictNode(thisGraph, thisChild);
                }
            }
            
            else if (childName.equalsIgnoreCase("array"))
            {
                if("Attributes".equalsIgnoreCase(key))
                {
                    processAttributesArrayNode(thisGraph, thisChild);
                }
            }
            
            else if (childName.equalsIgnoreCase("string"))
            {
                String theString = getChildValue(thisChild);
                if("GraphTitle".equalsIgnoreCase(key))
                {
                    thisGraph.graphTitle = theString;
                }
                
                else if ("MaxValue".equalsIgnoreCase(key))
                {
                    try {
                        thisGraph.maxValue = Integer.parseInt(theString);
                    } catch (NumberFormatException nfe)
                    {
                        throw new InvalidTemplateException(fileLocation, "Invalid maxValue: " + theString);
                    }
                }
                
                else if ("UsesFixMaxValue".equalsIgnoreCase(key))
                {
                    try {
                        int usesFixMaxValueInt = Integer.parseInt(theString);
                        
                        thisGraph.usesFixMaxValue = (usesFixMaxValueInt == 1 );
                        
                    } catch (NumberFormatException nfe)
                    {
                        throw new InvalidTemplateException(fileLocation, "Invalid value for UsesFixMaxValue: " + theString);
                    }
                }                
            }                            
        }        
        
        graphSet.putGraph(graphName,thisGraph);
    }
    
    /**
     * Process a dict node that contains attributeLabels
     */ 
    private void processAttributeLabelsDictNode(GraphInfo thisGraph, Node dictNode) throws InvalidTemplateException
    {
        NodeList children = dictNode.getChildNodes();
        
        String key = null;
        for (int i = 0; i < children.getLength(); i++)
        {
            Node thisNode = children.item(i);
            String name = thisNode.getNodeName();
            if ("key".equalsIgnoreCase(name))
            {
                key = getChildValue(thisNode);
            }
            else if (("string".equalsIgnoreCase(name) && (key != null)))
            {
                String str = getChildValue(thisNode);
                thisGraph.putAttributeLabel(key, str);
            }
        }
    }
    
    private void processAttributesArrayNode(GraphInfo thisGraph, Node arrayNode)
    {
        NodeList children = arrayNode.getChildNodes();
        
        for(int i = 0; i < children.getLength(); i++)
        {
            Node thisNode = children.item(i);
            String name = thisNode.getNodeName();
            if ("string".equalsIgnoreCase(name))
            {
                String str = getChildValue(thisNode);
                thisGraph.addAttribute(str);
            }            
        }        
    }
    
    
    
    
    /**
     * main method for testing
     */ 
    public static void main(String[] args)
    {
        
        try {
            GraphXMLParser parser = new GraphXMLParser();
            GraphSet graphs = parser.parse(new File("/home/erichson/medview/graph.xml"));
            graphs.dump();
             
        } catch (SAXParseException e)
        {
            System.out.println("Saxparseexception: " + e.getMessage());
            System.out.println("At: Line: " + e.getLineNumber() + ", column: " + e.getColumnNumber());        
            System.out.println("Pub id: " + e.getPublicId() + ", system id: " + e.getSystemId());        
        } catch (Exception e)
        {
            System.out.println("exception: " + e.getClass() +": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

