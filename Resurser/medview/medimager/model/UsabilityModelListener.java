package medview.medimager.model;

import java.util.*;

public interface UsabilityModelListener extends EventListener
{
	void functionalLayerStateChanged(UsabilityModelEvent e);
}
