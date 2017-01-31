/*
 * $Id: ValueTabbedPane.java,v 1.2 2004/11/04 12:04:39 lindahlf Exp $
 *
 * Created on June 15, 2001, 12:08 PM
 *
 */

package medview.formeditor.components;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Color.*;

import medview.formeditor.models.*;
import medview.formeditor.components.*;
import medview.formeditor.data.*;
import medview.formeditor.exceptions.*;
import medview.formeditor.interfaces.*;
import medview.formeditor.tools.*;

/**
 * A class for a tabbed pane with value fields. ChangeEvents are dispatched when
 * the selected field or tab is changed.
 *
 * @author  nils
 * @version
 *
 */

public class ValueTabbedPane extends JTabbedPane implements ActionListener, ChangeListener
{
	// MEMBERS
	
	private static final boolean debug = false;

	private ExaminationModel mModel;

	private PresetListPanel presetListPanel = null;

	private boolean designable;

	private LinkedHashMap tabTable;

	private Color mColor;

	private int mPrevIndex;

	private JSplitPane mSplitPhoto;

	private JSplitPane mSplitPresets;

	private int mPresetsListPanelW = 0;

	private DatahandlingHandler mDH = DatahandlingHandler.getInstance();

	// CONSTRUCTORS

	public ValueTabbedPane()
	{
		this(false);
	}

	public ValueTabbedPane(boolean pDesignable)
	{
		super(JTabbedPane.LEFT);
		
		designable = pDesignable;

		String jVr = System.getProperty("java.version");

		if (jVr.startsWith("1.4"))
		{
			this.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		
		// The tabbedPane should listen to itself for tab changes
		
		addChangeListener(this);
		
		mColor = this.getBackground();
	}

	public ValueTabbedPane(ExaminationModel in_model)
	{
		this();
		
		setModel(in_model);
	}

	public void setSplitPhoto(JSplitPane pLetfPanel)
	{
		mSplitPhoto = pLetfPanel;
	}

	public void setSplitPresets(JSplitPane pContentPanel)
	{
		mSplitPresets = pContentPanel;
	}

	/**
	 * Receive ActionEvents (list selections) from the 
	 * (preset)listpanel. Gets action when values should be put
	 * Also listens to selections from PictureChooserInputs, 
	 * to find new images.
	 */
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if (source instanceof PresetListPanel)
		{
			processPresetAction(e);
		}
	}

	/**
	 * Take care of an action sent from the presetlistpanel
	 * Actions from the presetlistpanel contains messages of 
	 * the type THIS=<newpreset> or NEXT=<newpreset>.
	 */
	private void processPresetAction(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		
		// Cut out everything before the =
		
		int equalPosition = cmd.indexOf('=');
		
		String listString = cmd.substring(0, equalPosition);
		
		String newValue = cmd.substring(equalPosition + 1);

		if (listString.equals("THIS")) // action to put value into the currently selected inputComponent
		{
			getSelectedInputComponent().putValue(newValue);
			
			getSelectedTab().scrollToSelectedComponent();
		}
		else if (listString.equals("NEXT"))
		{
			TabPanel activeTab = getSelectedTab(); // Get the current shown Tab in this ValueTabbedPane
			
			ValueInputComponent newNextInput = activeTab.getInputAfter(getSelectedInputComponent());
			
			activeTab.setSelectedInputComponent(newNextInput);
			
			ValueInputComponent nextInput = getSelectedInputComponent();

			ValueInputComponent selectedPane = getSelectedInputComponent();
			
			if (selectedPane != null)
			{
				selectedPane.putValue(newValue);
				
				getSelectedTab().scrollToSelectedComponent();
			}
			else
			{
				System.err.println("processPresetAction Error: selectedPane was null");
			}
		}
		else
		{
			System.err.println("ValueTabbedPane.actionPerformed: Error: did not recognize listString=" + listString);
		}
	}

	public ExaminationModel getCurrentExamination()
	{
		return mModel;
	}

	/**
	 * Gets the a numbered tab in thie ValueTabbedPane.
	 * @return the tab tab in this ValueTabbedPane corresponding to that number
	 * @throws IndexOutOfBoundsException when that tab does not exist
	 */
	public TabPanel getTab(int index) throws IndexOutOfBoundsException
	{
		TabPanel tab = (TabPanel)getComponentAt(index); // Get the first
		
		return tab;
	}

	/**
	 * Gets the ValueInputComponent that comes after the currently selected one
	 * @return the ValueInputComponent after the currently selected one
	 */
	public ValueInputComponent getNextInputComponent()
	{
		TabPanel selectedTab = getSelectedTab();
		
		ValueInputComponent selectedComponent = selectedTab.getSelectedInputComponent();
		
		return selectedTab.getNextInputComponent(selectedComponent);
	}

	public PresetListPanel getPresetListPanel()
	{
		return presetListPanel;
	}

	/**
	 * Return the field selected in the currently selected panel
	 */
	public ValueInputComponent getSelectedInputComponent()
	{
		TabPanel tp = getSelectedTab();
		
		if (tp != null)
		{
			return tp.getSelectedInputComponent();
		}
		else
		{
			return null;
		}
	}

	public TabPanel getSelectedTab()
	{
		TabPanel activeTab = (TabPanel)getSelectedComponent(); // Get the currently active Tab in the TabbedPane
		
		return activeTab;
	}

	/**
	 * Go to another tab, and focus that tab's first input component
	 */
	public void gotoTab(TabPanel newTab)
	{
		gotoTab(newTab.getName());
	}

	/**
	 * Go to another tab, and focus that tab's first input component
	 */
	public void gotoTab(String tabName)
	{
		if (tabTable.containsKey(tabName))
		{
			TabPanel tabPanelToSelect = (TabPanel)tabTable.get(tabName);
			
			setSelectedComponent(tabPanelToSelect); // select the tab
			
			ValueInputComponent componentToFocus = tabPanelToSelect.getFirstInputComponent();
			
			if (debug)
			{
				System.out.println("gotoTab focusing first input component in " +
				
					componentToFocus.getInputModel().getName());
			}
			
			componentToFocus.requestFocus();
		}
		else
		{
		}
	}

	public void clearValues()
	{
		mModel.clearValues();

		if (tabTable == null)
		{
			return;
		}
		if (tabTable.isEmpty())
		{
			return;
		}

		Collection values = tabTable.values();
		
		if (values == null)
		{
			return;
		}
		if (values.isEmpty())
		{
			return;
		}

		Iterator elems = values.iterator();
		
		while (elems.hasNext())
		{
			TabPanel aTab = (TabPanel)elems.next();
			
			aTab.clearValues();
		}
	}

	/*
	 * Checks the input values in this tabbedPane. // NE
	 * Returns true if the values are valid. Throws exception if not
	 * @throws ValueInputException if there are invalid values (the exception contains the explanation why)
	 */
	public boolean checkValues() throws ValueInputException
	{
		if (tabTable == null)
		{
			throw new ValueInputException(" Null Tab Table");
		}
		if (tabTable.isEmpty())
		{
			throw new ValueInputException(" Empty Tab Table");
		}

		Collection values = tabTable.values();
		
		if (values == null)
		{
			throw new ValueInputException(" Null Tab Values");
		}
		if (values.isEmpty())
		{
			throw new ValueInputException(" Empty Tab Values");
		}

		Iterator elems = values.iterator();
		
		while (elems.hasNext())
		{
			TabPanel aTab = (TabPanel)elems.next();
			
			aTab.checkInputValues(); // throws ValueInputException
		}
		
		return true;
	}

	public boolean hasIdType(TabPanel theTab)
	{
		if (tabTable == null)
		{
			return false;
		}
		if (tabTable.isEmpty())
		{
			return false;
		}

		Collection values = tabTable.values();

		if (values == null)
		{
			return false;
		}
		if (values.isEmpty())
		{
			return false;
		}

		Iterator elems = values.iterator();
		
		while (elems.hasNext())
		{
			TabPanel aTab = (TabPanel)elems.next();
			
			if (aTab != theTab)
			{
				if (aTab.hasIdType(null))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Build the gui, i.e clean up and add all the tabs 
	 * that are in the mModel
	 */
	public void rebuild()
	{
		String oldTabName = null;
		
		tabTable = new LinkedHashMap();

		// Store pre-selected tab
		
		TabPanel oldTab = (TabPanel) getSelectedComponent();
		
		if (oldTab != null)
		{
			oldTabName = oldTab.getName();
		}
		
		// Clear out old panes
		
		this.removeAll();

		Object catsOb[] = mModel.getCategories();
		
		if (catsOb == null)
		{
			return;
		}
		
		CategoryModel[] cats = (CategoryModel[]) catsOb;
		
		for (int i = 0; i < cats.length; i++)
		{
			CategoryModel aCatModel = cats[i];
			
			TabPanel tabPanel = new TabPanel(aCatModel, this, designable); // Create tab
			
			tabPanel.addChangeListener(this); // Listen for state changes (field selections)
			
			this.addTab(aCatModel.getTitle(), tabPanel);
			
			tabTable.put(aCatModel.getTitle(), tabPanel);
		}

		if (oldTabName != null)
		{
			gotoTab(oldTabName);
		}
	}

	public void setDesignable(boolean in_designable)
	{
		designable = in_designable;

		// Set all current tabPanels designable
		
		for (int i = 0; i < getComponentCount(); i++)
		{
			Component c = getComponent(i);
			
			if (c instanceof TabPanel)
			{
				((TabPanel)c).setDesignable(designable);
			}
		}
	}

	public void setPresetListPanel(PresetListPanel in_panel)
	{
		presetListPanel = in_panel;
	}

	public void setModel(ExaminationModel in_model)
	{
		// Remove old changelistener
		
		if (mModel != null)
		{
			mModel.removeChangeListener(this);
		}
		
		mModel = in_model;
		
		rebuild();
		
		// Add listener to rebuild the GUI when the ExaminationModel changes
		
		mModel.addChangeListener(this);
	}

	/*
	 * Scroll and show the photo panel
	 */
	private void showPhotoPanel(boolean isPicture)
	{
		if (mSplitPhoto == null)
		{
			return;
		}

		int vTotalH = mSplitPhoto.getHeight();
		
		int vLocation = mSplitPhoto.getDividerLocation();
		
		int vPhotoH = mSplitPhoto.getRightComponent().getHeight();

		if (isPicture)
		{
			if (vPhotoH < 50)
			{
				mSplitPhoto.setDividerLocation(vTotalH - 150);
			}
		}
		else
		{
		}
	}

	private void showPresetPanel(boolean pShowIt)
	{
		if (mSplitPresets == null)
		{
			return;
		}

		int vTotalW = mSplitPresets.getWidth();
		
		int vLocation = mSplitPresets.getDividerLocation();
		
		int vListW = mSplitPresets.getRightComponent().getWidth();

		if (pShowIt)
		{
			if (mPresetsListPanelW == 0)
			{
				mPresetsListPanelW = 150;
			}

			mSplitPresets.setDividerLocation(vTotalW - mPresetsListPanelW);
		}
		else
		{
			if (vListW > 5)
			{
				mSplitPresets.setDividerLocation(vTotalW - 4);
				mPresetsListPanelW = vListW;
			}
		}
	}

	public void stateChanged(javax.swing.event.ChangeEvent e)
	{
		if (e.getSource() == this) // source is valuetabbedpane
		{
			Component selectedComponent = this.getSelectedComponent(); // Get component that makes up current tab
			
			if (selectedComponent instanceof TabPanel) // Changed to a new TabPanel
			{
				if (mPrevIndex >= 0 && this.getTabCount() > mPrevIndex)
				{
					this.setBackgroundAt(mPrevIndex, mColor);
				}
				
				TabPanel aTabPan = (TabPanel)selectedComponent;

				// Reset selected input to the first one
				
				aTabPan.resetSelectedInput();
				
				mPrevIndex = this.getSelectedIndex();
				
				this.setBackgroundAt(mPrevIndex, Color.white);
				
				boolean isPhoto = aTabPan.isPhotoTab();
				
				showPresetPanel(!isPhoto);
				
				aTabPan.scrollToPanel(null);

				if (debug)
				{
					System.out.println("stateChanged [source == this]");
				}
				
				aTabPan.focusSelectedInput();
			}
			else if (selectedComponent == null)
			{
			}
			else
			{
				System.out.println(
				    "Error: ValueTabbedPane.stateChanged: tab changed, but unknown component type [" +
				    selectedComponent.getClass() + "]");
			}
		}
		else if (e.getSource() instanceof ExaminationModel)
		{
			// The examinationModel has changed, so rebuild
			
			rebuild();
		}
		else if (e.getSource() instanceof TabPanel)
		{ 
			// Caret has moved to anther input field
			
			updatePresetPanel();
		}
		else
		{
			System.out.println("Notice: stateChanged could not handle instance of " + e.getSource().getClass());
		}

		if (debug)
		{
			System.out.println("stateChanged finished, Source class was = " + e.getSource().getClass());
		}
	}

	public void focusSelectedInput()
	{
		getSelectedTab().focusSelectedInput();
	}

	public void updatePresetPanel()
	{
		if (presetListPanel == null)
		{
		}
		else
		{
			presetListPanel.updatePresets(this); // Send a valuetabbedpane reference so presetlistpanel can get presets
		}
	}

}
