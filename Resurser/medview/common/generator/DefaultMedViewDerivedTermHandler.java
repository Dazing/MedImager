package medview.common.generator;

import java.util.*;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.*;

/**
 * Basically a forwarder class to the data layer's derived
 * term handling capabilities. It does convert the data layers
 * type descriptors though.
 */
public class DefaultMedViewDerivedTermHandler implements MedViewDerivedTermHandler
{

	// DERIVED TERM HANDLER INTERFACE IMPLEMENTATIONS

	public String[] getDerivedTerms()
	{
		return medview.datahandling.DerivedTermHandler.instance().getDerivedTerms();
	}

	public boolean isDerivedTerm(String term)
	{
		return medview.datahandling.DerivedTermHandler.instance().isDerivedTerm(term);
	}

	public String getDerivedTermDerivee(String term) throws se.chalmers.cs.medview.docgen.NotDerivedTermException
	{
		try
		{
			return medview.datahandling.DerivedTermHandler.instance().getDerivedTermDerivee(term);
		}
		catch (medview.datahandling.NotDerivedTermException exc)
		{
			throw new se.chalmers.cs.medview.docgen.NotDerivedTermException(exc.getMessage());
		}
	}

	public String[] getDerivedTermValues(String term)
	{
		return medview.datahandling.DerivedTermHandler.instance().getDerivedTermValues(term);
	}

	public String[] getAllDerivedTermPossibleValues(String term)
	{
		return medview.datahandling.DerivedTermHandler.instance().getAllDerivedTermPossibleValues(term);
	}

	public String getDerivedTermTypeDescriptor(String term) throws se.chalmers.cs.medview.docgen.NotDerivedTermException
	{
		try
		{
			return MedViewGeneratorUtilities.convertDataToTextTypeDescriptor(

				medview.datahandling.DerivedTermHandler.instance().getDerivedTermTypeDescriptor(term));
		}
		catch (medview.datahandling.NotDerivedTermException exc)
		{
			throw new se.chalmers.cs.medview.docgen.NotDerivedTermException(exc.getMessage());
		}
	}


	// MEDVIEW DERIVED TERM HANDLER IMPLEMENTATIONS

	public void setExaminationDate(Date date)
	{
		medview.datahandling.DerivedTermHandler.instance().setExaminationDate(date);
	}

	public void setPatientIdentifier(PatientIdentifier pid)
	{
		medview.datahandling.DerivedTermHandler.instance().setPatientIdentifier(pid);
	}
}
