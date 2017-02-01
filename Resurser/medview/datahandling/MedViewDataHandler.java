/*
 * @(#)MedViewDataHandler.java
 *
 * $Id: MedViewDataHandler.java,v 1.64 2006/04/24 14:17:05 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: MedViewDataHandler.java,v $
 * Revision 1.64  2006/04/24 14:17:05  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.63  2005/10/21 09:00:20  erichson
 * Changed DEFAULT_TERM_VALUE to "N/A".
 *
 * Revision 1.62  2005/09/09 15:42:07  lindahlf
 * Server cachning
 *
 * Revision 1.61  2005/06/03 15:46:23  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.60  2005/03/24 16:26:07  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.59  2005/03/16 13:52:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.58  2005/02/24 17:09:28  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.57  2005/02/17 10:23:09  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.56  2005/01/30 15:29:16  lindahlf
 * T4 Integration
 *
 * Revision 1.55  2004/12/08 14:47:20  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.54  2004/11/24 15:18:27  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.53  2004/11/19 12:33:33  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.52  2004/11/11 22:36:49  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.51  2004/11/09 21:14:34  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.50  2004/11/06 01:11:21  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.49  2004/11/04 20:07:54  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.48  2004/11/04 12:04:59  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.47  2004/10/23 14:51:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.46  2004/10/19 21:40:44  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.45  2004/10/19 09:58:59  erichson
 * Moved default term value string to  DEFAULT_TERM_VALUE field
 *
 * Revision 1.44  2004/10/01 16:39:48  lindahlf
 * no message
 *
 * Revision 1.43  2004/08/24 18:25:12  lindahlf
 * no message
 *
 * Revision 1.42  2004/04/08 13:19:41  lindahlf
 * Fixed support for YYMMDD-XXXX and YYMMDDXXXX style swedish pnrs
 *
 * Revision 1.41  2004/04/01 00:36:28  lindahlf
 * no message
 *
 * Revision 1.40  2004/03/29 12:51:01  erichson
 * Added getMedViewTempDirectory() // Nils
 *
 * Revision 1.39  2004/03/18 16:37:13  lindahlf
 * Ordnade till PID-format bugg
 *
 * Revision 1.38  2004/03/10 17:04:01  lindahlf
 * Tweak av PID-map
 *
 * Revision 1.37  2004/03/08 23:58:27  lindahlf
 * no message
 *
 * Revision 1.36  2004/02/24 18:41:36  lindahlf
 * Same pcode generated for existant pid
 *
 * Revision 1.35  2004/02/19 18:21:26  lindahlf
 * Major update patch 1
 *
 * Revision 1.34  2004/02/02 17:20:42  lindahlf
 * no message
 *
 * Revision 1.33  2004/02/02 14:38:50  lindahlf
 * no message
 *
 * Revision 1.32  2004/01/20 19:42:16  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.31  2003/09/09 17:28:31  erichson
 * Changed log comment for 1.30 check-in
 *
 * Revision 1.30  2003/09/09 17:26:46  erichson
 * saveTree -> saveExamination
 *
 * Revision 1.29  2003/08/20 20:53:09  lindahlf
 * Ordnat så att bilderna inte använder alldeles för mycket minne
 *
 * Revision 1.28  2003/08/19 16:03:14  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.26  2003/06/10 00:36:14  lindahlf
 * Journal switch support - SCP update - multiline dialog about and question etc
 *
 * Revision 1.25  2003/05/06 14:17:12  nazari
 * Nothing changed
 *
 * Revision 1.24  2003/04/10 01:47:21  lindahlf
 * no message
 *
 * Revision 1.23  2003/03/13 01:21:46  lindahlf
 * Ordnat textbuggen för SC i page-vyn och diverse annat
 *
 * Revision 1.22  2002/12/06 00:32:03  lindahlf
 * Added support for notifying of progress in the data handling layer using the new ProgressNotifiable interface in misc.foundation - a new method was added to the ExaminationDataHandler interface and thus MedViewDataHandler for this purpose. // Fredrik
 *
 * Revision 1.21  2002/12/02 16:56:47  lindahlf
 * Lade till 'various resources' i CVS root, där jag tänkte vi kunde lägga sådant vi vill dela till varandra inom CVS som inte har med kod att göra (koden ligger ju under respektive paket). Detta har inget att göra med hur strukturen ser ut i levererade applikationer etc, det är ju redan löst med preference / user dirs etc. Ni får protestera om ni tycker det är fel att lägga detta här. // Fredrik
 *
 * Revision 1.20  2002/11/25 15:30:45  lindahlf
 * Removed derived term type 'pcode', added derived term handling // Fredrik
 *
 * Revision 1.19  2002/11/19 00:10:42  lindahlf
 * Added preference functionality. - Fredrik
 *
 * Revision 1.18  2002/11/01 13:39:13  lindahlf
 * Updates to MedSummary and SummaryCreator, also some template and translator files for Nils and Goran. // Fredrik
 *
 * Revision 1.17  2002/10/23 08:45:16  nazari
 * Only conflict solution
 *
 * Revision 1.16  2002/10/22 15:48:01  zachrisg
 * Added method getDefaultValue(String term).
 *
 * Revision 1.15  2002/10/22 15:36:46  erichson
 * changed saveExamination -> saveTree // NE
 *
 */

package medview.datahandling;

import java.awt.image.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*;

import misc.foundation.*;
import misc.foundation.io.*;
import misc.foundation.text.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * <b> GENERAL INFORMATION </b><br>
 * <br>
 * The MedViewDataHandler class is a facade for the common
 * data handling part of the medview programs. Core medview
 * functionality involving various data and its processing
 * are kept in the common datahandling packages, while more
 * application-specific handling should be contained in the
 * appropriate package structure of that application.<br>
 * <br>
 * There are two things you need to specify when setting up
 * the datahandler layer to use a specific datahandler: 1)
 * you have to specify the class name (or instance) of the
 * datahandler you wish to use, and 2) you need to specify
 * the location from which it will read and write data. The
 * following are the locations used by the current datahandlers:<br>
 * <br>
 * <i>TermDataHandler</i><br>
 * <br>
 * Term Definition Location<br>
 * Term Value Location<br>
 * <br>
 * <i>ExaminationDataHandler</i><br>
 * <br>
 * Examination Data Location<br>
 * <br>
 * You do not specify a location for the template and translator
 * datahandler, since the location is specified at save/load-time.<br>
 * <br>
 * To set which datahandler you want to use, call the methods
 * setXXXDataHandlerToUse() in this facade. There are two variants,
 * one which take an instance and the other which takes a class name
 * (which will fire exceptions if the class name is invalid). After
 * you have set your datahandler, you specify which location it should
 * write to and read from using the appropriate setXXXLocation()
 * methods in this facade. After this has been done, you can call the
 * facade methods that deal with data, and they will be delegated to
 * the set datahandler, which will use the set location.<br>
 * <br>
 * <b>TO DATALAYER DEVELOPERS</b><br>
 * <br>
 * When implementing within the datahandling layer, you should keep
 * some things in mind.<br>
 * <br>
 * <i>Exceptions</i><br>
 * <br>
 * Exceptions should carry descriptive messages in english, i.e. not
 * language-dependant messages, since these messages are not intended
 * for the end user. The exceptions should be caught in higher layers
 * and there appropriate information to the user should be displayed.
 *
 * @author Fredrik Lindahl
 */
public class MedViewDataHandler
{
	// SINGLETON INSTANCE

	/**
	 * Obtains the data handler singleton instance. Will only instantiate a data
	 * handler the first time of access.
	 *
	 * @return MedViewDataHandler
	 */
	public static MedViewDataHandler instance()
	{
		if (instance == null)
		{
			instance = new MedViewDataHandler();
		}

		return instance;
	}


	// SETTING OF CURRENT DATA HANDLERS AND PCODE GENERATOR

	/**
	 * Sets the examination datahandler to use using the specified class name. Will
	 * fire one or two events, depending on the value of the 'pending' parameter.
	 * If 'pending' is true, you indicate that there is a data location change
	 * pending, and that the set() method should fire the location change event. In
	 * this case, the method will only fire one event indicating that the class has
	 * been changed. If 'pending' is false, it will also fire an event indicating
	 * that the data location has changed.
	 *
	 * @param className String
	 * @param pending boolean
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void setExaminationDataHandlerToUse(String className, boolean pending) throws
		ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Object o = Class.forName(className).newInstance();

		setExaminationDataHandlerToUse((ExaminationDataHandler)o, pending);
	}

	/**
	 * Sets the term datahandler to use using the specified class name. Will fire
	 * one or two events, depending on the value of the 'pending' parameter. If
	 * 'pending' is true, you indicate that there is a data location change
	 * pending, and that the set() method should fire the location change event. In
	 * this case, the method will only fire one event indicating that the class has
	 * been changed. If 'pending' is false, it will also fire an event indicating
	 * that the data location has changed.
	 *
	 * @param className String
	 * @param pending boolean
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void setTermDataHandlerToUse(String className, boolean pending) throws
		ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Object o = Class.forName(className).newInstance();

		setTermDataHandlerToUse((TermDataHandler)o, pending);
	}

	/**
	 * Sets the template and translator datahandler to use using the specified
	 * class name. Will also cause an event to be fired to all registered data
	 * listeners indicating that the data handler has changed. If a data handler is
	 * not set, and a method that uses a data handler is called, the factory will
	 * return a default data handler. Note that this method will not fire an event
	 * indicating that any location has changed.
	 *
	 * @param className String
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void setTemplateAndTranslatorDataHandlerToUse(String className) throws
		ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Object o = Class.forName(className).newInstance();

		TemplateAndTranslatorDataHandler tATDH = (TemplateAndTranslatorDataHandler)o;

		setTemplateAndTranslatorDataHandlerToUse(tATDH);
	}

	/**
	 * Sets the pcode generator to use using the specified class name. Will also
	 * cause an event to be fired to all registered pcode listeners indicating that
	 * the pcode generator has changed. If the pcode generator has not been set,
	 * and a method using the current pcode generator is called, the factory
	 * returns a default pcode generator.
	 *
	 * @param className String
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void setPCodeGeneratorToUse(String className) throws
		ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Object o = Class.forName(className).newInstance();

		PCodeGenerator pCG = (PCodeGenerator)o;

		setPCodeGeneratorToUse(pCG);
	}


	// SETTING OF CURRENT DATA HANDLERS AND PCODE GENERATOR

	/**
	 * Sets the examination datahandler to use. Will fire one or two events,
	 * depending on the value of the 'pending' parameter. If 'pending' is true, you
	 * indicate that there is a data location change pending, and that the set()
	 * method should fire the location change event. In this case, the method will
	 * only fire one event indicating that the class has been changed. If 'pending'
	 * is false, it will also fire an event indicating that the data location has
	 * changed.
	 *
	 * @param eDH ExaminationDataHandler
	 * @param pending boolean
	 */
	public void setExaminationDataHandlerToUse(ExaminationDataHandler eDH, boolean pending)
	{
		ExaminationDataHandler curr = DataHandlerFactory.instance().getExaminationDataHandler();

		curr.removeExaminationDataHandlerListener(examinationDataHandlerListener);

		eDH.addExaminationDataHandlerListener(examinationDataHandlerListener);

		DataHandlerFactory.instance().setExaminationDataHandlerToUse(eDH);

		fireExaminationDataHandlerChanged(eDH.getClass().getName());

		if (!pending)
		{
			fireExaminationDataLocationChanged(eDH.getExaminationDataLocation());
		}
	}

	/**
	 * Sets the term datahandler to use. Will fire one or two events, depending on
	 * the value of the 'pending' parameter. If ' pending' is true, you indicate
	 * that there is a data location change pending, and that the set() method
	 * should fire the location change event. In this case, the method will only
	 * fire one event indicating that the class has been changed. If 'pending' is
	 * false, it will also fire an event indicating that the data location has
	 * changed.
	 *
	 * @param tDH TermDataHandler
	 * @param pending boolean
	 */
	public void setTermDataHandlerToUse(TermDataHandler tDH, boolean pending)
	{
		TermDataHandler curr = DataHandlerFactory.instance().getTermDataHandler();

		curr.removeTermDataHandlerListener(termDataHandlerListener);

		tDH.addTermDataHandlerListener(termDataHandlerListener);

		DataHandlerFactory.instance().setTermDataHandlerToUse(tDH);

		fireTermDataHandlerChanged(tDH.getClass().getName());

		if (!pending)
		{
			fireTermLocationChanged();
		}
	}

	/**
	 * Sets the template and translator datahandler to use. Will fire an event
	 * indicating that the datahandler has changed.
	 *
	 * @param tATDH TemplateAndTranslatorDataHandler
	 */
	public void setTemplateAndTranslatorDataHandlerToUse(TemplateAndTranslatorDataHandler tATDH)
	{
		DataHandlerFactory.instance().setTemplateAndTranslatorDataHandlerToUse(tATDH);

		fireTemplateAndTranslatorDataHandlerChanged(tATDH.getClass().getName());
	}

	/**
	 * Sets the pcode generator to use. Will fire an event to all registered pcode
	 * listeners that the generator has changed. If the user ID has been set, it
	 * will be set in the new generator as well. If the examination data location
	 * has been set, it is set in the new generator.
	 *
	 * @param pCG PCodeGenerator
	 */
	public void setPCodeGeneratorToUse(PCodeGenerator pCG)
	{
		PCodeGenerator curr = pCodeGeneratorFactory.getPCodeGenerator();

		curr.removePCodeGeneratorListener(pCodeGeneratorListener);

		pCG.addPCodeGeneratorListener(pCodeGeneratorListener);

		pCodeGeneratorFactory.setPCodeGeneratorToUse(pCG);

		if (isUserIDSet())
		{
			pCG.setGeneratedPCodePrefix(getUserID());
		}

		if (isExaminationDataLocationSet())
		{
			pCG.setExaminationDataLocation(getExaminationDataLocation());
		}

		firePCodeGeneratorChanged(pCG.getClass().getName());
	}


	// CURRENT CLASS NAMES OF DATA HANDLERS AND PCODE GENERATOR

	/**
	 * Obtains the class name of the current examination datahandler in use.
	 *
	 * @return String
	 */
	public String getExaminationDataHandlerInUse()
	{
		return DataHandlerFactory.instance().getCurrentEDHClassName();
	}

	/**
	 * Obtains the class name of the current term datahandler in use
	 *
	 * @return String
	 */
	public String getTermDataHandlerInUse()
	{
		return DataHandlerFactory.instance().getCurrentTDHClassName();
	}

	/**
	 * Obtains the class name of the current template and translator datahandler in
	 * use.
	 *
	 * @return String
	 */
	public String getTemplateAndTranslatorDataHandlerInUse()
	{
		return DataHandlerFactory.instance().getCurrentTATDHClassName();
	}

	/**
	 * Obtains the class name of the current pcode generator in use.
	 * @return String
	 */
	public String getPCodeGeneratorInUse()
	{
		return pCodeGeneratorFactory.getCurrentPCodeGeneratorClassName();
	}


	// DEFAULT DATA HANDLERS AND PCODE GENERATOR

	/**
	 * Obtains the class name of the default examination data handler.
	 *
	 * @return String
	 */
	public String getDefaultExaminationDataHandler()
	{
		return DataHandlerFactory.instance().getDefaultEDHClassName();
	}

	/**
	 * Obtains the class name of the default term data handler.
	 *
	 * @return String
	 */
	public String getDefaultTermDataHandler()
	{
		return DataHandlerFactory.instance().getDefaultTDHClassName();
	}

	/**
	 * Obtains the class name of the default template and translator data handler.
	 *
	 * @return String
	 */
	public String getDefaultTemplateAndTranslatorDataHandler()
	{
		return DataHandlerFactory.instance().getDefaultTATDHClassName();
	}

	/**
	 * Obtains the class name of the default pcode generator in use.
	 * @return String
	 */
	public String getDefaultPCodeGenerator()
	{
		return pCodeGeneratorFactory.getDefaultPCodeGeneratorClassName();
	}


	// EXAMINATION DATAHANDLER METHODS

	/**
	 * Obtain an array of the patients found at the currently set examination data
	 * location.
	 *
	 * @throws IOException
	 * @return PatientIdentifier[]
	 */
	public PatientIdentifier[] getPatients() throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getPatients();
	}

	/**
	 * Obtain an array of the patients found at the currently set examination data
	 * location. Will notify the specified progress notifiable of the progress
	 * during obtainal.
	 *
	 * @param notifiable ProgressNotifiable
	 * @throws IOException
	 * @return PatientIdentifier[]
	 */
	public PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getPatients(notifiable);
	}

	/**
	 * Returns all examinations found in the knowledge base after the
	 * time of last cache construct.
	 *
	 * 1) getPatients().
	 * 2) getExaminations(pid).
	 * 3) getExaminationValueContainer(pid).
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the time of last cache construct.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().refreshExaminations();
	}

	/**
	 * Returns all examinations found in the knowledge base after the
	 * specified time.
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @param sinceDate the examinations returned are the ones added to
	 * the knowledge base after this time.
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the specified time.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().refreshExaminations(sinceTime);
	}

	/**
	 * Obtain an array of images associated with the specified examination
	 * identifier.
	 *
	 * @param id ExaminationIdentifier
	 * @throws IOException
	 * @return ExaminationImage[]
	 */
	public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException
	{
		try
		{
			return DataHandlerFactory.instance().getExaminationDataHandler().getImages(id);
		}
		catch (NoSuchExaminationException e)
		{
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Returns the number of images kept in the knowledge base for the specified
	 * patient.
	 *
	 * @param pid PatientIdentifier
	 * @throws IOException
	 * @return int
	 */
	public int getImageCount(PatientIdentifier pid) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getImageCount(pid);
	}

	/**
	 * Returns the total number of examinations available in the data
	 * source handled in the data layer.
	 *
	 * @return the number of examinations.
	 */
	public int getExaminationCount() throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationCount();
	}

	/**
	 * Returns the total number of examinations for the specified patient.
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public int getExaminationCount(PatientIdentifier pid) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationCount(pid);
	}

	/**
	 * Returns an array of all examinations in the knowledge base for the specified
	 * patient.
	 *
	 * @param pid PatientIdentifier
	 * @throws IOException
	 * @return ExaminationIdentifier[]
	 */
	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getExaminations(pid);
	}

	/**
	 * Returns all examination value containers at the currently set
	 * examination data location.
	 * @param not ProgressNotifiable
	 * @return ExaminationValueContainer[]
	 * @throws <any>
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getAllExaminationValueContainers(not);
	}

	/**
	 * Returns all examination value containers at the currently set
	 * examination data location. Here you can specify an optimization
	 * hint.
	 * @param hint
	 * @return ExaminationValueContainer[]
	 * @throws IOException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getAllExaminationValueContainers(not, hint);
	}

	/**
	 * Returns an ExaminationValueContainer containing the
	 * values for the examination identified by the argument.
	 * Will return null if there is no examination value
	 * container for the argument identifier.
	 *
	 * @param id ExaminationIdentifier
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationValueContainer(id);
	}

	/**
	 * Returns an ExaminationValueContainer containing the
	 * values for the examination identified by the argument.
	 * Will return null if there is no examination value
	 * container for the argument identifier. You can also
	 * specify a hint which indicates whether you want to
	 * have memory-efficient data retrieval or fast and
	 * efficient data retrieval (at the expense of some memory).
	 * The hint constants are defined in the data layer constants
	 * interface.
	 *
	 * @param id ExaminationIdentifier
	 * @param hint int
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws
		IOException, NoSuchExaminationException, InvalidHintException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationValueContainer(id, hint);
	}

	/**
	 * Saves a Tree of examination data to the tree file database, along with a set
	 * of examination images. An event will be fired upon successful completion of
	 * the save notifying interested data listeners of the event.
	 *
	 * @param tree a Tree of examination data to be saved to the database
	 * @param imageArray an array of ExaminationImages to be saved to the database
	 * @return whether the examination previously existed in the database or if the
	 * save resultet in a new examination being added to it.
	 * @throws IOException if some error occurs during saving.
	 */
	public int saveExamination(Tree tree, ExaminationImage[] imageArray) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().saveExamination(tree, imageArray);
	}

	/**
	 * Saves the specified tree to the specified location. Observe that this method
	 * bypasses the currently set examination data location. The method is
	 * internally synchronized to avoid retrieval deadlock after an event has been
	 * fired. The intended use of this method is to be able to store a tree to
	 * another location without having to set the core examination data location,
	 * for instance for storing a tree to a temporary location if the primary
	 * location is inaccessible.
	 *
	 * @param tree Tree
	 * @param imageArray ExaminationImage[]
	 * @param location String
	 * @return whether the examination previously existed in the database or if the
	 * save resultet in a new examination being added to it.
	 * @throws IOException
	 */
	public int saveExamination(Tree tree, ExaminationImage[] imageArray, String location) throws IOException
	{
		return DataHandlerFactory.instance().getExaminationDataHandler().saveExamination(tree, imageArray, location);
	}

	/**
	 * Removes the specified examination (along with co-existing data such as images)
	 * from the underlying data location.
	 *
	 * @param eid ExaminationIdentifier
	 * @throws IOException
	 */
	public void removeExamination(ExaminationIdentifier eid) throws IOException
	{
		DataHandlerFactory.instance().getExaminationDataHandler().removeExamination(eid);
	}

	/**
	 * Returns the set examination data location in it's 'raw' form. For example,
	 * it could return 'c:\medview\databases\MSTest.mvd'. If the data location is
	 * not set, a language dependant string specifying this is returned.
	 *
	 * @return String
	 */
	public String getExaminationDataLocation()
	{
		if (isExaminationDataLocationSet())
		{
			return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationDataLocation();
		}
		else
		{
			return getLanguageString(MedViewLanguageConstants.OTHER_NOT_SET_LS_PROPERTY);
		}
	}

	/**
	 * Returns the data location expressed in a (perhaps) more simple way than the
	 * 'raw' data location. For example, it could return 'MSTest.mvd' instead of
	 * the full file path to the mvd. This could be used for displaying the chosen
	 * location in a graphical user interface, for example. If the data location is
	 * not set, a language dependant string specifying this is returned.
	 *
	 * @return String
	 */
	public String getExaminationDataLocationID()
	{
		if (isExaminationDataLocationSet())
		{
			return DataHandlerFactory.instance().getExaminationDataHandler().getExaminationDataLocationID();
		}
		else
		{
			return getLanguageString(MedViewLanguageConstants.OTHER_NOT_SET_LS_PROPERTY);
		}
	}

	/**
	 * Sets the location of the examination data. For example - for a MVD handler,
	 * the location could be 'c:\medview\databases\MSTest.mvd', while for a SQL
	 * handler could be something like 'login:password@server.myip.org:1024'. If
	 * the new location is different from the previously set location, an event
	 * will be fired indicating that the examination data location has changed.
	 *
	 * @param location String
	 */
	public void setExaminationDataLocation(String location)
	{
		if (!location.equals(DataHandlerFactory.instance().getExaminationDataHandler().getExaminationDataLocation()))
		{
			DataHandlerFactory.instance().getExaminationDataHandler().setExaminationDataLocation(location);

			pCodeGeneratorFactory.getPCodeGenerator().setExaminationDataLocation(location);

			examinationDataSearchEngine.clearCache();
		}
	}

	/**
	 * Returns whether or not the examination data location has been set (i.e.
	 * whether the currently set examination datahandler's specific data location
	 * has been set or not).
	 *
	 * @return boolean
	 */
	public boolean isExaminationDataLocationSet()
	{
		return (DataHandlerFactory.instance().getExaminationDataHandler().getExaminationDataLocation() != null);
	}

	/**
	 * Returns whether or not the current implementation class for the
	 * ExaminationDataHandler interface deems the currently set location to be
	 * valid. If the location has not been set, it is deemed invalid.
	 *
	 * @return boolean
	 */
	public boolean isExaminationDataLocationValid()
	{
		return (DataHandlerFactory.instance().getExaminationDataHandler().isExaminationDataLocationValid());
	}


	// TERM DATAHANDLER

	/**
	 * Returns whether or not the specified term exists. Will throw exceptions if
	 * the necessary storage locations where term information is kept is
	 * unavailable. A term exists if it is defined in the term definition location.
	 * Note that a derived term (such as 'PCode(age)') may exist in the template
	 * but not in the data location files, so this method will return a false value
	 * if asked if such a derived term exists.
	 *
	 * @param term String
	 * @throws IOException
	 * @return boolean
	 */
	public boolean termExists(String term) throws IOException
	{
		return DataHandlerFactory.instance().getTermDataHandler().termExists(term);
	}

	/**
	 * Returns whether or not the specified term contains the specified value as
	 * one of its values.
	 *
	 * @param term String
	 * @param value Object
	 * @throws IOException
	 * @return boolean
	 */
	public boolean valueExists(String term, Object value) throws IOException
	{
		return DataHandlerFactory.instance().getTermDataHandler().valueExists(term, value);
	}

	/**
	 * Returns whether or not the specified type is valid. It is valid if it can be
	 * recognized as one of the types specified by the constants in the
	 * TermDataHandler interface.
	 *
	 * @param type int
	 * @return boolean
	 */
	public boolean isValidTermType(int type)
	{
		return DataHandlerFactory.instance().getTermDataHandler().isValidTermType(type);
	}

	/**
	 * Adds a term with the specified type to permanent storage. Will throw
	 * exceptions if the type is invalid or the term information locations could
	 * not be parsed for some reason. Also, will throw exceptions if the term
	 * information locations are unavailable. When the term is added, it will be
	 * added to the term definition listing.
	 *
	 * @param term String
	 * @param type int
	 * @throws IOException
	 * @throws InvalidTypeException
	 */
	public void addTerm(String term, int type) throws IOException, InvalidTypeException
	{
		DataHandlerFactory.instance().getTermDataHandler().addTerm(term, type);
	}

	/**
	 * Adds a term with the specified type to permanent storage. Will throw
	 * exceptions if the type is invalid or the term information locations could
	 * not be parsed for some reason. Also, will throw exceptions if the term
	 * information locations are unavailable. When the term is added, it will be
	 * added to the term definition listing.
	 *
	 * @param term String
	 * @param type String
	 * @throws IOException
	 * @throws InvalidTypeException
	 */
	public void addTerm(String term, String type) throws IOException, InvalidTypeException
	{
		DataHandlerFactory.instance().getTermDataHandler().addTerm(term, type);
	}

	/**
	 * Removes the specified term from the permanent storage. Throws exceptions if
	 * this cannot be performed or if the term's type is invalid.
	 *
	 * @param term String
	 * @throws IOException
	 * @throws NoSuchTermException
	 */
	public void removeTerm(String term) throws IOException, NoSuchTermException
	{
		DataHandlerFactory.instance().getTermDataHandler().removeTerm(term);
	}

	/**
	 * Return all possible values associated with a specified term. Will return the
	 * values as an array of String objects. Note that if the term exists, but has
	 * no value mapping in the value location, an empty array will be returned. A
	 * null value is never returned.
	 *
	 * @param term String
	 * @throws IOException
	 * @throws NoSuchTermException
	 * @return String[]
	 */
	public String[] getValues(String term) throws IOException, NoSuchTermException
	{
		return DataHandlerFactory.instance().getTermDataHandler().getValues(term);
	}

	/**
	 * Return the value of a term that should be used
	 * when an examination does not provide a value for
	 * the term.
	 * @param term the term which default value should
	 * be returned.
	 * @return the default value of a term.
	 */
	public String getDefaultValue(String term)
	{
		return DEFAULT_TERM_VALUE;
	}

	/**
	 * Adds a value to the specified term. Throws an exception if for some reason
	 * this cannot be accomplished, where the exception specifies what went wrong.
	 *
	 * @param term String
	 * @param value Object
	 * @throws IOException
	 * @throws NoSuchTermException
	 * @throws InvalidTypeException
	 */
	public void addValue(String term, Object value) throws IOException, NoSuchTermException, InvalidTypeException
	{
		DataHandlerFactory.instance().getTermDataHandler().addValue(term, value);
	}

	/**
	 * Removes a value from a term (value = 'Italien', 'Ryssland', etc.). Throws an
	 * exception if this cannot be accomplished for some reason, where the
	 * exception specifies what went wrong.
	 *
	 * @param term String
	 * @param value Object
	 * @throws IOException
	 * @throws NoSuchTermException
	 * @throws InvalidTypeException
	 */
	public void removeValue(String term, Object value) throws IOException, NoSuchTermException, InvalidTypeException
	{
		DataHandlerFactory.instance().getTermDataHandler().removeValue(term, value);
	}

	/**
	 * Returns the main type of the passed-along term as one of the type-defining
	 * integers found in the TermDataHandler interface.
	 *
	 * @param term String
	 * @throws IOException
	 * @throws NoSuchTermException
	 * @throws InvalidTypeException
	 * @return int
	 */
	public int getType(String term) throws IOException, NoSuchTermException, InvalidTypeException
	{
		return DataHandlerFactory.instance().getTermDataHandler().getType(term);
	}

	/**
	 * Returns a text description of the specified term type. The text description
	 * returned is equal to one of the string constants defined in the
	 * TermDataHandler interface.
	 *
	 * @param term String
	 * @throws IOException
	 * @throws NoSuchTermException
	 * @throws InvalidTypeException
	 * @return String
	 */
	public String getTypeDescriptor(String term) throws IOException, NoSuchTermException, InvalidTypeException
	{
		return DataHandlerFactory.instance().getTermDataHandler().getTypeDescriptor(term);
	}

	/**
	 * Returns a textual description of the specified term type. The text
	 * description returned is equal to one of the string constants defined in the
	 * TermDataHandler interface. A null value will be returned if none of the
	 * integer constants in the TermDataHandler interface matches the specified
	 * type.
	 *
	 * @param type int
	 * @throws InvalidTypeException
	 * @return String
	 */
	public String getTypeDescriptor(int type) throws InvalidTypeException
	{
		return DataHandlerFactory.instance().getTermDataHandler().getTypeDescriptor(type);
	}

	/**
	 * Returns all defined terms. If the term information locations are unavailable
	 * for some reason, the returned array will be empty, but a non-null value is
	 * guaranteed. Note that derived terms such as 'PCode(age)' are not contained
	 * in the returned array, only the low-level terms contained in the definitions
	 * file (usually, but not necessarily, also in the term value file).
	 *
	 * @throws IOException
	 * @return String[]
	 */
	public String[] getTerms() throws IOException
	{
		return DataHandlerFactory.instance().getTermDataHandler().getTerms();
	}

	/**
	 * Sets the location of the term definitions. This will simply set the value of
	 * a property that is placed in permanent storage and that can be returned at
	 * later executions.
	 *
	 * @param loc String
	 */
	public void setTermDefinitionLocation(String loc)
	{
		if (!loc.equals(DataHandlerFactory.instance().getTermDataHandler().getTermDefinitionLocation()))
		{
			DataHandlerFactory.instance().getTermDataHandler().setTermDefinitionLocation(loc);
		}
	}

	/**
	 * Returns the location of the term definitions. If the location has not yet
	 * been set, or if the location is invalid, a null value will be returned.
	 *
	 * @return String
	 */
	public String getTermDefinitionLocation()
	{
		return DataHandlerFactory.instance().getTermDataHandler().getTermDefinitionLocation();
	}

	/**
	 * Sets the location of the term values. This will simply set the value of a
	 * property that is placed in permanent storage and that can be returned at
	 * later executions.
	 *
	 * @param loc String
	 */
	public void setTermValueLocation(String loc)
	{
		if (!loc.equals(DataHandlerFactory.instance().getTermDataHandler().getTermValueLocation()))
		{
			DataHandlerFactory.instance().getTermDataHandler().setTermValueLocation(loc);
		}
	}

	/**
	 * Returns the location of the term values. If the location has not yet been
	 * set, or if the location is invalid, a null value will be returned.
	 *
	 * @return String
	 */
	public String getTermValueLocation()
	{
		return DataHandlerFactory.instance().getTermDataHandler().getTermValueLocation();
	}

	/**
	 * Returns whether or not the currently set (or not set) term definition
	 * location is a valid one.
	 *
	 * @return boolean
	 */
	public boolean isTermDefinitionLocationValid()
	{
		return DataHandlerFactory.instance().getTermDataHandler().isTermDefinitionLocationValid();
	}

	/**
	 * Returns whether or not the currently set (or not set) term value location is
	 * a valid one.
	 *
	 * @return boolean
	 */
	public boolean isTermValueLocationValid()
	{
		return DataHandlerFactory.instance().getTermDataHandler().isTermValueLocationValid();
	}

	/**
	 * Returns whether or not the term definition location has been set.
	 *
	 * @return boolean
	 */
	public boolean isTermDefinitionLocationSet()
	{
		return (DataHandlerFactory.instance().getTermDataHandler().isTermDefinitionLocationSet());
	}

	/**
	 * Returns whether or not the term value location has been set.
	 *
	 * @return boolean
	 */
	public boolean isTermValueLocationSet()
	{
		return (DataHandlerFactory.instance().getTermDataHandler().isTermValueLocationSet());
	}


	// TEMPLATE AND TRANSLATOR DATAHANDLER

	public void saveTemplate(TemplateModel model, String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		TemplateModelWrapper wrapper = new TemplateModelWrapper();

		wrapper.setTemplateModelLocation(filePath);

		wrapper.setTemplateModel(model);

		saveTemplate(wrapper);
	}

	public void saveTemplate(TemplateModelWrapper wrapper) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().saveTemplate(wrapper);
	}

	public TemplateModel loadTemplate(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotLoadException
	{
		TemplateModelWrapper wrapper = DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().loadTemplate(filePath);

		return wrapper.getTemplateModel();
	}

	public TemplateModelWrapper loadTemplateWrapper(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotLoadException
	{
		return DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().loadTemplate(filePath);
	}

	public void saveTranslator(TranslatorModel model, String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().saveTranslator(model, filePath);
	}

	public TranslatorModel loadTranslator(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotLoadException
	{
		return DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().loadTranslator(filePath);
	}

	public String[] getSectionNames(String filePath)
	{
		return DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().getSectionNames(filePath);
	}

	public String parseTATDHLocation(String location)
	{
		return DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().parseIdentifier(location);
	}


	// PCODE PARSING

	/**
	 *
	 * @deprecated use the PatientIdentifier concept instead.
	 * @param pCode String
	 * @return int
	 */
	public int getAge(String pCode)
	{
		try
		{
			return pidParser.getAge(new PatientIdentifier(pCode));
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 *
	 * @deprecated use the PatientIdentifier concept instead.
	 * @param pCode String
	 * @param atDate Date
	 * @return int
	 */
	public int getAge(String pCode, Date atDate)
	{
		try
		{
			return pidParser.getAge(new PatientIdentifier(pCode), atDate);
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 *
	 * @deprecated use the PatientIdentifier concept instead.
	 * @param pCode String
	 * @return int
	 */
	public int getYearOfBirth(String pCode)
	{
		try
		{
			return pidParser.getYearOfBirth(new PatientIdentifier(pCode));
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 *
	 * @deprecated use the PatientIdentifier concept instead.
	 * @param pCode String
	 * @return int
	 */
	public int getGender(String pCode)
	{
		try
		{
			return pidParser.getGender(new PatientIdentifier(pCode));
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 *
	 * @deprecated use the PatientIdentifier concept instead.
	 * @return String
	 */
	public String getExamplePCode()
	{
		ExamplePIDGeneratorFactory f = ExamplePIDGeneratorFactory.instance();

		return f.getExamplePIDGenerator().generateExamplePID().getPCode();
	}

	/**
	 * Returns the age of the specified patient at the current date. If none of the
	 * current pid parsers can recognize the specified patient identifier format,
	 * an exception will be thrown.
	 *
	 * @param pid PatientIdentifier
	 * @throws InvalidPIDException
	 * @return int
	 */
	public int getAge(PatientIdentifier pid) throws InvalidPIDException
	{
		return pidParser.getAge(pid);
	}

	/**
	 * Returns the age of the specified patient at the specified date. If none of
	 * the current pid parsers can recogn exception will be thrown.
	 *
	 * @param pid PatientIdentifier
	 * @param atDate Date
	 * @throws InvalidPIDException
	 * @return int
	 */
	public int getAge(PatientIdentifier pid, Date atDate) throws InvalidPIDException
	{
		return pidParser.getAge(pid, atDate);
	}

	/**
	 * Returns the year of birth of the specified patient. If none of the current
	 * pid parsers can recognize the specified patient identifier format, an
	 * exception will be thrown.
	 *
	 * @param pid PatientIdentifier
	 * @throws InvalidPIDException
	 * @return int
	 */
	public int getYearOfBirth(PatientIdentifier pid) throws InvalidPIDException
	{
		return pidParser.getYearOfBirth(pid);
	}

	/**
	 * Returns the gender of the specified patient as one of the integer constants
	 * found in the PIDParser interface. If none of the current pid parsers can
	 * recognize the specified patient identifier format, an exception will be
	 * thrown.
	 *
	 * @param pid PatientIdentifier
	 * @throws InvalidPIDException
	 * @return int
	 */
	public int getGender(PatientIdentifier pid) throws InvalidPIDException
	{
		return pidParser.getGender(pid);
	}

	/**
	 * Converts the specified pid to a 'normalized' format. For instance, if
	 * swedish personal numbers are used, and this method is called with the
	 * argument of '197703292462', then the method would return the string
	 * '19770329-2462'. The normalized format is the format one would wish everyone
	 * to enter from the start, and also the format of the pid that should be
	 * displayed to the user.
	 *
	 * @param pid String
	 * @throws InvalidRawPIDException
	 * @return String
	 */
	public String normalizePID(String pid) throws InvalidRawPIDException
	{
		return pidValidator.normalizePID(pid);
	}

	/**
	 * Returns a generated example patient identifier, using the currently set
	 * example pid generator.
	 *
	 * @return PatientIdentifier
	 */
	public PatientIdentifier generateExamplePID()
	{
		return examplePIDGeneratorFactory.getExamplePIDGenerator().generateExamplePID();
	}

	/**
	 * Validates the specified identifier string, i.e. returns whether or not it is
	 * one of the recognized formats. For instance, this method would return true
	 * if called with one of: "G00019771", "GA00010490", "GOT0000019771",
	 * "770329-9282", "7703299282" etc.
	 *
	 * @param pid String
	 * @return boolean
	 */
	public boolean validates(String pid)
	{
		return pidValidator.validates(pid);
	}

	/**
	 * Returns whether or not the specified string is in a recognized pcode format.
	 *
	 * @param s String
	 * @return boolean
	 */
	public boolean isPCode(String s)
	{
		for (int ctr = 0; ctr < pCodeValidators.length; ctr++)
		{
			if (pCodeValidators[ctr].validates(s))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Takes a general pid string and tries to generate a matching pcode string for
	 * it. If the data layer cannot recognize the pid, an exception will be thrown.
	 * A run-number will be consumed when using this method (i.e. the number in the
	 * number generator location will be increased after the method call).
	 *
	 * @param pid String
	 * @throws InvalidRawPIDException
	 * @throws CouldNotGeneratePCodeException
	 * @return String
	 */
	public String obtainPCode(String pid) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return pCodeGeneratorFactory.getPCodeGenerator().obtainPCode(pid, true);
	}

	/**
	 * Takes a general pid string and tries to generate a matching pcode string for
	 * it. If the data layer cannot recognize the pid, an exception will be thrown.
	 * The consumeNr determines whether or not a run-number should be consumed
	 * during generation of the pcode, if set to false, the run-number will be the
	 * current one but it wont increase after the method call.
	 *
	 * @param pid String
	 * @param consumeNr boolean
	 * @throws InvalidRawPIDException
	 * @throws CouldNotGeneratePCodeException
	 * @return String
	 */
	public String obtainPCode(String pid, boolean consumeNr) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return pCodeGeneratorFactory.getPCodeGenerator().obtainPCode(pid, consumeNr);
	}

	/**
	 * Same as the other generatePCode(pid) method, but this version also provides
	 * the opportunity to provide a progress notifiable that is notified of
	 * progress (the building of the cache etc might take a little while, therefore
	 * the user might want to see progress).
	 *
	 * @param pid String
	 * @param not ProgressNotifiable
	 * @throws InvalidRawPIDException
	 * @throws CouldNotGeneratePCodeException
	 * @return String
	 */
	public String obtainPCode(String pid, ProgressNotifiable not) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return pCodeGeneratorFactory.getPCodeGenerator().obtainPCode(pid, true, not);
	}

	/**
	 * Same as the previous generatePCode() methods, but this method provides the
	 * opportunity to specify all kinds of arguments at once.
	 *
	 * @param pid String
	 * @param consumeNr boolean
	 * @param not ProgressNotifiable
	 * @throws InvalidRawPIDException
	 * @throws CouldNotGeneratePCodeException
	 * @return String
	 */
	public String obtainPCode(String pid, boolean consumeNr, ProgressNotifiable not) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return pCodeGeneratorFactory.getPCodeGenerator().obtainPCode(pid, consumeNr, not);
	}

	/**
	 * Sets the user ID of the current user. If the user ID is in an invalid
	 * format, an exception will be thrown. The user ID is stored in the data
	 * layer, since the preferences are stored on a user basis and it is assumed
	 * that the same user will have the same user ID throughout the applications.
	 *
	 * @param userID String
	 * @throws InvalidUserIDException
	 */
	public void setUserID(String userID) throws InvalidUserIDException
	{
		if (userID.length() != 3)
		{
			String lS = MedViewLanguageConstants.ERROR_USER_ID_NOT_OF_LENGTH_3;

			String m = getLanguageString(lS) + "(" + userID + ")";

			throw new InvalidUserIDException(m);
		}

		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_ID_PROPERTY;

		mVDSH.setUserStringPreference(key, userID, c);

		pCodeGeneratorFactory.getPCodeGenerator().setGeneratedPCodePrefix(userID);

		fireUserIDChanged(userID);
	}

	/**
	 * Sets the user name of the current user. The user name is stored in the data
	 * layer, since the preferences are stored on a user basis and it is assumed
	 * that the same user will have the same user ID throughout the applications.
	 *
	 * @param userName String
	 */
	public void setUserName(String userName)
	{
		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_NAME_PROPERTY;

		mVDSH.setUserStringPreference(key, userName, c);

		fireUserNameChanged(userName);
	}

	/**
	 * Returns the user ID of the current user.
	 *
	 * @return String
	 */
	public String getUserID()
	{
		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_ID_PROPERTY;

		return mVDSH.getUserStringPreference(key, "", c);
	}

	/**
	 * Returns the user name of the current user.
	 *
	 * @return String
	 */
	public String getUserName()
	{
		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_NAME_PROPERTY;

		return mVDSH.getUserStringPreference(key, "", c);
	}

	/**
	 * Returns whether or not the user ID has been set.
	 *
	 * @return boolean
	 */
	public boolean isUserIDSet()
	{
		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_ID_PROPERTY;

		return mVDSH.isUserPreferenceSet(key, c);
	}

	/**
	 * Returns whether or not the user name has been set.
	 *
	 * @return boolean
	 */
	public boolean isUserNameSet()
	{
		Class c = MedViewDataUserProperties.class;

		String key = MedViewDataUserProperties.USER_NAME_PROPERTY;

		return mVDSH.isUserPreferenceSet(key, c);
	}

	/**
	 * Sets the location (if applicable for current pcode number generator) of the
	 * resource used for retrieving next available number in sequence. This could,
	 * for instance, be the path to a lock file if the lock file pcode number
	 * generator is used.
	 *
	 * @param loc String
	 */
	public void setPCodeNRGeneratorLocation(String loc)
	{
		PCodeGenerator pCG = pCodeGeneratorFactory.getPCodeGenerator();

		pCG.setNumberGeneratorLocation(loc);

		firePCodeNRGeneratorLocationChanged(loc);
	}

	/**
	 * Returns the location (if applicable for current pcode number generator) of
	 * the resource used for retrieving next available number in sequence. This
	 * could, for instance, be the path to a lock file if the lock file pcode
	 * number generator is used. If the location for the current
	 *
	 * @return String
	 */
	public String getPCodeNRGeneratorLocation()
	{
		PCodeGenerator pCG = pCodeGeneratorFactory.getPCodeGenerator();

		return pCG.getNumberGeneratorLocation();
	}

	/**
	 * Since a number generator location must be set for most implementations of
	 * pcode-generators, there exists this method that returns whether or not there
	 * is a valid number generator location set.
	 *
	 * @return boolean
	 */
	public boolean isPCodeNRGeneratorLocationSet()
	{
		PCodeGenerator pCG = pCodeGeneratorFactory.getPCodeGenerator();

		return pCG.isNumberGeneratorLocationSet();
	}


	// MEDIA

	/**
	 * Obtains the image icon for the specified image resource id. The id needs to
	 * be one of the constants defined in the interface MedViewMediaConstants. Will
	 * return null if the resource was not recognized or if the resource could not
	 * be located.
	 *
	 * @param id String
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon(String id)
	{
		return mediaHandler.getImageIcon(id);
	}


	/**
	 * Obtains the image for the specified image resource id. The id needs to be
	 * one of the constants defined in the MedViewMediaConstants interface. Will
	 * return null if the resource was not recognized or could not be located.
	 *
	 * @param id String
	 * @return Image
	 */
	public BufferedImage getImage(String id)
	{
		return mediaHandler.getImage(id);
	}


	// SEARCHING THE KNOWLEDGE BASE

	/**
	 * Performs a search in the current examination data location. The
	 * search string is parsed, if for some reason the search engine
	 * implementation cannot parse the search string, an exception is
	 * thrown.
	 * @param searchString String
	 * @param not ProgressNotifiable
	 * @return ExaminationDataSearchResult[]
	 * @throws CouldNotParseException
	 * @throws CouldNotSearchException
	 */
	public ExaminationDataSearchResult[] searchExaminationData(String searchString, ProgressNotifiable not)
		throws misc.foundation.text.CouldNotParseException, CouldNotSearchException
	{
		return examinationDataSearchEngine.searchExaminationData(searchString, not);
	}


	// LANGUAGE

	/**
	 * Retrieves the language-specific string corresponding to the currently set
	 * language's means of describing the phrase described by the constant
	 * identifier.
	 *
	 * @param propertyIdentifier String
	 * @return String
	 */
	public String getLanguageString(String propertyIdentifier)
	{
		return languageHandler.getLanguageString(propertyIdentifier);
	}


	/**
	 * Tries to change the language to the specified language identifier. The
	 * language identifier is the file name of the language resource file (.lrf
	 * file), so if the file is named "english.lrf", the user should specify the
	 * argument "english" to this method.
	 *
	 * @param langId String
	 * @throws LanguageException
	 */
	public void changeLanguage(String langId) throws LanguageException
	{
		languageHandler.changeLanguage(langId);
	}


	/**
	 * Retrieves a list of the currently installed and available languages on the
	 * system.
	 *
	 * @return String[]
	 */
	public String[] getAvailableLanguages()
	{
		return languageHandler.getAvailableLanguages();
	}


	/**
	 * Retrieves the current language's descriptor, which is used to show to the
	 * user the language currently in use. For instance, could be "english".
	 *
	 * @return String
	 */
	public String getCurrentLanguageDescriptor()
	{
		return languageHandler.getCurrentLanguageDescriptor();
	}


	// PREFERENCE METHODS

	/**
	 * Removes the preference from the preferences.
	 * @param key String the preference to remove
	 * @param c Class the class used to determine the
	 * preference node
	 */
	public void clearUserPreference(String key, Class c)
	{
		mVDSH.clearUserPreference(key, c);

		fireUserPreferenceChanged(key);
	}

	/**
	 * Removes the preference from the preferences.
	 * @param key String the preference to remove
	 * @param c Class the class used to determine the
	 * preference node
	 */
	public void clearSystemPreference(String key, Class c)
	{
		mVDSH.clearSystemPreference(key, c);

		fireSystemPreferenceChanged(key);
	}


	/**
	 * Returns whether or not the specified user
	 * preference has been set.
	 * @param key String the preference you want to
	 * check.
	 * @param c Class the class determining the preference
	 * node
	 * @return boolean whether or not the preference
	 * is set to anything.
	 */
	public boolean isUserPreferenceSet(String key, Class c)
	{
		return mVDSH.isUserPreferenceSet(key, c);
	}

	/**
	 * Returns whether or not the specified system
	 * preference has been set.
	 * @param key String the preference you want to
	 * check.
	 * @param c Class the class determining the preference
	 * node
	 * @return boolean whether or not the preference
	 * is set to anything.
	 */
	public boolean isSystemPreferenceSet(String key, Class c)
	{
		return mVDSH.isSystemPreferenceSet(key, c);
	}


	/**
	 * Sets a string preference in the user preference tree. Will cause an event to
	 * be fired to all registered preference listeners that a user preference's
	 * value has changed.
	 *
	 * @param key String
	 * @param val String
	 * @param c Class
	 */
	public void setUserStringPreference(String key, String val, Class c)
	{
		mVDSH.setUserStringPreference(key, val, c);

		fireUserPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as a string.
	 * @param key String
	 * @param def String
	 * @param c Class
	 * @return String
	 */
	public String getUserStringPreference(String key, String def, Class c)
	{
		return mVDSH.getUserStringPreference(key, def, c);
	}


	/**
	 * Sets a boolean preference in the user preference tree. Will cause an event
	 * to be fired to all registered preference listeners that a user preference's
	 * value has changed.
	 *
	 * @param key String
	 * @param val boolean
	 * @param c Class
	 */
	public void setUserBooleanPreference(String key, boolean val, Class c)
	{
		mVDSH.setUserBooleanPreference(key, val, c);

		fireUserPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as a boolean.
	 * @param key String
	 * @param def boolean
	 * @param c Class
	 * @return boolean
	 */
	public boolean getUserBooleanPreference(String key, boolean def, Class c)
	{
		return mVDSH.getUserBooleanPreference(key, def, c);
	}


	/**
	 * Sets an integer preference in the user preference tree. Will cause an event
	 * to be fired to all registered preference listeners that a user preference's
	 * value has changed.
	 *
	 * @param key String
	 * @param val int
	 * @param c Class
	 */
	public void setUserIntPreference(String key, int val, Class c)
	{
		mVDSH.setUserIntPreference(key, val, c);

		fireUserPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as an integer.
	 *
	 * @param key String
	 * @param def int
	 * @param c Class
	 * @return int
	 */
	public int getUserIntPreference(String key, int def, Class c)
	{
		return mVDSH.getUserIntPreference(key, def, c);
	}

	/**
	 * Sets a double preference in the user preference tree. Will cause
	 * an event to be fired to all registered preference listeners that
	 * a user preference's value has changed.
	 *
	 * @param key String
	 * @param val double
	 * @param c Class
	 */
	public void setUserDoublePreference(String key, double val, Class c)
	{
		mVDSH.setUserDoublePreference(key, val, c);

		fireUserPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as a double.
	 *
	 * @param key String
	 * @param def double
	 * @param c Class
	 * @return double
	 */
	public double getUserDoublePreference(String key, double def, Class c)
	{
		return mVDSH.getUserDoublePreference(key, def, c);
	}


	/**
	 * Sets a string preference in the system preference tree. Will cause an event
	 * to be fired to all registered preference listeners that a system
	 * preference's value has changed.
	 *
	 * @param key String
	 * @param val String
	 * @param c Class
	 */
	public void setSystemStringPreference(String key, String val, Class c)
	{
		mVDSH.setSystemStringPreference(key, val, c);

		fireSystemPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as a string.
	 * @param key String
	 * @param def String
	 * @param c Class
	 * @return String
	 */
	public String getSystemStringPreference(String key, String def, Class c)
	{
		return mVDSH.getSystemStringPreference(key, def, c);
	}


	/**
	 * Sets a boolean preference in the system preference tree. Will cause an event
	 * to be fired to all registered preference listeners that a system
	 * preference's value has changed.
	 *
	 * @param key String
	 * @param val boolean
	 * @param c Class
	 */
	public void setSystemBooleanPreference(String key, boolean val, Class c)
	{
		mVDSH.setSystemBooleanPreference(key, val, c);

		fireSystemPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as a boolean.
	 * @param key String
	 * @param def boolean
	 * @param c Class
	 * @return boolean
	 */
	public boolean getSystemBooleanPreference(String key, boolean def, Class c)
	{
		return mVDSH.getSystemBooleanPreference(key, def, c);
	}


	/**
	 * Sets an integer preference in the system preference tree. Will cause an
	 * event to be fired to all registered preference listeners that a system
	 * preference's value has changed.
	 *
	 * @param key String
	 * @param val int
	 * @param c Class
	 */
	public void setSystemIntPreference(String key, int val, Class c)
	{
		mVDSH.setSystemIntPreference(key, val, c);

		fireSystemPreferenceChanged(key);
	}

	/**
	 * Obtains the specified preference, as an integer.
	 * @param key String
	 * @param def int
	 * @param c Class
	 * @return int
	 */
	public int getSystemIntPreference(String key, int def, Class c)
	{
		return mVDSH.getSystemIntPreference(key, def, c);
	}


	// PROPERTY METHODS (DEPRECATED - USE PREFERENCE METHODS INSTEAD)

	/**
	 * Checks to see if a certain property has been set or not. Will query the
	 * default user preference node for the property value. When using 'property'
	 * methods of the data handling class, the default user non-class preference
	 * node is used to store the property. You might consider using the
	 * 'preferences' methods as an alternative if you want more control over where
	 * the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 * @return boolean
	 */
	public boolean isPropertySet(String prop)
	{
		return (mVDSH.getUserStringPreference(prop, null, null) != null);
	}


	/**
	 * Will remove the specified property such that future calls to the
	 * isPropertySet() method for the specified property will return false. When
	 * using 'property' methods of the data handling class, the default user
	 * non-class preference node is used to store the property. You might consider
	 * using the 'preferences' methods as an alternative if you want more control
	 * over where the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 */
	public void clearProperty(String prop)
	{
		mVDSH.clearUserPreference(prop);
	}


	/**
	 * Returns the boolean value of the specified flag property, or false if there
	 * is some error or if the property has not yet been set. The default user
	 * preference node will be queried for the value of the property. When using
	 * 'property' methods of the data handling class, the default user non-class
	 * preference node is used to store the property. You might consider using the
	 * 'preferences' methods as an alternative if you want more control over where
	 * the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 * @return boolean
	 */
	public boolean getFlagProperty(String prop)
	{
		return mVDSH.getUserBooleanPreference(prop, false, null);
	}


	/**
	 * Sets the boolean value of the flag specified by the argument. The default
	 * user preference node will contain the preference when this method is used.
	 * The method will result in a notification to all property change listeners
	 * registered to the data handler. When using 'property' methods of the data
	 * handling class, the default user non-class preference node is used to store
	 * the property. You might consider using the 'preferences' methods as an
	 * alternative if you want more control over where the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 * @param value boolean
	 */
	public void setFlagProperty(String prop, boolean value)
	{
		mVDSH.setUserBooleanPreference(prop, value, null);

		fireFlagPropertyChanged(prop, value);
	}


	/**
	 * Returns the string value of the specified user property, or false if there
	 * is some error or if the property has not yet been set. The default user
	 * preference node will be queried for the value of the property. When using
	 * 'property' methods of the data handling class, the default user non-class
	 * preference node is used to store the property. You might consider using the
	 * 'preferences' methods as an alternative if you want more control over where
	 * the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 * @return String
	 */
	public String getUserProperty(String prop)
	{
		return mVDSH.getUserStringPreference(prop, null, null);
	}


	/**
	 * Sets the string value of the property specified by the argument. The default
	 * user preference node will contain the preference when this method is used.
	 * The method will result in a notification to all property change listeners
	 * registered to the data handler. When using 'property' methods of the data
	 * handling class, the default user non-class preference node is used to store
	 * the property. You might consider using the 'preferences' methods as an
	 * alternative if you want more control over where the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param prop String
	 * @param value String
	 */
	public void setUserProperty(String prop, String value)
	{
		mVDSH.setUserStringPreference(prop, value, null);

		fireUserPropertyChanged(prop, value);
	}


	// DATA EVENT RELATED

	/**
	 * Registers a data listener to the data handler.
	 *
	 * @param listener MedViewDataListener
	 */
	public void addMedViewDataListener(MedViewDataListener listener)
	{
		listenerList.add(MedViewDataListener.class, listener);
	}

	/**
	 * Removes a data listener from the list of listeners kept by the data handler.
	 *
	 * @param listener MedViewDataListener
	 */
	public void removeMedViewDataListener(MedViewDataListener listener)
	{
		listenerList.remove(MedViewDataListener.class, listener);
	}


	private void fireExaminationDataLocationChanged(String loc)
	{
		clearDataEvent();

		sharedDataEvent.setLocation(loc);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationDataLocationChanged(sharedDataEvent);
			}
		}
	}

	private void fireExaminationDataLocationIDChanged(String locID)
	{
		clearDataEvent();

		sharedDataEvent.setLocationID(locID);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationDataLocationIDChanged(sharedDataEvent);
			}
		}
	}

	private void fireExaminationAdded(ExaminationIdentifier id)
	{
		clearDataEvent();

		sharedDataEvent.setIdentifier(id);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationAdded(sharedDataEvent);
			}
		}
	}

	private void fireExaminationUpdated(ExaminationIdentifier id)
	{
		clearDataEvent();

		sharedDataEvent.setIdentifier(id);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationUpdated(sharedDataEvent);
			}
		}
	}

	private void fireExaminationRemoved(ExaminationIdentifier id)
	{
		clearDataEvent();

		sharedDataEvent.setIdentifier(id);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationRemoved(sharedDataEvent);
			}
		}
	}

	private void fireTermLocationChanged()
	{
		clearDataEvent();

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					termLocationChanged(sharedDataEvent);
			}
		}
	}

	private void fireTermAdded(String term)
	{
		clearDataEvent();

		sharedDataEvent.setTerm(term);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					termAdded(sharedDataEvent);
			}
		}
	}

	private void fireTermRemoved(String term)
	{
		clearDataEvent();

		sharedDataEvent.setTerm(term);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					termRemoved(sharedDataEvent);
			}
		}
	}

	private void fireValueAdded(String term, Object value)
	{
		clearDataEvent();

		sharedDataEvent.setTerm(term);

		sharedDataEvent.setValue(value);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					valueAdded(sharedDataEvent);
			}
		}
	}

	private void fireValueRemoved(String term, Object value)
	{
		clearDataEvent();

		sharedDataEvent.setTerm(term);

		sharedDataEvent.setValue(value);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					valueRemoved(sharedDataEvent);
			}
		}
	}

	private void fireExaminationDataHandlerChanged(String className)
	{
		clearDataEvent();

		sharedDataEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					examinationDataHandlerChanged(sharedDataEvent);
			}
		}
	}

	private void fireTermDataHandlerChanged(String className)
	{
		clearDataEvent();

		sharedDataEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					termDataHandlerChanged(sharedDataEvent);
			}
		}
	}

	private void fireTemplateAndTranslatorDataHandlerChanged(String className)
	{
		clearDataEvent();

		sharedDataEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).

					templateAndTranslatorDataHandlerChanged(sharedDataEvent);
			}
		}
	}

	private void fireUserIDChanged(String userID)
	{
		clearDataEvent();

		sharedDataEvent.setUserID(userID);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).userIDChanged(sharedDataEvent);
			}
		}
	}

	private void fireUserNameChanged(String userName)
	{
		clearDataEvent();

		sharedDataEvent.setUserName(userName);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewDataListener.class)
			{
				((MedViewDataListener)listeners[i + 1]).userNameChanged(sharedDataEvent);
			}
		}
	}


	// PCODE EVENT RELATED

	/**
	 * Adds a pcode listener to the list of listeners kept by the data handler
	 * instance. A pcode listener is notified whenever the pcode prefix has changed
	 * or the number generation location has changed.
	 *
	 * @param listener MedViewPCodeListener
	 */
	public void addMedViewPCodeListener(MedViewPCodeListener listener)
	{
		listenerList.add(MedViewPCodeListener.class, listener);
	}

	/**
	 * Removes a pcode listener from the list of listeners kept by the data handler
	 * instance. A pcode listener is notified whenever the pcode prefix has changed
	 * or the number generation location has changed.
	 *
	 * @param listener MedViewPCodeListener
	 */
	public void removeMedViewPCodeListener(MedViewPCodeListener listener)
	{
		listenerList.remove(MedViewPCodeListener.class, listener);
	}

	private void firePCodeNRGeneratorLocationChanged(String location)
	{
		clearPCodeEvent();

		sharedPCodeEvent.setNRGeneratorLocation(location);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPCodeListener.class)
			{
				((MedViewPCodeListener)listeners[i + 1]).

					nrGeneratorLocationChanged(sharedPCodeEvent);
			}
		}
	}

	private void fireGeneratedPCodePrefixChanged(String prefix)
	{
		clearPCodeEvent();

		sharedPCodeEvent.setGeneratedPCodePrefix(prefix);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPCodeListener.class)
			{
				((MedViewPCodeListener)listeners[i + 1]).

					generatedPCodePrefixChanged(sharedPCodeEvent);
			}
		}
	}

	private void firePCodeGeneratorChanged(String className)
	{
		clearPCodeEvent();

		sharedPCodeEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPCodeListener.class)
			{
				((MedViewPCodeListener)listeners[i + 1]).

					pCodeGeneratorChanged(sharedPCodeEvent);
			}
		}
	}


	// PREFERENCE AND {PROPERTY EVENT] RELATED

	/**
	 * Adds a listener notified of preference changes to the list of listeners
	 * associated with the data handler.
	 *
	 * @param listener MedViewPreferenceListener
	 */
	public void addMedViewPreferenceListener(MedViewPreferenceListener listener)
	{
		listenerList.add(MedViewPreferenceListener.class, listener);
	}


	/**
	 * Removes a listener from the list of listeners associated with the data
	 * handler
	 *
	 * @param listener MedViewPreferenceListener
	 */
	public void removeMedViewPreferenceListener(MedViewPreferenceListener listener)
	{
		listenerList.remove(MedViewPreferenceListener.class, listener);
	}

	/**
	 * Adds a listener notified of property changes to the list of listeners
	 * associated with the data handler. When using 'property' methods of the data
	 * handling class, the default user non-class preference node is used to store
	 * the property. You might consider using the 'preferences' methods as an
	 * alternative if you want more control over where the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param listener MedViewPropertyListener
	 */
	public void addMedViewPropertyListener(MedViewPropertyListener listener)
	{
		listenerList.add(MedViewPropertyListener.class, listener);
	}


	/**
	 * Removes a listener from the list of listeners associated with the data
	 * handler. When using 'property' methods of the data handling class, the
	 * default user non-class preference node is used to store the property. You
	 * might consider using the 'preferences' methods as an alternative if you want
	 * more control over where the properties are placed.
	 *
	 * @deprecated Use the preference methods instead.
	 * @param listener MedViewPropertyListener
	 */
	public void removeMedViewPropertyListener(MedViewPropertyListener listener)
	{
		listenerList.remove(MedViewPropertyListener.class, listener);
	}

	/**
	 * Fire to all registered preference listeners that the value of the specified
	 * user preference has changed.
	 *
	 * @param pref String
	 */
	private void fireUserPreferenceChanged(String pref)
	{
		MedViewPreferenceEvent firedEvent = new MedViewPreferenceEvent(this, pref);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPreferenceListener.class)
			{
				((MedViewPreferenceListener)listeners[i + 1]).userPreferenceChanged(firedEvent);
			}
		}
	}

	/**
	 * Fire to all registered preference listeners that the value of the specified
	 * system preference has changed.
	 *
	 * @param pref String
	 */
	private void fireSystemPreferenceChanged(String pref)
	{
		MedViewPreferenceEvent firedEvent = new MedViewPreferenceEvent(this, pref);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPreferenceListener.class)
			{
				((MedViewPreferenceListener)listeners[i + 1]).systemPreferenceChanged(firedEvent);
			}
		}
	}

	private void fireUserPropertyChanged(String prop, String val)
	{
		MedViewPropertyEvent firedEvent = new MedViewPropertyEvent(this, prop, val);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPropertyListener.class)
			{
				((MedViewPropertyListener)listeners[i + 1]).userPropertyChanged(firedEvent);
			}
		}
	}

	private void fireFlagPropertyChanged(String prop, boolean val)
	{
		MedViewPropertyEvent firedEvent = new MedViewPropertyEvent(this, prop, val);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == MedViewPropertyListener.class)
			{
				((MedViewPropertyListener)listeners[i + 1]).flagPropertyChanged(firedEvent);
			}
		}
	}


	// UTILITY METHODS

	/**
	 * Retrieves the users home directory appended with the medview specific
	 * subfolder. For instance, on a Windows system, this could be 'c:\documents
	 * and settings\fredrik\medview\'.
	 *
	 * @return String
	 */
	public String getUserHomeDirectory()
	{
		return mVDSH.getUserHomeDirectory();
	}

	public String getUserCacheDirectory()
	{
		return mVDSH.getUserCacheDirectory();
	}

	/**
	 * Gets a temporary directory for use by MedView.
	 * Implemented as {system property <code>java.io.tmpdir</code>}/medview.
	 * On UNIX systems the default value of <code>java.io.tmpdir</code> is
	 * typically "/tmp" or "/var/tmp"; on Microsoft Windows systems it is
	 * typically "c:\\temp".
	 *
	 * @return the path to the medview temp directory.
	 * @see java.io.File#createTempFile(java.lang.String, java.lang.String, java.io.File)
	 */
	public String getMedViewTempDirectory()
	{
		return mVDSH.getMedViewTempDirectory(); // Added by Nils 2004-03-29
	}

	/**
	 * Returns a full path to the directory that is used by default for placing log
	 * files and the like.
	 *
	 * @return String
	 */
	public String getDefaultLogDirectory()
	{
		String fS = System.getProperty("file.separator");

		return (mVDSH.getUserHomeDirectory() + "logs" + fS);
	}


	/**
	 * Utility method for clearing the shared data
	 * event from any eventually set values.
	 */
	private void clearDataEvent()
	{
		sharedDataEvent.setTerm(null);

		sharedDataEvent.setValue(null);

		sharedDataEvent.setClassName(null);

		sharedDataEvent.setIdentifier(null);

		sharedDataEvent.setLocation(null);

		sharedDataEvent.setLocationID(null);
	}

	/**
	 * Utility method for clearing the shared pcode
	 * event from any eventually set values.
	 */
	private void clearPCodeEvent()
	{
		sharedPCodeEvent.setGeneratedPCodePrefix(null);

		sharedPCodeEvent.setNRGeneratorLocation(null);

		sharedPCodeEvent.setClassName(null);
	}


	// SHUT-DOWN NOTIFICATION

	/**
	 * Call this method when your program is shutting down.
	 */
	public void shuttingDown()
	{
		DataHandlerFactory.instance().getExaminationDataHandler().shuttingDown();

		DataHandlerFactory.instance().getTermDataHandler().shuttingDown();

		DataHandlerFactory.instance().getTemplateAndTranslatorDataHandler().shuttingDown();

		pCodeGeneratorFactory.getPCodeGenerator().shuttingDown();
	}


	// CONSTRUCTOR

	private MedViewDataHandler()
	{
		// settings handler

		mVDSH = MedViewDataSettingsHandler.instance();

		// factories

		pCodeGeneratorFactory = PCodeGeneratorFactory.instance();

		examplePIDGeneratorFactory = new ExamplePIDGeneratorFactory();

		// media and language handlers

		mediaHandler = new MedViewMediaHandler();

		languageHandler = new MedViewLanguageHandler();

		// listener list

		listenerList = new EventListenerList();

		// pid validators and parsers

		pidValidator = new CompositeRawPIDValidator();

		pidParser = new CompositePIDParser();

		OldPCodeFormatRawPIDValidator oldV = new OldPCodeFormatRawPIDValidator();

		NewPCodeFormatRawPIDValidator newV = new NewPCodeFormatRawPIDValidator();

		pCodeValidators = new RawPIDValidator[]
			{oldV, newV};

		// shared events

		sharedDataEvent = new MedViewDataEvent(this);

		sharedPCodeEvent = new MedViewPCodeEvent(this);

		// add listener to default term datahandler

		termDataHandlerListener = new TDHListener();

		TermDataHandler defaultTDH = DataHandlerFactory.instance().getDefaultTermDataHandler();

		defaultTDH.addTermDataHandlerListener(termDataHandlerListener);

		// add listener to default examination datahandler

		examinationDataHandlerListener = new EDHListener();

		ExaminationDataHandler defaultEDH = DataHandlerFactory.instance().getDefaultExaminationDataHandler();

		defaultEDH.addExaminationDataHandlerListener(examinationDataHandlerListener);

		// add listener to default pcode handler

		pCodeGeneratorListener = new PCGListener();

		PCodeGenerator defaultPCG = pCodeGeneratorFactory.getDefaultPCodeGenerator();

		defaultPCG.addPCodeGeneratorListener(pCodeGeneratorListener);

		// search engine

		examinationDataSearchEngine = new DefaultExaminationDataSearchEngine();
	}

	// MEMBERS

	private final PIDParser pidParser;

	private final RawPIDValidator pidValidator;

	private final RawPIDValidator[] pCodeValidators;

	private MedViewDataEvent sharedDataEvent;

	private MedViewPCodeEvent sharedPCodeEvent;

	private MedViewDataSettingsHandler mVDSH;

	private static MedViewDataHandler instance;

	private final EventListenerList listenerList;

	private final MedViewMediaHandler mediaHandler;

	private final MedViewLanguageHandler languageHandler;

	private final PCodeGeneratorFactory pCodeGeneratorFactory;

	private final PCodeGeneratorListener pCodeGeneratorListener;

	private final TermDataHandlerListener termDataHandlerListener;

	private final ExamplePIDGeneratorFactory examplePIDGeneratorFactory;

	private final ExaminationDataSearchEngine examinationDataSearchEngine;

	private final ExaminationDataHandlerListener examinationDataHandlerListener;

	public static final String DEFAULT_TERM_VALUE = "N/A";


	// INNER CLASS - TERM DATAHANDLER LISTENER

	private class TDHListener implements TermDataHandlerListener
	{
		public void termAdded(TermDataHandlerEvent e)
		{
			fireTermAdded(e.getTerm());
		}

		public void termRemoved(TermDataHandlerEvent e)
		{
			fireTermRemoved(e.getTerm());
		}

		public void valueAdded(TermDataHandlerEvent e)
		{
			fireValueAdded(e.getTerm(), e.getValue());
		}

		public void valueRemoved(TermDataHandlerEvent e)
		{
			fireValueRemoved(e.getTerm(), e.getValue());
		}

		public void termDefinitionLocationChanged(TermDataHandlerEvent e)
		{
			fireTermLocationChanged();
		}

		public void termValueLocationChanged(TermDataHandlerEvent e)
		{
			fireTermLocationChanged();
		}
	}

	// INNER CLASS - EXAMINATION DATAHANDLER LISTENER

	private class EDHListener implements ExaminationDataHandlerListener
	{
		public void examinationLocationChanged(ExaminationDataHandlerEvent e)
		{
			fireExaminationDataLocationChanged(e.getLocation());
		}

		public void examinationLocationIDChanged(ExaminationDataHandlerEvent e)
		{
			fireExaminationDataLocationIDChanged(e.getLocationID());
		}

		public void examinationAdded(ExaminationDataHandlerEvent e)
		{
			fireExaminationAdded(e.getIdentifier());
		}

		public void examinationUpdated(ExaminationDataHandlerEvent e)
		{
			fireExaminationUpdated(e.getIdentifier());
		}

		public void examinationRemoved(ExaminationDataHandlerEvent e)
		{
			fireExaminationRemoved(e.getIdentifier());
		}
	}

	// INNER CLASS - PCODE GENERATOR LISTENER

	private class PCGListener implements PCodeGeneratorListener
	{
		public void locationChanged(PCodeGeneratorEvent e)
		{
			firePCodeNRGeneratorLocationChanged(e.getLocation());
		}

		public void prefixChanged(PCodeGeneratorEvent e)
		{
			fireGeneratedPCodePrefixChanged(e.getPrefix());
		}
	}


	// UNIT TEST METHOD

	public static void main(String[] args)
	{
	}

}
