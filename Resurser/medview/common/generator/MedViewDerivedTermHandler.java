package medview.common.generator;

import java.util.*;

import medview.datahandling.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface MedViewDerivedTermHandler extends se.chalmers.cs.medview.docgen.DerivedTermHandler
{
	void setExaminationDate(Date date);

	void setPatientIdentifier(PatientIdentifier pid);
}
