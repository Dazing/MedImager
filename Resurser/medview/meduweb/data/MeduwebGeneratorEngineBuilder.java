/*
 * @(#)MeduwebGeneratorEngineBuilder.java
 *
 * $Id: MeduwebGeneratorEngineBuilder.java,v 1.1 2003/07/21 21:55:07 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import medview.common.generator.*;
import medview.common.template.*;
import medview.common.translator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import javax.swing.event.*;

/**
 * The generator engine builder 'builds' all
 * necessary components for generation onto a
 * generator engine. When all components have
 * been built, the builder will fire an event
 * notifying that it is now ready.
 *
 * To specify which engine implementation you want
 * to use for the actual text generation, you need
 * to set the following system property to the fully
 * specified name of the engine implementation:<br>
 * <br>
 * <i>medview.common.generator.GeneratorEngine</i><br>
 * <br>
 * This can be done either directly from the command
 * prompt when running the program, or by using the
 * provided 'setGeneratorEngineClass()' method in the
 * builder.
 *
 * NOTE: as the date of this writing, the builder
 * clones the template model, so the actual engine
 * subclass does not have access to the passed
 * template model. The same goes for the translator
 * model class. This is done so that the engine
 * implementation can freely manipulate the models
 * as it sees fit.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MeduwebGeneratorEngineBuilder
{

	/**
	 * Sets the system property <i>medview.common.generator.GeneratorEngine</i>
	 * that defines the fully specified class name of the generator engine to
	 * use. This can be done either by using this method or by specifying the
	 * property directly from the command line when running your application.
	 * @param className the fully specified class name of the engine you wish
	 * to use for the text generation (an implementation of the GeneratorEngine
	 * interface).
	 */
	public void setGeneratorEngineClass(String className)
	{
		System.setProperty(SET_GENERATOR_ENGINE_PROPERTY, className);
	}





	public void buildIdentifiers( ExaminationIdentifier[] ids ) throws CouldNotBuildEngineException
	{
		if (engine == null) { buildEngine(); }

		if ((ids == null) || (ids.length == 0))
		{
			removeIdentifiers();

			return;
		}

		builtIdentifiers = ids;

		engine.setIdentifiers(ids);

		identifiersSet = true;

		checkIfShouldFire();
	}

	public void buildValueContainers( ExaminationValueContainer[] cont ) throws CouldNotBuildEngineException
	{
		if (engine == null) { buildEngine(); }

		if ((cont == null) || (cont.length == 0))
		{
			removeValueContainers();

			return;
		}

		builtValueContainers = cont;

		engine.setValueContainers(cont);

		containersSet = true;

		checkIfShouldFire();
	}

	public void buildSections( String[] sections ) throws CouldNotBuildEngineException
	{
		if (engine == null) { buildEngine(); }

		if ((sections == null) || (sections.length == 0))
		{
			removeSections();

			return;
		}

		builtSections = sections;

		engine.setSections(sections);

		sectionsSet = true;

		checkIfShouldFire();
	}

	public void buildTemplateModel( TemplateModel model ) throws CouldNotBuildEngineException
	{
		if (engine == null) { buildEngine(); }

		if (model == null)
		{
			removeTemplateModel();

			return;
		}

		builtTemplateModel = (TemplateModel) model.clone();

		engine.setTemplateModel(builtTemplateModel);

		templateSet = true;

		checkIfShouldFire();
	}

	public void buildTranslatorModel( MeduwebTranslatorModel model ) throws CouldNotBuildEngineException
	{
		if (engine == null) { buildEngine(); }

		if (model == null)
		{
			removeTranslatorModel();

			return;
		}

		builtTranslatorModel = (MeduwebTranslatorModel) model.clone();

		engine.setTranslatorModel(builtTranslatorModel);

		translatorSet = true;

		checkIfShouldFire();
	}

	protected void checkIfShouldFire()
	{
		if ((getRequiredElements() == 0) && (lastFiredEngineStatus != READY))
		{
			fireEngineStatusChanged(READY);
		}
	}

	private void buildEngine( ) throws CouldNotBuildEngineException
	{
		String engineClass = System.getProperty(SET_GENERATOR_ENGINE_PROPERTY);

		try
		{
			engine = (MeduwebGeneratorEngine) Class.forName(engineClass).newInstance();
		}
		catch (Exception e1)
		{
			try
			{
				engine = (MeduwebGeneratorEngine) Class.forName(DEFAULT_GENERATOR_ENGINE_CLASS_NAME).newInstance();
			}
			catch (Exception e2)
			{
				throw new CouldNotBuildEngineException(e2.getMessage());
			}
		}
	}





	public ExaminationIdentifier[] getBuiltIdentifiers()
	{
		return builtIdentifiers;
	}

	public MeduwebTranslatorModel getBuiltTranslatorModel()
	{
		return builtTranslatorModel;
	}

	public TemplateModel getBuiltTemplateModel()
	{
		return builtTemplateModel;
	}

	public ExaminationValueContainer[] getBuiltValueContainers()
	{
		return builtValueContainers;
	}

	public String[] getBuiltSections()
	{
		return builtSections;
	}





	public void addGEBListener( GeneratorEngineBuilderListener listener )
	{
		listenerList.add(GeneratorEngineBuilderListener.class, listener);
	}

	public void removeGEBListener( GeneratorEngineBuilderListener listener )
	{
		listenerList.remove(GeneratorEngineBuilderListener.class, listener);
	}

	protected void fireEngineStatusChanged(int status)
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == GeneratorEngineBuilderListener.class)
			{
				if (event == null) { event = new GeneratorEngineBuilderEvent(); }

				event.setEngineStatus(status); lastFiredEngineStatus = status;

				((GeneratorEngineBuilderListener)listeners[i+1]).engineStatusChanged(event);
			}
		}
	}





	public void removeIdentifiers( )
	{
		identifiersSet = false;

		builtIdentifiers = null;

		if (lastFiredEngineStatus == READY) { fireEngineStatusChanged(FURTHER); }
	}

	public void removeValueContainers( )
	{
		containersSet = false;

		builtValueContainers = null;

		if (lastFiredEngineStatus == READY) { fireEngineStatusChanged(FURTHER); }
	}

	public void removeSections( )
	{
		sectionsSet = false;

		builtSections = null;

		if (lastFiredEngineStatus == READY) { fireEngineStatusChanged(FURTHER); }
	}

	public void removeTemplateModel( )
	{
		templateSet = false;

		builtTemplateModel = null;

		if (lastFiredEngineStatus == READY) { fireEngineStatusChanged(FURTHER); }
	}

	public void removeTranslatorModel( )
	{
		translatorSet = false;

		builtTranslatorModel = null;

		if (lastFiredEngineStatus == READY) { fireEngineStatusChanged(FURTHER); }
	}

	public void removeAllBuilt( )
	{
		removeValueContainers();

		removeSections();

		removeTemplateModel();

		removeTranslatorModel();

		removeIdentifiers();
	}





	public MeduwebGeneratorEngine getEngine( ) throws FurtherElementsRequiredException
	{
		int req = getRequiredElements();

		if (req == 0)
		{
			return engine;
		}
		else
		{
			StringBuffer buffy = new StringBuffer();

			buffy.append(ERROR_MESSAGE_PREFIX);

			int preL = buffy.length();

			if ((req & EXAMINATION_CONTAINERS_REQUIRED) == 1)
			{
				buffy.append(EXAMINATION_CONTAINERS_REQUIRED_MESSAGE);
			}
			if ((req & SECTIONS_REQUIRED) == 1)
			{
				if (buffy.length() != preL) { buffy.append(", "); }

				buffy.append(SECTIONS_REQUIRED_MESSAGE);
			}
			if ((req & TEMPLATE_MODEL_REQUIRED) == 1)
			{
				if (buffy.length() != preL) { buffy.append(", "); }

				buffy.append(TEMPLATE_MODEL_REQUIRED_MESSAGE);
			}
			if ((req & TRANSLATOR_MODEL_REQUIRED) == 1)
			{
				if (buffy.length() != preL) { buffy.append(", "); }

				buffy.append(TRANSLATOR_MODEL_REQUIRED_MESSAGE);
			}
			if ((req & EXAMINATION_IDENTIFIERS_REQUIRED) == 1)
			{
				if (buffy.length() != preL) { buffy.append(", "); }

				buffy.append(EXAMINATION_IDENTIFIERS_REQUIRED_MESSAGE);
			}

			buffy.append(".");

			throw new FurtherElementsRequiredException(buffy.toString());
		}
	}

	public int getRequiredElements( )
	{
		int ret = 0;

		if (!containersSet) { ret += EXAMINATION_CONTAINERS_REQUIRED; }

		if (!sectionsSet) { ret += SECTIONS_REQUIRED; }

		if (!templateSet) { ret += TEMPLATE_MODEL_REQUIRED; }

		if (!translatorSet) { ret += TRANSLATOR_MODEL_REQUIRED; }

		if (!identifiersSet) { ret += EXAMINATION_IDENTIFIERS_REQUIRED; }

		return ret;
	}




	public MeduwebGeneratorEngineBuilder()
	{
		mVDH = MeduwebDataHandler.instance();

		listenerList = new EventListenerList();

		lastFiredEngineStatus = FURTHER;

		containersSet = false;

		sectionsSet = false;

		templateSet = false;

		translatorSet = false;

		identifiersSet = false;
	}


	private String[] builtSections;

	private int lastFiredEngineStatus;

	private EventListenerList listenerList;

	private TemplateModel builtTemplateModel;

	private MeduwebTranslatorModel builtTranslatorModel;

	private ExaminationIdentifier[] builtIdentifiers;

	private ExaminationValueContainer[] builtValueContainers;

	private GeneratorEngineBuilderEvent event;

	private MeduwebDataHandler mVDH;

	private MeduwebGeneratorEngine engine;


	private boolean containersSet;

	private boolean sectionsSet;

	private boolean templateSet;

	private boolean translatorSet;

	private boolean identifiersSet;


	public static final int EXAMINATION_CONTAINERS_REQUIRED = 1;

	public static final int EXAMINATION_IDENTIFIERS_REQUIRED = 2;

	public static final int SECTIONS_REQUIRED = 4;

	public static final int TEMPLATE_MODEL_REQUIRED = 8;

	public static final int TRANSLATOR_MODEL_REQUIRED = 16;


	private static final int READY = GeneratorEngineBuilderEvent.ALL_ELEMENTS_BUILT;

	private static final int FURTHER = GeneratorEngineBuilderEvent.FURTHER_ELEMENTS_REQUIRED;


	private static final String ERROR_MESSAGE_PREFIX = "Further elements required until engine complete: ";

	private static final String EXAMINATION_CONTAINERS_REQUIRED_MESSAGE = "examination value containers";

	private static final String SECTIONS_REQUIRED_MESSAGE = "included sections";

	private static final String TEMPLATE_MODEL_REQUIRED_MESSAGE = "template model";

	private static final String TRANSLATOR_MODEL_REQUIRED_MESSAGE = "translator model";

	private static final String EXAMINATION_IDENTIFIERS_REQUIRED_MESSAGE = "examination identifiers";


	private static final String SET_GENERATOR_ENGINE_PROPERTY = "medview.meduweb.data.MeduwebGeneratorEngine";

	private static final String DEFAULT_GENERATOR_ENGINE_CLASS_NAME = "medview.meduweb.data.MeduwebDefaultGeneratorEngine";

}
