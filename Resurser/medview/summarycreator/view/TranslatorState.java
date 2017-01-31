/*
 * @(#)TranslatorState.java
 *
 * $Id: TranslatorState.java,v 1.7 2005/02/24 16:32:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.util.*;

import javax.swing.*;

import se.chalmers.cs.medview.docgen.translator.*;

public abstract class TranslatorState
{

	public abstract void translatorChanged( );

	public abstract void displayTerm( String term );

	protected abstract void addToCardPanel( JPanel panel );

	protected abstract void addToStateHashMap( HashMap hashMap );

	protected abstract void updateCard( TranslationModel model );

	/**
	 * If the state represents a view for a term
	 * it should add the type of the term to map
	 * to the state in the TranslatorView class.
	 * The method is called from the TranslatorView
	 * object which asks all it's states if it wants
	 * to add itself to the map. If a term later is
	 * to be displayed, the type of the term will be
	 * queried, and then the state corresponding to
	 * the queried type will use it's view and set
	 * the card layout to display it (this is done by
	 * means of the displayTerm() method).
	 */

	public TranslatorState( TranslatorView translatorView )
	{
		this.translatorView = translatorView;
	}

	protected TranslatorView translatorView;

	protected AbstractTermView view;

}
