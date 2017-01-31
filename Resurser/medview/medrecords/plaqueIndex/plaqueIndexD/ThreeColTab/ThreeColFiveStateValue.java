package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;

import medview.medrecords.data.*;

import misc.gui.components.*;

public class ThreeColFiveStateValue extends ThreeColValue
{
	public ThreeColFiveStateValue()
	{
		mValues = new Object[3];

		for (int ctr=0; ctr<mValues.length; ctr++)
		{
			mValues[ctr] = new Integer(-1);		// -1 -> not checked
		}
	}
}