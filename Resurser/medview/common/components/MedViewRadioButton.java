/*
 * @(#)MedViewRadioButton.java
 *
 * $Id: MedViewRadioButton.java,v 1.2 2004/12/08 14:46:34 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components;

import javax.swing.*;

import medview.datahandling.*;

/**
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewRadioButton extends JRadioButton
{
	/**
	 */
	public MedViewRadioButton(String lS)
	{
		super();

		setText(MedViewDataHandler.instance().getLanguageString(lS));
	}
}
