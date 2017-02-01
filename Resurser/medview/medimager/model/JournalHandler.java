/**
 * @(#) MedImagerJournalHandler.java
 */

package medview.medimager.model;

import javax.swing.text.*;

import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medimager.foundation.*;

import misc.foundation.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class JournalHandler implements MedImagerConstants
{
	// MEMBERS

	private String templateLocation = null;

	private String translatorLocation = null;

	private MedViewGeneratorEngineBuilder builder = new MedViewGeneratorEngineBuilder(); // has default medview engine


	// CONSTRUCTOR(S)

	public JournalHandler() // at construction - local datahandling assumed - call setUseRemoteDatahandling() otherwise
	{
		// template

		templateLocation = MedViewDataHandler.instance().getUserStringPreference(

			LOCAL_GENERATION_TEMPLATE_LOCATION_PROPERTY, null, MedImagerConstants.class);

		if (templateLocation != null)
		{
			try
			{
				setJournalTemplate(templateLocation); // -> CouldNotSetJournalTemplateException
			}
			catch (CouldNotSetJournalTemplateException exc)
			{
				System.err.println("Could not set template '" + templateLocation + "'");
			}
		}

		// translator

		translatorLocation = MedViewDataHandler.instance().getUserStringPreference(

			LOCAL_GENERATION_TRANSLATOR_LOCATION_PROPERTY, null, MedImagerConstants.class);

		if (translatorLocation != null)
		{
			try
			{
				setJournalTranslator(translatorLocation); // -> CouldNotSetJournalTemplateException
			}
			catch (CouldNotSetJournalTranslatorException exc)
			{
				System.err.println("Could not set translator '" + translatorLocation + "'");
			}
		}

		// term definition

		String setLocalTermDefinitionLocation = getLocalTermDefinitionLocation(); // returns empty string if not set

		if (setLocalTermDefinitionLocation.length() != 0)
		{
			setLocalTermDefinitionLocation(setLocalTermDefinitionLocation);
		}

		// term value

		String setLocalTermValueLocation = getLocalTermValueLocation(); // returns empty string if not set

		if (setLocalTermValueLocation.length() != 0)
		{
			setLocalTermValueLocation(setLocalTermValueLocation);
		}
	}

	public void setUseRemoteDataHandling(boolean useRemote)
	{
		if (useRemote)
		{
			if (usesRemoteDataHandling())
			{
				return; // already using remote datahanding
			}
			else
			{
				MedViewDataHandler.instance().setTermDataHandlerToUse(

					new medview.datahandling.RemoteTermDataHandlerClient(), true); // set up datahandler

				MedViewDataHandler.instance().setTermDefinitionLocation(

					MedViewDataHandler.instance().getUserStringPreference(

						REMOTE_SERVER_LOCATION_PROPERTY, "", MedImagerConstants.class)); // server loc should be set prior to call

				MedViewDataHandler.instance().setTermValueLocation(

					MedViewDataHandler.instance().getUserStringPreference(

						REMOTE_SERVER_LOCATION_PROPERTY, "", MedImagerConstants.class)); // server loc should be set prior to call
			}
		}
		else
		{
			if (!usesRemoteDataHandling())
			{
				return; // already not using remote datahandling
			}
			else
			{
				try
				{
					MedViewDataHandler.instance().setTermDataHandlerToUse( // -> Exception

						MedViewDataHandler.instance().getDefaultTermDataHandler(), true); // set up datahandler

					MedViewDataHandler.instance().setTermDefinitionLocation(getLocalTermDefinitionLocation());

					MedViewDataHandler.instance().setTermValueLocation(getLocalTermValueLocation());
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					System.exit(1); // this should never happen - if it does it's programmer error!
				}

			}
		}
	}

	public boolean usesRemoteDataHandling()
	{
		return MedViewDataHandler.instance().getTermDataHandlerInUse().equals(

			"medview.datahandling.RemoteTermDataHandlerClient");
	}

	// TERM DEFINITION LOCATION

	public void setLocalTermDefinitionLocation(String termDefinitionLocation)
	{
		if (!usesRemoteDataHandling())
		{
			MedViewDataHandler.instance().setTermDefinitionLocation(termDefinitionLocation);
		}

		MedViewDataHandler.instance().setUserStringPreference(LOCAL_GENERATION_TERM_DEFINITION_LOCATION_PROPERTY,

			termDefinitionLocation, MedImagerConstants.class);
	}

	public String getLocalTermDefinitionLocation()
	{
		return MedViewDataHandler.instance().getUserStringPreference(LOCAL_GENERATION_TERM_DEFINITION_LOCATION_PROPERTY,

			"", MedImagerConstants.class);
	}

	// TERM VALUE LOCATION

	public void setLocalTermValueLocation(String termValueLocation)
	{
		if (!usesRemoteDataHandling())
		{
			MedViewDataHandler.instance().setTermValueLocation(termValueLocation);
		}

		MedViewDataHandler.instance().setUserStringPreference(LOCAL_GENERATION_TERM_VALUE_LOCATION_PROPERTY,

			termValueLocation, MedImagerConstants.class);
	}

	public String getLocalTermValueLocation()
	{
		return MedViewDataHandler.instance().getUserStringPreference(LOCAL_GENERATION_TERM_VALUE_LOCATION_PROPERTY,

			"", MedImagerConstants.class);
	}


	// JOURNAL OBTAINING

	public StyledDocument getJournal( PatientIdentifier pid, final ProgressNotifiable not ) throws CouldNotGenerateJournalException
	{
		try
		{
			ExaminationIdentifier[] ids = MedViewDataHandler.instance().getExaminations(pid); // -> IOException

			builder.buildIdentifiers(ids);

			ValueContainer[] vcs = new ValueContainer[ids.length];

			for (int ctr=0; ctr<ids.length; ctr++)
			{
				final ExaminationValueContainer evc = MedViewDataHandler.instance().getExaminationValueContainer(ids[ctr]); // Exception(s)

				vcs[ctr] = new ValueContainer()
				{
					public String[] getValues(String term) throws se.chalmers.cs.medview.docgen.NoSuchTermException
					{
						try
						{
							return evc.getValues(term); // -> NoSuchTermException
						}
						catch (medview.datahandling.NoSuchTermException exc)
						{
							throw new se.chalmers.cs.medview.docgen.NoSuchTermException(exc);
						}
					}

					public String[] getTermsWithValues()
					{
						return evc.getTermsWithValues();
					}
				};
			}

			builder.buildValueContainers(vcs);

			builder.buildSections(getJournalTemplateModel().getAllContainedSections());

			se.chalmers.cs.medview.docgen.misc.ProgressNotifiable wrappedNotifiable = new

				se.chalmers.cs.medview.docgen.misc.ProgressNotifiable()
			{
				public void setCurrent(int c)
				{
					not.setCurrent(c);
				}

				public void setTotal(int t)
				{
					not.setTotal(t);
				}

				public void setDescription(String d)
				{
					not.setDescription(d);
				}

				public int getCurrent()
				{
					return not.getCurrent();
				}

				public int getTotal()
				{
					return not.getTotal();
				}

				public String getDescription()
				{
					return not.getDescription();
				}

				public boolean isIndeterminate()
				{
					return not.isIndeterminate();
				}

				public void setIndeterminate(boolean indeterminate)
				{
					not.setIndeterminate(indeterminate);
				}
			};

			return builder.getEngine().generateDocument(wrappedNotifiable);
		}
		catch (Exception exc)
		{
			throw new CouldNotGenerateJournalException(exc);
		}
	}


	// TEMPLATE

	public void setJournalTemplate( String templateLocation ) throws CouldNotSetJournalTemplateException
	{
		try
		{
			TemplateModelWrapper wrapper = MedViewDataHandler.instance().loadTemplateWrapper(templateLocation); // -> CouldNotLoadException

			this.templateLocation = templateLocation; // if we made it here the location is proper

			builder.buildTemplateModel(wrapper.getTemplateModel());

			MedViewDataHandler.instance().setUserStringPreference(LOCAL_GENERATION_TEMPLATE_LOCATION_PROPERTY,

				templateLocation, MedImagerConstants.class);
		}
		catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
		{
			throw new CouldNotSetJournalTemplateException(exc);
		}
	}

	public TemplateModel getJournalTemplateModel( ) throws CouldNotObtainJournalTemplateException
	{
		try
		{
			return builder.getBuiltTemplateModel();
		}
		catch (NotBuiltException exc)
		{
			throw new CouldNotObtainJournalTemplateException(exc.getMessage());
		}
	}

	public String getJournalTemplateLocation() // might be null
	{
		return templateLocation;
	}


	// TRANSLATOR

	public void setJournalTranslator( String translatorLocation ) throws CouldNotSetJournalTranslatorException
	{
		try
		{
			builder.buildTranslatorModel(MedViewDataHandler.instance().loadTranslator(translatorLocation)); // -> CouldNotLoadException

			this.translatorLocation = translatorLocation; // if we made it here the location is proper

			MedViewDataHandler.instance().setUserStringPreference(LOCAL_GENERATION_TRANSLATOR_LOCATION_PROPERTY,

				translatorLocation, MedImagerConstants.class);
		}
		catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
		{
			throw new CouldNotSetJournalTranslatorException(exc);
		}
	}

	public TranslatorModel getJournalTranslatorModel( ) throws CouldNotObtainJournalTranslatorException
	{
		try
		{
			return builder.getBuiltTranslatorModel();
		}
		catch (NotBuiltException exc)
		{
			throw new CouldNotObtainJournalTranslatorException(exc.getMessage());
		}
	}

	public String getJournalTranslatorLocation()
	{
		return translatorLocation;
	}
}
