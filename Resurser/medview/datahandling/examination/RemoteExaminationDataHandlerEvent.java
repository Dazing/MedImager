package medview.datahandling.examination;

import java.io.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

public class RemoteExaminationDataHandlerEvent implements Serializable
{

	public PatientIdentifier getPatient()
	{
		return patient;
	}

	public String getClientHost()
	{
		return clientHost;
	}

	public ExaminationIdentifier getIdentifier()
	{
		return identifier;
	}

	public String getServerName()
	{
		return serverName;
	}



	public void setPatient(PatientIdentifier patient)
	{
		this.patient = patient;
	}

	public void setClientHost(String host)
	{
		this.clientHost = host;
	}

	public void setIdentifier(ExaminationIdentifier id)
	{
		this.identifier = id;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}



	public RemoteExaminationDataHandlerEvent()
	{
		super();
	}

	private PatientIdentifier patient;

	private String clientHost;

	private String serverName;

	private ExaminationIdentifier identifier;
}
