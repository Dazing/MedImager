/*
 * ApplicationFrame.java
 *
 * Created on den 13 augusti 2003, 12:32
 *
 * $Log: ApplicationFrame.java,v $
 * Revision 1.4  2004/12/08 14:42:50  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2004/10/01 16:39:50  lindahlf
 * no message
 *
 * Revision 1.2  2003/11/11 13:35:49  oloft
 * Switching mainbranch
 *
 * Revision 1.1.2.1  2003/09/08 13:20:03  erichson
 * First check-in
 *
 */

package medview.medrecords.components;

import medview.medrecords.events.*;

import misc.gui.components.*;

/**
 * Base class of MedRecordsFrame and FormEditor.
 *
 * To extend this class:
 *   override imageChosen() if you want to receive imageChoice events.
 *
 * @author  nix
 */
public abstract class ApplicationFrame extends MainShell implements
	InputValueChangeListener, ImageChoiceListener
{
	public ApplicationFrame()
	{
		super();
	}

	/**
	 * Override this if you are interested in receiving 
	 * image choices. MedRecordsFrame wants to, for example.
	 */
	public void imageChosen(ImageChoiceEvent ice) { }

	public abstract PhotoPanel getPhotoPanel();

	public abstract void showTranslationEditor(medview.medrecords.models.PresetModel presetModel, String newValue);

}
