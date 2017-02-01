/**
 * @(#) DataComponentPackage.java
 */

package medview.common.data;

public interface DataComponentPackage extends Comparable
{
	public String getPackageName( );

	public void setPackageName( String name );

	public void setUsesTemplate(boolean b);
	
	public void setUsesTranslator(boolean b);
    
    public void setUsesGraph(boolean b);
	
    public boolean usesTemplate();

	public boolean usesTranslator();
    
    public boolean usesGraph();
	
	
    public void setTemplateLocation( String location );
	
	public void setTranslatorLocation( String location );
	
	public void setExaminationLocation( String location );
    
    public void setGraphLocation( String location );
	
    public void setTermValuesLocation( String location );
	
	public void setTermDefinitionsLocation( String location );
	
	public void setDatabaseLocation( String location );
    
    public String getGraphLocation( );
    
    public String getTemplateLocation( );
	
	public String getTranslatorLocation( );
	
	public String getExaminationLocation( );
    
	public String getTermValuesLocation( );
	
	public String getTermDefinitionsLocation( );
	
	public String getDatabaseLocation( );
}
