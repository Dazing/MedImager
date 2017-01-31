package medview.medserver;

import javax.swing.*;

import medview.datahandling.*;

import medview.medserver.model.*;
import medview.medserver.view.*;

/**
 */
public class MedServer implements MedViewMediaConstants
{
	public static void main(String[] args)
	{
		// look and feel

		try
		{
			try
			{
				String prop = MedServerModelUserProperties.LOOK_AND_FEEL_PROPERTY;

				String userLAFClass = MedViewDataHandler.instance().getUserStringPreference(

					prop, null, MedServerModelUserProperties.class);

				if (userLAFClass != null) // the L&F has been set by the user
				{
					UIManager.setLookAndFeel(userLAFClass);
				}
				else // no L&F set by the user, try to set system L&F
				{
					String sysLAFClass = UIManager.getSystemLookAndFeelClassName();

					UIManager.setLookAndFeel(sysLAFClass);
				}
			}
			catch (Exception e)
			{
				System.out.println(

					"Warning: could not set either the user set " +

					"look and feel or the look and feel for the " +

					"current system, will resort to using the " +

					"cross platform look and feel.");

				String crossLAFClass = UIManager.getCrossPlatformLookAndFeelClassName();

				UIManager.setLookAndFeel(crossLAFClass);
			}
		}
		catch (Exception e)
		{
			System.err.println(

				"FATAL ERROR: Could not set any of the following " +

				"look and feels: user, system, or cross platform. " +

				"Cannot run the program - exiting with error code 1.");

			System.exit(1);
		}

		// language

		String prop = MedServerModelUserProperties.LANGUAGE_PROPERTY;

		String setLang = MedViewDataHandler.instance().getUserStringPreference(prop, null,

			MedServerModelUserProperties.class);

		if (setLang != null)
		{
			try
			{
				MedViewDataHandler.instance().changeLanguage(setLang);
			}
			catch (LanguageException e)
			{
				System.err.print("Could not load specified language ");

				System.err.print("(" + setLang + ") - will use default ");

				System.err.print("language instead.");
			}
		}

		// data handling

		MedViewDataHandler.instance().setExaminationDataHandlerToUse(

			new medview.datahandling.examination.CachingExaminationDataHandler(

				new medview.datahandling.examination.MVDHandler()),true);

		// initializations and showing

		MedServerModel model = new MedServerModel();

		MedServerFrame frame = new MedServerFrame(model);

		frame.setIconImage(MedViewDataHandler.instance().getImage(FRAME_IMAGE_ICON));

		frame.show();
	}
}
