/*
 * @(#)EmptyState.java
 *
 * $Id: EmptyState.java,v 1.7 2005/02/24 16:32:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.util.*;

import javax.swing.*;

import se.chalmers.cs.medview.docgen.translator.*;

public class EmptyState extends TranslatorState
{

	public void displayTerm( String term )
	{
		translatorView.displayCard(EMPTY_VIEW_CARD_IDENTIFIER);
	}

	public void translatorChanged( ) {}

	protected void addToCardPanel( JPanel panel )
	{
		panel.add(EMPTY_VIEW_CARD_IDENTIFIER, view);
	}

	protected void addToStateHashMap( HashMap hashMap ) {}

	protected void updateCard( TranslationModel model )	{}



	private void initEmptyView()
	{
		view = TermViewFactory.instance().createEmptyTermView();
	}

	public EmptyState( TranslatorView translatorView )
	{
		super(translatorView);

		initEmptyView();
	}

	protected final String EMPTY_VIEW_CARD_IDENTIFIER = "empty";
}
