package medview.medrecords.data;


import java.io.*;
import java.util.*;
import medview.datahandling.examination.tree.*;
import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.tools.*;
import javax.swing.*;
import medview.datahandling.*;

/**
 * Handles the interaction between the PDA and the PC. 
 */
public class PDAHack {
    // Could just as well have a single static method
    // but since this is just a hack....
    private static PDAHack instance;

    public PDAHack() {}

    public static PDAHack instance() {
        if (instance == null) {
            instance = new PDAHack();
        }
        return instance;
    }

    /**
     * Presets input components with values given in the tree file. 
     * 
     * @param pTreeFile a tree file with values. 
     * @param pVTP a ValueTabbedPane with input components.  
     * @return true if the process was a success, false otherwise. 
     */
    public boolean readPDA(File pTreeFile, ValueTabbedPane pVTP) {
        try {
            Tree aNode = (new TreeFileHandler()).loadTreeFile(pTreeFile);
            return readPDA(aNode, pVTP);
        } catch(Exception e) {
            return false;
        }
    }
    

    /**
     * Presets input components with values given in the tree file. 
     * 
     * @param pFileName the name for a tree file with values. 
     * @param pVTP a ValueTabbedPane with input components.
     * @return true if the process was a success, false otherwise. 
     */
    public boolean readPDA(String pFileName, ValueTabbedPane pVTP) throws IOException, Exception {
        Tree aNode = (new TreeFileHandler().loadTree(pFileName));
        return readPDA(aNode, pVTP);
    }
    
    /**
     * Presets input components with values given in the tree file. 
     * 
     * @param pReader a file reader for a tree file with values. 
     * @param pVTP a ValueTabbedPane with input components.
     * @return true if the process was a success, false otherwise. 
     */
    public boolean readPDA(Reader pReader, ValueTabbedPane pVTP) throws IOException, Exception {
        Tree aNode = (new TreeFileHandler().loadTree(pReader));
        return readPDA(aNode, pVTP);
    }
    
    /**
     * Presets input components with values given in the tree. 
     * 
     * @param pTree a tree with values. 
     * @param pVTP a ValueTabbedPane with input components.
     * @return true if the process was a success, false otherwise. 
     */
    private boolean readPDA(Tree pTree, ValueTabbedPane pVTP) throws IOException, Exception {   
    	/* Removes unwanted nodes. */
       	Tree deleteNode;
    	deleteNode = pTree.getNode(MedViewDataConstants.DATE_TERM_NAME);
    	if (deleteNode != null) {
    		deleteNode.deleteNode();
    	}
    	deleteNode = pTree.getNode(MedViewDataConstants.PCODE_TERM_NAME);
    	if (deleteNode != null)	{
    		deleteNode.deleteNode();
    	}
    	deleteNode = pTree.getNode(MedViewDataConstants.CONCRETE_ID_TERM_NAME);
    	if (deleteNode != null) {
    		deleteNode.deleteNode();
    	}
    	
        /* Loop through all tabs. */
        TabPanel[] tabPanes = pVTP.getTabs();
        for (int i = 0; i < tabPanes.length; i++) {
            TabPanel currentTab = tabPanes[i];
            ValueInputComponent[] inputComponents = currentTab.getInputComponents();

            /* Loop through inputComponents */
            for (int j = 0; j < inputComponents.length; j++) {
                ValueInputComponent currentInput = inputComponents[j];
                /* Inserts values corresponding to this inputs, if such exists. */
                insertValues(pTree, currentInput);
            }
        }
        
        /* Presets the Mucos node with values. */
        Tree mucosNode = pTree.getNode("Mucos");
        if (mucosNode != null) {
        	pVTP.getMucosPanel().setValues(mucosNode);
        	mucosNode.deleteNode();
        }

        boolean rVal = true;
        String exceptionMsg = "Please use a correct template.\nTerms which could not be inserted:\n";
        ExaminationValueTable table = new ExaminationValueTable(pTree);
        String[] termsWithValues = table.getTermsWithValues();
        for (int i=0; i<termsWithValues.length; i++) {
        	Tree n = pTree.getNode(termsWithValues[i]);
        	if (n != null) {
        		exceptionMsg = exceptionMsg + "\n" + termsWithValues[i];
        		rVal = false;
        	}
        }
        if (!rVal) {
        	throw new Exception(exceptionMsg);
        }
        
        return rVal;
    }

    /**
     * Sets values for the input component, if such exists in the tree.
     * Removes any values that has been set. 
     * 
     * @param pTree the tree with values. 
     * @param pVIC the input component. 
     */
    private void insertValues(Tree pTree, ValueInputComponent pVIC) {
        Tree aNode = pTree.getNode(pVIC.getName());
               
        if (aNode != null) {
            for (Enumeration aLeaf = aNode.getChildrenEnumeration(); aLeaf.hasMoreElements();) {
                Tree theLeaf = (Tree) aLeaf.nextElement();
                String theVal = theLeaf.getValue();
                pVIC.putPreset(theVal);
            }
            aNode.deleteNode();
        }     
    }
}

