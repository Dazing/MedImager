package medview.medimager.view.settings;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;

import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.common.filefilter.*;

import medview.datahandling.*;

import medview.medimager.model.*; // for unit test only
import medview.medimager.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedImagerJournalSCP extends SettingsContentPanel implements

	MedViewLanguageConstants, GUIConstants
{
	// MEMBERS

	private JLabel templateLabel;

	private JLabel translatorLabel;

	private JTextField templateTextField;

	private JTextField translatorTextField;

	private JButton templateMoreButton;

	private JButton translatorMoreButton;

	private MedImagerFrame frame;


	// CONSTRUCTOR(S) AND RELATED

	public MedImagerJournalSCP(CommandQueue commandQueue, MedImagerFrame frame)
	{
		super(commandQueue, new Object[] { frame });
	}

	protected void initSubMembers()
	{
		frame = (MedImagerFrame) subConstructorData[0];
	}

	protected void createComponents()
	{
		// template

		templateLabel = new JLabel("Aktuell mall:");

		templateTextField = new JTextField();

		templateTextField.setEditable(false);

		templateMoreButton = new MedViewMoreButton(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				File chosenFile = MedViewDialogs.instance().createAndShowChooseFileDialog(frame, new DialogXMLFileFilter());

				if (chosenFile != null)
				{
					templateTextField.setText(chosenFile.getPath());

					commandQueue.addToQueue(new ChangeTemplateLocationCommand(chosenFile.getPath()));
				}
			}
		});

		// translator

		translatorLabel = new JLabel("Aktuell översättare:");

		translatorTextField = new JTextField();

		translatorTextField.setEditable(false);

		translatorMoreButton = new MedViewMoreButton(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				File chosenFile = MedViewDialogs.instance().createAndShowChooseFileDialog(frame, new DialogXMLFileFilter());

				if (chosenFile != null)
				{
					translatorTextField.setText(chosenFile.getPath());

					commandQueue.addToQueue(new ChangeTranslatorLocationCommand(chosenFile.getPath()));
				}
			}
		});
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, templateLabel, 		0, 0, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, templateTextField, 	1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, templateMoreButton, 	2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,0), BOTH);

		GUIUtilities.gridBagAdd(this, translatorLabel, 		0, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, translatorTextField, 	1, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, translatorMoreButton, 	2, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(), 	0, 2, 3, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}


	// TAB DESCRIPTION

	public String getTabLS()
	{
		return TAB_MEDIMAGER_JOURNAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDIMAGER_JOURNAL_DESCRIPTION_LS_PROPERTY;
	}


	// SETTINGS SHOWN / HIDDEN

	protected void settingsShown()
	{
		templateTextField.setText(frame.getModel().getJournalTemplateLocation());

		translatorTextField.setText(frame.getModel().getJournalTranslatorLocation());
	}

	protected void settingsHidden()
	{
	}


	private class ChangeTemplateLocationCommand implements Command
	{
		public void execute()
		{
			try
			{
				frame.getModel().setJournalTemplate(location);
			}
			catch (CouldNotSetJournalTemplateException exc)
			{
				MedViewDialogs.instance().createAndShowErrorDialog(frame, exc.getMessage());
			}
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeTemplateLocationCommand);
		}

		public ChangeTemplateLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}

	private class ChangeTranslatorLocationCommand implements Command
	{
		public void execute()
		{
			try
			{
				frame.getModel().setJournalTranslator(location);
			}
			catch (CouldNotSetJournalTranslatorException exc)
			{
				MedViewDialogs.instance().createAndShowErrorDialog(frame, exc.getMessage());
			}
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeTranslatorLocationCommand);
		}

		public ChangeTranslatorLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}


	// UNIT TEST

	public static void main(String[] args)
	{
	}
}
