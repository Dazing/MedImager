/**
 * @(#) MedRecordsGraphAdapter.java
 * $Id: MedRecordsGraphAdapter.java,v 1.1 2006/11/15 22:34:55 oloft Exp $
 */

package medview.medsummary.view;

import java.awt.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medrecords.*;
import medview.medrecords.data.*;

import medview.medsummary.model.*;

/**
 * This specific implementation of the graph
 * adapter interface uses the MedRecords
 * application for viewing graphs.
 *
 * @author Olof Torgersson
 */
public class MedRecordsGraphAdapter implements GraphAdapter
{

	public void initiateGraph( ExaminationIdentifier id, Component ownerComp )
	{
		MedRecords.showGraphWin(id, ownerComp);
	}

	public MedRecordsGraphAdapter(MedSummaryModel model)
	{
		this.model = model;
	}

	private MedSummaryModel model;
}

