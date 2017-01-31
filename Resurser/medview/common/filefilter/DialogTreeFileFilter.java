/*
 * @(#)DialogHTMLFileFilter.java
 *
 * $Id: DialogTreeFileFilter.java,v 1.2 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Olof  Torgersson
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogTreeFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogTreeFileFilter()
{
		super(new String[] {"tree", "Tree", "TREE"}, FILEFILTER_TREE_FILES_LS_PROPERTY);
}
}