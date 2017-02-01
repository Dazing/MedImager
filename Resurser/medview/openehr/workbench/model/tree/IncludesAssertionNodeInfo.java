//
//  IncludesAssertionNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-14.
//
//  $Id: IncludesAssertionNodeInfo.java,v 1.3 2008/12/15 20:08:59 oloft Exp $
//

package medview.openehr.workbench.model.tree;
import br.com.zilics.archetypes.models.am.archetype.assertion.Assertion;

/**
 *
 * @author Olof Torgersson
 */
public class IncludesAssertionNodeInfo extends ArchetypeSlotAssertionNodeInfo {

    public IncludesAssertionNodeInfo(Assertion a) {
        super(a);
    }

    @Override
    public String getImageName() {
        return "cadl_include.gif";
    }


}
