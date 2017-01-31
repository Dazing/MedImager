/*
 * @(#)DialogImageFileFilter.java
 *
 * $Id: DialogImageFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogImageFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogImageFileFilter()
	{
		super(new String[] {".gif", ".jpg", ".jpeg"}, FILEFILTER_IMAGE_FILES_LS_PROPERTY);
	}
}