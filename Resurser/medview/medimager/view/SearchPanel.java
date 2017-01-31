package medview.medimager.view;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;

import java.util.*;

import javax.imageio.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;

import misc.foundation.*;

import misc.gui.components.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

public class SearchPanel extends JPanel implements MedImagerActionConstants, GUIConstants,
	ClipboardOwner, MedViewMediaConstants, MedImagerConstants
{
	// 'leaf' GUI components - invariant to usability model layer changes

	private JTextField searchField; // can gain focus

	private JButton searchButton; // can gain focus

	private ResultPanel resultPanel; // can gain focus (custom focus listening)

	private JSlider sizeSlider; // can gain focus

	private JButton selectAllButton; // can gain focus

	private JButton addButton; // can gain focus

	private ResultLabel[] resultLabels; // can gain focus

	private JPanel searchPanel; // focus transparent (upper panel)

	private JPanel sizePanel; // focus transparent

	private JLabel searchLabel; // focus transparent

	private JLabel sizeSmallLabel; // focus transparent

	private JLabel sizeLargeLabel; // focus transparent

	private JScrollPane resultScrollPane; // focus transparent

	// various other members

	private Action searchAction;

	private MedImagerModel model;

	private MedImagerFrame frame;

	private UsabilityModel usabilityModel;

	private ShellState state = new ShellState(); // contains actions

	private Vector internalFocusableComponentVector = new Vector(); // internal components that may gain focus


	// CONSTRUCTOR(S) AND RELATED METHODS

	public SearchPanel(MedImagerModel m, MedImagerFrame frame)
	{
		super();

		this.model = m;

		this.frame = frame;

		// set direct usability model reference

		this.usabilityModel = model.getUsabilityModel();

		// create and set shell state actions

		state.setAction(COPY_ACTION, new CopyAction());

		state.setAction(JOURNAL_ACTION, new JournalAction());

		state.setAction(ENLARGE_IMAGE_ACTION, new EnlargeImageAction());

		state.setAction(INFORMATION_ACTION, new InformationAction());

		// set up panel layout common to all usability layers

		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createEmptyBorder(CCS, CCS, CCS, CCS));

		// leaf component creation

		createGUILeafComponents(); // adds the focusable components to the internal component vector

		// attach focus handling to all internal components that might gain focus

		Enumeration enm = internalFocusableComponentVector.elements();

		while (enm.hasMoreElements())
		{
			((Component)enm.nextElement()).addFocusListener(new InternalFocusListener());
		}

		// attach focus handling to the panel itself

		addFocusListener(new InternalFocusListener());

		// listen to sizing changes

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				resultPanel.updatePanelHeight();
			}
		});

		// layout panel

		layoutPanel();

		// attach usability model listener

		usabilityModel.addUsabilityModelListener(new UsabilityModelListener()
		{
			public void functionalLayerStateChanged(UsabilityModelEvent e)
			{
				refreshPanelLayout();
			}
		});
	}

	private void createGUILeafComponents()
	{
		// search panel

		searchLabel = new JLabel("Sökord:");

		searchField = new JTextField();

		searchField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					searchAction.actionPerformed(null);
				}
			}

			public void keyReleased(KeyEvent e)
			{
			}

			public void keyTyped(KeyEvent e)
			{
			}
		});

		internalFocusableComponentVector.add(searchField);

		// search

		searchAction = new SearchAction();

		searchButton = new JButton(searchAction);

		internalFocusableComponentVector.add(searchButton);

		searchPanel = new JPanel(new GridBagLayout());

		// result panel

		resultPanel = new ResultPanel(new FlowLayout(FlowLayout.LEFT, CCS, CCS));

		internalFocusableComponentVector.add(resultPanel);

		resultScrollPane = new JScrollPane(resultPanel);

		resultScrollPane.getViewport().setBackground(Color.WHITE);

		// select and add

		selectAllButton = new JButton("Markera alla");

		internalFocusableComponentVector.add(selectAllButton);

		selectAllButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (resultLabels == null)
				{
					return;
				}

				for (int ctr = 0; ctr < resultLabels.length; ctr++)
				{
					resultLabels[ctr].setSelected(true);
				}
			}
		});

		addButton = new JButton("Lägg till");

		internalFocusableComponentVector.add(addButton);

		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Vector addVector = new Vector();

				for (int ctr = 0; ctr < resultLabels.length; ctr++)
				{
					if (resultLabels[ctr].isSelected())
					{
						addVector.add(resultLabels[ctr].getResult());
					}
				}

				DatabaseImageSearchResult[] r = new DatabaseImageSearchResult[addVector.size()];

				addVector.toArray(r);

				model.addToNode(r, model.getMyImagesRoot());
			}
		});

		// size panel

		int defSizeSliderValue = MedViewDataHandler.instance().getUserIntPreference(

			LAST_DATABASE_SEARCH_IMAGES_SIZE_SLIDER_VALUE_PROPERTY, 100, MedImagerConstants.class);

		sizeSlider = new JSlider(50, 120, defSizeSliderValue);

		internalFocusableComponentVector.add(sizeSlider);

		sizeSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				MedViewDataHandler.instance().setUserIntPreference(LAST_DATABASE_SEARCH_IMAGES_SIZE_SLIDER_VALUE_PROPERTY,

					sizeSlider.getValue(), MedImagerConstants.class);

				double scaleValue = ((double)sizeSlider.getValue()) / 100.0;

				if ((resultLabels == null) || (resultLabels.length == 0))
				{
					return;
				}
				else
				{
					scaleResultLabels(scaleValue);
				}
			}
		});

		sizeSmallLabel = new JLabel(MedViewDataHandler.instance().getImageIcon(TREES_SMALL_ICON));

		sizeLargeLabel = new JLabel(MedViewDataHandler.instance().getImageIcon(TREES_LARGE_ICON));

		sizePanel = new JPanel(new BorderLayout(CCS, 0));

		sizePanel.add(sizeSmallLabel, BorderLayout.WEST);

		sizePanel.add(sizeSlider, BorderLayout.CENTER);

		sizePanel.add(sizeLargeLabel, BorderLayout.EAST);
	}

	private void layoutPanel()
	{
		JPanel addPanel = new JPanel(new GridBagLayout()); // not a leaf, since it changes with usability layer

		GUIUtilities.gridBagAdd(searchPanel, searchLabel, 0, 0, 1, 1, 0, 0, CENT, new Insets(0, 0, 0, CCS), BOTH);

		GUIUtilities.gridBagAdd(searchPanel, searchField, 1, 0, 1, 1, 1, 0, CENT, new Insets(0, 0, 0, CCS), BOTH);

		GUIUtilities.gridBagAdd(searchPanel, searchButton, 2, 0, 1, 1, 0, 0, CENT, new Insets(0, 0, 0, 0), BOTH);

		GUIUtilities.gridBagAdd(this, searchPanel, 0, 0, 1, 1, 1, 0, CENT, new Insets(0, 0, CCS, 0), BOTH);

		GUIUtilities.gridBagAdd(this, resultScrollPane, 0, 1, 1, 1, 0, 1, CENT, new Insets(0, 0, CCS, 0), BOTH);

		GUIUtilities.gridBagAdd(this, sizePanel, 0, 2, 1, 1, 0, 0, CENT, new Insets(0, 0, CCS, 0), BOTH);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			GUIUtilities.gridBagAdd(addPanel, selectAllButton, 0, 0, 1, 1, 1, 0, WEST, new Insets(0, 0, 0, 0), NONE);

			GUIUtilities.gridBagAdd(addPanel, addButton, 1, 0, 1, 1, 1, 0, EAST, new Insets(0, 0, 0, 0), NONE);

			GUIUtilities.gridBagAdd(this, addPanel, 0, 3, 1, 1, 0, 0, CENT, new Insets(0, 0, 0, 0), BOTH);
		}
	}

	private void refreshPanelLayout()
	{
		removeAll(); // removes all leaf components from the layout panels (i.e. from the non-leaf components)

		layoutPanel(); // re-layouts the panel
	}


	// IMAGE SCALING

	private void scaleResultLabels(double scale)
	{
		try
		{
			int newWidth = (int)(resultLabels[0].getResult().getThumbImage().getWidth(null) * scale); // -> IOException

			int newHeight = (int)(resultLabels[0].getResult().getThumbImage().getHeight(null) * scale); // -> IOException

			for (int ctr = 0; ctr < resultLabels.length; ctr++)
			{
				resultLabels[ctr].setIcon(new ImageIcon(

					resultLabels[ctr].getResult().getThumbImage().getScaledInstance( // -> IOException

					newWidth, newHeight, Image.SCALE_FAST)));
			}

			resultPanel.updatePanelHeight();
		}
		catch (IOException exc) // should never happen here, since called by slider operation
		{
			exc.printStackTrace();
		}
	}

	private ImageIcon[] scaleDatabaseImageSearchResults(DatabaseImageSearchResult[] results,

		double scale, ProgressNotifiable notifiable) // null image icons if error during thumbnail obtaining
	{
		notifiable.setDescription("Skalar om resultat (totalt " + results.length + " st.)");

		ImageIcon[] retArr = new ImageIcon[results.length];

		notifiable.setTotal(retArr.length);

		for (int ctr = 0; ctr < results.length; ctr++)
		{
			notifiable.setCurrent(ctr);

			try
			{
				BufferedImage thumbImage = results[ctr].getThumbImage(); // -> IOException

				int width = (int)(thumbImage.getWidth(null) * scale);

				int height = (int)(thumbImage.getHeight(null) * scale);

				retArr[ctr] = new ImageIcon(thumbImage.getScaledInstance(

					width, height, Image.SCALE_FAST));
			}
			catch (IOException exc)
			{
				System.err.println("Could not read image input file " + results[ctr].getImageName());

				retArr[ctr] = null;
			}
		}

		return retArr;
	}


	// CLIPBOARDOWNER INTERFACE METHODS

	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		// deliberately a no-op implementation
	}


	// FOCUS RELATED METHODS

	protected void panelGainedFocusFromOutside()
	{
		if (frame.getCurrentState() != state)
		{
			frame.pluginState(state); // will plug in actions, but all shells are disabled

			updateActions();
		}
	}

	protected void panelLostFocusToOutside()
	{
		clearSelections();
	}


	// RESULT PANEL RELATED

	/**
	 * Called by various code within this panel, makes the
	 * actions keep a proper enabled/disabled state according
	 * to the selections made.
	 */
	public void updateActions()
	{
		DatabaseImageSearchResult[] selResults = getSelectedResults();

		state.getAction(COPY_ACTION).setEnabled(selResults.length != 0);

		state.getAction(JOURNAL_ACTION).setEnabled(selResults.length != 0);

		state.getAction(ENLARGE_IMAGE_ACTION).setEnabled(selResults.length != 0);

		state.getAction(INFORMATION_ACTION).setEnabled(selResults.length != 0);
	}

	/**
	 * Obtains all the currently selected results. Will return
	 * an empty array if no results are currently selected, or
	 * if there are no result labels at all.
	 *
	 * @return DatabaseImageSearchResult[]
	 */
	public DatabaseImageSearchResult[] getSelectedResults()
	{
		if (resultLabels == null)
		{
			return new DatabaseImageSearchResult[0];
		}
		else
		{
			Vector retVect = new Vector();

			for (int ctr = 0; ctr < resultLabels.length; ctr++)
			{
				if (resultLabels[ctr].isSelected())
				{
					retVect.add(resultLabels[ctr].getResult());
				}
			}

			DatabaseImageSearchResult[] res = new DatabaseImageSearchResult[retVect.size()];

			retVect.toArray(res);

			return res;
		}
	}

	/**
	 * Constructs a Transferable object for clipboard operations initiated
	 * within the search panel. This Transferable is intended for the local
	 * clipboard.
	 *
	 * @return Transferable
	 */
	public Transferable constructLocalSearchPanelTransferable()
	{
		final DatabaseImageSearchResult[] results = getSelectedResults(); // here since otherwise could change between drop and select

		return new Transferable()
		{
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (flavor.equals(MedImagerDataTransfer.dbImageFlavor))
				{
					Vector retVect = new Vector();

					for (int ctr = 0; ctr < results.length; ctr++)
					{
						retVect.add(results[ctr]);
					}

					return retVect;
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] {MedImagerDataTransfer.dbImageFlavor};
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return (flavor.equals(MedImagerDataTransfer.dbImageFlavor));
			}
		};
	}

	/**
	 * Constructs a Transferable object for clipboard operations initiated
	 * within the search panel. This Transferable is intended for the System
	 * clipboard.
	 *
	 * @return Transferable
	 */
	public Transferable constructSystemSearchPanelTransferable()
	{
		final DatabaseImageSearchResult[] results = getSelectedResults(); // here since otherwise could change between drop and select

		return new Transferable()
		{
			File[] tempFiles = null; // for performance reasons (getTransferData() is called many times in inter-system transfer)

			DataFlavor[] supportedFlavors = new DataFlavor[]
			{
				DataFlavor.stringFlavor, DataFlavor.imageFlavor,

				DataFlavor.javaFileListFlavor
			};

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (flavor.equals(DataFlavor.stringFlavor))
				{
					StringBuffer buffy = new StringBuffer();

					for (int ctr = 0; ctr < results.length; ctr++)
					{
						if (buffy.length() != 0)
						{
							buffy.append(",");
						}

						buffy.append(results[ctr] + "");
					}

					return buffy.toString();
				}
				else if (flavor.equals(DataFlavor.imageFlavor))
				{
					return results[0].getFullImage();
				}
				else if (flavor.equals(DataFlavor.javaFileListFlavor))
				{
					if (tempFiles == null)
					{
						tempFiles = new File[results.length];

						for (int ctr=0; ctr < results.length; ctr++)
						{
							tempFiles[ctr] = File.createTempFile(results[ctr].getEID().toString(), ".jpg");

							ImageIO.write(results[ctr].getFullImage(), "jpg", tempFiles[ctr]);
						}
					}

					Vector retVect = new Vector(tempFiles.length);

					for (int ctr=0; ctr<tempFiles.length; ctr++)
					{
						retVect.add(tempFiles[ctr]);
					}

					return retVect;
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				return supportedFlavors;
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				for (int ctr = 0; ctr < supportedFlavors.length; ctr++)
				{
					if (supportedFlavors[ctr].equals(flavor))
					{
						return true;
					}
				}

				return false;
			}
		};
	}

	/**
	 * Constructs a Transferable object for drag-and-drop operations
	 * initiated within the panel. Since the Transferable is not placed
	 * on the system clipboard, both the local and system transferable
	 * are put together here. Local transferables always have priority
	 * over system transferables.
	 */
	public Transferable constructSystemAndLocalSearchPanelTransferable()
	{
		return new Transferable()
		{
			private Transferable localTransferable = constructLocalSearchPanelTransferable();

			private Transferable systemTransferable = constructSystemSearchPanelTransferable();

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				if (localTransferable.isDataFlavorSupported(flavor))
				{
					return localTransferable.getTransferData(flavor);
				}
				else if (systemTransferable.isDataFlavorSupported(flavor))
				{
					return systemTransferable.getTransferData(flavor);
				}
				else
				{
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors()
			{
				DataFlavor[] localFlavors = localTransferable.getTransferDataFlavors();

				DataFlavor[] systemFlavors = systemTransferable.getTransferDataFlavors();

				Vector vector = new Vector();

				for (int ctr = 0; ctr < localFlavors.length; ctr++)
				{
					vector.add(localFlavors[ctr]);
				}

				for (int ctr = 0; ctr < systemFlavors.length; ctr++)
				{
					vector.add(systemFlavors[ctr]);
				}

				DataFlavor[] retArr = new DataFlavor[vector.size()];

				vector.toArray(retArr);

				return retArr;
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return ((localTransferable.isDataFlavorSupported(flavor)) ||

					(systemTransferable.isDataFlavorSupported(flavor)));
			}
		};
	}

	public void clearSelections()
	{
		resultPanel.clearSelections();
	}


	// INTERNAL FOCUS LISTENER

	/**
	 * Instances are applied to both the search panel itself, as
	 * well as to all the internal components (that may gain focus).
	 */
	private class InternalFocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if (enm.nextElement() == oComp)
				{
					return;
				}
			}

			panelGainedFocusFromOutside(); // if we get here, we gained focus from outside component
		}

		public void focusLost(FocusEvent e)
		{
			Component oComp = e.getOppositeComponent();

			if (frame.isOwnerComponent(oComp)) // i.e. a toolbar button or menu bar
			{
				return;
			}

			Enumeration enm = internalFocusableComponentVector.elements();

			while (enm.hasMoreElements())
			{
				if (enm.nextElement() == oComp)
				{
					return;
				}
			}

			panelLostFocusToOutside(); // if we get here, we lost focus to outside component
		}
	}


	// RESULT PANEL

	private class ResultPanel extends JPanel implements Scrollable
	{
		private void clearSelections()
		{
			if (resultLabels != null)
			{
				for (int ctr = 0; ctr < resultLabels.length; ctr++)
				{
					resultLabels[ctr].setSelected(false);
				}
			}
		}

		private void updatePanelHeight()
		{
			if ((resultLabels == null) || (resultLabels.length == 0))
			{
				revalidate();

				repaint();

				return;
			}

			int nrHor = 1;

			int labelWidth = resultLabels[0].getPreferredSize().width;

			int panelWidth = getSize().width; // tracks viewport width

			if (resultLabels.length > 1)
			{
				int labelXEnd = 2 * (CCS + labelWidth);

				while ((nrHor < resultLabels.length) && (labelXEnd <= panelWidth))
				{
					nrHor++;

					labelXEnd += (CCS + labelWidth);
				}
			}

			int imH = resultLabels[0].getPreferredSize().height;

			int nrImages = resultLabels.length;

			int nrVer = nrImages / nrHor;

			if ((nrImages % nrHor) > 0)
			{
				nrVer++;
			}

			int heightCalc = (nrVer * imH) + (nrVer * CCS) + CCS;

			setPreferredSize(new Dimension(getSize().width, heightCalc));

			revalidate();

			repaint();
		}

		public Dimension getPreferredScrollableViewportSize()
		{
			return new Dimension(10, 10); // doesn't matter here, since stretched
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return 10;
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return 100;
		}

		public boolean getScrollableTracksViewportHeight()
		{
			return false;
		}

		public boolean getScrollableTracksViewportWidth()
		{
			return true;
		}

		public ResultPanel(LayoutManager manager)
		{
			super(manager);

			setOpaque(false);

			addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					ResultPanel.this.requestFocusInWindow();
				}
			});
		}
	}


	// RESULT LABEL

	private class ResultLabel extends JLabel
	{
		private Border eBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

		private Border selectedBorder = BorderFactory.createLineBorder(Color.RED, 3);

		private Border unselLineBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);

		private Border unselectedBorder = BorderFactory.createCompoundBorder(eBorder, unselLineBorder);

		private DatabaseImageSearchResult result;

		private boolean selected = false;

		public ResultLabel(DatabaseImageSearchResult res, ImageIcon icon)
		{
			super(icon);

			this.result = res;

			setBorder(unselectedBorder);

			setToolTipText(res.getPID() + " (" + res.getImageName() + ")");

			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						state.getAction(ENLARGE_IMAGE_ACTION).actionPerformed(null);
					}
				}

				public void mousePressed(MouseEvent e)
				{
					resultPanel.requestFocusInWindow();

					if (!isSelected())
					{
						if (!e.isControlDown())
						{
							resultPanel.clearSelections();
						}

						setSelected(!isSelected());
					}
					else
					{
						if (e.isControlDown())
						{
							setSelected(!isSelected());
						}
					}
				}

				public void mouseReleased(MouseEvent e)
				{
					if (isSelected())
					{
						if (!e.isControlDown())
						{
							resultPanel.clearSelections();

							setSelected(true);
						}
					}

					if (e.isPopupTrigger())
					{
						JPopupMenu popupMenu = new JPopupMenu();

						popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(COPY_ACTION)));

						popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(JOURNAL_ACTION)));

						popupMenu.add(new SmallIconJMenuItem(frame.getShellForID(ENLARGE_IMAGE_ACTION)));

						popupMenu.show(ResultLabel.this, e.getX(), e.getY());
					}
				}
			});

			DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(ResultLabel.this,

				DnDConstants.ACTION_COPY, new DragGestureListener()
			{
				public void dragGestureRecognized(DragGestureEvent dge)
				{
					dge.startDrag(null, constructSystemAndLocalSearchPanelTransferable()); // null - default cursor
				}
			});
		}

		public boolean isSelected()
		{
			return selected;
		}

		public void setSelected(boolean sel)
		{
			this.selected = sel;

			if (selected)
			{
				setBorder(selectedBorder);
			}
			else
			{
				setBorder(unselectedBorder);
			}

			updateActions();
		}

		public DatabaseImageSearchResult getResult()
		{
			return result;
		}
	}


	// SEARCH ACTION

	private class SearchAction extends AbstractAction
	{
		public SearchAction()
		{
			super("Sök");
		}

		public void actionPerformed(ActionEvent e)
		{
			final Frame owner;

			Window windowAncestor = SwingUtilities.getWindowAncestor(SearchPanel.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}
			else
			{
				owner = null;
			}

			resultPanel.removeAll();

			NotifyingRunnable runnable = new NotifyingRunnable()
			{
				public void run()
				{
					// this code will be placed on a thread other than the event dispatch thread

					try
					{
						final DatabaseImageSearchResult[] results = model.performDatabaseSearch(

							searchField.getText(), getNotifiable()); // -> CouldNotParseException, CouldNotSearchException

						final ImageIcon[] scaledIcons = scaleDatabaseImageSearchResults(results,

							((double)sizeSlider.getValue()) / 100.0, getNotifiable());

						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								Vector resultLabelVector = new Vector(scaledIcons.length);

								for (int ctr = 0; ctr < scaledIcons.length; ctr++)
								{
									if (scaledIcons[ctr] != null) // null if thumbnail could not be obtained
									{
										resultLabelVector.add(new ResultLabel(results[ctr], scaledIcons[ctr]));
									}
								}

								resultLabels = new ResultLabel[resultLabelVector.size()];

								resultLabelVector.toArray(resultLabels);

								for (int ctr = 0; ctr < resultLabels.length; ctr++)
								{
									resultPanel.add(resultLabels[ctr]);
								}

								resultPanel.updatePanelHeight();
							}
						});
					}
					catch (final Exception exc)
					{
						exc.printStackTrace();

						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								MedViewDialogs.instance().createAndShowErrorDialog(owner, exc.getMessage());
							}
						});
					}
				}
			};

			MedViewDialogs.instance().startProgressMonitoring(owner, runnable);
		}
	}


	// COPY ACTION

	private class CopyAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			Clipboard sCB = Toolkit.getDefaultToolkit().getSystemClipboard();

			sCB.setContents(constructSystemSearchPanelTransferable(), SearchPanel.this);

			Clipboard lCB = MedImagerDataTransfer.instance().getLocalClipboard();

			lCB.setContents(constructLocalSearchPanelTransferable(), SearchPanel.this);
		}
	}


	// JOURNAL ACTION

	private class JournalAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			DatabaseImageSearchResult[] results = getSelectedResults();

			PatientIdentifier[] pids = new PatientIdentifier[results.length];

			for (int ctr = 0; ctr < results.length; ctr++)
			{
				pids[ctr] = results[ctr].getPID();
			}

			frame.showJournals(pids);
		}
	}


	// ENLARGE IMAGE ACTION

	private class EnlargeImageAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			final Frame owner;

			Window windowAncestor = SwingUtilities.getWindowAncestor(SearchPanel.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}
			else
			{
				owner = null;
			}

			DatabaseImageSearchResult[] results = getSelectedResults();

			Image[] images = new Image[results.length];

			String[] titles = new String[results.length];

			for (int ctr = 0; ctr < results.length; ctr++)
			{
				String pidS = results[ctr].getPID() + "";

				String eidS = results[ctr].getEID() + "";

				titles[ctr] = pidS + " - " + eidS;

				try
				{
					images[ctr] = results[ctr].getFullImage(); // -> IOException
				}
				catch (IOException exc)
				{
					MedViewDialogs.instance().createAndShowErrorDialog(owner,

						"Could not enlarge image " + results[ctr].getImageName());
				}
			}

			MedViewDialogs.instance().createAndShowImageDialogs(owner, titles, images);
		}
	}


	// INFORMATION ACTION

	private class InformationAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}

}
