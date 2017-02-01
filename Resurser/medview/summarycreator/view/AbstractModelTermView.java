/*
 * @(#)AbstractModelTermView.java
 *
 * $Id: AbstractModelTermView.java,v 1.10 2005/02/24 16:32:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

public abstract class AbstractModelTermView extends AbstractTermView
	implements TranslationModelListener, SeparatedTranslationModelListener,
		MedViewLanguageConstants
{

	protected final boolean usesAutoVG() { return true; }

	protected final boolean usesSeparators() { return true; }

	protected final boolean usesValueButtons() { return true; }

	protected final boolean usesTermDescription() { return true; }





	public void vgChanged(TranslationModelEvent e)
	{
		boolean autoVGCBSel = autoVGCheckBox.isSelected();

		boolean gemVGCBSel = gemenVGCheckBox.isSelected();

		boolean verVGCBSel = versalVGCheckBox.isSelected();

		boolean modelAutoVGSel = (e.getAutoVG());

		boolean modelGemVGSel = (e.getVGPolicy() == TranslationModel.AUTO_VG_POLICY_GEMEN);

		boolean modelVerVGSel = (e.getVGPolicy() == TranslationModel.AUTO_VG_POLICY_VERSAL);

		if (autoVGCBSel != modelAutoVGSel) { autoVGCheckBox.setSelected(modelAutoVGSel); }

		if (gemVGCBSel != modelGemVGSel) { gemenVGCheckBox.setSelected(modelGemVGSel); }

		if (verVGCBSel != modelVerVGSel) { versalVGCheckBox.setSelected(modelVerVGSel); }
	}

	public void separatorChanged(SeparatedTranslationModelEvent e)
	{
		String sepFieldText = sepField.getText();

		String modelSep = e.getSeparator();

		if (!sepFieldText.equals(modelSep)) { sepField.setText(modelSep); }
	}

	public void ntlSeparatorChanged(SeparatedTranslationModelEvent e)
	{
		String ntlSepFieldText = ntlSepField.getText();

		String modelSep = e.getSeparator();

		if (!ntlSepFieldText.equals(modelSep)) { ntlSepField.setText(modelSep); }
	}

	/* NOTE: The rest of the methods contained in the respective
	 * TranslationModelListener and SeparatedTranslationModelListener
	 * interfaces are left for subclasses to implement, since they can
	 * not be implemented in this abstract class. Note also the order of
	 * events when dealing with the checkboxes - when the vg is changed
	 * in the model, the vgChanged() method is called, which in turn checks
	 * if the VG setting of the VG checkbox equals that of the model. If not,
	 * it sets the checkbox to the VG setting in the model. This, in turn,
	 * will trigger an event being fired to the auto vg item listener which
	 * is created and attached to the check box in the method below. The item
	 * listener will only care about selection events of the checkbox, and
	 * compares it to the selected status in the model. If they match, nothing
	 * is done (this is the case when the check is updated from a model vg
	 * change event). If they don't match (i.e. the user has clicked the
	 * check box) the model is updated.
	 *
	 * NOTE: the separatorChanged() and ntlSeparatorChanged() methods can
	 * perform the cast without instance checking of type, since they will
	 * only be called if the actual model type is of separated type. */





	public ItemListener getAutoVGItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				boolean sel = (e.getStateChange() == ItemEvent.SELECTED);

				if (!(currentModel.isAutoVG() == sel)) { currentModel.setAutoVG(sel); }
			}
		};
	}

	public ItemListener getGemenItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() != ItemEvent.SELECTED) { return; }

				if (currentModel.getVGPolicy() != TranslationModel.AUTO_VG_POLICY_GEMEN)
				{
					currentModel.setVGPolicy(TranslationModel.AUTO_VG_POLICY_GEMEN);
				}
			}
		};
	}

	public ItemListener getVersalItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() != ItemEvent.SELECTED) { return; }

				if (currentModel.getVGPolicy() != TranslationModel.AUTO_VG_POLICY_VERSAL)
				{
					currentModel.setVGPolicy(TranslationModel.AUTO_VG_POLICY_VERSAL);
				}
			}
		};
	}

	protected DocumentListener getSeparatorDocumentListener()
	{
		return new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e)
			{
				if (isProgrammaticSeparatorUpdate()) { return; }

				SeparatedTranslationModel model = (SeparatedTranslationModel) currentModel;

				String modelSep = model.getSeparator();

				String viewSep = sepField.getText();

				if (!modelSep.equals(viewSep)) { model.setSeparator(viewSep); }
			}

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}
		};
	}

	protected DocumentListener getNTLSeparatorDocumentListener()
	{
		return new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e)
			{
				if (isProgrammaticSeparatorUpdate()) { return; }

				SeparatedTranslationModel model = (SeparatedTranslationModel) currentModel;

				String modelSep = model.getNTLSeparator();

				String viewSep = ntlSepField.getText();

				if (!modelSep.equals(viewSep)) { model.setNTLSeparator(viewSep); }
			}

			public void removeUpdate(DocumentEvent e)
			{
				this.insertUpdate(e);
			}
		};
	}





	public TranslationModel getModel()
	{
		return currentModel;
	}

	public void setModel( TranslationModel model )
	{
		oldModel = currentModel;

		currentModel = model;

		detachOldModelListeners();

		attachModelListeners();

		setTerm(model.getTerm());
	}

	protected void detachOldModelListeners( )
	{
		if (oldModel != null)
		{
			oldModel.removeTranslationModelListener(this);

			if (oldModel instanceof SeparatedTranslationModel)
			{
				((SeparatedTranslationModel)oldModel).removeSeparatedTranslationModelListener(this);
			}
		}
	}

	protected void attachModelListeners( )
	{
		currentModel.addTranslationModelListener(this);

		if (currentModel instanceof SeparatedTranslationModel)
		{
			((SeparatedTranslationModel)currentModel).addSeparatedTranslationModelListener(this);
		}
	}





	/* NOTE: The flow of method calls is as
	 * follows: setTerm() -> updateView() ->
	 * updateVG() -> updateButtons() ->
	 * updateTopPanel() -> updateSeparators() ->
	 * updateDescription().
	 */

	protected void updateButtons()
	{
		getAddValueAction().setEnabled(!isDerived());

		getRemoveValueAction().setEnabled(false);
	}

	protected void updateVG()
	{
		autoVGCheckBox.setSelected(currentModel.isAutoVG());

		gemenVGCheckBox.setSelected(currentModel.getVGPolicy() == TranslationModel.AUTO_VG_POLICY_GEMEN);

		versalVGCheckBox.setSelected(currentModel.getVGPolicy() == TranslationModel.AUTO_VG_POLICY_VERSAL);
	}

	protected void updateSeparators()
	{
		if (currentModel instanceof SeparatedTranslationModel)
		{
			SeparatedTranslationModel model = (SeparatedTranslationModel) currentModel;

			setProgrammaticSeparatorUpdate(true);

			sepField.setText(model.getSeparator());

			ntlSepField.setText(model.getNTLSeparator());

			setProgrammaticSeparatorUpdate(false);
		}
		else
		{
			disableAllSeparatorComponents();
		}
	}

	protected void updateDescription()
	{
		if (currentModel != null)
		{
			updateDescription(currentModel.getTypeDescriptor());
		}
		else
		{
			super.updateDescription();
		}
	}





	protected boolean isProgrammaticSeparatorUpdate()
	{
		return programmaticSeparatorUpdate;
	}

	private void setProgrammaticSeparatorUpdate(boolean flag)
	{
		programmaticSeparatorUpdate = flag;
	}

	/**
	 * When you update the text in the separator
	 * text fields, java will first remove the
	 * current text (which will fire a removeUpdate
	 * document event to all registered document
	 * listeners), and then add the new text (which
	 * will fire a insertUpdate document event to all
	 * registered document listeners). Thus, in order
	 * for the subclasses to distinguish between a
	 * user update of the document in the separator
	 * fields and a programmatic update of the document
	 * which occurs when switching term with the same
	 * type, the subclasses can query the superclass
	 * method isProgrammaticSeparatorUpdate() to find
	 * out if a change was caused by the program or
	 * by the user (usually, only the user changes are
	 * of interest in the listener methods). */





	protected AbstractModelTermView()
	{
		programmaticSeparatorUpdate = false;
	}

	protected TranslationModel oldModel;

	protected TranslationModel currentModel;

	private boolean programmaticSeparatorUpdate;

}
