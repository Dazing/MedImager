/*
 * @(#)MeduwebIntervalTranslationModel.java
 *
 * $Id: MeduwebIntervalTranslationModel.java,v 1.1 2003/07/21 21:55:07 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import medview.datahandling.*;

import medview.common.translator.*;

import misc.domain.*;

public class MeduwebIntervalTranslationModel extends MeduwebSingleValueTranslationModel implements MedViewLanguageConstants
{

	public int getType( )
	{
		return TermDataHandler.INTERVAL_TYPE;
	}

	protected String getTypeDescription( )
	{
		return TermDataHandler.INTERVAL_STRING;
	}

	public String getTypeIdentifier()
	{
		return TermDataHandler.INTERVAL_STRING;
	}





	public void addValue( Object value, String trans, Date date) // all addValue() methods calls this one...
	{
		Interval interval = null;

		if (value instanceof Interval)
		{
			interval = (Interval) value;
		}
		else if (value instanceof String)
		{
			interval = extractIntervalFromString((String)value);

			if (interval == null) { return; } // null if not possible to extract...
		}
		else
		{
			System.out.print("MeduwebIntervalTranslationModel: WARNING : tried to ");

			System.out.print("add a value to the interval translation model ");

			System.out.print("whose class was of '" + value.getClass().getName());

			System.out.println(" and was not recognized, will skip this value...");

			return;
		}

		if (!(intervalAlreadyPresent(interval)))
		{
			super.addValue(interval, trans, date);
		}
		else
		{
			System.out.print("MeduwebIntervalTranslationModel: WARNING : tried to ");

			System.out.print("add an interval to the interval translation model ");

			System.out.print("that already was in the model, will skip this value...");

			return;
		}
	}

	public void removeValue(Object value) // does nothing if not proper interval [string]...
	{
		Interval interval = null;

		if (value instanceof Interval)
		{
			interval = (Interval) value;
		}
		if (value instanceof String)
		{
			interval = extractIntervalFromString((String)value);
		}

		if (interval != null) { super.removeValue(interval); }
	}

	protected boolean intervalAlreadyPresent(Interval value)
	{
		float valueMin = value.getMin();

		float valueMax = value.getMax();

		Iterator iter = translations.values().iterator();

		while (iter.hasNext())
		{
			Translation currTranslation = (Translation) iter.next();

			Interval currInterval = (Interval) currTranslation.getValue();

			float currMin = currInterval.getMin();

			float currMax = currInterval.getMax();

			if ((valueMin >= currMin && valueMin <= currMax) ||

				(valueMax >= currMin && valueMax <= currMax) ||

				(valueMin <= currMin && valueMax >= currMax))
			{
				return true;
			}
		}

		return false;
	}

	protected Interval extractIntervalFromString(String s)
	{
		try
		{
			return Interval.parse(s);
		}
		catch (Exception e)
		{
			System.out.print("MeduwebIntervalTranslationModel: WARNING : tried to ");

			System.out.print("parse an invalid interval string (" + s + ")");

			return null;
		}
	}

	public void setTranslation(Object value, String translation, Date date)
	{
		Interval interval = null;

		if (value instanceof Interval)
		{
			interval = (Interval) value;
		}
		else if (value instanceof String)
		{
			interval = extractIntervalFromString((String)value);

			if (interval == null) { return; } // null if not possible to extract...
		}
		else
		{
			System.out.print("MeduwebIntervalTranslationModel: WARNING : tried to ");

			System.out.print("set a translation for a value ");

			System.out.print("whose class was of '" + value.getClass().getName());

			System.out.println("' and was not recognized");

			return;
		}

		super.setTranslation(interval, translation, date);
	}





	public boolean containsValue(Object value)
	{
		Interval interval = null;

		if (value instanceof Interval)
		{
			interval = (Interval) value;
		}
		else if (value instanceof String)
		{
			interval = extractIntervalFromString((String)value);

			if (interval == null) { return true; }
		}
		else
		{
			System.out.print("MeduwebIntervalTranslationModel: WARNING : tried to ");

			System.out.print("check if a translation model contained a value ");

			System.out.print("for a class of '" + value.getClass().getName());

			System.out.println("' which is not recognized...");

			return false;
		}

		return super.containsValue(interval);
	}





	public String getTranslation( Object value )
	{
		if (value instanceof String)
		{
			try
			{
				value = Float.valueOf((String)value);
			}
			catch (NumberFormatException e) { return TRANSLATION_DEFAULT; }
		}

		if (!(value instanceof Float)) { return TRANSLATION_DEFAULT; }

		Translation translation = getTranslationForValue((Float)value);

		if (translation != null) { return translation.getTranslation(); }

		return TRANSLATION_DEFAULT; // value not in translation model
	}

	protected Translation getTranslationForValue(Float value)
	{
		Iterator iter = translations.values().iterator();

		while (iter.hasNext())
		{
			Translation currTranslation = (Translation) iter.next();

			Interval currInterval = (Interval) currTranslation.getValue();

			if ((value.floatValue() >= currInterval.getMin()) && (value.floatValue() <= currInterval.getMax()))
			{
				return currTranslation;
			}
		}

		return null;
	}





	public String[] getPreviewValues( ) // returns empty string array if none...
	{
		Vector previewVector = null;

		Iterator iter = translations.values().iterator();

		while (iter.hasNext())
		{
			Translation currTranslation = (Translation) iter.next();

			if (currTranslation.isPreview())
			{
				if (previewVector == null) { previewVector = new Vector(); }

				Interval transInterval = (Interval) currTranslation.getValue();

				previewVector.add(extractIntervalPreviewValue(transInterval));

				break;
			}
		}

		if (previewVector != null) // a preview has been set...
		{
			String[] retArray = new String[1];

			previewVector.toArray(retArray);

			return retArray;
		}
		else
		{
			return new String[0];
		}
	}

	public void setPreviewStatus(Object value, boolean flag)
	{
		if (value instanceof String)
		{
			Interval interval = extractIntervalFromString((String)value);

			if (interval != null)
			{
				super.setPreviewStatus(interval, flag);
			}
			else
			{
				System.out.print("MeduwebIntervalTranslationModel: WARNING : ");

				System.out.print("tried to set preview status for a ");

				System.out.print("string interval '" + value + "' ");

				System.out.print("but could not extract an Interval ");

				System.out.println("object from the string, doing nothing...");

				return;
			}
		}
		else
		{
			super.setPreviewStatus(value, flag);
		}
	}

	protected String extractIntervalPreviewValue(Interval interval)
	{
		float intMin = interval.getMin();

		float intMax = interval.getMax();

		return ((intMax + intMin) / 2) + "";
	}





	public String getValueDescription( )
	{
		return mVDH.getLanguageString(OTHER_INTERVAL_VALUE_DESCRIPTION_LS_PROPERTY);
	}

	public String getTranslationDescription( )
	{
		return mVDH.getLanguageString(OTHER_INTERVAL_TRANSLATION_DESCRIPTION_LS_PROPERTY);
	}





	public Object clone()
	{
		return super.partiallyClone(new MeduwebIntervalTranslationModel(term));
	}





	public MeduwebIntervalTranslationModel( String term )
	{
		super(term);
	}

	public MeduwebIntervalTranslationModel( String term, Object[] values, String[] translations )
	{
		super(term, values, translations);
	}

}