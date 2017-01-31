// $Id: MedSummary.java,v 1.12 2010/07/01 09:26:04 oloft Exp $

package medview.medsummary;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

import java.awt.event.*;

import java.io.*;

import javax.swing.*;

import medview.datahandling.*;

import medview.medsummary.model.*;
import medview.medsummary.view.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * For now, there can only be one MedSummary frame in
 * one running application instance. If the main entry
 * point is this class, look-and-feel and language will
 * be set up according to the settings used in the MS
 * application. If another application invokes the various
 * startup methods, no automatic look-and-feel and language
 * settings will be made, but the invoker can choose to call
 * the look-and-feel and language setup methods which will
 * setup the system to use the settings in the MS app.
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedSummary
{
	// STATIC MEMBERS

	/** The singleton MedSummary frame - created at first use. */
	private static MedSummaryFrame medSummaryFrame = null;

	/** The singleton data handler. */
	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();


	// STARTUP METHODS

	/**
	 * Starts the MedSummary application.
	 */
	public static void startMedSummary()
	{
		startMedSummary(null, false);
	}

	/**
	 * Starts the MedSummary application. Tries to pre-select
	 * and add (to the select tree) the specified patient.
	 * @param pid PatientIdentifier
	 */
	public static void startMedSummary(PatientIdentifier pid)
	{
		startMedSummary(pid, false);
	}

	/**
	 * Starts the MedSummary application. Tries to pre-select
	 * and add (to the select tree) the specified patient. If
	 * the boolean parameter is set to true, will invoke the
	 * feeder application for new input for the specified patient.
	 * @param pid PatientIdentifier
	 * @param invokeFeeder boolean
	 */
	public static void startMedSummary(final PatientIdentifier pid, boolean invokeFeeder)
	{
		if (!frameHasBeenCreated())
		{
			medSummaryFrame = createFrame();
		}

		if (pid != null)
		{
			try
			{
				if (mVDH.getExaminationCount(pid) > 0) // -> IOException
				{
					medSummaryFrame.addPatient(pid); // adds to patient select tree
				}
			}
			catch (IOException exc)
			{
				/* NOTE: For now, in the future the user should be presented with the
				   option to set the data-location and then this should be re-tried. */

				exc.printStackTrace();

				JOptionPane.showMessageDialog(null, exc.getMessage(), "Critical error - could not obtain " +

					"examination count for '" + pid + "' - exiting", JOptionPane.ERROR_MESSAGE);

				System.exit(1);
			}
		}

		if (invokeFeeder)
		{
			if (!medSummaryFrame.isShowing())
			{
				medSummaryFrame.addWindowFocusListener(new WindowAdapter()
				{
					public void windowGainedFocus(WindowEvent e)
					{
						if (pid != null)
						{
							medSummaryFrame.initiateFeeder(pid); // to ensure startup after MS
						}
						else
						{
							medSummaryFrame.initiateFeeder();
						}

						medSummaryFrame.removeWindowFocusListener(this);
					}
				});

				medSummaryFrame.show();
            }
			else
			{
				if (pid != null)
				{
					medSummaryFrame.initiateFeeder(pid);
				}
				else
				{
					medSummaryFrame.initiateFeeder();
				}
			}
            
        }
		else
		{
			medSummaryFrame.show();
		}
	}


	// FRAME CREATION-RELATED

	/**
	 * Returns whether or not the frame has been
	 * constructed yet.
	 * @return boolean
	 */
	public static boolean frameHasBeenCreated()
	{
		return (medSummaryFrame != null);
	}

	/**
	 * Creates the frame, but does not show it.
	 * @return MedSummaryFrame
	 */
	private static MedSummaryFrame createFrame()
	{
		MedSummaryModel model = new MedSummaryModel();

		MedSummaryFrame frame = new MedSummaryFrame(model);

		return frame;
	}


	// TEXT GENERATION SETUP

	public static void setupMSTextGeneration()
	{
		System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");

		System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");

		System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");

		System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");
	}


	// LOOK-AND-FEEL SETUP

	/**
	 * Sets up the system to use the look-and-feel setup
	 * set in the MedSummary applications preferences.
	 */
	public static void setupMSPrefLookAndFeel()
	{
		//PlasticLookAndFeel.setMyCurrentTheme(new DesertBlue()); <- to fix Java 5 look-and-feel bug

		UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

		try
		{
			try
			{
				String prop = MedSummaryUserProperties.LOOK_AND_FEEL_PROPERTY;

				String userLAFClass = mVDH.getUserStringPreference(prop, null, MedSummaryUserProperties.class);

				if (userLAFClass != null) // the L&F has been set by the user
				{
					UIManager.setLookAndFeel(userLAFClass);
				}
				else
				{
					UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
				}
			}
			catch (Exception e)
			{
				String a = "Warning: could not set either the user set ";

				String b = "look and feel or the plastic look and feel, ";

				String c = "will resort to using the cross platform look and feel.";

				System.err.println(a + b + c);

				String crossLAFClass = UIManager.getCrossPlatformLookAndFeelClassName();

				UIManager.setLookAndFeel(crossLAFClass);
			}
		}
		catch (Exception e)
		{
			String a = "FATAL ERROR: Could not set any of the following ";

			String b = "look and feels: user, plastic, or cross platform. ";

			String c = "Cannot run the program - exiting with error code.";

			System.err.println(a + b + c);

			System.exit(1);
		}
	}


	// LANGUAGE SETUP

	/**
	 * Sets up the system to use the language setup
	 * set in the MedSummary applications preferences.
	 */
	public static void setupMSPrefLanguage()
	{
		String setLang = mVDH.getUserStringPreference(MedSummaryUserProperties.LANGUAGE_PROPERTY,

			null, MedSummaryUserProperties.class);

		if (setLang != null)
		{
			try
			{
				mVDH.changeLanguage(setLang);
			}
			catch (LanguageException e)
			{
				System.err.print("Could not load specified language ");

				System.err.print("(" + setLang + ") - will use default ");

				System.err.print("language instead.");
			}
		}
	}


	// MAIN METHOD

	/**
	 * Starts the MedSummary application.
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		setupMSTextGeneration();

		setupMSPrefLookAndFeel();

		setupMSPrefLanguage();

		startMedSummary();
	}
}
