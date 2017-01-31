/*
 * @(#)MeduwebRegularNode.java
 *
 * $Id: MeduwebRegularNode.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import javax.swing.text.*;

import medview.common.translator.*;
import medview.common.generator.*;

public class MeduwebRegularNode extends MeduwebTranslatedNode
{
	public boolean parseNode( ) throws CouldNotParseNodeException
	{
		try
		{
			MeduwebTranslationModel model = getTranslationModel();

			int startOffs = getStartPosition().getOffset();

			int endOffs = getEndPosition().getOffset() + 1; // term

			AttributeSet attr = getStartCharacterAttributes();

			String[] values = getValues();

			if ((values == null) || (values.length == 0)) // nothing to replace...
			{
				representsValid = false; return false;
			}
			
			String translation = null;
			translation = model.getTranslation(values[0]);

			if (translation == null) { translation = values[0]; }

			MeduwebTranslationParser parser = MeduwebTranslationParser.instance();

			translation = parser.parse(translation, values[0], model);

			if (translation == null) // parser indicates 'line remove request'...
			{
				representsValid = false; return false;
			}

			/* NOTE: the translation parser's responsibility is to take
			 * one translated string (raw from the translator), and to
			 * expand any entity references (such as $?$ or $NOLINE$)
			 * and return the expanded / parsed translation. If the
			 * returned translation is null, the translation parser has
			 * indicated that the translation should be removed (in other
			 * words, a value of false should be returned from the parseNode()
			 * method). The translator parser also takes into account the
			 * VG setting of the model, such that the itself translations are
			 * set to the VG setting (if used). */

			getDocument().remove(startOffs, endOffs - startOffs);

			getDocument().insertString(startOffs, translation, attr);

			representsValid = true; // indicate that text is valid...

			return (!isDerived()); // whether or not significant...
		}
		catch (BadLocationException e)
		{
			throw new CouldNotParseNodeException(e.getMessage());
		}
	}

	public MeduwebRegularNode( String termName ) { super(termName); }

}
