package medview.medserver.data;

import medview.datahandling.*;

public interface RemoteTermDataHandlerImplListener
{
	public void termAdded(RemoteTermDataHandlerEvent e);
	
	public void termRemoved(RemoteTermDataHandlerEvent e);
	
	public void valueAdded(RemoteTermDataHandlerEvent e);
	
	public void valueRemoved(RemoteTermDataHandlerEvent e);
	
	public void termHashMapRequested(RemoteTermDataHandlerEvent e);
	
	public void termListRequested(RemoteTermDataHandlerEvent e);

	public void typeRequested(RemoteTermDataHandlerEvent e);

	public void valuesRequested(RemoteTermDataHandlerEvent e);
}
