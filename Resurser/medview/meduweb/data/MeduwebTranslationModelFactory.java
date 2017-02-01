/*
 * @(#)MeduwebTranslationModelFactory.java
 *
 * $Id: MeduwebTranslationModelFactory.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import medview.datahandling.*;
import medview.common.translator.*;
import misc.foundation.*;

public class MeduwebTranslationModelFactory
{

	public static MeduwebTranslationModelFactory instance()
	{
		if (instance == null) { instance = new MeduwebTranslationModelFactory(); }

		return instance;
	}





	public MeduwebTranslationModel createTranslationModel( String term ) throws
		CouldNotResolveTypeException, CouldNotRecognizeTypeException, NotModelTypeException
	{
		try // beware: will not work for derived terms - should never be called for such a term...
		{
			return createTranslationModel(term, mVDH.getType(term), null, null);
		}
		catch (DefinitionLocationNotFoundException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (InvalidTypeException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (CouldNotParseException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (NoSuchTermException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
	}


	public MeduwebTranslationModel createTranslationModel( String term, Object[] values, String[] translations ) throws
		CouldNotResolveTypeException, CouldNotRecognizeTypeException, NotModelTypeException
	{
		try // beware: will not work for derived terms - should never be called for such a term...
		{
			return createTranslationModel(term, mVDH.getType(term), values, translations);
		}
		catch (DefinitionLocationNotFoundException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (InvalidTypeException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (CouldNotParseException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
		catch (NoSuchTermException e)
		{
			throw new CouldNotResolveTypeException(e.getMessage());
		}
	}


	public MeduwebTranslationModel createTranslationModel( String term, int type ) throws
		CouldNotRecognizeTypeException, NotModelTypeException
	{
		return createTranslationModel(term, type, null, null);
	}


	public MeduwebTranslationModel createTranslationModel( String term, String type ) throws
		CouldNotConvertException, CouldNotRecognizeTypeException, NotModelTypeException
	{
		return createTranslationModel(term, convertType(type), null, null); // only non-derived...
	}


	public MeduwebTranslationModel createTranslationModel( String term, int termType, Object[] values, String[] translations ) throws
		CouldNotRecognizeTypeException, NotModelTypeException
	{
		if (!isRecognizedType(termType))
		{
			String mess = "Unrecognized type for term (" + term + " - " + termType + ")";

			throw new CouldNotRecognizeTypeException(mess);
		}

		switch(termType)
		{
			case TermDataHandler.REGULAR_TYPE:
			{
				return new MeduwebRegularTranslationModel(term, values, translations);
			}

			case TermDataHandler.MULTIPLE_TYPE:
			{
				return new MeduwebDefaultSeparatedTranslationModel(term, values, translations);
			}

			case TermDataHandler.INTERVAL_TYPE:
			{
				return new MeduwebIntervalTranslationModel(term, values, translations);
			}

			default:
			{
				String mess = "Term not of model type (" + term + " - " + termType + ")";

				throw new NotModelTypeException(mess);
			}
		}
	}





	public boolean isTranslationTerm( String term )
	{
		try
		{
			int type = mVDH.getType(term);

			return (isTranslationType(type));
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean isTranslationType( int type )
	{
		return ((type == TermDataHandler.REGULAR_TYPE) ||

				(type == TermDataHandler.MULTIPLE_TYPE) ||

				(type == TermDataHandler.INTERVAL_TYPE));
	}

	public boolean isRecognizedType( int type )
	{
		return ((type == TermDataHandler.FREE_TYPE) ||

				(type == TermDataHandler.REGULAR_TYPE) ||

				(type == TermDataHandler.MULTIPLE_TYPE) ||

				(type == TermDataHandler.INTERVAL_TYPE));
	}

	public boolean isRecognizedType( String type )
	{
		return (type.equalsIgnoreCase(TermDataHandler.FREE_STRING) ||

				type.equalsIgnoreCase(TermDataHandler.REGULAR_STRING) ||

				type.equalsIgnoreCase(TermDataHandler.MULTIPLE_STRING) ||

				type.equalsIgnoreCase(TermDataHandler.INTERVAL_STRING));
	}

	public String[] getRecognizedTypeDescriptors()
	{
		return new String[]
		{
			TermDataHandler.FREE_STRING,

			TermDataHandler.REGULAR_STRING,

			TermDataHandler.MULTIPLE_STRING,

			TermDataHandler.INTERVAL_STRING
		};
	}

	private int convertType(String type) throws CouldNotConvertException
	{
		if (type.equalsIgnoreCase(TermDataHandler.FREE_STRING)) { return TermDataHandler.FREE_TYPE; }

		if (type.equalsIgnoreCase(TermDataHandler.REGULAR_STRING)) { return TermDataHandler.REGULAR_TYPE; }

		if (type.equalsIgnoreCase(TermDataHandler.MULTIPLE_STRING)) { return TermDataHandler.MULTIPLE_TYPE; }

		if (type.equalsIgnoreCase(TermDataHandler.INTERVAL_STRING)) { return TermDataHandler.INTERVAL_TYPE; }

		throw new CouldNotConvertException("Type '" + type + "' not recognized");
	}

	/*
	 * ==========================================================
	 *                        EXCEPTION KEY
	 * ==========================================================
	 *
	 * CouldNotResolveTypeException
	 * ----------------------------
	 * Occurs when the data handler for some reason cannot resolve
	 * the specified type. For instance, there may be an error in
	 * the file system if the term definitions are read from a file.
	 *
	 *
	 * NotModelTypeException
	 * ---------------------
	 * Occurs when the specified type is recognized but is not
	 * one of the types for which translation models are used
	 * (such as 'free').
	 *
	 *
	 * CouldNotRecognizeTypeException
	 * ------------------------------
	 * Occurs when the specified type is not a known type of this
	 * implementation.
	 *
	 *
	 * CouldNotConvertException
	 * ------------------------
	 * Occurs when a type specified as a string for some reason
	 * cannot be converted to the type specified as an int.
	 *
	 * ==========================================================
	 */








	private MeduwebTranslationModelFactory( ) { }

	private static MeduwebTranslationModelFactory instance;

	private MeduwebDataHandler mVDH = MeduwebDataHandler.instance();

}
