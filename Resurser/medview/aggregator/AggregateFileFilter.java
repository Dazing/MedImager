package medview.aggregator;

import javax.swing.filechooser.*;
import java.io.File;
//import javax.swing.*;



/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class AggregateFileFilter extends FileFilter implements java.io.FileFilter {
	private String  mExtention ;
	private boolean mIsDirectory;
	private String  mDescription;

    public AggregateFileFilter(String extention) {
		mExtention = extention;
    }
	public void setIsDirectory(boolean isDir){
		mIsDirectory = isDir;
	}
    public boolean accept(File f) {
		if(mIsDirectory ){
			String extension = getExtension(f);

			if(! f.isDirectory() ) return false;
			else if(extension == null) return false;
			else if(extension.equals(mExtention))  return true;
            else return false;
		}
		else{
			String extension = getExtension(f);
			if(extension == null) return false;

			//if(f.isDirectory() ) return true;
			if(extension.equals(mExtention))  return true;
			else return false;
		}
    }

    public String getExtention(){
        return mExtention ;
    }
	public void setDescription(String str){
	    mDescription = str;
	}
	    // The description of this filter
    public String getDescription() {
		return mDescription;
    }
	 //Get the extension of a file.
    private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                    ext = s.substring(i+1).toLowerCase();
            }
            return ext;
    }
}






