package medview.meduweb.data;

import medview.datahandling.examination.*;
import medview.datahandling.*;
/**
 *Thanks Fredrik Lindahl for almost all cut & pasted code ;)
 *@author Figge
 *@version 1.0
 */
public class MeduwebDataHandlerFactory  {
	

	public ExaminationDataHandler getExaminationDataHandler( )
	{
		try
		{

			String set = DEFAULT_EDH_CLASS;

			if (examinationDataHandler != null)
			{
				String cEDH = examinationDataHandler.getClass().getName();

				if (set.equals(cEDH)) { return examinationDataHandler; }
			}

			examinationDataHandler = (ExaminationDataHandler) Class.forName(set).newInstance();

			return examinationDataHandler;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public TermDataHandler getTermDataHandler( )
	{
		try
		{

			String set = DEFAULT_TDH_CLASS;

			if (termDataHandler != null)
			{
				String cTDH = termDataHandler.getClass().getName();

				if (set.equals(cTDH)) { return termDataHandler; }
			}

			termDataHandler = (TermDataHandler) Class.forName(set).newInstance();
			termDataHandler.setTermDefinitionLocation(System.getProperty("base.dir") + 
							"\\termDefinitions.txt");
			termDataHandler.setTermValueLocation(System.getProperty("base.dir") +
							"\\termValues.txt");
			return termDataHandler;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public medview.meduweb.data.TemplateAndTranslatorDataHandler getTemplateAndTranslatorDataHandler( )
	{
		try
		{
			String set = DEFAULT_TATDH_CLASS;

			if (templateAndTranslatorDataHandler != null)
			{
				String cTATDH = templateAndTranslatorDataHandler.getClass().getName();

				if (set.equals(cTATDH)) { return templateAndTranslatorDataHandler; }
			}

			templateAndTranslatorDataHandler = (medview.meduweb.data.TemplateAndTranslatorDataHandler) Class.forName(set).newInstance();

			return templateAndTranslatorDataHandler;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println("getTemplateAndTranslatorDataHandler( ) returnerade null");
		return null;
	}



	public MeduwebDataHandlerFactory()
	{
	}


	private TermDataHandler termDataHandler;

	private ExaminationDataHandler examinationDataHandler;

	private medview.meduweb.data.TemplateAndTranslatorDataHandler templateAndTranslatorDataHandler;


	private static final String DEFAULT_TDH_CLASS = "medview.meduweb.data.MeduwebParsedTermDataHandler";

	private static final String DEFAULT_EDH_CLASS = "medview.datahandling.examination.MVDHandler";

	private static final String DEFAULT_TATDH_CLASS = "medview.meduweb.data.MeduwebTemplateAndTranslatorDataHandler";


	public static final String CURRENT_TATDH_CLASS_PROPERTY = "currentTemplateAndTranslatorDataHandlerClass";

	public static final String CURRENT_EDH_CLASS_PROPERTY = "currentExaminationDataHandlerClass";

	public static final String CURRENT_TDH_CLASS_PROPERTY = "currentTermDataHandlerClass";



}
