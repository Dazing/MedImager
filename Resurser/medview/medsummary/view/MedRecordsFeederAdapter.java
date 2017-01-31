/**
 * @(#) MedRecordsFeederAdapter.java
 * $Id: MedRecordsFeederAdapter.java,v 1.16 2005/02/17 10:05:57 lindahlf Exp $
 */

package medview.medsummary.view;

import java.awt.*;

import medview.datahandling.*;

import medview.medrecords.*;
import medview.medrecords.data.*;

import medview.medsummary.model.*;

/**
 * This specific implementation of the feeder
 * adapter interface uses the MedRecords
 * application for feeding.
 *
 * @author Fredrik Lindahl
 */
public class MedRecordsFeederAdapter implements FeederAdapter
{
	public boolean allowsShutDown()
	{
		return MedRecords.exitApplication();
	}

	public void initiateFeeder( Component ownerComp )
	{
		this.initiateFeeder(null, ownerComp);
	}

	public void initiateFeeder( PatientIdentifier pid, Component ownerComp )
	{
		if (model.usesRemoteDataHandling())
		{
			try
			{
				MedViewDataHandler.instance().setPCodeGeneratorToUse(

					"medview.datahandling.RemotePCodeGeneratorClient");

				/* NOTE: when setting a new pcode generator, if the examination
				   data location has been set before (which it has in this case
				   in MedSummary) the new pcode generator will have its data
				   location set to the previously set location. And in the case
				   of the remote pcode generator specified above, this location is
				   synonymous with the pcode nr generator location. Thus this is all
				   that has to be done here. */
			}
			catch (ClassNotFoundException exc)
			{
				exc.printStackTrace();

				System.exit(1); // fatal error
			}
			catch (InstantiationException exc)
			{
				exc.printStackTrace();

				System.exit(1);	// fatal error
			}
			catch (IllegalAccessException exc)
			{
				exc.printStackTrace();

				System.exit(1);	// fatal error
			}
		}
		else
		{
			if (PreferencesModel.instance().isPCodeNRGeneratorLocationSet())
			{
				MedViewDataHandler.instance().setPCodeNRGeneratorLocation(

					PreferencesModel.instance().getPCodeNRGeneratorLocation());

				/* NOTE: if the MedSummary application does not use remote
				   data handling, it is using a local datahandler that can
				   also be used in the feeder application. Thus, we already
				   have a pcode number generator set (in conjunction with the
				   idea that the same settings and handlers should be used in
				   the invoked application that is used in the invoker), so
				   all we have to do is to set the pcode number generator
				   location (if it has been set in the feeder application at
				   some time. */
			}
		}

		MedRecords.startMedRecords(pid, ownerComp, false); // false -> MR not owner
	}

	public MedRecordsFeederAdapter(MedSummaryModel model)
	{
		this.model = model;
	}

	private MedSummaryModel model;
}
