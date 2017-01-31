/**
 * @(#) DefaultDataComponentPackage.java
 */

package medview.common.data;

import medview.datahandling.*;

public class DefaultDataComponentPackage implements DataComponentPackage
{
	// MEMBERS
	
	private boolean usesTemplate;

	private boolean usesTranslator;

	private boolean usesExamination;
    
    private boolean usesGraph;

	
    private String packageName = null;
	
	
	private String templateLocation = null;
	
	private String translatorLocation = null;
    
    private String graphLocation = null;
	
    private String examinationLocation = null;

    private String termDefinitionsLocation = null;
	
	private String termValuesLocation = null;
	
	private String databaseLocation = null;
    
    private MedViewDataHandler mVDH = MedViewDataHandler.instance();
	
	
	// CONSTRUCTOR
	
	public DefaultDataComponentPackage( )
	{
	}
	
	
	// COMPARISON AND EQUALS
	
	public boolean equals(Object other)
	{
		if (other instanceof DataComponentPackage)
		{
			return packageName.equals(((DataComponentPackage)other).getPackageName());
		}
		else
		{
			return false;
		}
	}
	
	public int compareTo(Object other) // compare package names
	{
		if (other instanceof DataComponentPackage)
		{
			return packageName.compareTo(((DataComponentPackage)other).getPackageName());
		}
		else
		{
			return -1;
		}
	}
	
	
	// STRING VERSION
	
	public String toString()
	{
		return packageName;
	}
	
	
	// USE SET
	
	public void setUsesExamination(boolean b)
	{
		this.usesExamination = b;
	}
	
	public void setUsesTemplate(boolean b)
	{
		this.usesTemplate = b;
	}
    
    public void setUsesGraph(boolean b)
	{
		this.usesGraph = b;
	}
    
    public void setUsesTranslator(boolean b)
	{
		this.usesTranslator = b;
	}
	
	// USE GET
	
	public boolean usesExamination()
	{
		return usesExamination;
	}

	public boolean usesTemplate()
	{
		return usesTemplate;
	}
	
    public boolean usesGraph()
	{
		return usesGraph;
	}
	public boolean usesTranslator()
	{
		return usesTranslator;
	}
	
	// SETTING LOCATIONS
	
	public void setGraphLocation( String location )
	{
		this.graphLocation = location;
	}
    
    public void setTemplateLocation( String location )
	{
		this.templateLocation = location;
	}
	
	public void setTranslatorLocation( String location )
	{	
		this.translatorLocation = location;
	}
	
	public void setExaminationLocation( String location )
	{
		this.examinationLocation = location;
	}
	 
    public void setTermDefinitionsLocation(String termDefinitionsLocation)
    {
        this.termDefinitionsLocation = termDefinitionsLocation;
    }
    
    public void setTermValuesLocation(String termValuesLocation)
    {
        this.termValuesLocation = termValuesLocation;
    }

    public void setDatabaseLocation(String databaseLocation)
    {
        this.databaseLocation = databaseLocation;
    }
    
    // OBTAINING LOCATIONS
	public String getGraphLocation( )
	{
		return graphLocation;
	}
    
    public String getTemplateLocation( )
	{
		return templateLocation;
	}

	public String getTranslatorLocation( )
	{
		return translatorLocation;
	}

	public String getExaminationLocation( )
	{
		return examinationLocation;
	}
    
    public String getTermDefinitionsLocation()
    {
        return termDefinitionsLocation;
    }
    
    public String getTermValuesLocation()
    {
        return termValuesLocation;
    }
    
    public String getDatabaseLocation()
    {
        return databaseLocation;
    }
	// PACKAGE NAME
	
	public String getPackageName( )
	{
		return packageName;
	}
	
	public void setPackageName( String name )
	{
		this.packageName = name;
	}

}
