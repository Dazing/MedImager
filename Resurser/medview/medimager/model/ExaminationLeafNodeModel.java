/**
 * @(#) ExaminationLeafModel.java
 */

package medview.medimager.model;

import java.util.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

/**
 * A node model containing information about an
 * examination.
 * @author Fredrik Lindahl
 */
public interface ExaminationLeafNodeModel
{
	/**
	 * Return the associated examination ID.
	 */
	public abstract ExaminationIdentifier getEID( );

	/**
	 * Return the associated examination date.
	 */
	public abstract Date getExaminationDate( );

	/**
	 * Return the associated patient identifier.
	 */
	public abstract PatientIdentifier getPID( );

	/**
	 * Set the associated examination identifier.
	 */
	public abstract void setEID( ExaminationIdentifier eid );

	/**
	 * Set the associated patient identifier.
	 */
	public abstract void setPID( PatientIdentifier pid );


}
