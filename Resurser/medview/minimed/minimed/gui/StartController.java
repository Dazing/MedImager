package minimed.gui;

import minimed.*;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

/**
 * A controller for the start screen.
 * 
 * @author Joni Paananen
 */
public class StartController extends MinimedController implements MouseListener, SelectionListener
	, GUIConstants {
	private StartView view;
	
	/**
	 * Creates a StartController
	 * @param d The display to show the start screen on.
	 */
	public StartController(MinimedModel m, Display d){
		super(m, d);
	}
	
	/**
	 * Sets the view to be controlled.
	 * 
	 * @param pStartView the view to be set.
	 */
	public void setView(MinimedView pStartView) {
		view = (StartView)pStartView;
	}
	
	/**
	 * Closes the starting screen
	 */
	public void close() {
		view.dispose();
	}
	
	/**
	 * Handles events from the labels.
	 * 
	 * @param pEvent an event sent by a labels.
	 */
	public void mouseDown(MouseEvent pEvent) {
		if (pEvent.button == 1) {
			switchView(view.getAction(pEvent.getSource()));
		}
	}
	
	/**
	 * Handles events from the menu.
	 * 
	 * @param pEvent an event sent by the menu.
	 */
	public void widgetSelected(SelectionEvent pEvent){
		if (pEvent.getSource() instanceof MenuItem) {
			MenuItem i = (MenuItem)pEvent.getSource();
			String name = i.getText();
			
			if (name.equals(model.i18n("Quit"))) {
				System.exit(0);
			} else if (name.equals(model.i18n("About MiniMed"))) {
				switchView(ABOUT);
			}
		}
	}
	
	/**
	 * Not implemented
	 * 
	 * @param pEvent a selection event. 
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
	
	/**
	 * Not implemented
	 * 
	 * @param pEvent a mouse event. 
	 */
	public void mouseUp(MouseEvent pEvent) {}
	
	/**
	 * Not implemented
	 * 
	 * @param pEvent a mouse event. 
	 */
	public void mouseDoubleClick(MouseEvent pEvent) {}
}
