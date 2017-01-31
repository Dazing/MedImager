package medview.medimager.model;

import java.util.*;

/**
 * Event class associated with the usabiliy model used in
 * the MedImager application.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class UsabilityModelEvent extends EventObject
{
	public void setFunctionalLayer(int functionalLayer)
	{
		this.functionalLayer = functionalLayer;
	}

	public int getFunctionalLayer()
	{
		return functionalLayer;
	}

	public void setFunctionalLayerState(boolean state)
	{
		this.functionalLayerState = state;
	}

	public boolean getFunctionalLayerState()
	{
		return functionalLayerState;
	}


	public UsabilityModelEvent(Object source)
	{
		super(source);
	}

	private int functionalLayer = -1;

	private boolean functionalLayerState = false;
}
