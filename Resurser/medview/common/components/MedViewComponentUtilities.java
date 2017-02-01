package medview.common.components;

import java.awt.*;

import javax.swing.*;

/**
 * <p>Title: The MedView Project</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewComponentUtilities
{
	/**
	 * Returns the nearest Dialog ancestor, or null if there is no window
	 * ancestor or if the window ancestor exists but is not a Dialog.
	 * @param comp Component
	 * @return Dialog
	 */
	public static Dialog getClosestDialogAncestor(Component comp)
	{
		Window windowAncestor = SwingUtilities.getWindowAncestor(comp);

		if ((windowAncestor != null) && (windowAncestor instanceof Dialog))
		{
			return (Dialog) windowAncestor;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the nearest Frame ancestor, or null if there is no window
	 * ancestor or if the window ancestor exists but is not a Frame.
	 * @param comp Component
	 * @return Frame
	 */
	public static Frame getClosestFrameAncestor(Component comp)
	{
		Window windowAncestor = SwingUtilities.getWindowAncestor(comp);

		if ((windowAncestor != null) && (windowAncestor instanceof Frame))
		{
			return (Frame) windowAncestor;
		}
		else
		{
			return null;
		}
	}
}
