/*
 * @(#)DialogHTMLFileFilter.java
 *
 * $Id: DialogHTMLFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogHTMLFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogHTMLFileFilter()
	{
		super(new String[] {".html", ".htm"}, FILEFILTER_HTML_FILES_LS_PROPERTY);
	}
}