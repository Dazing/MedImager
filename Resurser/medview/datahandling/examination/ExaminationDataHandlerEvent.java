package medview.datahandling.examination;

import java.util.*;

public class ExaminationDataHandlerEvent extends EventObject
{

	public void setLocation(String loc)
	{
		this.location = loc;
	}

	public void setIdentifier(ExaminationIdentifier id)
	{
		this.identifier = id;
	}

	public void setLocationID(String locID)
	{
		this.locationID = locID;
	}


	public String getLocation()
	{
		return location;
	}

	public ExaminationIdentifier getIdentifier()
	{
		return identifier;
	}

	public String getLocationID()
	{
		return locationID;
	}


	public ExaminationDataHandlerEvent(Object source)
	{
		super(source);
	}

	private ExaminationIdentifier identifier;

	private String locationID;

	private String location;
}