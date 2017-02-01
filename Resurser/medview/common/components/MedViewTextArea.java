/*
 * @(#)MedViewTextArea.java
 *
 * $Id: MedViewTextArea.java,v 1.2 2004/12/08 14:46:34 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components;

import javax.swing.*;

import medview.datahandling.*;

/**
 * A simple text area component providing support
 * for language strings as used in the medview
 * context. Provide the language string that
 * represents the abstract text to be displayed
 * in the text area, and it will be transformed to
 * the text in use by the current language. It
 * will also listen for language changes and
 * update the area text whenever the language
 * changes.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewTextArea extends JTextArea
{
	/**
	 * Constructs a medview label. Takes
	 * the language string to use in
	 * determining the face of the label.
	 */
	public MedViewTextArea(String lS)
	{
		super();

		setText(MedViewDataHandler.instance().getLanguageString(lS));
	}
}
