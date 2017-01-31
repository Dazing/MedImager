/*
 * @(#)DefaultMedViewGeneratorEngine.java
 *
 * $Id: DefaultMedViewGeneratorEngine.java,v 1.1 2005/02/24 14:34:46 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.generator;

import medview.datahandling.examination.*;

import se.chalmers.cs.medview.docgen.*;

public class DefaultMedViewGeneratorEngine extends DefaultGeneratorEngine implements
	MedViewGeneratorEngine
{
	
	// ADDITIONAL SETTERS
	
	public void setIdentifiers( ExaminationIdentifier[] ids )
	{
		this.identifiers = ids;
	}
	
	// ADDITIONAL GETTERS
	
	public ExaminationIdentifier[] getIdentifiers()
	{
		return identifiers;
	}
	
	// ADDITIONAL CLEAR

	public void clearIdentifiers()
	{
		this.identifiers = null;
	}

	// OVERRIDDEN METHODS
	
	protected String[] getValuesForTerm(String termName, ValueContainer vC, int instanceNr)
	{
		if (DerivedTermHandlerFactory.usesDerivedTermHandler())
		{
			MedViewDerivedTermHandler dTH = (MedViewDerivedTermHandler) DerivedTermHandlerFactory.getDerivedTermHandler();
			
			dTH.setPatientIdentifier(identifiers[instanceNr].getPID());
			
			dTH.setExaminationDate(identifiers[instanceNr].getTime());
		}
		
		return super.getValuesForTerm(termName, vC, instanceNr);
	}

	// MEMBERS
	
	protected ExaminationIdentifier[] identifiers;

}
