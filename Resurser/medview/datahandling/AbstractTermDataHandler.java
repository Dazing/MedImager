package medview.datahandling;

import java.io.*;

import javax.swing.event.*;

/**
 * A class that provides some basic utilities that all term
 * datahandlers might need.
 *
 * @author Fredrik Lindahl
 */
public abstract class AbstractTermDataHandler implements TermDataHandler
{

	// LISTENERS AND EVENT NOTIFICATION

	/**
	 * Adds a term datahandler listener.
	 */
	public void addTermDataHandlerListener(TermDataHandlerListener l)
	{
		listenerList.add(TermDataHandlerListener.class, l);
	}

	/**
	 * Removes a term datahandler listener.
	 */
	public void removeTermDataHandlerListener(TermDataHandlerListener l)
	{
		listenerList.remove(TermDataHandlerListener.class, l);
	}

	protected void fireTermAdded(String term, int type)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term);

		event.setType(type);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termAdded(event);
			}
	     }
	}

	protected void fireTermRemoved(String term)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termRemoved(event);
			}
	     }
	}

	protected void fireValueAdded(String term, Object value)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term);

		event.setValue(value);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).valueAdded(event);
			}
	     }
	}

	protected void fireValueRemoved(String term, Object value)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term);

		event.setValue(value);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).valueRemoved(event);
			}
	     }
	}

	protected void fireDefinitionLocationChanged(String loc)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setLocation(loc);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termDefinitionLocationChanged(event);
			}
	     }
	}

	protected void fireValueLocationChanged(String loc)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setLocation(loc);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termValueLocationChanged(event);
			}
		}
	}


	// SHUT-DOWN NOTIFICATION

	public void shuttingDown()
	{
	}


	// TERM TYPES AND TYPE DESCRIPTORS

	public boolean isValidTermType(int type)
	{
		return ((type == TermDataHandler.REGULAR_TYPE) ||

			(type == TermDataHandler.MULTIPLE_TYPE) ||

			(type == TermDataHandler.INTERVAL_TYPE) ||

			(type == TermDataHandler.FREE_TYPE));
	}

	public boolean isValidTermTypeDescriptor(String typeDesc)
	{
		return (typeDesc.equals(TermDataHandler.REGULAR_TYPE_DESCRIPTOR) ||

			typeDesc.equals(TermDataHandler.MULTIPLE_TYPE_DESCRIPTOR) ||

			typeDesc.equals(TermDataHandler.INTERVAL_TYPE_DESCRIPTOR) ||

			typeDesc.equals(TermDataHandler.FREE_TYPE_DESCRIPTOR));
	}

	public String getTypeDescriptor(String term) throws NoSuchTermException, InvalidTypeException, IOException
	{
		return getTypeDescriptor(getType(term)); // -> IOException, NoSuchTermException, InvalidTypeException
	}

	public String getTypeDescriptor(int type) throws InvalidTypeException
	{
		switch(type)
		{
			case TermDataHandler.FREE_TYPE:
			{
				return TermDataHandler.FREE_TYPE_DESCRIPTOR;
			}

			case TermDataHandler.REGULAR_TYPE:
			{
				return TermDataHandler.REGULAR_TYPE_DESCRIPTOR;
			}

			case TermDataHandler.MULTIPLE_TYPE:
			{
				return TermDataHandler.MULTIPLE_TYPE_DESCRIPTOR;
			}

			case TermDataHandler.INTERVAL_TYPE:
			{
				return TermDataHandler.INTERVAL_TYPE_DESCRIPTOR;
			}
		}

		throw new InvalidTypeException(type + "");
	}

	public boolean recognizesTerm(String term)
	{
		try
		{
			getTypeDescriptor(term);

			return true;
		}
		catch (Exception exc)
		{
			return false;
		}
	}


	// TERM ADDITION / REMOVAL

	/**
	 * Adds the specified term, exactly as it is given and with its
	 * specified type, to the term definition location. Note that
	 * this causes a term to be defined in the definition location
	 * without existing at all in the value location, but this is
	 * perfectly legal. Note also that the term will first be removed
	 * if it exists already (even if it exists with another case
	 * combination). The type will first be parsed for its integer
	 * version to check that it is valid.
	 */
	public void addTerm(String term, String typeDesc) throws IOException, InvalidTypeException
	{
		addTerm(term, parseType(typeDesc));
	}

	/**
	 * Converts a term type from string format to its integer
	 * representation.
	 */
	protected int parseType(String typeDesc) throws InvalidTypeException
	{
		if (typeDesc.equalsIgnoreCase(TermDataHandler.FREE_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.FREE_TYPE;
		}

		if (typeDesc.equalsIgnoreCase(TermDataHandler.REGULAR_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.REGULAR_TYPE;
		}

		if (typeDesc.equalsIgnoreCase(TermDataHandler.MULTIPLE_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.MULTIPLE_TYPE;
		}

		if (typeDesc.equalsIgnoreCase(TermDataHandler.INTERVAL_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.INTERVAL_TYPE;
		}

		throw new InvalidTypeException(typeDesc);
	}

	/**
	 * The settings handler.
	 */
	protected MedViewDataSettingsHandler mVDSH = MedViewDataSettingsHandler.instance();

	/**
	 * List of registered event listeners.
	 */
	protected EventListenerList listenerList = new EventListenerList();


}
