/*
 * @(#)MedSummarySummaryPanel.java
 *
 * $Id: MedSummarySummaryPanel.java,v 1.19 2005/03/16 13:55:06 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import medview.common.actions.*;
import medview.common.dialogs.*;
import medview.common.generator.*;
import medview.common.print.*;
import medview.common.text.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medsummary.model.*;

import misc.gui.actions.*;
import misc.gui.constants.*;
import misc.gui.print.*;
import misc.gui.utilities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import se.chalmers.cs.medview.docgen.*;

public class MedSummarySummaryPanel extends JPanel implements
	ActionContainer, MedViewTextActionContainer, MedViewLanguageConstants,
	MedSummaryActions, MedSummaryUserProperties, MedSummaryFlagProperties,
	MedViewPrintProperties, MedSummaryConstants
{

	public Action getAction(String actionID)
	{
		if (textActions.isMedViewTextAction(actionID))
		{
			return textActions.getAction(actionID);
		}
		else
		{
			return (Action) actions.get(actionID);
		}
	}

	/**
	 * Sets the graphic template (the one surrounding
	 * the actual textual template) to use. It is
	 * supposed that the template is equal to one of
	 * the page creators supported by the journal page
	 * creator factory. An exception is thrown if the
	 * specified template is not supported by the
	 * factory. This will also update the 'last
	 * graphic template used' property so it is used
	 * the next time the application is started.
	 */
	public void setGraphicTemplateToUse(String template) throws
		InvalidGraphicTemplateException
	{
		try
		{
			Class userPropClass = MedSummaryUserProperties.class;

			pCFactory.setPageCreatorToUse(template); // identifier

			pageView.setPageCreator(pCFactory.createPageCreator()); // triggers refresh

			mVDH.setUserStringPreference(LAST_GRAPHIC_TEMPLATE_USED_PROPERTY, template, userPropClass);
		}
		catch (InvalidPageCreatorException e)
		{
			throw new InvalidGraphicTemplateException(e.getMessage());
		}
	}

	/**
	 * Returns the graphic template currently in use
	 * by the journal page creator factory. This will
	 * be one of the page creator identifiers supported
	 * by the factory.
	 */
	public String getGraphicTemplateInUse()
	{
		return pCFactory.getPageCreatorIdentifierInUse();
	}

	/**
	 * Returns an array of all the available graphic
	 * templates (page creator identifiers) supported
	 * by the journal page creator factory.
	 */
	public String[] getAvailableGraphicTemplates()
	{
		return pCFactory.getAllPageCreatorIdentifiers();
	}

	public MedViewTextActionCollection getTextActionCollection()
	{
		return textActions;
	}

	public void setDocument(StyledDocument doc)
	{
		if (doc == null)
		{
			textPane = null;

			pageView.setEnabled(false);

			pageView.setBase(null);

			textActions.disableActions();

			textActions.setAttachedTextPane(null);

			printJournalAction.setEnabled(false);

			saveAsRTFAction.setEnabled(false);

			saveAsHTMLAction.setEnabled(false);

			closeGeneratedPageAction.setEnabled(false);
		}
		else
		{
			setHeaderPCodeAndDate();

			textPane = new JTextPane(doc);

			pageView.setBase(textPane);

			pageView.setEnabled(true);

			pageViewContainer.getViewport().setViewPosition(new Point(0,0)); // reset slider 041119

			textActions.setAttachedTextPane(textPane);

			textActions.enableActions();

			printJournalAction.setEnabled(true);

			saveAsRTFAction.setEditor(textPane);

			saveAsHTMLAction.setEditor(textPane);

			saveAsRTFAction.setEnabled(true);

			saveAsHTMLAction.setEnabled(true);

			closeGeneratedPageAction.setEnabled(true);
		}
	}

	private void setHeaderPCodeAndDate()
	{
		MedViewGeneratorEngineBuilder b = model.getGeneratorEngineBuilder();

		try
		{
			ValueContainer[] vCs = b.getBuiltValueContainers();

			ExaminationIdentifier[] eIs = b.getBuiltIdentifiers();

			if (vCs != null)
			{
				String pc = null;

				String pcToSet = null;

				for (int ctr = 0; ctr < vCs.length; ctr++)
				{
					if (ctr == 0)
					{
						pCFactory.setDate(eIs[ctr].getTime());
					}

					pc = eIs[ctr].getPID().getPCode();

					if ((pcToSet != null) && (!pc.equals(pcToSet)))
					{
						pCFactory.setPCode(UNKNOWN_PCODE_STRING);

						return;
					}
					else
					{
						pcToSet = pc;
					}
				}

				pCFactory.setPCode(pcToSet);
			}
			else
			{
				pCFactory.setPCode(UNKNOWN_PCODE_STRING);

				pCFactory.setDate(new Date());

				/* we should never get here if all works */
			}
		}
		catch (NotBuiltException exc)
		{
			exc.printStackTrace();

			pCFactory.setPCode(UNKNOWN_PCODE_STRING);

			pCFactory.setDate(new Date());

			/* we should never get here if all works */
		}
	}



	private void layoutPanel()
	{
		setLayout(new BorderLayout());

		add(pageViewContainer, BorderLayout.CENTER);
	}

	private void initSimpleMembers()
	{
		model = mediator.getModel();

		mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();

		pCFactory = new JournalPageCreatorFactory();
	}

	private void initPageCreatorToUse()
	{
		Class userPropClass = MedSummaryUserProperties.class;

		String prop = LAST_GRAPHIC_TEMPLATE_USED_PROPERTY;

		String dPC = pCFactory.getDefaultPageCreatorIdentifier();

		if (mVDH.isUserPreferenceSet(prop, userPropClass))
		{
			try
			{
				String pC = mVDH.getUserStringPreference(prop, "", userPropClass);

				pCFactory.setPageCreatorToUse(pC);
			}
			catch (InvalidPageCreatorException e)
			{
				try
				{
					pCFactory.setPageCreatorToUse(dPC);
				}
				catch (InvalidPageCreatorException e2)
				{
					mVD.createAndShowErrorDialog(mediator, e2.getMessage());

					System.exit(1); // fatal error
				}
			}
		}
		else
		{
			try
			{
				pCFactory.setPageCreatorToUse(dPC);

				mVDH.setUserStringPreference(prop, dPC, userPropClass);
			}
			catch (InvalidPageCreatorException e2)
			{
				mVD.createAndShowErrorDialog(mediator, e2.getMessage());

				System.exit(1); // fatal error
			}
		}
	}

	private void initPageView() // sets property defaults if not set
	{
		Class flagPropClass = MedSummaryFlagProperties.class;

		Class userPropClass = MedSummaryUserProperties.class;

		String padEmptyProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY;

		String padEmptyVProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY;

		String padEmptyHProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY;

		String useBorderProp = USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY;

		String borderTypeProp = BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY;

		String maxHeightProp = MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY;

		int defEmptyHor = DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL;

		int defEmptyVer = DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL;

		int defBordType = DEFAULT_BORDER_TYPE_AROUND_INSERTED_IMAGES;

		int defInsertHeight = DEFAULT_HEIGHT_OF_INSERTED_IMAGES;

		int vertPad, horPad, bordType, maxHeight;

		pageView = new TextPageView(pCFactory.createPageCreator());

		if (mVDH.isUserPreferenceSet(padEmptyProp, flagPropClass)) // if set, so are hor vert
		{
			vertPad = mVDH.getUserIntPreference(padEmptyVProp, defEmptyVer, userPropClass);

			horPad = mVDH.getUserIntPreference(padEmptyHProp, defEmptyHor, userPropClass);
		}
		else
		{
			mVDH.setUserBooleanPreference(padEmptyProp, true, flagPropClass); // true = default (use pad)

			mVDH.setUserIntPreference(padEmptyHProp, defEmptyHor, userPropClass); // default

			mVDH.setUserIntPreference(padEmptyVProp, defEmptyVer, userPropClass); // default

			vertPad = defEmptyVer;

			horPad = defEmptyHor;
		}

		if (mVDH.isUserPreferenceSet(useBorderProp, flagPropClass))
		{
			bordType = mVDH.getUserIntPreference(borderTypeProp, defBordType, userPropClass);
		}
		else
		{
			mVDH.setUserBooleanPreference(useBorderProp, true, flagPropClass); // true = default (use border)

			mVDH.setUserIntPreference(borderTypeProp, defBordType, userPropClass); // default

			bordType = defBordType;
		}

		if (mVDH.isUserPreferenceSet(maxHeightProp, userPropClass))
		{
			maxHeight = mVDH.getUserIntPreference(maxHeightProp, defInsertHeight,userPropClass);
		}
		else
		{
			mVDH.setUserIntPreference(maxHeightProp, defInsertHeight, userPropClass); // default

			maxHeight = defInsertHeight;
		}

		pageView.setVerticalDropImagePadding(vertPad);

		pageView.setHorizontalDropImagePadding(horPad);

		pageView.setDropImageBorderType(bordType);

		pageView.setMaximumImageDropHeight(maxHeight);

		pageViewContainer = new JScrollPane(pageView);

		int unitIncrement = GUIConstants.DEFAULT_SCROLLABLE_UNIT_INCREMENT;

		pageViewContainer.getVerticalScrollBar().setUnitIncrement(unitIncrement);

		pageViewContainer.getHorizontalScrollBar().setUnitIncrement(unitIncrement);

		pageViewContainer.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	}

	private void initActions()
	{
		actions = new HashMap();


		// set up kept actions

		setupPrintJournalAction();

		saveAsRTFAction = new SaveAsRTFAction();

		saveAsHTMLAction = new SaveAsHTMLAction();

		closeGeneratedPageAction = new CloseGeneratedPageAction();


		// register actions

		actions.put(SAVE_AS_RTF_ACTION, saveAsRTFAction);

		actions.put(SAVE_AS_HTML_ACTION, saveAsHTMLAction);

		actions.put(PRINT_JOURNAL_ACTION, printJournalAction);

		actions.put(CLOSE_GENERATED_PAGE_ACTION, closeGeneratedPageAction);

		mediator.registerAction(SAVE_AS_RTF_ACTION, saveAsRTFAction);

		mediator.registerAction(SAVE_AS_HTML_ACTION, saveAsHTMLAction);

		mediator.registerAction(PRINT_JOURNAL_ACTION, printJournalAction);

		mediator.registerAction(CLOSE_GENERATED_PAGE_ACTION, closeGeneratedPageAction);


		// set up and register text edit actions

		textActions = new MedViewTextActionCollection();

		String[] actionNames = textActions.getContainedActionNames();

		for (int ctr=0; ctr<actionNames.length; ctr++)
		{
			Action currAction = textActions.getAction(actionNames[ctr]);

			mediator.registerAction(actionNames[ctr], currAction);
		}
	}

	private void setupPrintJournalAction()
	{
		printJournalAction = pageView.getPrintAction();

		MedViewDataHandler mVDH = MedViewDataHandler.instance();

		String actionNamePrefix = ACTION_NAME_PREFIX_LS_PROPERTY;

		String actionDescPrefix = ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY;

		String printName = actionNamePrefix + ACTION_PRINT_JOURNAL_LS_PROPERTY;

		String printDesc = actionDescPrefix + ACTION_PRINT_JOURNAL_LS_PROPERTY;

		printJournalAction.putValue(Action.NAME, mVDH.getLanguageString(printName));

		printJournalAction.putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(printDesc));

		printJournalAction.putValue(Action.SMALL_ICON, mVDH.getImageIcon(MedViewMediaConstants.PRINT_IMAGE_ICON_24));
	}

	private void initListeners()
	{
		listener = new Listener();

		model.addMedSummaryModelListener(listener);

		mVDH.addMedViewPreferenceListener(listener);
	}

	private void initSetDocument()
	{
		setDocument(model.getDocument());
	}

	public MedSummarySummaryPanel(MedSummaryFrame mediator)
	{
		this.mediator = mediator;

		initSimpleMembers();

		initPageCreatorToUse();

		initPageView();

		layoutPanel();

		initActions();

		initListeners();

		initSetDocument();
	}

	private HashMap actions;

	private Listener listener;

	private JTextPane textPane;

	private MedViewDialogs mVD;

	private TextPageView pageView;

	private MedSummaryModel model;

	private MedViewDataHandler mVDH;

	private MedSummaryFrame mediator;

	private Action printJournalAction;

	private JScrollPane pageViewContainer;

	private SaveAsRTFAction saveAsRTFAction;

	private Action closeGeneratedPageAction;

	private SaveAsHTMLAction saveAsHTMLAction;

	private MedViewTextActionCollection textActions;

	private JournalPageCreatorFactory pCFactory;

	private static final String UNKNOWN_PCODE_STRING = "XXXXXXXX";








	private class CloseGeneratedPageAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			model.setDocument(null); // triggers event to listeners
		}

		public CloseGeneratedPageAction()
		{
			super(ACTION_CLOSE_GENERATED_PAGE_LS_PROPERTY);
		}
	}

	private class Listener implements MedSummaryModelListener, MedViewPreferenceListener
	{
		public void documentReplaced(MedSummaryModelEvent e)
		{
			GUIUtilities.invokeOnDispatch(new Runnable()
			{
				public void run()
				{
					setDocument(model.getDocument());
				}
			});
		}

		public void userPreferenceChanged(MedViewPreferenceEvent e)
		{
			checkForRefresh(e);
		}

		public void systemPreferenceChanged(MedViewPreferenceEvent e)
		{
			checkForRefresh(e);
		}

		private void checkForRefresh(MedViewPreferenceEvent e)
		{
			Class userPropClass = MedSummaryUserProperties.class;

			Class flagPropClass = MedSummaryFlagProperties.class;

			if (e.getPreferenceName().equals(MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY))
			{
				int height = mVDH.getUserIntPreference(MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY, -10, userPropClass);

				pageView.setMaximumImageDropHeight(height);

				pageView.refresh();

				return;
			}
			if (e.getPreferenceName().equals(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY))
			{
				int pad = mVDH.getUserIntPreference(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY, -10, userPropClass);

				pageView.setVerticalDropImagePadding(pad);

				pageView.refresh();

				return;
			}
			if (e.getPreferenceName().equals(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY))
			{
				int pad = mVDH.getUserIntPreference(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY, -10, userPropClass);

				pageView.setHorizontalDropImagePadding(pad);

				pageView.refresh();

				return;
			}
			if (e.getPreferenceName().equals(BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY))
			{
				int type = mVDH.getUserIntPreference(BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY, -10, userPropClass);

				pageView.setDropImageBorderType(type);

				pageView.refresh();

				return;
			}
			if (e.getPreferenceName().equals(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY))
			{
				boolean useEmpty = mVDH.getUserBooleanPreference(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY, false, flagPropClass);

				if (!useEmpty)
				{
					pageView.setVerticalDropImagePadding(0); // does not affect the property

					pageView.setHorizontalDropImagePadding(0); // does not affect the property
				}
				else
				{
					int hPad = mVDH.getUserIntPreference(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY, -10, userPropClass);

					int vPad = mVDH.getUserIntPreference(PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY, -10, userPropClass);

					pageView.setHorizontalDropImagePadding(hPad);

					pageView.setVerticalDropImagePadding(vPad);
				}

				pageView.refresh();

				return;
			}
			if (e.getPreferenceName().equals(USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY))
			{
				int none = PageRenderer.IMAGE_BORDER_NONE;

				boolean useBorder = mVDH.getUserBooleanPreference(USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY, false, flagPropClass);

				if (!useBorder)
				{
					pageView.setDropImageBorderType(none); // does not affect the property
				}
				else
				{
					pageView.setDropImageBorderType(mVDH.getUserIntPreference(BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY, -10, userPropClass));
				}

				pageView.refresh();

				return;
			}

			for (int ctr=0; ctr<addrProps.length; ctr++)
			{
				if (e.getPreferenceName().equals(addrProps[ctr]))
				{
					pageView.refresh(); // address or logo changed
				}
			}
		}

		private String[] addrProps =
		{
			SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_2_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_3_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_4_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_5_PROPERTY,

			SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY
		};

		public void dataLocationChanged(MedSummaryModelEvent e)
		{
		}

		public void dataLocationIDChanged(MedSummaryModelEvent e)
		{
		}

		public void patientsChanged(MedSummaryModelEvent e)
		{
		}

		public void examinationAdded(MedSummaryModelEvent e)
		{
		}

		public void examinationUpdated(MedSummaryModelEvent e)
		{
		}

		public void sectionsChanged(MedSummaryModelEvent e)
		{
		}

		public void templateChanged(MedSummaryModelEvent e)
		{
		}

		public void templateIDChanged(MedSummaryModelEvent e)
		{
		}

		public void translatorChanged(MedSummaryModelEvent e)
		{
		}

		public void translatorIDChanged(MedSummaryModelEvent e)
		{
		}

		public void currentPackageChanged(MedSummaryModelEvent e)
		{
		}

		public void includedPackagesUpdated(MedSummaryModelEvent e)
		{
		}
	}
}
