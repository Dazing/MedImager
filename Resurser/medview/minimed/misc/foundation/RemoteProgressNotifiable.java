package misc.foundation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface for remote progress notifying.
 *
 * @author Fredrik Lindahl
 */
public interface RemoteProgressNotifiable extends Remote
{
	public void setCurrent(int c) throws RemoteException;

	public void setDescription(String d) throws RemoteException;

	public void setTotal(int t) throws RemoteException;

	public int getCurrent() throws RemoteException;

	public int getTotal() throws RemoteException;

	public String getDescription() throws RemoteException;

	public void setIndeterminate(boolean indeterminate) throws RemoteException;

	public boolean isIndeterminate() throws RemoteException;
}
