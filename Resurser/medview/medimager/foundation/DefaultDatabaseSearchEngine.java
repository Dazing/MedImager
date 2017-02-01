package medview.medimager.foundation;

import java.awt.image.*;

import java.io.*;

import java.util.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import misc.foundation.*;
import misc.foundation.text.*;

/**
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
public class DefaultDatabaseSearchEngine implements DatabaseSearchEngine, MedImagerConstants
{
	private int maximumHits;

	public DefaultDatabaseSearchEngine()
	{
		maximumHits = MedViewDataHandler.instance().getUserIntPreference(MAXIMUM_SEARCH_HITS_PROPERTY,

			100, MedImagerConstants.class);
	}

	/**
	 * Performs a search against the currently set database location,
	 * and returns an array of result objects.
	 * @param searchText String
	 * @return DatabaseImageSearchResult[]
	 */
	public DatabaseImageSearchResult[] search(String searchText, ProgressNotifiable not)
		throws CouldNotParseException, CouldNotSearchException
	{
		ExaminationDataSearchResult[] searchResults = MedViewDataHandler.instance().

			searchExaminationData(searchText, not); // -> CouldNotParseException, CouldNotSearchException

		Vector resultVector = new Vector();

		outerLoop:
		for (int ctr = 0; ctr < searchResults.length; ctr++)
		{
			final ExaminationDataSearchResult currentResult = searchResults[ctr];

			try
			{
				ExaminationImage[] examinationImages = currentResult.getExaminationImages(); // -> IOException

				for (int ctr2 = 0; ctr2 < examinationImages.length; ctr2++)
				{
					final ExaminationImage currentImage = examinationImages[ctr2];

					if (resultVector.size() == maximumHits)
					{
						break outerLoop;
					}
					else
					{
						resultVector.add(new DatabaseImageSearchResult()
						{
							private BufferedImage cachedThumbImage;

							public PatientIdentifier getPID()
							{
								return currentResult.getExaminationIdentifier().getPID();
							}

							public ExaminationIdentifier getEID()
							{
								return currentResult.getExaminationIdentifier();
							}

							public BufferedImage getFullImage() throws IOException
							{
								return currentImage.getFullImage();
							}

							public BufferedImage getThumbImage() throws IOException
							{
								if (cachedThumbImage == null)
								{
									cachedThumbImage = currentImage.getThumbnail(); // -> IOException
								}

								return cachedThumbImage;
							}

							public String getImageName()
							{
								return currentImage.getName();
							}
						});
					}
				}
			}
			catch (IOException exc)
			{
				ExaminationIdentifier eid = currentResult.getExaminationIdentifier();

				System.err.println("WARNING: DefaultDatabaseSearchEngine: could not read images for '" + eid);
			}
		}

		DatabaseImageSearchResult[] results = new DatabaseImageSearchResult[resultVector.size()];

		resultVector.toArray(results);

		return results;
	}

	/**
	 * Returns the maximum number of hits (search results) to be
	 * returned from a search.
	 * @return int
	 */
	public int getMaximumHits()
	{
		return this.maximumHits;
	}

	/**
	 * Sets the maximum number of hits (search results) to be
	 * returned from a search.
	 * @param maximumHits int
	 */
	public void setMaximumHits(int maximumHits)
	{
		if ((maximumHits >= 100) && (maximumHits <= 1000))
		{
			MedViewDataHandler.instance().setUserIntPreference(MAXIMUM_SEARCH_HITS_PROPERTY,

				maximumHits, MedImagerConstants.class);

			this.maximumHits = maximumHits;
		}
		else
		{
			System.err.println("Maximum hits must be >= 100 and <= 1000");
		}
	}

	// UNIT TEST METHOD

	public static void main(String[] args)
	{
		try
		{
			MedViewDataHandler.instance().setExaminationDataLocation("c:\\databasen.mvd");

			DefaultDatabaseSearchEngine searchEngine = new DefaultDatabaseSearchEngine();

			DatabaseImageSearchResult[] results = searchEngine.search("lichen", new SystemOutputProgressNotifiable());

			System.out.println(results.length);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
