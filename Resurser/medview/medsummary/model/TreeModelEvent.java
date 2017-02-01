/*
 * @(#)TreeModelEvent.java
 *
 * $Id: TreeModelEvent.java,v 1.6 2003/03/13 01:21:46 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.util.*;

public class TreeModelEvent extends EventObject
{
	public PatientModel getPatientModel( )
	{
		return patientModel;
	}

	public ExaminationModel getExaminationModel( )
	{
		return examinationModel;
	}

	public TreeModelEvent( Object source )
	{
		this(source, null, null);
	}

	public TreeModelEvent( Object source, PatientModel pM, ExaminationModel eM )
	{
		super(source);

		this.patientModel = pM;

		this.examinationModel = eM;
	}

	private PatientModel patientModel;

	private ExaminationModel examinationModel;
}
