/*
 * @(#)TermListView.java
 *
 * $Id: TermListView.java,v 1.12 2004/12/08 14:46:26 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;

import misc.gui.components.*;
import misc.gui.constants.*;

public class TermListView extends JPanel implements MedViewLanguageConstants
{

	public void selectTerm(String term)
	{
		listPanel.selectEntry(term);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);

		listPanel.setEnabled(e);
	}

	protected void layoutView()
	{
		setLayout(new BorderLayout());

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

		setBorder(BorderFactory.createEmptyBorder(cCS,cCS,cCS,cCS));

		String[] terms = new String[0];

		try
		{
			terms = model.getTerms();
		}
		catch (CouldNotRetrieveTermsException e)
		{
			mVD.createAndShowErrorDialog(mediator, e.getMessage());
		}

		SearchableListModel sCModel = new SearchableListModel(terms);

		int selMode = ListSelectionModel.SINGLE_SELECTION;

		listPanel = new SearchableListPanel(sCModel, selMode);

		listPanel.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				String term = (String) listPanel.getSelectedEntry();

				if (term != null)
				{
					mediator.displayTerm(term);
				}
			}
		});

		listPanel.addListMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					mediator.addTerm((String)listPanel.getSelectedEntry());
				}
			}
		});

		updateSearchFieldLabel();

		add(listPanel, BorderLayout.CENTER);
	}

	private void updateSearchFieldLabel()
	{
		String lS = LABEL_TERM_DESCRIPTOR_LS_PROPERTY;

		listPanel.setSearchText(mVDH.getLanguageString(lS));
	}



	public TermListView( SummaryCreatorFrame mediator )
	{
		this.mediator = mediator;

		modList = new ModelListener();

		mVDH = MedViewDataHandler.instance();

		mVD = MedViewDialogs.instance();

		model = mediator.getModel();

		model.addSummaryCreatorModelListener(modList);

		layoutView();
	}

	protected MedViewDialogs mVD;

	protected MedViewDataHandler mVDH;

	protected SummaryCreatorModel model;

	protected SummaryCreatorFrame mediator;

	protected SearchableListPanel listPanel;

	protected SummaryCreatorModelListener modList;


	private class ModelListener implements SummaryCreatorModelListener
	{
		public void termListingChanged(SummaryCreatorModelEvent e)
		{
			SearchableListModel listModel = listPanel.getModel();

			listModel.clearEntries();

			try
			{
				listModel.setEntries(model.getTerms());
			}
			catch (CouldNotRetrieveTermsException exc)
			{
				listModel.setEntries(new String[0]);

				mVD.createAndShowErrorDialog(mediator, exc.getMessage());
			}
		}
	}
}
