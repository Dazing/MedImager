/*
 * @(#)DataHandlerFactory.java
 *
 * $Id: DataHandlerFactory.java,v 1.13 2005/03/24 16:26:06 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import medview.datahandling.examination.*;

import se.chalmers.cs.medview.docgen.*;

/**
 * A factory that produces various datahandlers. You have
 * the option of setting explicitly which datahandler to
 * use, or implicitly by changing the default as specified
 * in the MedViewDataConstants interface. Each separate JVM
 * running application must specify which data handler to
 * use (if other than the defaults defined in the interface),
 * or the defaults will be used.
 *
 * @author Fredrik Lindahl
 */
class DataHandlerFactory implements MedViewDataConstants
{

	// CURRENT DATAHANDLER CLASS NAMES

	/**
	 * Returns the currently used examination data handler
	 * class name.
	 */
	public String getCurrentEDHClassName()
	{
		return getExaminationDataHandler().getClass().getName();
	}

	/**
	 * Returns the currently used term data handler
	 * class name.
	 */
	public String getCurrentTDHClassName()
	{
		return getTermDataHandler().getClass().getName();
	}

	/**
	 * Returns the currently used template and translator
	 * data handler class name.
	 */
	public String getCurrentTATDHClassName()
	{
		return getTemplateAndTranslatorDataHandler().getClass().getName();
	}


	// DEFAULT DATAHANDLER CLASS NAMES

	/**
	 * Returns the class name of the default examination
	 * data handler.
	 */
	public String getDefaultEDHClassName()
	{
		return getDefaultExaminationDataHandler().getClass().getName();
	}

	/**
	 * Returns the class name of the default term data
	 * handler.
	 */
	public String getDefaultTDHClassName()
	{
		return getDefaultTermDataHandler().getClass().getName();
	}

	/**
	 * Returns the class name of the default template and
	 * translator data handler.
	 */
	public String getDefaultTATDHClassName()
	{
		return getDefaultTemplateAndTranslatorDataHandler().getClass().getName();
	}


	// CURRENT DATAHANDLER SETTING

	/**
	 * Sets the examination data handler to return.
	 */
	public void setExaminationDataHandlerToUse(ExaminationDataHandler handler)
	{
		this.examinationDataHandler = handler;
	}

	/**
	 * Sets the term data handler to return.
	 */
	public void setTermDataHandlerToUse(TermDataHandler handler)
	{
		this.termDataHandler = handler;
	}

	/**
	 * Sets the template and translator data handler to return.
	 */
	public void setTemplateAndTranslatorDataHandlerToUse(TemplateAndTranslatorDataHandler handler)
	{
		this.templateAndTranslatorDataHandler = handler;
	}


	// CURRENT DATAHANDLER OBTAINING

	/**
	 * Obtains a reference to the currently set
	 * examination data handler. If it is not set,
	 * the default data handler (as obtained from
	 * MedViewDataConstants) will be instantiated
	 * and used. If this cannot be done, the
	 * application will exit with a fatal error.
	 */
	public ExaminationDataHandler getExaminationDataHandler( )
	{
		if (examinationDataHandler == null)
		{
			return getDefaultExaminationDataHandler();
		}
		else
		{
			return examinationDataHandler;
		}
	}

	/**
	 * Obtains a reference to the currently set
	 * term data handler. If it is not set, the
	 * default data handler (as obtained from
	 * MedViewDataConstants) will be instantiated
	 * and used. If this cannot be done, the
	 * application will exit with a fatal error.
	 */
	public TermDataHandler getTermDataHandler( )
	{
		if (termDataHandler == null)
		{
			return getDefaultTermDataHandler();
		}
		else
		{
			return termDataHandler;
		}
	}

	/**
	 * Obtains a reference to the currently set
	 * template and translator data handler. If
	 * it is not set, the default data handler
	 * (as obtained from MedViewDataConstants)
	 * will be instantiated and used. If this
	 * cannot be done, the application will
	 * exit with a fatal error.
	 */
	public TemplateAndTranslatorDataHandler getTemplateAndTranslatorDataHandler( )
	{
		if (templateAndTranslatorDataHandler == null)
		{
			return getDefaultTemplateAndTranslatorDataHandler();
		}
		else
		{
			return templateAndTranslatorDataHandler;
		}
	}


	// DEFAULT DATAHANDLERS

	/**
	 * Obtains the default examination data handler object. If
	 * this cannot be constructed, it is constructed a fatal error
	 * and the application will exit.
	 */
	public ExaminationDataHandler getDefaultExaminationDataHandler()
	{
		if (defaultExaminationDataHandler == null)
		{
			String c = DEFAULT_EXAMINATION_DATA_HANDLER_CLASS;

			try
			{
				defaultExaminationDataHandler = (ExaminationDataHandler) Class.forName(c).newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();

				System.exit(1); // fatal error
			}
		}

		return defaultExaminationDataHandler;
	}

	/**
	 * Obtains the default term data handler object. If
	 * this cannot be constructed, it is constructed a fatal error
	 * and the application will exit.
	 */
	public TermDataHandler getDefaultTermDataHandler()
	{
		if (defaultTermDataHandler == null)
		{
			String c = DEFAULT_TERM_DATA_HANDLER_CLASS;

			try
			{
				defaultTermDataHandler = (TermDataHandler) Class.forName(c).newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();

				System.exit(1); // fatal error
			}
		}

		return defaultTermDataHandler;
	}

	/**
	 * Obtains the default template and translator data handler object. If
	 * this cannot be constructed, it is constructed a fatal error
	 * and the application will exit.
	 */
	public TemplateAndTranslatorDataHandler getDefaultTemplateAndTranslatorDataHandler()
	{
		if (defaultTemplateAndTranslatorDataHandler == null)
		{
			String c = DEFAULT_TEMPLATE_AND_TRANSLATOR_DATA_HANDLER_CLASS;

			try
			{
				defaultTemplateAndTranslatorDataHandler = (TemplateAndTranslatorDataHandler) Class.forName(c).newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();

				System.exit(1); // fatal error
			}
		}

		return defaultTemplateAndTranslatorDataHandler;
	}


	// SINGLETON

	public static DataHandlerFactory instance()
	{
		if (instance == null)
		{
			instance = new DataHandlerFactory();
		}

		return instance;
	}


	// CONSTRUCTOR AND MEMBERS

	private DataHandlerFactory() { }

	// singleton instance

	private static DataHandlerFactory instance = null;

	// current datahandlers

	private TermDataHandler termDataHandler;

	private ExaminationDataHandler examinationDataHandler;

	private TemplateAndTranslatorDataHandler templateAndTranslatorDataHandler;

	// default datahandlers

	private TemplateAndTranslatorDataHandler defaultTemplateAndTranslatorDataHandler;

	private ExaminationDataHandler defaultExaminationDataHandler;

	private TermDataHandler defaultTermDataHandler;

}
