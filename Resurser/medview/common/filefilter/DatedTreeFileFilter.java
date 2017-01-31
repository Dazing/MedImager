package medview.common.filefilter;

import java.io.*;

import java.util.*;

import medview.datahandling.*;

public class DatedTreeFileFilter extends TreeFileFilter
{
	public boolean accept(File file)
	{
		String fileName = file.getName();

		String ender = MedViewDataConstants.MVD_TREE_FILE_ENDER;

		if (date != null)
		{
			return ((fileName.endsWith(ender)) && (file.lastModified() > date.getTime()));
		}
		else
		{
			return (fileName.endsWith(ender));
		}
	}

	public DatedTreeFileFilter(Date date)
	{
		this.date = date;
	}

	private Date date = null;
}
