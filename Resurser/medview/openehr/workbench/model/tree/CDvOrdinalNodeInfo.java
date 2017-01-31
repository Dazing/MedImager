//
//  CDvOrdinalNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CDvOrdinalNodeInfo.java,v 1.5 2008/12/28 16:18:10 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.quantity.CDvOrdinal;

public class CDvOrdinalNodeInfo extends CDomainTypeNodeInfo {

	private CDvOrdinal cDVO;
	
	public CDvOrdinalNodeInfo(CDvOrdinal c) {
		cDVO = c;
	}
	
    @Override
	public String getImageName() {
		return "c_dv_ordinal.gif";
	}
	
    @Override
	public String toString() {
		
		String display = cDVO.getNodeName(getLanguage());

        // String occurs = cDVO.getOccurrences().toString();

		if (display == null) {
			display = cDVO.getRmTypeName();
		}

        // return "! " + display + " (CO, " + occurs + ")";
		 return display;
	}

}
