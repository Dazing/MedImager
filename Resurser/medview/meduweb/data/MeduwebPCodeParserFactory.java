/*
 * @(#)MeduwebPCodeParserFactory.java
 * --------------------------------
 * Original author: Fredrik Lindahl, Rebuilt for Meduweb by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import medview.datahandling.*;

/**
 *
 */
public class MeduwebPCodeParserFactory
{

	public PCodeParser getPCodeParser( )
	{
		String dP = "medview.datahandling.DefaultPCodeParser";

		try
		{
			pCodeParser = (PCodeParser) Class.forName(dP).newInstance();

			return pCodeParser;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			System.exit(1);
		}

		return null;
	}





	public MeduwebPCodeParserFactory() {}

	private PCodeParser pCodeParser;


	private static final String CURRENT_PCP_CLASS_PROPERTY = "currentPCodeParserClass";

}
