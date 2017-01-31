package medview.common.generator;

import medview.datahandling.examination.*;

import se.chalmers.cs.medview.docgen.*;

public interface MedViewGeneratorEngine extends GeneratorEngine
{
	// SETTERS
	
	void setIdentifiers(ExaminationIdentifier[] eids);
	
	// GETTERS
	
	ExaminationIdentifier[] getIdentifiers();
	
	// CLEAR
	
	void clearIdentifiers();
}
