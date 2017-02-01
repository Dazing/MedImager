/*
 * @(#)TextDialog.java
 *
 * $Id: TextDialog.java,v 1.5 2006/04/24 14:17:02 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: TextDialog.java,v $
 * Revision 1.5  2006/04/24 14:17:02  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/05/18 18:21:04  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.3  2002/11/01 13:39:07  lindahlf
 * Updates to MedSummary and SummaryCreator, also some template and translator files for Nils and Goran. // Fredrik
 *
 * Revision 1.2  2002/10/25 10:48:17  zachrisg
 * Just made the class public.
 *
 */

package medview.common.dialogs;

import medview.datahandling.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

public class TextDialog extends AbstractDialog implements MedViewLanguageConstants
{
	public TextDialog(Frame owner, StyledDocument doc, String title, boolean modal)
	{
		super(owner, modal, new Object[] {doc, title});
	}

	public TextDialog(Dialog owner, StyledDocument doc, String title, boolean modal)
	{
		super(owner, modal, new Object[] {doc, title});
	}

	public Object getObjectData()
	{
		return null;
	}

	protected JPanel createMainPanel()
	{
		return new ContentPanel();
	}

	protected void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TextDialog.this.dispose();
			}
		});
	}

	protected String getTitleLS()
	{
		return null;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_CLOSE_LS_PROPERTY
		};
	}

	protected void init()
	{
		setResizable(false);
	}

	protected void setupTitle()
	{
		setTitle((String)additionalData[1]);
	}

	private class ContentPanel extends JPanel
	{
		public ContentPanel()
		{
			setLayout(new BorderLayout());

			setPreferredSize(new Dimension(600,500));

			StyledDocument doc = (StyledDocument) additionalData[0];

			JTextPane jTextPane = new JTextPane(doc);

			jTextPane.setEditable(false);

			add(new JScrollPane(jTextPane), BorderLayout.CENTER);
		}
	}
}
