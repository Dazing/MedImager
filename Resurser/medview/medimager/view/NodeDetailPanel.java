/**
 * @(#) NodeDetailPanel.java
 */

package medview.medimager.view;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.io.*;

import java.util.*;

import javax.sound.sampled.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * When it comes to actions and the enabled / disabled state of
 * the panel, the following rules apply:
 *
 * 1) The only thing governing whether the panel is enabled or
 *    disabled is the current node that the panel is supposed
 *    to show the information of. Thus, enabling / disabling
 *    of the components and also the actions is the sole
 *    responsibility of the displayNode() functionality.
 *
 * 2) The 'plug-in' of actions into the shell owner is another
 *    separate concern, and should be handled by the framework
 *    used in the MedImager context for this, i.e. by the focus
 *    related methods.
 *
 * 3) The text components in the panel should support 'pluggable'
 *    cut, copy, and paste functionality. Since the only place to
 *    plug such actions into the action state of the panel is in
 *    the focus listener (where the text component gaining focus is
 *    accessible), the focus listener also takes care of plugging in
 *    the focused text component's text actions into the state
 *    action wrappers. Since the Swing editor kit actions are by
 *    default always enabled, and I dont want that, a separate caret
 *    listener is attached to each of the text components, enabling
 *    or disabling the wrapper actions corresponding to the current
 *    selection in a text component.
 *
 * @author Fredrik Lindahl
 */
public class NodeDetailPanel extends JPanel implements GUIConstants,
	MedViewMediaConstants, MedImagerActionConstants, MedImagerConstants
{
	// audio recording and playback

	private boolean capturing = false; // flag indicating whether or not capturing audio atm

	private Action recordAction = new RecordAction(); // these are not included in the common shell

	private Action playAction = new PlayAction(); // these are not included in the common shell

	private Action stopAction = new StopAction(); // these are not included in the common shell

	// 'leaf' GUI components - invariant to usability model layer changes

	private JLabel imageLabel; // focus transparent

	private JScrollPane journalScrollPane; // focus transparent

	private JScrollPane noteScrollPane; // focus transparent

	private JButton fullImageButton; // can gain focus

	private JButton editImageButton; // can gain focus

	private JTextPane journalPane; // can gain focus

	private JTextPane notePane; // can gain focus

	private JButton generateJournalButton; // can gain focus

	private JLabel descriptionLabel; // focus transparent

	private JTextField descriptionField; // can gain focus

	private JButton recordButton; // can gain focus

	private JButton playButton; // can gain focus

	private JButton stopButton; // can gain focus

	// various other members

	private MedImagerModel model;

	private DefaultLeafNodeModel currNode = null; // the currently displayed node

	private Vector internalFocusableComponentVector = new Vector(); // internal components that may gain focus

	private ShellState state = new ShellState(); // contains actions

	private MedImagerFrame frame; // owns a shell state

	private boolean blockInputListening = false; // if programmatic inserts - dont listen

	private CaretListener caretListener = new CaretSelectionListener();

	private UsabilityModel usabilityModel;


	// CONSTRUCTOR(S) AND RELATED METHODS

	public NodeDetailPanel( MedImagerModel model, MedImagerFrame frame )
	{
		super();

		this.model = model;

		this.frame = frame;

		// set direct usability model reference

		this.usabilityModel = model.getUsabilityModel();

		// create and set shell state actions

		state.setAction(CUT_ACTION, new CutAction());

		state.setAction(COPY_ACTION, new CopyAction());

		state.setAction(PASTE_ACTION, new PasteAction());

		state.setAction(ENLARGE_IMAGE_ACTION, new EnlargeImageAction());

		state.setAction(INFORMATION_ACTION, new InformationAction());

		// set up panel layout common to all usability layers

		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createEmptyBorder(CCS,CCS,CCS,CCS));

		// leaf component creation

		createLeafGUIComponents(); // adds the focusable components to the internal component vector

		// attach focus handling to all internal components that might gain focus

		Enumeration enm = internalFocusableComponentVector.elements();

		while (enm.hasMoreElements())
		{
			attachFocusHandling((Component)enm.nextElement());
		}

		// layout the panel

		layoutPanel();

		// initially disable panel

		disableEntirePanel();

		// attach usability model listener

		usabilityModel.addUsabilityModelListener(new UsabilityModelListener()
		{
			public void functionalLayerStateChanged(UsabilityModelEvent e)
			{
				refreshPanelLayout();
			}
		});
	}

	private void createLeafGUIComponents()
	{
		// the image label

		imageLabel = new JLabel();

		imageLabel.setPreferredSize(new Dimension(320,240));

		imageLabel.setMinimumSize(new Dimension(320,240));

		imageLabel.setBorder(BorderFactory.createEtchedBorder());

		// journal pane

		journalPane = new JTextPane();

		journalPane.addCaretListener(caretListener);

		internalFocusableComponentVector.add(journalPane);

		journalScrollPane = new JScrollPane(journalPane);

		// image buttons

		fullImageButton = new JButton(state.getAction(ENLARGE_IMAGE_ACTION));

		fullImageButton.setText("Se Bild i Fullstorlek");

		internalFocusableComponentVector.add(fullImageButton);

		editImageButton = new JButton("Redigera Bild");

		internalFocusableComponentVector.add(editImageButton);

		// description panel

		descriptionLabel = new JLabel("Beskrivning:");

		descriptionField = new JTextField();

		descriptionField.setPreferredSize(new Dimension(0, GUIConstants.TEXTFIELD_HEIGHT_NORMAL));

		descriptionField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (!blockInputListening())
				{
					currNode.setDescription(descriptionField.getText());
				}
			}

			public void removeUpdate(DocumentEvent e)
			{
				if (!blockInputListening())
				{
					currNode.setDescription(descriptionField.getText());
				}
			}
		});

		descriptionField.addCaretListener(caretListener);

		internalFocusableComponentVector.add(descriptionField);

		// note pane

		notePane = new JTextPane();

		notePane.addCaretListener(caretListener);

		notePane.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (!blockInputListening())
				{
					currNode.setNote(notePane.getText());
				}
			}

			public void removeUpdate(DocumentEvent e)
			{
				if (!blockInputListening())
				{
					currNode.setNote(notePane.getText());
				}
			}
		});

		internalFocusableComponentVector.add(notePane);

		noteScrollPane = new JScrollPane(notePane);

		// play, stop, record buttons

		recordButton = new JButton(recordAction);

		recordButton.setText("");

		internalFocusableComponentVector.add(recordButton);

		playButton = new JButton(playAction);

		playButton.setText("");

		internalFocusableComponentVector.add(playButton);

		stopButton = new JButton(stopAction);

		stopButton.setText("");

		internalFocusableComponentVector.add(stopButton);

		// generate journal

		generateJournalButton = new JButton("Generera ny Journal");

		internalFocusableComponentVector.add(generateJournalButton);
	}

	private void layoutPanel()
	{
		// image buttons

		JPanel imageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,CCS,0));

		imageButtonPanel.add(fullImageButton);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_EDIT))
		{
			imageButtonPanel.add(editImageButton);
		}

		// description

		JPanel descriptionPanel = new JPanel(new BorderLayout(CCS,CCS));

		descriptionPanel.add(descriptionLabel, BorderLayout.WEST);

		descriptionPanel.add(descriptionField, BorderLayout.CENTER);

		// audio recording and playback (dictation)

		JPanel dictationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,CCS,0));

		dictationPanel.add(recordButton);

		dictationPanel.add(playButton);

		dictationPanel.add(stopButton);

		// journal

		JPanel journalButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,CCS,0));

		journalButtonPanel.add(generateJournalButton);

		// layout

		GUIUtilities.gridBagAdd(this, imageLabel, 		0,0,1,1,0,0,CENT,new Insets(0,0,CCS,CCS),BOTH);

		GUIUtilities.gridBagAdd(this, journalScrollPane, 	1,0,1,4,1,0,CENT,new Insets(0,0,CCS,0),BOTH);

		GUIUtilities.gridBagAdd(this, imageButtonPanel,		0,1,1,1,0,0,CENT,new Insets(0,0,CCS,CCS),BOTH);

		GUIUtilities.gridBagAdd(this, descriptionPanel,		0,2,1,1,0,0,CENT,new Insets(0,0,CCS,CCS),BOTH);

		GUIUtilities.gridBagAdd(this, noteScrollPane,		0,3,1,1,0,1,CENT,new Insets(0,0,CCS,CCS),BOTH);

		GUIUtilities.gridBagAdd(this, dictationPanel,		0,4,1,1,0,0,CENT,new Insets(0,0,0,CCS),BOTH);

		GUIUtilities.gridBagAdd(this, journalButtonPanel,	1,4,1,1,0,0,CENT,new Insets(0,0,0,0),BOTH);
	}

	private void refreshPanelLayout()
	{
		removeAll();

		layoutPanel();
	}


	// INPUT BLOCKING

	private void setBlockInputListening(boolean flag)
	{
		this.blockInputListening = flag;
	}

	private boolean blockInputListening()
	{
		return blockInputListening;
	}


	// FOCUS (SHELLOWNER FRAMEWORK) RELATED

	private class NodeDetailPanelFocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			if (frame.isOwnerComponent(oComp))	// i.e. a toolbar button or menu bar
			{
				return;
			}

			// plug in text actions if a text component gained focus

			if (e.getSource() instanceof JTextComponent)
			{
				JTextComponent textComponent = (JTextComponent) e.getSource();

				Action[] actions = textComponent.getActions();

				for (int ctr=0; ctr<actions.length; ctr++)
				{
					if (actions[ctr].getValue(Action.NAME).equals(DefaultEditorKit.cutAction))
					{
						actions[ctr].setEnabled(false);	// enable / disable decided by caret listener

						((TextActionWrapperAction)state.getAction(CUT_ACTION)).setWrappedAction(actions[ctr]);
					}
					else if (actions[ctr].getValue(Action.NAME).equals(DefaultEditorKit.copyAction))
					{
						actions[ctr].setEnabled(false);	// enable / disable decided by caret listener

						((TextActionWrapperAction)state.getAction(COPY_ACTION)).setWrappedAction(actions[ctr]);
					}
					else if (actions[ctr].getValue(Action.NAME).equals(DefaultEditorKit.pasteAction))
					{
						((TextActionWrapperAction)state.getAction(PASTE_ACTION)).setWrappedAction(actions[ctr]);
					}
				}
			}
			else
			{
				((TextActionWrapperAction)state.getAction(CUT_ACTION)).setWrappedAction(null);

				((TextActionWrapperAction)state.getAction(COPY_ACTION)).setWrappedAction(null);

				((TextActionWrapperAction)state.getAction(PASTE_ACTION)).setWrappedAction(null);
			}

			// if the opposite component is an internal, we already have plugged in the actions

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if ((Component)enm.nextElement() == oComp)
				{
					return;
				}
			}

			// if we get here, we need to plug in action state into shell

			panelGainedFocusFromOutside();
		}

		public void focusLost(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			if (frame.isOwnerComponent(oComp))	// i.e. a toolbar button or menu bar
			{
				return;
			}

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if ((Component)enm.nextElement() == oComp)
				{
					return; // focus was lost to other internal component
				}
			}

			panelLostFocusToOutside(); // focus lost to outside component - will disable all actions
		}
	}

	public void attachFocusHandling(Component comp)
	{
		comp.addFocusListener(new NodeDetailPanelFocusListener());
	}

	protected void panelGainedFocusFromOutside()
	{
		frame.pluginState(state);
	}

	protected void panelLostFocusToOutside()
	{
	}


	// DISPLAY

	public NodeModel getDisplayedNode( ) // can return null
	{
		return currNode;
	}

	public void displayNode(NodeModel node)
	{
		setBlockInputListening(true);

		if ((node == null) || !(node instanceof DefaultLeafNodeModel))
		{
			this.currNode = null;

			// disable entire panel

			disableEntirePanel();
		}
		else
		{
			this.currNode = (DefaultLeafNodeModel) node;

			imageLabel.setIcon(new ImageIcon(currNode.getMediumImage()));

			descriptionField.setText(currNode.getDescription());

			notePane.setText(currNode.getNote());

			// enable non action components (text actions disabled / enabled according to focus)

			enableNonActionComponents();

			// enable 'enlarge image' and 'image information' actions

			state.getAction(ENLARGE_IMAGE_ACTION).setEnabled(true);

			state.getAction(INFORMATION_ACTION).setEnabled(true);

			// enable audio actions according to node audio data

			recordAction.setEnabled(true);

			playAction.setEnabled(currNode.containsAudioData());

			stopAction.setEnabled(false);

			// journal

			new Thread(new Runnable()
			{
				public void run()
				{
					final StyledDocument[] docs = frame.getJournals(

						new PatientIdentifier[] { currNode.getPID() });

					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							journalPane.setDocument(docs[0]);
						}
					});
				}

			}).start();
		}

		setBlockInputListening(false);
	}


	// ENABLING AND DISABLING PANEL COMPONENTS

	private void enableNonActionComponents()
	{
		setNonActionComponentsEnabled(true);
	}

	public void disableEntirePanel()
	{
		setNonActionComponentsEnabled(false);

		disableAllActions();
	}

	private void setNonActionComponentsEnabled(boolean flag)
	{
		// set component enabled status

		imageLabel.setEnabled(flag);

		journalPane.setEnabled(flag);

		editImageButton.setEnabled(flag);

		descriptionLabel.setEnabled(flag);

		descriptionField.setEnabled(flag);

		notePane.setEnabled(flag);

		generateJournalButton.setEnabled(flag);

		// clear text and images if disabled

		if (!flag)
		{
			imageLabel.setIcon(null);

			setBlockInputListening(true);

			journalPane.setText("");

			descriptionField.setText("");

			notePane.setText("");

			setBlockInputListening(false);
		}
	}


	// ENABLING / DISABLING OF ACTIONS

	public void disableAllActions()
	{
		// disable text actions

		state.getAction(CUT_ACTION).setEnabled(false);

		state.getAction(COPY_ACTION).setEnabled(false);

		state.getAction(PASTE_ACTION).setEnabled(false);

		// disable non text / audio actions

		state.getAction(ENLARGE_IMAGE_ACTION).setEnabled(false);

		state.getAction(INFORMATION_ACTION).setEnabled(false);

		// disable audio actions

		recordAction.setEnabled(false);

		playAction.setEnabled(false);

		stopAction.setEnabled(false);
	}


	// TEXT ACTION WRAPPER

	private abstract class TextActionWrapperAction extends AbstractAction
	{
		private Action wrappedAction = null;

		private PropertyChangeListener propList = null;

		public void actionPerformed(ActionEvent e)
		{
			if (wrappedAction != null)
			{
				wrappedAction.actionPerformed(e);
			}
		}

		public void setWrappedAction(Action action)
		{
			// remove previously wrapped action's property listener

			if (wrappedAction != null)
			{
				wrappedAction.removePropertyChangeListener(propList);
			}

			// update currently wrapped action

			this.wrappedAction = action;

			if (action != null)
			{
				// attach enabled / disabled listener to wrapped action

				propList = new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						setEnabled(wrappedAction.isEnabled());
					}
				};

				wrappedAction.addPropertyChangeListener(propList);

				setEnabled(wrappedAction.isEnabled());
			}
			else
			{
				setEnabled(false);
			}
		}

		public TextActionWrapperAction(String name)
		{
			super(name);
		}
	}


	// CUT ACTION

	private class CutAction extends TextActionWrapperAction
	{
		public CutAction()
		{
			super("Klipp ut");
		}
	}


	// COPY ACTION

	private class CopyAction extends TextActionWrapperAction
	{
		public CopyAction()
		{
			super("Kopiera");
		}
	}


	// PASTE ACTION

	private class PasteAction extends TextActionWrapperAction
	{
		public PasteAction()
		{
			super("Klistra in");
		}
	}


	// ENLARGE IMAGE ACTION

	private class EnlargeImageAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(NodeDetailPanel.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			String[] titles = new String[] { currNode.getPID() + " - " + currNode.getEID() };

			Image[] images = new Image[] { currNode.getFullImage() };

			MedViewDialogs.instance().createAndShowImageDialogs(owner, titles, images);
		}

		public EnlargeImageAction()
		{
			super("Fullstorlek...");
		}
	}


	// INFORMATION ACTION

	private class InformationAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}

		public InformationAction()
		{
			super("Ytterligare information...");
		}
	}


	// CARET SELECTION LISTENER

	private class CaretSelectionListener implements CaretListener
	{
		public void caretUpdate(CaretEvent e)
		{
			boolean sourceEditable = ((JTextComponent)e.getSource()).isEditable();

			state.getAction(CUT_ACTION).setEnabled((e.getDot() != e.getMark()) && sourceEditable);

			state.getAction(COPY_ACTION).setEnabled(e.getDot() != e.getMark());
		}
	}


	// RECORD BUTTON ACTION LISTENER

	private class RecordAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				// starting recording - toggle actions

				this.setEnabled(false);

				playAction.setEnabled(false);

				stopAction.setEnabled(true);

				// record audio

				DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);

				final TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(lineInfo);

				targetDataLine.open(AUDIO_FORMAT);

				targetDataLine.start();

				Thread captureThread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							byte[] buffer = new byte[10000];

							ByteArrayOutputStream bout = new ByteArrayOutputStream();

							capturing = true;

							while (capturing)
							{
								int byteCount = targetDataLine.read(buffer, 0, buffer.length);

								if (byteCount > 0)
								{
									bout.write(buffer, 0, byteCount);
								}
							}

							final byte[] audioData = bout.toByteArray();

							currNode.setAudioDataObtainer(new AudioDataObtainer()
							{
								public byte[] getAudioByteArray() throws IOException
								{
									return audioData;
								}
							});

							bout.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});

				captureThread.start();
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		public RecordAction()
		{
			super("Spela in", MedViewDataHandler.instance().getImageIcon(RECORD_IMAGE_ICON_14));
		}
	}

	// PLAY BUTTON ACTION LISTENER

	private class PlayAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				// starting playback - toggle actions

				recordAction.setEnabled(false);

				this.setEnabled(false);

				stopAction.setEnabled(true);

				// playback audio

				InputStream bin = new ByteArrayInputStream(currNode.getAudioData());

				long nrOfFrames = currNode.getAudioData().length / AUDIO_FORMAT.getFrameSize();

				final AudioInputStream audioInputStream = new AudioInputStream(bin, AUDIO_FORMAT, nrOfFrames);

				DataLine.Info info = new DataLine.Info(SourceDataLine.class, AUDIO_FORMAT);

				final SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

				sourceDataLine.open(AUDIO_FORMAT);

				sourceDataLine.start();

				Thread playThread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							byte[] buffer = new byte[10000];

							int byteCount = 0;

							while ((byteCount = audioInputStream.read(buffer, 0, buffer.length)) != -1)
							{
								sourceDataLine.write(buffer, 0, byteCount);
							}

							sourceDataLine.drain();

							sourceDataLine.close();

							// playback finished - toggle actions

							recordAction.setEnabled(true);

							PlayAction.this.setEnabled(true);

							stopAction.setEnabled(false);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});

				playThread.start();
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		public PlayAction()
		{
			super("Spela upp", MedViewDataHandler.instance().getImageIcon(PLAY_IMAGE_ICON_14));
		}
	}

	// STOP BUTTON ACTION LISTENER

	private class StopAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			capturing = false;

			// stopped listening or recording - toggle actions

			recordAction.setEnabled(true);

			playAction.setEnabled(true);

			this.setEnabled(false);
		}

		public StopAction()
		{
			super("Stopp", MedViewDataHandler.instance().getImageIcon(STOP_IMAGE_ICON_14));
		}
	}
}
