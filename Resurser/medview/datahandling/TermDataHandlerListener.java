package medview.datahandling;

import java.util.*;

public interface TermDataHandlerListener extends EventListener
{
	public abstract void termAdded(TermDataHandlerEvent e);

	public abstract void termRemoved(TermDataHandlerEvent e);

	public abstract void valueAdded(TermDataHandlerEvent e);

	public abstract void valueRemoved(TermDataHandlerEvent e);

	public abstract void termDefinitionLocationChanged(TermDataHandlerEvent e);

	public abstract void termValueLocationChanged(TermDataHandlerEvent e);
}