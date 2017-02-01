package medview.medimager.view.settings;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*; // for unit testing only
import medview.medimager.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedImagerDataHandlingSCP extends SettingsContentPanel implements

	MedViewLanguageConstants, GUIConstants
{

	private MedImagerFrame frame;

	private DataLocationSettingsPanel dataPanel;

	public MedImagerDataHandlingSCP(CommandQueue commandQueue, MedImagerFrame frame)
	{
		super(commandQueue, new Object[] { frame });
	}

	public String getTabLS()
	{
		return TAB_DATA_LOCATIONS_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		frame = (MedImagerFrame) subConstructorData[0];
	}

	protected void createComponents()
	{
		dataPanel = new DataLocationSettingsPanel(commandQueue, frame);

		dataPanel.addDataPanelListener(new DataPanelListener());
	}

	protected void layoutPanel()
	{
		// layout the panel

		GUIUtilities.gridBagAdd(this, dataPanel,	0, 0, 1, 0, 1, 1, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),	0, 1, 0, 1, 1, 1, CENT, new Insets(0,0,0,0), BOTH);
	 }

	protected void settingsShown()
	{
		dataPanel.setLocalLocationText(frame.getModel().getLocalExaminationDataLocation());

		dataPanel.setTermDefinitionLocationText(frame.getModel().getLocalTermDefinitionLocation());

		dataPanel.setTermValueLocationText(frame.getModel().getLocalTermValueLocation());

		dataPanel.setServerLocationText(frame.getModel().getRemoteExaminationDataLocation());

		dataPanel.setUsesRemote(frame.getModel().usesRemoteDataHandling());
	 }

	private class DataPanelListener implements DataLocationSettingsPanel.DataLocationSettingsListener
	{
		public void localLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			frame.getModel().setLocalExaminationDataLocation(e.getRequestedLocation());
		}

		public void serverLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			frame.getModel().setRemoteExaminationDataLocation(e.getRequestedLocation());
		}

		public void useRemoteDataHandlingChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			frame.getModel().setUseRemoteDataHandling(e.getRequestedUseFlag());
		}

		public void termDefinitionLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			frame.getModel().setLocalTermDefinitionLocation(e.getRequestedLocation());
		}

		public void termValueLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			frame.getModel().setLocalTermValueLocation(e.getRequestedLocation());
		}
		public void pdaDataLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			// todo
		}
	}

/*
	 public String getTabLS()
	 {
		 return TAB_DATA_LOCATIONS_LS_PROPERTY;
	 }

	 public String getTabDescLS()
	 {
		 return TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY;
	 }


	 protected void settingsShown()
	 {
		 dataPanel.setLocalLocationText(mediator.getModel().getLocalExaminationDataLocation());

		 dataPanel.setTermDefinitionLocationText(mediator.getModel().getLocalTermDefinitionLocation());

		 dataPanel.setTermValueLocationText(mediator.getModel().getLocalTermValueLocation());

		 dataPanel.setServerLocationText(mediator.getModel().getServerLocation());

		 dataPanel.setUsesRemote(mediator.getModel().usesRemoteDataHandling());
	 }


	 protected void initSubMembers()
	 {
		 mediator = (MedSummaryFrame) subConstructorData[0];
	 }

	 protected void createComponents()
	 {
		 dataPanel = new DataLocationSettingsPanel(commandQueue, mediator);

		 dataPanel.addDataPanelListener(new Listener());
	 }

	 protected void layoutPanel()
	 {
		 // layout the panel

		 GUIUtilities.gridBagAdd(this, dataPanel,	0, 0, 1, 0, 1, 1, CENT, new Insets(0,0,0,0), BOTH);

		 GUIUtilities.gridBagAdd(this, Box.createGlue(),	0, 1, 0, 1, 1, 1, CENT, new Insets(0,0,0,0), BOTH);
	 }

	 public MedSummaryDataHandlingSCP(CommandQueue queue, MedSummaryFrame mediator)
	 {
		 super(queue, new Object[] { mediator });
	 }

	 private MedSummaryFrame mediator;

	 private DataLocationSettingsPanel dataPanel;



	 private class Listener implements DataLocationSettingsPanel.DataLocationSettingsListener
	 {
		 public void localLocationChangeRequested(DataLocationSettingsEvent e)
		 {
			 mediator.getModel().setLocalExaminationDataLocation(e.getRequestedLocation());
		 }

		 public void serverLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		 {
			 mediator.getModel().setServerLocation(e.getRequestedLocation());
		 }

		 public void useRemoteDataHandlingChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		 {
			 mediator.getModel().setUseRemoteDataHandling(e.getRequestedUseFlag());
		 }

		 public void termDefinitionLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		 {
			 mediator.getModel().setLocalTermDefinitionLocation(e.getRequestedLocation());
		 }

		 public void termValueLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		 {
			 mediator.getModel().setLocalTermValueLocation(e.getRequestedLocation());
		 }
	}
*/

/*
	// MEMBERS

	private JRadioButton useLocalRadioButton;

	private JRadioButton useRemoteRadioButton;

	private ButtonGroup localRemoteButtonGroup;

	private JLabel localMVDLabel;

	private JLabel remoteMVDLabel;

	private JLabel maximumHitLabel;

	private JLabel maximumHitNrLabel;

	private JSlider maximumHitSlider;

	private JTextField localMVDTextField;

	private JTextField remoteMVDTextField;

	private JButton localMVDMoreButton;

	private boolean suppressEvents = false;

	private MedImagerFrame frame;


	// CONSTRUCTOR(S) AND RELATED

	public MedImagerDataHandlingSCP(CommandQueue commandQueue, MedImagerFrame frame)
	{
		super(commandQueue, new Object[] { frame });
	}

	protected void initSubMembers() // overridden
	{
		frame = (MedImagerFrame) subConstructorData[0];
	}

	protected void createComponents()
	{
		// local button

		useLocalRadioButton = new JRadioButton("Använd lokal databas");

		useLocalRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (!suppressEvents && e.getStateChange() == ItemEvent.SELECTED)
				{
					commandQueue.addToQueue(new ChangeUseRemoteCommand(false));
				}
			}
		});

		// remote button

		useRemoteRadioButton = new JRadioButton("Använd fjärr-databas");

		useRemoteRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (!suppressEvents && e.getStateChange() == ItemEvent.SELECTED)
				{
					commandQueue.addToQueue(new ChangeUseRemoteCommand(true));
				}
			}
		});

		// button group

		localRemoteButtonGroup = new ButtonGroup();

		localRemoteButtonGroup.add(useLocalRadioButton);

		localRemoteButtonGroup.add(useRemoteRadioButton);

		// local MVD

		localMVDLabel = new JLabel("Lokal kunskapsbas:");

		localMVDTextField = new JTextField();

		localMVDTextField.setEditable(false);

		localMVDMoreButton = new MedViewMoreButton(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				String mVDLocation = MedViewDialogs.instance().createAndShowLoadMVDDialog(frame);

				if (mVDLocation != null)
				{
					localMVDTextField.setText(mVDLocation);

					commandQueue.addToQueue(new ChangeLocalMVDLocationCommand(mVDLocation));
				}
			}
		});

		// remote MVD

		remoteMVDLabel = new JLabel("Server IP (x.x.x.x):");

		remoteMVDTextField = new JTextField();

		remoteMVDTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) { process(e); }

			public void removeUpdate(DocumentEvent e) { process(e); }

			private void process(DocumentEvent e)
			{
				try
				{
					commandQueue.addToQueue(new ChangeRemoteMVDLocationCommand(

						e.getDocument().getText(0, e.getDocument().getLength())));
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					System.exit(1); // this should never happen - if it does, it's programmer error!
				}
			}
		});

		// maximum hits

		maximumHitLabel = new JLabel("Maximalt antal träffar:");

		maximumHitSlider = new JSlider(100, 1000, frame.getModel().getMaximumHits());

		maximumHitSlider.setPaintTicks(true);

		maximumHitSlider.setPaintTrack(true);

		maximumHitSlider.setMajorTickSpacing(100);

		maximumHitSlider.setMinorTickSpacing(10);

		maximumHitNrLabel = new JLabel(maximumHitSlider.getValue() + " st.");

		maximumHitSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (!maximumHitSlider.getValueIsAdjusting())
				{
					commandQueue.addToQueue(new ChangeMaximumHitsCommand(maximumHitSlider.getValue()));
				}

				maximumHitNrLabel.setText(maximumHitSlider.getValue() + " st.");
			}
		});
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, localMVDLabel, 		0, 0, 1, 1, 0, 0, CENT, new Insets(0,0,CGS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, localMVDTextField, 	1, 0, 1, 1, 1, 0, CENT, new Insets(0,0,CGS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, localMVDMoreButton, 	2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,CGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, maximumHitLabel,		0, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, maximumHitSlider,		1, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, maximumHitNrLabel,	2, 1, 1, 1, 0, 0, CENT, new Insets(0,0,CCS,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(), 	0, 2, 3, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}


	// TAB DESCRIPTION

	public String getTabLS()
	{
		return TAB_MEDIMAGER_SEARCH_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDIMAGER_SEARCH_DESCRIPTION_LS_PROPERTY;
	}


	// SETTINGS SHOWN / HIDDEN

	protected void settingsShown()
	{
		suppressEvents = true;

		maximumHitSlider.setValue(frame.getModel().getMaximumHits());

		localMVDTextField.setText(MedViewDataHandler.instance().getUserStringPreference(

			MedImagerConstants.LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY, "", MedImagerConstants.class));

		remoteMVDTextField.setText(MedViewDataHandler.instance().getUserStringPreference(

			MedImagerConstants.REMOTE_EXAMINATION_DATA_LOCATION_PROPERTY, "", MedImagerConstants.class));

		if (MedViewDataHandler.instance().getUserBooleanPreference(

			MedImagerConstants.USE_REMOTE_DATAHANDLING_PROPERTY, false, MedImagerConstants.class))
		{
			useRemoteRadioButton.setSelected(true);
		}
		else
		{
			useLocalRadioButton.setSelected(true);
		}

		suppressEvents = false;
	}

	protected void settingsHidden()
	{
	}


	private class ChangeUseRemoteCommand implements Command
	{
		public void execute()
		{
			frame.getModel().setUseRemoteDataHandling(useRemote);
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeUseRemoteCommand);
		}

		public ChangeUseRemoteCommand(boolean useRemote)
		{
			this.useRemote = useRemote;
		}

		private boolean useRemote;
	}

	private class ChangeLocalMVDLocationCommand implements Command
	{
		public void execute()
		{
			frame.getModel().setLocalExaminationDataLocation(location);
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeLocalMVDLocationCommand);
		}

		public ChangeLocalMVDLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}

	private class ChangeRemoteMVDLocationCommand implements Command
	{
		public void execute()
		{
			frame.getModel().setRemoteExaminationDataLocation(location);
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeRemoteMVDLocationCommand);
		}

		public ChangeRemoteMVDLocationCommand(String location)
		{
			this.location = location;
		}

		private String location;
	}


	private class ChangeMaximumHitsCommand implements Command
	{
		public void execute()
		{
			frame.getModel().setMaximumHits(hits);
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeMaximumHitsCommand);
		}

		public ChangeMaximumHitsCommand(int hits)
		{
			this.hits = hits;
		}

		private int hits;
	}


	// UNIT TEST

	public static void main(String[] args)
	{
		try
		{
			MedViewDataHandler.instance().changeLanguage("Svenska");

			CommandQueue cQ = new DefaultCommandQueue();

			MedImagerFrame frame = new MedImagerFrame(new MedImagerModel());

			SettingsContentPanel sCP = new MedImagerDataHandlingSCP(cQ, frame);

			SettingsDialog dialog = new SettingsDialog(cQ);

			dialog.addSettingsPanel(sCP);

			dialog.setLocationRelativeTo(null);

			dialog.show();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
*/
}
