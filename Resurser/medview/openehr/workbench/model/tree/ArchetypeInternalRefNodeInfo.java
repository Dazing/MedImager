//
//  ArchetypeInternalRefNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: ArchetypeInternalRefNodeInfo.java,v 1.5 2009/01/03 17:26:32 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.ArchetypeInternalRef;

import medview.datahandling.*;

public class ArchetypeInternalRefNodeInfo extends CReferenceObjectNodeInfo {

    private ArchetypeInternalRef aiRef;

    public ArchetypeInternalRefNodeInfo(ArchetypeInternalRef c) {
        aiRef = c;
    }

    @Override
    public String getImageName() {
        return "archetype_internal_ref.gif";
    }

    @Override
    public String toString() {
        return MedViewDataHandler.instance().getLanguageString(MedViewLanguageConstants.OTHER_OEHR_USE_LS_PROPERTY) + " " + aiRef.getTargetPath();
    // return "!* " + aiRef.toString();
    }
}
