/*
 * @(#)MeduwebDefaultSeparatedTranslationModel.java
 *
 * $Id: MeduwebDefaultSeparatedTranslationModel.java,v 1.1 2003/07/21 21:55:06 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;
import medview.common.translator.*;
import medview.datahandling.*;

public class MeduwebDefaultSeparatedTranslationModel extends MeduwebSeparatedTranslationModel implements MedViewLanguageConstants
{

	protected String getTypeDescription( )
	{
		return TermDataHandler.MULTIPLE_STRING;
	}

	public int getType( )
	{
		return TermDataHandler.MULTIPLE_TYPE;
	}

	public String getTypeIdentifier()
	{
		return TermDataHandler.MULTIPLE_STRING;
	}





	public String getTranslationDescription( )
	{
		return mVDH.getLanguageString(OTHER_MULTIPLE_TRANSLATION_DESCRIPTION_LS_PROPERTY);
	}

	public String getValueDescription( )
	{
		return mVDH.getLanguageString(OTHER_MULTIPLE_VALUE_DESCRIPTION_LS_PROPERTY);
	}




	public Object clone()
	{
		return super.partiallyClone(new MeduwebDefaultSeparatedTranslationModel(term));
	}





	public MeduwebDefaultSeparatedTranslationModel( String term )
	{
		super(term);
	}

	public MeduwebDefaultSeparatedTranslationModel( String term, Object[] values, String[] translations)
	{
		super(term, values, translations);
	}

}
