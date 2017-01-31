/*
 * @(#)DialogMVDFileFilter.java
 *
 * $Id: DialogMVDFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogMVDFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogMVDFileFilter()
	{
		super(new String[] { ".MVD", ".mvd", ".Mvd" }, FILEFILTER_MVD_DIRECTORIES_LS_PROPERTY);
	}
}