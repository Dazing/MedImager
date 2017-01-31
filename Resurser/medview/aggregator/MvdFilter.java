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

/**
  * A filter for recognizing the '.forest' file extension.
  * Utility class for the open/save dialogs.
*/
public class MvdFilter extends FileFilter{

	private  String mExtention = "mvd";

    public MvdFilter( ) {
		super();
    }

	// Accept all directories and  forest directorys.
    public boolean accept(File f) {
		String extension = getExtension(f);
		if(! f.isDirectory() ) return false;
		if (extension == null) return true;
		else if(extension.equals(mExtention) ) return true;
		else return false;

    }
    public String getExtention(){
        return mExtention;
    }
	    // The description of this filter
    public String getDescription() {
            return "Mvd (*.mvd)";
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
