/*
 * @(#)MeduwebSingleValueTranslationModel.java
 *
 * $Id: MeduwebSingleValueTranslationModel.java,v 1.0
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import medview.common.translator.*;

import java.util.*;

/**
 * Single value translations models represent
 * terms that can only have one value specified.
 *
 * @author Fredrik Lindahl
 */
public abstract class MeduwebSingleValueTranslationModel extends MeduwebTranslationModel
{

	public String[] getPreviewValues( ) // returns empty string array if none...
	{
		Iterator iter = translations.values().iterator();

		while (iter.hasNext())
		{
			Translation cT = (Translation) iter.next();

			if (cT.isPreview()) { return new String[] {(String)cT.getValue()}; }
		}

		return new String[0];
	}

	public void setPreviewStatus(Object value, boolean flag)
	{
		if (flag == true) { super.clearAllPreviews(); }

		super.setPreviewStatus(value, flag);
	}





	/* NOTE: the superclass method takes care of
	 * converting the value to lowercase, so it
	 * is not done here. Also, when returning the
	 * preview value array, the single value model
	 * makes sure that only one value is returned,
	 * namely, the first value found to be the preview
	 * among the kept translations. */





	public MeduwebSingleValueTranslationModel(String term)
	{
		super(term);
	}

	public MeduwebSingleValueTranslationModel(String term, Object[] values, String[] translations)
	{
		super(term, values, translations);
	}

}