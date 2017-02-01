package minimed.gui.exam;


import minimed.MinimedModel;
import minimed.gui.GUIConstants;
import minimed.gui.MinimedController;
import minimed.gui.MinimedView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Widget;

/**
 * A controller for the exam view.
 * 
 * @author Joni Paananen
 */
public class ExamController extends MinimedController implements SelectionListener, GUIConstants {
	private ExamView view;
	
	/**
	 * Creates a StartController
	 * 
	 * @param pModel the model to base the view on.
	 * @param pDisplay The display to show the exam screen on.
	 */
	public ExamController(MinimedModel pModel, Display pDisplay){
		super(pModel, pDisplay);
	}

	/**
	 * Sets the view to be controlled.
	 * 
	 * @param pExamView the view to be set.
	 */
	public void setView(MinimedView pExamView) {
		view = (ExamView)pExamView;
	}
	
	/**
	 * Closes the exam screen.
	 */
	public void close() {
		model.flush();
		view.dispose();
	}
	
	/**
	 * Handles all events from the menu.
	 * 
	 * @param pEvent an event sent by the menu. 
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		Widget source = (Widget)pEvent.getSource();
		if (source.getData().equals("Spara och återgå")) {
			if (view.saveTree()) {
				switchView(JOURNAL);
			}
		} else if (source.getData().equals("Återgå utan att spara")) {
			MessageBox question = new MessageBox(view.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			question.setMessage(model.i18n("Are you sure you want to close the exam without saving?"));
			if (question.open() == SWT.YES) {
				switchView(JOURNAL);
			}
		} else if (source.getData().equals("Spara")) {
			view.saveTree();
		}
	}
	
	/**
	 * Not implemented
	 * 
	 * @param pEvent a selection event. 
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
}