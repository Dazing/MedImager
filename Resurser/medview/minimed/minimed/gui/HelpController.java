package minimed.gui;


import minimed.MinimedModel;
import minimed.help.Question;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

/**
 * A controller for the help screen.
 * 
 * @author Jens Hultberg
 */
public class HelpController extends MinimedController implements SelectionListener, ModifyListener
	, GUIConstants {
	private HelpView view;
	
	/**
	 * Creates a HelpController object.
	 * 
	 * @param pModel the model that controls the behavior of the controller.
	 * @param pDisplay the display to be associated with the controller.
	 */
	public HelpController(MinimedModel pModel, Display pDisplay) {
		super(pModel, pDisplay);
	}

	/**
	 * Sets the view to be used.
	 * 
	 * @param pStartView the view to use (must be a StartView). 
	 */
	public void setView(MinimedView pStartView) {
		view = (HelpView)pStartView;
	}
	
	/**
	 * Closes the controller.
	 */
	public void close() {
		view.dispose();
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
  
	/**
	 * Handles ModifyEvents sent by the view. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void modifyText(ModifyEvent pEvent) {
		Text searchString = (Text)pEvent.getSource();
		view.selectTableItem(searchString.getText());
	}
	
	/**
	 * Returns to the start view.
	 */
	public void returnToStartView() {
		switchView(START);
	}
	
	/**
	 * Handles SelectionEvents sent by the view. 
	 * 
	 * @param pEvent an event sent by the view. 
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		/* When the source is the menu, pEvent.item is null */
		if(pEvent.item == null) {
			MenuItem i = (MenuItem)pEvent.getSource();
			String label = i.getText();
		
			if (label == model.i18n("Close")) {
				returnToStartView();
			}
		/*
		 * Otherwise it might be either a Tree (in that case
		 * we do nothing) or a TreeItem or a TableItem
		 */
		} else {
			Object o = pEvent.item.getData();
			if (o != null) {
				Question q = (Question)pEvent.item.getData();
				view.setArticle(q);
			}
		}
	}
}

