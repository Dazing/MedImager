//
//  ArchetypeSlotNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: ArchetypeSlotNodeInfo.java,v 1.5 2009/01/03 17:30:04 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.ArchetypeSlot;
import br.com.zilics.archetypes.models.rm.support.basic.*;

public class ArchetypeSlotNodeInfo extends CReferenceObjectNodeInfo {

	private ArchetypeSlot aSlot;
	
	public ArchetypeSlotNodeInfo(ArchetypeSlot c) {
		aSlot = c;
	}
	
    @Override
	public String getImageName() {
        String name = "archetype_slot";

        if (isOptional()) {
            name = name + OPTIONAL_PATH_COMPONENT;
        }
		return name + "." + PATH_EXTENSION;
	}
	
    @Override
	public String toString() {
        String display = aSlot.getNodeName(getLanguage());

		if (display == null) {
			display = aSlot.getRmTypeName();
            if (aSlot.getAnyAllowed()) {
                display = display + " = *";
            }
		}
		return display;
	}

    protected Boolean isOptional() {
		Interval<Integer> interval = aSlot.getOccurrences();

		int lower = interval.getLower().intValue();

		//System.out.println("Interval: " + interval.toString() + " " + toString());

		return (lower < 0) || ((lower <= 0) && interval.isLowerIncluded());
	}
}
