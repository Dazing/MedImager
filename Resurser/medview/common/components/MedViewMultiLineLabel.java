/*
 * @(#)MedViewMultiLineLabel.java
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components;

import medview.datahandling.*;
import misc.gui.components.*;

/**
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewMultiLineLabel extends MultiLineLabel
{
	/**
	 */
	public MedViewMultiLineLabel(final String lS)
	{
		super(MedViewDataHandler.instance().getLanguageString(lS));
	}
}
