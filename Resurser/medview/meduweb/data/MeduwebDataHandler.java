/*
 * @(#)MeduwebDataHandler.java
 *
 * $Id: MeduwebDataHandler.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: MeduwebDataHandler.java,v $
 * Revision 1.3  2003/08/19 22:00:44  d99figge
 * Diverse uppdateringar. Se Bugzilla för detaljer.
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
 * Added support for notifying of progress in the data handling layer using the new ProgressNotifiable interface in misc.foundation - a new method was added to the ExaminationDataHandler interface and thus MeduwebDataHandler for this purpose. // Fredrik
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

package medview.meduweb.data;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.examination.*;
import medview.datahandling.*;

import misc.foundation.*;
import misc.foundation.io.*;

/**
 * The MeduwebDataHandler class is a facade for the common
 * data handling part of the medview programs. Core medview
 * functionality involving various data and its processing
 * are kept in the common datahandling packages, while more
 * application-specific handling should be contained in the
 * appropriate package structure of that application.
 *
 * @author Fredrik Lindahl
 */
public class MeduwebDataHandler
{

// -----------------------------------------------------------------------------
// ********************** EXAMINATION DATAHANDLING METHODS *********************
// -----------------------------------------------------------------------------

	/**
	 * Returns an array of all the patients listed
	 * at the currently set data location. If the
	 * data location for some reason is inaccesible
	 * or if it contains no patients, an empty array
	 * will be returned. A null value is never to be
	 * returned.
	 */
	public String[] getPatients() throws IOException
	{
		return dataHandlerFactory.getExaminationDataHandler().getPatients();
	}


	/**
	 * Returns an array of all the patients listed
	 * at the currently set data location. If the
	 * data location for some reason is inaccesible
	 * or if it contains no patients, an empty array
	 * will be returned. A null value is never to be
	 * returned. This version of the method accepts a
	 * Notifiable implementation to notify of the
	 * progress of the patient retrieval. If the
	 * argument is null, the method does exactly the
	 * same thing as the normal getPatients() method.
	 */
	public String[] getPatients( ProgressNotifiable notifiable ) throws IOException
	{
		return dataHandlerFactory.getExaminationDataHandler().getPatients(notifiable);
	}


	/**
	 * Returns an array of newly added examination identifiers
	 * since the last time either the getExaminations(), the
	 * getPatients(), the getImageCount(), the getExaminations(),
	 * or the getExaminationValueContainers() methods (or this
	 * method itself) was called. Can be used to check if new
	 * examinations have been added while the application has
	 * been running.
	 * @throws IOException if some error occurs during the
	 * retrieval of the examination list.
	 */
	public ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		return dataHandlerFactory.getExaminationDataHandler().refreshExaminations();
	}


	/**
	 * Returns an integer representing the total
	 * number of images taken for a patient with
	 * the specified patient code. If the data
	 * location for some reason is inaccessible,
	 * or some other error occurs, a value of 0
	 * will be returned.
	 */
	public int getImageCount(String patientCode) throws IOException
	{
		return dataHandlerFactory.getExaminationDataHandler().getImageCount(patientCode);
	}


	/**
	 * Returns an array of ExaminationIdentifiers describing
	 * the examinations for the specified patientcode. An empty
	 * array is returned if no examinations for the specified
	 * patient code were found or if the data location for some
	 * reason could not be read. Note that null values are never
	 * returned.
	 */
	public ExaminationIdentifier[] getExaminations(String patientCode) throws IOException
	{
		return dataHandlerFactory.getExaminationDataHandler().getExaminations(patientCode);
	}


	/**
	 * Returns an ExaminationValueContainer containing the
	 * values for the examination identified by the argument.
	 * Will return null if there is no examination value
	 * container for the argument identifier.
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		return dataHandlerFactory.getExaminationDataHandler().getExaminationValueContainer(id);
	}


    /**
     * Saves a Tree of examination data to the tree file database. An
     * event will be fired upon successful completion of the save
     * notifying interested data listeners of the event.
     * @param pTree a Tree of examination data to be saved to the database
     * @throws IOException if some error occurs during saving.
     */
    /*public void saveTree(medview.datahandling.examination.tree.Tree tree) throws IOException
    {
		dataHandlerFactory.getExaminationDataHandler().saveTree(tree);
    }*/

	/**
	 * Returns the set examination data location
	 * in it's 'raw' form. For example, it could
	 * return 'c:\medview\databases\MSTest.mvd'.
	 * A null value will be returned if the set
	 * location is invalid or for some other reason
	 * cannot be accessed. Also, if the location
	 * has not yet been set, a null value will be
	 * returned.
	 */
	public String getExaminationDataLocation()
	{
		return dataHandlerFactory.getExaminationDataHandler().getExaminationDataLocation();
	}


	/**
	 * Returns the data location expressed in a
	 * (perhaps) more simple way than the 'raw'
	 * data location. For example, it could
	 * return 'MSTest.mvd' instead of the full
	 * file path to the mvd. This could be used
	 * for displaying the chosen location in a
	 * graphical user interface, for example. A
	 * string specifying that the location is not
	 * valid or not set correctly will be returned
	 * if the location is not set or invalid.
	 */
	public String getExaminationDataLocationID( )
	{
		return dataHandlerFactory.getExaminationDataHandler().getExaminationDataLocationID();
	}


	/**
	 * Sets the location of the examination data.
	 * For example - for a MVD handler, the location
	 * could be 'c:\medview\databases\MSTest.mvd',
	 * while for a SQL handler could be something like
	 * 'login:password@server.myip.org:1024'.
	 */
	public void setExaminationDataLocation(String location) throws InvalidDataLocationException
	{
		dataHandlerFactory.getExaminationDataHandler().setExaminationDataLocation(location);

	}

// -----------------------------------------------------------------------------
// *****************************************************************************
// -----------------------------------------------------------------------------








// -----------------------------------------------------------------------------
// *************************** TERM HANDLING METHODS ***************************
// -----------------------------------------------------------------------------

	/**
	 * Returns whether or not the specified
	 * term exists. Will throw exceptions if
	 * the necessary storage locations where
	 * term information is kept is unavailable.
	 * A term exists if it is defined in the
	 * term definition location. Note that a
	 * derived term (such as 'PCode(age)') may
	 * exist in the template but not in the
	 * data location files, so this method will
	 * return a false value if asked if such a
	 * derived term exists.
	 */
	public boolean termExists(String term) throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		return dataHandlerFactory.getTermDataHandler().termExists(term);
	}


	/**
	 * Returns whether or not the specified
	 * type is valid. It is valid if it can
	 * be recognized as one of the types
	 * specified by the constants in the
	 * TermDataHandler interface.
	 */
	public boolean isValidTermType(int type)
	{
		return dataHandlerFactory.getTermDataHandler().isValidTermType(type);
	}


	/**
	 * Adds a term with the specified type to
	 * permanent storage. Will throw exceptions
	 * if the type is invalid or the term information
	 * locations could not be parsed for some reason.
	 * Also, will throw exceptions if the term information
	 * locations are unavailable. When the term is added,
	 * it will be added to the term definition listing.
	 */
	public void addTerm(String term, int type) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		InvalidTypeException, ValueLocationNotFoundException
	{
		dataHandlerFactory.getTermDataHandler().addTerm(term, type);
	}


	/**
	 * Adds a term with the specified type to
	 * permanent storage. Will throw exceptions
	 * if the type is invalid or the term information
	 * locations could not be parsed for some reason.
	 * Also, will throw exceptions if the term information
	 * locations are unavailable. When the term is added,
	 * it will be added to the term definition listing.
	 */
	public void addTerm(String term, String type) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		InvalidTypeException, ValueLocationNotFoundException
	{
		dataHandlerFactory.getTermDataHandler().addTerm(term, type);
	}


	/**
	 * Removes the specified term from the
	 * permanent storage. Throws exceptions
	 * if this cannot be performed or if the
	 * term's type is invalid.
	 */
	public void removeTerm(String term) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		ValueLocationNotFoundException, InvalidTypeException
	{
		dataHandlerFactory.getTermDataHandler().removeTerm(term);
	}


	/**
	 * Return all possible values associated with a specified term.
	 * Will return the values as an array of String objects. Note
	 * that if the term exists, but has no value mapping in the
	 * value location, an empty array will be returned. A null value
	 * is never returned.
	 */
	public String[] getValues(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		ValueLocationNotFoundException, CouldNotParseException
	{
		return dataHandlerFactory.getTermDataHandler().getValues(term);
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
		return "<n/a>"; // dummy implementation...
	}


	/**
	 * Adds a value to the specified term. Throws an
	 * exception if for some reason this cannot be
	 * accomplished, where the exception specifies
	 * what went wrong.
	 */
	public void addValue(String term, Object value) throws
		NoSuchTermException, ValueLocationNotFoundException,
		DefinitionLocationNotFoundException, CouldNotParseException,
		InvalidTypeException
	{
		dataHandlerFactory.getTermDataHandler().addValue(term, value);
	}


	/**
	 * Removes a value from a term (value = 'Italien',
	 * 'Ryssland', etc.). Throws an exception if this
	 * cannot be accomplished for some reason, where
	 * the exception specifies what went wrong.
	 */
	public void removeValue(String term, Object value) throws
		NoSuchTermException, ValueLocationNotFoundException,
		DefinitionLocationNotFoundException, CouldNotParseException, InvalidTypeException
	{
		dataHandlerFactory.getTermDataHandler().removeValue(term, value);
	}


	/**
	 * Returns the main type of the passed-along term as
	 * one of the type-defining integers found in the
	 * TermDataHandler interface.
	 */
	public int getType(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		InvalidTypeException, CouldNotParseException
	{
		return dataHandlerFactory.getTermDataHandler().getType(term);
	}


	/**
	 * Returns a text description of the specified term type.
	 * The text description returned is equal to one of the
	 * string constants defined in the TermDataHandler
	 * interface.
	 */
	public String getTypeDescription(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		InvalidTypeException, CouldNotParseException
	{
		return dataHandlerFactory.getTermDataHandler().getTypeDescription(term);
	}


	/**
	 * Returns a textual description of the specified
	 * term type. The text description returned is equal
	 * to one of the string constants defined in the
	 * TermDataHandler interface. A null value will be
	 * returned if none of the integer constants in the
	 * TermDataHandler interface matches the specified
	 * type.
	 */
	public String getTypeDescription(int type)
	{
		return dataHandlerFactory.getTermDataHandler().getTypeDescription(type);
	}


	/**
	 * Returns all defined terms. If the term information
	 * locations are unavailable for some reason, the
	 * returned array will be empty, but a non-null value
	 * is guaranteed. Note that derived terms such as
	 * 'PCode(age)' are not contained in the returned array,
	 * only the low-level terms contained in the definitions
	 * file (usually, but not necessarily, also in the term
	 * value file).
	 */
	public String[] getTerms() throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		return dataHandlerFactory.getTermDataHandler().getTerms();
	}


	/**
	 * Removes all terms from the term definition and
	 * value locations, and sets them to the specified
	 * arguments. This method is for performance reasons,
	 * you can do exactly the same thing term-by-term using
	 * other term methods. Note that this method is highly
	 * sensitive to exceptions if misused, and that it
	 * should only be used if you are sure of what you are
	 * doing, since it will remove the term value and
	 * definition location files prior to adding the new
	 * values and definitions.
	 * @param terms a String array of the desired term names.
	 * @param types a String array of the desired term types,
	 * where types[x] is the type of terms[x].
	 * @param values an Object array of Object arrays, where
	 * values[x] contains the object array containing terms[x]'s
	 * values to be specified in the term location.
	 */
	public void setTerms(String[] terms, String[] types, Object[][] values) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		ValueLocationNotFoundException, InvalidTypeException
	{
		dataHandlerFactory.getTermDataHandler().setTerms(terms, types, values);
	}


	/**
	 * Sets the location of the term definitions.
	 * This will simply set the value of a property
	 * that is placed in permanent storage and that
	 * can be returned at later executions. Will
	 * fire a 'term location changed' event to all
	 * registered data handler data listeners.
	 */
	public void setTermDefinitionLocation(String loc)
	{
		dataHandlerFactory.getTermDataHandler().setTermDefinitionLocation(loc);


	}


	/**
	 * Returns the location of the term definitions.
	 * If the location has not yet been set, or if the
	 * location is invalid, a null value will be
	 * returned.
	 */
	public String getTermDefinitionLocation()
	{
		return dataHandlerFactory.getTermDataHandler().getTermDefinitionLocation();
	}


	/**
	 * Sets the location of the term values.
	 * This will simply set the value of a property
	 * that is placed in permanent storage and that
	 * can be returned at later executions. Will
	 * fire a 'term location changed' event to all
	 * registered data handler data listeners.
	 */
	public void setTermValueLocation(String loc)
	{
		dataHandlerFactory.getTermDataHandler().setTermValueLocation(loc);

	}


	/**
	 * Returns the location of the term values.
	 * If the location has not yet been set, or if the
	 * location is invalid, a null value will be
	 * returned.
	 */
	public String getTermValueLocation()
	{
		return dataHandlerFactory.getTermDataHandler().getTermValueLocation();
	}


	/**
	 * Returns whether or not the currently set
	 * (or not set) term definition location is
	 * a valid one.
	 */
	public boolean isTermDefinitionLocationValid()
	{
		return dataHandlerFactory.getTermDataHandler().isTermDefinitionLocationValid();
	}

	/**
	 * Returns whether or not the currently set
	 * (or not set) term value location is
	 * a valid one.
	 */
	public boolean isTermValueLocationValid()
	{
		return dataHandlerFactory.getTermDataHandler().isTermValueLocationValid();
	}

// -----------------------------------------------------------------------------
// *****************************************************************************
// -----------------------------------------------------------------------------








// -----------------------------------------------------------------------------
// ********************** TEMPLATE AND TRANSLATOR METHODS **********************
// -----------------------------------------------------------------------------

	public void saveTemplate(medview.common.template.TemplateModel model, String filePath)
		throws CouldNotSaveException
	{
		TemplateModelWrapper wrapper = new TemplateModelWrapper();

		wrapper.setTemplateModelLocation(filePath);

		wrapper.setTemplateModel(model);

		saveTemplate(wrapper);
	}


	public void saveTemplate(TemplateModelWrapper wrapper)
		throws CouldNotSaveException
	{
		dataHandlerFactory.getTemplateAndTranslatorDataHandler().saveTemplate(wrapper);
	}


	public medview.common.template.TemplateModel loadTemplate(String filePath)
		throws InvalidVersionException, CouldNotLoadException
	{
		TemplateModelWrapper wrapper = dataHandlerFactory.getTemplateAndTranslatorDataHandler().loadTemplate(filePath);

		return wrapper.getTemplateModel();
	}


	public TemplateModelWrapper loadTemplateWrapper(String filePath)
		throws InvalidVersionException, CouldNotLoadException
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().loadTemplate(filePath);
	}


	public void saveTranslator(medview.meduweb.data.MeduwebTranslatorModel model, String filePath)
	{
		dataHandlerFactory.getTemplateAndTranslatorDataHandler().saveTranslator(model, filePath);
	}


	public medview.meduweb.data.MeduwebTranslatorModel loadTranslator(String filePath)
		throws CouldNotLoadException
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().loadTranslator(filePath);
	}


	public String[] getSectionNames(String filePath)
		throws NoSuchTemplateException
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().getSectionNames(filePath);
	}


	public String parseTATDHLocation(String location)
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().parseIdentifier(location);
	}


	/**
	 * @throws MethodNotSupportedException when the current
	 * implementation of the TemplateAndTranslatorDataHandler
	 * interface does not use directories for data storage,
	 * maybe it uses SQL / JDBC data storage or other forms.
	 */
	public String getDefaultTemplateDirectory() throws MethodNotSupportedException
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().getDefaultTemplateDirectory();
	}


	/**
	 * @throws MethodNotSupportedException when the current
	 * implementation of the TemplateAndTranslatorDataHandler
	 * interface does not use directories for data storage,
	 * maybe it uses SQL / JDBC data storage or other forms.
	 */
	public String getDefaultTranslatorDirectory() throws MethodNotSupportedException
	{
		return dataHandlerFactory.getTemplateAndTranslatorDataHandler().getDefaultTranslatorDirectory();
	}

// -----------------------------------------------------------------------------
// ********************** TEMPLATE AND TRANSLATOR METHODS **********************
// -----------------------------------------------------------------------------








// -----------------------------------------------------------------------------
// *************************** PCODE PARSING METHODS ***************************
// -----------------------------------------------------------------------------

	/**
	 * Retrieves the current age of the patient
	 * with the specified pcode.
	 * @param pCode the pcode.
	 * @return the age of the patient identified
	 * by the pcode, as it is today.
	 */
	public int getAge(String pCode) throws InvalidPCodeException
	{
		return pCodeParserFactory.getPCodeParser().getAge(pCode);
	}


	/**
	 * Retrieves the age of the patient with the specified pcode
	 * at a certain point in time.
	 * @param pCode the pcode.
	 * @param atDate the time at which the age of
	 * the patient should be returned.
	 * @return the age of the patient as it was
	 * at the specified date.
	 */
	public int getAge(String pCode, Date atDate) throws InvalidPCodeException
	{
		return pCodeParserFactory.getPCodeParser().getAge(pCode, atDate);
	}


	/**
	 * Retrieves the internal patient identifier (for instance, G9238).
	 * @param pCode the PCode string that information is extracted from.
	 * @return a String object representing the internal patient identifier.
	 * @throws InvalidPCodeException if the specified pcode is invalid format.
	 */
	public String getPatientIdentifier(String pCode) throws InvalidPCodeException
	{
		return pCodeParserFactory.getPCodeParser().getPatientIdentifier(pCode);
	}


	/**
	 * Retrieves the year of birth (1900-2099) for the patient with the
	 * passed-along patient code.
	 * @param pCode the PCode string that information is extracted from.
	 * @return an integer representing the year of birth for the patient.
	 * @throws InvalidPCodeException if the specified pcode is invalid format.
	 */
	public int getYearOfBirth(String pCode) throws InvalidPCodeException
	{
		return pCodeParserFactory.getPCodeParser().getYearOfBirth(pCode);
	}


	/**
	 * Retrieve the gender of the patient as integer constants.
	 * @param pCode the PCode string that information is extracted from.
	 * @return either PCodeParser.MALE or PCodeParser.FEMALE (integer constants).
	 * @throws InvalidPCodeException if the specified pcode is invalid format.
	 */
	public int getGender(String pCode) throws InvalidPCodeException
	{
		return pCodeParserFactory.getPCodeParser().getGender(pCode);
	}

	/**
	 *Retrieve the prefix that specifies the person that carried out the examination.
	 *@param pCode the PCode string that information is extracted from.
	 *@return The text string specifying the examiner/dentist.
	 *@throws InvalidPCodeException if the specified pcode is invalid format.
	 */
	public String getPrefix(String pCode) throws InvalidPCodeException
	{
		return ((DefaultPCodeParser)pCodeParserFactory.getPCodeParser()).getPrefix(pCode);
	}
	
	/**
	 * Returns whether or not the specified pcode is valid. The format
	 * for pcodes in this instance of PCodeParser is A[A]NNNN(0|9)YY(0|1),
	 * so an example would be G01959390.
	 * @return a boolean value indicating the validity of the
	 * specified pCode.
	 */
	public boolean validates(String pCode)
	{
		return pCodeParserFactory.getPCodeParser().validates(pCode);
	}


	/**
	 * Retrieves an example p-code, conforming to the format
	 * recognized by the PCodeParser implementation.
	 * @return a pcode string in a format supported by the
	 * current implementation.
	 */
	public String getExamplePCode()
	{
		return pCodeParserFactory.getPCodeParser().getExamplePCode();
	}

// -----------------------------------------------------------------------------
// *****************************************************************************
// -----------------------------------------------------------------------------
// ***************************** LANGUAGE METHODS ******************************
// -----------------------------------------------------------------------------

	/**
	 * Retrieves the language-specific string corresponding
	 * to the currently set language's means of describing
	 * the phrase described by the constant identifier.
	 */
	public  String getLanguageString(String propertyIdentifier)
	{
		return languageHandler.getLanguageString(propertyIdentifier);
	}


	/**
	 * Tries to change the language to the specified language
	 * identifier. The language identifier is the file name of
	 * the language resource file (.lrf file), so if the file
	 * is named "english.lrf", the user should specify the
	 * argument "english" to this method.
	 */
	public void changeLanguage(String langId) throws LanguageException
	{
		languageHandler.changeLanguage(langId);

	}


	/**
	 * Retrieves a list of the currently installed and
	 * available languages on the system.
	 */
	public String[] getAvailableLanguages()
	{
		return languageHandler.getAvailableLanguages();
	}


	/**
	 * Retrieves the current language's descriptor, which
	 * is used to show to the user the language currently
	 * in use. For instance, could be "english".
	 */
	public String getCurrentLanguageDescriptor()
	{
		return languageHandler.getCurrentLanguageDescriptor();
	}






// -----------------------------------------------------------------------------
// ******************************* OTHER METHODS *******************************
// -----------------------------------------------------------------------------

	/**
	 * Obtains the data handler singleton instance.
	 * Will only instantiate a data handler the
	 * first time of access.
	 */
	public static MeduwebDataHandler instance()
	{
		if (instance == null) { instance = new MeduwebDataHandler(); }

		return instance;
	}



// -----------------------------------------------------------------------------
// *****************************************************************************
// -----------------------------------------------------------------------------

	private MeduwebDataHandler()
	{

		dataHandlerFactory = new MeduwebDataHandlerFactory();

		pCodeParserFactory = new MeduwebPCodeParserFactory();

		languageHandler = new MeduwebLanguageHandler();
	}

	private static MeduwebDataHandler instance;

	private MeduwebDataHandlerFactory dataHandlerFactory;

	private MeduwebPCodeParserFactory pCodeParserFactory;

	private final MeduwebLanguageHandler languageHandler;









// -----------------------------------------------------------------------------
// **************************** UNIT TEST METHOD *******************************
// -----------------------------------------------------------------------------

	public static void main(String[] args)
	{
		try
		{
			MeduwebDataHandler mVDH = MeduwebDataHandler.instance();

			System.out.println("Will not test data handler term methods");

			System.out.println("NOTE - do not forget to remove the added ");

			System.out.println("terms afterwards from the term value and ");

			System.out.println("definition locations!");

			System.out.println("=======================================");

			System.out.print("Term definition location = ");

			System.out.println(mVDH.getTermDefinitionLocation());

			System.out.print("Term value location = ");

			System.out.println(mVDH.getTermValueLocation());

			System.out.println("termExists(\"born\") = " + mVDH.termExists("born"));

			System.out.println("termExists(\"BorN\") = " + mVDH.termExists("BorN"));

			System.out.println("termExists(\"hopp\") = " + mVDH.termExists("hopp"));

			System.out.print("adding term \"test1\" int type multiple...");

			mVDH.addTerm("test1", TermDataHandler.MULTIPLE_TYPE);

			System.out.println("done!");

			System.out.print("adding term \"test2\" int type free...");

			mVDH.addTerm("test2", TermDataHandler.FREE_TYPE);

			System.out.println("done!");

			System.out.print("adding term \"test3\" int type interval...");

			mVDH.addTerm("test3", TermDataHandler.INTERVAL_TYPE);

			System.out.println("done!");

			System.out.print("adding term \"test4\" int type regular...");

			mVDH.addTerm("test4", TermDataHandler.REGULAR_TYPE);

			System.out.println("done!");

			System.out.print("adding term \"teSt5\" string type multiple...");

			mVDH.addTerm("teSt5", TermDataHandler.MULTIPLE_STRING);

			System.out.println("done!");

			System.out.print("adding term \"Test6\" string type free...");

			mVDH.addTerm("Test6", TermDataHandler.FREE_STRING);

			System.out.println("done!");

			System.out.print("adding term \"tesT7\" string type interval...");

			mVDH.addTerm("tesT7", TermDataHandler.INTERVAL_STRING);

			System.out.println("done!");

			System.out.print("adding term \"TEST8\" string type regular...");

			mVDH.addTerm("TEST8", TermDataHandler.REGULAR_STRING);

			System.out.println("done!");

			System.out.print("adding values \"banan\", \"äpple\", and \"päron\" to term \"test1\"...");

			mVDH.addValue("test1", "banan");

			mVDH.addValue("test1", "äpple");

			mVDH.addValue("test1", "päron");

			System.out.println("done!");

			System.out.print("removing term \"test2\"...");

			mVDH.removeTerm("test2");

			System.out.println("done!");

			System.out.print("removing value \"äpple\" from term \"test1\"...");

			mVDH.removeValue("test1", "äpple");

			System.out.println("done!");
		}
		catch (Exception e)
		{
			System.out.println("An exception was thrown -> " + e.getClass().getName());

			System.out.println(e.getMessage());
		}
	}

// -----------------------------------------------------------------------------
// *****************************************************************************
// -----------------------------------------------------------------------------

}
