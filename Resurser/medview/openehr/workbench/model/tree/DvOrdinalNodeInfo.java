//
//  DvOrdinalNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-14.
//
//  $Id: DvOrdinalNodeInfo.java,v 1.7 2008/12/30 20:48:22 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.rm.datatypes.quantity.DvOrdinal;
import br.com.zilics.archetypes.models.rm.datatypes.text.*;
import br.com.zilics.archetypes.models.am.archetype.Archetype;
import medview.openehr.workbench.model.*;

public class DvOrdinalNodeInfo extends ArchetypeNodeInfo {

    private DvOrdinal dvOrdinal;
    private Archetype archetype;
    private Boolean assumed;
    
     public DvOrdinalNodeInfo(DvOrdinal dvo) {

        dvOrdinal = dvo;
        archetype = null;
        assumed = false;
    }

    public DvOrdinalNodeInfo(DvOrdinal dvo, Archetype a) {

        dvOrdinal = dvo;
        archetype = a;
        assumed = false;
    }

     public DvOrdinalNodeInfo(DvOrdinal dvo, Archetype a, Boolean b) {

        dvOrdinal = dvo;
        archetype = a;
        assumed = b;
    }

    @Override
    public String getImageName() {
        return "ordinal.gif";
    }

    @Override
    public String toString() {
        Integer value = dvOrdinal.getValue();
        DvCodedText codedText = dvOrdinal.getSymbol();
        String symbol = codedText.getDefiningCode().getCodeString();
        String extra = " (Assumed)";
        String code = symbol;
        
        if (archetype != null) {
            String text = ArchetypeUtilities.getTextValue(archetype, code, getLanguage());
            if (text != null) {
                symbol = text;
            }
        }
        String display = value.toString() + " | " + symbol + (assumed ? extra : "");
        if (getDisplayMode()==DisplayMode.TECHNICAL && code != null) {
            display = display + " -- " + code;
        }
        return display;
    }
}
