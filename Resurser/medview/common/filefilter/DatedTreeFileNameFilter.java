package medview.common.filefilter;

import java.io.*;

import java.util.*;

import medview.datahandling.*;

public class DatedTreeFileNameFilter extends TreeFileNameFilter
{
	public boolean accept(File dir, String name)
	{
		File file = new File(dir, name);

		String ender = MedViewDataConstants.MVD_TREE_FILE_ENDER;

		if (date != null)
		{
			return ((name.endsWith(ender)) && (file.lastModified() > date.getTime()));
		}
		else
		{
			return (name.endsWith(ender));
		}
	}

	public DatedTreeFileNameFilter(Date date)
	{
		this.date = date;
	}

	private Date date = null;
}
