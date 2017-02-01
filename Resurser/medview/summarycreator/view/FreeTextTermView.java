/*
 * @(#)FreeTextTermView.java
 *
 * $Id: FreeTextTermView.java,v 1.6 2002/11/01 13:39:22 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import javax.swing.*;

public class FreeTextTermView extends AbstractTermView
{
	protected boolean usesTermDescription() { return true; }

	protected JPanel getTopPanel() { return new JPanel(); }

	public FreeTextTermView( ) {}
}
