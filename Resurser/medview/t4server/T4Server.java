/*
 * @(#)T4Server.java
 *
 * $Id: T4Server.java,v 1.11 2008/09/01 11:07:00 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

 package medview.t4server;

import java.io.*;

import java.net.*;

import javax.swing.*;

import javax.xml.parsers.*;

import medview.datahandling.*;

import medview.medrecords.data.*;
import medview.medrecords.MedRecords;

import medview.medsummary.*;
import medview.common.data.DataComponentPackageUtilities;
import medview.common.data.DataComponentPackage;

import org.xml.sax.*;

import org.w3c.dom.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class T4Server
{
	// CLIENT HANDLING

	protected void handleClientConnect(Socket socket)
	{
		if (DEBUG_MODE) {
			System.out.println(OUTPUT_PREFIX + "Client connected");
		}

		try
		{
			if (DEBUG_MODE) {
				System.out.println(OUTPUT_PREFIX + "Building XML DOM tree");
			}
			
			Document document = documentBuilder.parse(socket.getInputStream());

			// ACTIVATE FOR DEBUGGING ->
			if (DEBUG_MODE) {
				printXML(document, -1);

				System.out.println();
			}
			// <------------------------

			socket.close();

			Element personNode = (Element) document.getElementsByTagName(PERSON_TAG).item(0);

			String pidString = personNode.getAttribute(PID_ATTRIBUTE);

			String pCodeString = mVDH.obtainPCode(pidString);

			PatientIdentifier pid = new PatientIdentifier(pCodeString, mVDH.normalizePID(pidString));

			if (!MedSummary.frameHasBeenCreated())
			{
				MedSummary.setupMSPrefLookAndFeel();

				MedSummary.setupMSPrefLanguage();
			}
            
            if (PreferencesModel.instance().getStartMedSummary())
            {
                //if want to start both medrecords and medsummary
                MedSummary.startMedSummary(pid, true);
            }
            else
            {
                //if only want to start medrecords
                MedRecords.startMedRecords(pid,null,false);
            }
                        

        }
		catch (SAXException exc)
		{
			exc.printStackTrace();

			JOptionPane.showMessageDialog(null, exc.getMessage(), "Critical error - could not parse xml",

				JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
		catch (InvalidRawPIDException exc)
		{
			exc.printStackTrace();

			JOptionPane.showMessageDialog(null, exc.getMessage(), "Critical error - invalid raw PID",

				JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
		catch (CouldNotGeneratePCodeException exc)
		{
			exc.printStackTrace();

			JOptionPane.showMessageDialog(null, exc.getMessage(), "Critical error - Lock file is not set or is invalid - exiting",

				JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			JOptionPane.showMessageDialog(null, exc.getMessage(), "Critical error - I/O error",

				JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
	}


	// DEBUG METHOD

	private void printXML(Node node, int offs)
	{
		if (offs != -1)
		{
			for (int ctr = 0; ctr < offs; ctr++)
			{
				System.out.print(" ");
			}
		}

		if (node.hasAttributes())
		{
			System.out.print("<" + node.getNodeName());

			NamedNodeMap attrMap = node.getAttributes();

			for (int ctr=0; ctr<attrMap.getLength(); ctr++)
			{
				if (ctr == 0)
				{
					System.out.print(" " + attrMap.item(ctr).getNodeName() +

						"=\"" + attrMap.item(ctr).getNodeValue() + "\"");
				}
				else if (ctr == attrMap.getLength() - 1)
				{
					if (offs != -1)
					{
						System.out.println(", " + attrMap.item(ctr).getNodeName() +

							"=\"" + attrMap.item(ctr).getNodeValue() + "\">");
					}
					else
					{
						System.out.print(", " + attrMap.item(ctr).getNodeName() +

							"=\"" + attrMap.item(ctr).getNodeValue() + "\">");
					}
				}
				else
				{
					System.out.print(", " + attrMap.item(ctr).getNodeName() +

						"=\"" + attrMap.item(ctr).getNodeValue() + "\"");
				}
			}
		}
		else
		{
			if (offs != -1)
			{
				System.out.println("<" + node.getNodeName() + ">");
			}
			else
			{
				System.out.print("<" + node.getNodeName() + ">");
			}
		}

		NodeList nodeList = node.getChildNodes();

		for (int ctr=0; ctr<nodeList.getLength(); ctr++)
		{
			if (offs != -1)
			{
				printXML(nodeList.item(ctr), offs + 2);
			}
			else
			{
				printXML(nodeList.item(ctr), -1);
			}
		}
	}


	// CONSTRUCTOR

	public T4Server()
	{
		// verify that necessary data properties are set

		if (!PreferencesModel.instance().isPCodeNRGeneratorLocationSet())
		{
			JOptionPane.showMessageDialog(null, "Lock file is not set or is invalid - exiting",

				"Critical error", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
		else
		{
			mVDH.setPCodeNRGeneratorLocation(PreferencesModel.instance().getPCodeNRGeneratorLocation());
		}

		if (!mVDH.isUserIDSet())
		{
			JOptionPane.showMessageDialog(null, "User ID must be set - exiting",

				"Critical error", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}

		if (PreferencesModel.instance().usesRemoteDataHandling())
		{
			if (!PreferencesModel.instance().isServerLocationSet())
			{
				JOptionPane.showMessageDialog(null, "Database location must be set - exiting",

					"Critical error", JOptionPane.ERROR_MESSAGE);

				System.exit(1);
			}
			else
			{
				mVDH.setExaminationDataLocation(PreferencesModel.instance().getServerLocation());
			}
		}
		else
		{
            //set database location to the one in the current package
            if (mVDH.isUserPreferenceSet(PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY, PreferencesModel.class))
            {
                String currentPackageName = mVDH.getUserStringPreference(
                        PreferencesModel.LAST_DATA_COMPONENT_PACKAGE_PROPERTY, null, PreferencesModel.class);
                
                DataComponentPackage[] allIncludedPackages = DataComponentPackageUtilities.obtainIncludedPackages(PreferencesModel.class);
                
                if (currentPackageName != null) {
                    for (int ctr = 0; ctr < allIncludedPackages.length; ctr++) {
                        if (allIncludedPackages[ctr].getPackageName().equals(currentPackageName))
                        {
                            mVDH.setExaminationDataLocation(allIncludedPackages[ctr].getDatabaseLocation());
                            break;
                        }
                    }
                }

            }
            else
            {
             JOptionPane.showMessageDialog(null, "Package is not set - exiting",

				"Critical error", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
            }
            
            
            /*if (!PreferencesModel.instance().isLocalDatabaseLocationSet())
			{
				JOptionPane.showMessageDialog(null, "Database location must be set - exiting",

					"Critical error", JOptionPane.ERROR_MESSAGE);

				System.exit(1);
			}
			else
			{
				mVDH.setExaminationDataLocation(PreferencesModel.instance().getLocalDatabaseLocation());
			}*/
		}

		// initialize XML handling

		documentBuilderFactory = DocumentBuilderFactory.newInstance();

		try
		{
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		}
		catch (Exception exc)
		{
			System.err.println(OUTPUT_PREFIX + "Critical error - could not create document builder");

			exc.printStackTrace();

			System.exit(1);
		}

		// set up text generation system

		System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");

		System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");

		System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");

		System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");

		// listen to socket

		try
		{
			ServerSocket serverSocket = new ServerSocket(port);

			System.out.println(OUTPUT_PREFIX + "Listening for connections on port " + port);

			while (true)
			{
				handleClientConnect(serverSocket.accept());

				if (DEBUG_MODE) {
					System.out.println(OUTPUT_PREFIX + "Listening for connections on port " + port);
				}
			}
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			System.exit(1);
		}
	}

	// OBJECT MEMBERS

	private DocumentBuilder documentBuilder;

	private DocumentBuilderFactory documentBuilderFactory;

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	// STATIC MEMBERS

	private static boolean DEBUG_MODE = true;	// whether debug messages are printed or not
	
	private static int port; // initialized in main()

	private static final int DEFAULT_PORT = 50000;

	private static final String PERSON_TAG = "Person";

	private static final String PID_ATTRIBUTE = "Personal_Number";

	private static final String OUTPUT_PREFIX = "T4Server> ";


	// STATIC MAIN METHOD

	public static void main(String[] args)
	{
		if (args.length == 1)
		{
			try
			{
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException exc)
			{
				exc.printStackTrace();

				System.exit(1);
			}
		}
		else
		{
			port = DEFAULT_PORT;
		}

		new T4Server();
	}
}
