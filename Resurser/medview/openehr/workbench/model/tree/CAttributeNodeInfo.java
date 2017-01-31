//
//  CAttributeNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CAttributeNodeInfo.java,v 1.3 2008/12/14 17:03:19 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.CAttribute;
import br.com.zilics.archetypes.models.rm.support.basic.*;

public class CAttributeNodeInfo extends ArchetypeNodeInfo {

	private CAttribute cAttribute;
	
	public String getImageName() {
		return "c_attribute.gif";
	}

	public String getPath(){

        return cAttribute.getCanonicalPath();
    }
    
	public CAttribute getNodeInfo() {
		return cAttribute;
	}
	
	public void setNodeInfo(CAttribute c) {
		cAttribute = c;
	}
	
	protected Boolean isOptional() {
		Interval<Integer> interval = cAttribute.getExistence();
		
		int lower = interval.getLower().intValue();
		
		//System.out.println("Interval: " + interval.toString());
		
		return (lower < 0) || ((lower <= 0) && interval.isLowerIncluded());
	}

}
