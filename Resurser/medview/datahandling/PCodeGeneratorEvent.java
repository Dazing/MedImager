/*
 * @(#) PCodeGeneratorEvent.java
 *
 * $Id: PCodeGeneratorEvent.java,v 1.1 2004/01/20 19:42:18 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public class PCodeGeneratorEvent extends EventObject
{
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public void setLocation(String loc)
	{
		this.location = loc;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public String getLocation()
	{
		return location;
	}

	public PCodeGeneratorEvent(Object source)
	{
		super(source);
	}

	private String location;

	private String prefix;
}