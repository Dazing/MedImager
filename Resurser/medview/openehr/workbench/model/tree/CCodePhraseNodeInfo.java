//
//  CCodePhraseNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CCodePhraseNodeInfo.java,v 1.5 2009/01/03 17:34:55 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.text.CCodePhrase;

public class CCodePhraseNodeInfo extends CDomainTypeNodeInfo {

	private CCodePhrase cCPhrase;
	
	public CCodePhraseNodeInfo(CCodePhrase c) {
		cCPhrase = c;
	}
	
    @Override
	public String getImageName() {
		return "c_code_phrase.gif";
	}

    @Override
	public String toString() {
		
		// String display = cCPhrase.getNodeName("en");
        String display = cCPhrase.getTerminologyId().name();
        String occurs = cCPhrase.getOccurrences().toString();

		if (display == null) {
			display = cCPhrase.getRmTypeName();
		}

        // return "! " + display + " (CO, " + occurs + ")";
		return display;
	}

}
