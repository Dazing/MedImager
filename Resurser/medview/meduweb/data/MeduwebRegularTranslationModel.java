/*
 * @(#)MeduwebRegularTranslationModel.java
 *
 * $Id: MeduwebRegularTranslationModel.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import medview.datahandling.*;
import medview.common.translator.*;

public class MeduwebRegularTranslationModel extends MeduwebSingleValueTranslationModel implements MedViewLanguageConstants
{

	public int getType( )
	{
		return TermDataHandler.REGULAR_TYPE;
	}

	protected String getTypeDescription( )
	{
		return TermDataHandler.REGULAR_STRING;
	}

	public String getTypeIdentifier()
	{
		return TermDataHandler.REGULAR_STRING;
	}





	public String getTranslationDescription( )
	{
		return mVDH.getLanguageString(OTHER_REGULAR_VALUE_DESCRIPTION_LS_PROPERTY);
	}

	public String getValueDescription( )
	{
		return mVDH.getLanguageString(OTHER_REGULAR_TRANSLATION_DESCRIPTION_LS_PROPERTY);
	}




	public Object clone()
	{
		return super.partiallyClone(new MeduwebRegularTranslationModel(term));
	}





	public MeduwebRegularTranslationModel( String term )
	{
		super(term);
	}

	public MeduwebRegularTranslationModel( String term, Object[] values, String[] translations)
	{
		super(term, values, translations);
	}

}
