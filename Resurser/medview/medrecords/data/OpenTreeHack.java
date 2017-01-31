
/**
 *  OpenTreeHack.java
 *  MedView
 *
 *  Created by Olof Torgersson on 2006-09-07.
 *  $Id: OpenTreeHack.java,v 1.1 2006/09/13 22:00:43 oloft Exp $
 *
 */

package medview.medrecords.data;

import java.io.*;
import java.util.*;

import medview.common.data.*;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.examination.tree.*;
import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.tools.*;


public class OpenTreeHack 
{
	
	private final static String fileSep = System.getProperty("file.separator");
	
    // Could just as well have a single static method
    // but since this is just a hack....
    private static OpenTreeHack instance;
	
	private Date date;
	
    private OpenTreeHack() {
		date = null;
    }
	
    public static OpenTreeHack instance() 
    {
        if (instance == null)
        {
            instance = new OpenTreeHack();
        }
        return instance;
    }
	
    public boolean readTreeFile(File pTreeFile, ValueTabbedPane vtp) {
		Tree tree;
        try {
			tree = MedViewUtilities.loadTree(pTreeFile);
		}
        catch(Exception e) {
            System.out.println("An error occured reading " + pTreeFile.toString() + ": " +  e.toString());
            return false;
		}
		try {
			date = MedViewUtilities.constructExaminationDate(tree);
		}
		catch(Exception e) {
			System.out.println("An error occured extracting date from " + pTreeFile.toString() + ": " +  e.toString());
			return false;
		}
		return loadValuesFromTree(tree, vtp);
    }
    
	public Date getDate(){
		return date;
	}
    
    private boolean loadValuesFromTree(Tree aNode, ValueTabbedPane vtp)
    {
        //  System.out.println("TouchScreenHack readTouchScreen: " + pTreeFile.getAbsolutePath());
        
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
                if (true /*!currentInput.isIdentification()*/) 
                {                    
                    insertValues(aNode, currentInput); // Search tree for matching content for this input
                }
            }
        }
		// Plaque
		vtp.loadPlaqueTree(aNode);
		
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
				
				if (vic instanceof PictureChooserInput) {
					//System.out.println("term: " + vic.getName() + ", value: " + theVal);
					theVal = fixImagePath(theVal);
				}
                vic.putPreset(theVal);
            }
        }        
    }
	
	private String fixImagePath(String imagePath) {
		String newPath = null;
		char fileSepChar = fileSep.toCharArray()[0];
		
		newPath = imagePath.replace('\\', fileSepChar);
		newPath = newPath.replace('/', fileSepChar);
		
		String picturesPath = getPicturesPath();
		
		//System.out.println("Fix image Paths: Old image path=" + imagePaths[i]);
		
		String relativePath = getPictureRelativeImagePath(newPath);
		//System.out.println("relative part of current image: " + relativePath);
		newPath = picturesPath + fileSep + relativePath;
		
		//System.out.println("Fix image Paths: new (relative) image path=" + imagePaths[i]);
		return newPath;
	}
	
	
	private String getPicturesPath()
	{
		String picturePath = MedViewDataHandler.instance().getExaminationDataLocation() + fileSep + "Pictures";
		//System.out.println("getPicturesPath() gave = " + path);
		return picturePath;
	}
	
	
	
	/**
		* Returns the part of a picture path relative to the "Pictures" part
	 * for example, "c:/mvd/pictures/g01/g01.jpg" returns "/g01/g01.jpg"
	 */
	private String getPictureRelativeImagePath(String oldPath)
	{
		StringTokenizer tok = new StringTokenizer(oldPath, File.separator);
		
		String nextToLast = null;
		String last = null;
		
		
		while (tok.hasMoreTokens())
		{
			nextToLast = last;
			last = tok.nextToken();
		}
		
		String fileName = last;
		String directory = nextToLast;
		
		if (directory == null)
		{
			return fileName; // no path before it
		}
		
		// remove extension of the file name
		
		String fileNameWithoutExtension;
		int dotPosition = last.lastIndexOf('.');
		if (dotPosition < 0)
		{
			fileNameWithoutExtension = last;
		}
		else
		{
			fileNameWithoutExtension = last.substring(0, dotPosition); // will not include the dot
		}
		
		// System.out.println("directory = " + directory + ", fileNameWithoutExtension = " + fileNameWithoutExtension);
		
		/*
		 String relativePart;
		 
		 if (directory.toUpperCase().equals(fileNameWithoutExtension.toUpperCase())) {
			 System.out.println("equal, keeping directory part");
			 relativePart = directory + fileSep + fileName;
		 } else { // not equal
			 relativePart =  fileName;
		 }
		 System.out.println("relativePart = " + relativePart);
		 return relativePart;
		 */
		
		String relativePart = directory + fileSep + fileName;
		
		String testPath = getPicturesPath() + fileSep + fileName;
		if (new java.io.File(testPath).exists())
		{
			return fileName;
		}
		
		testPath = getPicturesPath() + fileSep + relativePart;
		if (new java.io.File(testPath).exists())
		{
			return relativePart;
		}
		
		// If we reach this part, we could not find the image...
		System.out.println("DataSource: Could not find a matching image for " + relativePart);
		
		return relativePart;
	}
	
}
