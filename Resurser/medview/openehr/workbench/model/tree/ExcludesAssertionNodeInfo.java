//
//  ExcludesAssertionNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-04.
//
//  $Id: ExcludesAssertionNodeInfo.java,v 1.2 2008/12/15 20:08:25 oloft Exp $
//

package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.assertion.Assertion;

public class ExcludesAssertionNodeInfo extends ArchetypeSlotAssertionNodeInfo {

    public ExcludesAssertionNodeInfo(Assertion a) {
        super(a);
    }

    @Override
    public String getImageName() {
        return "cadl_exclude.gif";
    }


}