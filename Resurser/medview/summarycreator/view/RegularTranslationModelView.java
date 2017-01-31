/*
 * @(#)RegularTranslationModelView.java
 *
 * $Id: RegularTranslationModelView.java,v 1.9 2005/02/24 16:32:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import javax.swing.*;

import medview.datahandling.*;

public class RegularTranslationModelView extends AbstractTableTermView implements MedViewLanguageConstants
{
	public String[] getTableColumnNamesLS()
	{
		return new String[]
		{
			OTHER_PREVIEW_DESCRIPTION_LS_PROPERTY,

			OTHER_REGULAR_VALUE_DESCRIPTION_LS_PROPERTY,

			OTHER_REGULAR_TRANSLATION_DESCRIPTION_LS_PROPERTY
		};
	}
}
