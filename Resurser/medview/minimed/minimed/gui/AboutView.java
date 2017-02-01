package minimed.gui;


import minimed.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * A view for the about sceen 
 * 
 * @author Andreas Andersson
 */
public class AboutView extends MinimedView {
	private GridLayout gridLayout;
	private GridData gridInput;
	private Menu menu;
	private Label description;
	private Label comment;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pShell the shell to use for showing the view
	 * @param pModel the model to base the view on
	 * @param pController the controller related to the view
	 */	
	public AboutView(Shell pShell, MinimedModel pModel, AboutController pController) {
		super(pShell, pModel, pController);
		
		gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 1;
		this.setLayout(gridLayout);
		
		/* Print out the about text */
		description = new Label(this, SWT.BOLD);
		description.setText("MiniMed (rev. " + pModel.getMinimedRevision() + ")");
		description.setFont(getStyledFont(this, SWT.BOLD));
		gridInput = new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING);
		gridInput.heightHint = 250;
		comment = new Label(this, (SWT.WRAP));
		comment.setText(pModel.i18n("MiniMed was developed as a D3 project during the school year of 2005 and 2006 at Chalmers University of Technology, under the guidance of Olof Torgersson.") + "\n\n" 
				+ pModel.i18n("The members of the project group, in alphabetical order:") + "\n"
				+ " Andreas Andersson (xanan)\n Andreas Johansson (torgin)\n Andreas Nilsson (andreasn)\n Jens Hultberg (jenhul)\n Jonas Hadin (di3hadj)\n Joni Paananen (paananen)\n Markus Johansson (majohans)\n Mikael Lindström (limpan)");
		comment.setLayoutData(gridInput);

		/* Build the program menu */
		buildMenu();
	}
	

	/**
	 * Disposes the view, the menubar and all children to the view
	 */
	public void dispose() {
		super.dispose();
		menu.dispose();
	}
	
	/**
	 * Builds the menu.
	 */
	private void buildMenu(){
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText(model.i18n("Menu"));
		file.setMenu(fileMenu);
		
		MenuItem close = new MenuItem(fileMenu, SWT.PUSH);
		close.setText(model.i18n("Close"));
		close.addSelectionListener((AboutController)controller);
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
	
    /**
     * Creates a font that fits the current OS, and has a certain style.
     * 
     * @param pControl a control to get the font data from
     * @param pStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>
     * @return the font created with the given data
     */
	private Font getStyledFont(Control pControl, int pStyle) {
		FontData[] fd = pControl.getFont().getFontData();
		for(int i = 0; i < fd.length; i++) {
			fd[i].setStyle(pStyle);
		}
		Font result = new Font(pControl.getDisplay(), fd);
		return result;
	}
}