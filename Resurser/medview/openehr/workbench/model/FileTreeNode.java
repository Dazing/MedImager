//
//  FileTreeNode.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-11-20.
//
//  $Id: FileTreeNode.java,v 1.3 2008/12/26 23:23:30 oloft Exp $
//
package medview.openehr.workbench.model;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileTreeNode extends DefaultMutableTreeNode {
	
	private static final int NOT_PARSED = 0;
	private static final int PARSE_ERROR = 1;
	private static final int PARSED = 2;
	private static final int VALIDATED = 3;

	private int state;
	
	public FileTreeNode(Object userObject) { 
		super(userObject); 
		state = NOT_PARSED;
		}
	
	public void setStateNoteParsed() {
		state = NOT_PARSED;
	}
	
	public void setStateParseError() {
		state = PARSE_ERROR;
	}
	
	public void setStateParsed() {
		state = PARSED;
	}
	
	public void setStateValidated() {
		state = VALIDATED;
	}
	
	public String getImageName() {
		String name = "archetype_2.gif";
		switch (state) {
			case PARSE_ERROR:
				name = "archetype_parse_failed_2.gif";
				break;
			case PARSED:
				name = "archetype_parsed_2.gif";
				break;
			case VALIDATED:
				name = "archetype_valid_2.gif";
				break;
			default:
				break;
		}
		return name;
	}
	
	public String toString()
	{
		int dotIndex;
		File userObject = (File) getUserObject();
		
		String name = userObject.getName();
		
		if (name.endsWith(ArchetypeUtilities.ADL_EXTENSION)) {
			dotIndex = name.indexOf(".");
			
			if (dotIndex != -1) {
				name = name.substring(dotIndex+1);
			}
		}
		
		dotIndex = name.lastIndexOf(".");
		dotIndex = (dotIndex >= 0 ? dotIndex : name.length());
		name = name.substring(0,dotIndex);
		
		return capitalize(name);
	}
	
	private  String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
