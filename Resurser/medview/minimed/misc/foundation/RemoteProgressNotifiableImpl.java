package misc.foundation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A simple implementation of the RemoteProgressNotifiable
 * interface that should be enough for all uses of this
 * notification technique over RMI. All updates to the local
 * notifiable are done on a separate thread, so that the
 * server is not delayed by the notification procedure.
 *
 * @author Fredrik Lindahl
 */
public class RemoteProgressNotifiableImpl extends UnicastRemoteObject
	implements RemoteProgressNotifiable
{

// ------------------------------------------------------
// ******** REMOTE PROGRESS NOTIFIABLE INTERFACE ********
// ------------------------------------------------------

	public void setCurrent(final int c) throws RemoteException
	{
		Thread a = new Thread(new Runnable()
		{
			public void run() { localNotifiable.setCurrent(c); }
		});

		a.start();
	}

	public void setDescription(final String d) throws RemoteException
	{
		Thread a = new Thread(new Runnable()
		{
			public void run() { localNotifiable.setDescription(d); }
		});

		a.start();
	}

	public void setTotal(final int t) throws RemoteException
	{
		Thread a = new Thread(new Runnable()
		{
			public void run() { localNotifiable.setTotal(t); }
		});

		a.start();
	}

	public void setIndeterminate(final boolean indeterminate) throws RemoteException
	{
		Thread a = new Thread(new Runnable()
		{
			public void run() { localNotifiable.setIndeterminate(indeterminate); }
		});

		a.start();
	}

	public int getCurrent() throws RemoteException
	{
		return localNotifiable.getCurrent();
	}

	public int getTotal() throws RemoteException
	{
		return localNotifiable.getTotal();
	}

	public String getDescription() throws RemoteException
	{
		return localNotifiable.getDescription();
	}

	public boolean isIndeterminate() throws RemoteException
	{
		return localNotifiable.isIndeterminate();
	}

// ------------------------------------------------------
// ******************************************************
// ------------------------------------------------------


	public RemoteProgressNotifiableImpl(ProgressNotifiable not) throws RemoteException
	{
		this.localNotifiable = not;
	}

	private ProgressNotifiable localNotifiable;
}
