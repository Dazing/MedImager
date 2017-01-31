/**
 *  TouchScreenHack.java
 *  MedView
 *
 *
 *  Created by Olof Torgersson on Mon Sep 29 2003.
 * $Id: TouchScreenHack.java,v 1.5 2006/09/13 22:00:06 oloft Exp $
 * 
 * $Log: TouchScreenHack.java,v $
 * Revision 1.5  2006/09/13 22:00:06  oloft
 * Added Open functionality
 *
 * Revision 1.4  2005/07/06 12:06:30  erichson
 * Removed a println + some cleanup
 *
 * Revision 1.3  2005/06/14 15:09:48  erichson
 * Added more methods to read touchscreen data, to allow import from medForm. // NE
 *
 *
 */

package medview.medrecords.data;

import java.io.*;
import java.util.*;

import medview.datahandling.examination.tree.*;
import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.tools.*;

public class TouchScreenHack 
{

    // Could just as well have a single static method
    // but since this is just a hack....
    private static TouchScreenHack instance;

    private TouchScreenHack() {
    }

    public static TouchScreenHack instance() 
    {
        if (instance == null)
        {
            instance = new TouchScreenHack();
        }
        return instance;
    }

    public boolean readTouchScreen(File pTreeFile, ValueTabbedPane vtp)
    {
        try
        {
            Tree aNode = (new TreeFileHandler()).loadTreeFile(pTreeFile);

            return readTouchScreen(aNode, vtp);
        }
        catch(Exception e)
        {
            System.out.println("An error occured reading " + pTreeFile.toString() + ": " +  e.toString());
            return false;
        }
    }
    
    public boolean readTouchScreen(String treeString, ValueTabbedPane vtp) throws IOException
    {
        Tree aNode = (new TreeFileHandler().loadTree(treeString));
        return readTouchScreen(aNode, vtp);
    }
    
    public boolean readTouchScreen(Reader reader, ValueTabbedPane vtp) throws IOException
    {
        Tree aNode = (new TreeFileHandler().loadTree(reader));
        return readTouchScreen(aNode, vtp);
    }
    
    private boolean readTouchScreen(Tree aNode, ValueTabbedPane vtp) throws IOException
    {
        //System.out.println("TouchScreenHack readTouchScreen: " + pTreeFile.getAbsolutePath());
        
        //Tree aNode = (new TreeFileHandler()).loadTreeFile(pTreeFile);
        //System.out.println(aNode.toString());

        // loop through all tabs
        TabPanel[] tabPanes = vtp.getTabs();
        int tCount = tabPanes.length;

        for (int i=0; i<tCount; i++) 
        {
            TabPanel currentTab = tabPanes[i];
            ValueInputComponent[] inputComponents = currentTab.getInputComponents();
            int iCount = inputComponents.length;

            // loop through inputComponents
            for (int j=0; j<iCount; j++) 
            {
                ValueInputComponent currentInput = inputComponents[j];
                String name = currentInput.getName();
                //System.out.println(name);
                if (!currentInput.isIdentification()) 
                {                    
                    insertValues(aNode, currentInput); // Search tree for matching content for this input
                }
            }
        }
        return true;

    }

    private void insertValues(Tree t, ValueInputComponent vic) 
    {
        Tree aNode = t.getNode(vic.getName());

        if(aNode != null)
        {
            for (Enumeration aLeaf = aNode.getChildrenEnumeration(); aLeaf.hasMoreElements(); )
            {
                Tree   theLeaf     = (Tree) aLeaf.nextElement();
                String theVal      = theLeaf.getValue();
                //System.out.println("term: " + vic.getName() + ", value: " + theVal);
                vic.putPreset(theVal);
            }
        }        
    }
}
