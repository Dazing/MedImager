// $Id: SummaryCreator.java,v 1.5 2010/07/01 09:26:33 oloft Exp $

package medview.summarycreator;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

import javax.swing.*;

import medview.datahandling.*;

import medview.summarycreator.view.*;
import medview.summarycreator.model.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * The starter class for the SummaryCreator application.
 * @author Fredrik Lindahl
 */
public class SummaryCreator
{
	public static void setupSCTextGeneration()
	{
		System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");

		System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");

		System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");

		System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");
	}

	public static void setupSCPrefLookAndFeel()
	{
		//PlasticLookAndFeel.setMyCurrentTheme(new DesertBlue()); <- to fix Java 5 look-and-feel bug

		UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

		try
		{
			try
			{
				String userLAFClass = MedViewDataHandler.instance().getUserStringPreference(

					SummaryCreatorUserProperties.LOOK_AND_FEEL_PROPERTY, null, SummaryCreatorUserProperties.class);

				if (userLAFClass != null) // the L&F has been set by the user
				{
					UIManager.setLookAndFeel(userLAFClass);
				}
				else // no L&F set by the user, try to set system L&F
				{
					UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
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

				"Cannot run the program - exiting with error code.");

			System.exit(1);
		}
	}

	public static void setupSCPrefLanguage()
	{
		String setLang = MedViewDataHandler.instance().getUserStringPreference(

			SummaryCreatorUserProperties.LANGUAGE_PROPERTY, null, SummaryCreatorUserProperties.class);

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
	}

	public static void startSummaryCreator()
	{
		SummaryCreatorModel model = new SummaryCreatorModel();

		SummaryCreatorFrame sCFrame = new SummaryCreatorFrame(model);

		String iconID = MedViewMediaConstants.FRAME_IMAGE_ICON;

		sCFrame.setIconImage(MedViewDataHandler.instance().getImage(iconID));

		sCFrame.show();
	}

	public static void main(String[] args)
	{
		setupSCTextGeneration();

		setupSCPrefLookAndFeel();

		setupSCPrefLanguage();

		startSummaryCreator();
	}
}
