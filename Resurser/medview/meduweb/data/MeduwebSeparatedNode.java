/*
 * @(#)MeduwebSeparatedNode.java
 *
 * $Id: MeduwebSeparatedNode.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import javax.swing.text.*;
import medview.common.generator.*;
import medview.common.translator.*;

import medview.datahandling.*;

public class MeduwebSeparatedNode extends MeduwebTranslatedNode
{

	public boolean parseNode( ) throws CouldNotParseNodeException
	{
		try
		{      
                        MeduwebSeparatedTranslationModel model = (MeduwebSeparatedTranslationModel) getTranslationModel();

			int startOffs = getStartPosition().getOffset();

			int endOffs = getEndPosition().getOffset() + 1;

			AttributeSet attr = getStartCharacterAttributes();

			String[] values = getValues();

			if ((values == null) || (values.length == 0))
			{
				representsValid = false; return false;
			}

			String[] translations = new String[values.length];

			MeduwebTranslationParser parser = MeduwebTranslationParser.instance();

			StringBuffer buffy = new StringBuffer();

			String ntlSep = model.getNTLSeparator();

			String sep = model.getSeparator();

			String currTranslation = null;

			for (int ctr=0; ctr<values.length; ctr++)
			{
				currTranslation = model.getTranslation(values[ctr]);

				if (currTranslation == null) { currTranslation = values[ctr]; }

				currTranslation = parser.parse(currTranslation, values[ctr], model);

				/* NOTE: the parser will return a null value if, for any reason, the
				 * line should be removed. This is indicated further up the chain by
				 * the node by returning false from the parseNode() method. */

				if (currTranslation == null)
				{
					representsValid = false; return false;
				}

				if (ctr == 0)
				{
					buffy.append(currTranslation); 
				}
				else if (ctr == values.length - 1)
				{
					boolean sS = GeneratorUtilities.startsWithSeparator(ntlSep);    

					boolean sW = GeneratorUtilities.startsWithWhiteSpace(ntlSep);

					if (!sS && !sW) { buffy.append(WHITESPACE); } buffy.append(ntlSep);

					if (!ntlSep.endsWith(WHITESPACE)) { buffy.append(WHITESPACE); }

					buffy.append(currTranslation);
				}
				else
				{
					buffy.append(sep);

					if (!sep.endsWith(WHITESPACE)) { buffy.append(WHITESPACE); }

					buffy.append(currTranslation);
				}
			}

			getDocument().remove(startOffs, endOffs - startOffs);

			getDocument().insertString(startOffs, buffy.toString(), attr);

			representsValid = true; // indicate that text is valid...

			return (!isDerived()); // whether 'significant' or not...
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new CouldNotParseNodeException(e.getMessage());
		}

	}

	public MeduwebSeparatedNode( String termName ) { super(termName); }

	private static final MeduwebDataHandler mVDH = MeduwebDataHandler.instance();

	private static final String WHITESPACE = " ";

}
