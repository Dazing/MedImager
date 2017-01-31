//
//  CComplexObjectNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CComplexObjectNodeInfo.java,v 1.7 2009/01/03 17:35:44 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.CComplexObject;
import br.com.zilics.archetypes.models.rm.support.basic.*;

public class CComplexObjectNodeInfo extends CObjectNodeInfo {

    private CComplexObject ccObj;

    public CComplexObjectNodeInfo(CComplexObject c) {
        ccObj = c;
    }

    @Override
    public String getImageName() {
        return getImageName("c_complex_object", isOptional(), isMultiple());
    }

    @Override
    public String getPath() {
        return ccObj.getCanonicalPath();
    }

    @Override
    public String toString() {

        String display = ccObj.getNodeName(getLanguage());

        if (display != null && getDisplayMode() == DisplayMode.TECHNICAL) {
            display = ccObj.getRmTypeName() + " " + display + " [" + ccObj.getNodeId() + "]";
        }
        if (display == null) {
            display = ccObj.getRmTypeName();
            if (ccObj.getAnyAllowed()) {
                display = display + " = *";
            }
        }

        //return "! " + display + " (CO, " + occurs + ")";
        return display;

    }

    protected Boolean isOptional() {
        Interval<Integer> interval = ccObj.getOccurrences();

        int lower = interval.getLower().intValue();

        //System.out.println("Interval: " + interval.toString() + " " + toString());

        return (lower < 0) || ((lower <= 0) && interval.isLowerIncluded());
    }

    protected Boolean isMultiple() {
        Interval<Integer> interval = ccObj.getOccurrences();

        Integer upperObj = interval.getUpper();
        int upper;

        if (upperObj != null) {

            upper = upperObj.intValue();

        } else {
            return true;
        }

        //System.out.println("Upper: " + upper);
        //System.out.println("Interval: " + interval.toString());

        return (upper > 1); // saknas special fall för upper = 2 och not included [0,2[ typ
    }
}
