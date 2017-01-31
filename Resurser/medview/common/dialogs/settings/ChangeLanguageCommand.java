/*
 * @(#)ChangeLanguageCommand.java
 *
 * $Id: ChangeLanguageCommand.java,v 1.4 2006/04/24 14:16:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import java.awt.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeLanguageCommand implements Command
{
	public boolean shouldReplace(Command command)
	{
		return (command instanceof ChangeLanguageCommand);
	}

	public void execute()
	{
		try
		{
			MedViewDataHandler.instance().changeLanguage(language);
		}
		catch (LanguageException e)
		{
			String errMess = e.getMessage();

			MedViewDialogs mVD = MedViewDialogs.instance();

			mVD.createAndShowErrorDialog(owner, errMess);
		}
	}


	public ChangeLanguageCommand(Frame owner, String language)
	{
		this.owner = owner;

		this.language = language;
	}

	private Frame owner;

	private String language;
}
