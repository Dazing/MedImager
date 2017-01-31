/**
 *
 * $Id: DirectoryFileView.java,v 1.2 2003/07/01 17:28:45 erichson Exp $ 
 * 
 * $Log: DirectoryFileView.java,v $
 * Revision 1.2  2003/07/01 17:28:45  erichson
 * Does not reload Tree icon every time now.
 *
 */

package medview.aggregator;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.io.File;
//import java.util.Hashtable;


/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class DirectoryFileView extends FileView {
	private String[] mExtentions;
        
        private static final Icon treeIcon = (Icon)new ImageIcon(AggregatorFrame.class.getResource("tree.gif"));
        
    public DirectoryFileView(String[] exs) {
		super();
		mExtentions = exs;
    }
	 public DirectoryFileView(String exs) {
	    super();
		mExtentions = new String[1];
		mExtentions[0] = exs;

    }
	public Boolean isTraversable(File f){
		if(! f.isDirectory()) return Boolean.FALSE;

		String ext = getExtension(f);
		if(ext == null) return Boolean.TRUE ;

		if(isFiltered(f)) return Boolean.FALSE;
		else return   Boolean.TRUE;
	}

	public Boolean isHidden(File f) {
	    String name = f.getName();
	    if(name != null && !name.equals("") && name.charAt(0) == '.') {
	        return Boolean.TRUE;
	    }
		else {
	        return Boolean.FALSE;
	    }
    }
	public Icon getIcon(File f) {
		Icon icon = null;
		String extension = getExtension(f);
		if(isFiltered(f)){
			icon = treeIcon;
			//icon = (Icon)new ImageIcon("group.gif");
		}
		return icon;
    }

	public String getExtension(File f) {
		String name = f.getName();
		if(name != null) {
			int extensionIndex = name.lastIndexOf('.');
			if(extensionIndex < 0) {
			    return null;
			}
			return name.substring(extensionIndex+1).toLowerCase();
		}
		return null;
    }

	boolean isFiltered(File f){
		String ext = getExtension(f);
		if (ext == null) return false;
		for (int i = 0 ; i < mExtentions.length; i++){
			if(ext.compareTo(mExtentions[i]) == 0 ) return true ;
		}
		return false;
	}
}