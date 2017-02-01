/*
 * @(#)DialogDirectoryFileFilter.java
 *
 * $Id: DialogDirectoryFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogDirectoryFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogDirectoryFileFilter()
	{
		super(new String[] {}, FILEFILTER_DIRECTORY_LS_PROPERTY);
	}
}