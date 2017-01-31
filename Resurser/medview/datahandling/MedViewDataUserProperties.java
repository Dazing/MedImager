/*
 * @(#)MedViewDataUserProperties.java
 *
 * $Id: MedViewDataUserProperties.java,v 1.8 2004/02/19 18:21:26 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

/**
 * NOTE (030729) - The properties that were here
 * earlier has been removed in order to provide a
 * more dynamic environment where multiple applications
 * can have same datahandlers with different locations.
 * The interface will remain for some time until it is
 * determined whether or not it is necessary. If not, it
 * might be removed in the future.
 *
 * A collection of constants used by the classes in
 * the datahandling layer for retrieving values of
 * preferences and properties. These properties can
 * also be used by outer classes to obtain and set
 * values, for instance to set which class to use for
 * examination datahandling dynamically during runtime.
 *
 * It is imperative that the class-defining properties
 * are set through the MedViewDataHandler facade, and
 * not directly. If they are set directly, no events
 * will be fired to registered datahandling listeners
 * that the implementation class has changed, and no
 * appropriate action can thus be taken dynamically.
 * @author Fredrik Lindahl
 */
public interface MedViewDataUserProperties
{
	public static final String USER_ID_PROPERTY = "userID";

	public static final String USER_NAME_PROPERTY = "userName";
}
