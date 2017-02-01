/*
 * @(#)DefaultMedViewTranslationModelFactory.java
 *
 * $Id: DefaultMedViewTranslationModelFactory.java,v 1.1 2005/03/16 11:24:00 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.generator;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class DefaultMedViewTranslationModelFactory implements TranslationModelFactory
{

	// TRANSLATION MODEL CREATION METHODS

	public TranslationModel createTranslationModel( String term, String typeDesc ) throws
		CouldNotCreateException
	{
		return this.createTranslationModel(term, typeDesc, null, null);
	}

	public TranslationModel createTranslationModel( String term, String typeDesc, Object[] values, String[] translations ) throws
		CouldNotCreateException
	{
		if (typeDesc.equalsIgnoreCase(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR))
		{
			return new MedViewMultipleTranslationModel(term, values, translations);
		}
		else if (typeDesc.equalsIgnoreCase(BasicTermHandler.INTERVAL_TYPE_DESCRIPTOR))
		{
			return new MedViewIntervalTranslationModel(term, values, translations);
		}
		else if (typeDesc.equalsIgnoreCase(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR))
		{
			return new MedViewRegularTranslationModel(term, values, translations);
		}
		else
		{
			throw new CouldNotCreateException(typeDesc + " is an unrecognized type");
		}
	}


	// TYPE DESCRIPTOR METHODS

	public boolean isTranslationTypeDescriptor(String typeDesc)
	{
		return (typeDesc.equalsIgnoreCase(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR) ||

			typeDesc.equalsIgnoreCase(BasicTermHandler.INTERVAL_TYPE_DESCRIPTOR) ||

			typeDesc.equalsIgnoreCase(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR));
	}

	public boolean isRecognizedTypeDescriptor(String typeDesc)
	{
		return (isTranslationTypeDescriptor(typeDesc) ||

			typeDesc.equals(TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR));
	}
}
