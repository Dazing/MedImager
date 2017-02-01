package medview.summarycreator.model;

import java.util.*;

public interface SummaryCreatorModelListener extends EventListener
{
	public void termListingChanged(SummaryCreatorModelEvent e);
}