/*
 * $Id: ValueInput.java,v 1.7 2010/07/01 19:30:17 oloft Exp $
 *
 * Created on July 20, 2001, 10:33 PM
 *
 * $Log: ValueInput.java,v $
 * Revision 1.7  2010/07/01 19:30:17  oloft
 * Added isRequired
 *
 * Revision 1.6  2010/06/22 15:29:29  oloft
 * Added isEditable
 *
 * Revision 1.5  2007/10/17 15:17:05  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.4  2005/02/17 10:26:04  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.2  2003/11/11 14:43:20  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.2  2003/10/18 14:50:47  oloft
 * Builds tree file with new file names
 *
 * Revision 1.1.2.1  2003/09/08 13:26:52  erichson
 * First check-in.
 *
 */

package medview.medrecords.interfaces;

import java.util.*;

import medview.datahandling.examination.tree.*;

import medview.medrecords.components.*;
import medview.medrecords.events.*;
import medview.medrecords.exceptions.*;
import medview.medrecords.models.*;

/**
 *
 * Interface for value inputs.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public interface ValueInput
{
	public String[] getValues();

	public void clearContents();
	
	public String getDescription();

	public String getName();

	// INPUT

	public void focusInput();

	public void addInputFocusListener(InputFocusListener ifl);

	public void removeInputFocusListener(InputFocusListener ifl);
	
	public void verifyInput() throws ValueInputException;

	// PRESETS

	public PresetModel getPresetModel();

	public void putPreset(String value);

    public void putCustomPreset(String key,String value);

    // EDITABLE / ENABLE etc

	public void setEditable(boolean editable);
	
	public boolean isEditable(); // ToDo: Now this means can have new values, is this correct?
	
	// Whether values are required or not
	public boolean isRequired();

	public void setEnabled(boolean b);

	public boolean isEnabled();

	// TREE

	public Tree getTreeRepresentation(Date date, String pCode);

	public void addInputValueChangeListener(InputValueChangeListener ivcl);

	public void removeInputValueChangeListener(InputValueChangeListener ivcl);

	public void registerListeners(ApplicationFrame frame);

}
