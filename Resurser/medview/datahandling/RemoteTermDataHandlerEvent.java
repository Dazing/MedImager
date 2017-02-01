package medview.datahandling;

import java.io.*;

public class RemoteTermDataHandlerEvent implements Serializable
{
	// GETTERS
	
	public int getType() 
	{
		return type;
	}

	public String getTerm() 
	{ 
		return term; 
	}

	public Object getValue() 
	{
		return value; 
	}

	public String getClientHost() 
	{ 
		return clientHost; 
	}

	// SETTERS

	public void setType(int type)
	{
		this.type = type;
	}

	public void setTerm(String term)
	{
		this.term = term;	
	}
	
	public void setValue(Object value)
	{
		this.value = value;		
	}
	
	public void setClientHost(String cH)
	{
		this.clientHost = cH;
	}

	// CONSTRUCTOR

	public RemoteTermDataHandlerEvent()
	{
	}

	// MEMBERS

	private int type;

	private String term;

	private Object value;

	private String clientHost;
}
