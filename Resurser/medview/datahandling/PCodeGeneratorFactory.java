/**
 * @(#) PCodeGeneratorFactory.java
 */

package medview.datahandling;

/**
 * A factory singleton class for generating pcode
 * generators used by the medview datahandling layer.
 * 
 * <p>Copyright: Copyright (c) 2004</p> 
 * 
 * <p>Company: The MedView Project</p>
 * 
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class PCodeGeneratorFactory implements MedViewDataConstants
{
	// MEMBERS
	
	private PCodeGenerator currentPCodeGenerator;
	
	private PCodeGenerator defaultPCodeGenerator;
	
	private static PCodeGeneratorFactory instance;
	
	// CONSTRUCTORS
	
	private PCodeGeneratorFactory() 
	{
		
	}
	
	// CURRENT PCODE GENERATOR
	
	/**
	 * Obtain the current pcode generator in use.
	 * @return PCodeGenerator the pcode generator 
	 * being used at the moment.
	 */
	public PCodeGenerator getPCodeGenerator( )
	{
		if (currentPCodeGenerator == null)
		{
			return getDefaultPCodeGenerator();
		}
		else
		{
			return currentPCodeGenerator;
		}
	}
	
	/**
	 * Sets the pcode generator to use.
	 * @param gen PCodeGenerator The pcode generator
	 * that should be used.
	 */
	public void setPCodeGeneratorToUse(PCodeGenerator gen)
	{
		this.currentPCodeGenerator = gen;
	}
	
	/**
	 * Obtains the current pcode generators class
	 * name as a String.
	 * @return String the class name of the pcode
	 * generator currently in use.
	 */
	public String getCurrentPCodeGeneratorClassName( )
	{
		return getPCodeGenerator().getClass().getName();
	}
	
	// DEFAULT PCODE GENERATOR
	
	/**
	 * Obtains the pcode generator used if none has
	 * been explicitly set.
	 * @return PCodeGenerator the default pcode generator
	 * being used.
	 */
	public PCodeGenerator getDefaultPCodeGenerator( )
	{
		if (defaultPCodeGenerator == null)
		{
			String c = DEFAULT_PCODE_GENERATOR_CLASS;

			try
			{
				// construct and set up the default pcode generator
				
				defaultPCodeGenerator = (PCodeGenerator) Class.forName(c).newInstance();
				
				if (MedViewDataHandler.instance().isExaminationDataLocationSet())
				{
					String loc = MedViewDataHandler.instance().getExaminationDataLocation();
					
					defaultPCodeGenerator.setExaminationDataLocation(loc);
				}
				
				if (MedViewDataHandler.instance().isPCodeNRGeneratorLocationSet())
				{					
					String loc = MedViewDataHandler.instance().getPCodeNRGeneratorLocation();
					
					defaultPCodeGenerator.setNumberGeneratorLocation(loc);
				}
				
				if (MedViewDataHandler.instance().isUserIDSet())
				{
					String id = MedViewDataHandler.instance().getUserID();
					
					defaultPCodeGenerator.setGeneratedPCodePrefix(id);
				}
			}
			catch (Exception e) 
			{ 
				e.printStackTrace();
			}
		}

		return defaultPCodeGenerator;
	}
	
	/**
	 * Obtains the class name of the default pcode 
	 * generator used if none has been explicitly set.
	 * @return String the class name of the default
	 * pcode generator.
	 */
	public String getDefaultPCodeGeneratorClassName( )
	{
		return getDefaultPCodeGenerator().getClass().getName();
	}
	
	// SINGLETON INSTANCE METHOD
	
	/**
	 * Obtains the singleton instance of the
	 * pcode generator factory.
	 * @return PCodeGeneratorFactory the singleton
	 * instance of the pcode generator factory.
	 */
	public static PCodeGeneratorFactory instance( )
	{
		if (instance == null)
		{
			instance = new PCodeGeneratorFactory();
		}
		
		return instance;
	}
}
