package medview.common.filefilter;

import java.io.*;

import medview.datahandling.*;

public class TreeFileFilter implements FileFilter
{
	public boolean accept(File file)
	{
		String ender = MedViewDataConstants.MVD_TREE_FILE_ENDER;

		return (file.getName().endsWith(ender));
	}
}
