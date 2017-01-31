package minimed.gui;


import minimed.*;
import minimed.gui.exam.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is used for setting and switching controllers 
 * and views for Minimed.
 * 
 * @author Jens Hultberg
 */
public class GUIHandler implements Observer, GUIConstants {
	private MinimedModel model;
	private MinimedController controller;
	private MinimedView view;
	private Display display;
	private Shell shell;
	private GridData gridData;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pDisplay the display to use. 
	 */
	public GUIHandler(Display pDisplay) {
		display = pDisplay;

		/* Creates and shows a splash screen. */
		shell = new Shell(display, SWT.NO_TRIM);
		InputStream is = getClass().getResourceAsStream("bilder/logo.jpg");
		Image logo = new Image(display, is);
		ImageData imdata = logo.getImageData();
		shell.setSize(imdata.width, imdata.height);
		Rectangle r = display.getBounds();
		int shellX = (r.width - imdata.width) / 2;
		int shellY = (r.height - imdata.height) / 2;
		shell.setLocation(shellX, shellY);
		shell.open();
		GC gc = new GC(shell);
		gc.drawImage(logo,0,0);
		
		/* Do as much work as possible while the splash screen is showing. */
		model = new MinimedModel();
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridData = new GridData(GridData.FILL_BOTH);
		controller = new StartController(model, display);
		controller.addObserver(this);
		
		/* Removes the splash screen. */
		gc.dispose();
		logo.dispose();
		shell.dispose();

		/* Creates a new shell and starts the rest of the program. */
		shell = new Shell(display, SWT.RESIZE);
		shell.setText("MiniMed");
		shell.setLayout(gridLayout);
		//shell.setBounds(200,200,240,320); // For testing only! remove this line!!!!!
		view = new StartView(shell, model, (StartController)controller);
		view.setLayoutData(gridData);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	/**
	 * Used for switching screen. 
	 * 
	 * @param pScreen the screen to switch to, as defined in GUIConstants.
	 */
	private void switchScreen(int pScreen) {
		if (pScreen == START) {
			controller.close();
			
			controller = new StartController(model, display);
			controller.addObserver(this);
			view = new StartView(shell, model, controller);
			view.setLayoutData(gridData);
			
			shell.layout();
		} else if (pScreen == JOURNAL) {
			controller.close();
			
			controller = new JournalController(model, display);
			controller.addObserver(this);
			view = new JournalView(shell, model, (JournalController)controller);
			view.setLayoutData(gridData);
			
			shell.layout();
		} else if (pScreen == SETTINGS) {
			controller.close();

			controller = new SettingsController(model, display);
			controller.addObserver(this);
			view = new SettingsView(shell, model, (SettingsController)controller);
			view.setLayoutData(gridData);
			
			shell.layout();
		} else if (pScreen == EXAM) {
			controller.close();

			controller = new ExamController(model, display);
			controller.addObserver(this);
			view = new ExamView(shell, model, (ExamController)controller);
			
			/* Construction of ExamView may fail, in which case it is disposed at once. */
			if (!view.isDisposed()) {
				view.setLayoutData(gridData);
			}
			
			shell.layout();
		} else if (pScreen == HELP) {
			controller.close();

			controller = new HelpController(model, display);
			controller.addObserver(this);
			view = new HelpView(shell, model, (HelpController)controller);
			
			/* Construction of HelpView may fail, in which case it is disposed at once. */
			if (!view.isDisposed()) {
				view.setLayoutData(gridData);
			}
			
			shell.layout();
		} else if (pScreen == ABOUT) {
			controller.close();
	
			controller = new AboutController(model, display);
			controller.addObserver(this);
			view = new AboutView(shell, model, (AboutController)controller);
			
			view.setLayoutData(gridData);
			shell.layout();
		}
	}
	
	/**
	 * Called by the controller when switching screen.
	 */
	public void update(Observable o, Object arg) {
		int screen = ((MinimedController)o).getAction();
		switchScreen(screen);
	}
}
