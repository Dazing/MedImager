/**
 * @(#) DataComponentPackagePanelListener.java
 */

package medview.common.components.datapackage;

import java.util.*;

public interface DataComponentPackagePanelListener extends EventListener
{
	void includedPackageAdded( DataComponentPackagePanelEvent event );
	
	void includedPackageRemoved( DataComponentPackagePanelEvent event );
	
	void includedPackageNewDefault( DataComponentPackagePanelEvent event );
	
	void globalPackageAdded( DataComponentPackagePanelEvent event );
	
	void globalPackageRemoved( DataComponentPackagePanelEvent event );
	
	void globalPackageEdited( DataComponentPackagePanelEvent event );
}
