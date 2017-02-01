//
//  ArchetypeNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: ArchetypeNodeInfo.java,v 1.7 2008/12/31 16:27:26 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import medview.openehr.workbench.model.ArchetypeUtilities;

public class ArchetypeNodeInfo {
	
	public final static String OPTIONAL_PATH_COMPONENT = "_optional";
	
	public final static String MULTIPLE_PATH_COMPONENT = "_multiple";
	
	public final static String PATH_EXTENSION = "gif";
	
	public enum DisplayMode {DOMAIN, TECHNICAL}
	
	private String lang = ArchetypeUtilities.DEFAULT_LANGUAGE;
	
	private DisplayMode displayMode = DisplayMode.DOMAIN;
	
	public String getLanguage() {
		return lang;
	}
	
	public void setLanguage(String l) {
		lang = l;
	}
	
	public DisplayMode getDisplayMode() {
		return displayMode;
	}
	
	public void setDisplayMode(DisplayMode dm) {
		displayMode = dm;
	}
	
	public String getImageName() {
		return null;
	}
	
    public String getPath(){
        return null;
    }
    
	protected String getImageName(String name, Boolean isOptional, Boolean isMultiple) {
		String result = name;
		
		if (isMultiple) {
			result = result+MULTIPLE_PATH_COMPONENT;
		}
		if (isOptional) {
			result = result+OPTIONAL_PATH_COMPONENT;
		}
		return result+"."+PATH_EXTENSION;
	}
}
