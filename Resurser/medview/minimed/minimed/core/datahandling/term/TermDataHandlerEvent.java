package minimed.core.datahandling.term;

import java.util.EventObject;

public class TermDataHandlerEvent extends EventObject
{

	public void setTerm(String term)
	{
		this.term = term;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public void setLocation(String loc)
	{
		this.loc = loc;
	}


	public String getTerm()
	{
		return term;
	}

	public int getType()
	{
		return type;
	}

	public Object getValue()
	{
		return value;
	}

	public String getLocation()
	{
		return loc;
	}


	public TermDataHandlerEvent(Object source)
	{
		super(source);
	}

	private int type;

	private String loc;

	private String term;

	private Object value;
}
