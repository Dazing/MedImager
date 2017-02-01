package medview.medserver.data;

import medview.datahandling.examination.*;

public interface RemoteExaminationDataHandlerImplListener
{
	public void examinationAdded(RemoteExaminationDataHandlerEvent e);

	public void examinationUpdated(RemoteExaminationDataHandlerEvent e);

	public void serverNameChanged(RemoteExaminationDataHandlerEvent e);

	public void locationChanged(RemoteExaminationDataHandlerEvent e);

	public void patientListRequested(RemoteExaminationDataHandlerEvent e);

	public void examinationCountRequested(RemoteExaminationDataHandlerEvent e);

	public void examinationListRequested(RemoteExaminationDataHandlerEvent e);

	public void allExaminationValueContainersRequested(RemoteExaminationDataHandlerEvent e);

	public void examinationValueContainerRequested(RemoteExaminationDataHandlerEvent e);

	public void imageCountRequested(RemoteExaminationDataHandlerEvent e);

	public void imagesRequested(RemoteExaminationDataHandlerEvent e);

	public void refreshPerformed(RemoteExaminationDataHandlerEvent e);
}
