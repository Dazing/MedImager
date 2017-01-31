/**
 * @(#) UsabilityModel.java
 */

package medview.medimager.model;

import javax.swing.event.EventListenerList;

import java.util.*;

/**
 * Contains methods dealing with the usability model, which
 * encompasses layers and user levels.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class UsabilityModel
{
	// MEMBERS

	/*
	 * NOTE: if a layer indicating a higher level of expertise
	 * is activated, the underlying layers should be activated
	 * as well. This is why the constants are numbered in an
	 * increasing fashion connected to the level of expertise
	 * each constant represents. Higher layers than the one being
	 * set should also be deactivated.
	 */

	public static final int LAYER_SEARCH = 0;

	public static final int LAYER_STORE_AWAY = 1;

	public static final int LAYER_ORGANIZE = 2;

	public static final int LAYER_EDIT = 3;

	public static final int LAYER_SHARE = 4;


	public static final int LOWERMOST_LAYER = LAYER_SEARCH;

	public static final int UPPERMOST_LAYER = LAYER_SHARE;

	public static final int LAYER_DELTA = 1;


	private EventListenerList listenerList = new EventListenerList();

	private boolean[] functionalLayers = new boolean[5];


	// CONSTRUCTION AND INITIALIZATION

	/**
	 * Constructs the usability model.
	 */
	public UsabilityModel( )
	{
		initializeFunctionalLayers();
	}

	private void initializeFunctionalLayers()
	{
		functionalLayers[LOWERMOST_LAYER] = true;
	}


	// FUNCTIONAL LAYERS

	/**
	 * Returns the boolean value of the specified usability
	 * constant.
	 * @param c int the usability constant.
	 * @return boolean the value of the usability constant.
	 */
	public boolean isFunctionalLayerActive( int c )
	{
		if (isFunctionalLayerConstant(c))
		{
			return functionalLayers[c];
		}
		else
		{
			System.err.println("'" + c + "' is not a functional layer constant - returning false");

			return false;
		}
	}

	/**
	 * Sets the functional layer to use. It is the
	 * lead layer since all layers 'below' it are
	 * inherently active.
	 * @param c int the usability constant.
	 * @param v boolean the value to set.
	 */
	public void setFunctionalLeadLayer( int c)
	{
		Vector layersThatChanged = new Vector(); // for event firing

		if (isFunctionalLayerConstant(c))
		{
			if (functionalLayers[c])
			{
				// already active, should change?

				if (isFunctionalLayerConstant(c+1))
				{
					for (int ctr=(c+1); ctr<=UPPERMOST_LAYER; ctr++)
					{
						if (isFunctionalLayerActive(ctr))
						{
							functionalLayers[ctr] = false;

							layersThatChanged.add(new Integer(ctr));
						}
					}

					if (layersThatChanged.size() == 0)
					{
						// c is active layer, and layers above are all inactive - already set - no change
					}
					else
					{
						Enumeration enm = layersThatChanged.elements();

						while (enm.hasMoreElements())
						{
							fireFunctionalLayerStateChanged(((Integer)enm.nextElement()).intValue());
						}
					}
				}
				else
				{
					// c is uppermost layer and already active - no change
				}
			}
			else
			{
				for (int ctr=LOWERMOST_LAYER; ctr<=c; ctr++)
				{
					if (!isFunctionalLayerActive(ctr))
					{
						functionalLayers[ctr] = true;

						layersThatChanged.add(new Integer(ctr));
					}
				}

				Enumeration enm = layersThatChanged.elements();

				while (enm.hasMoreElements())
				{
					fireFunctionalLayerStateChanged(((Integer)enm.nextElement()).intValue());
				}
			}
		}
		else
		{
			System.err.println("'" + c + "' is not a functional layer constant - cannot set");
		}
	}

	/**
	 * Obtains the functional lead layer.
	 * @return int
	 */
	public int getFunctionalLeadLayer()
	{
		for (int ctr = LOWERMOST_LAYER; ctr <= UPPERMOST_LAYER; ctr++)
		{
			if (functionalLayers[ctr] == false)
			{
				return (ctr-1);
			}
			else if (ctr == UPPERMOST_LAYER)
			{
				return UPPERMOST_LAYER;
			}
		}

		return -1;
	}

	private boolean isFunctionalLayerConstant(int c)
	{
		return ((c >= LAYER_SEARCH) && (c <= LAYER_SHARE));
	}


	// EVENT LISTENERS AND FIRING

	public void addUsabilityModelListener(UsabilityModelListener l)
	{
		listenerList.add(UsabilityModelListener.class, l);
	}

	public void removeUsabilityModelListener(UsabilityModelListener l)
	{
		listenerList.remove(UsabilityModelListener.class, l);
	}

	protected void fireFunctionalLayerStateChanged(int c)
	{
		Object[] listeners = listenerList.getListenerList();

		UsabilityModelEvent event = new UsabilityModelEvent(this);

		event.setFunctionalLayer(c);

		event.setFunctionalLayerState(functionalLayers[c]);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == UsabilityModelListener.class)
			{
				((UsabilityModelListener)listeners[i+1]).functionalLayerStateChanged(event);
			}
		 }
	}


	// UNIT TEST METHOD

	public static void main(String[] args)
	{
		UsabilityModel model = new UsabilityModel();

		model.addUsabilityModelListener(new UsabilityModelListener()
		{
			public void functionalLayerStateChanged(UsabilityModelEvent e)
			{
				System.out.println("functionalLayerStateChanged() for layer '" +

					e.getFunctionalLayer() + "' - " + e.getFunctionalLayerState());
			}
		});

		model.setFunctionalLeadLayer(LAYER_SEARCH);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_SHARE);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_EDIT);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_SEARCH);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_SEARCH);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_SHARE);

		System.out.println("---------------------");

		model.setFunctionalLeadLayer(LAYER_SHARE);

		System.out.println("---------------------");
	}
}
