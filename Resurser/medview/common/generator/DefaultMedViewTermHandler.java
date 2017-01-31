/*
 * @(#)MedViewGeneratorEngineBuilder.java
 *
 * $Id: DefaultMedViewTermHandler.java,v 1.3 2007/04/08 10:35:13 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.generator;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.*;

public class DefaultMedViewTermHandler implements BasicTermHandler
{

	public	String[] getTerms() {
		System.out.println("Dummy implementation of method getTerms in DefaultMedViewTermHandler called");
		
		return null;
	}

	public String getTypeDescriptor(String term) throws CouldNotObtainValidTypeException
	{
		try
		{
			String dataTypeDesc = MedViewDataHandler.instance().getTypeDescriptor(term);

			return MedViewGeneratorUtilities.convertDataToTextTypeDescriptor(dataTypeDesc);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();

			return TermHandler.UNKNOWN_TYPE_DESCRIPTOR;
		}
	}

	public boolean recognizesTerm(String term)
	{
		try
		{
			if (MedViewDataHandler.instance().termExists(term))
			{
				int type = MedViewDataHandler.instance().getType(term);

				if (MedViewDataHandler.instance().isValidTermType(type))
				{
					return true;
				}
			}

			return false;
		}
		catch (Exception exc)
		{
			return false;
		}
	}
	
	public Object[] getValues(String term) {
		System.out.println("Dummy implementation of method getValues in DefaultMedViewTermHandler called");

		return null;
	}
	
}
