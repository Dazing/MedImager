/*
 * $Id: TabPanel.java,v 1.25 2010/07/01 08:12:14 oloft Exp $
 *
 * Created on June 21, 2001, 8:07 PM
 *
 * Contains editables.
 *
 */

package medview.medrecords.components;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

import medview.medrecords.exceptions.*;
import medview.medrecords.events.*;
import medview.medrecords.models.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.data.PreferencesModel;


public class TabPanel extends JPanel implements InputFocusListener, ChangeListener,
	ActionListener, MedViewLanguageConstants
{

	private CategoryModel model;

	private ValueInputComponent selectedInput;

	private Vector inputComponents;

	private ValueTabbedPane parentTabbedPane;

	private Vector changeListeners;

	private Vector actionListeners;

	private JScrollPane scrollPane;

	public TabPanel(CategoryModel catModel, ValueTabbedPane in_parentTabbedPane)
	{
		this(catModel, in_parentTabbedPane, false);
	}

	public TabPanel(CategoryModel catModel, ValueTabbedPane in_parentTabbedPane, boolean pDesignable)
	{
		super();

		setRequestFocusEnabled(false);

		inputComponents = new Vector();
		
		parentTabbedPane = in_parentTabbedPane;

		this.setLayout(new BorderLayout());

		setModel(catModel);
		
		resetSelectedInput();
	}

	public void addActionListener(ActionListener newListener)
	{
		if (actionListeners == null)
		{
			actionListeners = new Vector();
		}

		if (!actionListeners.contains(newListener))
		{
			actionListeners.add(newListener);
		}
	}

	public void addChangeListener(ChangeListener newListener)
	{
		if (changeListeners == null)
		{
			changeListeners = new Vector();
		}

		if (!changeListeners.contains(newListener))
		{
			changeListeners.add(newListener);
		}
	}

	public void resetAllComponents()
	{
		ValueInputComponent[] inputs = getInputComponents();
		
		for (int i = 0; i < inputs.length; i++)
		{
			inputs[i].clearContents();
		}
	}


	public void fireActionEvent(ActionEvent ev)
	{
		for (Iterator it = actionListeners.iterator(); it.hasNext();)
		{
			ActionListener nextListener = (ActionListener)it.next();
			
			nextListener.actionPerformed(ev);
		}
	}

	public void fireStateChanged()
	{
		if (changeListeners == null)
		{
		}
		else if (changeListeners.size() == 0)
		{
		}
		else
		{
			for (Iterator it = changeListeners.iterator(); it.hasNext(); )
			{
				ChangeListener nextListener = (ChangeListener)it.next();

				nextListener.stateChanged(new ChangeEvent(this));
			}
		}
	}

	public void inputFocusGained(InputFocusEvent e)
	{
		ValueInputComponent newInput = e.getNewInput();

		if (newInput != selectedInput)
		{
			setSelectedInputComponent(newInput);
		}
	}

	public ValueInputComponent[] getInputComponents()
	{
		ValueInputComponent[] inputs = new ValueInputComponent[inputComponents.size()];
		
		inputs = (ValueInputComponent[])inputComponents.toArray(inputs);
		
		return inputs;
	}

	public ValueInputComponent getFirstInputComponent()
	{
		ValueInputComponent[] components = getInputComponents();
		
		if (components.length <= 0)
		{
			return null;
		}
		else
		{
                    int index = 0;

                    // Find first input that's not an InfoInput
                    while (index < components.length) {
                        if (! (components[index] instanceof InfoInput)) {
                            return components[index];
                        }
                        else {
                            index++;
                        }

                    }
                    // If we get here there were only InfoInputs
			return components[0];
		}
	}

	public ValueInputComponent getPreviousInputComponent(ValueInputComponent vic)
	{
		return getInputBefore(vic);
	}

	public ValueInputComponent getNextInputComponent(ValueInputComponent vic)
	{
		return getInputAfter(vic);
	}

	public ValueTabbedPane getParentPane()
	{
		return parentTabbedPane;
	}

	public ValueInputComponent getInputBefore(ValueInputComponent input)
	{
		if (input == null)
		{
			return null;
		}
		else
		{
			int index = inputComponents.indexOf(input);

			if ( (index < 0) )
			{
			}
			else
			{
                ValueInputComponent nextUp;
                do
                {
                    index--;
                    if(index < 0)
                    {
                        return null;
                    }
                    nextUp = (ValueInputComponent)inputComponents.get(index);
                }
                while((nextUp instanceof InfoInput));
                return nextUp;
			}

			return null; // none found
		}
	}

	public ValueInputComponent getInputAfter(ValueInputComponent input)
	{
		if (input == null)
		{
			return null;
		}
		else
		{
			int index = inputComponents.indexOf(input);

			if ( (index < 0) )
			{
			}
			else
			{
                ValueInputComponent nextUp;
                do
                {
                    index++;
                    if(index > (inputComponents.size() - 1))
                    {
                        return null;
                    }
                    nextUp = (ValueInputComponent)inputComponents.get(index);
                }
                while((nextUp instanceof InfoInput));
                return nextUp;
			}

			return null; // none found
		}
	}

	public ValueInputComponent getSelectedInputComponent()
	{
		return selectedInput;
	}

	/**
	 * Scrolls to the selected container, or more specifically the 
	 * inputcontainer (border with term name) for the input. This
	 * method is called each time the tab changes.
	 */
	public void scrollToSelectedComponent()
	{
		if (selectedInput != null)
                {
                    scrollToPanel(selectedInput.getInputContainerPanel()); // all valueInputComponents are JPanels now
                }
	}

	public void scrollToPanel(JPanel slctComp)
	{
		JViewport viewport = scrollPane.getViewport(); // the visible view
		
		Rectangle vwRect = viewport.getBounds();

		if (slctComp != null)
		{
			vwRect.x = 0;
			
			vwRect.y = 0;

			JComponent aP = (JComponent)viewport.getView();
			
			slctComp.scrollRectToVisible(vwRect);
		}
		else
		{
			System.err.println("TabPanel scrolling failed: slctComp is null");
		}
	}

	private void rebuild() // rebuild all components from the models
	{
		inputComponents = new Vector();
		
		// set up main panel
		
		JPanel fieldPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		setName(model.getTitle());
		
		removeAll();
		
		// obtain input models
		
		InputModel[] inputs = model.getInputs();

		// gridbagconstraints common to all input components

		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridheight = 1;
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		for (int i = 0; i < inputs.length; i++)
		{
			gbc.gridy = i;
			
			InputModel inputModel = inputs[i];

			ValueInputComponent inputComponent = null;
			
			try
			{
				inputComponent = InputFactory.createInput(inputModel);
				
				inputComponent.setParentTab(this);
	
				inputComponent.addInputFocusListener(this);
	
				inputComponent.registerListeners(parentTabbedPane.getApplicationFrame());
	
				inputComponent.addInputValueChangeListener(getParentPane().getApplicationFrame());
	
				inputComponents.add(inputComponent);
			}
			catch (UnknownTypeException ute)
			{
				JOptionPane.showMessageDialog(this, "Error in rebuild: " + ute.getMessage(),
					
					"TabPanel.rebuild()", JOptionPane.ERROR_MESSAGE);
				
				return;
			}

			InputContainerPanel containerPanel = InputContainerFactory.createContainerPanel(inputComponent);
			
			inputComponent.setInputContainerPanel(containerPanel);

			containerPanel.setTabPanel(this);
			
			/*if (i == inputs.length - 1)
			{
				gbc.gridheight = GridBagConstraints.RELATIVE;
			}*/
			
			fieldPanel.add(containerPanel, gbc);
		}
		
		gbc.weighty = 1;
		gbc.gridy = inputs.length;
		
		fieldPanel.add(Box.createGlue(), gbc);

		scrollPane = new JScrollPane(fieldPanel);

		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); - man vill inte ha denna scroll

		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		
		scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
		
		this.add(scrollPane, BorderLayout.CENTER);
		
		selectedInput = null;
		
		validate();
	}

	public void resetSelectedInput()
	{
		ValueInputComponent firstInput = getFirstInputComponent();
                
                if (firstInput != null) 
                    setSelectedInputComponent(firstInput);
                else
                    System.out.println("Warning: There are no input components in the tab...");
	}

	public boolean isPhotoTab()
	{
		if (model == null)
		{
			return false;
		}
		
		InputModel[] inputs = model.getInputs();

		if (inputs == null)
		{
			return false;
		}
		
		if (inputs.length < 1)
		{
			return false;
		}
		
		if (inputs[0] == null)
		{
			return false;
		}
		
		InputModel inputModel = inputs[0];
		if (inputModel instanceof TraumaModel)
		{
            return true;
        }
		if (inputModel instanceof MineralizationModel)
		{
            return true;
        }
		if (inputModel instanceof ErosionModel)
		{
            return true;
        }
        if (inputModel instanceof PictureChooserModel)
		{
			ValueInputComponent aCom = this.getSelectedInputComponent();
			
			if (aCom instanceof PictureChooserInput)
			{
				((PictureChooserInput)aCom).viewPicturePath(PreferencesModel.instance().getImageInputLocation());
			}
			
			return true;
		}
		
		return false;
	}

	public void setDesignable(boolean in_designable)
	{
		// not used here
	}

	public void setSelectedInputComponent(ValueInputComponent input)
	{
		if (input != null)
                {
                        selectedInput = input;

                        fireStateChanged();

                        input.focusInput();
                }
                else
                {
                        System.out.println("Debug: Warning: Tried to focus null component");
                }
	}

	public void focusSelectedInput()
	{
		ValueInputComponent input = getSelectedInputComponent();
                if (input != null)
                    input.focusInput();
	}

	public CategoryModel getModel()
	{
		return model;
	}

	public void setModel(CategoryModel in_tabModel)
	{
		if (model != null)
		{
			model.removeChangeListener(this);
		}

		model = in_tabModel;
		
		rebuild();

		model.addChangeListener(this);
	}

	public void stateChanged(javax.swing.event.ChangeEvent evt)
	{
		Object source = evt.getSource();

		if (source instanceof CategoryModel)
		{
			rebuild(); // Rebuild GUI if category definition changes
		}
	}

	/**
	 * Call this method to detach the panel as a listener from
	 * everything it listens to.
	 */
	public void detachAsListener()
	{		
		// remove this from examination model

		if (model != null) // Fredrik 041207
		{
			model.removeChangeListener(this);
		}
		
		// find the picture chooser input and remove it as listener from its model
		
		Enumeration enm = inputComponents.elements();
		
		while (enm.hasMoreElements())
		{
			Object next = enm.nextElement();
			
			if (next instanceof PictureChooserInput)
			{
				((PictureChooserInput)next).detachAsListener();
			}
		}
	}

	/**
	 * The TabPanel listens to its input components for choices.
	 * For example, PictureChooserInputs fire ActionEvents when 
	 * pictures are chosen.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		Object source = e.getSource();

		if (source instanceof PictureChooserInput)
		{
			parentTabbedPane.addPhotoPanelImage(e.getActionCommand()); // the action command contains the path to the image to be added
		}
	}

	/**
	 * Clear all values for all inputs in this tab
	 */
	public void clearValues()
	{
		for (Iterator it = inputComponents.iterator(); it.hasNext(); )
		{
			ValueInputComponent nextInputComponent = (ValueInputComponent)it.next();
			
			nextInputComponent.clearContents();
		}
	}

	/**
	 * Checks the input values for all ValueInputComponents in this tab. // NE
	 * Returns normally if they are ok, otherwise throws ValueInputException
	 * @throws ValueInputException if one or more input values is incorrect
	 */
	public void checkInputValues() throws ValueInputException
	{
		for (Iterator it = inputComponents.iterator(); it.hasNext(); )
		{
			ValueInputComponent nextInputComponent = (ValueInputComponent)it.next();

			try
			{
				nextInputComponent.verifyInput();
			}
			catch (ValueInputException vie)
			{
				String prefix = MedViewDataHandler.instance().getLanguageString(

					ERROR_MISSING_REQUIRED_INPUT_VALUE);

				throw new ValueInputException(prefix + " (" + this.getModel().getTitle() + " / " +

					nextInputComponent.getName() + ")", nextInputComponent, this);
			}
		}
	}

	public Tree getTreeRepresentation(String pid, Date date, String pCode)
	{
		Tree rootNode = new TreeBranch(getName());
		
		ValueInputComponent[] inputComponents = getInputComponents();
		
		for (int i = 0; i < inputComponents.length; i++)
		{
			rootNode.addChild(inputComponents[i].getTreeRepresentation(date, pCode));
		}
		
		return rootNode;
	}
}

/*
 * $Log: TabPanel.java,v $
 * Revision 1.25  2010/07/01 08:12:14  oloft
 * getFirstInputComponent returns first non-info input
 *
 * Revision 1.24  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.23  2008/01/31 13:23:26  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.22  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.21  2005/07/18 13:31:02  erichson
 * Added null checking to a bunch of methods, since NullPointerException would be thrown if a tab without inputs exists.
 *
 * Revision 1.20  2005/02/17 10:05:15  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.19  2005/01/30 15:19:20  lindahlf
 * T4 Integration
 *
 * Revision 1.18  2004/12/08 14:42:52  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.17  2004/11/16 21:24:04  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.16  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.15  2003/11/11 13:49:15  oloft
 * Switching mainbranch
 *
 * Revision 1.14.2.11  2003/10/24 21:06:50  oloft
 * Debug edits
 *
 * Revision 1.14.2.10  2003/10/21 06:25:51  oloft
 * *** empty log message ***
 *
 * Revision 1.14.2.9  2003/10/20 14:43:01  oloft
 * File transfer
 *
 * Revision 1.14.2.8  2003/10/18 14:50:45  oloft
 * Builds tree file with new file names
 *
 * Revision 1.14.2.7  2003/10/17 10:57:03  oloft
 * File transfer
 *
 * Revision 1.14.2.6  2003/10/14 11:55:12  oloft
 * Enabled Shift-Tab
 *
 * Revision 1.14.2.5  2003/10/10 15:57:30  oloft
 * no changes
 *
 * Revision 1.14.2.4  2003/09/08 13:17:20  erichson
 * Just javadoc
 *
 * Revision 1.14.2.3  2003/09/03 23:07:59  erichson
 * Fixed scrollToSelectedComponent so that it scrolls to the inputContainer cleanly. Fixes bugzilla bug 164.
 *
 * Revision 1.14.2.2  2003/08/16 14:40:24  erichson
 * cleaned up and unified addInput() method
 *
 * Revision 1.14.2.1  2003/08/14 12:04:58  erichson
 * Changed input handling methods, overhauled the listener system
 *
 * Revision 1.14  2003/07/23 14:34:47  erichson
 * added focusSelectedInput() method, and calling this from setSelectedInputComponent()
 *
 * Revision 1.13  2003/07/22 16:52:28  erichson
 * Improved error reporting when validating inputs: Some methods liked to return null, they are now changed to throw ValueInputExceptions.
 *
 */
