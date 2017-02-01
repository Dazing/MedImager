/*
 * @(#)MeduwebSeparatedTranslationModel.java
 *
 * $Id: MeduwebSeparatedTranslationModel.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import javax.swing.event.*;
import medview.common.translator.*;
import java.util.*;

/**
 * A separated translation model contains information
 * about how the values should be separated (ie the
 * values can be multiple).
 *
 * @author Fredrik Lindahl
 */
public abstract class MeduwebSeparatedTranslationModel extends MeduwebTranslationModel
{

	public String getNTLSeparator( )
	{
		return nTLSeparator;
	}

	public String getSeparator( )
	{
		return separator;
	}

	public void setNTLSeparator( String sep )
	{
		this.nTLSeparator = sep;

		fireNTLSeparatorChanged(sep);
	}

	public void setSeparator( String sep )
	{
		this.separator = sep;

		fireSeparatorChanged(sep);
	}




	 /* NOTE: The translation model superclass returns
	  * an array of preview values when asked for, ie
	  * it returns all the translations that have been
	  * tagged as previews. Thus the separated translation
	  * model subclass does not have to override this
	  * functionality. */





	public void addSeparatedTranslationModelListener(SeparatedTranslationModelListener l)
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(SeparatedTranslationModelListener.class, l);
	}

	public void removeSeparatedTranslationModelListener(SeparatedTranslationModelListener l)
	{
		if (listenerList == null) { return; }

		listenerList.remove(SeparatedTranslationModelListener.class, l);
	}

	protected void fireSeparatorChanged(String separator)
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == SeparatedTranslationModelListener.class)
			{
				SeparatedTranslationModelEvent e = new SeparatedTranslationModelEvent(this, separator);

				((SeparatedTranslationModelListener)listeners[i+1]).separatorChanged(e);
			}
		}
	}

	protected void fireNTLSeparatorChanged(String ntlSeparator)
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == SeparatedTranslationModelListener.class)
			{
				SeparatedTranslationModelEvent e = new SeparatedTranslationModelEvent(this, ntlSeparator);

				((SeparatedTranslationModelListener)listeners[i+1]).ntlSeparatorChanged(e);
			}
		}
	}





	protected MeduwebTranslationModel partiallyClone(MeduwebTranslationModel model)
	{
		((MeduwebSeparatedTranslationModel)model).setNTLSeparator(nTLSeparator);

		((MeduwebSeparatedTranslationModel)model).setSeparator(separator);

		return super.partiallyClone(model);
	}





	private void initSeparators()
	{
		this.separator = DEFAULT_SEPARATOR;

		this.nTLSeparator = DEFAULT_NTL_SEPARATOR;
	}

	public MeduwebSeparatedTranslationModel( String term )
	{
		super(term);

		initSeparators();
	}

	public MeduwebSeparatedTranslationModel( String term, Object[] values, String[] translations )
	{
		super(term, values, translations);

		initSeparators();
	}

	protected String separator;

	protected String nTLSeparator;

	protected EventListenerList listenerList;

	protected static final String DEFAULT_SEPARATOR = ",";

	protected static final String DEFAULT_NTL_SEPARATOR = ",";

}
