//
//  ArchetypeSlotAssertionNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-14.
//
//  $Id: ArchetypeSlotAssertionNodeInfo.java,v 1.2 2009/01/03 17:28:02 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import br.com.zilics.archetypes.models.am.archetype.assertion.Assertion;

public abstract class ArchetypeSlotAssertionNodeInfo extends AssertionNodeInfo {

    private Assertion assertion;

    public ArchetypeSlotAssertionNodeInfo(Assertion a) {
        assertion = a;
    }

    public Assertion getAssertion() {
        return assertion;
    }

    @Override
    public String toString() {
        return assertion.toString();
    }
}
