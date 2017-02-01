package minimed.gui;


import minimed.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

import java.io.*;
import java.util.Observable;

/**
 * A view for the start screen.
 * 
 * @author Joni Paananen
 */
public class StartView extends MinimedView implements GUIConstants {
	public static String QUIT = "Avsluta", ABOUT_MINIMED = "Om MiniMed";
	private GridLayout layout;
	private Image[] images;
	private Label[] buttons;
	private Menu menubar;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param pShell the shell used by the application.
	 * @param pModel the model used.
	 * @param pController the controller to be associated with the view.
	 */
	public StartView(Shell pShell, MinimedModel pModel, MinimedController pController){
		super(pShell, pModel, pController);
		
		layout = new GridLayout(3, true);
		layout.marginTop = 50;
		this.setLayout(layout);
		
		setImages();
		setButtons();
		setLabels();
		
		menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);
		MenuItem close = new MenuItem(menubar, SWT.PUSH);
		close.setText(pModel.i18n("Quit"));
		close.addSelectionListener((SelectionListener)controller);
		
		MenuItem about = new MenuItem(menubar, SWT.PUSH);
		about.setText(pModel.i18n("About MiniMed"));
		about.addSelectionListener((SelectionListener)controller);
	}
	
	/**
	 * Returns the wanted action.
	 *   
	 * @param pObject an object. 
	 * @return the desired action, as defined in GUIConstants.
	 */
	public int getAction(Object pObject){
		if(pObject == buttons[0]) {
			return JOURNAL;
		} else if(pObject == buttons[1]) {
			return SETTINGS;
		} else if(pObject == buttons[2]) {
			return HELP;
		} else {
			return ILLEGAL;
		}
	}
	
	/**
	 * Disposes the view. 
	 */
	public void dispose() {
		for(int i = 0; i < images.length; i++){
			images[i].dispose();
		}
		super.dispose();
		menubar.dispose();
	}

	/**
	 * Not implemented
	 */
	public void update(Observable o, Object arg) {}
	
	/**
	 * Sets all images. 
	 */
	private void setImages() {
		images = new Image[3];
		InputStream is = getClass().getResourceAsStream("bilder/icon_folder.gif");
		images[0] = new Image(shell.getDisplay(), is);
		is = getClass().getResourceAsStream("bilder/icon_settings.gif");
		images[1] = new Image(shell.getDisplay(), is);
		is = getClass().getResourceAsStream("bilder/icon_help.gif");
		images[2] = new Image(shell.getDisplay(), is);
	}
	
	/**
	 * Sets all buttons.
	 */
	private void setButtons() {
		buttons = new Label[3];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Label(this, SWT.NONE);
			buttons[i].setImage(images[i]);
			center(buttons[i]);
			buttons[i].addMouseListener((MouseListener)controller);
		}
	}
	
	/**
	 * Sets all labels.
	 */
	private void setLabels() {
		Label l1 = new Label(this, SWT.CENTER);
		Label l2 = new Label(this, SWT.CENTER);
		Label l3 = new Label(this, SWT.CENTER);

		center(l1);
		center(l2);
		center(l3);

		l1.setText(model.i18n("Journals"));
		l2.setText(model.i18n("Settings"));
		l3.setText(model.i18n("Help"));
	}
	
	/**
	 * Centers the given control.
	 * 
	 * @param pControl the control to center.
	 */
	private void center(Control pControl){
		GridData data = new GridData();
		data.horizontalAlignment = SWT.CENTER;
		pControl.setLayoutData(data);
	}
}
