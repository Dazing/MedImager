/*
 * @(#)DialogRTFFileFilter.java
 *
 * $Id: DialogRTFFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogRTFFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogRTFFileFilter()
	{
		super(new String[] {".rtf"}, FILEFILTER_RTF_FILES_LS_PROPERTY);
	}
}