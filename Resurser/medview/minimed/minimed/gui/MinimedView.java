package minimed.gui;


import minimed.MinimedModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is the abstract superclass of all views in Minimed. 
 * 
 * @author Jens Hultberg
 */
public abstract class MinimedView extends Composite {
	protected Shell shell;
	protected MinimedModel model;
	protected MinimedController controller;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pShell the shell used by the application.
	 * @param pModel the model used by the application.
	 * @param pController the controller which controls this view.
	 */
	public MinimedView(Shell pShell, MinimedModel pModel, MinimedController pController) {
		super(pShell, SWT.NONE);
		shell = pShell;
		model = pModel;
		controller = pController;
		controller.setView(this);
	}
}