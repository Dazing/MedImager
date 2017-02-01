/*
 * @(#)ImageDialog.java
 *
 * $Id: ImageDialog.java,v 1.8 2010/06/28 08:46:17 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import medview.datahandling.MedViewLanguageConstants;

class ImageDialog extends AbstractDialog implements MedViewLanguageConstants
{
	public ImageDialog(Frame owner, String title, String path)
	{
		super(owner, true, new Object[] {title, path, null});
	}

	public ImageDialog(Frame owner, String title, Image image)
	{
		super(owner, true, new Object[] {title, null, image});
	}

	public ImageDialog(Frame owner, String title, String path, boolean modal)
	{
		super(owner, modal, new Object[] {title, path, null});
	}

	public ImageDialog(Frame owner, String title, Image image, boolean modal)
	{
		super(owner, modal, new Object[] {title, null, image});
	}

	public ImageDialog(Dialog owner, String title, String path)
	{
		super(owner, true, new Object[] {title, path, null});
	}

	public ImageDialog(Dialog owner, String title, Image image)
	{
		super(owner, true, new Object[] {title, null, image});
	}

	public ImageDialog(Dialog owner, String title, String path, boolean modal)
	{
		super(owner, modal, new Object[] {title, path, null});
	}

	public ImageDialog(Dialog owner, String title, Image image, boolean modal)
	{
		super(owner, modal, new Object[] {title, null, image});
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
				ImageDialog.this.dispose();
			}
		});
	}

	protected String[] getButtonFacesLS()
	{
		return new String[] { BUTTON_CLOSE_LS_PROPERTY };
	}

	protected String getTitleLS()
	{
		return null;
	}

    @Override
	protected void setupTitle()
	{
		setTitle((String)additionalData[0]);
	}

	private class ContentPanel extends JPanel
	{
		private void layoutContent()
		{
			setLayout(new BorderLayout());

			ImageIcon icon = null;

			if (additionalData[1] == null)
			{
				icon = new ImageIcon((Image)additionalData[2]);
			}
			else
			{
				icon = new ImageIcon((String)additionalData[1]);
			}

			JLabel imageLabel = new JLabel(icon);

			imageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

			add(imageLabel, BorderLayout.CENTER);
		}

		public ContentPanel()
		{
			layoutContent();
		}
	}
}
