//
//  CQuantityItemNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-13.
//
//  $Id: CQuantityItemNodeInfo.java,v 1.3 2009/01/03 17:39:56 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.quantity.CQuantityItem;
import br.com.zilics.archetypes.models.rm.support.basic.Interval;

public class CQuantityItemNodeInfo extends ArchetypeNodeInfo {

    private CQuantityItem cQItem;

    public CQuantityItemNodeInfo(CQuantityItem cqi) {

        cQItem = cqi;
    }

    @Override
    public String getImageName() {
        return "c_quantity_item.gif";
    }

    @Override
    public String toString() {
        String mString = null;
        Interval<Double> magnitude = cQItem.getMagnitude();

        if (magnitude != null) {
            mString = ": " + magnitude.toString();
        }
        return cQItem.getUnits() + (mString != null ? mString : "");
    }
}
