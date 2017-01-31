/*
 * @(#)TermDataHandler.java
 *
 * $Id: TermDataHandler.java,v 1.14 2005/02/24 17:09:28 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.io.*;

/**
 * Defines methods used to handle terms and their
 * values and types. When adding terms, they
 * should be added exactly as they are typed, with
 * letter case in mind. When removing a term, when
 * retrieving values for a term, when checking if
 * a term exists, when adding a value to a term,
 * when removing a value from a term, and when
 * retrieving the integer type or string type
 * representation, the specified term to operate on
 * will be located as existant if it appears in any
 * case (i.e. the two calls 'addValue("born", v)'
 * and 'addValue("BoRN", v)' will do exactly the same
 * thing, if the term "Born" is in the definitions
 * and/or value locations).
 *
 * @author Fredrik Lindahl
 */
public interface TermDataHandler
{

	public static final String FREE_TYPE_DESCRIPTOR = "free";

	public static final String REGULAR_TYPE_DESCRIPTOR = "regular";

	public static final String MULTIPLE_TYPE_DESCRIPTOR = "multiple";

	public static final String INTERVAL_TYPE_DESCRIPTOR = "interval";

	public static final String UNKNOWN_TYPE_DESCRIPTOR = "unknown";


	public static final int FREE_TYPE = 1;

	public static final int REGULAR_TYPE = 2;

	public static final int MULTIPLE_TYPE = 4;

	public static final int INTERVAL_TYPE = 8;


	// LISTENERS AND EVENT NOTIFICATION

	/**
	 * Adds a term datahandler listener.
	 */
	void addTermDataHandlerListener(TermDataHandlerListener l);

	/**
	 * Removes a term datahandler listener.
	 */
	void removeTermDataHandlerListener(TermDataHandlerListener l);


	// SHUT-DOWN NOTIFICATION

	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	void shuttingDown();


	// TERM TYPES AND TYPE DESCRIPTORS

	/**
	 * Returns whether or not the specified
	 * type is valid. It is valid if it is
	 * recognized by the implementation class.
	 */
	boolean isValidTermType(int type);

	/**
	 * Returns whether or not the specified type
	 * descriptor is valid.
	 * @param typeDesc String
	 * @return boolean
	 */
	boolean isValidTermTypeDescriptor(String typeDesc);

	/**
	 * Returns the main type of the passed-along term as
	 * a type-defining integer.
	 */
	int getType(String term) throws NoSuchTermException, IOException, InvalidTypeException;

	/**
	 * Returns a textual description of the specified term.
	 * The text description returned is equal to one of
	 * the string constants defined in the TermDataHandler
	 * interface. An exception will be thrown if the type
	 * of the specified term could not be obtained.
	 */
	String getTypeDescriptor(String term) throws NoSuchTermException, InvalidTypeException, IOException;

	/**
	 * Returns a textual description of the specified
	 * term type. The text description returned is equal
	 * to one of the string constants defined in the
	 * TermDataHandler interface. A null value will be
	 * returned if none of the integer constants in the
	 * TermDataHandler interface matches the specified
	 * type.
	 */
	String getTypeDescriptor(int type) throws InvalidTypeException;


	// TERM LOCATIONS

	/**
	 * Returns whether or not the currently set
	 * (or not set) term definition location is
	 * a valid one.
	 */
	boolean isTermDefinitionLocationValid();

	/**
	 * Returns whether or not the currently set
	 * (or not set) term value location is
	 * a valid one.
	 */
	boolean isTermValueLocationValid();

	/**
	 * Returns whether or not the term definition
	 * location has been set.
	 * @return boolean
	 */
	boolean isTermDefinitionLocationSet();

	/**
	 * Returns whether or not the term value location
	 * has been set.
	 * @return boolean
	 */
	boolean isTermValueLocationSet();

	/**
	 * Sets the location of the term definitions.
	 * This will simply set the value of a property
	 * that is placed in permanent storage and that
	 * can be returned at later executions.
	 */
	void setTermDefinitionLocation(String loc);

	/**
	 * Returns the location of the term definitions.
	 * If the location has not yet been set, or if the
	 * location is invalid, a null value will be
	 * returned.
	 */
	String getTermDefinitionLocation();

	/**
	 * Sets the location of the term values.
	 * This will simply set the value of a property
	 * that is placed in permanent storage and that
	 * can be returned at later executions.
	 */
	void setTermValueLocation(String loc);

	/**
	 * Returns the location of the term definitions.
	 * If the location has not yet been set, or if
	 * the location is invalid, a null value will be
	 * returned.
	 */
	String getTermValueLocation();


	// TERM ADDITION / REMOVAL

	/**
	 * Adds a term with the specified type to
	 * permanent storage. Will throw exceptions
	 * if the type is invalid or the term information
	 * locations could not be parsed for some reason.
	 * Also, will throw exceptions if the term information
	 * locations are unavailable. When the term is added,
	 * it will be added to the term definition listing.
	 */
	void addTerm(String term, int type) throws IOException, InvalidTypeException;

	/**
	 * Adds a term with the specified type to
	 * permanent storage. Will throw exceptions
	 * if the type is invalid or could not be
	 * parsed for some reason. Also, will throw
	 * exceptions if the term information locations
	 * are unavailable for some reason. When the
	 * term is added, it will be added to the term
	 * definition listing.
	 */
	void addTerm(String term, String typeDesc) throws IOException, InvalidTypeException;

	/**
	 * Removes the specified term from the
	 * permanent storage. Throws exception
	 * if this cannot be performed or if the
	 * term is non-existant.
	 */
	void removeTerm(String term) throws IOException, NoSuchTermException;


	// VALUES

	/**
	 * Return all possible values associated with a specified term.
	 * Will return the values as an array of String objects, if the
	 * term value location for some reason is inaccessible or if
	 * some other failure occurs, an exception will be thrown. A
	 * null value is never returned from this method.
	 */
	String[] getValues(String term) throws IOException, NoSuchTermException;

	/**
	 * Adds a value to the specified term. Throws an
	 * exception if for some reason this cannot be
	 * accomplished, where the exception specifies
	 * what went wrong.
	 */
	void addValue(String term, Object value) throws IOException, NoSuchTermException, InvalidTypeException;

	/**
	 * Removes a value from a term (value = 'Italien',
	 * 'Ryssland', etc.). Throws an exception if this
	 * cannot be accomplished for some reason, where
	 * the exception specifies what went wrong.
	 */
	void removeValue(String term, Object value) throws IOException, NoSuchTermException, InvalidTypeException;


	// MISC

	/**
	 * Returns whether or not the specified
	 * term exists. Will throw exceptions if
	 * the necessary storage locations where
	 * term information is kept is unavailable.
	 * A term exists if it is defined in the
	 * term definition location.<br>
	 * <br>
	 * Note: when determining whether or not a
	 * term exists, this should be insensitive
	 * to letter case. Thus, say you have a term
	 * specified as "Born", and you call
	 * termExists("born"), this method will return
	 * true.
	 */
	boolean termExists(String term) throws IOException;

	/**
	 * Returns whether or not the specified value
	 * exists for the specified term.
	 */
	boolean valueExists(String term, Object value) throws IOException;

	/**
	 * Returns all defined terms. If the term information
	 * locations are unavailable for some reason, the
	 * returned array will be empty, but a non-null value
	 * is guaranteed.
	 */
	String[] getTerms() throws IOException;

}
