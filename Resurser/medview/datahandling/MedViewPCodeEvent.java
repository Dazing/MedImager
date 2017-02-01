/*
 * @(#)MedViewPCodeEvent.java
 *
 * $Id: MedViewPCodeEvent.java,v 1.2 2004/11/04 12:04:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

import medview.datahandling.examination.*;

public class MedViewPCodeEvent extends EventObject
{
	// SETTERS
	
	public void setGeneratedPCodePrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	public void setNRGeneratorLocation(String loc)
	{
		this.location = loc;
	}
	
	public void setClassName(String className)
	{
		this.className = className;
	}
	
	
	// GETTERS
	
	public String getGeneratedPCodePrefix()
	{
		return prefix;
	}

	public String getNRGeneratorLocation()
	{
		return location;
	}

	
	// CONSTRUCTOR

	public MedViewPCodeEvent(Object source)
	{
		super(source);
	}

	// MEMBERS

	private String	prefix;

	private String location;
	
	private String className;
}
