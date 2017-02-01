package minimed.gui;


import minimed.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * A controller for the about screen.
 * 
 * @author Andreas Andersson
 */
public class AboutController extends MinimedController implements SelectionListener, GUIConstants {
	private AboutView view;
	
	/**
	 * Creates an instance of an AboutController
	 * 
	 * @param pModel the model that controls the behavior of the controller.
	 * @param pDisplay the display to be associated with the controller.
	 */
	public AboutController(MinimedModel pModel, Display pDisplay) {
		super(pModel, pDisplay);
	}

	/**
	 * Sets the view to be used.
	 * 
	 * @param pStartView The view to use (must be a StartView). 
	 */
	public void setView(MinimedView pStartView) {
		view = (AboutView)pStartView;
	}
	
	/**
	 * Closes the controller.
	 */
	public void close() {
		view.dispose();
	}

	/**
	 * Returns the user requested action.
	 */
	public int getAction(){
		return START;
	}
	
	/**
	 * Handles SelectionEvents sent by the view. 
	 * 
	 * @param pEvent an event sent by the view. 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		if(pEvent.getSource() instanceof MenuItem){
			MenuItem i = (MenuItem)pEvent.getSource();
			String name = i.getText();
			
			if(name.compareTo(model.i18n("Close")) == 0){
				switchView(START);
			}
		}
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
}

