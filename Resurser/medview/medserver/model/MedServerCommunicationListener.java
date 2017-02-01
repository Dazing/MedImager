package medview.medserver.model;

import java.util.*;

/**
 * The model fires a mix of communication events, some
 * relating to the term datahandling, and some relating
 * to the examination datahandling. Also, the model will
 * fire events concerning the server status, like activation
 * and deactivation events. This interface groups together
 * all possible communication-related events that the model
 * can fire, and any class interested in being notified when
 * such events occur can implement this interface and register
 * with the model.
 * @author Fredrik Lindahl
 */
public interface MedServerCommunicationListener extends EventListener
{
	public void patientListRequested(MedServerCommunicationEvent e);

	public void examinationCountRequested(MedServerCommunicationEvent e);

	public void examinationListRequested(MedServerCommunicationEvent e);

	public void allExaminationValueContainersRequested(MedServerCommunicationEvent e);

	public void examinationValueContainerRequested(MedServerCommunicationEvent e);

	public void imageCountRequested(MedServerCommunicationEvent e);

	public void examinationAdded(MedServerCommunicationEvent e);

	public void examinationUpdated(MedServerCommunicationEvent e);

	public void imagesRequested(MedServerCommunicationEvent e);

	public void refreshPerformed(MedServerCommunicationEvent e);


	public void termListRequested(MedServerCommunicationEvent e);

	public void termHashMapRequested(MedServerCommunicationEvent e);

	public void valuesRequested(MedServerCommunicationEvent e);

	public void typeRequested(MedServerCommunicationEvent e);

	public void termExistanceQueried(MedServerCommunicationEvent e);

	public void termAdded(MedServerCommunicationEvent e);

	public void termRemoved(MedServerCommunicationEvent e);

	public void valueAdded(MedServerCommunicationEvent e);

	public void valueRemoved(MedServerCommunicationEvent e);


	public void serverActivated(MedServerCommunicationEvent e);

	public void serverDeactivated(MedServerCommunicationEvent e);


	public void pCodeRequested(MedServerCommunicationEvent e);
}
