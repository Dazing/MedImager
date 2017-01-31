/*
 * @(#)EmptyTermView.java
 *
 * $Id: EmptyTermView.java,v 1.7 2005/02/24 16:32:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import javax.swing.*;

public class EmptyTermView extends AbstractTermView
{
	protected void updateDescription( )
	{
		currentTermLabel.setText((term == null) ? "" : term + " / (empty)");
	}

	protected JPanel getTopPanel() { return new JPanel(); }

	public EmptyTermView( )	{}
}
