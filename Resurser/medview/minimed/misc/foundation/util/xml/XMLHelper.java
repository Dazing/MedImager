/*
 * XMLHelper.java
 *
 * Created on den 6 juli 2005, 09:32
 *
 * $Id: XMLHelper.java,v 1.1 2006/05/29 18:33:04 limpan Exp $
 *
 * $Log: XMLHelper.java,v $
 * Revision 1.1  2006/05/29 18:33:04  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.2  2005/07/18 13:29:51  erichson
 * Added CDATA removal
 *
 * Revision 1.1  2005/07/06 12:05:39  erichson
 * First check-in.
 *
 */

package misc.foundation.util.xml;

import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper methods for xml processing.
 *
 * @author Nils Erichson
 */
public class XMLHelper 
{
    
    public static String getChildValue(Node node) throws DOMException
    {
        Node child = node.getFirstChild();
        if (child != null)
        {
            String val = child.getNodeValue();
            if (val.toUpperCase().startsWith("[CDATA"))
            {
                val = val.substring(7,val.length()-2);
            }
            return val;
        }
        else
            return "NULL";
    }
        
    // case sensitive
    public static Node[] getChildrenNamed(Node node, String name) 
    {
        ArrayList v = new ArrayList();
        
        NodeList nList = node.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++)
        {
            Node n = nList.item(i);
            if (n.getNodeName().equalsIgnoreCase(name))
            {
                v.add(n);
            }
        }
        
        Node[] nodeArray = new Node[v.size()];
        nodeArray = (Node[]) v.toArray(nodeArray);
        return nodeArray;
    }
    
    public static Node getChildNamed(Node node, String name) 
    {
        return getChildrenNamed(node, name)[0];
    }
    
    public static boolean getBooleanValue(Node node)
    {
        String value = node.getNodeValue();
        return parseBoolean(value);
    }
    
    public static boolean getChildBooleanValue(Node node)
    {
        Node child = node.getFirstChild();
        return getBooleanValue(child);
    }
    
    public static boolean parseBoolean(String value)
    {        
        if (value.trim().equalsIgnoreCase("true"))
            return true;
        else
            return false;        
    }
}
