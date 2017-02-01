/*
 * @(#)TemplateViewWrapper.java
 *
 * $Id: TemplateViewWrapper.java,v 1.11 2007/04/08 15:55:59 oloft Exp $
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
import javax.swing.text.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.gui.actions.*;

import se.chalmers.cs.medview.docgen.template.*;

public class TemplateViewWrapper implements SummaryCreatorActions, ActionContainer
{

	public Action getAction(String name)
	{
		return tASH.getAction(name);
	}

	public boolean allowsExit()
	{
		return tASH.allowsExit();
	}

	public void setEnabled(boolean e)
	{
		paneContainer.setEnabled(e);

		paneScrollPane.setEnabled(e);

		tASH.setActionEnabledStatus(e);

		if (basePane != null) { basePane.setEnabled(e); }
	}

	public void addTerm(String term)
	{
		try
		{
			if (model == null) { return; }

			int offs = basePane.getCaretPosition();

			Element el = model.getDocument().getCharacterElement(offs);

			model.addTerm(term, offs, el.getAttributes());

			basePane.requestFocus();
		}
		catch (Exception e)
		{
			String err = e.getMessage();

			MedViewDialogs.instance().createAndShowErrorDialog(mediator, err);
		}
	}

	private void setModel(TemplateModel newModel)
	{
		if (model != null) // the old model
		{
			model.removeTemplateModelListener(listener);

			basePane.removeMouseListener(listener);

			basePane.removeCaretListener(listener);
		}

		model = newModel;

		if (model != null) // the new model
		{
			basePane = new JTextPane();

			basePane.addMouseListener(listener);

			basePane.addCaretListener(listener);

			basePane.setEnabled(model.containsSections());

			basePane.setEditorKit(painter.getTemplateEditorKit());

			basePane.setDocument(model.getDocument());

			basePane.setMargin(basePaneMargin);

			model.addTemplateModelListener(listener);

			paneScrollPane.setViewportView(basePane);
		}
		else
		{
			basePane = null;

			paneScrollPane.setViewportView(null);
		}

		tASH.basePaneChanged(basePane);

		paneContainer.repaint();
	}

	public JTextPane getBasePane()
	{
		return basePane;
	}

	public JPanel getPaneContainer()
	{
		return paneContainer;
	}

	public SummaryCreatorFrame getMediator()
	{
		return mediator;
	}

	public TemplateModel getModel()
	{
		return model;
	}

	private void initSimpleMembers()
	{
		listener = new Listener();

		basePaneMargin = new Insets(6,26,6,6);

		painter = new TemplatePainter(this);

		tASH = new TemplateActionStateHandler(this);
	}

	private void initKeeper()
	{
		keeper = mediator.getModel();

		keeper.addTemplateModelKeeperListener(listener);
	}

	private void initPaneContainer()
	{
		paneScrollPane = new JScrollPane();

		paneScrollPane.setPreferredSize(new Dimension(10,10)); // see note...

		paneContainer = new JPanel(new BorderLayout());

		paneContainer.add(paneScrollPane, BorderLayout.CENTER);
	}

	public TemplateViewWrapper( SummaryCreatorFrame mediator )
	{
		this.mediator = mediator;

		initSimpleMembers();

		initKeeper();

		initPaneContainer();
	}

	private JPanel paneContainer;

	private TemplateModel model;

	private TemplateModelKeeper keeper;

	private SummaryCreatorFrame mediator;

	private JScrollPane paneScrollPane;

	private TemplateActionStateHandler tASH;

	private TemplatePainter painter;

	private Insets basePaneMargin;

	private Listener listener;

	private JTextPane basePane;

	private static int newDocCounter;

    private final String UNTITLED_TEMPLATE_NAME = MedViewDataHandler.instance().getLanguageString(MedViewLanguageConstants.TITLE_NEW_TEMPLATE_LS_PROPERTY);

	static { newDocCounter = 1; }

	/* NOTE: if the pane scrollpane's preferred size
	 * is not set explicitly, it will expand and
	 * consume all available width when a template is
	 * loaded or text is entered. By specifying a pref
	 * size we override this so that the preferred
	 * sizes of the adjacent components (the term list
	 * and the translator view) are the ones defining
	 * the various split divider locations. */










	private class Listener extends MouseAdapter implements
		TemplateModelKeeperListener, TemplateModelListener, CaretListener
	{
		public void mouseClicked(MouseEvent e)
		{
			if (!model.containsSections()) { return; }

			int offs = basePane.viewToModel(e.getPoint());

			termModelAtOffs = model.getTermModel(offs);

			if (termModelAtOffs == null) { return; }

			int tStart = termModelAtOffs.getStartOffset();

			int tEnd = termModelAtOffs.getEndOffset() + 1;

			basePane.select(tStart, tEnd);
		}

		public void templateModelChanged(TemplateModelKeeperEvent e)
		{
			setModel(keeper.getTemplateModel());
		}

		public void templateModelLocationChanged(TemplateModelKeeperEvent e)
		{
			String modelLocation = keeper.getTemplateModelLocation();

			boolean containsModel = keeper.containsTemplateModel();

			if (containsModel && (modelLocation == null)) // new template
			{
				mediator.templateLocationChanged(UNTITLED_TEMPLATE_NAME + " " + newDocCounter++);
			}
			else if (containsModel && (modelLocation != null))
			{
				mediator.templateLocationChanged(modelLocation);
			}
			else
			{
				mediator.templateLocationChanged("");
			}
		}

		public void contentChanged(TemplateModelEvent e)
		{
			tASH.contentChanged(); // template state might change
		}

		public void caretUpdate(CaretEvent e)
		{
			if (!model.containsSections()) { return; }

			if (e.getDot() == oldDot) { return; } // old dot = last known dot position...

			termAtOffs = model.getTermName(e.getDot());

			if (termAtOffs != null) { mediator.termChosenInTemplate(termAtOffs); }

			if (model.allowsNormalEditing()) { return; }

			int validPos = model.getValidPosition(e.getDot(), oldDot);

			if (validPos == -1) { return; } // -1 -> no sections... IS THIS TEST NEEDED?

			if (validPos != e.getDot())
			{
				basePane.setCaretPosition(validPos);
			}
			else
			{
				oldDot = e.getDot();

				basePane.repaint();
			}
		}

		public void sectionAdded(TemplateModelEvent e)
		{
			SectionModel sectMod = e.getSectionModel();

			if ((sectMod != null))
			{
				basePane.setCaretPosition(sectMod.getStartOffset());

				if (!basePane.isEnabled()) { basePane.setEnabled(true); }

				basePane.requestFocus();
			}
		}

		public void sectionRemoved(TemplateModelEvent e)
		{
			if (model.containsSections())
			{
				basePane.setCaretPosition(0); // for now...
			}
			else
			{
				basePane.setEnabled(false);
			}
		}

		public void sectionRenamed(TemplateModelEvent e)
		{
			paneContainer.repaint();
		}

		public void editStatusChanged(TemplateModelEvent e) { }

		public void termAdded(TemplateModelEvent e) { }

		public void termRemoved(TemplateModelEvent e) { }


		private TermModel termModelAtOffs = null;

		private String termAtOffs = null;

		private int oldDot = 0;
	}

}
