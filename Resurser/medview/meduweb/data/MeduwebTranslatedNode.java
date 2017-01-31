/*
 * @(#)MeduwebTranslatedNode.java
 *
 * $Id: MeduwebTranslatedNode.java
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import medview.common.translator.*;
import medview.common.generator.*;

public abstract class MeduwebTranslatedNode extends TermNode
{

	public MeduwebTranslationModel getTranslationModel( )
	{
		return model;
	}



	public void setTranslationModel( MeduwebTranslationModel model )
	{
		this.model = model;
	}



	public MeduwebTranslatedNode( String termName ) { super(termName); }

	private MeduwebTranslationModel model;

}
