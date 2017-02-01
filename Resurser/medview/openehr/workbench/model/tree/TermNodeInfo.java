//
//  TermNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-13.
//
//  $Id: TermNodeInfo.java,v 1.7 2009/01/03 17:50:23 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.Archetype;

import medview.datahandling.*;

import medview.openehr.workbench.model.ArchetypeUtilities;

public class TermNodeInfo extends ArchetypeNodeInfo {

    private String term;
    private Archetype archetype;

    public TermNodeInfo(String trm, Archetype a) {
        term = trm;
        archetype = a;
    }

    @Override
    public String getImageName() {

        return "term.gif";
    }

    @Override
    public String toString() {
        String text = ArchetypeUtilities.getTextValue(archetype, term, getLanguage());
        String display = (text != null ? text : "(" + MedViewDataHandler.instance().getLanguageString(MedViewLanguageConstants.OTHER_OEHR_RUBRIC_FOR_LS_PROPERTY) + " " + term + ")");
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = display + " -- " + term;
        }
        return display;
    }
}
