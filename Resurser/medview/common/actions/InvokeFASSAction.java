package medview.common.actions;

import edu.stanford.ejalbert.*;

import java.awt.event.*;

import java.io.*;

import medview.datahandling.*;

/**
 * An action that starts a web-browser window and directs it
 * to a specified url.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class InvokeFASSAction extends MedViewAction implements
	MedViewLanguageConstants, MedViewMediaConstants
{
	// MEMBERS

	private String url = null;

	// CONSTRUCTOR

	public InvokeFASSAction()
	{
		super(ACTION_INVOKE_FASS_LS_PROPERTY, INVOKE_FASS_IMAGE_ICON_24);
	}

	// URL SETTER AND GETTER

	public void setURL(String url)
	{
		this.url = url;
	}

	public String getURL()
	{
		return this.url;
	}

	// ACTION

	public void actionPerformed(ActionEvent e)
	{
		try
		{
			// place this as a setting (via preferences) later // Fredrik

			if (url != null)
			{
				BrowserLauncher.openURL(url);
			}
			else
			{
				System.out.print("Warning: InvokeFASSAction: null url");
			}
		}
		catch (java.net.MalformedURLException exc)
		{
			exc.printStackTrace();
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}
}
