package medview.datahandling;

import java.io.*;

import java.util.*;

import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import misc.foundation.*;
import misc.foundation.text.*;

/**
 * Simple implementation of a search engine, that loads all examination value
 * containers into memory at first search occasion, and then searches in this
 * data collection subsequently.<br>
 * <br>
 * Some things to consider:<br>
 * <ul>
 * <li>The first search will take a lot of time in this implementation, it is
 * advised that you pass in a progress notifiable when searching</li>
 * <li>If examination data is added after the first search has been performed,
 * the added data will not be included in the search. The implementation might
 * be improved in the future such that notification of added data is supported</li>
 * </ul>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class DefaultExaminationDataSearchEngine implements ExaminationDataSearchEngine
{
	private ExaminationDataSearchResult[] cache;

	public DefaultExaminationDataSearchEngine() {}

	/**
	 * Implementations may use a caching-technique to speed up subsequent
	 * searches. If this method is called, eventual cached information is
	 * to be cleared from the implementation search cache. Typically called
	 * when the location the search is performed on changes.
	 */
	public void clearCache()
	{
		cache = null; // forces reconstruction at next search
	}

	/**
	 * Performs a search in the current examination data location. The
	 * search string is parsed, if for some reason the search engine
	 * implementation cannot parse the search string, an exception is
	 * thrown.
	 * @param searchString String
	 * @param not ProgressNotifiable
	 * @return ExaminationDataSearchResult[]
	 * @throws CouldNotParseException
	 */
	public ExaminationDataSearchResult[] searchExaminationData(String searchString, ProgressNotifiable not)
		throws CouldNotParseException, CouldNotSearchException
	{
		// extract information from search string

		TermValueMapping[] termValueMappings = parseTermValueMappings(searchString); // -> CouldNotParseException

		String[] allValues = parseAllValues(searchString); // -> CouldNotParseException

		// build array of all possible search result objects (full database) - some of these are included in the result later

		if (cache == null)
		{
			try
			{
				cacheAllPossibleSearchResults(not); // -> IOException
			}
			catch (IOException exc)
			{
				throw new CouldNotSearchException(exc);
			}
		}

		// perform actual search

		if (not != null)
		{
			not.setDescription("Searching value containers");

			not.setTotal(cache.length);
		}

		Vector retVect = new Vector();

		ExaminationValueContainer currentEVC;

		main_loop:
		for (int ctr = 0; ctr < cache.length; ctr++)
		{
			if (not != null)
			{
				not.setCurrent(ctr);
			}

			try
			{
				currentEVC = cache[ctr].getExaminationValueContainer(); // -> IOException, NoSuchExaminationException

				term_value_loop:
				for (int ctr2 = 0; ctr2 < termValueMappings.length; ctr2++)
				{
					if (currentEVC.termHasValues(termValueMappings[ctr2].term))
					{
						String[] values = currentEVC.getValues(termValueMappings[ctr2].term); // -> NoSuchTermException

						for (int ctr3 = 0; ctr3 < values.length; ctr3++)
						{
							if (values[ctr3].toLowerCase().indexOf(termValueMappings[ctr2].value.toLowerCase()) != -1)
							{
								continue term_value_loop;
							}
						}

						continue main_loop; // if we get here -> a term-value mapping was non-existant -> do not add result
					}
					else
					{
						continue main_loop; // if we get here -> a required term was non-existant -> do not add result
					}
				}

				String[] allTermsWithValues = currentEVC.getTermsWithValues(); // -> IOException

				all_value_loop:
				for (int ctr2 = 0; ctr2 < allValues.length; ctr2++)
				{
					for (int ctr3 = 0; ctr3 < allTermsWithValues.length; ctr3++)
					{
						String[] values = currentEVC.getValues(allTermsWithValues[ctr3]); // -> NoSuchTermException

						for (int ctr4 = 0; ctr4 < values.length; ctr4++)
						{
							if (values[ctr4].toLowerCase().indexOf(allValues[ctr2].toLowerCase()) != -1)
							{
								continue all_value_loop;
							}
						}
					}

					continue main_loop; // if we get here -> an 'all value' was not found in current search result -> do not add result
				}

				retVect.add(cache[ctr]); // passed both tests -> include search result in return vector
			}
			catch (IOException exc)
			{
				throw new CouldNotSearchException(exc);
			}
			catch (NoSuchExaminationException exc)
			{
				throw new CouldNotSearchException(exc);
			}
			catch (NoSuchTermException exc)
			{
				throw new CouldNotSearchException(exc);
			}
		}

		ExaminationDataSearchResult[] retArr = new ExaminationDataSearchResult[retVect.size()];

		retVect.toArray(retArr); // convert vector to return array

		return retArr;
	}

	private void cacheAllPossibleSearchResults(ProgressNotifiable not) throws IOException
	{
		// use the currently set data handler to obtain data

		ExaminationValueContainer[] evcs = MedViewDataHandler.instance().getAllExaminationValueContainers(not); // -> IOException

		cache = new ExaminationDataSearchResult[evcs.length];

		for (int ctr=0; ctr<evcs.length; ctr++)
		{
			cache[ctr] = new DefaultExaminationDataSearchResult(evcs[ctr]);
		}
	}

	private static TermValueMapping[] parseTermValueMappings(String searchString) throws CouldNotParseException
	{
		Vector retVect = new Vector();

		StringTokenizer tok1 = new StringTokenizer(searchString, "&", false); // false -> do not return token delimiters

		while (tok1.hasMoreTokens())
		{
			String token = tok1.nextToken().trim();

			StringTokenizer tok2 = new StringTokenizer(token, "=", false); // false -> do not return token delimiters

			if (tok2.countTokens() == 1) // an 'all value'
			{
				continue;
			}
			else if (tok2.countTokens() == 2) // term = value
			{
				String term = tok2.nextToken().trim();

				String value = tok2.nextToken().trim();

				retVect.add(new TermValueMapping(term, value));
			}
			else
			{
				throw new CouldNotParseException(token + " contains more than 2 tokens");
			}
		}

		TermValueMapping[] retArr = new TermValueMapping[retVect.size()];

		retVect.toArray(retArr);

		return retArr; // might return empty array
	}

	private static String[] parseAllValues(String searchString) throws CouldNotParseException
	{
		Vector retVect = new Vector();

		StringTokenizer tok1 = new StringTokenizer(searchString, "&", false); // false -> do not return token delimiters

		while (tok1.hasMoreTokens())
		{
			String token = tok1.nextToken().trim();

			StringTokenizer tok2 = new StringTokenizer(token, "=", false); // false -> do not return token delimiters

			if (tok2.countTokens() == 1) // an 'all value'
			{
				retVect.add(tok2.nextToken().trim());
			}
			else if (tok2.countTokens() == 2) // term = value
			{
				continue;
			}
			else
			{
				throw new CouldNotParseException(token + " contains more than 2 tokens");
			}
		}

		String[] retArr = new String[retVect.size()];

		retVect.toArray(retArr);

		return retArr; // might return empty array
	}


	// PRIVATE UTILITY CLASS(ES)

	private static class TermValueMapping
	{
		public TermValueMapping(String term, String value)
		{
			this.term = term;

			this.value = value;
		}

		protected String term, value;
	}


	// UNIT TEST

	/**
	 * Unit test method for testing search functionality.
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			// set up the data handling

			MedViewDataHandler.instance().setExaminationDataLocation("C:\\Databasen.mvd");

			// create the search engine, perform first search, print result evc's

			ExaminationDataSearchEngine engine = new DefaultExaminationDataSearchEngine();

			String searchString = "Care-provider=jontell & foto & Treat-type=informatioN";

			ExaminationDataSearchResult[] results = engine.searchExaminationData(searchString,

				new SystemOutputProgressNotifiable());

			printResults(searchString, results);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

	/**
	 * Utility method for unit test method.
	 * @param searchString String
	 * @param results ExaminationValueContainer[]
	 */
	private static void printResults(String searchString, ExaminationDataSearchResult[] results)
	{
		try
		{
			System.out.println("Total number of results = " + results.length);

			TermValueMapping[] termValueMappings = parseTermValueMappings(searchString);

			String[] allValues = parseAllValues(searchString);

			System.out.println("The term-value mappings parsed from the search string are:");

			for (int ctr=0; ctr<termValueMappings.length; ctr++)
			{
				System.out.println(termValueMappings[ctr].term + "<->" + termValueMappings[ctr].value);
			}

			System.out.println("The all values parsed from the search string are:");

			for (int ctr=0; ctr<allValues.length; ctr++)
			{
				System.out.println(allValues[ctr]);
			}

			for (int ctr=0; ctr<results.length; ctr++)
			{
				ExaminationValueContainer cont = results[ctr].getExaminationValueContainer();

				System.out.print("EVC(" + ctr + "):[");

				for (int ctr2=0; ctr2<termValueMappings.length; ctr2++)
				{
					System.out.print(termValueMappings[ctr2].term + ":[");

					String[] values = cont.getValues(termValueMappings[ctr2].term);

					for (int ctr3=0; ctr3<values.length; ctr3++)
					{
						System.out.print("{" + values[ctr3] + "}");
					}

					System.out.print("]");
				}

				String[] termsWithValues = cont.getTermsWithValues();

				for (int ctr2=0; ctr2<allValues.length; ctr2++)
				{
					for (int ctr3=0; ctr3<termsWithValues.length; ctr3++)
					{
						String[] values = cont.getValues(termsWithValues[ctr3]);

						for (int ctr4=0; ctr4<values.length; ctr4++)
						{
							if (values[ctr4].toLowerCase().indexOf(allValues[ctr2].toLowerCase()) != -1)
							{
								System.out.print("{" + values[ctr4] + "}");
							}
						}
					}
				}

				System.out.println("]");
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
