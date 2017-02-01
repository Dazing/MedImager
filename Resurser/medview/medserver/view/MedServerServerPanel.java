package medview.medserver.view;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.io.*;

import java.text.*;

import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import medview.common.components.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medserver.model.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class MedServerServerPanel extends JPanel implements
	MedViewLanguageConstants
{
	public void ensureLastLineVisible()
	{
		try
		{
			serverTrafficArea.scrollRectToVisible(serverTrafficArea.modelToView(

				serverTrafficArea.getDocument().getEndPosition().getOffset() - 1));
		}
		catch (BadLocationException exc)
		{
			exc.printStackTrace();
		}
	}

	private void initializeComponents()
	{
		// server name to clients

		serverNameLabel = new JLabel(mVDH.getLanguageString(LABEL_SERVER_NAME_TO_CLIENTS_LS_PROPERTY));

		serverNameField = new JTextField(medServerModel.getServerNameToClients());

		serverNameField.setEditable(false);

		serverNameField.setBackground(Color.white);

		changeServerNameAction = new ChangeServerNameAction();

		serverNameMoreButton = new MedViewMoreButton(changeServerNameAction);


		// server traffic panel

		serverTrafficPanel = new JPanel();

		serverTrafficArea = new JTextArea();

		serverTrafficArea.setEditable(false);

		serverTrafficArea.setWrapStyleWord(false);

		serverTrafficArea.setLineWrap(true);

		Font font = UIManager.getFont("TextArea.font");

		String fontName = font.getName();

		int fontStyle = font.getStyle();

		int fontSize = font.getSize();

		serverTrafficArea.setFont(new Font(fontName, fontStyle, fontSize-1));
	}

	private void layoutPanel()
	{
		setLayout(new GridBagLayout());

		int west = GridBagConstraints.WEST;

		int east = GridBagConstraints.EAST;

		int none = GridBagConstraints.NONE;

		int both = GridBagConstraints.BOTH;

		final int cgs = GUIConstants.COMPONENT_GROUP_SPACING;

		final int ccs = GUIConstants.COMPONENT_COMPONENT_SPACING;

		serverTrafficPanel.setLayout(new BorderLayout());

		String titLS = TITLE_SERVER_TRAFFIC_LS_PROPERTY;

		String title = mVDH.getLanguageString(titLS);

		Insets i = new Insets(cgs, cgs, cgs, cgs);

		GUIUtilities.attachProperTitledBorder(serverTrafficPanel, title, i);

		serverTrafficPanel.add(new JScrollPane(serverTrafficArea), BorderLayout.CENTER);

		add(serverNameLabel, this, 			0, 0, 1, 1, 0, 0, east, none, new Insets(cgs,cgs,ccs,ccs));

		add(serverNameField, this, 			1, 0, 1, 1, 1, 0, east, both, new Insets(cgs,0,ccs,ccs));

		add(serverNameMoreButton, this, 	2, 0, 1, 1, 0, 0, east, both, new Insets(cgs,0,ccs,cgs));

		add(serverTrafficPanel, this, 		0, 1, 3, 1, 0, 1, west, both, new Insets(cgs,cgs,cgs,cgs));
	}

	private void add(Component comp, JPanel panel, int x, int y, int gw, int gh, int wx,
		int wy, int anchor, int fill, Insets insets)
	{
		gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = gw; gbc.gridheight = gh;

		gbc.weightx = wx; gbc.weighty = wy; gbc.fill = fill; gbc.anchor = anchor;

		gbc.insets = insets; panel.add(comp, gbc);
	}

	private void attachListeners()
	{
		medServerModel.addMedServerCommunicationListener(commListener);
	}

	public MedServerServerPanel(MedServerFrame medServerFrame)
	{
		this.medServerFrame = medServerFrame;

		this.medServerModel = medServerFrame.getModel();

		mVDH = MedViewDataHandler.instance();

		mVD = MedViewDialogs.instance();

		initializeComponents();

		layoutPanel();

		attachListeners();
	}

	private MedViewDialogs mVD;

	private MedViewDataHandler mVDH;

	private MedServerFrame medServerFrame;

	private MedServerModel medServerModel;

	private JPanel serverTrafficPanel;

	private JTextArea serverTrafficArea;

	private JLabel serverNameLabel;

	private JTextField serverNameField;

	private JButton serverNameMoreButton;

	private Action changeServerNameAction;

	private GridBagConstraints gbc = new GridBagConstraints();

	private CommunicationListener commListener = new CommunicationListener();





// -------------------------------------------------------------------------------
// ******************************** INNER CLASSES ********************************
// -------------------------------------------------------------------------------

	private class ChangeServerNameAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String curr = medServerModel.getServerNameToClients();

			String descLS = LABEL_CHANGE_SERVER_NAME_DESCRIPTION_LS_PROPERTY;

			String desc = mVDH.getLanguageString(descLS);

			String name = mVD.createAndShowChangeNameDialog(medServerFrame, curr, desc, 15);

			if (name != null) // dialog is dismissed if null
			{
				medServerModel.setServerNameToClients(name);

				serverNameField.setText(name);
			}
		}
	}


	private class CommunicationListener implements MedServerCommunicationListener
	{
		// examination related

		public void examinationAdded(MedServerCommunicationEvent e)
		{
			log(e, "Examination added to patient '" + e.getPatient() + "'", false);
		}

		public void examinationUpdated(MedServerCommunicationEvent e)
		{
			log(e, "Examination updated '" + e.getExamination() + "'", false);
		}

		public void patientListRequested(MedServerCommunicationEvent e)
		{
			log(e, "Patient list requested", false);
		}

		public void examinationCountRequested(MedServerCommunicationEvent e)
		{
			log(e, "Examination count requested", false);
		}

		public void examinationListRequested(MedServerCommunicationEvent e)
		{
			log(e, "Examinations requested for patient '" + e.getPatient() + "'", false);
		}

		public void allExaminationValueContainersRequested(MedServerCommunicationEvent e)
		{
			log(e, "All examination value containers requested", false);
		}

		public void examinationValueContainerRequested(MedServerCommunicationEvent e)
		{
			log(e, "Values requested for examination '" + e.getExamination() + "'", false);
		}

		public void imagesRequested(MedServerCommunicationEvent e)
		{
			log(e, "Images requested for examination '" + e.getExamination() + "'", false);
		}

		public void refreshPerformed(MedServerCommunicationEvent e)
		{
			log(e, "Refresh performed", false);
		}

		public void imageCountRequested(MedServerCommunicationEvent e)
		{
			// not of interest atm
		}


		// term related (flag indicates if -> special term log file)

		public void termExistanceQueried(MedServerCommunicationEvent e)
		{
			log (e, "Term existance queried for term '" + e.getTerm() + "'", false);
		}

		public void termListRequested(MedServerCommunicationEvent e)
		{
			log(e, "Term list requested", false);
		}

		public void termHashMapRequested(MedServerCommunicationEvent e)
		{
			log(e, "Term hash map requested", false);
		}

		public void termAdded(MedServerCommunicationEvent e)
		{
			log(e, "Term '" + e.getTerm() + "' added", true);
		}

		public void termRemoved(MedServerCommunicationEvent e)
		{
			log(e, "Term '" + e.getTerm() + "' removed", true);
		}

		public void valueAdded(MedServerCommunicationEvent e)
		{
			String term = e.getTerm();

			Object val = e.getValue();

			log(e, "Value '" + val + "' added to term '" + term + "'", true);
		}

		public void valueRemoved(MedServerCommunicationEvent e)
		{
			String term = e.getTerm();

			Object val = e.getValue();

			log(e, "Value '" + val + "' removed from term '" + term + "'", true);
		}

		public void valuesRequested(MedServerCommunicationEvent e)
		{
			// not of interest atm
		}

		public void typeRequested(MedServerCommunicationEvent e)
		{
			// not of interest atm
		}


		// server related

		public void serverActivated(MedServerCommunicationEvent e)
		{
			log(e, "Server activated", false);
		}

		public void serverDeactivated(MedServerCommunicationEvent e)
		{
			log(e, "Server deactivated", false);
		}


		// pcode generation

		public void pCodeRequested(MedServerCommunicationEvent e)
		{
			log(e, "PCode requested for '" + e.getPID() + "', returned '" +

				e.getPCode() + "'", false);
		}


		// utility methods

		private void log(MedServerCommunicationEvent e, String text, boolean toTerm)
		{
			String client = e.getClientHost();

			String app = "(" + formatter.format(new Date());

			String withClient = app + ", " + client + ") " + text + "\n";

			String withoutClient = app + ") " + text + "\n";

			serverTrafficArea.append(withoutClient);

			if (serverTrafficArea.getLineCount() > 300)
			{
				try
				{
					Document doc = serverTrafficArea.getDocument();

					doc.remove(0, serverTrafficArea.getLineEndOffset(0));
				}
				catch (BadLocationException exc)
				{
					exc.printStackTrace(); // this should never happen
				}
			}

			try
			{
				medServerModel.appendToLog(withClient);

				if (toTerm)
				{
					medServerModel.appendToTermLog(withClient);
				}
			}
			catch (IOException exc)
			{
				mVD.createAndShowErrorDialog(medServerFrame, exc.getMessage());
			}

			ensureLastLineVisible();
		}

		// members

		private int dS = DateFormat.SHORT;

		private int tS = DateFormat.SHORT;

		private DateFormat formatter = DateFormat.getDateTimeInstance(dS, tS);
	}

// -------------------------------------------------------------------------------
// *******************************************************************************
// -------------------------------------------------------------------------------
}
