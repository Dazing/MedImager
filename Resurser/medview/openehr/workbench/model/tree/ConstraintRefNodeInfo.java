//
//  ConstraintRefNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: ConstraintRefNodeInfo.java,v 1.4 2008/12/28 14:57:23 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.ConstraintRef;

import medview.openehr.workbench.model.ArchetypeUtilities;

public class ConstraintRefNodeInfo extends CObjectNodeInfo {

    private ConstraintRef cRef;

    public ConstraintRefNodeInfo(ConstraintRef c) {
        cRef = c;
    }

    @Override
    public String toString() {
        String display = cRef.getReference();

        if (display != null) {
            String text = ArchetypeUtilities.getConstraintTextValue(cRef.getOwnerArchetype(), display, getLanguage());

            return (text != null ? text : "cRef: " + display);
        }
        return cRef.getRmTypeName();
    }

    public String getImageName() {
        return "archetype_code_ref.gif";
    }
}
