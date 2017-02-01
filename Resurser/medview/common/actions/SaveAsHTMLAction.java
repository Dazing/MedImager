/*
 * @(#)SaveAsHTMLAction.java
 *
 * $Id: SaveAsHTMLAction.java,v 1.5 2006/04/24 14:17:44 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.actions;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import medview.common.dialogs.*;
import medview.datahandling.*;

public class SaveAsHTMLAction extends MedViewAction
{
	public void actionPerformed(ActionEvent e)
	{
		MedViewDialogs mVD = MedViewDialogs.instance();

		Frame owner = null;

		Window windowAncestor = SwingUtilities.getWindowAncestor(editor);

		if ((windowAncestor != null) && (windowAncestor instanceof Frame))
		{
			owner = (Frame) windowAncestor;
		}

		String ret = mVD.createAndShowSaveHTMLDialog(owner);

		if (ret != null)
		{
			try
			{
				FileOutputStream fOS = new FileOutputStream(ret);

				Document doc = editor.getDocument();

				HTMLEditorKit editorKit = new HTMLEditorKit();

				editorKit.write(fOS, doc, 0, doc.getLength());
			}
			catch (IOException iOE)
			{
				MedViewDataHandler mVDH = MedViewDataHandler.instance();

				String prop = ERROR_WRITE_HTML_LS_PROPERTY;

				String err = mVDH.getLanguageString(prop);

				mVD.createAndShowErrorDialog(owner, err);
			}
			catch (BadLocationException bLE)
			{
				String err = "BadLocationException thrown in WriteHTMLAction";

				mVD.createAndShowErrorDialog(owner, err);
			}
		}
	}

	public void setEditor(JEditorPane editor)
	{
		this.editor = editor;
	}

	public SaveAsHTMLAction()
	{
		this(null);
	}

	public SaveAsHTMLAction(JEditorPane editor)
	{
		super(ACTION_SAVE_AS_HTML_LS_PROPERTY);

		this.editor = editor;
	}

	private JEditorPane editor;

	/* NOTE
	 * ----
	 * The HTMLEditorKit's write() method checks the type of
	 * the document that is passed to it. If the document is
	 * a HTMLDocument (subclass of StyledDocument), the writer
	 * which writes the document is set to an instance of a
	 * HTMLWriter. If it is a StyledDocument, the writer is set
	 * to a MinimalHTMLWriter. If not one of the above, the
	 * document is written without any HTML consideration (the
	 * superclass method is called).
	 */
}
