
/**
 *
 * $Id: TreesReader.java,v 1.3 2004/02/20 14:24:04 erichson Exp $
 *
 * $Log: TreesReader.java,v $
 * Revision 1.3  2004/02/20 14:24:04  erichson
 * Changed tree reading to just skip the invalid tree file, not terminate the entire loading if one tree file is invalid
 *
 * Revision 1.2  2003/06/24 17:11:51  erichson
 * Added progress handling // NE
 *
 *
 */

package medview.aggregator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import misc.event.*; // ProgressListener etc
//import medview.data.TreeFileHandler;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class TreesReader {
    
    private String          mTreeDirPath;
    private File            mDirectory;
    private int             mCountFiles;
    private java.util.Vector          progressListeners;
        
    public TreesReader(File adir) {        
        mDirectory  = adir ;
        mCountFiles = 0;
        progressListeners = new java.util.Vector();
    }
    
    public TreesReader(String dirPath) {
        this(new File(dirPath));        
    }
    
    
    private File[] listFiles() {
        AggregateFileFilter aFileFilter = new AggregateFileFilter("tree");
        aFileFilter.setDescription("Tree (*.tree)");
        aFileFilter.setIsDirectory(false);
        
        File[] files = mDirectory.listFiles(aFileFilter);
        //mCountFiles  = files.length;
        return files;
    }
    
    AggregateHashTable  readTreesToHash(){
        //TreeFileHandler aTf = new TreeFileHandler(mTreeDirPath);
        //File[]  files       = aTf.listTreeFiles();
        
        File[]  files         = listFiles();
        if(files.length == 0) return null;
        
        File    aTreeFile; //       = files[0];
        Node    aTree; //          = EncodeTreeFile.makeTree(aTreeFile);
        
        /*if (aTree == null){
            Ut.prt("Error in making tree " + aTreeFile.getName() );
            return null;
        }*/
        AggregateHashTable hsTable =  new AggregateHashTable(); // (aTree);
        
        for(int i = 0; i < files.length; i++){
            aTreeFile   = files[i];
            Node aTr = EncodeTreeFile.makeTree(aTreeFile);
            
            fireProgressMade(0,i+1,files.length); // Update progress listeners
            
            if(aTr == null) {
                Ut.prt("Error in making tree " + aTreeFile.getName() );
            } else {
                hsTable.addTree(aTr);
                mCountFiles++;
            }
        }
        
        return hsTable;
    }
    
    // Tell progress listeners to update
    private void fireProgressMade(int min, int curr, int max) {
        
        ProgressEvent progressEvent = new ProgressEvent("Reading", "", this, min, curr, max);
        
        for(java.util.Iterator it = progressListeners.iterator(); it.hasNext();) {
            ProgressListener pl = (ProgressListener) it.next();
            pl.progressMade(progressEvent);
        }
    }
    
    public void addProgressListener(ProgressListener pl) {
        progressListeners.add(pl);
    }
    
    public void removeProgressListener(ProgressListener pl) {
        progressListeners.remove(pl);
    }
    
    int getCountFiles(){
        return mCountFiles;
    }
    
}
