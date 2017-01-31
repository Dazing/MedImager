/*
 * @(#)MeduwebGeneratorEngine.java
 *
 * $Id: MeduwebGeneratorEngine.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl, modded for meduweb by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import javax.swing.text.*;

import medview.common.template.*;
import medview.common.translator.*;

import medview.datahandling.examination.*;
import medview.common.generator.*;
import misc.foundation.*;

public abstract class MeduwebGeneratorEngine
{

	public void setSections( String[] sections )
	{
		this.sections = sections;
	}

	public void setTemplateModel( TemplateModel model )
	{
		this.templateModel = model;
	}

	public void setTranslatorModel( MeduwebTranslatorModel model )
	{
		this.translatorModel = model;
	}

	public void setIdentifiers( ExaminationIdentifier[] ids )
	{
		this.identifiers = ids;
	}

	public void setValueContainers( ExaminationValueContainer[] cont )
	{
		this.valueContainers = cont;
	}

	/* NOTE: In the current implementation, you may safely
	 * assume that the set template model is free to operate
	 * and modify any way you see fit (when you are coding the
	 * subclass implementation of GeneratorEngine). The other
	 * set members, on the other hand, may be the actual members
	 * used and thus may not be modified IN ANY WAY by the
	 * subclass implementation. This might change in a future
	 * version of the system, at which time this note should be
	 * modified to reflect this fact. // Fredrik 02.11.05 */




	public void clearSections( )
	{
		this.sections = null;
	}

	public void clearTemplateModel( )
	{
		this.templateModel = null;
	}

	public void clearTranslatorModel( )
	{
		this.translatorModel = null;
	}

	public void clearIdentifiers( )
	{
		this.identifiers = null;
	}

	public void clearValueContainers( )
	{
		this.valueContainers = null;
	}





	public abstract StyledDocument generateDocument( ) throws
		CouldNotGenerateException;

	public abstract StyledDocument generateDocument( ProgressNotifiable not ) throws
		CouldNotGenerateException;


	protected String[] sections;

	protected TemplateModel templateModel;

	protected MeduwebTranslatorModel translatorModel;

	protected ExaminationIdentifier[] identifiers;

	protected ExaminationValueContainer[] valueContainers;

}
