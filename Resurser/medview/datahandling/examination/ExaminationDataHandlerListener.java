/**
 * @(#) ExaminationDataHandlerListener.java
 */

package medview.datahandling.examination;

import java.util.*;

/**
 * An interface that should be implemented if you
 * are interested in listening to events from the
 * examination datahandler.
 * @author Fredrik Lindahl
 */
public interface ExaminationDataHandlerListener extends EventListener
{
	void examinationLocationChanged( ExaminationDataHandlerEvent e );

	void examinationAdded( ExaminationDataHandlerEvent e );

	void examinationUpdated(ExaminationDataHandlerEvent e);

	void examinationRemoved(ExaminationDataHandlerEvent e);

	void examinationLocationIDChanged( ExaminationDataHandlerEvent e );
}
