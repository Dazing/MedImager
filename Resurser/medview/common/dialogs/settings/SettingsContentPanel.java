/*
 * @(#)SettingsContentPanel.java
 *
 * $Id: SettingsContentPanel.java,v 1.8 2005/06/03 15:44:51 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import java.awt.*;

import medview.datahandling.*;

import misc.domain.*;

import misc.gui.constants.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * The abstract superclass to use for all settings
 * content panels used within the MedView framework.
 *
 * This superclass sets the layout manager to a
 * GridBagLayout, as well as provides all settings
 * content panels with a consistent spacing between
 * themselves and the surrounding tabbed container.
 * Also, references to the command queue and the
 * data handler used is provided to subclasses.
 * @author Fredrik Lindahl
 */
public abstract class SettingsContentPanel extends JPanel
{
	// TAB DESCRIPTIONS

	public abstract String getTabLS();

	public abstract String getTabDescLS();


	// METHODS CALLED WHEN SETTINGS DIALOG IS SHOWN / HIDDEN

	protected void settingsHidden()	{}

	protected abstract void settingsShown();


	// METHODS CALLED DURING CONSTRUCTION

	/**
	 * If you intend to use one of the parameters passed to the
	 * constructor in one of the hook methods, you need to place
	 * it in the sub constructor data array and then extract the
	 * reference in this method.
	 */
	protected void initSubMembers()	{}

	protected abstract void createComponents(); // hook method

	protected abstract void layoutPanel(); // hook method


	// CONSTRUCTOR(S)

	public SettingsContentPanel(CommandQueue queue)
	{
		this(queue, null);
	}

	public SettingsContentPanel(CommandQueue queue, Object[] subConstructorData)
	{
		this.subConstructorData = subConstructorData;

		this.commandQueue = queue;

		// reference to datahandler

		mVDH = MedViewDataHandler.instance();

		// layout init

		gbl = new GridBagLayout();

		gbc = new GridBagConstraints();

		setLayout(gbl);

		// border

		int cGS = GUIConstants.COMPONENT_GROUP_SPACING;

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

		setBorder(new EmptyBorder(cGS + cCS, cGS + cCS, cGS + cCS, cGS + cCS));

		// hook call - initialize submembers (non-gui components)

		initSubMembers();

		// hook call - create components (gui components)

		createComponents();

		// hook call - layout panel (layout created gui components)

		layoutPanel();
	}

	// MEMBERS

	protected GridBagLayout gbl;

	protected GridBagConstraints gbc;

	protected Object[] subConstructorData;

	protected CommandQueue commandQueue;

	protected MedViewDataHandler mVDH;

}
