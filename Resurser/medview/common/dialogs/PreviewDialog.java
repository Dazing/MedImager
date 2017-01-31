/*
 * @(#)PreviewDialog.java
 *
 * $Id: PreviewDialog.java,v 1.4 2006/04/24 14:17:01 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import medview.datahandling.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

class PreviewDialog extends AbstractDialog implements MedViewLanguageConstants
{
	public PreviewDialog(Frame owner, StyledDocument doc, String trans)
	{
		super(owner, true, new Object[] {doc, trans});
	}

	public PreviewDialog(Dialog owner, StyledDocument doc, String trans)
	{
		super(owner, true, new Object[] {doc, trans});
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
				PreviewDialog.this.dispose();
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
			BUTTON_CANCEL_PREVIEW_LS_PROPERTY
		};
	}

	protected void setupTitle()
	{
		String prop = TITLE_PREVIEW_TEMPLATE_LS_PROPERTY;

		String prefix = mVDH.getLanguageString(prop);

		String translator = (String) additionalData[1];

		setTitle(prefix + "'" + translator + "'");
	}

	private class ContentPanel extends JPanel
	{
		public ContentPanel()
		{
			setLayout(new BorderLayout());

			setPreferredSize(new Dimension(627, 858));

			setBorder(BorderFactory.createEmptyBorder(8,16,8,16));

			StyledDocument doc = (StyledDocument) additionalData[0];

			JTextPane jTextPane = new JTextPane(doc);

			jTextPane.setEditable(false);

			Insets margin = new Insets(5,5,5,5);

			jTextPane.setMargin(margin);

			Border docBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black),
				BorderFactory.createEmptyBorder(5,5,5,5));

			jTextPane.setBorder(docBorder);

			add(new JScrollPane(jTextPane), BorderLayout.CENTER);
		}
	}
}
