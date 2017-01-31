package medview.common.filefilter;

import java.io.*;

import medview.datahandling.*;

public class TreeFileNameFilter implements FilenameFilter
{
	public boolean accept(File dir, String name)
	{
		String ender = MedViewDataConstants.MVD_TREE_FILE_ENDER;

		return name.endsWith(ender);
	}
}
