/*
 * @(#)TranslatorViewMediator.java
 *
 * $Id: TranslatorViewMediator.java,v 1.7 2006/04/24 14:16:44 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;

import misc.gui.actions.*;

import se.chalmers.cs.medview.docgen.translator.*;

public interface TranslatorViewMediator extends MutableActionContainer
{
	public abstract TranslatorModelKeeper getTranslatorModelKeeper();

	public abstract Frame getParentFrame();

}
