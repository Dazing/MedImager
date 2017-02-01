package medview.medserver.model;

import java.util.*;

/**
 * Besides from firing communication-related events,
 * the medserver model will fire events that do not
 * have an origin in communication. For instance, if
 * a term is added locally to the term definition
 * location, this fact is noted by the model firing
 * such an event to all registered model listeners.
 * @author Fredrik Lindahl
 */
public interface MedServerModelListener extends EventListener
{
	public void termAdded(MedServerModelEvent e);

	public void termRemoved(MedServerModelEvent e);

	public void valueAdded(MedServerModelEvent e);

	public void valueRemoved(MedServerModelEvent e);

	public void termDefinitionLocationChanged(MedServerModelEvent e);

	public void termValueLocationChanged(MedServerModelEvent e);

	public void examinationAdded(MedServerModelEvent e);
	
	public void examinationUpdated(MedServerModelEvent e);

	public void examinationDataLocationChanged(MedServerModelEvent e);

	public void examinationDataLocationIDChanged(MedServerModelEvent e);

	public void termDataHandlerChanged(MedServerModelEvent e);

	public void examinationDataHandlerChanged(MedServerModelEvent e);

	public void tATDataHandlerChanged(MedServerModelEvent e);
	
	public void pCodePrefixChanged(MedServerModelEvent e);
	
	public void numberGeneratorLocationChanged(MedServerModelEvent e);
	
	public void pCodeGeneratorChanged(MedServerModelEvent e);
}
