/*
 * @(#)SaveAsRTFAction.java
 *
 * $Id: SaveAsRTFAction.java,v 1.5 2006/04/24 14:17:44 lindahlf Exp $
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
import javax.swing.text.rtf.*;

import medview.common.dialogs.*;
import medview.datahandling.*;

public class SaveAsRTFAction extends MedViewAction
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

		String ret = mVD.createAndShowSaveRTFDialog(owner);

		if (ret != null)
		{
			try
			{
				FileOutputStream fOS = new FileOutputStream(ret);

				Document doc = editor.getDocument();

				RTFEditorKit editorKit = new RTFEditorKit();

				editorKit.write(fOS, doc, 0, doc.getLength());
			}
			catch (IOException iOE)
			{
				MedViewDataHandler mVDH = MedViewDataHandler.instance();

				String prop = ERROR_WRITE_RTF_LS_PROPERTY;

				String err = mVDH.getLanguageString(prop);

				mVD.createAndShowErrorDialog(owner, err);
			}
			catch (BadLocationException bLE)
			{
				String err = "BadLocationException thrown in WriteRTFAction";

				mVD.createAndShowErrorDialog(owner, err);
			}
		}
	}

	public void setEditor(JEditorPane editor)
	{
		this.editor = editor;
	}

	public SaveAsRTFAction()
	{
		this(null);
	}

	public SaveAsRTFAction(JEditorPane editor)
	{
		super(ACTION_SAVE_AS_RTF_LS_PROPERTY);

		this.editor = editor;
	}

	private JEditorPane editor;
}
