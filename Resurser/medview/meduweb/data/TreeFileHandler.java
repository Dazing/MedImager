/*
 * @(#)TreeFileHandler.java
 *
 * $Id: TreeFileHandler.java,v 1.4 2004/11/11 22:36:54 lindahlf Exp $
 *
 */

package medview.meduweb.data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import misc.foundation.*; 
public class TreeFileHandler extends medview.datahandling.examination.tree.TreeFileHandler {

    // Detta är skumt, ska det vara string istället?
	public TreeFileHandler(Object o){
		super((String)o);
	}
	
	private String treeFilePath = "";
	
    protected File[] listTreeFiles() throws IOException {

        Vector treeFiles = new Vector();

        File[] files = (new File(treeFilePath)).listFiles(treeFileFilter);

        if (files == null)
            throw new IOException("I/O error, or " + treeFilePath + "is not a directory!");
        return files;
    }	
	
   public String getExaminationDataLocation() {

        if (treeFilePath == null) {
            return "";
        }
        else {
            File treeDir = new File(treeFilePath);

            if (treeDir.exists() && treeDir.isDirectory()) {
                return treeDir.getPath();
            }
            else {
                return "";
            }
        }
    }
    
    
}
