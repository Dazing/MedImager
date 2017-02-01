/*
 * @(#)MedSummaryModelEvent.java
 *
 * $Id: MedSummaryModelEvent.java,v 1.6 2004/11/19 12:34:00 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.util.*;

import medview.datahandling.examination.*;

/**
 */
public class MedSummaryModelEvent extends EventObject
{
	public void setExaminationIdentifier(ExaminationIdentifier id)
	{
		this.examinationIdentifier = id;
	}
	
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return this.examinationIdentifier;
	}
	
	/**
	 * @param source
	 */
	public MedSummaryModelEvent( Object source )
	{
		super(source);
	}
	
	private ExaminationIdentifier examinationIdentifier;


}
