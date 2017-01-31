package medview.medimager.view;

import java.util.*;

public interface BrowseTreeListener extends EventListener
{
	public abstract void nodeSelectionChanged(BrowseTreeEvent e);
}
