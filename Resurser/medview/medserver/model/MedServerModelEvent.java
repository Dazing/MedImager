package medview.medserver.model;

import java.util.*;

import medview.datahandling.examination.*;

/**
 * An event class associated with method calls
 * to the MedServerModelListener methods.
 * @author Fredrik Lindahl
 */
public class MedServerModelEvent extends EventObject
{

	/**
	 * Sets the term associated with the
	 * event. The default value is null.
	 */
	void setTerm(String term)
	{
		this.term = term;
	}

	/**
	 * Sets the value associated with the
	 * event. The default value is null.
	 */
	void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * Sets the location associated with the
	 * event. The default value is null.
	 */
	void setLocation(String loc)
	{
		this.location = loc;
	}

	/**
	 * Sets the location ID associated with the
	 * event. The default value is null.
	 */
	void setLocationID(String locID)
	{
		this.locationID = locID;
	}

	/**
	 * Sets the class name associated with the
	 * event. The default value is null.
	 */
	void setClassName(String cN)
	{
		this.className = cN;
	}

	/**
	 * Sets the examination identifier associated
	 * with the event. The default value is null.
	 */
	void setExamination(ExaminationIdentifier id)
	{
		this.examination = id;
	}
	
	/**
	 * Sets the prefix associated with the event. The
	 * default value is null.
	 * @param prefix String
	 */
	void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}


	/**
	 * Obtains the term associated with the
	 * event. The default value is null.
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * Obtains the value associated with the
	 * event. The default value is null.
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * Obtains the location associated with the
	 * event. The default value is null.
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Obtains the location ID associated with
	 * the event. The default value is null.
	 */
	public String getLocationID()
	{
		return locationID;
	}

	/**
	 * Obtains the class name associated with
	 * the event. The default value is null.
	 */
	public String getClassName()
	{
		return className;
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
	 * Obtains the prefix associated with the
	 * event. Default value is null.
	 * @return String
	 */
	public String getPrefix()
	{
		return this.prefix;
	}


	/**
	 * Constructs a MedServerModelEvent with
	 * the specified source.
	 */
	public MedServerModelEvent(Object source)
	{
		super(source);
	}

	private String term = null;

	private Object value = null;

	private String prefix = null;

	private String location = null;

	private String className = null;

	private String locationID = null;

	private ExaminationIdentifier examination = null;

}
