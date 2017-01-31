/**
 * @(#) FeederAdapter.java
 */

package medview.medsummary.view;

import java.awt.*;

import medview.datahandling.*;

/**
 * In order to decrease the coupling
 * between MedSummary and the feeder
 * application, this interface was
 * constructed.
 *
 * @author Fredrik Lindahl
 */
public abstract interface FeederAdapter
{
	/**
	 * Returns whether or not a shut down is 
	 * safe (i.e. whether or not a shutdown
	 * would cause data to be lost in the feeder).
	 * @return boolean whether or not a shutdown
	 * (a call to System.Exit()) would result in
	 * data being lost in the feeder.
	 */
	public abstract boolean allowsShutDown();
	
	/**
	 * Initiates the feeder without specifying
	 * a patient identifier. The actual feeder
	 * specifics are dealt with in the
	 * implementation classes.
	 *
	 * @param comp the component over which the
	 * feeder should appear (usually centered).
	 */
	public abstract void initiateFeeder( Component comp );

	/**
	 * Initiates the feeder using the specified
	 * patient identifier. The actual feeder
	 * specifics are dealt with in the various
	 * implementation classes.
	 * @param identifier the patient identifier
	 * that the feeder form is for.
	 * @param comp the component over which the
	 * feeder should appear (usually centered).
	 */
	public abstract void initiateFeeder( PatientIdentifier pid, Component comp );


}
