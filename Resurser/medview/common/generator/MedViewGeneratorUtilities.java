/*
 * @(#)MedViewGeneratorUtilities.java
 *
 * $Id: MedViewGeneratorUtilities.java,v 1.3 2008/01/31 13:23:27 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.generator;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class MedViewGeneratorUtilities
{

	/**
	 * Effectively transforms an examination value container (used
	 * to contain the values in the data layer of the medview system)
	 * to a ValueContainer used in the text generation subsystem.
	 * @param cont ExaminationValueContainer
	 * @return ValueContainer
	 */
	public static ValueContainer wrapExaminationValueContainer(final ExaminationValueContainer cont)
	{
		return new ValueContainer()
		{
			public String[] getValues(String term) throws se.chalmers.cs.medview.docgen.NoSuchTermException
			{
				try
				{
					return cont.getValues(term);
				}
				catch (medview.datahandling.NoSuchTermException exc)
				{
					throw new se.chalmers.cs.medview.docgen.NoSuchTermException(exc);
				}
			}

			public String[] getTermsWithValues()
			{
				return cont.getTermsWithValues();
			}

		};
	}

	/**
	 * Converts the data type descriptor to the corresponding
	 * text generation type descriptor (used in the text generation
	 * subsystem). The reason for having two separate type descriptors
	 * is to make each subsystem as independent of the other as
	 * possible. The text generation subsystem only deals with text
	 * generation type descriptors, while the data subsystem deals
	 * with data type descriptors (because they can be used for other
	 * purposes than text generation).
	 * @param typeDesc String
	 * @return String
	 */
	public static String convertDataToTextTypeDescriptor(String typeDesc)
	{
		if (typeDesc.equals(TermDataHandler.FREE_TYPE_DESCRIPTOR))
		{
			return TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(TermDataHandler.REGULAR_TYPE_DESCRIPTOR))
		{
			return BasicTermHandler.REGULAR_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(TermDataHandler.MULTIPLE_TYPE_DESCRIPTOR))
		{
			return BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(TermDataHandler.INTERVAL_TYPE_DESCRIPTOR))
		{
			return BasicTermHandler.INTERVAL_TYPE_DESCRIPTOR;
		}
		else
		{
			return TermHandler.UNKNOWN_TYPE_DESCRIPTOR;
		}
	}

	/**
	 * The same as the convertDataToTextTypeDescription, but the
	 * other way round.
	 * @param typeDesc String
	 * @return String
	 */
	public static String convertTextToDataTypeDescriptor(String typeDesc)
	{
		if (typeDesc.equals(TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.FREE_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.REGULAR_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.MULTIPLE_TYPE_DESCRIPTOR;
		}
		else if (typeDesc.equals(BasicTermHandler.INTERVAL_TYPE_DESCRIPTOR))
		{
			return TermDataHandler.INTERVAL_TYPE_DESCRIPTOR;
		}
		else
		{
			return TermDataHandler.UNKNOWN_TYPE_DESCRIPTOR;
		}
	}

	/**
	 * Will synchronize the passed-along translator model and its contents with the contents of
	 * the global term definition and value file. If a new term is encountered in the global term
	 * location, this term is added to the model. If new values are encountered in the global term
	 * location, containing the values for a certain term X, the new values are added to the
	 * model's translation model for the term X. Note that this synchronize method also makes sure
	 * that the derived terms and values are synchronized. Also note that this method does NOT
	 * remove any terms that are existant in the model but not in the global term list.
	 * <br><br>
	 * If there has been new derived term additions, they will not be existant in an old translator
	 * model. The new derived terms need to be added to the model in order for it to be up-to-date with
	 * the latest version of the applications. This is done by, after checking against the value and
	 * definition files, looping through the array of derived terms obtained by the derived term
	 * handler. If some term is found in this list, and it is not in the model, it is added to the
	 * model. If some term is found in this list, and it is in the model, the terms values are checked
	 * so that they are a 1:1 match of the values returned from the derived term handler. If there has
	 * been added a new derived term value, it is added to the translationmodel representing the
	 * derived term in the translator model.
	 */
	public static void synchronizeTranslatorModel( TranslatorModel model ) throws SynchronizingException
	{
		try
		{
			String[] definitionTerms = mVDH.getTerms(); // obtain global terms

			String[] modelTerms = model.getContainedTerms(); // obtain terms in model

			TranslationModelFactory modelFactory = TranslationModelFactoryCreator.getTranslationModelFactory();

			for (int ctr1=0; ctr1<definitionTerms.length; ctr1++) // for each global term...
			{
				if (!arrayContainsValue(modelTerms, definitionTerms[ctr1])) // check for existance of new global term
				{
					// NEW GLOBAL TERM EXISTS

					String textTypeDesc = MedViewGeneratorUtilities.convertDataToTextTypeDescriptor(

						mVDH.getTypeDescriptor(definitionTerms[ctr1]));

					if (modelFactory.isTranslationTypeDescriptor(textTypeDesc)) // if the term is of translation type...
					{
						TranslationModel tModel = modelFactory.createTranslationModel(definitionTerms[ctr1], textTypeDesc);

						model.addTranslationModel(tModel); // add new translation to translator model

						String[] values = mVDH.getValues(definitionTerms[ctr1]); // obtain global values for term

						if (values == null) { continue; } // if none exist, continue loop...

						for (int ctr2=0; ctr2<values.length; ctr2++) // for each global value for current term...
						{
                            //this is a hack to allow custom values saved to the treefiles
                            //0||germany will show "germany" but save "0"
                            //so if we encounter || we put only the first part into the currentTermVector
                            String [] temp;
                            temp = values[ctr2].split("\\|\\|");
                            if(temp.length==2)
                            {
                                //we have a custom value
                                values[ctr2]=temp[0];
                            }
                            model.addValue(definitionTerms[ctr1], values[ctr2]); // add value to translation model
						}
					}
				}
				else
				{
					// GLOBAL TERM ALREADY EXISTANT IN MODEL

					String[] defValues = mVDH.getValues(definitionTerms[ctr1]); // obtain global term values

					TranslationModel m = model.getTranslationModel(definitionTerms[ctr1]);

					for (int ctr2=0; ctr2<defValues.length; ctr2++) // for each global term value...
					{
                        
                        //this is a hack to allow custom values saved to the treefiles
                        //0||germany will show "germany" but save "0"
                        //so if we encounter || we put only the first part into the currentTermVector
                        String [] temp;
                        temp = defValues[ctr2].split("\\|\\|");
                        if(temp.length==2)
                        {
                            //we have a custom value
                            defValues[ctr2]=temp[0];
                        }

                        if (!m.containsValue(defValues[ctr2])) // check if new global value for term exists...
						{
							// NEW VALUE HAS BEEN ADDED FOR TERM


							m.addValue(defValues[ctr2]); // add newly found global value to translation model
						}
					}
				}
			}

			String[] dT = medview.datahandling.DerivedTermHandler.instance().getDerivedTerms(); // obtain all derived terms

			for (int ctr1 = 0; ctr1 < dT.length; ctr1++) // for each derived term...
			{
				if (!model.containsTranslationModel(dT[ctr1])) // if the term is not in the model...
				{
					// THE MODEL DID NOT HAVE A TRANSLATION MODEL FOR THE DERIVED TERM

					String textTypeDesc = MedViewGeneratorUtilities.convertDataToTextTypeDescriptor(

						medview.datahandling.DerivedTermHandler.instance().getDerivedTermTypeDescriptor(dT[ctr1])); // obtain term type from derived term handler

					TranslationModel tModel = modelFactory.createTranslationModel(dT[ctr1], textTypeDesc);

					model.addTranslationModel(tModel); // add translation model for term to translator model
				}

				TranslationModel tModel = model.getTranslationModel(dT[ctr1]); // obtain [newly added] translation model

				Object[] dVals = medview.datahandling.DerivedTermHandler.instance().getAllDerivedTermPossibleValues(dT[ctr1]); // obtain all values for derived term from handler

				for (int ctr2 = 0; ctr2 < dVals.length; ctr2++) // for each of the derived term values obtained from handler...
				{
					if (!tModel.containsValue(dVals[ctr2])) // if the translation model does not contain the value...
					{
						// THE TRANSLATION MODEL DID NOT CONTAIN THE DERIVED VALUE OBTAINED FROM THE DTH

						tModel.addValue(dVals[ctr2]); // add the value to the translation model
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new SynchronizingException(e.getMessage());
		}
	}

	private static boolean arrayContainsValue(String[] arr, String value)
	{
		for (int ctr=0; ctr<arr.length; ctr++)
		{
			if (arr[ctr].equalsIgnoreCase(value)) { return true; }
		}

		return false;
	}

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();

}
