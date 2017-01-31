//
//  CMultipleAttributeNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CMultipleAttributeNodeInfo.java,v 1.3 2008/12/14 17:05:17 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.CMultipleAttribute;
import br.com.zilics.archetypes.models.rm.support.basic.*;

public class CMultipleAttributeNodeInfo extends CAttributeNodeInfo {

	private static String IMAGE_BASE_NAME = "c_attribute_multiple";
	
	public CMultipleAttributeNodeInfo(CMultipleAttribute c) {
		setNodeInfo(c);
	}

	public String getImageName() {
		return getImageName(IMAGE_BASE_NAME, isOptional(), false);
	}
	
	public String toString() {
            
            String card = ((CMultipleAttribute)getNodeInfo()).getCardinality().toString();

            card = " (CA, " + card + ")";
			
					//System.out.println(isOptional());

			//return "!" + getNodeInfo().getRmAttributeName()+ card;
            return getNodeInfo().getRmAttributeName();
	}

protected Boolean isOptional() {
		Interval<Integer> interval = ((CMultipleAttribute)getNodeInfo()).getCardinality().getInterval();

		int lower = interval.getLower().intValue();

		//System.out.println("Interval: " + interval.toString());

		return interval.has(new Integer(0)); //lower < 0) || ((lower <= 0) && interval.isLowerIncluded());
	}
}
