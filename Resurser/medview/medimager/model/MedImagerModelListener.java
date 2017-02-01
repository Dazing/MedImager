/**
 * @(#) MedImagerModelListener.java
 */

package medview.medimager.model;

import java.util.*;

/**
 * An event listener to the main model facade of the
 * MedImager application.
 * @author Fredrik Lindahl
 */
public interface MedImagerModelListener extends EventListener
{
	/**
	 * Event indicating that the journal template has
	 * changed.
	 * @param event MedImagerModelEvent
	 */
	void journalTemplateChanged(MedImagerModelEvent event);

	/**
	 * Event indicating that the journal translator has
	 * changed.
	 * @param event MedImagerModelEvent
	 */
	void journalTranslatorChanged(MedImagerModelEvent event);
}
