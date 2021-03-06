/*
 * @(#)RegularState.java
 *
 * $Id: RegularState.java,v 1.7 2005/03/16 13:49:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.util.*;

import javax.swing.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 */
public class RegularState extends TermState
{

	/**
	 * @param panel
	 */
	protected void addToCardPanel( JPanel panel )
	{
		panel.add(REGULAR_VIEW_CARD_IDENTIFIER, view);
	}

	protected void addToStateHashMap( HashMap hashMap )
	{
		hashMap.put(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR, this);
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

		translatorView.displayCard(REGULAR_VIEW_CARD_IDENTIFIER);
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
	public RegularState( TranslatorView translatorView )
	{
		super(translatorView);

		view = TermViewFactory.instance().createTermView(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR);
	}

	protected final String REGULAR_VIEW_CARD_IDENTIFIER = "regular";
}
