//
//  WorkbenchModelListener.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-20.
//
//  $Id: WorkbenchModelListener.java,v 1.3 2009/01/05 11:59:25 oloft Exp $
//

package medview.openehr.workbench.model;

import java.util.*;

public interface WorkbenchModelListener extends EventListener {

	public void archetypesLocationChanged(WorkbenchModelEvent e);
	
	public void templatesLocationChanged(WorkbenchModelEvent e);
	
	public void displayModeChanged(WorkbenchModelEvent e);

}
