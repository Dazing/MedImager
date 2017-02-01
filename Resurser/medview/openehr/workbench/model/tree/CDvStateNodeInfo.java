//
//  CDvStateNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CDvStateNodeInfo.java,v 1.2 2008/12/27 23:38:34 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.basic.CDvState;

public class CDvStateNodeInfo extends CDomainTypeNodeInfo {

	private CDvState cDvState;
	
	public CDvStateNodeInfo(CDvState c) {
		cDvState = c;
	}
	
	public String toString() {
		
		String display = cDvState.getNodeName(getLanguage());

        String occurs = cDvState.getOccurrences().toString();

		if (display == null) {
			display = cDvState.getRmTypeName();
		}

        return "! " + display + " (CO, " + occurs + ")";
		
	}

}
