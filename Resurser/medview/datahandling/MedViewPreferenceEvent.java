/*
 * @(#)MedViewPreferenceEvent.java
 *
 * $Id: MedViewPreferenceEvent.java,v 1.2 2004/11/16 21:24:46 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public class MedViewPreferenceEvent extends EventObject
{
	public String getPreferenceName()
	{
		return prefName;
	}

	public MedViewPreferenceEvent(Object source, String pref)
	{
		super(source);

		this.prefName = pref;
	}

	private String prefName;
}
