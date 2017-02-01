/*
 * @(#)DialogFileFilter.java
 *
 * $Id: DialogFileFilter.java,v 1.6 2005/06/03 15:44:12 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.filefilter;

import java.io.*;
import java.util.*;

import medview.datahandling.*;

public class DialogFileFilter extends javax.swing.filechooser.FileFilter

	implements MedViewLanguageConstants
{

	public boolean accept(File f)
	{
		String fileName = f.getName().toLowerCase();

		Enumeration enm = extensions.elements();

		while (enm.hasMoreElements())
		{
			String currExtension = (String) enm.nextElement();

			if (fileName.endsWith(currExtension)) { return true; }
		}

		if (f.isDirectory()) { return true; }

		return false;
	}

	public String[] getExtensions()
	{
		String[] retArr = new String[extensions.size()];

		extensions.toArray(retArr);

		return retArr;
	}

	public String getDescription()
	{
		return description;
	}

	public DialogFileFilter(String[] extensions, String descLS)
	{
		this.extensions = new Vector();

		for (int ctr=0; ctr<extensions.length; ctr++)
		{
			this.extensions.add((String)extensions[ctr]);
		}

		description = MedViewDataHandler.instance().getLanguageString(descLS);
	}

	private Vector extensions;

	private String description;
}
