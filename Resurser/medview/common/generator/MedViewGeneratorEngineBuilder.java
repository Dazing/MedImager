/*
 * @(#)MedViewGeneratorEngineBuilder.java
 *
 * $Id: MedViewGeneratorEngineBuilder.java,v 1.4 2007/04/09 15:14:39 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.generator;

import se.chalmers.cs.medview.docgen.*;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.MedViewLanguageConstants;
import medview.datahandling.examination.ExaminationIdentifier;

public class MedViewGeneratorEngineBuilder extends GeneratorEngineBuilder
{
	// COMPONENT BUILD

	public void buildIdentifiers( ExaminationIdentifier[] ids )
	{
		if ((ids == null) || (ids.length == 0))
		{
			removeIdentifiers();

			return;
		}

		((MedViewGeneratorEngine)engine).setIdentifiers(ids);

		identifiersBuilt = true;

		checkIfShouldFireAllBuilt(); // defined in superclass
	}


	// OBTAINING BUILT COMPONENTS

	public ExaminationIdentifier[] getBuiltIdentifiers() throws NotBuiltException
	{
		if (identifiersBuilt)
		{
			return ((MedViewGeneratorEngine)engine).getIdentifiers();
		}
		else
		{
			throw new NotBuiltException();
		}
	}


	// COMPONENT REMOVAL

	public void removeAllBuilt( )
	{
		super.removeAllBuilt();

		removeIdentifiers();
	}

	public void removeIdentifiers( )
	{
		identifiersBuilt = false;

		checkIfShouldFireFurtherRequired(); // defined in superclass
	}


	// ENGINE BUILD

	public boolean furtherElementsRequired()
	{
		return (super.furtherElementsRequired() || !identifiersBuilt);
	}

	/**
	 * This method is called once during construction.
	 * @return GeneratorEngine
	 * @throws CouldNotBuildEngineException
	 */
	protected GeneratorEngine buildEngine( ) throws CouldNotBuildEngineException {
		GeneratorEngine engine;
		
		String setPropertyValue = System.getProperty(INSTANCE_PROPERTY);
		
		if (setPropertyValue == null) {
			engine = new DefaultMedViewGeneratorEngine();
		}
		else {
			try {
				
				engine = (MedViewGeneratorEngine) Class.forName(setPropertyValue).newInstance(); // -> Exceptions
				
			} catch (Exception exc) {
				exc.printStackTrace();
				
				System.exit(1); // fatal error
				
				return null; // unreachable
			}
		}
		MedViewDataHandler mVDH = MedViewDataHandler.instance();
		
		engine.setMessage(GeneratorEngine.SPAWNING_NOTIFiCATION_MESSAGE, 
						  mVDH.getLanguageString(MedViewLanguageConstants.LABEL_BUILDING_PARSE_TREES_LS_PROPERTY));

		engine.setMessage(GeneratorEngine.BUILDING_NOTIFiCATION_MESSAGE, 
						  mVDH.getLanguageString(MedViewLanguageConstants.LABEL_PARSING_EXAMINATIONS_LS_PROPERTY));
		
		engine.setMessage(GeneratorEngine.PARSING_NOTIFiCATION_MESSAGE, 
						  mVDH.getLanguageString(MedViewLanguageConstants.LABEL_PARSING_TREES_LS_PROPERTY));
		
		return engine;
	}


	// CONSTRUCTOR

	public MedViewGeneratorEngineBuilder()
	{
		super();
	}

	/**
	 * Flag indicating if the identifiers have been
	 * built upon the engine yet.
	 */
	protected boolean identifiersBuilt = false;
}
