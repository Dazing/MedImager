package minimed;


import org.eclipse.swt.widgets.Display;
import minimed.gui.GUIHandler;

/**
 * Starts up the entire framework, piece by piece.
 *
 * @author Andreas Andersson
 */
public class MinimedMain {
	
    public static void main(String[] args) {
        /* Create the display to use throughout the application */
        Display display = new Display();

        /* Initialize the minimed.gui */
        GUIHandler gui = new GUIHandler(display);
    }
}