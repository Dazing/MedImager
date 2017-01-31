package medview.medserver.model;

import java.util.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

/**
 * An event class associated with method calls
 * to the MedServerCommunicationListener methods.
 * @author Fredrik Lindahl
 */
public class MedServerCommunicationEvent extends EventObject
{

	/**
	 * Sets the patient associated with the
	 * event. The default value is null.
	 */
	public void setPatient(PatientIdentifier patient)
	{
		this.patient = patient;
	}

	/**
	 * Sets the examination associated with the
	 * event. The default value is null.
	 */
	public void setExamination(ExaminationIdentifier id)
	{
		this.examination = id;
	}

	/**
	 * Sets the term associated with the
	 * event. The default value is null.
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}

	/**
	 * Sets the value associated with the
	 * event. The default value is null.
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * Sets the client host associated with
	 * the event. The default value is null.
	 */
	public void setClientHost(String host)
	{
		this.clientHost = host;
	}

	/**
	 * Sets the rmi log line associated with
	 * the event. The default value is null.
	 */
	public void setLogLine(String line)
	{
		this.logLine = line;
	}
	
	/**
	 * Sets the pcode associated with the
	 * event. Default value is null.
	 */
	public void setPCode(String pCode)
	{
		this.pCode = pCode;
	}
	
	/**
	 * Sets the pid associated with the event.
	 * Default value is null.
	 */
	public void setPID(String pid)
	{
		this.pid = pid;
	}


	/**
	 * Obtains the patient associated with
	 * the event. The default value is null.
	 */
	public PatientIdentifier getPatient()
	{
		return patient;
	}

	/**
	 * Obtains the examination associated with
	 * the event. The default value is null.
	 */
	public ExaminationIdentifier getExamination()
	{
		return examination;
	}

	/**
	 * Obtains the term associated with
	 * the event. The default value is null.
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * Obtains the value associated with
	 * the event. The default value is null.
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * Obtains the client host associated with
	 * the event. The default value is null.
	 */
	public String getClientHost()
	{
		return clientHost;
	}

	/**
	 * Obtains the rmi log line associated with
	 * the event. The default value is null.
	 */
	public String getLogLine()
	{
		return logLine;
	}
	
	/**
	 * Returns the pcode associated with the
	 * event. Default value is null.
	 */
	public String getPCode()
	{
		return pCode;
	}
	
	/**
	 * Returns the pid associated with the
	 * event. Default value is null.
	 */
	public String getPID()
	{
		return pid;
	}


	/**
	 * Constructs a MedServerCommunicationEvent with
	 * the specified source.
	 */
	public MedServerCommunicationEvent(Object source)
	{
		super(source);
	}

	private String pid = null;
	
	private String pCode = null;

	private String term = null;

	private Object value = null;

	private String logLine = null;

	private String clientHost = null;

	private PatientIdentifier patient = null;

	private ExaminationIdentifier examination = null;

}
