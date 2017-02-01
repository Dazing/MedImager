/*
 * $Id: MedViewUtilities.java,v 1.10 2006/09/13 21:44:29 oloft Exp $
 */

package medview.common.data;

import java.io.*;

import java.text.*;

import java.util.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;

import misc.foundation.io.*;

/**
 * A place to put utility methods that might
 * be of use to all medview applications.
 *
 * @author Fredrik Lindahl
 */
public class MedViewUtilities implements MedViewDataConstants,
	MedViewLanguageConstants
{

	// INTERNAL MEMBERS

	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();


	// METHODS


	/**
	 * Extracts a (pid, pcode) pair from the specified
	 * tree file. Guaranteed to return a non-null object,
	 * which might represent one of four possible combinations:
	 * (null, null), (pid, null), (null, pcode), or (pid, pcode).
	 * @param treeFile File
	 * @return PIDPCodePair
	 * @throws IOException
	 */
	public static PIDPCodePair parsePIDPCode(File treeFile) throws IOException
	{
		String pid = null, pCode = null, line = null;

		BufferedReader currBufferedReader = new BufferedReader(new InputStreamReader(

			new FileInputStream(treeFile), IOConstants.ISO_LATIN_1_CHARACTER_ENCODING));

		while ((line = currBufferedReader.readLine()) != null)
		{
			if (line.length() < 1)
			{
				continue;
			}

			if (line.substring(1).trim().equalsIgnoreCase(PID_TERM_NAME))
			{
				line = currBufferedReader.readLine(); // value of pid node

				pid = line.substring(1, line.indexOf(MVD_NODE_ENDER));
			}

			if (line.substring(1).trim().equalsIgnoreCase(PCODE_TERM_NAME))
			{
				line = currBufferedReader.readLine(); // value of pid node

				pCode = line.substring(1, line.indexOf(MVD_NODE_ENDER));
			}

			if ((pid != null) && (pCode != null))
			{
				break;
			}
		}

		return new PIDPCodePair(pid, pCode);
	}

	/**
	 * Extracts a (pid, pcode) pair from the specified
	 * tree file. Guaranteed to return a non-null object,
	 * which might represent one of four possible combinations:
	 * (null, null), (pid, null), (null, pcode), or (pid, pcode).
	 * @param tree Tree
	 * @return PIDPCodePair
	 */
	public static PIDPCodePair parsePIDPCode(Tree tree)
	{
		Tree pidNode = tree.getNode(PID_TERM_NAME);

		String pid = null;

		if (pidNode != null)
		{
			pid = (String)((Tree)pidNode.getChildrenEnumeration().nextElement()).getValue();
		}

		Tree pCodeNode = tree.getNode(PCODE_TERM_NAME);

		String pCode = null;

		if (pCodeNode != null)
		{
			pCode = (String)((Tree)pCodeNode.getChildrenEnumeration().nextElement()).getValue();
		}

		return new PIDPCodePair(pid, pCode);
	}

	/**
	 * Returns whether the specified line is
	 * a node line in a recognized tree file.
	 */
	public static boolean isNode(String nodeLine)
	{
		return (nodeLine.startsWith(MVD_NODE_PREFIX));
	}

	/**
	 * Returns whether the specified line is
	 * a leaf line in a recognized tree file.
	 */
	public static boolean isLeaf(String nodeLine)
	{
		return (nodeLine.startsWith(MVD_LEAF_PREFIX));
	}

	/**
	 * Constructs an ExaminationValueContainer from the specified tree.
	 * @param tree Tree
	 * @return ExaminationValueContainer
	 */
	public static ExaminationValueContainer constructExaminationValueContainer(Tree tree)
	{
		try
		{
			return constructExaminationValueContainer(constructExaminationIdentifier(tree), // -> CouldNotConstructIdentifierException

				new BufferedReader(new StringReader(tree.toString())));
		}
		catch (IOException exc) // should never happen since we already have the tree
		{
			exc.printStackTrace();

			System.exit(1);

			return null;
		}
		catch (CouldNotConstructIdentifierException exc)
		{
			exc.printStackTrace();

			System.exit(1); // fatal error

			return null;
		}
	}

	/**
	 * Constructs an ExaminationValueContainer from a tree-file formatted
	 * reader input.
	 * @param reader BufferedReader
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws InvalidHintException
	 */
	public static ExaminationValueContainer constructExaminationValueContainer(ExaminationIdentifier eid, BufferedReader reader) throws
		IOException
	{
		try
		{
			return constructExaminationValueContainer(eid, reader, OPTIMIZE_FOR_EFFICIENCY);
		}
		catch (InvalidHintException exc) // this will never happen since we specify the hint here
		{
			exc.printStackTrace();

			System.exit(1);

			return null; // never get here
		}
	}

	/**
	 * Constructs an ExaminationValueContainer from a tree-file formatted
	 * reader input. Takes a parameter indicating how the creation should
	 * be optimized (ex. for memory or for efficiency).
	 * @param reader BufferedReader a reader to a tree-file formatted input.
	 * @return ExaminationValueContainer the constructed value container.
	 */
	public static ExaminationValueContainer constructExaminationValueContainer(ExaminationIdentifier eid, BufferedReader reader, int hint) throws
		IOException, InvalidHintException
	{
		// skip initial line

		reader.readLine(); // -> IOException

		// set up variables

		HashMap hashMap = new HashMap();

		String currLine = null;

		// start parsing

		while ((currLine = reader.readLine()) != null) // -> IOException
		{
			parseLine(currLine, hashMap, reader); // -> IOException
		}

		/* NOTE: the map now contains mappings String -> Vector, where the
		   string is a node's name (without the prefix and ender) which
		   points to a vector of the nodes values. Nodes which did not have
		   any values will not exist as keys in the hashmap anymore, the
		   only nodes in the hashmap are the ones containing values. */

		reader.close();

		// depending on optimization hint, return the examination value container

		if (hint == OPTIMIZE_FOR_EFFICIENCY)
		{
			return new EfficientExaminationValueContainer(hashMap, eid);
		}
		else if (hint == OPTIMIZE_FOR_MEMORY)
		{
			return new MemoryExaminationValueContainer(hashMap, eid);
		}

		throw new InvalidHintException("Hint '" + hint + "' not recognized by utility class!");
	}

	/**
	 * Recursive parse method that adds nodes and
	 * their corresponding leaf children to a
	 * hash map. The hash map is indexed by the node
	 * string values and each entry referes to a vector
	 * containing the child string values. To deal
	 * with multi-line leaves, a reader must be
	 * specified as well.
	 */
	public static int parseLine(String line, HashMap map, BufferedReader reader) throws IOException
	{
		int terminates = extractTerminates(line); // nr of terminating # chars

		if (MedViewUtilities.isNode(line)) // if a line is a node, create vector of children and add to map
		{
			String value = extractNodeValue(line, reader);

			if (terminates == 0) // children exists
			{
				String currLine;

				Vector childVect = new Vector();

				while (terminates == 0 && reader.ready()) // ready() added by NE 041112 since this loop could lock up and stop on a nullpointer (thrown by readLine) if tree files could not be parsed properly
				{
					currLine = reader.readLine();

					childVect.add(extractNodeValue(currLine, reader));

					terminates = parseLine(currLine, map, reader);
				}

				map.put(value, childVect);
			}
			else
			{
				// don't do anything (we don't want key -> null) // Fredrik 041117
			}
		}

		return--terminates;
	}

	/**
	 * Takes a line in a tree file and returns the node
	 * value of the line. For instance, a line 'NPatient#'
	 * returns 'Patient'. In order to deal with multi-line
	 * leaves, a reader must also be supplied.
	 */
	public static String extractNodeValue(String nodeLine, BufferedReader reader)
	{
		try
		{
			int startIndex = -1;

			int endIndex = -1;

			if (MedViewUtilities.isNode(nodeLine))
			{
				startIndex = MVD_NODE_PREFIX.length();
			}

			if (MedViewUtilities.isLeaf(nodeLine))
			{
				startIndex = MVD_LEAF_PREFIX.length();
			}

			if (startIndex == -1)
			{
				return "ERROR_PARSE"; // not a valid node start -> -1
			}

			StringBuffer buffy = new StringBuffer();

			if (nodeLine.endsWith(MVD_NODE_ENDER))
			{
				endIndex = nodeLine.indexOf(MVD_NODE_ENDER); // first occurence of

				buffy.append(nodeLine.substring(startIndex, endIndex));
			}
			else
			{
				buffy.append(nodeLine.substring(startIndex));

				if (isLeaf(nodeLine)) // multi-line leaf 030909 Fredrik
				{
					String nextLine = reader.readLine();

					while (!nextLine.endsWith(MVD_NODE_ENDER))
					{
						buffy.append("\n" + nextLine);

						nextLine = reader.readLine();
					}

					endIndex = nextLine.indexOf(MVD_NODE_ENDER); // first occurence of

					buffy.append("\n" + nextLine.substring(0, endIndex));
				}
			}

			return buffy.toString().intern(); // memory fix (!)
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return "ERROR_PARSE";
		}
	}

	/**
	 * Returns the number of terminates found in
	 * the specified node line. A terminate is, for
	 * instance, the "#" character used in the tree
	 * file format.
	 */
	public static int extractTerminates(String nodeLine)
	{
		int firstEndIndex = nodeLine.indexOf(MVD_NODE_ENDER);

		if (firstEndIndex == -1)
		{
			return 0; // there were no enders in the line
		}

		int lastEndIndex = nodeLine.lastIndexOf(MVD_NODE_ENDER);

		return nodeLine.substring(firstEndIndex, lastEndIndex).length() + 1;
	}

	/**
	 * Returns whether or not the specified path
	 * ends with the mvd extension according to our
	 * standard.
	 */
	public static boolean isPathMVD(String path)
	{
		return (path.endsWith(MVD_DIRECTORY_SUFFIX) ||

			path.endsWith(MVD_DIRECTORY_SUFFIX + File.separator));
	}

	/**
	 * Constructs an ExaminationIdentifier from a Tree object.
	 * @param tree Tree the tree out of which you want to obtain
	 * an ExaminationIdentifier object.
	 * @return ExaminationIdentifier the resulting examination
	 * identifier based on the data in the tree.
	 * @throws CouldNotConstructIdentifierException if, for some
	 * reason, the identifier could not be constructed.
	 */
	public static ExaminationIdentifier constructExaminationIdentifier(Tree tree) throws
		CouldNotConstructIdentifierException
	{
		// obtain date

		String[] vals = tree.getValuesOfNodesNamed(MedViewDataConstants.DATE_TERM_NAME);

		if ((vals == null) || (vals.length == 0))
		{
			throw new CouldNotConstructIdentifierException("There is no date node in the tree!");
		}

		Date date = null;

		try
		{
			date = new DefaultTreeFileDateParser().extractDate(vals[0]);
		}
		catch (CouldNotParseDateException e)
		{
			throw new CouldNotConstructIdentifierException("Could not parse the tree date node!");
		}

		// construct patient identifier

		vals = tree.getValuesOfNodesNamed(MedViewDataConstants.PCODE_TERM_NAME);

		if ((vals == null) || (vals.length == 0))
		{
			throw new CouldNotConstructIdentifierException("There is no pcode node in the tree!");
		}

		String pCodeValue = vals[0];

		String pidValue = null;

		vals = tree.getValuesOfNodesNamed(MedViewDataConstants.PID_TERM_NAME);

		if ((vals != null) && (vals.length != 0))
		{
			pidValue = vals[0];
		}

		PatientIdentifier pid = new PatientIdentifier(pCodeValue, pidValue);

		return new MedViewExaminationIdentifier(pid, date);
	}

	public static Date constructExaminationDate(Tree tree) throws CouldNotConstructDateException 	{
		// obtain date
		
		String[] vals = tree.getValuesOfNodesNamed(MedViewDataConstants.DATE_TERM_NAME);
		
		if ((vals == null) || (vals.length == 0))
		{
			throw new CouldNotConstructDateException("There is no date node in the tree!");
		}
		
		Date date = null;
		
		try
		{
			date = new DefaultTreeFileDateParser().extractDate(vals[0]);
		}
		catch (CouldNotParseDateException e)
		{
			throw new CouldNotConstructDateException("Could not parse the tree date node!");
		}
				
		return date;
	}
	
	/**
	 * Reads in a file in a recognized tree file
	 * format and constructs a Tree object representing
	 * that tree.
	 */
	public static Tree loadTree(File treeFile) throws IOException
	{
		String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

		FileInputStream fileInputStream = new FileInputStream(treeFile);

		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, enc);

		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String currLine = bufferedReader.readLine(); // first line special

		Tree rootNode = new TreeBranch(currLine);

		Tree parent = rootNode; // root node is initial parent

		while ((currLine = bufferedReader.readLine()) != null)
		{
			if (isNode(currLine))
			{
				Tree child = new TreeBranch(extractValue(currLine, bufferedReader));

				parent.addChild(child);

				parent = child;

				int terminates = extractTerminates(currLine);

				while (terminates > 0)
				{
					if (parent != rootNode)
					{
						parent = parent.getParent();
					}

					terminates--;
				}
			}
			if (isLeaf(currLine)) // the reader is sent along because of multiline leaf possibility
			{
				Tree child = new TreeLeaf(extractValue(currLine, bufferedReader));

				parent.addChild(child);

				int terminates = extractTerminates(currLine);

				while (terminates > 1)
				{
					parent = parent.getParent();

					terminates--;
				}
			}
		}

		fileInputStream.close();

		return rootNode;
	}

	/**
	 * Saves a Tree root to the specified file.
	 * @param root Tree
	 * @param file File
	 * @throws IOException
	 */
	public static void saveTree(Tree root, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);

		OutputStreamWriter osw = new OutputStreamWriter(fos, IOConstants.ISO_LATIN_1_CHARACTER_ENCODING);

		osw.write(root.toString()); // Replaces old writeNode functionality. // NE 03-0907

		osw.flush();

		osw.close();
	}

	/**
	 * Obtains the specified date in a format used in the concrete
	 * identification node in the Tree structure, and also used
	 * in most tree file names following the pcode.
	 * @param date Date
	 * @return String
	 */
	public static String obtainConcreteIdentificationDateString(Date date)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

		return dateFormat.format(date);
	}


	// PRIVATE UTILITY METHODS USED BY THE METHODS ABOVE

	/**
	 * Extracts the node value from the specified
	 * tree file formatted line. Takes a buffered
	 * reader in case the value is a multi-line
	 * value and further lines need to be read.
	 */
	private static String extractValue(String nodeLine, BufferedReader reader) // reader for multiline leaf
	{
		try
		{
			if (!((nodeLine.startsWith("N") || nodeLine.startsWith("L"))))
			{
				System.out.println("WARNING: invalid line read from tree file '" + nodeLine + "'");
			}

			StringBuffer buffy = new StringBuffer();

			if (nodeLine.endsWith("#"))
			{
				buffy.append(nodeLine.substring(1, nodeLine.indexOf("#")));
			}
			else
			{
				buffy.append(nodeLine.substring(1));

				if (isLeaf(nodeLine)) // multi-line leaf 030909 Fredrik
				{
					String nextLine = reader.readLine();

					while (!nextLine.endsWith("#"))
					{
						buffy.append("\n" + nextLine);

						nextLine = reader.readLine();
					}

					buffy.append("\n" + nextLine.substring(0, nextLine.indexOf("#")));
				}
			}

			return buffy.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return "ERROR_DURING_PARSE";
		}
	}

	/**
	 * ExaminationValueContainer implementation that tries to
	 * optimize look-up time in a container.
	 *
	 * @author Fredrik Lindahl
	 * @version 1.0
	 */
	private static class EfficientExaminationValueContainer implements
		Serializable, ExaminationValueContainer
	{
		private HashMap hashMap;

		private ExaminationIdentifier eid;

		public EfficientExaminationValueContainer(HashMap map, ExaminationIdentifier eid)
		{
			this.hashMap = map;

			this.eid = eid;
		}

		public String[] getValues(String term) throws NoSuchTermException
		{
			if (hashMap.containsKey(term))
			{
				String[] retArr = new String[((Vector)hashMap.get(term)).size()];

				((Vector)hashMap.get(term)).toArray(retArr);

				return retArr;
			}
			else
			{
				throw new NoSuchTermException(term);
			}
		}

		public void addValue(String term, String value)
		{
			// By Nils 050126

			Vector v = (Vector)hashMap.get(term);

			if (v == null)
			{
				v = new Vector();

				hashMap.put(term, v);
			}

			v.add(term);
		}

		public boolean termHasValues(String term)
		{
			return hashMap.containsKey(term);
		}

		public String[] getTermsWithValues()
		{
			String[] retArr = new String[hashMap.size()];

			hashMap.keySet().toArray(retArr);

			return retArr;
		}

		public void internalize()
		{
			HashMap tempMap = new HashMap(hashMap.size());

			String[] termsWithValues = getTermsWithValues();

			try
			{
				for (int ctr = 0; ctr < termsWithValues.length; ctr++)
				{
					String[] values = getValues(termsWithValues[ctr]); // -> NoSuchTermException

					Vector vect = new Vector(values.length);

					for (int ctr2 = 0; ctr2 < values.length; ctr2++)
					{
						vect.add(values[ctr2].intern()); // internalize
					}

					tempMap.put(termsWithValues[ctr].intern(), vect); // internalize
				}

				hashMap = tempMap; // switch the map to the internalized one
			}
			catch (NoSuchTermException exc)
			{
				exc.printStackTrace(); // this should never happen, but print stack trace if it does (internalizing fails)
			}
		}

		public ExaminationIdentifier getExaminationIdentifier()
		{
			return eid;
		}
	}

	/**
	 * ExaminationValueContainer implementation that tries to
	 * minimize the memory footprint taken up by the container.
	 *
	 * @author Fredrik Lindahl
	 * @version 1.0
	 */
	private static class MemoryExaminationValueContainer implements
		Serializable, ExaminationValueContainer
	{
		private Entry[] entries;

		private ExaminationIdentifier eid;

		public MemoryExaminationValueContainer(HashMap map, ExaminationIdentifier eid)
		{
			entries = new Entry[map.keySet().size()];

			Iterator iter = map.keySet().iterator();

			int entryCounter = 0;

			while (iter.hasNext())
			{
				Entry entry = new Entry();

				entry.node = (String)iter.next();

				entry.values = new String[((Vector)map.get(entry.node)).size()];

				((Vector)map.get(entry.node)).toArray(entry.values);

				entries[entryCounter++] = entry;
			}

			this.eid = eid;
		}

		public String[] getValues(String term) throws NoSuchTermException
		{
			for (int ctr = 0; ctr < entries.length; ctr++)
			{
				if (entries[ctr].node.equals(term))
				{
					return entries[ctr].values;
				}
			}

			throw new NoSuchTermException(term);
		}

		public void addValue(String term, String value)
		{
			// By Nils 050126
			// Find the vector

			Entry theEntry = null;

			search:
				for (int i = 0; i < entries.length; i++)
			{
				if (entries[i].node.equals(term))
				{
					theEntry = entries[i];

					break search;
				}
			}

			if (theEntry == null)
			{
				theEntry = new Entry();

				theEntry.node = term;

				theEntry.values = new String[0];

				Entry[] oldentries = entries;

				entries = new Entry[oldentries.length + 1];

				for (int i = 0; i < oldentries.length; i++)
				{
					entries[i] = oldentries[i];
				}

				entries[oldentries.length] = theEntry;
			}

			String[] oldvalues = theEntry.values;

			theEntry.values = new String[oldvalues.length + 1];

			for (int i = 0; i < oldvalues.length; i++)
			{
				theEntry.values[i] = oldvalues[i];
			}

			theEntry.values[oldvalues.length] = value;

		}

		public boolean termHasValues(String term)
		{
			for (int ctr = 0; ctr < entries.length; ctr++)
			{
				if (entries[ctr].node.equalsIgnoreCase(term))
				{
					return true;
				}
			}

			return false;
		}

		public String[] getTermsWithValues()
		{
			String[] retArr = new String[entries.length];

			for (int ctr = 0; ctr < entries.length; ctr++)
			{
				retArr[ctr] = entries[ctr].node;
			}

			return retArr;
		}

		private class Entry implements Serializable
		{
			public String node = null;

			public String[] values = null;
		}

		public void internalize()
		{
			for (int ctr=0; ctr<entries.length; ctr++)
			{
				String[] values = entries[ctr].values;

				String[] newValues = new String[values.length];

				for (int ctr2=0; ctr2<values.length; ctr2++)
				{
					newValues[ctr2] = values[ctr2].intern(); // internalize
				}

				entries[ctr].values = newValues; // switch entries values to internalized ones

				entries[ctr].node = entries[ctr].node.intern(); // internalize and switch
			}
		}

		public ExaminationIdentifier getExaminationIdentifier()
		{
			return eid;
		}
	}


	// NESTED CLASSES

	/**
	 * A simple (pid, pcode) class.
	 * <p>Copyright: Copyright (c) 2004</p>
	 *
	 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
	 *
	 * @author Fredrik Lindahl
	 * @version 1.0
	 */
	public static class PIDPCodePair
	{
		public String getPID()
		{
			return pid;
		}

		public String getPCode()
		{
			return pcode;
		}

		public PIDPCodePair(String pid, String pcode)
		{
			this.pid = pid;

			this.pcode = pcode;
		}

		private String pid;

		private String pcode;
	}
}
