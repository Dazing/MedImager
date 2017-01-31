/*
 * @(#)FreeState.java
 *
 * $Id: FreeState.java,v 1.6 2005/02/24 16:32:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import javax.swing.*;

import java.util.*;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 */
public class FreeState extends TermState
{

	/**
	 * @param panel
	 */
	protected void addToCardPanel( JPanel panel )
	{
		panel.add(FREE_VIEW_CARD_IDENTIFIER, view);
	}

	protected void addToStateHashMap( HashMap hashMap )
	{
		hashMap.put(TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR, this);
	}

	/**
	 * @param term
	 */
	public void displayTerm( String term )
	{
		view.setTerm(term);

		translatorView.displayCard(FREE_VIEW_CARD_IDENTIFIER);
	}

	/**
	 */
	public void translatorChanged( ) { }

	/**
	 * @param model
	 */
	protected void updateCard( TranslationModel model ) {}


	/**
	 * @param translatorView
	 */
	public FreeState( TranslatorView translatorView )
	{
		super(translatorView);

		view = TermViewFactory.instance().createTermView(TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR);
	}

	protected final String FREE_VIEW_CARD_IDENTIFIER = "free";
}
