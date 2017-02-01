/**
 *
 * $Id: CategoryReader.java,v 1.4 2005/08/26 18:51:35 erichson Exp $
 *
 * $Log: CategoryReader.java,v $
 * Revision 1.4  2005/08/26 18:51:35  erichson
 * Bugfix to avoid NullPointerException if trying to load non-existent file. (Now throws IOException instead). // NE
 *
 *
 */

package medview.aggregator;

import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 *
 * $Id: CategoryReader.java,v 1.4 2005/08/26 18:51:35 erichson Exp $
 *
 * $Log: CategoryReader.java,v $
 * Revision 1.4  2005/08/26 18:51:35  erichson
 * Bugfix to avoid NullPointerException if trying to load non-existent file. (Now throws IOException instead). // NE
 *
 * Revision 1.3  2002/10/09 14:41:18  erichson
 * Restored panel handling functionality, added some documentation
 *
 */

/**
 * A class to read Category data from an MVG file to a GroupTreeUI component
 */

public class CategoryReader 
{
    File        mDirectory;
    String	    mCategory;
    // Vector      mMvaFiles;
    
    
    // GroupTreeUI mGroupTree;
    
    static String       ConstRecordsSign    = "DecRecords = (";
    static String       ConsEndRecordSign   = ");";
    static String       ConsEndValueSign    = "\";}";
    static String       ConsGroupSign       = "{category = ";
    static String       ConsValueSign       = ";include = YES; value = \"";
    
    public CategoryReader() {
    }
    
    //public GroupTreeUI getCategory(){
    //	return mGroupTree;
    //}
    
    /**
     * Read a category from a a file, AND put the resulting component in aPanel
     * @return a Group-Tree UI component containing the category
     * @deprecated hard-coding use of GUI components in a constructor is bad coding style. It's better to use {@link #readCategory(File)} and {@link #setPane(JScrollPane)}
     */
    public GroupTreeUI readCategory(File dirFile, JScrollPane aPanel) throws IOException {
        GroupTreeUI treeUI = readCategory(dirFile); 
        treeUI.setPane(aPanel);
        return treeUI;
    }
    
    /**
     * Read a category from an MVG file
     * @return a Group-Tree UI component containing the category
     */
    public GroupTreeUI readCategory(File dirFile) throws IOException 
    {
        // System.out.println("Trying readCategory of file " + dirFile.getPath()); // TODO: Remove (debug)
        
        String aDirName     = dirFile.getName();
        mDirectory          = dirFile;
        int indx            = aDirName.indexOf('.');
        mCategory           = aDirName.substring(0,indx);                
        
        try {
            File[] files = listFiles();
            GroupTreeUI ui = readFiles(files);
            return ui;
        } 
        catch (IOException ioe)
        {
            throw new IOException("Failed to read aggregation " + dirFile.getPath() + " - reason: " + ioe.getMessage());
        }
    }
    
    private GroupTreeUI readFiles(File[] files) throws IOException 
    {
        if (files == null)
        {
            throw new IOException("readFiles(File[]): No files to read! (null array)");
        }
        
        GroupTreeUI mGroupTree           = new GroupTreeUI(mCategory); // new GroupTreeUI(aPanel,mCategory);
        // listFiles();
        //ListIterator aList	= mMvaFiles.listIterator();
        ListIterator aList = Arrays.asList(files).listIterator();
        while (aList.hasNext())
        {
            File aFile = (File)aList.next();
            readAttribute(aFile, mGroupTree);
        }
        
        return mGroupTree;
        
    }
    
    private File[] listFiles() {
        AggregateFileFilter aFileFilter = new AggregateFileFilter("mva");
        aFileFilter.setDescription("Mva (*.mva)");
        aFileFilter.setIsDirectory(false);
        
        File[] files = mDirectory.listFiles(aFileFilter);
        
        return files;
    }
    
    private void readAttribute(File aFile, GroupTreeUI mGroupTree) throws IOException {
        String      aName       = aFile.getName();
        //Ut.prt("read file mva = " + aName);
        int         index       = aName.indexOf('.') ;
        String      attName     = aName.substring(0,index);
        
        //Ut.prt("attrib  = " + attName);
        mGroupTree.addAttrib(attName);
        
        Vector      lines       = getGroups(aFile);
        if(lines == null) return;
        
        Enumeration anEnum      = lines.elements();
        while (anEnum.hasMoreElements()){
            String aLine = (String)anEnum.nextElement();
            String aGrp  = getGroup(aLine);
            String aVal  = getValue(aLine);
            mGroupTree.addGroup(attName,aGrp) ;
            mGroupTree.addValue(attName,aGrp,aVal);
        }
    }
    
    private Vector getGroups(File aFile) throws IOException {
        Vector  lines = readFileToVector(aFile) ;
        int     indx  = 0;
        boolean found = false;
        
        if(lines == null || lines.isEmpty() ) return null;
        
        //Ut.prt("Lines = " + lines.size());
        
        while ( indx < lines.size() && !found){
            String aLine = (String)lines.get(indx++);
            if(aLine == null) return null;
            if(aLine.indexOf(ConstRecordsSign) >= 0)
                found = true;
        }
        if(! found	) return null;
        //Ut.prt("founf= " + indx);
        Vector groups = new Vector();
        for (int i = indx; i < lines.size(); i++){
            String aLine = (String) lines.get(i);
            if(aLine.indexOf(ConsEndRecordSign) >= 0 )
                break;
            
            groups.add(aLine);
        }
        if(!groups.isEmpty()) return groups ;
        return null;
    }
    
    private String getGroup(String aLine){
        int indx = aLine.indexOf(ConsGroupSign) ;
        if (indx < 0) return  null;
        
        int indxS = indx + ConsGroupSign.length();
        int indxE = aLine.indexOf(ConsValueSign) ;
        String grp = aLine.substring(indxS,indxE);
        
        return grp;
    }
    private String getValue(String aLine){
        int indx = aLine.indexOf(ConsValueSign) ;
        if (indx < 0) return null;
        
        int indxS = indx + ConsValueSign.length();
        int indxE = aLine.indexOf(ConsEndValueSign);// -1 ;
        String value = aLine.substring(indxS,indxE);
        return value;
    }
    
    
    private Vector readFileToVector(File aFile) throws IOException {
        Vector lines = new Vector();
        
        FileInputStream fis     = null;
        InputStreamReader isr   = null;
        BufferedReader reader   = null;
        
        
            fis = new FileInputStream(aFile);
            isr = new InputStreamReader(fis,"ISO-8859-1");
            reader = new BufferedReader(isr);
        
        
            while (reader.ready())
                lines.add(reader.readLine());
                
        return lines;
    }
}