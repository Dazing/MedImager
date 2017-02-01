package medview.datahandling;

import java.io.*;

import java.util.EventObject;

/**
 * @author Fredrik Lindahl
 * 
 * @version 1.0
 */
public class RemotePCodeGeneratorEvent implements Serializable
{
	private String pid;
	
	private String pCode;
	
	private String host;
	
	public RemotePCodeGeneratorEvent()
	{
	}
	
	public void setPID(String pid)
	{
		this.pid = pid;
	}
	
	public void setPCode(String pCode)
	{
		this.pCode = pCode;
	}
	
	public void setClientHost(String host)
	{
		this.host = host;
	}
	
	public String getPID()
	{
		return this.pid;
	}
	
	public String getPCode()
	{
		return this.pCode;
	}
	
	public String getClientHost()
	{
		return this.host;
	}	
}
