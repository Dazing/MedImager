/**
 * @(#) MedImagerModelEvent.java
 */

package medview.medimager.model;

import java.util.*;

/**
 * An event object describing an event originating from
 * the MedImager main model facade.
 */
public class MedImagerModelEvent extends EventObject
{
	public MedImagerModelEvent( Object source )
	{
		super(source);
	}
}
