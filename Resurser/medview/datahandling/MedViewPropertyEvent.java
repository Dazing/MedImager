/*
 * @(#)MedViewPropertyEvent.java
 *
 * $Id: MedViewPropertyEvent.java,v 1.5 2002/11/01 13:39:15 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public class MedViewPropertyEvent extends EventObject
{
	public String getPropertyName()
	{
		return propName;
	}

	public String getUserPropertyValue()
	{
		return userProp;
	}

	public boolean getFlagPropertyValue()
	{
		return flagProp;
	}

	public MedViewPropertyEvent(Object source, String name, String value)
	{
		super(source);

		this.propName = name;

		this.userProp = value;
	}

	public MedViewPropertyEvent(Object source, String name, boolean value)
	{
		super(source);

		this.propName = name;

		this.flagProp = value;
	}

	private String propName;

	private String userProp;

	private boolean flagProp;
}