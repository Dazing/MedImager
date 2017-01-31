/**
 * @(#) ChosenImagesSearchEngine.java
 */

package medview.medimager.model;

/**
 * A way to search all the node models in the current library
 * root node model for certain data.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface ChosenImagesSearchEngine
{
	/**
	 * Obtain the node models contained in the model that
	 * fits the specified search string.
	 * @param searchText String
	 * @return NodeModel[]
	 */
	public abstract NodeModel[] search( String searchText );

}
