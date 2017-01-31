/*
 * $Id: ValueTabbedPane.java,v 1.48 2010/07/01 08:15:42 oloft Exp $
 *
 * Created on June 15, 2001, 12:08 PM
 *
 * $Log: ValueTabbedPane.java,v $
 * Revision 1.48  2010/07/01 08:15:42  oloft
 * v 4.5 minor edits
 *
 * Revision 1.47  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.46  2008/06/12 09:21:21  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textfält om man sparar med felaktigt infyllt textfält.
 * 164: Tabbning mellan inputs scrollar hela formuläret så att den aktuella inputen alltid är synlig
 * Övrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM för att tilldela mer minne så att större bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar bättre in samt att inga nollor sparas om inget är ifyllt.
 * Drag'n'drop för bilder fungerade ej och är borttaget tills vidare
 * Text i felmeddelandet vid inmatat värde utan att trycka på enter ändrat
 *
 * Revision 1.45  2008/01/31 13:23:26  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.44  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.43  2006/09/13 22:00:05  oloft
 * Added Open functionality
 *
 * Revision 1.42  2006/05/29 18:32:48  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.41  2005/08/23 09:06:41  erichson
 * bugfix in gotoTab method: getFirstInput returns null if a tab is empty, and requestFocus would be called on the null pointer.
 *
 * Revision 1.40  2005/04/29 09:48:16  erichson
 * bug fix: If you changed the template when the active panel in the tabbedPane was not a TabPanel, you would get a ClassCastException.
 * Rewrote the functionality to not assume a TabPanel, but still do the TabPanel-specific stuff if it is.
 *
 * Revision 1.39  2005/04/26 12:45:25  erichson
 * Update to allow new plaque input to listen to medrecords for tab changes.
 *
 * Revision 1.38  2005/02/17 10:05:15  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.37  2005/01/30 15:19:20  lindahlf
 * T4 Integration
 *
 * Revision 1.36  2004/12/20 13:13:33  erichson
 * Added mucos component to tab type checking
 *
 * Revision 1.35  2004/12/08 14:42:52  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.34  2004/12/06 19:04:21  erichson
 * Added mucos component // NE
 *
 * Revision 1.33  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.32  2004/02/26 12:14:05  lindahlf
 * Added Cache support to MVDHandler, and proper patient id support when invoking MR from MS
 *
 * Revision 1.31  2004/01/06 22:06:50  oloft
 * Minor fixes
 *
 * Revision 1.30  2003/12/21 21:54:12  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.29  2003/11/11 13:50:59  oloft
 * Switching mainbranch
 *
 * Revision 1.28.2.6  2003/10/20 14:43:02  oloft
 * File transfer
 *
 * Revision 1.28.2.5  2003/10/18 14:50:45  oloft
 * Builds tree file with new file names
 *
 * Revision 1.28.2.4  2003/09/09 13:55:33  erichson
 * Updated identification handling (bug 181) and added InvalidExaminationModelException handling
 *
 * Revision 1.28.2.3  2003/09/08 16:36:06  erichson
 * changed makeTree to getTabTrees()
 *
 * Revision 1.28.2.2  2003/08/16 14:41:28  erichson
 * Cleaned up photoPanel handling, removed actionListener since we now have PictureChoiceEvent
 *
 * Revision 1.28.2.1  2003/08/14 10:47:00  erichson
 * Major reworking of tab handling etc, because of the new input system
 *
 * Revision 1.28  2003/07/23 14:19:06  erichson
 * Updated the tab handling (gotoTab etc) to fix focus bugs (bugzilla bug 26, 29).
 *
 */

package medview.medrecords.components;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Color.*;

import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

import medview.medrecords.components.inputs.*;
import medview.medrecords.data.*;
import medview.medrecords.exceptions.*;
import medview.medrecords.models.*;
import medview.medrecords.tools.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;

/**
 * A class for a tabbed pane with value fields. ChangeEvents are dispatched when
 * the selected field or tab is changed.
 *
 * @author  nils
 * @version
 *
 */

public class ValueTabbedPane extends JTabbedPane implements ActionListener,
	ChangeListener, MedViewDataConstants
{

	private static final boolean debug = false;

	private ExaminationModel mModel;

	private PresetListPanel presetListPanel = null;
	
	private boolean designable;

	private MucosInputComponent mMucosPanel;

    private LinkedHashMap tabTable;

	private StartPlaque mPlaquePanel;

	private JSplitPane mSplitPhoto;

	private JSplitPane mSplitPresets;
	
	private ApplicationFrame applicationFrame = null;

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

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

		addChangeListener(this); // listen to itself for tab changes
	}

        public ValueTabbedPane(ExaminationModel in_model) throws InvalidExaminationModelException
	{
		this();

		setModel(in_model);
	}

	public void setApplicationFrame(ApplicationFrame af)
	{
		applicationFrame = af;
	}

	public ApplicationFrame getApplicationFrame()
	{
		return applicationFrame;
	}

	public void setSplitPhoto(JSplitPane pLeftPanel)
	{
		mSplitPhoto = pLeftPanel;
	}

	public void setSplitPresets(JSplitPane pContentPanel)
	{
		mSplitPresets = pContentPanel;
    }

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if (source instanceof PresetListPanel)
		{
			processPresetAction(e);
		}
	}

	protected void addPhotoPanelImage(String path)
	{
		PhotoPanel photoPanel = getPhotoPanel();

		if (photoPanel == null)
		{
			return;
		}

		photoPanel.addImage(path);

		showPhotoPanel(true);
	}

	private PhotoPanel getPhotoPanel()
	{
		return applicationFrame.getPhotoPanel();
	}

	/**
	 * Take care of an action sent from the presetlistpanel
	 * Actions from the presetlistpanel contains messages of the
	 * type THIS=<newpreset> or NEXT=<newpreset>.
	 */
	private void processPresetAction(ActionEvent e)
	{
		String cmd = e.getActionCommand();

		int equalPosition = cmd.indexOf('=');

		String listString = cmd.substring(0, equalPosition);

		String newValue = cmd.substring(equalPosition + 1);
        String customValue = "";

        if (listString.equals("THIS"))
		{
            String [] temp;
            temp = newValue.split("\\|\\|");
            if(temp.length==2)
            {
                //we have a custom value
                customValue=temp[0];
                newValue=temp[1];
                getSelectedInputComponent().putCustomPreset(newValue,customValue);
            }
            getSelectedInputComponent().putPreset(newValue);

			getSelectedTab().scrollToSelectedComponent();

		}
		else if (listString.equals("NEXT"))
		{
			TabPanel activeTab = getSelectedTab(); // Get the current shown Tab in this ValueTabbedPane

			ValueInputComponent newNextInput = activeTab.getInputAfter(getSelectedInputComponent());

			activeTab.setSelectedInputComponent(newNextInput);

			ValueInputComponent selectedPane = getSelectedInputComponent();

			if (selectedPane != null)
			{

                String [] temp;
                temp = newValue.split("\\|\\|");
                if(temp.length==2)
                {
                //we have a custom value
                customValue=temp[0];
                newValue=temp[1];
                getSelectedInputComponent().putCustomPreset(newValue,customValue);
                }
                selectedPane.putPreset(newValue);

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

	public TabPanel getFirstTab()
	{
		Component[] components = getComponents();

		for (int i = 0; i < components.length; i++)
		{
			if (components[i] instanceof TabPanel)
			{
				return (TabPanel)components[i];
			}
		}

		Ut.error("No first TabPanel in ValuetabbedPane!");

		return null;
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
		try
		{
			TabPanel activeTab = (TabPanel)getSelectedComponent();

			return activeTab;
		}
		catch (java.lang.ClassCastException e)
		{
			return null;
		}
	}


	public void gotoFirstTab()
	{
		gotoTab(getFirstTab());
	}

	/**
	 * Go to another tab, and focus that tab's first
	 * input component
	 */
	public void gotoTab(TabPanel newTab)
	{
		gotoTab(newTab.getName());
	}

	/**
	 * Go to another tab, and focus that tab's first
	 * input component
	 */
	public void gotoTab(String tabName)
	{
		int index = indexOfTab(tabName);
                if (index != -1)
                {
                    Component comp = getComponentAt(index);
                    setSelectedComponent(comp); // select the tab
                    
                    if (comp instanceof TabPanel) // Panel with inputs (not mucos or plaque)
                    {
                        // Request focus for the first input
                        TabPanel tabPanel = (TabPanel) comp;
			ValueInputComponent componentToFocus = tabPanel.getFirstInputComponent();

                        if (componentToFocus != null)
                            componentToFocus.requestFocus();
                    }
		}
	}

	public void clearAllInputs()
	{
		TabPanel[] tabs = getTabs();

		for (int i = 0; i < tabs.length; i++)
		{
			tabs[i].clearValues();
		}
	}

	/**
	 * Checks the input values in this tabbedPane. // NE
	 * Returns true if the values are valid. Throws exception if not
	 * @throws ValueInputException if there are invalid values
	 * (the exception contains the explanation why)
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

			aTab.checkInputValues(); // -> ValueInputException
		}

		return true;
	}

	/**
	 * Build the gui, i.e clean up and add all the tabs that
	 * are in the mModel
	 */
	public void rebuild() throws InvalidExaminationModelException
	{
		System.out.println("ValueTabbedPane rebuild");
		
		String oldTabName = null;

		tabTable = new LinkedHashMap();

		// store pre-selected tab

		int oldTabIndex = getSelectedIndex();

		if (oldTabIndex != -1)
		{
			oldTabName = getTitleAt(oldTabIndex);
		}

		// clear old panes

		this.removeAll();

		Object catsOb[] = mModel.getCategories();

		if (catsOb == null)
		{
			throw new InvalidExaminationModelException("cats = null for mModel " + mModel.toString());
		}

		CategoryModel[] cats = (CategoryModel[])catsOb;

		for (int i = 0; i < cats.length; i++)
		{
			CategoryModel aCatModel = cats[i];

			TabPanel tabPanel = new TabPanel(aCatModel, this, designable);

			tabPanel.addChangeListener(this);

            //if a category has the attribute VISIBLE = "false"
            //we only add the category to the tabTable
            if(aCatModel.isVisible())
            {
                this.addTab(aCatModel.getTitle(), tabPanel);
            }

            tabTable.put(aCatModel.getTitle(), tabPanel);
		}

		if (!designable && PreferencesModel.instance().getUsePlaqueIndex())
		{
			mPlaquePanel = new StartPlaque(this); // StartPlaque listens for tab changes on this

			this.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_PLAQUE_INDEX_LS_PROPERTY), mPlaquePanel);
		}
		
		if (!designable && PreferencesModel.instance().getUseMucos())
		{
			mMucosPanel = new MucosInputComponent();
			
			this.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_MUCOS_LS_PROPERTY), mMucosPanel);
	        }

        if (oldTabName != null)
		{
			gotoTab(oldTabName);
		}
    }

	public Tree getPlaqueTree()
	{
		if (mPlaquePanel != null)
		{
			return mPlaquePanel.buildTree();
		}

		return null;
	}
	
	public void loadPlaqueTree(Tree t) {
		if (mPlaquePanel != null)
		{
			mPlaquePanel.loadTree(t);
		}		
	}
	
	public Tree getMucosTree()
	{
		if (mMucosPanel != null)
		{
			return mMucosPanel.buildTree();
		}

		return null;
	}


    public void setDesignable(boolean in_designable)
	{
		designable = in_designable;

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

	public void setModel(ExaminationModel in_model) throws InvalidExaminationModelException
	{
		if (in_model == null)
		{
			throw new InvalidExaminationModelException("setModel: Given model is null");
		}

		if (mModel != null)
		{
			mModel.removeChangeListener(this);
		}

		mModel = in_model;

		rebuild();

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

		int vPhotoH = mSplitPhoto.getRightComponent().getHeight();

		if (isPicture)
		{
			if (vPhotoH < 50)
			{
				mSplitPhoto.setDividerLocation(vTotalH - 150);
			}
		}
	}

	/**
	 * Call this method to detach the panel as a listener from
	 * everything it listens to.
	 */
	public void detachAsListener()
	{		
		// remove this from examination model
		
		if (mModel != null) // Fredrik 041207
		{
			mModel.removeChangeListener(this);
		}
		
		// remove from tab panels
		
		Component[] components = getComponents();
	
		for (int i = 0; i < components.length; i++)
		{
			if (components[i] instanceof TabPanel)
			{
				((TabPanel)components[i]).detachAsListener();
			}
		}
	}

	/**
	 * The ValueTabbedPane has a ChangeListener that receives events when:
	 * 1. The selected tab changes (event received from itself)
	 * 2. An examinationModel has changed (-> rebuild) (event received from ExaminationModel
	 * 3. The focus has changed (event received from a TabPanel)
	 */
	public void stateChanged(javax.swing.event.ChangeEvent e)
	{
        if (e.getSource() == this)
		{
			tabChanged();
		}
		else if (e.getSource() instanceof ExaminationModel)
		{
			try
			{
				rebuild();
			}
			catch (InvalidExaminationModelException ieme)
			{
				JOptionPane.showMessageDialog(this, "Invalid examination model: " + ieme.getMessage());

				return;
			}
		}
		else if (e.getSource() instanceof TabPanel)
		{
			updatePresetPanel();
		}
		else
		{
			System.out.println("Notice: stateChanged could not handle instance of " + e.getSource().getClass());
		}
    }

	/**
	 * Called when the tab is changed and the GUI needs updating
	 */
	public void tabChanged()
	{
		Component selectedComponent = this.getSelectedComponent();

		if (selectedComponent == null)
		{
			return;
		}
		else if (selectedComponent instanceof StartPlaque)
		{
			showPresetPanel(false);
        }
		else if (selectedComponent instanceof MucosInputComponent)
		{
			showPresetPanel(false);
        }
        else if (selectedComponent instanceof TabPanel)
		{
			final TabPanel aTabPan = (TabPanel) selectedComponent;

			aTabPan.resetSelectedInput(); // reset selected input to the first one

			boolean isPhoto = aTabPan.isPhotoTab(); // the isPhotoTab() method is nasty - sideeffect: sets photos! NOT simple boolean

			showPresetPanel(!isPhoto); // do not show preset panel if the panel is the photo panel

			aTabPan.scrollToSelectedComponent();

			aTabPan.focusSelectedInput();
        }

		else
		{
			System.out.println("Error: ValueTabbedPane.stateChanged:" +

				"tab changed, but unknown component type [" + selectedComponent.getClass() + "]");
		}
	}

	public void focusSelectedInput()
	{
		TabPanel st = getSelectedTab();

		if (st != null)
		{
			st.focusSelectedInput();
		}
	}

        // ToDo: When the preset panel is hidden and then shown again some display problems occur.
        // The reason seems to be that the tab panel becomes bigger when it gets more space and then it does not become
        // smaller again when the preset panel is shown
        // Could one fix it by making the ValueTabbedPane's width snaller before bringing in the presets?
	private void showPresetPanel(boolean show)
	{
		if (mSplitPresets == null)
		{
			return;
		}
		else
		{	
			int totalSplitWidth = mSplitPresets.getWidth();
			
			int prefListWidth = mSplitPresets.getRightComponent().getPreferredSize().width;
	
			int divWidth = mSplitPresets.getDividerSize();
			
			if (show)
			{
                            // Tried to fix size problem by setting size here but that does not seem to work
                            /*
                                System.out.println("w1: " + this.getWidth());
                                this.setSize(this.getWidth()-prefListWidth-divWidth, this.getHeight());
                                System.out.println("w2: " + this.getWidth());
                            */
				mSplitPresets.setDividerLocation(totalSplitWidth - prefListWidth - divWidth);
				
			}
			else
			{
				mSplitPresets.setDividerLocation(totalSplitWidth - divWidth);
			}		
		}
	}

	public void updatePresetPanel()
	{
		if (presetListPanel != null)
		{
			// send a valuetabbedpane reference so presetlistpanel can get presets

			presetListPanel.updatePresets(this);
		}
	}

	/**
	 * Returns all VISIBLE TabPanels in this tab
	 */
	public TabPanel[] getTabs()
	{
		Component[] components = getComponents();

		Vector v = new Vector();

		for (int i = 0; i < components.length; i++)
		{
			if (components[i] instanceof TabPanel)
			{
				v.add(components[i]);
			}
		}

		TabPanel[] tabPanels = new TabPanel[v.size()];

		tabPanels = (TabPanel[])v.toArray(tabPanels);

		return tabPanels;
	}

	/**
	 * Returns all TabPanels in this tab
	 */
	public LinkedHashMap getTabsTable()
	{
		return tabTable;
	}


    /**
	 * Returns the Mucos panel.
	 * 
	 * @return the Mucos panel, or null if it does not exist.
	 **/
	public MucosInputComponent getMucosPanel() {
		return mMucosPanel;
	}

	/**
	 * Gets an array of Trees (one Tree for each tab in this valueTabbedPane)
     * also includes tabs that are not visible
	 * @return an array of Trees corresponding to tabs
	 */
	public Tree[] getTreeRepresentations(String pid, Date examinationDate, String pCode)
	{
        LinkedHashMap tabs = getTabsTable();
		Tree[] treeArray = new Tree[tabs.size()];

        int i = 0;
        for (Iterator it = tabs.values().iterator(); it.hasNext();)
        {
            TabPanel tabPanel = (TabPanel)it.next();
            treeArray[i] = tabPanel.getTreeRepresentation(pid, examinationDate, pCode);
            i++;
        }
		return treeArray;
	}

	public void setIdentification(PatientIdentifier pid)
	{
		TabPanel[] tabs = getTabs();

		for (int i = 0; i < tabs.length; i++)
		{
			ValueInputComponent[] inputs = tabs[i].getInputComponents();

			for (int inputCount = 0; inputCount < inputs.length; inputCount++)
			{
				if (inputs[inputCount].isIdentification())
				{
					if (inputs[inputCount].getName().equalsIgnoreCase(PCODE_TERM_NAME))
					{
						inputs[inputCount].putPreset(pid.getPCode());
					}
					else if (inputs[inputCount].getName().equalsIgnoreCase(PID_TERM_NAME))
					{
						if (pid.containsPID())
						{
							inputs[inputCount].putPreset(pid.getPID());
						}
					}
				}
			}
		}
	}
}
