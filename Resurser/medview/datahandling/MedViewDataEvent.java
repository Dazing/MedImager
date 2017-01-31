/*
 * @(#)MedViewDataEvent.java
 *
 * $Id: MedViewDataEvent.java,v 1.8 2004/02/19 18:21:26 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

import medview.datahandling.examination.*;

/**
 * An event class for notifying datahandler
 * listeners of events relating to data fired
 * from the datahandling layer.
 *
 * @author Fredrik Lindahl
 */
public class MedViewDataEvent extends EventObject
{

// -----------------------------------------------
// ********************* SET *********************
// -----------------------------------------------

	/**
	 * Sets the term associated with the
	 * event. Default is null.
	 */
	void setTerm(String term) // package
	{
		this.term = term;
	}

	/**
	 * Sets the value associated with the
	 * event. Default is null.
	 */
	void setValue(Object value) // package
	{
		this.value = value;
	}

	/**
	 * Sets the class name associated with
	 * the event. Default is null.
	 */
	void setClassName(String className) // package
	{
		this.className = className;
	}

	/**
	 * Sets the location associated with
	 * the event. Default is null.
	 */
	void setLocation(String loc)
	{
		this.location = loc;
	}

	/**
	 * Sets the location ID associated with
	 * the event. Default is null.
	 */
	void setLocationID(String locID)
	{
		this.locationID = locID;
	}

	/**
	 * Sets the identifier associated with
	 * the event. Default is null.
	 */
	void setIdentifier(ExaminationIdentifier id)
	{
		this.id = id;
	}

	/**
	 * Sets the user id associated with the
	 * event. Default is null.
	 */
	void setUserID(String userID)
	{
		this.userID = userID;
	}

	/**
	 * Sets the user name associated with
	 * the event. Default is null.
	 */
	void setUserName(String userName)
	{
		this.userName = userName;
	}

// -----------------------------------------------
// ***********************************************
// -----------------------------------------------



// -----------------------------------------------
// ********************* GET *********************
// -----------------------------------------------

	/**
	 * Obtains the term associated with the
	 * cause of the event. Could be null if
	 * the event was fired for other reasons.
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * Obtains the value associated with the
	 * cause of the event. Could be null if
	 * the event was fired for other reasons.
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * Obtains the class name associated with
	 * the cause of the event. Could be null if
	 * the event was fired for other reasons.
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Obtains the location associated with
	 * the cause of the event. Could be null if
	 * the event was fired for other reasons.
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Obtains the location ID associated with
	 * the cause of the event. Could be null if
	 * the event was fired for other reasons.
	 */
	public String getLocationID()
	{
		return locationID;
	}

	/**
	 * Obtains the associated examination
	 * identifier. Could be null if the event
	 * was fired for other reasons.
	 */
	public ExaminationIdentifier getIdentifier()
	{
		return id;
	}

	/**
	 * Obtains the associated user id.
	 * Could be null if the event
	 * was fired for other reasons.
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * Obtains the associated user name.
	 * Could be null if the event was
	 * fired for other reasons.
	 */
	public String getUserName()
	{
		return userName;
	}

// -----------------------------------------------
// ***********************************************
// -----------------------------------------------



	/**
	 * Constructs a new data event with the
	 * specified source.
	 */
	public MedViewDataEvent(Object source)
	{
		super(source);
	}

	private String term;

	private Object value;

	private String userID;

	private String userName;

	private String className;

	private String location;

	private String locationID;

	private ExaminationIdentifier id;
}