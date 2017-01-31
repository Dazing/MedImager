/*
 * @(#)DialogXMLFileFilter.java
 *
 * $Id: DialogXMLFileFilter.java,v 1.4 2010/06/28 08:50:14 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import medview.datahandling.MedViewLanguageConstants;

public class DialogXMLFileFilter extends DialogFileFilter implements MedViewLanguageConstants
{
	public DialogXMLFileFilter()
	{
		super(new String[] {".xml"}, FILEFILTER_XML_FILES_LS_PROPERTY);
	}
}