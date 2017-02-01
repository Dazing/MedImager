/*
 * @(#)MedViewCheckBox.java
 *
 * $Id: MedViewCheckBox.java,v 1.7 2004/12/08 14:46:34 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components;

import javax.swing.*;

import medview.datahandling.*;

/**
 * A check box that deals with language and
 * property and preference handling.
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewCheckBox extends JCheckBox
{
	/**
	 * Use this version of the constructor to use the old
	 * properties medview api. This is discouraged, since
	 * we are trying to make a transition to the preferences
	 * api.
	 */
	public MedViewCheckBox(String lS, String sProp, boolean defSel)
	{
		super();

		this.lS = lS;

		this.propOrPref = sProp;

		this.defSel = defSel;

		this.propClass = null;

		mVDH = MedViewDataHandler.instance();

		if (propOrPref != null)
		{
			if (mVDH.isPropertySet(propOrPref))
			{
				setSelected(mVDH.getFlagProperty(propOrPref));
			}
			else
			{
				mVDH.setFlagProperty(propOrPref, defSel);

				setSelected(defSel);
			}
		}

		setText(mVDH.getLanguageString(lS));

		if (sProp != null)
		{
			mVDH.addMedViewPropertyListener(new MedViewPropertyAdapter()
			{
				public void flagPropertyChanged(MedViewPropertyEvent e)
				{
					if (e.getPropertyName().equals(propOrPref))
					{
						boolean pS = mVDH.getFlagProperty(propOrPref);

						boolean tS = isSelected();

						if (pS != tS)
						{
							setSelected(pS);
						}
					}
				}
			});
		}
	}

	/**
	 * Use this version of the constructor to use the new and
	 * preferred preferences api in medview.
	 */
	public MedViewCheckBox(String lS, String pref, boolean defSel, Class c)
	{
		super();

		this.lS = lS;

		this.propOrPref = pref;

		this.defSel = defSel;

		this.propClass = c;

		mVDH = MedViewDataHandler.instance();

		if (propOrPref != null)
		{
			if (mVDH.isUserPreferenceSet(propOrPref, propClass))
			{
				setSelected(mVDH.getUserBooleanPreference(propOrPref, true, propClass));
			}
			else
			{
				mVDH.setUserBooleanPreference(propOrPref, defSel, propClass);

				setSelected(defSel);
			}
		}

		setText(mVDH.getLanguageString(lS));

		if (propOrPref != null)
		{
			mVDH.addMedViewPreferenceListener(new MedViewPreferenceListener()
			{
				public void userPreferenceChanged(MedViewPreferenceEvent e)
				{
					if (e.getPreferenceName().equals(propOrPref))
					{
						boolean prefSet = mVDH.getUserBooleanPreference(propOrPref, true, propClass);

						boolean selected = isSelected();

						if (prefSet != selected)
						{
							setSelected(prefSet);
						}
					}
				}

				public void systemPreferenceChanged(MedViewPreferenceEvent e) {}
			});
		}
	}

	private String lS;

	private String propOrPref;

	private Class propClass;

	private boolean defSel;

	private MedViewDataHandler mVDH;

}
