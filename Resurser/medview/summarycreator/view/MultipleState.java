/*
 * @(#)MultipleState.java
 *
 * $Id: MultipleState.java,v 1.7 2005/03/16 13:49:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import javax.swing.*;

import java.util.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 */
public class MultipleState extends TermState
{

	/**
	 * @param panel
	 */
	protected void addToCardPanel( JPanel panel )
	{
		panel.add(MULTIPLE_VIEW_CARD_IDENTIFIER, view);
	}

	protected void addToStateHashMap( HashMap hashMap )
	{
		hashMap.put(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR, this);
	}

	/**
	 * @param term
	 */
	public void displayTerm( String term )
	{
		TranslatorModelKeeper keeper = translatorView.getModelKeeper();

		TranslatorModel model = keeper.getTranslatorModel();

		TranslationModel tModel = model.getTranslationModel(term);

		updateCard(tModel);

		translatorView.displayCard(MULTIPLE_VIEW_CARD_IDENTIFIER);
	}

	/**
	 */
	public void translatorChanged( )
	{
		String term = translatorView.getCurrentTerm();

		if (term != null) { displayTerm(term); }
	}

	/**
	 * @param model
	 */
	protected void updateCard( TranslationModel model )
	{
		((AbstractModelTermView)view).setModel(model);
	}

	/**
	 * @param translatorView
	 */
	public MultipleState( TranslatorView translatorView )
	{
		super(translatorView);

		view = TermViewFactory.instance().createTermView(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR);
	}

	protected final String MULTIPLE_VIEW_CARD_IDENTIFIER = "multiple";
}
