/**
 * $Id: ProgressDialog.java,v 1.7 2006/04/24 14:17:01 lindahlf Exp $
 *
 * $Log: ProgressDialog.java,v $
 * Revision 1.7  2006/04/24 14:17:01  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.6  2005/06/03 15:45:18  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/11/24 15:18:23  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/10/21 12:13:00  erichson
 * Changed indeterminate from a separate boolean to mapping to the progressBar
 *
 * Revision 1.3  2004/10/21 12:10:20  erichson
 * Updated with (set/get)Indeterminate since ProgressNotifiable interface was updated.
 *
 */

package medview.common.dialogs;

import java.awt.*;

import javax.swing.*;

import medview.datahandling.*;

import misc.foundation.*;

import misc.gui.utilities.*;

/**
 * The ProgressDialog class implements the ProgressNotifiable
 * interface, i.e. its purpose is to display a dialog to the
 * user and notify of progress. All methods should be called
 * from a thread *other* than the event dispatch thread.
 *
 * The dialog does not hide or show itself, so this is up to
 * the calling parties to do. This is because of event dispatch
 * thread issues making it difficult to run a progress dialog
 * and also wait for the progress to continue before continuing
 * execution. The dialog class updates the visual appearance
 * of the dialog after each method call, placing the actual
 * update on the event dispatch thread and then returning.
 *
 * @author Fredrik Lindahl
 */
public class ProgressDialog extends AbstractDialog implements MedViewLanguageConstants, ProgressNotifiable
{
	private int total;

	private int current;

	private String description;

	private JProgressBar progressBar;

	private JLabel descLabel;

	public ProgressDialog(Frame owner)
	{
		super(owner, false, null);

		buttons[0].setEnabled(false);
	}

	public ProgressDialog(Dialog owner)
	{
		super(owner, false, null);

		buttons[0].setEnabled(false);
	}

	/**
	 * Invoked from a thread other than the event
	 * dispatch thread.
	 */
	public void setCurrent(final int c)
	{
		this.current = c;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				progressBar.setValue(c);
			}
		});
	}

	/**
	 * Invoked from a thread other than the event
	 * dispatch thread.
	 */
	public void setTotal(final int t)
	{
		this.total = t;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				progressBar.setMaximum(t);
			}
		});
	}

	/**
	 * Invoked from a thread other than the event
	 * dispatch thread.
	 */
	public void setDescription(final String d)
	{
		this.description = d;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				descLabel.setText(d);
			}
		});
	}

	/**
	 * Returns the current progress value as shown
	 * in the dialog.
	 */
	public int getCurrent()
	{
		return this.current;
	}

	/**
	 * Returns the current total value as shown in
	 * the dialog.
	 */
	public int getTotal()
	{
		return this.total;
	}

	/**
	 * Returns the current description as shown (or
	 * not) in the dialog.
	 */
	public String getDescription()
	{
		return this.description;
	}

        public boolean isIndeterminate()
        {
		return progressBar.isIndeterminate();
        }

        public void setIndeterminate(boolean indeterminate)
        {
		progressBar.setIndeterminate(indeterminate);
        }

	protected JPanel createMainPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		int both = GridBagConstraints.BOTH;

		int west = GridBagConstraints.WEST;

		JPanel retPanel = new JPanel(gbl);

		progressBar = new JProgressBar();

		descLabel = new JLabel();

		Dimension dim = new Dimension(300,20);

		descLabel.setPreferredSize(dim);

		GUIUtilities.gridBagAdd(retPanel, descLabel, 0,0,1,1,0,0, west, new Insets(0,0,4,0),both);

		GUIUtilities.gridBagAdd(retPanel, progressBar, 0,1,1,1,0,0, west,new Insets(0,0,0,0),both);

		return retPanel;
	}

	protected String getTitleLS()
	{
		return TITLE_OBTAINING_INFORMATION_LS_PROPERTY;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

	protected void registerListeners() {}

	public Object getObjectData()
	{
		return null;
	}
}
