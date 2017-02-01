/*
 * @(#)DataLocationSettingsPanel.java
 *
 * $Id: DataLocationSettingsPanel.java,v 1.7 2006/05/29 18:32:47 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * A panel presenting the user with a choice to use
 * local or remote datahandling, as well as specifying
 * the locations of the local and remote datahandling.
 *
 * @author Fredrik Lindahl
 */
public class DataLocationSettingsPanel extends JPanel implements
	MedViewLanguageConstants, GUIConstants
{

	// EVENT HANDLING AND LISTENING

	public void addDataPanelListener(DataLocationSettingsListener l)
	{
		listenerList.add(DataLocationSettingsListener.class, l);
	}

	public void removeDataPanelListener(DataLocationSettingsListener l)
	{
		listenerList.remove(DataLocationSettingsListener.class, l);
	}

	private void fireLocalLocationChangeRequested(String location)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).

				localLocationChangeRequested(e);
		}
	}

	private void fireServerLocationChangeRequested(String location)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).

				serverLocationChangeRequested(e);
		}
	}

	private void fireUseRemoteDataHandlingChangeRequested(boolean useRemote)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedUseFlag(useRemote);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).

				useRemoteDataHandlingChangeRequested(e);
		}
	}

	private void fireTermDefinitionLocationChangeRequested(String location)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).

				termDefinitionLocationChangeRequested(e);
		}
	}

	private void fireTermValueLocationChangeRequested(String location)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).

				termValueLocationChangeRequested(e);
		}
	}

	private void firePDADataLocationChangeRequested(String location)
	{
		Object[] listeners = listenerList.getListenerList();

		DataLocationSettingsEvent e = new DataLocationSettingsEvent();

		e.setRequestedLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			((DataLocationSettingsListener)listeners[i+1]).pdaDataLocationChangeRequested(e);
		}
	}

	// SET METHODS

	public void setLocalLocationText(String text)
	{
		ignoreFieldEvents = true;

		localLocationField.setText(text);

		ignoreFieldEvents = false;
	}

	public void setServerLocationText(String text)
	{
		ignoreFieldEvents = true;

		serverLocationField.setText(text);

		ignoreFieldEvents = false;
	}

	public void setTermDefinitionLocationText(String text)
	{
		ignoreFieldEvents = true;

		termDefField.setText(text);

		ignoreFieldEvents = false;
	}

	public void setTermValueLocationText(String text)
	{
		ignoreFieldEvents = true;

		termValField.setText(text);

		ignoreFieldEvents = false;
	}

	public void setPDALocationText(String text)
	{
		ignoreFieldEvents = true;

		pdaLocationField.setText(text);

		ignoreFieldEvents = false;
	}

	public void setUsesRemote(boolean b)
	{
		doNotFireDueToSelection = true;

		if (b)
		{
			radioGroup.setSelected(useRemoteRadioButton.getModel(), true);
		}
		else
		{
			radioGroup.setSelected(useLocalRadioButton.getModel(), true);
		}

		doNotFireDueToSelection = false;
	}


	// LAYOUT

	protected void layoutPanel()
	{
		// layout manager

		setLayout(new GridBagLayout());


		// labels

		localLocationLabel = new JLabel(mVDH.getLanguageString(LABEL_LOCAL_LOCATION_LS_PROPERTY));

		termDefLabel = new JLabel(mVDH.getLanguageString(LABEL_TERM_DEFINITION_LOCATION_LS_PROPERTY));

		termValLabel = new JLabel(mVDH.getLanguageString(LABEL_TERM_VALUE_LOCATION_LS_PROPERTY));

		serverLocationLabel = new JLabel(mVDH.getLanguageString(LABEL_SERVER_LOCATION_LS_PROPERTY));

		pdaLocationLabel = new JLabel(mVDH.getLanguageString(LABEL_PDA_LOCATION_LS_PROPERTY));


		// radio buttons

		useLocalRadioButton = new MedViewRadioButton(RADIO_BUTTON_USE_LOCAL_DATAHANDLING_LS_PROPERTY);

		useRemoteRadioButton = new MedViewRadioButton(RADIO_BUTTON_USE_REMOTE_DATAHANDLING_LS_PROPERTY);

		useLocalRadioButton.addItemListener(radioListener);

		useRemoteRadioButton.addItemListener(radioListener);

		radioGroup.add(useLocalRadioButton);

		radioGroup.add(useRemoteRadioButton);


		// local location text field

		localLocationField = new JTextField();

		localLocationField.setEditable(false);

		localLocationField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(); }

			public void removeUpdate(DocumentEvent e) { process(); }

			private void process()
			{
				if (!ignoreFieldEvents)
				{
					commandQueue.addToQueue(new ChangeLocalLocationCommand(localLocationField.getText()));
				}
			}
		});

		// term definition location text field

		termDefField = new JTextField();

		termDefField.setEditable(false);

		termDefField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(); }

			public void removeUpdate(DocumentEvent e) { process(); }

			private void process()
			{
				if (!ignoreFieldEvents)
				{
					commandQueue.addToQueue(new ChangeTDLCommand(termDefField.getText()));
				}
			}
		});

		// term value location text field

		termValField = new JTextField();

		termValField.setEditable(false);

		termValField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(); }

			public void removeUpdate(DocumentEvent e) { process(); }

			private void process()
			{
				if (!ignoreFieldEvents)
				{
					commandQueue.addToQueue(new ChangeTVLCommand(termValField.getText()));
				}
			}
		});

		// server location text field

		serverLocationField = new JTextField();

		serverLocationField.setEditable(true);

		serverLocationField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(); }

			public void removeUpdate(DocumentEvent e) { process(); }

			private void process()
			{
				if (!ignoreFieldEvents)
				{
					commandQueue.addToQueue(new ChangeServerLocationCommand(serverLocationField.getText()));
				}
			}
		});

		// pda location text field
		
		pdaLocationField = new JTextField();

		pdaLocationField.setEditable(false);

		pdaLocationField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(); }

			public void removeUpdate(DocumentEvent e) { process(); }

			private void process()
			{
				if (!ignoreFieldEvents)
				{
					commandQueue.addToQueue(new ChangePDALocationCommand(pdaLocationField.getText()));
				}
			}
		});


		// more buttons

		localLocationMoreButton = new MedViewMoreButton(localLocationChangeAction);

		termDefMoreButton = new MedViewMoreButton(termDefChangeAction);

		termValMoreButton = new MedViewMoreButton(termValChangeAction);

		pdaLocationMoreButton = new MedViewMoreButton(pdaLocationChangeAction);


		// fictive more width

		Component moreStrut = Box.createHorizontalStrut(termDefMoreButton.getSize().width);


		// titled panels (local and remote)

		localPanel = new JPanel(new GridBagLayout());

		distPanel = new JPanel(new GridBagLayout());

		pdaPanel = new JPanel(new GridBagLayout());

		GUIUtilities.attachProperTitledBorder(localPanel, mVDH.getLanguageString(TITLE_LOCAL_SETTINGS_LS_PROPERTY), new Insets(cCS, cCS, cCS, cCS));

		GUIUtilities.attachProperTitledBorder(distPanel, mVDH.getLanguageString(TITLE_DISTRIBUTED_SETTINGS_LS_PROPERTY), new Insets(cCS, cCS, cCS, cCS));

		GUIUtilities.attachProperTitledBorder(pdaPanel, mVDH.getLanguageString(TITLE_PDA_SETTINGS_LS_PROPERTY), new Insets(cCS, cCS, cCS, cCS));


		// layout

		GUIUtilities.gridBagAdd(localPanel, localLocationLabel,		0, 0, 1, 1, 0, 0, EAST, new Insets(0,0,cCS,cCS), NONE);

		GUIUtilities.gridBagAdd(localPanel, localLocationField,		1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,cCS,cCS), BOTH);

		GUIUtilities.gridBagAdd(localPanel, localLocationMoreButton,	2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,cCS,0), NONE);

		GUIUtilities.gridBagAdd(localPanel, termDefLabel,		0, 1, 1, 1, 0, 0, EAST, new Insets(0,0,cCS,cCS), NONE);

		GUIUtilities.gridBagAdd(localPanel, termDefField,		1, 1, 1, 1, 1, 0, CENT, new Insets(0,0,cCS,cCS), BOTH);

		GUIUtilities.gridBagAdd(localPanel, termDefMoreButton,		2, 1, 1, 1, 0, 0, CENT, new Insets(0,0,cCS,0), NONE);

		GUIUtilities.gridBagAdd(localPanel, termValLabel,		0, 2, 1, 1, 0, 0, EAST, new Insets(0,0,0,cCS), NONE);

		GUIUtilities.gridBagAdd(localPanel, termValField,		1, 2, 1, 1, 1, 0, CENT, new Insets(0,0,0,cCS), BOTH);

		GUIUtilities.gridBagAdd(localPanel, termValMoreButton,		2, 2, 1, 1, 0, 0, CENT, new Insets(0,0,0,0), NONE);


		GUIUtilities.gridBagAdd(distPanel, serverLocationLabel,		0, 0, 1, 1, 0, 0, EAST, new Insets(0,0,0,cCS), NONE);

		GUIUtilities.gridBagAdd(distPanel, serverLocationField,		1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,0,cCS), BOTH);

		GUIUtilities.gridBagAdd(distPanel, moreStrut,			2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,0,0), NONE);

		GUIUtilities.gridBagAdd(pdaPanel, pdaLocationLabel,             0, 0, 1, 1, 0, 0, EAST, new Insets(0,0,cCS,cCS), NONE);

		GUIUtilities.gridBagAdd(pdaPanel, pdaLocationField,             1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,cCS,cCS), BOTH);

		GUIUtilities.gridBagAdd(pdaPanel, pdaLocationMoreButton,        2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,cCS,0), NONE);


		GUIUtilities.gridBagAdd(this, useLocalRadioButton,	0, 0, 1, 1, 1, 0, EAST, new Insets(0,0,cCS,cCS), NONE);

		GUIUtilities.gridBagAdd(this, useRemoteRadioButton, 	1, 0, 1, 1, 1, 0, WEST, new Insets(0,0,cCS,0), NONE);

		GUIUtilities.gridBagAdd(this, localPanel,		0, 1, 2, 1, 0, 0, CENT, new Insets(0,0,cCS,0), BOTH);

		GUIUtilities.gridBagAdd(this, distPanel,		0, 2, 2, 1, 0, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, pdaPanel,         0, 3, 2, 1, 0, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 3, 2, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}


	// UTILITY METHODS

	public void disableDistHandling()
	{
		setDistPanelEnabled(false);

		useRemoteRadioButton.setEnabled(false);
	}

	private void setLocalPanelEnabled(boolean flag)
	{
		localLocationLabel.setEnabled(flag);

		termDefLabel.setEnabled(flag);

		termValLabel.setEnabled(flag);

		localLocationField.setEnabled(flag);

		termDefField.setEnabled(flag);

		termValField.setEnabled(flag);

		localLocationChangeAction.setEnabled(flag);

		termDefChangeAction.setEnabled(flag);

		termValChangeAction.setEnabled(flag);

		localPanel.setEnabled(flag);
	}

	private void setDistPanelEnabled(boolean flag)
	{
		serverLocationLabel.setEnabled(flag);

		serverLocationField.setEnabled(flag);

		distPanel.setEnabled(flag);
	}


	// CONSTRUCTOR(S)

	/**
	 * Constructs a data location settings panel, using the
	 * specified command queue and the specified component as
	 * a parent component for centering dialogs (change term
	 * definition location etc) over. This version of the method
	 * will use the full functionality of the panel, i.e. it is
	 * assumed that 'distributed' data handling is to be used.
	 *
	 * @param queue CommandQueue
	 * @param parComp Component
	 */
	public DataLocationSettingsPanel(CommandQueue queue, Frame owner)
	{
		this(queue, owner, true);
	}

	/**
	 * Constructs a data location settings panel, using the
	 * specified command queue and the specified component as
	 * a parent component for centering dialogs (change term
	 * definition location etc) over. This version of the method
	 * allows you to specify whether or not the 'distributed' data
	 * handling part should be enabled in the panel.
	 *
	 * @param queue CommandQueue
	 * @param parComp Component
	 * @param useDistHandling boolean
	 */
	public DataLocationSettingsPanel(CommandQueue queue, Frame owner, boolean useDistHandling)
	{
		this.commandQueue = queue;

		this.owner = owner;

		layoutPanel();

		if (!useDistHandling)
		{
			disableDistHandling();
		}
	}


	// MEMBERS

	private CommandQueue commandQueue;

	private Frame owner;

	private JPanel localPanel;

	private JPanel distPanel;

	private JPanel pdaPanel;

	private JLabel localLocationLabel;

	private JLabel termDefLabel;

	private JLabel termValLabel;

	private JLabel serverLocationLabel;

	private JLabel pdaLocationLabel;

	private JTextField localLocationField;

	private JTextField termDefField;

	private JTextField termValField;

	private JTextField serverLocationField;

	private JTextField pdaLocationField;

	private JButton localLocationMoreButton;

	private JButton termDefMoreButton;

	private JButton termValMoreButton;

	private JButton pdaLocationMoreButton;

	private JRadioButton useLocalRadioButton;

	private JRadioButton useRemoteRadioButton;


	private boolean ignoreFieldEvents = false;

	private boolean doNotFireDueToSelection = false;

	private ButtonGroup radioGroup = new ButtonGroup();

	private MedViewDialogs mVD = MedViewDialogs.instance();

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private Action localLocationChangeAction = new LocalLocationChangeAction();

	private Action termDefChangeAction = new TermDefinitionChangeAction();

	private Action termValChangeAction = new TermValueChangeAction();

	private Action pdaLocationChangeAction = new PDADataLocationChangeAction();

	private EventListenerList listenerList = new EventListenerList();

	private RadioListener radioListener = new RadioListener();

	private int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

	// EVENT CLASSES

	/**
	 * Instances of this listener can be attached to the
	 * data location settings panel, so that users can know
	 * when various settings have changed (or request change).
	 */
	public interface DataLocationSettingsListener extends EventListener
	{
		public void localLocationChangeRequested(DataLocationSettingsEvent e);

		public void serverLocationChangeRequested(DataLocationSettingsEvent e);

		public void useRemoteDataHandlingChangeRequested(DataLocationSettingsEvent e);

		public void termDefinitionLocationChangeRequested(DataLocationSettingsEvent e);

		public void termValueLocationChangeRequested(DataLocationSettingsEvent e);

		public void pdaDataLocationChangeRequested(DataLocationSettingsEvent e);
	}

	/**
	 * These events are always attached to the method call to
	 * the listener interface DataLocationSettingsPanelListener.
	 * You can use the getRequestedValue() method
	 */
	public class DataLocationSettingsEvent extends EventObject
	{
		public boolean getRequestedUseFlag()
		{
			return requestedUseFlag;
		}

		private void setRequestedUseFlag(boolean flag)
		{
			this.requestedUseFlag = flag;
		}

		public String getRequestedLocation()
		{
			return requestedLocation;
		}

		private void setRequestedLocation(String loc)
		{
			this.requestedLocation = loc;
		}

		private DataLocationSettingsEvent()
		{
			super(DataLocationSettingsPanel.this);
		}

		private String requestedLocation;

		private boolean requestedUseFlag;
	}


	// LISTENERS

	private class RadioListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				if (!doNotFireDueToSelection) // when setting up panel from outside - event should not be fired
				{
					commandQueue.addToQueue(new ChangeUseRemoteCommand(

						radioGroup.getSelection() == useRemoteRadioButton.getModel()));
				}

				setLocalPanelEnabled(e.getSource() == useLocalRadioButton);

				setDistPanelEnabled(e.getSource() != useLocalRadioButton);
			}
		}
	}


	// ACTIONS

	private class LocalLocationChangeAction extends MedViewAction // coupled to the more button
	{
		public void actionPerformed(ActionEvent e)
		{
			String filePath = mVD.createAndShowLoadMVDDialog(owner);

			if (filePath != null)
			{
				localLocationField.setText(filePath); // document listener will place command in queue
			}
		}

		public LocalLocationChangeAction()
		{
			super(ACTION_CHANGE_DATA_LOCATION_LS_PROPERTY);
		}
	}

	private class TermDefinitionChangeAction extends MedViewAction // coupled to the more button
	{
		public void actionPerformed(ActionEvent e)
		{
			String filePath = mVD.createAndShowChangeTermDefinitionDialog(owner);

			if (filePath != null)
			{
				termDefField.setText(filePath); // document listener will place command in queue
			}
		}

		public TermDefinitionChangeAction()
		{
			super(ACTION_CHANGE_TERM_DEFINITION_LOCATION);
		}
	}

	private class TermValueChangeAction extends MedViewAction // coupled to the more button
	{
		public void actionPerformed(ActionEvent e)
		{
			String filePath = mVD.createAndShowChangeTermValueDialog(owner);

			if (filePath != null)
			{
				termValField.setText(filePath); // document listener will place command in queue
			}
		}

		public TermValueChangeAction()
		{
			super(ACTION_CHANGE_TERM_VALUE_LOCATION);
		}
	}

	private class PDADataLocationChangeAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String filePath = mVD.createAndShowLoadMVDDialog(owner);

			if (filePath != null)
			{
				pdaLocationField.setText(filePath);
			}
		}
		public PDADataLocationChangeAction()
		{
			super(ACTION_CHANGE_PDA_DATA_LOCATION_LS_PROPERTY);
		}
	}

	// COMMANDS

	private class ChangeLocalLocationCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeLocalLocationCommand);
		}

		public void execute()
		{
			fireLocalLocationChangeRequested(location);
		}

		public ChangeLocalLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}

	private class ChangeServerLocationCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeServerLocationCommand);
		}

		public void execute()
		{
			fireServerLocationChangeRequested(location);
		}

		public ChangeServerLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}

	private class ChangePDALocationCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangePDALocationCommand);
		}

		public void execute()
		{
			firePDADataLocationChangeRequested(location);
		}

		public ChangePDALocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}

	private class ChangeUseRemoteCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeUseRemoteCommand);
		}

		public void execute()
		{
			fireUseRemoteDataHandlingChangeRequested(useRemote);
		}

		public ChangeUseRemoteCommand(boolean useRemote)
		{
			this.useRemote = useRemote;
		}

		private boolean useRemote;
	}

	private class ChangeTDLCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeTDLCommand);
		}

		public void execute()
		{
			fireTermDefinitionLocationChangeRequested(tDLPath);
		}

		public ChangeTDLCommand(String tDLPath)
		{
			this.tDLPath = tDLPath;
		}

		private String tDLPath;
	}

	private class ChangeTVLCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeTVLCommand);
		}

		public void execute()
		{
			fireTermValueLocationChangeRequested(tVLPath);
		}

		public ChangeTVLCommand(String tVLPath)
		{
			this.tVLPath = tVLPath;
		}

		private String tVLPath;
	}
}
