package minimed.gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import minimed.*;

/**
 * A controller for the journal browsing sceen.
 * 
 * @author Joni Paananen
 */
public class JournalController extends MinimedController implements SelectionListener, GUIConstants {
	private JournalView view;
	
	/**
	 * Creates a new JournalController
	 */
	public JournalController(MinimedModel pModel, Display pDisplay){
		super(pModel, pDisplay);
	}
	
	/**
	 * Sets the view.
	 * 
	 * @param pStartView the view to set. 
	 */
	public void setView(MinimedView pStartView) {
		view = (JournalView)pStartView;
	}
	
	/**
	 * Handles SelectionEvents.
	 *
	 * @param pEvent an event sent by the menu.
	 */
	public void widgetSelected(SelectionEvent pEvent){
		if (pEvent.getSource() instanceof MenuItem) {
			MenuItem i = (MenuItem)pEvent.getSource();
			String name = i.getText();
			
			if (name.equals(model.i18n("Close"))) {
				switchView(START);
			} else if (name.equals(model.i18n("Add Exam"))) {
				boolean canSave = true;
				try {
					model.getSaveTreeFileHandler();
				} catch (Exception e) {
					canSave = false;
				}
				if (!canSave) {
					MessageBox error = new MessageBox(display.getActiveShell(), SWT.ICON_ERROR | SWT.OK);
					error.setMessage(model.i18n("The exam save dir is not properly configured!"));
					error.open();
				} else {
					switchView(EXAM);
				}
			} else if (name.equals(model.i18n("Delete"))) {
				view.deleteCurrentExam();
			} else if (name.equals(model.i18n("Open"))) {
				if (view.chooseCurrentExam(false)) {
					switchView(EXAM);
				}
			} else if (name.equals(model.i18n("Open As New"))) {
				if (view.chooseCurrentExam(true)) {
					switchView(EXAM);
				}
			}
		}
	}
	
	/**
	 * Not implemented.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
	
	/**
	 * This method closes the controller.
	 */	
	public void close(){
		model.disposeDatabaseTreeFileHandler();
		model.disposeSaveTreeFileHandler();
		view.dispose();
	}
}
