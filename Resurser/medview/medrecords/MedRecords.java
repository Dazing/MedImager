/*
 * $Log: MedRecords.java,v $
 * Revision 1.59  2010/07/01 09:26:17  oloft
 * Changed looks-import to support version 2.1.3
 *
 * Revision 1.58  2010/07/01 07:39:42  oloft
 * MR 4.5, minor edits
 *
 * Revision 1.57  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.56  2008/07/28 06:56:51  it2aran
 * * Package now includes
 * 	termdefinitions
 * 	termvalues
 * 	database
 * 	template
 * 	translator
 * and can be changed withour restarting (both in MSummary and MRecords
 * * removed more termdefinitions checks (the bug that slowed down MRecords) in MedSummary which should make it load faster
 *
 * Revision 1.55  2008/04/24 10:07:22  it2aran
 * My CVS client is strange, so a lot of files are reported as changed in docgen, when they aren't
 *
 * Revision 1.54  2008/01/31 13:23:26  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.53  2007/10/17 15:13:45  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.52  2006/11/15 16:26:03  oloft
 * Moved mAdded graph code previously in MedRecordsFrame to enable calling graph from MedSummary
 *
 * Revision 1.51  2006/09/13 22:00:05  oloft
 * Added Open functionality
 *
 * Revision 1.50  2006/04/24 14:17:46  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.49  2005/07/12 09:39:21  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.48  2005/03/16 13:56:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.47  2005/02/24 16:33:29  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.46  2005/02/17 10:07:19  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.45  2005/01/30 15:23:27  lindahlf
 * T4 Integration
 *
 * Revision 1.44  2004/12/08 14:43:03  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.43  2004/11/19 12:32:32  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.42  2004/11/09 21:14:29  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.41  2004/11/04 12:04:50  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.40  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.39  2004/05/18 18:21:05  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.38  2004/03/08 23:58:28  lindahlf
 * no message
 *
 * Revision 1.37  2004/02/28 17:51:26  lindahlf
 * no message
 *
 * Revision 1.36  2004/02/26 12:14:05  lindahlf
 * Added Cache support to MVDHandler, and proper patient id support when invoking MR from MS
 *
 * Revision 1.35  2004/02/19 18:21:27  lindahlf
 * Major update patch 1
 *
 * Revision 1.34  2004/01/27 15:43:27  oloft
 * PID introduction
 *
 * Revision 1.33  2004/01/11 22:27:54  oloft
 * version number
 *
 * Revision 1.32  2004/01/06 22:06:50  oloft
 * Minor fixes
 *
 * Revision 1.31  2003/12/29 22:03:55  oloft
 * Better prefs listener
 *
 * Revision 1.30  2003/12/29 15:19:02  oloft
 * transfer
 *
 * Revision 1.29  2003/12/29 00:39:56  oloft
 * Prefs title
 *
 * Revision 1.28  2003/12/27 11:25:01  oloft
 * transfer
 *
 * Revision 1.27  2003/12/22 14:51:42  oloft
 * Does not exit when called from MS
 *
 * Revision 1.26  2003/12/22 14:42:04  oloft
 * changed ms api
 *
 * Revision 1.25  2003/12/21 21:51:13  oloft
 * Revamped settings
 *
 * Revision 1.24  2003/11/11 13:17:08  oloft
 * Switching mainbranch
 *
 * Revision 1.23.2.11  2003/11/11 13:00:13  oloft
 * *** empty log message ***
 *
 * Revision 1.23.2.10  2003/10/26 21:22:59  oloft
 * Debug edits only
 *
 * Revision 1.23.2.9  2003/10/26 15:26:21  oloft
 * Minor refactoring
 *
 * Revision 1.23.2.8  2003/10/24 21:00:33  oloft
 * Reuses the same model fpr new windows
 *
 * Revision 1.23.2.7  2003/10/23 11:35:41  oloft
 * Enabled multi-document mode
 *
 * Revision 1.23.2.6  2003/10/21 23:05:55  oloft
 * Some refactoring, added newDocument method
 *
 * Revision 1.23.2.5  2003/10/06 09:16:24  oloft
 * interim
 *
 * Revision 1.23.2.4  2003/10/03 15:50:53  oloft
 * Added language handling code
 *
 * Revision 1.23.2.3  2003/10/03 11:56:59  oloft
 * *** empty log message ***
 *
 * Revision 1.23.2.2  2003/09/09 13:52:41  erichson
 * Outputs message on stdout when starting with p_code as argument
 *
 * Revision 1.23.2.1  2003/09/08 13:24:02  erichson
 * Commented out some obsolete configfile handling
 *
 * Revision 1.23  2003/07/23 14:33:17  erichson
 * changed starting clearExamination call to newExamination instead
 *
 */

package medview.medrecords;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.*;

import misc.domain.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.common.filefilter.*;

import medview.medrecords.components.*;
import medview.medrecords.components.graph.*;
import medview.medrecords.components.settings.*;
import medview.medrecords.models.*;
import medview.medrecords.data.*;
import medview.medrecords.data.graph.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Many
 * @version 1321423
 */
public class MedRecords implements MedViewLanguageConstants
{
	// START AND EXIT THE APPLICATION

	/**
	 * Starts a MedRecords application frame and shows
	 * the splash screen while loading it. The first
	 * call to this method will determine whether or
	 * not the application owns the JVM (i.e. if it is
	 * allowed to exit). Subsequent calls will assume
	 * the first call's ownership. Subsequent calls are
	 * effectively the same as calls to newDocument()
	 * but with the addition that a splash screen is
	 * shown.
	 * @param pid PatientIdentifier
	 * @param ownerComp Component
	 * @param isOwner boolean
	 */
	public static void startMedRecords(final PatientIdentifier pid, final Component ownerComp, boolean isOwner)
	{
		// the following block is performed once --->

		if (!ownerFinalized)
		{
			blockExit = !isOwner;

			if (isOwner)
			{
				synchronizeVisualPreferences(); // set up look-and-feel and language

				synchronizeDataPreferences();

				setupMRTextGeneration();
			}

			ownerFinalized = true;
		}

		// <-----------------------------------------

		splashWindow.show(); // splash shown on EDT

		Thread t = new Thread(new Runnable()
		{
			public void run() // document constructed on non-EDT thread so it wont hog the EDT and prevent painting
			{
				 newDocument(pid, ownerComp);
			}
		});

		t.start();
	}

	/**
	 * Returns whether the application is allowed to exit (i.e.
	 * after showing various save? dialogs etc. whether or not
	 * it is ok to exit). It will first ask each of the open
	 * documents if it is ok to exit. If all documents are ok
	 * with exiting, the actual closing of the documents will
	 * take place. If the MedRecords application is the owner
	 * of the JVM, it will then exit after having notified the
	 * data layer of shutdown.
	 * @return boolean whether or not MedRecords is ok with exiting,
	 * i.e. if everything is saved (or not saved if the user didn't
	 * want to save it).
	 */
	public static boolean exitApplication()
	{
		Enumeration enm = documents.elements();

		Vector removeVector = new Vector(documents.size());

		while (enm.hasMoreElements())
		{
			MedRecordsFrame mF = (MedRecordsFrame) enm.nextElement();

			if (!mF.canCloseDocument()) // asks the user if he wants to close
			{
				return false; // if one results in false -> cancel exit	(restore state)
			}
			else
			{
				removeVector.add(mF);
			}
		}

		enm = removeVector.elements();

		while (enm.hasMoreElements())
		{
			closeDocument((MedRecordsFrame)enm.nextElement()); // removes document from document vector and disposes it
		}

		if (!blockExit)
		{
			 // medrecords was not started from other application - can perform jvm termination

			mVDH.shuttingDown();

			System.exit(0);
		}

		return true; // ok to exit (needed in case we've blocked exit)
	}


	// DATA SYNCHRONIZATION METHODS

	/**
	 * Sets the following according to the
	 * preferences set in MedRecords:
	 *
	 * * Look-and-feel
	 * * Language
	 */
	private static void synchronizeVisualPreferences()
	{
		syncLookAndFeelWithPrefs();

		syncLanguageWithPrefs();
	}

	/**
	 * Sets the following according to the
	 * preferences set in MedRecords:
	 *
	 * * Term datahandling settings
	 * * Examination datahandling settings
	 * * PCode number generator settings
	 */
	private static void synchronizeDataPreferences()
	{
		syncTermDataHandlerWithPrefs();

		syncExaminationDataHandlerWithPrefs();

		syncPCodeGeneratorWithPrefs();
	}

	/**
	 * Makes sure that after the call, the
	 * look-and-feel matches the one (if any)
	 * set in the preferences of MedRecords.
	 */
	private static void syncLookAndFeelWithPrefs()
	{
		// look-and-feel setup

		//PlasticLookAndFeel.setMyCurrentTheme(new DesertBlue()); <- to fix Java 5 look-and-feel bug
		//PlasticLookAndFeel.setPlasticTheme(new DesertBlue()); // New try after upgrade, but seems to make app revert to old style metal
		
		UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

		// look-and-feel instantiation

		try
		{
			try
			{
				String userLAFClass = prefs.getLookAndFeel();

				if (userLAFClass != null)
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

	/**
	 * Makes sure that after the call, the
	 * language matches the one (if any) set
	 * in the preferences of MedRecords.
	 */
	private static void syncLanguageWithPrefs()
	{
		String setLang = prefs.getSelectedLanguage();

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

	/**
	 * Makes sure that after the call, the term
	 * datahandling settings matches the ones (if
	 * any) set in the preferences of MedRecords.
	 */
	private static void syncTermDataHandlerWithPrefs()
	{
		if(prefs.usesRemoteDataHandling())
		{
			if(!mVDH.getTermDataHandlerInUse().equalsIgnoreCase(REMOTE_TERM_DATAHANDLER_CLASS))
			{
				try
				{
					mVDH.setTermDataHandlerToUse(REMOTE_TERM_DATAHANDLER_CLASS, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (prefs.isServerLocationSet())
			{
				mVDH.setTermDefinitionLocation(prefs.getServerLocation()); // fires term location change event

				mVDH.setTermValueLocation(prefs.getServerLocation()); // fires term location change event
			}
		}
		else
		{
            //do nothing since we handle local database settings in package
            /*if (!mVDH.getTermDataHandlerInUse().equalsIgnoreCase(mVDH.getDefaultTermDataHandler()))
			{
				try
				{
					mVDH.setTermDataHandlerToUse(mVDH.getDefaultTermDataHandler(), true);
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

            //if (prefs.isTermDefinitionLocationSet())
			{
				mVDH.setTermDefinitionLocation(prefs.getTermDefinitionLocation()); // fires term location change event
			}

			//if (prefs.isTermValueLocationSet())
			{
				mVDH.setTermValueLocation(prefs.getTermValueLocation()); // fires term location change event
			}*/
		}
	}

	/**
	 * Makes sure that after the call, the examination
	 * datahandling settings matches the ones (if any)
	 * set in the preferences of MedRecords.
	 */
	private static void syncExaminationDataHandlerWithPrefs()
	{
		if (prefs.usesRemoteDataHandling())
		{
			if (!mVDH.getExaminationDataHandlerInUse().equalsIgnoreCase(REMOTE_EXAMINATION_DATAHANDLER_CLASS))
			{
				try
				{
					mVDH.setExaminationDataHandlerToUse(REMOTE_EXAMINATION_DATAHANDLER_CLASS, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (prefs.isServerLocationSet())
			{
				mVDH.setExaminationDataLocation(prefs.getServerLocation()); // will fire loc change event
			}
		}
		else
		{
            //do nothing since we handle local database settings in package
            /*
            if (!mVDH.getExaminationDataHandlerInUse().equalsIgnoreCase(mVDH.getDefaultExaminationDataHandler()))
			{
				try
				{
					mVDH.setExaminationDataHandlerToUse(mVDH.getDefaultExaminationDataHandler(), true);
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (prefs.isLocalDatabaseLocationSet())
			{
				mVDH.setExaminationDataLocation(prefs.getLocalDatabaseLocation()); // fires examination data location change event
			}
            */
        }
	}

	/**
	 * Makes sure that after the call, the pcode generator
	 * settings matches the ones (if any) set in the
	 * preferences of MedRecords.
	 */
	private static void syncPCodeGeneratorWithPrefs()
	{
		if (prefs.usesRemoteDataHandling())
		{
			if (!mVDH.getPCodeGeneratorInUse().equalsIgnoreCase(REMOTE_PCODE_GENERATOR_CLASS))
			{
				try
				{
					mVDH.setPCodeGeneratorToUse(REMOTE_PCODE_GENERATOR_CLASS); // will indirectly set examination data location
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}
		}
		else
		{
			if (!mVDH.getPCodeGeneratorInUse().equalsIgnoreCase(mVDH.getDefaultPCodeGenerator()))
			{
				try
				{
					mVDH.setPCodeGeneratorToUse(mVDH.getDefaultPCodeGenerator());
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (prefs.isPCodeNRGeneratorLocationSet())
			{
				mVDH.setPCodeNRGeneratorLocation(prefs.getPCodeNRGeneratorLocation()); // fires pcode nr gen location change event
			}
		}
	}

	public static void setupMRTextGeneration()
	{
		System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");

		System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");

		System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");

		System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");
	}


	// CREATE AND CLOSE DOCUMENTS (i.e. MedRecordsFrame instances)

	/**
	 * Constructs a new document, pid and ownerComponent can be null.
	 * @param pid PatientIdentifier
	 * @param ownerComponent Component
	 */
	public static void newDocument(PatientIdentifier pid, Component ownerComponent)
	{
		createNewDocument(pid, ownerComponent);
	}

	public static void openDocument(){		
		//String mvdPath = prefs.getLocalDatabaseLocation();
		
		// This path should be used in the dialog, but isn't yet
		//System.out.println(mvdPath);
				
		File treeFile = MedViewDialogs.instance().createAndShowChooseFileDialog(null, new DialogTreeFileFilter());
		
		if (treeFile != null) {
		MedRecordsFrame newDoc = createNewDocument(null, null);
			newDoc.loadExamination(treeFile);
		}
	}

	private static MedRecordsFrame createNewDocument(PatientIdentifier pid, Component ownerComponent) {
		// instantiate frame
		
		final MedRecordsFrame theFrame = ((pid != null) ?
										  
										  new MedRecordsFrame(getDefaultModel(), pid) :
										  
										  new MedRecordsFrame(getDefaultModel()));
		
		// attach window listener to listen for frame close events
		
		theFrame.addWindowListener(new WindowAdapter()
								   {
            @Override
			public void windowClosing(java.awt.event.WindowEvent evt)
		{
				closeDocument(theFrame); // will ask if save needed
		}
								   });
		
		// add frame to document vector
		
		documents.add(theFrame);
		
		// set frame location
		
		if (ownerComponent != null)
		{
			Point ownerLoc = ownerComponent.getLocation();
			
			theFrame.setLocation(ownerLoc.x + (documents.size() * MULTIPLE_DOCUMENT_OFFSET),
								 
								 ownerLoc.y + (documents.size() * MULTIPLE_DOCUMENT_OFFSET));
		}
		else
		{
			Point loc = theFrame.getLocation(); // constructor set location
			
			String prefSetLoc = mVDH.getUserStringPreference(PreferencesModel.MEDRECORDS_LOCATION_PROPERTY,
															 
															 null, PreferencesModel.class);
			
			if (prefSetLoc != null)
			{
				StringTokenizer t = new StringTokenizer(prefSetLoc, ",");
				
				int x = Integer.parseInt(t.nextToken());
				
				int y = Integer.parseInt(t.nextToken());
				
				loc = new Point(x,y);
			}
			
			theFrame.setLocation(loc.x + ((documents.size() - 1) * MULTIPLE_DOCUMENT_OFFSET),
								 
								 loc.y + ((documents.size() - 1) * MULTIPLE_DOCUMENT_OFFSET));
		}
		
		// show frame and close splash window
		
		SwingUtilities.invokeLater(new Runnable()
								   {
			public void run() // post-show swing component manipulation must be done on EDT
		{
				if (splashWindow.isShowing())
				{
					splashWindow.dispose(); // dispose splash when frame is created
				}
				
				theFrame.setVisible(true);
				
				theFrame.requestFocus();
		}
								   });
		return theFrame;
	}

	/**
	 * Closes the specified document.
	 * @param doc MedRecordsFrame
	 */
	public static void closeDocument(MedRecordsFrame doc)
	{
		if (doc.canCloseDocument())
		{
			documents.remove(doc);

			doc.dispose();

			if ((documents.isEmpty()) && !blockExit) // no more documents remain
			{
				mVDH.shuttingDown();

				System.exit(0);
			}
		}
	}


	// EXAMINATION MODEL

	private static void clearDefaultModel()
	{
		defaultModel = null;
	}

	private static ExaminationModel getDefaultModel()
	{
		if (defaultModel == null)
		{
			defaultModel = createEmptyForm();
		}

		return defaultModel;
	}

	public static ExaminationModel createEmptyForm()
	{
		ExaminationModel e = new ExaminationModel();

		CategoryModel c = new CategoryModel("Undefined");

		c.addInput(new FieldModel("Undefined", 1, null, "", "","Undefined"));

		e.addCategory(c);

		return e;
	}


	// PREFERENCES, ABOUT DIALOG AND OTHER DIALOG METHODS

	public static void showPreferences(MedRecordsFrame mf, String initialTabLS)
	{
		if (!settingsPanelsCreated)
		{
			CommandQueue queue = MedViewDialogs.instance().getSettingsCommandQueue();

			settingsPanelArray = new SettingsContentPanel[5];

			settingsPanelArray[0] = new MedRecordsVisualSCP(queue, getParentFrame());

			settingsPanelArray[1] = new MedRecordsDataHandlingSCP(queue, mf);

			settingsPanelArray[2] = new MedRecordsInputDataSCP(queue, getParentFrame());

			settingsPanelArray[3] = new MedRecordsExpertSCP(queue, mf);

			settingsPanelArray[4] = new MedRecordsDataComponentSCP(queue, getParentFrame());

			settingsPanelsCreated = true;
		}

		if (initialTabLS != null)
		{
			MedViewDialogs.instance().createAndShowSettingsDialog(mf, MedViewLanguageConstants.TITLE_PREFERENCES_LS_PROPERTY,

				settingsPanelArray, initialTabLS); // will set the initial tab to the specified one
		}
		else
		{
			MedViewDialogs.instance().createAndShowSettingsDialog(mf, MedViewLanguageConstants.TITLE_PREFERENCES_LS_PROPERTY,

				settingsPanelArray);
		}
	}

	public static Frame getParentFrame()
	{
		return ((Frame)documents.elementAt(0));
	}

	// Show graph for the specified examination
	public static void showGraphWin(ExaminationIdentifier id, Component ownerComponent) {
		try {
			ExaminationValueContainer evc = mVDH.getExaminationValueContainer(id);
			showGraphWin(evc, (Window)ownerComponent);
		}
		catch (NoSuchExaminationException e) {
		            String m = mVDH.getLanguageString(ERROR_WHILE_GRAPHING)
            + "\nNoSuchExaminationException:\n" + e.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog((Window)ownerComponent, m);
		}
		catch (IOException ioe) {             
            String m = mVDH.getLanguageString(ERROR_WHILE_GRAPHING)
            + ". I/O Error: \n" + ioe.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog((Window)ownerComponent, m);
        }
	}
	
	public static void showGraphWin(Tree t, Window ownerWindow) {		
		try {
			ExaminationValueContainer evc = new ExaminationValueTable(t); // -> IOException
			showGraphWin(evc, ownerWindow);
		} catch (IOException ioe) { // For example could not find the file, or read it...
            
            String m = mVDH.getLanguageString(ERROR_WHILE_GRAPHING)
            + ". I/O Error: \n" + ioe.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog(ownerWindow, m);
            showPreferences((MedRecordsFrame)ownerWindow, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
            
        }
	}
	
		
	public static void showGraphWin(ExaminationValueContainer evc, Window ownerWindow) {
		try {
			// Get graph info
			
			GraphXMLParser parser = new GraphXMLParser();
			
			String templateLocation = PreferencesModel.instance().getGraphTemplateLocation();
			
			GraphSet graphSet = parser.parse(new File(templateLocation));
			
			GraphBrowserComponent graphBrowser = new GraphBrowserComponent( graphSet, evc);
			
			showGraphFrame(graphBrowser, ownerWindow);
		} catch (javax.xml.parsers.ParserConfigurationException pce) {
		
            String m = mVDH.getLanguageString(ERROR_WHILE_PARSING_GRAPH_TEMPLATE)
            + "\nParserConfigurationException:\n" + pce.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog((Window)ownerWindow, m);
            showPreferences((MedRecordsFrame)ownerWindow, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
            
        } catch (org.xml.sax.SAXException e) {
		
            String m = mVDH.getLanguageString(ERROR_WHILE_PARSING_GRAPH_TEMPLATE)
            + "\nSAXException:\n" + e.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog(ownerWindow, m);
            MedRecords.showPreferences((MedRecordsFrame)ownerWindow, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
            
        } catch (InvalidTemplateException ite) {
		
            String m = mVDH.getLanguageString(ERROR_WHILE_PARSING_GRAPH_TEMPLATE)
            + "\nInvalid template [" + ite.getTemplateLocation() + "]: \n" + ite.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog(ownerWindow, m);
            showPreferences((MedRecordsFrame)ownerWindow, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
			
        }  catch (IOException ioe) { // For example could not find the file, or read it...
            
            String m = mVDH.getLanguageString(ERROR_WHILE_GRAPHING)
            + ". I/O Error: \n" + ioe.getMessage();
            
            MedViewDialogs.instance().createAndShowErrorDialog(ownerWindow, m);
            showPreferences((MedRecordsFrame)ownerWindow, TAB_MEDRECORDS_INPUT_LS_PROPERTY);
            
        }

		//catch (Exception e) {
		//	e.printStackTrace();
		//}
	}
	
private static void showGraphFrame(GraphBrowserComponent graphBrowser, Window ownerWindow) {
	graphFrame.setTitle(mVDH.getLanguageString(TITLE_GRAPH_VIEWER_LS_PROPERTY));
	graphFrame.setSize(PreferencesModel.instance().getGraphWindowSize());
	
	// Action copyClipboardAction = new ClipboardCopyAction(); // Todo: Clipboard functionality for this too?
	
	Container contentPane = graphFrame.getContentPane();
	contentPane.removeAll();
	contentPane.add(graphBrowser, BorderLayout.CENTER);
	
	graphFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	
	graphFrame.addWindowListener(new WindowAdapter() {
            @Override
		public void windowClosing(WindowEvent e) {
			Dimension graphDim = graphFrame.getSize();
			
			PreferencesModel.instance().setGraphWindowSize(graphDim);
			
			graphFrame.dispose();
		}
	});
	
	graphFrame.setLocationRelativeTo(ownerWindow);
	
	graphFrame.show();
}

	public static void showAboutWin()
	{
		String titLS = MedViewLanguageConstants.TITLE_ABOUT_MEDRECORDS_LS_PROPERTY;

		String txtLS = MedViewLanguageConstants.OTHER_ABOUT_MEDRECORDS_TEXT_LS_PROPERTY;

		MedViewDialogs.instance().createAndShowAboutDialog(getParentFrame(), titLS, "MedRecords",

			PreferencesModel.MEDRECORDS_VERSION_STRING, txtLS);
	}



	// CONSTRUCTOR

	private MedRecords() {} // defeat instantiation

	// MEMBERS

	private static boolean blockExit = false;

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private static PreferencesModel prefs = PreferencesModel.instance();

	private static final int MULTIPLE_DOCUMENT_OFFSET = 15; // in pixels

	private static SettingsContentPanel[] settingsPanelArray;

	private static boolean settingsPanelsCreated = false;

	private static Vector documents = new Vector();

	private static MedViewSplashWindow splashWindow;

	private static ExaminationModel defaultModel;

	private static boolean ownerFinalized = false;
	
	private static final JFrame graphFrame = new JFrame();

	private static final String REMOTE_TERM_DATAHANDLER_CLASS = "medview.datahandling.RemoteTermDataHandlerClient";

	private static final String REMOTE_EXAMINATION_DATAHANDLER_CLASS = "medview.datahandling.examination.RemoteExaminationDataHandlerClient";

	private static final String REMOTE_PCODE_GENERATOR_CLASS = "medview.datahandling.RemotePCodeGeneratorClient";

	static
	{
		// create splash window (shown in start method above)

		splashWindow = MedViewDialogs.instance().createSplashWindow(

			mVDH.getImage(MedViewMediaConstants.SPLASH_MEDRECORDS_IMAGE_ICON),

			OTHER_ABOUT_MEDRECORDS_TEXT_LS_PROPERTY,

			OTHER_LOADING_MEDRECORDS_TEXT_LS_PROPERTY,

			Color.white, MedViewSplashWindow.IMAGE_DEVELOPER_STYLE);

		// create and attach data-layer preference listener

		mVDH.addMedViewPreferenceListener(new MedViewPreferenceListener()
		{
			/* NOTE: this is a PREFERENCE listener, and not a DATA listener,
			   thus the preferences are set somewhere (usually in the settings
			   dialog) and this class, having the responsibility of syncing
			   the data layer with the settings in the preferences, will then
			   make sure that the data layer is set accordingly. */

			public void userPreferenceChanged(MedViewPreferenceEvent e)
			{
				String prefsKey = e.getPreferenceName();

				if (prefsKey.equals(PreferencesModel.LocalDatabaseLocation) ||

					prefsKey.equals(PreferencesModel.TermDefinitionLocation) ||

					prefsKey.equals(PreferencesModel.TermValuesLocation) ||

					prefsKey.equals(PreferencesModel.UseRemoteDataHandling) ||

					prefsKey.equals(PreferencesModel.ServerLocation))
				{
					synchronizeDataPreferences();
				}
				else if (prefsKey.equals(PreferencesModel.ImageCategoryName))
				{
					clearDefaultModel();
				}
			}

			public void systemPreferenceChanged(MedViewPreferenceEvent e) { }
		});
	}


	// MAIN METHOD

	public static void main(String[] args)
	{
		startMedRecords(null, null, true); // true -> owner
	}
}
