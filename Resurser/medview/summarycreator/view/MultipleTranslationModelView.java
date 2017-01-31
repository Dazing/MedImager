/*
 * @(#)MultipleTranslationModelView.java
 *
 * $Id: MultipleTranslationModelView.java,v 1.8 2005/02/24 16:32:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import medview.datahandling.*;

public class MultipleTranslationModelView extends AbstractTableTermView implements MedViewLanguageConstants
{
	public String[] getTableColumnNamesLS()
	{
		return new String[]
		{
			OTHER_PREVIEW_DESCRIPTION_LS_PROPERTY,

			OTHER_MULTIPLE_VALUE_DESCRIPTION_LS_PROPERTY,

			OTHER_MULTIPLE_TRANSLATION_DESCRIPTION_LS_PROPERTY
		};
	}
}
