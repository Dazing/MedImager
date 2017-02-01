/*
 * @(#)LanguageAndLAFSettingsPanel.java
 *
 * $Id: LanguageAndLAFSettingsPanel.java,v 1.5 2006/04/24 14:16:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class LanguageAndLAFSettingsPanel extends JPanel implements
	MedViewLanguageConstants, GUIConstants
{
	/**
	 * This method should be called from the settings
	 * content panel containing this panel. It will
	 * set the selection status appropriately.
	 */
	public void settingsShown() // called when container dialog showing...
	{
		currentLAFCombo.setSelectedItem(UIManager.getLookAndFeel().getName());

		currentLanguageCombo.setSelectedItem(mVDH.getCurrentLanguageDescriptor());
	}

	protected void layoutPanel()
	{
		setLayout(new GridBagLayout());

		String langLS = LABEL_CURRENT_LANGUAGE_LS_PROPERTY;

		String lafLS = LABEL_CURRENT_PLATFORM_LS_PROPERTY;

		String langText = mVDH.getLanguageString(langLS);

		String lafText = mVDH.getLanguageString(lafLS);

		currentLanguageLabel = new JLabel(langText);

		currentLAFLabel = new JLabel(lafText);

		currentLAFCombo = new JComboBox();

		String[] langs = mVDH.getAvailableLanguages();

		currentLanguageCombo = new JComboBox(langs);

		String currLang = mVDH.getCurrentLanguageDescriptor();

		currentLanguageCombo.setSelectedItem(currLang);

		currentLanguageCombo.addItemListener(new LanguageComboListener());

		UIManager.LookAndFeelInfo[] lAFs = UIManager.getInstalledLookAndFeels();

		for (int ctr=0; ctr<lAFs.length; ctr++)
		{
			currentLAFCombo.addItem(lAFs[ctr].getName());
		}

		currentLAFCombo.setSelectedItem(UIManager.getLookAndFeel().getName());

		currentLAFCombo.addItemListener(new LAFComboListener());

		GUIUtilities.gridBagAdd(this, currentLanguageLabel, 	0, 0, 1, 1, 0, 0, EAST, new Insets(0,0,cGS,cCS), NONE);

		GUIUtilities.gridBagAdd(this, currentLanguageCombo, 	1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, currentLAFLabel,      	0, 1, 1, 1, 0, 0, EAST, new Insets(0,0,cGS,cCS), NONE);

		GUIUtilities.gridBagAdd(this, currentLAFCombo, 		1, 1, 1, 1, 0, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, new JSeparator(), 	0, 2, 2, 1, 0, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 3, 2, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

	/**
	 * Constructs a language and look-and-feel settings panel,
	 * which will place language and LAF changes onto the
	 * queue specified. When the commands are executed, the
	 * specified properties will be set.
	 */
	public LanguageAndLAFSettingsPanel(CommandQueue queue, String lafProp,
		String langProp, Class lafPropClass, Class langPropClass)
	{
		super();

		this.commandQueue = queue;

		this.lookAndFeelProperty = lafProp;

		this.languageProperty = langProp;

		this.lookAndFeelPropertyClass = lafPropClass;

		this.languagePropertyClass = langPropClass;

		layoutPanel(); // does the actual panel layout

		UIManager.LookAndFeelInfo[] lAFs = UIManager.getInstalledLookAndFeels();

		for (int ctr=0; ctr<lAFs.length; ctr++)
		{
			lafNameClassMap.put(lAFs[ctr].getName(), lAFs[ctr].getClassName());
		}
	}

	private CommandQueue commandQueue;

	private Class lookAndFeelPropertyClass;

	private Class languagePropertyClass;

	private String lookAndFeelProperty;

	private String languageProperty;

	private JLabel currentLAFLabel;

	private JComboBox currentLAFCombo;

	private JLabel currentLanguageLabel;

	private JComboBox currentLanguageCombo;

	private HashMap lafNameClassMap = new HashMap();

	private MedViewDialogs mVD = MedViewDialogs.instance();

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private static final int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

	private static final int cGS = GUIConstants.COMPONENT_GROUP_SPACING;


	// LANGUAGE COMBO LISTENER

	private class LanguageComboListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				String lang = (String) currentLanguageCombo.getSelectedItem();

				commandQueue.addToQueue(new ChangeLanguageCommand(lang));
			}
		}
	}


	// LOOK-AND-FEEL COMBO LISTENER

	private class LAFComboListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				String n = (String) currentLAFCombo.getSelectedItem();

				String lookAndFeelClass = (String) lafNameClassMap.get(n);

				commandQueue.addToQueue(new ChangeLAFCommand(lookAndFeelClass));
			}
		}
	}


	// CHANGE LOOK-AND-FEEL COMMAND

	private class ChangeLAFCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeLAFCommand);
		}

		public void execute()
		{
			try
			{
				String lafProp = lookAndFeelProperty;

				Class lafPropClass = lookAndFeelPropertyClass;

				mVDH.setUserStringPreference(lafProp, className, lafPropClass);

				UIManager.setLookAndFeel(className);
			}
			catch (Exception e) { e.printStackTrace(); }
		}

		public ChangeLAFCommand(String className)
		{
			this.className = className;
		}

		private String className;
	}


	// CHANGE LANGUAGE COMMAND

	public class ChangeLanguageCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeLanguageCommand);
		}

		public void execute()
		{
			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(LanguageAndLAFSettingsPanel.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			try
			{
				String langProp = languageProperty;

				Class langPropClass = languagePropertyClass;

				mVDH.setUserStringPreference(langProp, language, langPropClass);

				mVDH.changeLanguage(language);

				mVD.createAndShowInfoDialog(owner, mVDH.getLanguageString(OTHER_LANGUAGE_CHANGE_WILL_TAKE_EFFECT_WHEN_RESTARTED_LS_PROPERTY));
			}
			catch (LanguageException e)
			{
				Component parent = (Component) LanguageAndLAFSettingsPanel.this;

				mVD.createAndShowErrorDialog(owner, e.getMessage());
			}
		}

		public ChangeLanguageCommand(String language)
		{
			this.language = language;
		}

		private String language;
	}
}
