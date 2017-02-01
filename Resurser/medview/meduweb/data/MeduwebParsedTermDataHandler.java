/*
 * @(#)MeduwebParsedTermDataHandler.java
 *
 *
 * --------------------------------
 * Original author: Fredrik Lindahl, Meduweb mods by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;
import java.io.*;

import misc.domain.*;
import misc.foundation.io.*;
import medview.datahandling.*;

/**
 *	Instances of this class implements the TermDataHandler
 *	interface by reading and writing to two files, one of
 *	which defines the terms and the other which defines
 *	the values for each term.
 *
 *	@author Fredrik Lindahl
 */
public class MeduwebParsedTermDataHandler implements TermDataHandler
{

	/**
	 * Checks if a term is existant in the term definition location. Note
	 * that a term can be existant without being in the value location,
	 * but a term can never be in the value location without being in the
	 * definition location. Note also that the term is deemed existant,
	 * even if it has another case than the one specified. Thus, say there
	 * exists a term "Born", then 'termExists("born")' will return true.
	 */
	public boolean termExists(String term) throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		return (getCorrespondingMapKey(term, readTermDefinitionHashMap()) != null);
	}


	/**
	 * Adds the specified term, exactly as it is given and with its specified
	 * type, to the term definition location. Note that this causes a term to
	 * be defined in the definition location without existing at all in the
	 * value location, but this is perfectly legal. Note also that the term will
	 * first be removed if it exists already (even if it exists with another
	 * case combination). The type will first be parsed for its integer version
	 * to check that it is valid.
	 */
	public void addTerm(String term, String type) throws
		ValueLocationNotFoundException, DefinitionLocationNotFoundException,
		CouldNotParseException, InvalidTypeException
	{
		this.addTerm(term, parseType(type));
	}


	/**
	 * Adds the specified term, exactly as it is given and with its specified
	 * type, to the term definition location. Note that this causes a term to
	 * be defined in the definition location without existing at all in the
	 * value location, but this is perfectly legal. Note also that the term will
	 * first be removed if it exists already (even if it exists with another
	 * case combination).
	 */
	public void addTerm(String term, int type) throws
		ValueLocationNotFoundException, DefinitionLocationNotFoundException,
		CouldNotParseException, InvalidTypeException
	{
		if (!isValidTermType(type)) { throw new InvalidTypeException(type + ""); }

		if (termExists(term)) { removeTerm(term); }

		HashMap termDefMap = readTermDefinitionHashMap();

		termDefMap.put(term, convertType(type));

		writeTermDefinitionHashMap(termDefMap);
	}


	/**
	 * Will first remove the specified term from the value location (if
	 * existant in it), followed by removing the term from the definition
	 * location. If the term is not existant at all an exception will be
	 * thrown. Note that a term existing in the value location but not in
	 * the definition location is invalid and may cause erroneous behavior.
	 */
	public void removeTerm(String term) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		ValueLocationNotFoundException, InvalidTypeException
	{
		Hashtable termValTable = readValueHashVectorTable();

		String valKey = getCorrespondingTableKey(term, termValTable);

		if (valKey != null) // <- term existant in value location...
		{
			termValTable.remove(valKey);

			writeValueHashVectorsToFile(termValTable);
		}

		HashMap termDefMap = readTermDefinitionHashMap();

		String defKey = getCorrespondingMapKey(term, termDefMap);

		if (defKey != null) // <- term existant in definition location...
		{
			termDefMap.remove(defKey);

			writeTermDefinitionHashMap(termDefMap);
		}
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
		try
		{
			convertType(type);

			return true;
		}
		catch (InvalidTypeException e)
		{
			return false;
		}
	}


	/**
	 * Returns whether or not the currently set
	 * (or not set) term definition location is
	 * a valid one.
	 */
	public boolean isTermDefinitionLocationValid()
	{
		String s = defFileLocation;

		if (s == null) { return false; }

		File f = new File(s);

		return (f.exists());
	}

	/**
	 * Returns whether or not the currently set
	 * (or not set) value definition location is
	 * a valid one.
	 */
	public boolean isTermValueLocationValid()
	{
		String vFLP = VALUE_FILE_LOCATION_PROPERTY;
		String s = valFileLocation;

		if (s == null) { return false; }

		File f = new File(s);

		return (f.exists());
	}


	/**
	 * Returns the values for the specified term. Note:
	 * the term is located case-insensitively, i.e. if
	 * a term exists in the value location called "Born",
	 * the calls 'getValues("Born") and getValues("born")'
	 * return the same array of strings. If the term is
	 * not in the definition location an exception will
	 * be thrown. An empty string array is returned if
	 * the term is defined but not existant in the value
	 * location.
	 */
	public String[] getValues(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		ValueLocationNotFoundException, CouldNotParseException
	{
		if (!termExists(term)) // <- term not in definition location -> can't be in value...
		{
			throw new NoSuchTermException(term);
		}

		Hashtable termValTable = readValueHashVectorTable();

		String key = getCorrespondingTableKey(term, termValTable);

		if (key == null) { return new String[0]; } // <- term in definition location but not in value...

		Vector valVect = (Vector) termValTable.get(key); // guaranteed non-null...

		String[] retArray = new String[valVect.size()];

		valVect.toArray(retArray);

		return retArray;
	}


	/**
	 * Adds the specified value to the specified term. Note
	 * that the vector of values for the specified term is
	 * located by a case-insensitive search for a key equal
	 * to the term in meaning but not necessarily in case.
	 * Note also that if a value already exists (even though
	 * in another case-combination), it will be removed prior
	 * to being re-added in the case as specified.
	 */
	public void addValue(String term, Object value) throws
		NoSuchTermException, ValueLocationNotFoundException,
		DefinitionLocationNotFoundException, CouldNotParseException,
		InvalidTypeException
	{
		Hashtable termValTable = readValueHashVectorTable();

		String key = getCorrespondingTableKey(term, termValTable);

		if (key == null)
		{
			if (!termExists(term))
			{
				throw new NoSuchTermException("No such term (" + term + ")");
			}
			else
			{
				Vector valVect = new Vector();

				valVect.add(value);

				termValTable.put(term, valVect);
			}
		}
		else
		{
			Vector valVect = (Vector) termValTable.get(key); // guaranteed non-null...

			String vectVal = getCorrespondingVectorValue(value, valVect);

			if (vectVal != null) { valVect.remove(vectVal); }

			valVect.add(value);
		}

		writeValueHashVectorsToFile(termValTable);
	}


	/**
	 * Removes the specified value from the specified term.
	 * Note that both the term and value will be located in
	 * a case-insensitive manner, thus if the term "Born" has
	 * a value "Sweden", the call 'removeValue("born", "sweden")'
	 * will work as expected (i.e. "Sweden" will be removed from
	 * "Born"). Furthermore, if a value removal results in the
	 * term having no values, the term is removed from the value
	 * location. Nothing is done if the term exists but has no
	 * values, or if the term has values but not the specified one.
	 */
	public void removeValue(String term, Object value) throws
		NoSuchTermException, ValueLocationNotFoundException,
		DefinitionLocationNotFoundException, CouldNotParseException,
		InvalidTypeException
	{
		if (!termExists(term))
		{
			throw new NoSuchTermException("No such term (" + term + ")");
		}

		Hashtable termValTable = readValueHashVectorTable();

		String key = getCorrespondingTableKey(term, termValTable);

		if (key == null) { return; } // term in definition but not in value...

		Vector valVect = (Vector) termValTable.get(key); // guaranteed non-null...

		String vectVal = getCorrespondingVectorValue(value, valVect);

		if (vectVal != null) // if null -> value not in vector...
		{
			valVect.remove(vectVal);

			if (valVect.size() == 0) { termValTable.remove(key); }

			writeValueHashVectorsToFile(termValTable);
		}
	}


	/**
	 * Returns the type of the specified type. The term
	 * can be specified in whatever case, the search for
	 * the term and its type is done case-independently.
	 * An exception is thrown if the term has a type that
	 * is not recognized.
	 */
	public int getType(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		InvalidTypeException, CouldNotParseException
	{
		HashMap termDefMap = readTermDefinitionHashMap();

		if (!termExists(term, termDefMap)) // <- utility method, avoids two read-ins...
		{
			throw new NoSuchTermException("No such term (" + term + ")");
		}

		String key = getCorrespondingMapKey(term, termDefMap);

		String typeName = (String) termDefMap.get(key);

		if (typeName.equalsIgnoreCase(TermDataHandler.REGULAR_STRING))
		{
			return TermDataHandler.REGULAR_TYPE;
		}
		else if (typeName.equalsIgnoreCase(TermDataHandler.MULTIPLE_STRING))
		{
			return TermDataHandler.MULTIPLE_TYPE;
		}
		else if (typeName.equalsIgnoreCase(TermDataHandler.INTERVAL_STRING))
		{
			return TermDataHandler.INTERVAL_TYPE;
		}
		else if (typeName.equalsIgnoreCase(TermDataHandler.FREE_STRING))
		{
			return TermDataHandler.FREE_TYPE;
		}
		else
		{
			String err = "Unknown type for term '" + term + "': " + typeName;

			throw new InvalidTypeException(err);
		}
	}


	/**
	 * Returns the type description for the specified
	 * term. The type description is always one of the
	 * type descriptors located in the TermDataHandler
	 * interface. An exception is thrown if the term
	 * has a type that is not recognized.
	 */
	public String getTypeDescription(String term) throws
		NoSuchTermException, DefinitionLocationNotFoundException,
		InvalidTypeException, CouldNotParseException
	{
		int type = getType(term);

		return getTypeDescription(type);
	}


	/**
	 * Returns the string representation of the type
	 * specified by the numeric argument. The string
	 * representation is always one of the type
	 * descriptors located in the TermDataHandler
	 * interface. An exception is thrown if the term
	 * has a type that is not recognized.
	 */
	public String getTypeDescription(int type)
	{
		switch(type)
		{
			case TermDataHandler.FREE_TYPE:
			{
				return TermDataHandler.FREE_STRING;
			}
			case TermDataHandler.REGULAR_TYPE:
			{
				return TermDataHandler.REGULAR_STRING;
			}
			case TermDataHandler.MULTIPLE_TYPE:
			{
				return TermDataHandler.MULTIPLE_STRING;
			}
			case TermDataHandler.INTERVAL_TYPE:
			{
				return TermDataHandler.INTERVAL_STRING;
			}
		}

		System.err.println("WARNING: PTDH getTypeDescription()");

		return null;
	}


	/**
	 * Returns an array of the terms present in the term
	 * definition location, represented exactly as they
	 * appear in the location, case-sensitive.
	 */
	public String[] getTerms() throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		HashMap termDefMap = readTermDefinitionHashMap();

		String[] retArray = new String[termDefMap.size()];

		termDefMap.keySet().toArray(retArray);

		return retArray;
	}


	/**
	 * Removes all previous terms from the definition and value locations
	 * and sets these - case sensitively - to the specified terms, with the
	 * specified types and values.
	 */
	public void setTerms(String[] terms, String[] types, Object[][] values) throws
		DefinitionLocationNotFoundException, CouldNotParseException,
		ValueLocationNotFoundException, InvalidTypeException
	{
		Vector vect = null;

		HashMap termDefMap = new HashMap(terms.length);

		Hashtable termValTable = new Hashtable(terms.length);

		for (int ctr1=0; ctr1<terms.length; ctr1++)
		{
			termDefMap.put(terms[ctr1], types[ctr1]);

			if (values[ctr1].length != 0)
			{
				vect = new Vector();

				for (int ctr2=0; ctr2<values[ctr1].length; ctr2++)
				{
					vect.add(values[ctr1][ctr2]);
				}

				termValTable.put(terms[ctr1], vect);
			}
		}

		writeTermDefinitionHashMap(termDefMap);

		writeValueHashVectorsToFile(termValTable);
	}


	/**
	 * Sets the location of the term definitions.
	 */
	public void setTermDefinitionLocation(String loc)
	{
		defFileLocation = loc;
	}


	/**
	 * Sets the location of the term values.
	 */
	public void setTermValueLocation(String loc)
	{
		valFileLocation = loc;
	}


	/**
	 * Returns the location of the term definitions.
	 */
	public String getTermDefinitionLocation()
	{

		String s = defFileLocation;

		if (s == null) { return null; }

		File loc = new File(s);

		return (loc.exists()) ? s : null;
	}


	/**
	 * Returns the location of the term values.
	 */
	public String getTermValueLocation()
	{

		String s = valFileLocation;

		if (s == null) { return null; }

		File loc = new File(s);

		return (loc.exists()) ? s : null;
	}





// ----------------------------------------------------------------------------------
// ******************************** UTILITY METHODS *********************************
// ----------------------------------------------------------------------------------

	/**
	 * Returns if the specified term exists in the
	 * specified map, independent of case. Thus, if
	 * the mapping "Born" -> "Sweden" exists in the map,
	 * and the call 'termExists("born")' is called, it
	 * will return the value 'true'.
	 */
	private boolean termExists(String term, HashMap map)
	{
		return (getCorrespondingMapKey(term, map) != null);
	}


	/**
	 * Returns the key as it is used in the specified map, located
	 * in a case-insensitive manner. If the key is not in the map
	 * (regardless of the combination of case) a null value will be
	 * returned. For instance, if the map a = ["Born" -> ["Sweden",
	 * ,"Iraq","Finland"], "Animal" -> ["Rabbit","Hawk"]], the method
	 * call 'getCorrespondingMapKey("born", a)' will return "Born".
	 */
	private String getCorrespondingMapKey(String key, HashMap map)
	{
		Object[] inMap = map.keySet().toArray();

		for (int ctr=0; ctr<inMap.length; ctr++)
		{
			if (((String)inMap[ctr]).equalsIgnoreCase(key))
			{
				return (String) inMap[ctr];
			}
		}

		return null;
	}


	/**
	 * Returns the key as it is used in the specified table, located
	 * in a case-insensitive manner. If the key is not in the table
	 * (regardless of the combination of case) a null value will be
	 * returned. For instance, if the table a = ["Born" -> ["Sweden",
	 * ,"Iraq","Finland"], "Animal" -> ["Rabbit","Hawk"]], the method
	 * call 'getCorrespondingTableKey("born", a)' will return "Born".
	 */
	private String getCorrespondingTableKey(String key, Hashtable table)
	{
		Object[] inTable = table.keySet().toArray();

		for (int ctr=0; ctr<inTable.length; ctr++)
		{
			if (((String)inTable[ctr]).equalsIgnoreCase(key))
			{
				return (String) inTable[ctr];
			}
		}

		return null;
	}


	/**
	 * Returns the value as it is used in the specified vector, located
	 * in a case-insensitive manner. If the value is not in the vector
	 * (regardless of the combination of case) a null value will be
	 * returned. For instance, if the vector a = ["Born", "Grey", "Rabbit"],
	 * the method call 'getCorrespondingVectorValue("born", a)' will return
	 * "Born".
	 */
	private String getCorrespondingVectorValue(Object value, Vector vector)
	{
		Object[] vectorValues = vector.toArray(); // <- must be a String[] (from file)...

		for (int ctr=0; ctr<vectorValues.length; ctr++)
		{
			if (((String)vectorValues[ctr]).equalsIgnoreCase(value.toString()))
			{
				return (String) vectorValues[ctr];
			}
		}

		return null;
	}


	/**
	 * Converts a term type from string format to its integer
	 * representation. The inverse of the convertType() method.
	 */
	private int parseType(String type) throws InvalidTypeException
	{
		if (type.equalsIgnoreCase(TermDataHandler.FREE_STRING))
		{
			return TermDataHandler.FREE_TYPE;
		}

		if (type.equalsIgnoreCase(TermDataHandler.REGULAR_STRING))
		{
			return TermDataHandler.REGULAR_TYPE;
		}

		if (type.equalsIgnoreCase(TermDataHandler.MULTIPLE_STRING))
		{
			return TermDataHandler.MULTIPLE_TYPE;
		}

		if (type.equalsIgnoreCase(TermDataHandler.INTERVAL_STRING))
		{
			return TermDataHandler.INTERVAL_TYPE;
		}

		throw new InvalidTypeException(type);
	}


	/**
	 * Converts a term type from integer format to its string
	 * representation. The inverse of the parseType() method.
	 */
	private String convertType(int type) throws InvalidTypeException
	{
		if (type == TermDataHandler.FREE_TYPE)
		{
			return TermDataHandler.FREE_STRING;
		}

		if (type == TermDataHandler.REGULAR_TYPE)
		{
			return TermDataHandler.REGULAR_STRING;
		}

		if (type == TermDataHandler.MULTIPLE_TYPE)
		{
			return TermDataHandler.MULTIPLE_STRING;
		}

		if (type == TermDataHandler.INTERVAL_TYPE)
		{
			return TermDataHandler.INTERVAL_STRING;
		}

		throw new InvalidTypeException(type + "");
	}


	/**
	 * Returns a hash table of vectors corresponding to
	 * the value location's terms and their values. The
	 * location is read case-sensitively and the keys are
	 * exactly as they appear in the value location. Note
	 * that if a key is existant in the returned hash table,
	 * it is guaranteed that there is a vector associated
	 * with it. A null value is never mapped to.
	 */
	private Hashtable readValueHashVectorTable() throws
		ValueLocationNotFoundException, CouldNotParseException
	{
		try
		{
			String vFLP = VALUE_FILE_LOCATION_PROPERTY;

			String valLoc = valFileLocation;

			if (valLoc == null) { return new Hashtable(); }

			FileInputStream valFileInputStream = new FileInputStream(valLoc);

			String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

			InputStreamReader iSR = new InputStreamReader(valFileInputStream, enc);

			BufferedReader in = new BufferedReader(iSR);

			Hashtable termValMap = new Hashtable();

			String line = null;

			while ((line = in.readLine()) != null) // while not eof...
			{
				if (line.startsWith(this.TERM_DEFINER)) // term-line...?
				{
					String currentTerm = line.substring(1).trim();

					Vector currentTermVector = new Vector();

					while (((line = in.readLine()) != null) && (line.trim().length() != 0))
					{
						currentTermVector.add(line.trim()); // term values...
					}

					termValMap.put(currentTerm, currentTermVector);
				}
			}

			valFileInputStream.close();

			return termValMap;
		}
		catch (FileNotFoundException e)
		{
			throw new ValueLocationNotFoundException(e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
	}


	/**
	 * Writes the hash table of vectors back to file, in most
	 * cases called after it has been read and modified.
	 */
	private void writeValueHashVectorsToFile(Hashtable table) throws
		ValueLocationNotFoundException, DefinitionLocationNotFoundException,
		InvalidTypeException, CouldNotParseException
	{
		try
		{
			String vFLP = VALUE_FILE_LOCATION_PROPERTY;

			String valLoc = valFileLocation;

			if (valLoc == null) { return; } // nothing is done if location not set...

			FileOutputStream streamOut = new FileOutputStream(valLoc);

			String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

			OutputStreamWriter writer = new OutputStreamWriter(streamOut, enc);

			PrintWriter out = new PrintWriter(writer, true);

			out.println("# MedView term value file - to be used in conjunction with the term definition file");

			out.println("#");

			out.println("# CAUTION: THIS FILE SHOULD NOT BE MODIFIED NON-PROGRAMATICALLY, AS IT MAY LEAD TO");

			out.println("# ERRORS WHILE RUNNING THE PROGRAMS, OR THE PROGRAMS BEING UNABLE TO START");

			out.println("# ---------------------------------------------------------------------------------\n");

			String[] keyArray = new String[table.size()];

			table.keySet().toArray(keyArray);

			Arrays.sort(keyArray, comp);

			Vector currentVector = null;

			for (int ctr1=0; ctr1<keyArray.length; ctr1++)
			{
				out.println(TERM_DEFINER + keyArray[ctr1]);

				currentVector = (Vector) table.get(keyArray[ctr1]);

				String[] valueArray = new String[currentVector.size()];

				int type = -1;

				try
				{
					type = getType(keyArray[ctr1]);
				}
				catch (Exception e) { /* default sorting (type = -1) */ }

				switch (type)
				{
					/* case TermDataHandler.INTERVAL_TYPE:  special case...
					{
						Vector intervalVector = new Vector(currentVector.size());

						Enumeration enm = currentVector.elements();

						while (enm.hasMoreElements())
						{
							String intString = (String) enm.nextElement();

							try
							{
								intervalVector.add(new Interval(intString));
							}
							catch (InvalidIntervalStringException e)
							{
								String mess = "Invalid interval string (" +

											  keyArray[ctr1] + "): " + intString;

								throw new CouldNotParseException(mess);
							}
						}

						Interval[] tA = new Interval[intervalVector.size()];

						intervalVector.toArray(tA);

						Arrays.sort(tA);

						for (int ctr=0; ctr<tA.length; ctr++)
						{
							valueArray[ctr] = tA[ctr].toString();
						}

						break;
					}*/

					default:
					{
						currentVector.toArray(valueArray);

						Arrays.sort(valueArray, comp);

						break;
					}
				}

				for (int ctr2=0; ctr2<valueArray.length; ctr2++)
				{
					out.println(valueArray[ctr2]);
				}

				out.println(""); // new line between terms...
			}

			streamOut.close();
		}
		catch (FileNotFoundException e)
		{
			throw new ValueLocationNotFoundException(e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
	}


	/**
	 * Returns a hash map of strings corresponding to
	 * the definition location's list of terms and their
	 * corresponding textual type representations. The
	 * terms are read exactly as-is, i.e. case-sensitive.
	 */
	private  HashMap readTermDefinitionHashMap() throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		try
		{
			String dFLP = DEFINITION_FILE_LOCATION_PROPERTY;

			String defLoc = defFileLocation;

			if (defLoc == null) { return new HashMap(); }

			FileInputStream fIS = new FileInputStream(defLoc);

			String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

			InputStreamReader iSR = new InputStreamReader(fIS, enc);

			BufferedReader in = new BufferedReader(iSR);

			HashMap termDefMap = new HashMap();

			String line = null;

			while ((line = in.readLine()) != null)
			{
				StringTokenizer tokenizer = new StringTokenizer(line, DEF_DELIMITERS);

				if (tokenizer.countTokens() != 2)
				{
					throw new CouldNotParseException("Invalid line in definition file: '" + line + "'");
				}

				String term = tokenizer.nextToken();
				String type = tokenizer.nextToken();

				termDefMap.put(term, type);
			}

			fIS.close();

			return termDefMap;
		}
		catch (FileNotFoundException e)
		{
			throw new DefinitionLocationNotFoundException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
	}


	/**
	 * Writes the hash map of strings back to file, in most
	 * cases called after it has been read and modified.
	 */
	private void writeTermDefinitionHashMap(HashMap termDefMap) throws
		DefinitionLocationNotFoundException, CouldNotParseException
	{
		try
		{
			String dFLP = DEFINITION_FILE_LOCATION_PROPERTY;

			String defLoc = defFileLocation;
			

			if (defLoc == null) { return; } // nothing is done if location not set...

			FileOutputStream fOS = new FileOutputStream(defLoc);

			String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

			OutputStreamWriter writer = new OutputStreamWriter(fOS, enc);

			PrintWriter out = new PrintWriter(writer, true); // autoflush...

			String[] termArr = new String[termDefMap.size()];

			termDefMap.keySet().toArray(termArr);

			Arrays.sort(termArr, comp); // sort terms...

			String currentTerm = null;

			int preferredTabs = 4;

			int termTabs = -1;

			int tabs = -1;

			for (int ctr=0; ctr<termArr.length; ctr++)
			{
				currentTerm = termArr[ctr];

				termTabs = currentTerm.length() / 8;

				tabs = preferredTabs - termTabs;

				out.print(currentTerm);

				for (int tc=0; tc<tabs; tc++) { out.print("\t"); }

				out.println(termDefMap.get(currentTerm));
			}

			fOS.close();
		}
		catch (FileNotFoundException e)
		{
			throw new DefinitionLocationNotFoundException(e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new CouldNotParseException(e.getMessage());
		}
	}

// ----------------------------------------------------------------------------------
// **********************************************************************************
// ----------------------------------------------------------------------------------








	private void initSimpleMembers()
	{
		comp = new StringNonCaseComparator();

	}

	public MeduwebParsedTermDataHandler()
	{
		initSimpleMembers();
	}

	public MeduwebParsedTermDataHandler(String dlocation, String vlocation) {
		initSimpleMembers();
		defFileLocation = dlocation;
		valFileLocation = vlocation;	
	}

	private Comparator comp;
	private String defFileLocation = null;
	private String valFileLocation = null;
	private static final String TERM_DEFINER = "$";

	private static final String TERM_DELIMITER = "\t";

	private static final String DEF_DELIMITERS = "\t";

	private static final String fileSep = System.getProperty("file.separator");

	private static final String VALUE_FILE_LOCATION_PROPERTY = "parsedTDHValueFileLocation";

	private static final String DEFINITION_FILE_LOCATION_PROPERTY = "parsedTDHDefinitionFileLocation";








	private class StringNonCaseComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			return ((((String)o1).toLowerCase()).compareTo(((String)o2).toLowerCase()));
		}
	}








// ----------------------------------------------------------------------------------
// ******************************** UNIT TEST METHOD ********************************
// ----------------------------------------------------------------------------------

	/* NOTE: Remember to check the term definition and value locations after you have
	 * run the unit test. A proper test should result in the addition of the following
	 * terms and types to the term definition location:
	 *
	 * A regular added term 1		REGULAR
	 * A regular added term 2		REGULAR
	 * A multiple added term 1		MULTIPLE
	 * A multiple added term 2		MULTIPLE
	 * A interval added term 2		INTERVAL
	 * A free added term 1			FREE
	 *
	 * The following values should have been added to the term value location:
	 *
	 * $A multiple added term 1
	 * Value 1
	 * Value 3
	 *
	 * $A multiple added term 2
	 * Value 2
	 * Value 3
	 */

	public static void main(String[] args)
	{
		MeduwebParsedTermDataHandler handler = new MeduwebParsedTermDataHandler();

		System.out.println("TERM TYPE TESTING");
		System.out.println("=================");

		try
		{
			int type1 = handler.getType("Note05");

			int type2 = handler.getType("Adv-drug");

			int type3 = handler.getType("allErgy");

			int type4 = handler.getType("vas-LIFE");;

			System.out.println(type1 + " - " + handler.convertType(type1));

			System.out.println(type2 + " - " + handler.convertType(type2));

			System.out.println(type3 + " - " + handler.convertType(type3));

			System.out.println(type4 + " - " + handler.convertType(type4));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		System.out.println("\nTERM OBTAINAL TESTING");
		System.out.println("=====================");

		try
		{
			String[] terms = handler.getTerms();

			for (int ctr=0; ctr<terms.length; ctr++)
			{
				System.out.println("Term " + (ctr+1) + ": " + terms[ctr]);
			}
		}
		catch (DefinitionLocationNotFoundException e)
		{
			System.out.println("DefinitionLocationNotFoundException thrown: " + e.getMessage());
		}
		catch (CouldNotParseException e)
		{
			System.out.println("CouldNotParseException thrown: " + e.getMessage());
		}

		System.out.println("\nTERM ADDING TESTING");
		System.out.println("===================");

		try
		{
			handler.addTerm("A regular added term 1", TermDataHandler.REGULAR_STRING);

			handler.addTerm("A multiple added term 1", TermDataHandler.MULTIPLE_TYPE);

			handler.addTerm("A interval added term 1", TermDataHandler.INTERVAL_TYPE);

			handler.addTerm("A free added term 1", TermDataHandler.FREE_STRING);

			handler.addTerm("A regular added term 2", TermDataHandler.REGULAR_STRING);

			handler.addTerm("A multiple added term 2", TermDataHandler.MULTIPLE_TYPE);

			handler.addTerm("A interval added term 2", TermDataHandler.INTERVAL_TYPE);

			handler.addTerm("A free added term 2", TermDataHandler.FREE_STRING);
		}
		catch (Exception e) { e.printStackTrace(); }

		System.out.println("\nVALUE ADDING TESTING");
		System.out.println("====================");

		try
		{
			handler.addValue("A multiple added term 1", "Value 1");

			handler.addValue("a multiple added term 1", "Value 2");

			handler.addValue("A multiPle added Term 1", "Value 3");

			handler.addValue("A multiple added term 2", "Value 1");

			handler.addValue("a multiple aDDed term 2", "Value 2");

			handler.addValue("A multiple added tERM 2", "Value 3");
		}
		catch (Exception e) { e.printStackTrace(); }

		System.out.println("\nVALUE REMOVAL TESTING");
		System.out.println("=====================");

		try
		{
			handler.removeValue("A multiple added term 1", "Value 2");

			handler.removeValue("a multiple aDDed term 2", "Value 1");
		}
		catch (Exception e) { e.printStackTrace(); }

		System.out.println("\nVALUE OBTAINAL TESTING");
		System.out.println("======================");

		try
		{
			String[] values = handler.getValues("bOrN");

			for (int ctr=0; ctr<values.length; ctr++)
			{
				System.out.println("Value " + (ctr+1) + ": " + values[ctr]);
			}
		}
		catch (Exception e) { e.printStackTrace(); }

		System.out.println("\nTYPE TESTING");
		System.out.println("============");

		try
		{
			System.out.println("Type (allergy): " + handler.getTypeDescription("ALleRGY"));

			System.out.println("Type (adv-drug): " + handler.getTypeDescription("adv-drug"));

			System.out.println("Type (vas-life): " + handler.getTypeDescription("vas-life"));

			System.out.println("Type (note04): " + handler.getTypeDescription("NOTE04"));
		}
		catch (Exception e) { e.printStackTrace(); }

		System.out.println("\nTERM REMOVAL TESTING");
		System.out.println("====================");

		try
		{
			handler.removeTerm("A interval added term 1");

			handler.removeTerm("A free added term 2");
		}
		catch (Exception e) { e.printStackTrace(); }
	}

// ----------------------------------------------------------------------------------
// **********************************************************************************
// ----------------------------------------------------------------------------------

}
