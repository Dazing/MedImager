package drymouthx.model.treetable;




/*import java.io.File;
import java.util.*;
import java.text.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;*/

import org.w3c.dom.*;


public class MyTreeModel extends AbstractTreeTableModel
                             implements TreeTableModel {


    // Number of columns.
    static protected int  cCount = 0;

    // Names of the columns.
    static protected String[]  cNames;

    // Types of the columns.
    static protected Class[]  cTypes = {TreeTableModel.class, Object.class, String.class};

    // The  returned file length for directories. 
    public static final Integer ZERO = new Integer(0); 

   public MyTreeModel(Element rotNod) {
        super(new MyTreeNode( rotNod) );

        Attr colCount = rotNod.getAttributeNode("noofcols");
        cCount = Integer.parseInt(colCount.getValue());
        cNames = new String [ cCount ];

        for (int i=0;i<cCount;i++){
            cNames [i] = rotNod.getAttributeNode("colname"+i).getValue();
        }
   }

    //
    // The TreeModel interface
    //

    public int getChildCount(Object node) { 
	MyTreeNode treeNode = ((MyTreeNode)node);
        return treeNode.getChildCount();
    }

    public Object getChild(Object node, int i) { 
	MyTreeNode treeNode = ((MyTreeNode)node);
        return treeNode.getChild(i);
    }

    //
    //  The TreeTableNode interface. 
    //

    public int getColumnCount() {
	return cCount;
    }

    public String getColumnName(int column) {
	return cNames[column];
    }

    public Class getColumnClass(int column) {
	return cTypes[column];
    }

    public void setValueAt(Object aValue, Object node, int column) {
        MyTreeNode treeNode = ((MyTreeNode)node);
        treeNode.element.setAttribute("value1", aValue.toString());
    }
    
    public Object getValueAt(Object node, int column) {
        MyTreeNode treeNode = ((MyTreeNode)node);
        try {
            switch (column) {
                case 0:
                    return "";
                case 1:
                    if (treeNode.element.getAttribute("datatype").contentEquals("Progressbar")) {
                        return new ProgressBar(Integer.parseInt(treeNode.element.getAttribute("value1")));
                    } else if (treeNode.element.getAttribute("datatype").contentEquals("Radiobutton")) {
                        return new RadioChoice(treeNode.element.getAttribute("value1").toString());
                    };
                    return null;
                      
                case 2:
                    return treeNode.element.getAttribute("value2");
                default:
                    return null;
            }
        }
	catch  (SecurityException se) { }
   	return null; 
    }
}


class MyTreeNode {
    Object[] children;      //Är en array av pekare till MyTreeNode-objekt som är barn till denna nod.
    Element element;        //Pekar ut ett objekt med data från DOM som kopplas till denna noden.


    public MyTreeNode(Element element) {
        this.element = element;
    }

    public String toString() { 
	return element.getAttribute("value0");
    }

    public int getChildCount() {
       int j= 0;
       if (element.hasChildNodes()){
            NodeList tmpElementList = element.getElementsByTagName("node");
            for (int i = 0; i<tmpElementList.getLength();i++) {
                Element tmpEl = (Element) tmpElementList.item(i);
                if (element.isSameNode(tmpEl.getParentNode())) {
                    j++;
                }
            }
        }
        return j;
    }

    public Object getChild(int i) {
    	if (children != null) {
	    return children[i];
	}
	try {
           int noofchilds= 0;
           if (element.hasChildNodes()){
               //Räkna ut antalet Children
               NodeList tmpElementList = element.getElementsByTagName("node");
                for (int j = 0; j<tmpElementList.getLength();j++) {
                    Element tmpEl = (Element) tmpElementList.item(j);
                    if (element.isSameNode(tmpEl.getParentNode())) {
                        noofchilds++;
                    }
                }
                //Deklarera Children till rätt storlek
               children = new MyTreeNode[ noofchilds ];
               //Fyll i Children[]
               int k = 0;
                for (int j = 0; j<tmpElementList.getLength();j++) {
                    Element tmpEl = (Element) tmpElementList.item(j);
                    if (element.isSameNode(tmpEl.getParentNode())) {
                        children[k] = new MyTreeNode((Element)tmpElementList.item(j));
                        k++;
                    }
                }
           } 
        } catch (SecurityException se) {}
	return children[i];
   }
}


