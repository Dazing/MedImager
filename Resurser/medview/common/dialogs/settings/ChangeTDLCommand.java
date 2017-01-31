/*
 * @(#)ChangeTDLCommand.java
 *
 * $Id: ChangeTDLCommand.java,v 1.1 2002/11/18 23:07:53 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeTDLCommand implements Command
{
	public boolean shouldReplace(Command command)
	{
		return (command instanceof ChangeTDLCommand);
	}

	public void execute()
	{
		mVDH.setTermDefinitionLocation(tDLPath);
	}



	public ChangeTDLCommand(String tDLPath)
	{
		this.tDLPath = tDLPath;
	}

	private static MedViewDataHandler mVDH;

	private String tDLPath;

	static
	{
		mVDH = MedViewDataHandler.instance();
	}
}