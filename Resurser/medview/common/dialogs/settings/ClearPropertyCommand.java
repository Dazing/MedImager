/*
 * @(#)ClearPropertyCommand.java
 *
 * $Id: ClearPropertyCommand.java,v 1.1 2003/06/10 00:36:13 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ClearPropertyCommand implements Command
{
	public boolean shouldReplace(Command command)
	{
		if (command instanceof ClearPropertyCommand)
		{
			return (prop.equals(((ClearPropertyCommand)command).prop));
		}

		return false;
	}

	public void execute()
	{
		MedViewDataHandler.instance().clearProperty(prop);
	}


	public ClearPropertyCommand(String prop)
	{
		this.prop = prop;
	}

	private String prop;
}