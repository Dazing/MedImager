/*
 * @(#)TemplateAndTranslatorDataHandler.java
 *
 * $Id: TemplateAndTranslatorDataHandler.java
 *
 * --------------------------------
 * Original author: Fredrik Lindahl, modded for Meduweb by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import medview.common.translator.*;
import medview.datahandling.*;
import misc.foundation.*;
import misc.foundation.io.*;

/**
 * The interface that defines what it means to be
 * a template and translator data handler. This
 * interface should be implemented if you wish to
 * define a special kind of template and translator
 * handling - the current default implementation is
 * the XMLTemplateAndTranslatorDataHandler, which
 * implements the methods defined here by using
 * various xml processing methods.
 *
 * @author Fredrik Lindahl
 */
public interface TemplateAndTranslatorDataHandler
{

	/**
	 * Retrieves an array of all categories for a certain
	 * template specified by a location - returns null if
	 * there is an error and throws an exception if the
	 * specified template does not exist.
	 */
	public abstract String[] getSectionNames(String location) throws
		NoSuchTemplateException;





	/**
	 * Saves a template model. All associated information (such as the
	 * associated translator model location and the location of the
	 * model etc.) are included in the wrapper object passed to the
	 * method.
	 */
	public abstract void saveTemplate(TemplateModelWrapper wrapper) throws
		CouldNotSaveException;


	/**
	 * Constructs and returns a template model wrapper object from the
	 * specified location. Will throw exceptions if a wrapper object for
	 * the sought template model could not be created.
	 */
	public abstract TemplateModelWrapper loadTemplate(String location) throws
		InvalidVersionException, CouldNotLoadException;





	/**
	 * Saves a translator with the specified name and model. The
	 * model is an instance of TranslatorModel, containing a table
	 * of TranslationModel implementations.
	 */
	public abstract void saveTranslator(MeduwebTranslatorModel model, String location);


	/**
	 * Loads a translator from the specified location.
	 * Constructs a TranslatorModel instance and return
	 * the constructed object.
	 */
	public abstract MeduwebTranslatorModel loadTranslator(String location) throws
		CouldNotLoadException;





	/**
	 * Since each implementation class of this interface
	 * has it's own unique way of locating examinations
	 * and associated information, the specific subclass
	 * has a responsibility to create a more readable id
	 * of a location. This method returns that id, which
	 * can be used in the gui layer (view) for identifying
	 * a current template and/or translator location.
	 */
	public abstract String parseIdentifier(String location);





	/**
	 * @return the default template subdirectory. If the
	 * implementation is such that the concept of a
	 * 'directory' is non-existant (for instance, the
	 * models are stored using SQL), an exception will be
	 * thrown stating that the method is not supported
	 * with the current implementation.
	 */
	public abstract String getDefaultTemplateDirectory() throws
		MethodNotSupportedException;

	/**
	 * @return the default translator subdirectory. If the
	 * implementation is such that the concept of a
	 * 'directory' is non-existant (for instance, the
	 * models are stored using SQL), an exception will be
	 * thrown stating that the method is not supported
	 * with the current implementation.
	 */
	public abstract String getDefaultTranslatorDirectory() throws
		MethodNotSupportedException;

}