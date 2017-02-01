/**
 * @(#) EmptyFeederAdapter.java
 */

package medview.medsummary.view;

import java.awt.*;

import medview.datahandling.*;

/**
 * This specific implementation of the
 * FeederAdapter interface can be used
 * if you want to develop the
 * MedSummary application without considering
 * the development status of the feeder
 * application.
 *
 * @author Fredrik Lindahl
 */
public class EmptyFeederAdapter implements FeederAdapter
{
	public EmptyFeederAdapter( ) { }

	public boolean allowsShutDown() { return true; }

	public void initiateFeeder( Component comp ) { }

	public void initiateFeeder( PatientIdentifier pid, Component comp ) { }
}
