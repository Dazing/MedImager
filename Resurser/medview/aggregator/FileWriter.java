package medview.aggregator;

import java.io.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class FileWriter {
    private	    GroupNode                   mRoot;
    
    public FileWriter(GroupNode aRoot) {
        mRoot = aRoot ;
    }
    
    
    /**
     * Save the created category in the given directory
     */
    void saveCategory(File aDir){
        if(! aDir.isDirectory() ) return; // Not a directory
        File defDir  = new java.io.File(aDir , "Definitions");
        //File defDir  = new java.io.File(aDir + "\\Definitions");
        defDir.mkdir();
        if(mRoot.isLeaf() ) return;
        GroupNode aChild  = (GroupNode)mRoot.getFirstChild();
        
        while ( aChild != null){
            createGroupFiles(aChild,aDir,"mva");
            createGroupFiles(aChild,defDir,"dxt");
            aChild = (GroupNode)aChild.getNextSibling();
        }
    }
    
    /**
     * Save a given attribute in a mva or a dxt file
     */
    private void createGroupFiles(GroupNode attNode,File aDir,String extention ){
        
        File   aFile    =  new File(aDir, attNode.toString() + "." + extention);
        //String aFileName = aDir.getPath() + "\\" + attNode.toString() + "." + extention;
        String aFileName   = aFile.getName() ;
        
        // PrintWriter outStrmWrtr = null;  //was in use
        OutputStreamWriter  outStrmWrtr = null;
        FileOutputStream    fOutStrm    = null;
        
        try {
            fOutStrm        = new FileOutputStream(aFile);
            outStrmWrtr     = new OutputStreamWriter(fOutStrm,"ISO-8859-1");    // new
            //outStrmWrtr  = new PrintWriter(fOutStrm, true);  // was in use
        }
        catch (Exception e) {
            System.err.println("Couln't open file " + aFileName + e.getMessage());
            return;
        }
        try {
            StringWriter stringOut = new StringWriter();
            saveAttributes(stringOut,attNode,extention);
            //outStrmWrtr.println(stringOut.toString());was in use
            outStrmWrtr.write(stringOut.toString()) ;
            outStrmWrtr.flush();  // new
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            fOutStrm.close();
            outStrmWrtr.close();
            fOutStrm = null;
            outStrmWrtr = null;
        }
        catch (IOException e) {
            System.err.println("IO error -" + e.getMessage());
            return;
        }
    }
    
    private void saveAttributes(StringWriter aStr,GroupNode attNode,String extention){
        if(extention.compareTo("mva") == 0) writeMvaAttribute(aStr,attNode);
        else writeDxtAttribute(aStr,attNode);
        
    }
    
    private void writeMvaAttribute(StringWriter aStr,GroupNode attNode){
        aStr.write("{\n\tDocAttribute = " + attNode.toString() + ";\n" );
        aStr.write("\tDocDefinition = \"/* Category for " + attNode.toString());
        aStr.write(" */\\n");
        
        if(attNode.isLeaf()) return;
        
        GroupNode aGrp  = (GroupNode)attNode.getFirstChild();
        if(aGrp == null) return;
        
        while ( aGrp != null){
            writeMvaDefGroup(aStr,aGrp);
            aGrp = (GroupNode)aGrp.getNextSibling();
        }
        aStr.write("\";\n");
        
        aStr.write("\tDecRecords = (\n");
        if(attNode.isLeaf()) return;
        
        aGrp  = (GroupNode)attNode.getFirstChild();
        while ( aGrp != null){
            writeMvaRecGroup(aStr,aGrp);
            aGrp = (GroupNode)aGrp.getNextSibling();
        }
        // Delete the last , sign.
        StringBuffer aBufStr= aStr.getBuffer();
        int len = aBufStr.length();
        
        aBufStr.replace(len-2,len,"\n\t");
        aStr.write(");\n}");
        
    }
    
    private void writeMvaRecGroup(StringWriter aStr,GroupNode grpNode){
        if(grpNode.isLeaf()) return;  // the group is empty
        GroupNode aValue  = (GroupNode)grpNode.getFirstChild();
        String grpName    =  grpNode.toString();
        while ( aValue != null){
            aStr.write("\t\t{category = " + grpName + ";include = YES; value = ");
            aStr.write("\"" + aValue.toString() + "\";},\n" );
            aValue = (GroupNode)aValue.getNextSibling();
        }
    }
    
    private void writeMvaDefGroup(StringWriter aStr,GroupNode grpNode){
        if(grpNode.isLeaf()) return; // the group is empty
        GroupNode aValue  = (GroupNode)grpNode.getFirstChild();
        String grpName    =  grpNode.toString();
        while ( aValue != null){
            aStr.write("'" + aValue.toString() + "' = '" + grpName + "'.\\n" );
            aValue = (GroupNode)aValue.getNextSibling();
        }
    }
    
    
    
    
    private void writeDxtAttribute(StringWriter aStr,GroupNode attNode){
        aStr.write("/* Category for " + attNode.toString() + " */\n" );
        if(attNode.isLeaf() ) return;
        
        GroupNode aGrp  = (GroupNode)attNode.getFirstChild();
        while ( aGrp != null){
            writeDxtGroup(aStr,aGrp);
            aGrp = (GroupNode)aGrp.getNextSibling();
        }
    }
    
    private void writeDxtGroup(StringWriter aStr,GroupNode grpNode){
        if(grpNode.isLeaf()) return;  // the group is empty
        GroupNode aValue  = (GroupNode)grpNode.getFirstChild();
        String grpName    =  grpNode.toString();
        while ( aValue != null){
            aStr.write("'" + aValue.toString() + "' = '" + grpName + "'.\n" );
            aValue = (GroupNode)aValue.getNextSibling();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}