/*
 * @(#) AbstractDialog.java
 *
 * $Id: AbstractDialog.java,v 1.14 2010/06/28 08:46:17 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import medview.datahandling.*;

import java.awt.*;
import javax.swing.*;

/**
 * An abstract base class for dialogs within the
 * MedView context. Some methods must be implemented
 * by the subclasses, while others have a default
 * implementation provided in this class.
 * @author Fredrik Lindahl
 */
abstract public class AbstractDialog extends MedViewDialog implements MedViewLanguageConstants, MedViewMediaConstants
{

// --------------------------------------------------------------------
// ***************** METHODS SUBCLASSES MUST OVERRIDE *****************
// --------------------------------------------------------------------

	/**
	 * Creates the main panel displayed in
	 * the center of the dialog. This method
	 * must be overridden by subclasses and
	 * return the relevant panel for the dialog
	 * subclass. The panel will be spaced to
	 * the edge by a pixel amount specified by
	 * the getMainPanelHorizontalSpacing()
	 * method (which may be overridden if
	 * necessary for a dialog with special needs).
	 */
	protected abstract JPanel createMainPanel();

	/**
	 * Attach all listeners in this method. The
	 * listeners for the buttons should always
	 * be attached here, but other listeners can
	 * also be attached in the context of this
	 * method.
	 */
	protected abstract void registerListeners();

	/**
	 * Obtains the language string for the
	 * title of the dialog. This method must
	 * be overridden by subclasses in order
	 * to obtain a localized version of a
	 * title of the dialog subclass. Note
	 * that the title <b>can</b> be set
	 * after construction with the method
	 * setTitleLS(), which will remove the
	 * language listener attached to the data
	 * layer at construction and attach a new
	 * one listening for the specified
	 * language string.
	 */
	protected abstract String getTitleLS();

	/**
	 * Obtains an array of language strings
	 * to use for locating the localized
	 * language strings to use on the faces
	 * of the buttons used in the dialog.
	 * The length of the returned array
	 * specifies the amount of buttons used
	 * in the dialog.
	 */
	protected abstract String[] getButtonFacesLS();

// --------------------------------------------------------------------
// ********************************************************************
// --------------------------------------------------------------------

// --------------------------------------------------------------------
// ****************** METHODS SUBCLASSES CAN OVERRIDE *****************
// --------------------------------------------------------------------

	/**
	 * Retrieves the preferred button
	 * location (can be LEFT, RIGHT,
	 * or CENTER). Default is CENTER.
	 * This method can be overridden
	 * by subclasses that has special
	 * needs.
	 */
	protected int getButtonLocation()
	{
		return CENTER;
	}

	/**
	 * Retrieves the preferred button
	 * dimension. The default is a
	 * dimension of 80x25. This method
	 * can be overridden by subclasses
	 * that has special needs.
	 */
	protected Dimension getButtonDimension()
	{
		return new Dimension(80, 25);
	}

	/**
	 * Retrieves the index of the button
	 * that should be the default. Default
	 * is 0. This method can be overridden
	 * by subclasses that has special needs.
	 */
	protected int getDefaultButtonIndex()
	{
		return 0;
	}

	/**
	 * Obtains the horizontal spacing used
	 * for the main panel (the spacing
	 * between the main panel and the edges
	 * of the dialog). The default is a
	 * horizontal spacing of 25 pixels. This
	 * method can be overridden by subclasses
	 * with special needs.
	 */
	protected int getMainPanelHorizontalSpacing()
	{
		return 25;
	}

	/**
	 * Returns the horizontal spacing between
	 * the bars (top and bottom) and the edge
	 * of the dialog. This method may not be
	 * overridden, but can be used by sub-
	 * classes that has set a main panel
	 * horizontal spacing of zero to determine
	 * where they might place information in
	 * order to make it look good.
	 */
	protected final int getBarHSpacing()
	{
		return 2;
	}

	/**
	 * Returns the vertical spacing between
	 * the bars (top and bottom) and the edge
	 * of the dialog. This method may not be
	 * overridden, but can be used by sub-
	 * classes to set proper spacing values.
	 */
	protected final int getBarVSpacing()
	{
		return 15;
	}

	/**
	 * Sets up the title to use for the dialog,
	 * the default is that the title is set to
	 * the language resource returned by the
	 * getTitleLS() method (which must be
	 * overridden by subclasses). If another
	 * title is to be used, subclasses can
	 * override this method.
	 */
	protected void setupTitle()
	{
		String titleLS = getTitleLS();

		if (titleLS != null)
		{
			setTitle(mVDH.getLanguageString(titleLS));
		}
		else
		{
			setTitle("No title set");
		}
	}

	/**
	 * If a subclass needs to do initializations of
	 * members that are used in the factory method
	 * framework, they can place these here. This
	 * method is called before any other framework
	 * method of the Abstract Dialog class.
	 */
	protected void initSubClassMembers()
	{
	}

// --------------------------------------------------------------------
// ********************************************************************
// --------------------------------------------------------------------

// --------------------------------------------------------------------
// ************************* UTILITY METHODS **************************
// --------------------------------------------------------------------

	/**
	 * Public utility method for setting the
	 * dialog title's language string. Note
	 * that there are currently two ways of
	 * setting a dialog's title. The main way
	 * (and most used) is by letting the
	 * subclass (whose title language string
	 * is usually known) override the getTitleLS()
	 * method and from it return the language
	 * string for the dialog. If there exist some
	 * special need for setting the title language
	 * string after construction, you have to use
	 * this method.
	 *
	 * REMOVE THIS METHOD ??? // Fredrik
	 */
	public void setTitleLS(String lS)
	{
		setTitle(mVDH.getLanguageString(lS));
	}

	/**
	 * Overrides the Dialog's class show()
	 * method, but does the exactly same
	 * thing, except that the default button
	 * gets initial focus.
	 */
    @Override
	public void show()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				buttons[getDefaultButtonIndex()].requestFocus();
			}
		});

		super.show();
	}

	/**
	 * Specifies whether or not the dialog
	 * was dismissed.
	 */
	protected final void setDismissed(boolean flag)
	{
		this.wasDismissed = flag;
	}

	/**
	 * Returns whether or not the dialog was
	 * dismissed. This method is specified in
	 * the MedViewDialog interface. The default
	 * implementation returns the value currently
	 * set by the setDismissed() method.
	 */
	public final boolean wasDismissed()
	{
		return wasDismissed;
	}

	/**
	 * Lays out the dialog.
	 */
	private final void layoutDialog()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		Container contentPane = getContentPane();

		contentPane.setLayout(gbl);

		int hS = getMainPanelHorizontalSpacing();

		int bHS = getBarHSpacing();

		int bVS = getBarVSpacing();

		gbc.gridx = 0;

		gbc.gridy = 0;

		gbc.insets = new Insets(10, bHS, bVS, bHS);

		gbc.fill = GridBagConstraints.HORIZONTAL;

		contentPane.add(new JSeparator(), gbc);

		gbc.gridx = 0;

		gbc.gridy = 1;

		gbc.insets = new Insets(0, hS, 0, hS);

		gbc.fill = GridBagConstraints.BOTH;

		contentPane.add(createMainPanel(), gbc);

		gbc.gridx = 0;

		gbc.gridy = 2;

		gbc.insets = new Insets(bVS, bHS, 5, bHS);

		gbc.fill = GridBagConstraints.HORIZONTAL;

		contentPane.add(new JSeparator(), gbc);

		gbc.gridx = 0;

		gbc.gridy = 3;

		gbc.insets = new Insets(0, bHS, 5, bHS);

		gbc.fill = GridBagConstraints.NONE;

		switch (getButtonLocation())
		{
			case LEFT:
			{
				gbc.anchor = GridBagConstraints.WEST;

				break;
			}

			case RIGHT:
			{
				gbc.anchor = GridBagConstraints.EAST;

				break;
			}

			default:
			{
				gbc.anchor = GridBagConstraints.CENTER;

				break;
			}
		}

		JPanel buttonPanel = new JPanel();

		LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 0);

		buttonPanel.setLayout(layout);

		for (int ctr = 0; ctr < buttons.length; ctr++)
		{
			buttonPanel.add(buttons[ctr]);
		}

		contentPane.add(buttonPanel, gbc);
	}

// --------------------------------------------------------------------
// ********************************************************************
// --------------------------------------------------------------------

	/**
	 *
	 * @param owner Dialog
	 * @param modal boolean
	 * @param add Object[]
	 */
	public AbstractDialog(Dialog owner, boolean modal, Object[] add)
	{
		super(owner, null, modal);

		construct(owner, add);
	}

	/**
	 *
	 * @param owner Frame
	 * @param modal boolean
	 * @param add Object[]
	 */
	public AbstractDialog(Frame owner, boolean modal, Object[] add)
	{
		super(owner, null, modal);

		construct(owner, add);
	}

	/**
	 * Called by both constructors.
	 * @param owner Window
	 * @param add Object[]
	 */
	private void construct(Window owner, Object[] add)
	{
		this.mVDH = MedViewDataHandler.instance();

		this.additionalData = add;

		this.wasDismissed = true;

		initSubClassMembers();

		// default close operation (OBS - otherwise default is HIDE -> mem leak)

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// title

		setupTitle();

		// buttons

		String[] lang = getButtonFacesLS();

		buttons = new JButton[lang.length];

		Dimension buttonDim = getButtonDimension();

		for (int ctr = 0; ctr < lang.length; ctr++)
		{
			buttons[ctr] = new JButton(mVDH.getLanguageString(lang[ctr]));

			buttons[ctr].setPreferredSize(buttonDim);
		}

		int dBI = getDefaultButtonIndex();

		SwingUtilities.getRootPane(this).setDefaultButton(buttons[dBI]);

		// layout

		layoutDialog();

		// listeners

		registerListeners(); // <- subclasses must override

		// display and position dialog

		pack();

		setResizable(false);

		setLocationRelativeTo(owner);
	}

	protected JButton[] buttons;

	private boolean wasDismissed;

	protected MedViewDataHandler mVDH;

	protected Object[] additionalData;

	/**
	 * NOTE:
	 *
	 * There was a memory leak in the MedSummary application
	 * caused by instances of ImageDialog (and their associated
	 * int[] byte arrays of image data) not being garbage
	 * collected. This was caused by the AbstractDialog class
	 * registering callbacks to the MVDH class for language
	 * listening. Thus, I now removed all such language callbacks,
	 * which results in the ImageDialogs being GC'd.
	 *
	 * // Fredrik 04-06-06
	 */

	public static final int LEFT = 0;

	public static final int RIGHT = 1;

	public static final int CENTER = 2;

}
