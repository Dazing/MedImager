package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;

import medview.medrecords.data.*;

import misc.gui.components.*;

public class ThreeColThreeStateValue extends ThreeColValue
{
	public ThreeColThreeStateValue()
	{
		mValues = new Object[3];

		for (int ctr=0; ctr<mValues.length; ctr++)
		{
			mValues[ctr] = new Integer(ThreeStateButton.NEUTRAL_STATE);
		}
	}
}