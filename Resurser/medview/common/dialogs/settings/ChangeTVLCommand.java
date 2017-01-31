/*
 * @(#)ChangeTDLCommand.java
 *
 * $Id: ChangeTVLCommand.java,v 1.1 2002/11/18 23:07:54 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeTVLCommand implements Command
{
	public boolean shouldReplace(Command command)
	{
		return (command instanceof ChangeTVLCommand);
	}

	public void execute()
	{
		mVDH.setTermValueLocation(tVLPath);
	}



	public ChangeTVLCommand(String tVLPath)
	{
		this.tVLPath = tVLPath;
	}

	private static MedViewDataHandler mVDH;

	private String tVLPath;

	static
	{
		mVDH = MedViewDataHandler.instance();
	}
}