/*
 * AbstractInputComponent.java
 *
 * Created on den 4 augusti 2003, 14:32
 *
 * $Id: AbstractInputComponent.java,v 1.8 2010/07/01 19:30:44 oloft Exp $
 *
 * $Log: AbstractInputComponent.java,v $
 * Revision 1.8  2010/07/01 19:30:44  oloft
 * Added isRequired
 *
 * Revision 1.7  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.6  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.5  2005/02/17 10:25:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/12/08 14:42:56  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2003/11/27 23:44:02  oloft
 * Field sizing
 *
 * Revision 1.2  2003/11/11 13:52:32  oloft
 * Switching mainbranch
 *
 * Revision 1.1.2.4  2003/10/18 14:50:46  oloft
 * Builds tree file with new file names
 *
 * Revision 1.1.2.3  2003/10/14 11:55:13  oloft
 * Enabled Shift-Tab
 *
 * Revision 1.1.2.2  2003/09/09 13:56:15  erichson
 * Added isIdentification() method
 *
 * Revision 1.1.2.1  2003/09/08 13:18:26  erichson
 * First check-in
 *
 */

package medview.medrecords.components.inputs;

import java.util.*; // Vector

import javax.swing.*;

import medview.medrecords.components.*;
import medview.medrecords.events.*;
import medview.medrecords.interfaces.*;
import medview.medrecords.models.*;

/**
 * Base class for input components
 * @author Nils Erichson
 */
public abstract class AbstractInputComponent extends JPanel implements ValueInput
{
	protected InputModel inputModel;

	protected TabPanel parentTab;

	protected Vector inputFocusListeners;

	protected Set inputValueChangeListeners;

	protected InputContainerPanel inputContainerPanel = null;

	public AbstractInputComponent(InputModel inputModel)
	{
		super();

		this.inputModel = inputModel;

		inputFocusListeners = new Vector();

		inputValueChangeListeners = new HashSet();
	}

	public InputContainerPanel getInputContainerPanel()
	{
		return inputContainerPanel;
	}

	public void setInputContainerPanel(InputContainerPanel icp)
	{
		inputContainerPanel = icp;
	}

	public void addInputValueChangeListener(InputValueChangeListener ivcl)
	{
		inputValueChangeListeners.add(ivcl);
	}

	public void removeInputValueChangeListener(InputValueChangeListener ivcl)
	{
		inputValueChangeListeners.remove(ivcl);
	}

	protected void fireInputValueChanged()
	{
		for (Iterator it = inputValueChangeListeners.iterator(); it.hasNext(); )
		{
			( (InputValueChangeListener)it.next()).inputValueChanged(new InputValueChangeEvent(this));
		}
	}

	public medview.medrecords.models.PresetModel getPresetModel()
	{
		return inputModel.getPresetModel();
	}

	/**
	 * Whether this input is the identification field (for p-code etc).
	 */
	public boolean isIdentification()
	{
		return inputModel.isIdentification();
	}

	// Whether new values are allowed or not
	public boolean isEditable() {
		return inputModel.isEditable();
	}
	
	// Whether values are required or not
	public boolean isRequired() {
		return inputModel.isRequired() || inputModel.isIdentification();
	}
	
	public TabPanel getParentTab()
	{
		return parentTab;
	}

	public void setParentTab(TabPanel parentTab)
	{
		this.parentTab = parentTab;
	}

	public String getDescription()
	{
		return inputModel.getDescription();
	}
	public String getComment()
	{
		return inputModel.getComment();
	}
	public String getName()
	{
		return inputModel.getName();
	}

	protected void fireInputFocusChanged(InputFocusEvent ife)
	{
		for (Iterator it = inputFocusListeners.iterator(); it.hasNext(); )
		{
			( (InputFocusListener)it.next()).inputFocusGained(ife);
		}
	}

	public void addInputFocusListener(InputFocusListener ifl)
	{
		inputFocusListeners.add(ifl);
	}

	public void removeInputFocusListener(InputFocusListener ifl)
	{
		inputFocusListeners.remove(ifl);
	}

	public boolean isPatientIdentifierType()
	{
		return inputModel.isIdentification();
	}

	public void registerListeners(ApplicationFrame frame)
	{
		// empty default implementation.
	}

	protected abstract void gotoPreviousInput();

	protected abstract void gotoNextInput();
}
