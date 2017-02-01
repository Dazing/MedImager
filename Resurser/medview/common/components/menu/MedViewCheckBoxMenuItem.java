/*
 * @(#)MedViewCheckBoxMenuItem.java
 *
 * $Id: MedViewCheckBoxMenuItem.java,v 1.1 2004/12/08 14:46:36 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.menu;

import java.awt.event.*;

import javax.swing.*;

import medview.datahandling.*;

/**
 * Constructs a check box menu item, which can use one
 * of the two api's for properties or preferences.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewCheckBoxMenuItem extends JCheckBoxMenuItem
{
	/**
	 * This version of the constructor uses the deprecated
	 * property api for constructing the check box menu
	 * item. You should use the other one instead, although
	 * this will still work. If the specified property has
	 * not yet been set, the constructor will set it.
	 */
	public MedViewCheckBoxMenuItem(final String lS, final String prop, final boolean defSel)
	{
		super();

		this.lS = lS;

		this.propOrPref = prop;

		mVDH = MedViewDataHandler.instance();

		if (!mVDH.isPropertySet(propOrPref))
		{
			mVDH.setFlagProperty(propOrPref, defSel); // set property to default if not set
		}

		setSelected(mVDH.getFlagProperty(prop));

		setText(mVDH.getLanguageString(lS));

		list = new Listener();

		addItemListener(list);

		mVDH.addMedViewPropertyListener(list);
	}

	/**
	 * Use this version of the constructor to obtain a check box menu
	 * item using preferences (preferred new api) instead of properties
	 * (old, deprecated api - should go from this). If the specified
	 * preference has not yet been set in the preference node for the
	 * specified class, the constructor will set it.
	 * @param lS The abstract language string of check box
	 * @param pref The preference name
	 * @param defSel Default selection status of check box
	 * @param prefClass The preference class (used for lookup in prefs)
	 */
	public MedViewCheckBoxMenuItem(final String lS, final String pref, final boolean defSel, final Class prefClass)
	{
		super();

		this.lS = lS;

		this.propOrPref = pref;

		this.prefClass = prefClass;

		mVDH = MedViewDataHandler.instance();

		if (!mVDH.isUserPreferenceSet(propOrPref, prefClass)) // set preference to default if not set
		{
			mVDH.setUserBooleanPreference(propOrPref, defSel, prefClass);
		}

		setSelected(mVDH.getUserBooleanPreference(propOrPref, defSel, prefClass));

		setText(mVDH.getLanguageString(lS));

		list = new Listener();

		addItemListener(list);

		mVDH.addMedViewPropertyListener(list);
	}

	private String lS;

	private Listener list;

	private String propOrPref;

	private Class prefClass = null;

	private MedViewDataHandler mVDH;



	private class Listener extends MedViewPropertyAdapter implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			boolean sel = (e.getStateChange() == ItemEvent.SELECTED);

			if (prefClass == null) // property api
			{
				if (mVDH.isPropertySet(propOrPref))
				{
					if (mVDH.getFlagProperty(propOrPref) != sel)
					{
						mVDH.setFlagProperty(propOrPref, sel);
					}
				}
				else
				{
					mVDH.setFlagProperty(propOrPref, sel);
				}
			}
			else // preference api
			{
				if (mVDH.isUserPreferenceSet(propOrPref, prefClass))
				{
					if (mVDH.getUserBooleanPreference(propOrPref, false, prefClass) != sel)
					{
						mVDH.setUserBooleanPreference(propOrPref, sel, prefClass);
					}
				}
				else
				{
					mVDH.setUserBooleanPreference(propOrPref, sel, prefClass);
				}
			}
		}

		public void flagPropertyChanged(MedViewPropertyEvent e)
		{
			if (e.getPropertyName().equals(propOrPref))
			{
				boolean propVal = e.getFlagPropertyValue();

				if (!(propVal == isSelected()))
				{
					setSelected(propVal);
				}
			}
		}
	}
}
