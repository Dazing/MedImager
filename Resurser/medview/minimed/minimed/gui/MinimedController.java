package minimed.gui;


import java.util.Observable;

import minimed.MinimedModel;

import org.eclipse.swt.widgets.Display;

/**
 * This class is the abstract superclass of all controllers in Minimed.
 * 
 * @author Jens Hultberg
 */
public abstract class MinimedController extends Observable implements GUIConstants {
	protected MinimedModel model;
	protected Display display;
	private int action = ILLEGAL;

	/**
	 * Sets the view. 
	 * 
	 * @param pView the view to be associated with the controller.
	 */
    public abstract void setView(MinimedView pView);
    
	/**
	 * Creates a new MinimedController object. 
	 * @param pModel The model.
	 */
	public MinimedController(MinimedModel pModel, Display pDisplay) {
		model = pModel;
		display = pDisplay;
	}

	/**
	 * Switches start view.
	 * 
	 * @param pView the view to switch to.
	 */
	public void switchView(int pView) {
		action = pView;
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	/**
	 * Closes the controller.
	 */	
	public abstract void close();
	
	/**
	 * Used by GUIHandler to get the action requested by this controller. 
	 * 
	 * @return the requested action. 
	 */
	public int getAction() {
		return action;
	}
	
	/**
	 * Returns the model.
	 * @return The model.
	 */
	public MinimedModel getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 * @param model The model to set.
	 */
	public void setModel(MinimedModel model) {
		this.model = model;
	}
}
