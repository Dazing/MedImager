package medview.common.generator;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.translator.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MedViewRegularTranslationModel extends RegularTranslationModel implements
	MedViewLanguageConstants
{
	public String getTranslationDescription( )
	{
		return mVDH.getLanguageString(OTHER_REGULAR_VALUE_DESCRIPTION_LS_PROPERTY);
	}

	public String getValueDescription( )
	{
		return mVDH.getLanguageString(OTHER_REGULAR_TRANSLATION_DESCRIPTION_LS_PROPERTY);
	}
	
	public MedViewRegularTranslationModel(String term)
	{
		super(term);
	}

	public MedViewRegularTranslationModel(String term, Object[] values, String[] translations)
	{
		super(term, values, translations);
	}
	
	protected MedViewDataHandler mVDH = MedViewDataHandler.instance();
}
