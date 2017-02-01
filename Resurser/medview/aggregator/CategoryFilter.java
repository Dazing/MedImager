package medview.aggregator;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class CategoryFilter extends FileFilter implements java.io.FileFilter {

	private  String mCatg = "mvg";

	public CategoryFilter( ) {
		super();
	}

	// Accept all directories and  mvg directorys.
    public boolean accept(File f){
		String extension = getExtension(f);

		if( ! f.isDirectory() ) return false;
		else if (extension == null) return true;
		else if(extension.equals(mCatg))  return true;
		else return false;
    }

    public String getExtention(){
        return mCatg;
    }
	    // The description of this filter
    public String getDescription() {
            return "Mvg (*.mvg)";
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