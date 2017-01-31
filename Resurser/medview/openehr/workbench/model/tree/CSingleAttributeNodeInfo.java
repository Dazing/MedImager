//
//  CSingleAttributeNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CSingleAttributeNodeInfo.java,v 1.5 2009/01/03 17:43:17 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.*;

public class CSingleAttributeNodeInfo extends CAttributeNodeInfo {

    private static String IMAGE_BASE_NAME = "c_attribute";

    //private CSingleAttribute cAttribute;
    public CSingleAttributeNodeInfo(CSingleAttribute c) {
        setNodeInfo(c);
    }

    @Override
    public String getImageName() {
        return getImageName(IMAGE_BASE_NAME, isOptional(), false);
    }

    @Override
    public String toString() {

        // String card = getNodeInfo().getExistence().toString();
        // card = " (CA, " + card + ")";

        // System.out.println(isOptional());

        //return "!" + getNodeInfo().getRmAttributeName()+ card;
        return getNodeInfo().getRmAttributeName();
    }
}
