/*
 * @(#)MeduwebTranslationModel.java
 *
 * $Id: MeduwebTranslationModel.java,
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import javax.swing.event.*;

import medview.common.generator.*;
import medview.common.translator.*;
import medview.datahandling.*;

/**
 * The translation model represents what it means
 * to be a 'container of translations for a term',
 * and fulfills responsibilities related to this.
 * When constructed, the term given to the translation
 * model is stored exactly as given. A table of mappings
 * from the eventual values in lower case to their
 * respective translation objects is also initiated.
 *
 * For developers: it is very important to keep in mind
 * that the internal hashmap of translations is indexed
 * by the value (if the value is a string) in lower case.
 * The actual value string (again if it is a string) is
 * kept in the referenced translation object.
 *
 * @author Fredrik Lindahl
 */
public abstract class MeduwebTranslationModel implements TranslationConstants, Cloneable
{

	public String getTranslation( Object value ) // returns null if non-existant...
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		Translation translation = (Translation) translations.get(value);

		if (translation != null) { return translation.getTranslation(); }

		return null;
	}

	public Translation[] getTranslations( )
	{
		Translation[] tA = new Translation[translations.size()];

		translations.values().toArray(tA);

		return tA;
	}

	public void setTranslation( Object value, String trans )
	{
		this.setTranslation(value, trans, new Date());
	}

	public void setTranslation( Object value, String trans, Date date ) // does nothing if non-existant or equal...
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		Translation translation = (Translation) translations.get(value);

		if ((translation != null) && !(translation.getTranslation().equals(trans)))
		{
			translation.setTranslation(trans);

			translation.setLastModified(date);

			fireTranslationChanged(translation);
		}
	}





	public boolean containsValue(Object value)
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		return (translations.get(value) != null);

		/* NOTE: this method was not correct
		 * earlier, which led to complete havoc
		 * in the system since the stored translators
		 * did not read in correctly. This was due to
		 * the fact that the translator synchronizer
		 * checked if a term's values in the value files
		 * were new to the loaded model, and if they were
		 * these new values were set (overwriting the old
		 * ones) in the translation. Since the values as
		 * specified in the term values file were in upper
		 * case and the hash map was indexed by the value in
		 * lower case, it thought that the translation model
		 * did not contain the value and overwrote the old
		 * one with new, blank, ones. It is very important
		 * to hide the fact that the hash map uses lower case
		 * value strings from the outside and that all methods
		 * take this into consideration. */
	}








	public abstract int getType( );

	public String getTerm( ) { return term; }

	public abstract String getTypeIdentifier();

	protected abstract String getTypeDescription( );

	public int getTranslationCount() { return translations.size(); }

	public String getDescription( ) { return term + " / " + getTypeDescription(); }








	public Object[] getValues( )
	{
		Translation[] trans = this.getTranslations();

		Object[] retArray = new Object[trans.length];

		for (int ctr=0; ctr<trans.length; ctr++)
		{
			retArray[ctr] = trans[ctr].getValue();
		}

		return retArray;
	}

	public void addValue( Object value )
	{
		this.addValue(value, new Date());
	}

	public void addValue( Object value, Date date )
	{
		this.addValue(value, TRANSLATION_DEFAULT, date);
	}

	public void addValue( Object value, String trans )
	{
		this.addValue(value, trans, new Date());
	}

	public void addValue( Object value, String trans, Date date )
	{
		this.addTranslation(new Translation(value, trans, date));
	}

	protected void addTranslation( Translation translation )
	{
		Object value = translation.getValue();

		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		translations.put(value, translation);

		fireTranslationAdded(translation);
	}



	public void removeValue( Object value ) // does nothing if non-existant...
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		Translation translation = (Translation) translations.get(value);

		if (translation != null)
		{
			translations.remove(value);

			fireTranslationRemoved(translation);
		}
	}

	public void removeValues( Vector values )
	{
		Enumeration enm = values.elements();

		Translation trans = null;

		Object value = null;

		while (enm.hasMoreElements())
		{
			value = enm.nextElement();

			if (value instanceof String)
			{
				value = ((String)value).toLowerCase(); // <- IMPORTANT
			}

			trans = (Translation) translations.get(value);

			translations.remove(value);

			fireTranslationRemoved(trans);
		}
	}

	public boolean valueExists( Object value )
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		return translations.containsKey(value);
	}








	public void addTranslationModelListener( TranslationModelListener l )
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(TranslationModelListener.class, l);
	}

	public void removeTranslationModelListener( TranslationModelListener l )
	{
		if (listenerList == null) { return; }

		listenerList.remove(TranslationModelListener.class, l);
	}

	protected void firePreviewChanged( Translation translation )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslationModelListener.class)
			{
				TranslationModelEvent e = new TranslationModelEvent(this, translation, false, -1);

				((TranslationModelListener)listeners[i+1]).previewChanged(e);
			}
		}

	}

	protected void fireTranslationRemoved( Translation translation )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslationModelListener.class)
			{
				TranslationModelEvent e = new TranslationModelEvent(this, translation, false, -1);

				((TranslationModelListener)listeners[i+1]).translationRemoved(e);
			}
		}
	}

	protected void fireTranslationAdded( Translation translation )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslationModelListener.class)
			{
				TranslationModelEvent e = new TranslationModelEvent(this, translation, false, -1);

				((TranslationModelListener)listeners[i+1]).translationAdded(e);
			}
		}
	}

	protected void fireTranslationChanged( Translation translation )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslationModelListener.class)
			{
				TranslationModelEvent e = new TranslationModelEvent(this, translation, false, -1);

				((TranslationModelListener)listeners[i+1]).translationChanged(e);
			}
		}
	}

	protected void fireVGChanged( boolean autoVG, int policy )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslationModelListener.class)
			{
				TranslationModelEvent e = new TranslationModelEvent(this, null, autoVG, policy);

				((TranslationModelListener)listeners[i+1]).vgChanged(e);
			}
		}
	}








	public String[] getPreviewValues()
	{
		Iterator iter = translations.values().iterator();

		Translation currTranslation = null;

		Vector values = new Vector();

		while (iter.hasNext())
		{
			currTranslation = (Translation) iter.next();

			if (currTranslation.isPreview()) { values.add((String)currTranslation.getValue()); }
		}

		String[] retArray = new String[values.size()];

		if (retArray.length != 0) { values.toArray(retArray); }

		return retArray;
	}

	public void setPreviewStatus(Object value, boolean flag) // does nothing if non-existant...
	{
		if (value instanceof String ) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		Translation translation = (Translation) translations.get(value);

		if (translation != null)
		{
			translation.setPreview(flag);

			firePreviewChanged(translation);
		}
	}

	public boolean isPreview( Object value ) // returns false if non-existant...
	{
		if (value instanceof String) { value = ((String)value).toLowerCase(); } // <- IMPORTANT

		Translation translation = (Translation) translations.get(value);

		if (translation != null) { return translation.isPreview(); }

		return false;
	}

	public void clearAllPreviews()
	{
		Iterator iter = translations.values().iterator();

		while (iter.hasNext())
		{
			Translation currTranslation = (Translation) iter.next();

			if (currTranslation.isPreview())
			{
				currTranslation.setPreview(false);

				firePreviewChanged(currTranslation);
			}
		}
	}









	public boolean isAutoVG( )
	{
		return autoVG;
	}

	public void setAutoVG( boolean autoVG )
	{
		this.autoVG = autoVG;

		fireVGChanged(autoVG, getVGPolicy());
	}

	public void setVGPolicy( int policy )
	{
		vGPolicy = policy;

		fireVGChanged(isAutoVG(), policy);
	}

	public void setVGPolicy( String policy )
	{
		setVGPolicy(TranslationModel.getVGPolicy(policy));
	}

	public int getVGPolicy( )
	{
		return vGPolicy;
	}

	public String getVGPolicyIdentifier()
	{
		return getVGPolicyIdentifier(getVGPolicy());
	}








	public static int getVGPolicy( String identifier ) // returns -1 if not recognized...
	{
		if (identifier.equalsIgnoreCase(AUTO_VG_POLICY_GEMEN_IDENTIFIER))
		{
			return AUTO_VG_POLICY_GEMEN;
		}
		else if (identifier.equals(AUTO_VG_POLICY_VERSAL_IDENTIFIER))
		{
			return AUTO_VG_POLICY_VERSAL;
		}
		else
		{
			return -1;
		}
	}

	public static String getVGPolicyIdentifier( int policy ) // returns null if not recognized...
	{
		switch (policy)
		{
			case AUTO_VG_POLICY_GEMEN:
			{
				return AUTO_VG_POLICY_GEMEN_IDENTIFIER;
			}

			case AUTO_VG_POLICY_VERSAL:
			{
				return AUTO_VG_POLICY_VERSAL_IDENTIFIER;
			}

			default:
			{
				return null;
			}
		}
	}








	public abstract String getValueDescription(); // used in view...

	public abstract String getTranslationDescription(); // used in view...








	public Object clone() { return null; } // needs to be overridden by each subclass for correct instantiation...

	protected MeduwebTranslationModel partiallyClone(MeduwebTranslationModel model)
	{
		Translation[] ts = this.getTranslations();

		for (int ctr=0; ctr<ts.length; ctr++)
		{
			model.addTranslation((Translation)ts[ctr].clone());
		}

		model.setVGPolicy(vGPolicy);

		model.setAutoVG(autoVG);

		return model;
	}








	private void initSimpleMembers()
	{
		autoVG = true;

		translations = new HashMap();

		vGPolicy = AUTO_VG_POLICY_GEMEN;

		mVDH = MeduwebDataHandler.instance();
	}

	private void initTranslations(Object[] values, String[] translations)
	{
		if ((values != null) && (translations != null))
		{
			if (values.length == translations.length)
			{
				for (int ctr=0; ctr<values.length; ctr++)
				{
					Translation translation = new Translation(values[ctr], translations[ctr]); // store original case in Translation value...

					if (values[ctr] instanceof String) { values[ctr] = ((String)values[ctr]).toLowerCase(); } // ...then we convert it to lower case...

					this.translations.put(values[ctr], translation); // ... and index the hash map by the lower case representation
				}
			}
			else
			{
				System.out.print("TranslationModel : WARNING : constructor called ");

				System.out.print("with a non-matching amount of values to translations,");

				System.out.println(" nothing will be added to the translation model...");
			}
		}
	}

	public MeduwebTranslationModel( String term )
	{
		this(term, null, null);
	}

	public MeduwebTranslationModel( String term, Object[] values, String[] translations )
	{
		this.term = term;

		initSimpleMembers();

		initTranslations(values, translations);
	}

	protected String term;

	protected int vGPolicy;

	protected boolean autoVG;

	protected HashMap translations;

	protected MeduwebDataHandler mVDH;

	protected EventListenerList listenerList;

	public static final int AUTO_VG_POLICY_GEMEN = 2;

	public static final int AUTO_VG_POLICY_VERSAL = 1;

	public static final String AUTO_VG_POLICY_VERSAL_IDENTIFIER = "versal";

	public static final String AUTO_VG_POLICY_GEMEN_IDENTIFIER = "gemen";
}
