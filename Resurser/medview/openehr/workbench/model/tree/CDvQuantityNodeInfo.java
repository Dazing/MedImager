//
//  CDvQuantityNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CDvQuantityNodeInfo.java,v 1.7 2008/12/30 22:32:38 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.quantity.CDvQuantity;
import br.com.zilics.archetypes.models.rm.datatypes.text.*;

public class CDvQuantityNodeInfo extends CDomainTypeNodeInfo {

    private CDvQuantity cDvQ;

    public CDvQuantityNodeInfo(CDvQuantity c) {
        cDvQ = c;
    }

    @Override
    public String getImageName() {
        return "c_dv_quantity.gif";
    }

    @Override
    public String toString() {

        String display = cDvQ.getNodeName(getLanguage());

        //System.out.println(">>> CDvQuantityNodeInfo display: " + display);
        // System.out.println(display != null? "Not null" : "Null");

        if (display == null) {
            CodePhrase cph = cDvQ.getProperty();

            //System.out.println("cph: " + cph.toString());
            
            if (cph != null) {
                String displayName = cph.getTerminologyId().name();
                String displayCode = cph.getCodeString();

                //System.out.println(displayName+"::"+displayCode);
                        //cDvQ.getProperty().getProperty().getCodeString();

                displayName = (displayName != null ? displayName : "");
                displayCode = (displayCode != null ? displayCode : "");

                display = "(" + displayName + "::" + displayCode + ")";

            // String occurs = cDvQ.getOccurrences().toString();
            }
            if (display == null) {
                display = cDvQ.getRmTypeName();
                return display;
            }

            //return "?!? " + display + " (CO, " + occurs + ")";
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cDvQ.getRmTypeName() + " " + display;
        }
            return display;
        }
        return "" /*cDvQ.toString()*/;
    }
}
