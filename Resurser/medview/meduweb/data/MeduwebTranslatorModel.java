/*
 * @(#)MeduwebTranslatorModel.java
 *
 * $Id: MeduwebTranslatorModel.java,v 1.1 2003/07/21 21:55:09 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl, modded for meduweb by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import java.util.*;

import javax.swing.event.*;

import medview.datahandling.*;
import medview.common.translator.*;

import misc.foundation.*;

/**
 * The translator model class is responsible for keeping a
 * collection of translation models and all that is related
 * to this fact. The model provides utility methods for
 * making it easier to deal with the kept translation models.
 * For instance, you can retrieve the translation model for
 * a certain term and then perform additions, removals, VG
 * settings etc. to this model, or you can use one of the
 * utility methods for dealing with these things directly
 * and let the translator model take care of the details.
 *
 * Adding and removing terms and values etc. will not affect
 * the core term definitions and values located elsewhere in
 * the system. It only affects the current model - if a term
 * is removed from the model and the model is later on read
 * back, the new term will be added by the translator
 * synchronizer. This could be set as a user setting in the
 * future (whether or not to use the translator synchronizer
 * when reading in stored translator models).
 *
 * For developers: it is very important to keep in mind
 * that the internal hashmap of translation models is
 * indexed by the name of the term in lower case. The actual
 * term string (original as it was passed) is kept in the
 * respective translation model instance.
 *
 * @author Fredrik Lindahl
 */
public class MeduwebTranslatorModel implements Cloneable
{

	public MeduwebTranslationModel getTranslationModel( String term )
	{
		return (MeduwebTranslationModel) translationModels.get(term.toLowerCase());
	}

	public MeduwebTranslationModel[] getTranslationModels()
	{
		MeduwebTranslationModel[] retArray = new MeduwebTranslationModel[translationModels.size()];

		translationModels.values().toArray(retArray);

		return retArray;
	}





	public boolean containsTranslationModel(String term)
	{
		return (translationModels.get(term.toLowerCase()) != null);
	}





	public MeduwebTranslationModel addTranslationModel( String term )
	{
		try // beware: only non-derived terms are recognized using this method...
		{
			MeduwebTranslationModel model = modelFactory.createTranslationModel(term);

			addAndFire(term, model);

			return model;
		}
		catch (NotModelTypeException e)
		{
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	public MeduwebTranslationModel addTranslationModel( String term, int type )
	{
		try
		{
			MeduwebTranslationModel model = modelFactory.createTranslationModel(term, type);

			addAndFire(term, model);

			return model;
		}
		catch (NotModelTypeException e)
		{
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	public MeduwebTranslationModel addTranslationModel( String term, String type )
	{
		try // beware: only non-derived types are recognized using this method...
		{
			MeduwebTranslationModel model = modelFactory.createTranslationModel(term, type);

			addAndFire(term, model);

			return model;
		}
		catch (NotModelTypeException e)
		{
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	public MeduwebTranslationModel addTranslationModel( String term, int type, Object[] values, String[] translations )
	{
		try
		{
			MeduwebTranslationModel model = modelFactory.createTranslationModel(term, type, values, translations);

			addAndFire(term, model);

			return model;
		}
		catch (NotModelTypeException e)
		{
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	public MeduwebTranslationModel addTranslationModel(MeduwebTranslationModel model)
	{
		addAndFire(model.getTerm(), model);

		return model;
	}

	private void addAndFire(String term, MeduwebTranslationModel model)
	{
		translationModels.put(term.toLowerCase(), model); // removes old if existant...

		model.addTranslationModelListener(updateListener);

		if (model instanceof MeduwebSeparatedTranslationModel)
		{
			MeduwebSeparatedTranslationModel m = (MeduwebSeparatedTranslationModel) model;

			m.addSeparatedTranslationModelListener(updateListener);
		}

		fireTranslationModelAdded(model);

		fireTranslatorModelUpdated();
	}

	public void removeTranslationModel( String term )
	{
		if (translationModels.containsKey(term.toLowerCase()))
		{
			MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

			model.removeTranslationModelListener(updateListener);

			if (model instanceof MeduwebSeparatedTranslationModel)
			{
				MeduwebSeparatedTranslationModel m = (MeduwebSeparatedTranslationModel) model;

				m.removeSeparatedTranslationModelListener(updateListener);
			}

			translationModels.remove(term.toLowerCase());

			fireTranslationModelRemoved(model);

			fireTranslatorModelUpdated();
		}
	}





	/**
	 * Returns whether or not the specified term
	 * is one that should be translated, and thus
	 * one that causes a corresponding Translation
	 * model to be created for it.
	 */
	public boolean isTranslationTerm( String term )
	{
		return modelFactory.isTranslationTerm(term);
	}


	/**
	 * Returns whether or not the specified type
	 * is one that should be translated, and thus
	 * one that causes a corresponding Translation
	 * model to be created for it's term.
	 */
	public boolean isTranslationType( int type )
	{
		return modelFactory.isTranslationType(type);
	}


	/**
	 * Returns whether or not the specified type
	 * is one that is recognized at all by the
	 * translation model factory, which in turn
	 * returns if it is recognized in the data
	 * layer.
	 */
	public boolean isRecognizedType( int type )
	{
		return modelFactory.isRecognizedType(type);
	}


	/**
	 * Returns whether or not the specified type
	 * is one that is recognized at all by the
	 * translation model factory, which in turn
	 * returns if it is recognized in the data
	 * layer.
	 */
	public boolean isRecognizedType( String type )
	{
		return modelFactory.isRecognizedType(type);
	}


	/**
	 * Returns the types recognized by the
	 * translation model factory, which in turn
	 * recognizes only the types recognized in
	 * the data layer (MedViewDataHandler).
	 */
	public String[] getRecognizedTypeDescriptors()
	{
		return modelFactory.getRecognizedTypeDescriptors();
	}





	public void addValue( String term, Object value )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.addValue(value); }
	}

	public void addValue( String term, Object value, String translation )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.addValue(value, translation); }
	}

	public void addValue( String term, Object value, String translation, Date date )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.addValue(value, translation, date); }
	}

	public void removeValue( String term, Object value )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.removeValue(value); }
	}

	public void setPreviewStatus( String term, Object value, boolean flag )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.setPreviewStatus(value, flag); }
	}

	public void setAutoVG( String term, boolean autoVG )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.setAutoVG(autoVG); }
	}

	public void setAutoVGPolicy( String term, int policy )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.setVGPolicy(policy); }
	}

	public void setAutoVGPolicy( String term, String policy )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null)
		{
			int vgP = MeduwebTranslationModel.getVGPolicy(policy);

			if (vgP != -1)
			{
				model.setVGPolicy(vgP);
			}
			else
			{
				System.out.print("MeduwebTranslatorModel : WARNING : tried to ");

				System.out.print("set an unknown autoVG policy for the ");

				System.out.print("term '" + term + "', the policy was ");

				System.out.println(policy + "...");
			}

			/* NOTE: if the translation model returns
			 * a -1 value when trying to convert the
			 * string policy to an int policy, the
			 * string policy is not recognized. */
		}
	}

	public void setNTLSeparator( String term, String sep ) throws MethodNotSupportedException
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (!(model instanceof MeduwebSeparatedTranslationModel))
		{
			throw new MethodNotSupportedException(this.getClass().getName() + " not instance of SeparatedTranslationModel");
		}

		if (model != null) { ((MeduwebSeparatedTranslationModel)model).setNTLSeparator(sep); }
	}

	public void setSeparator( String term, String sep ) throws MethodNotSupportedException
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (!(model instanceof MeduwebSeparatedTranslationModel))
		{
			throw new MethodNotSupportedException(this.getClass().getName() + " not instance of SeparatedTranslationModel");
		}

		if (model != null) { ((MeduwebSeparatedTranslationModel)model).setSeparator(sep); }
	}

	/* NOTE: we do not need to fire 'translator model
	 * updated' events here since the translation models
	 * fires such events to the update listener, which
	 * in turn will fire the update event for the translator. */





	public String[] getContainedTerms( )
	{
		String[] retArray = new String[translationModels.size()];

		Collection values = translationModels.values();

		MeduwebTranslationModel currModel = null;

		Iterator iter = values.iterator();

		int retArrayCtr = 0;

		while (iter.hasNext())
		{
			currModel = (MeduwebTranslationModel) iter.next();

			retArray[retArrayCtr++] = currModel.getTerm();
		}

		return retArray;
	}

	/* NOTE: it is very important to understand that
	 * the terms are indexed in the hashmap by their
	 * lower case representations, but the term kept
	 * in the actual TranslationModel is referenced
	 * in the original string version (containing the
	 * original case settings). */





	public Object[] getValues( String term )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { return model.getValues(); }

		return null;
	}

	public String getTranslation( String term, Object value )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { return model.getTranslation(value); }

		return null;
	}

	public String[] getTranslations( String term )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null)
		{
			Translation[] translations = model.getTranslations();

			String[] retArray = new String[translations.length];

			for (int ctr=0; ctr<translations.length; ctr++)
			{
				retArray[ctr] = translations[ctr].getTranslation();
			}

			return retArray;
		}

		return null;
	}





	public void setTranslation( String term, Object value, String trans )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.setTranslation(value, trans); }
	}

	public void setTranslation( String term, Object value, String trans, Date date )
	{
		MeduwebTranslationModel model = (MeduwebTranslationModel) translationModels.get(term.toLowerCase());

		if (model != null) { model.setTranslation(value, trans, date); }
	}





	public void addTranslatorModelListener( TranslatorModelListener listener )
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(TranslatorModelListener.class, listener);
	}

	public void removeTranslatorModelListener( TranslatorModelListener listener )
	{
		if (listenerList == null) { return; }

		listenerList.remove(TranslatorModelListener.class, listener);
	}

	public void removeAllListeners()
	{
		if (listenerList == null) { return; }

		EventListener[] listeners = listenerList.getListeners(TranslatorModelListener.class);

		for (int ctr=0; ctr<listeners.length; ctr++)
		{
			listenerList.remove(TranslatorModelListener.class, listeners[ctr]);
		}
	}

	private void fireTranslationModelRemoved( MeduwebTranslationModel model )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TranslatorModelEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslatorModelListener.class)
			{
				if (event == null) { event = new TranslatorModelEvent(this); }

				((TranslatorModelListener)listeners[i+1]).translationModelRemoved(event);
			}
		}
	}

	private void fireTranslationModelAdded( MeduwebTranslationModel model )
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TranslatorModelEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslatorModelListener.class)
			{
				if (event == null) { event = new TranslatorModelEvent(this); }

				((TranslatorModelListener)listeners[i+1]).translationModelAdded(event);
			}
		}
	}

	private void fireTranslatorModelUpdated()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TranslatorModelEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslatorModelListener.class)
			{
				if (event == null) { event = new TranslatorModelEvent(this); }

				((TranslatorModelListener)listeners[i+1]).translatorModelUpdated(event);
			}
		}
	}





	public Object clone()
	{
		MeduwebTranslatorModel clone = new MeduwebTranslatorModel();

		MeduwebTranslationModel[] models = getTranslationModels();

		MeduwebTranslationModel cTM = null; String term = null;

		for (int ctr=0; ctr<models.length; ctr++)
		{
			cTM = (MeduwebTranslationModel) models[ctr].clone();

			clone.addTranslationModel(cTM);
		}

		return clone;
	}





	private void initSimpleMembers()
	{
		modelFactory = MeduwebTranslationModelFactory.instance();

		updateListener = new UpdateListener(); // 'save needed' support

		translationModels = new HashMap();
	}

	public MeduwebTranslatorModel()
	{
		initSimpleMembers();
	}

	private MeduwebTranslationModelFactory modelFactory;

	private EventListenerList listenerList;

	private UpdateListener updateListener;

	private HashMap translationModels;





	private class UpdateListener implements TranslationModelListener, SeparatedTranslationModelListener
	{
			public void previewChanged(TranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void translationAdded(TranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void translationRemoved(TranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void translationChanged(TranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void vgChanged(TranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void ntlSeparatorChanged(SeparatedTranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}

			public void separatorChanged(SeparatedTranslationModelEvent e)
			{
				fireTranslatorModelUpdated();
			}
	}








	/* --------------------------------------
	 *            UNIT TEST METHOD
  	 * --------------------------------------
  	 */
	public static void main(String[] args)
	{
		TranslatorModel originalModel = new TranslatorModel();

		originalModel.addTranslationModel("Peach", TermDataHandler.FREE_TYPE);

		originalModel.addTranslationModel("Dade", TermDataHandler.INTERVAL_TYPE);

		originalModel.addTranslationModel("Apple", TermDataHandler.MULTIPLE_TYPE);

		originalModel.addTranslationModel("Ananas", TermDataHandler.REGULAR_TYPE);

		originalModel.addTranslationModel("Banana", TermDataHandler.FREE_TYPE);

		originalModel.addTranslationModel("Orange", TermDataHandler.REGULAR_TYPE);

		originalModel.addValue("Dade", new misc.domain.Interval((float)0.0, (float)1.9), "Dade 1's translation", new Date());

		originalModel.addValue("Dade", new misc.domain.Interval((float)2.0, (float)4.0), "Dade 2's translation", new Date());

		originalModel.addValue("Apple", "Apples value 1", "Apple 1's translation", new Date());

		originalModel.addValue("Apple", "Apples value 2", "Apple 2's translation", new Date());

		originalModel.addValue("Apple", "Apples value 3", "Apple 3's translation", new Date());

		originalModel.addValue("Apple", "Apples value 4", "Apple 4's translation", new Date());

		originalModel.addValue("Orange", "Oranges value 1", "Orange 1's translation", new Date());

		originalModel.addValue("Orange", "Oranges value 2", "Orange 2's translation", new Date());

		originalModel.setPreviewStatus("Dade", new Float(1.0), true);

		originalModel.setPreviewStatus("Apple", "Apples value 3", true);

		originalModel.setPreviewStatus("Orange", "Oranges value 1", true);

		originalModel.setAutoVGPolicy("Dade", TranslationModel.AUTO_VG_POLICY_VERSAL);

		originalModel.setAutoVGPolicy("Apple", TranslationModel.AUTO_VG_POLICY_VERSAL);

		originalModel.setAutoVGPolicy("Orange", TranslationModel.AUTO_VG_POLICY_GEMEN);

		originalModel.setAutoVG("Dade", false);

		originalModel.setAutoVG("Apple", true);

		originalModel.setAutoVG("Orange", false);

		try
		{
			originalModel.setNTLSeparator("Apple", "apples NTL sep");

			originalModel.setSeparator("Apple", "apples standard sep");
		}
		catch (MethodNotSupportedException e)
		{
			e.printStackTrace();
		}


		System.out.println();

		System.out.println("======================================================================");

		System.out.println("PRINTING ORIGINAL TRANSLATOR MODEL TERM NAMES, VALUES, TRANSLATIONS...");

		System.out.println("======================================================================");

		TranslationModel[] tModels = originalModel.getTranslationModels();

		for (int ctr=0; ctr<tModels.length; ctr++)
		{
			Translation[] tS = tModels[ctr].getTranslations();

			System.out.println(tModels[ctr].getTerm());

			System.out.println("autoVG = " + tModels[ctr].isAutoVG());

			System.out.println("policy = " + tModels[ctr].getVGPolicy());

			if (tModels[ctr] instanceof SeparatedTranslationModel)
			{
				System.out.println("separator = " + ((SeparatedTranslationModel)tModels[ctr]).getSeparator());

				System.out.println("NTL separator = " + ((SeparatedTranslationModel)tModels[ctr]).getNTLSeparator());
			}

			System.out.print("translation objects  = ");

			if (tS.length == 0)
			{
				System.out.print("none");
			}
			else
			{
				for (int ctr2=0; ctr2<tS.length; ctr2++)
				{
					System.out.print("{");

					System.out.print("value = " + tS[ctr2].getValue() + ", ");

					System.out.print("translation = " + tS[ctr2].getTranslation() + ", ");

					System.out.print("modified = " + tS[ctr2].getLastModified() + ", ");

					System.out.print("preview = " + tS[ctr2].isPreview());

					System.out.print("}");

					if (ctr2 != tS.length-1) { System.out.print(", "); }
				}
			}

			System.out.println(); System.out.println();
		}


		TranslatorModel clonedModel = (TranslatorModel) originalModel.clone();


		System.out.println(); System.out.println();

		System.out.println("======================================================================");

		System.out.println("PRINTING CLONED TRANSLATOR MODEL TERM NAMES, VALUES, TRANSLATIONS...");

		System.out.println("======================================================================");

		tModels = clonedModel.getTranslationModels();

		for (int ctr=0; ctr<tModels.length; ctr++)
		{
			Translation[] tS = tModels[ctr].getTranslations();

			System.out.println(tModels[ctr].getTerm());

			System.out.println("autoVG = " + tModels[ctr].isAutoVG());

			System.out.println("policy = " + tModels[ctr].getVGPolicy());

			if (tModels[ctr] instanceof SeparatedTranslationModel)
			{
				System.out.println("separator = " + ((SeparatedTranslationModel)tModels[ctr]).getSeparator());

				System.out.println("NTL separator = " + ((SeparatedTranslationModel)tModels[ctr]).getNTLSeparator());
			}

			System.out.print("translation objects  = ");

			if (tS.length == 0)
			{
				System.out.print("none");
			}
			else
			{
				for (int ctr2=0; ctr2<tS.length; ctr2++)
				{
					System.out.print("{");

					System.out.print("value = " + tS[ctr2].getValue() + ", ");

					System.out.print("translation = " + tS[ctr2].getTranslation() + ", ");

					System.out.print("modified = " + tS[ctr2].getLastModified() + ", ");

					System.out.print("preview = " + tS[ctr2].isPreview());

					System.out.print("}");

					if (ctr2 != tS.length-1) { System.out.print(", "); }
				}
			}

			System.out.println(); System.out.println();
		}
	}
}
