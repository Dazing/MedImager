package medview.foresttools;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;

import misc.foundation.*;

import misc.gui.components.*;
import misc.gui.constants.*;

import medview.common.data.*;
import medview.common.dialogs.*;

import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

import misc.foundation.io.*;

public class PCodeMapper extends MainShell
{
	protected int getMSHeight()
	{
		return 375;
	}

	protected int getMSWidth()
	{
		return 350;
	}

	protected String getMSTitle()
	{
		return "PCode-Mapper 1.0 Beta 1";
	}

	protected Insets getPadding()
	{
		return new Insets(5,5,5,5);
	}

	protected boolean usesStatusBar()
	{
		return false;
	}

	public PCodeMapper()
	{
		super();

		map = new HashMap();					// the map containing pcode -> pid

		filesToMapVector = new Vector();		// vector containing files in need of mapping

		filesNotToMapVector = new Vector();		// vector containing files not needing to be mapped

		// layout panel

		filesInNeedOfMappingListModel = new DefaultListModel();

		filesInNeedOfMappingList = new JList(filesInNeedOfMappingListModel);

		filesInNeedOfMappingList.setToolTipText("Files in selected mvd with no PID");

		JScrollPane jSP = new JScrollPane(filesInNeedOfMappingList);

		forestLabel = new JLabel("MVD:");

		forestField = new JTextField();								// field containing path to MVD

		forestField.setEditable(false);								// not editable, must be chosen

		forestField.setBackground(Color.white);

		int buttonWidth = GUIConstants.BUTTON_WIDTH_MORE;

		int buttonHeight = GUIConstants.BUTTON_HEIGHT_NORMAL;

		Dimension prefDim = new Dimension(buttonWidth, buttonHeight);

		changeForestAction = new ChangeForestAction();

		changeForestButton = new JButton(changeForestAction);

		changeForestButton.setPreferredSize(prefDim);

		mappingLabel = new JLabel("Mapping:");

		mappingField = new JTextField();							// field containing path to map file

		mappingField.setEditable(false);							// not editable, must be chosen

		mappingField.setBackground(Color.white);

		changeMappingButton = new JButton(new ChangeMappingAction());

		changeMappingButton.setPreferredSize(prefDim);

		nrOfPatientsToMapDescLabel = new JLabel("Nr of tree files in mvd with no PID:");

		nrOfPatientsToMapLabel = new JLabel("0"); 					// '0' initially displayed

		nrOfPatientsNotToMapDescLabel = new JLabel("Nr of tree files already containing PID:");

		nrOfPatientsNotToMapLabel = new JLabel("0"); 				// '0' initially displayed

		applyMappingsAction = new ApplyMappingsAction();

		applyButton = new JButton(applyMappingsAction);				// apply button initially disabled

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

		int cGS = GUIConstants.COMPONENT_GROUP_SPACING;

		mainPanel = new JPanel(gbl);

		gbc.gridx = 0;

		gbc.gridy = 0;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,cCS);

		mainPanel.add(forestLabel, gbc);

		gbc.gridx = 1;

		gbc.gridy = 0;

		gbc.weightx = 1;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.BOTH;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,cCS);

		mainPanel.add(forestField, gbc);

		gbc.gridx = 2;

		gbc.gridy = 0;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(changeForestButton, gbc);

		gbc.gridx = 0;

		gbc.gridy = 1;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,cCS);

		mainPanel.add(mappingLabel, gbc);

		gbc.gridx = 1;

		gbc.gridy = 1;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.BOTH;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,cCS);

		mainPanel.add(mappingField, gbc);

		gbc.gridx = 2;

		gbc.gridy = 1;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(changeMappingButton, gbc);

		gbc.gridx = 0;

		gbc.gridy = 2;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 2;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cCS,cCS);

		mainPanel.add(nrOfPatientsToMapDescLabel, gbc);

		gbc.gridx = 2;

		gbc.gridy = 2;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.EAST;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(nrOfPatientsToMapLabel, gbc);

		gbc.gridx = 0;

		gbc.gridy = 3;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 2;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.WEST;

		gbc.insets = new Insets(0,0,cGS,cCS);

		mainPanel.add(nrOfPatientsNotToMapDescLabel, gbc);

		gbc.gridx = 2;

		gbc.gridy = 3;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.EAST;

		gbc.insets = new Insets(0,0,cGS,0);

		mainPanel.add(nrOfPatientsNotToMapLabel, gbc);

		gbc.gridx = 0;

		gbc.gridy = 4;

		gbc.weightx = 0;

		gbc.weighty = 1;

		gbc.gridheight = 1;

		gbc.gridwidth = 3;

		gbc.fill = GridBagConstraints.BOTH;

		gbc.anchor = GridBagConstraints.CENTER;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(jSP, gbc);

		gbc.gridx = 0;

		gbc.gridy = 5;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 3;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.CENTER;

		gbc.insets = new Insets(0,0,0,0);

		mainPanel.add(applyButton, gbc);

		// set main panel as center component

		setCenterComponent(mainPanel);
	}


	private HashMap map;


	private Vector filesToMapVector;

	private Vector filesNotToMapVector;

	private JLabel nrOfPatientsNotToMapLabel;

	private JLabel nrOfPatientsNotToMapDescLabel;

	private JLabel nrOfPatientsToMapLabel;

	private JLabel nrOfPatientsToMapDescLabel;


	private JButton changeForestButton;

	private JButton changeMappingButton;


	private JLabel forestLabel;

	private JLabel mappingLabel;

	private JTextField forestField;

	private JTextField mappingField;


	private JPanel mainPanel;

	private JPanel leftPanel;

	private JPanel rightPanel;


	private JList filesInNeedOfMappingList;

	private DefaultListModel filesInNeedOfMappingListModel;

	private JLabel filesInNeedOfMappingLabel;


	private JButton applyButton;

	private ApplyMappingsAction applyMappingsAction;

	private ChangeForestAction changeForestAction;


	private static MedViewDialogs mVD = MedViewDialogs.instance();

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();





// ----------------------------------------------------------------------------------------------
// ******************************************* ACTIONS ******************************************
// ----------------------------------------------------------------------------------------------

	private class ChangeForestAction extends AbstractAction
	{
		public void readForest(final String path)
		{
			filesToMapVector.clear();					// clear vector of files to map

			filesNotToMapVector.clear();				// clear vector of files not to map

			filesInNeedOfMappingListModel.clear();		// clear file listing

			forestField.setText(path);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					forestField.setScrollOffset(0);
				}
			});

			NotifyingRunnable runnable = new NotifyingRunnable()
			{
				public void run()
				{
					String fileSep = File.separator;

					String forestPath = path + fileSep + "Forest.forest";

					File forestDir = new File(forestPath);

					File[] files = forestDir.listFiles(new TreeFileFilter());

					getNotifiable().setTotal(files.length);

					getNotifiable().setDescription("Reading tree files from mvd");

					Tree tree = null;

					for (int ctr=0; ctr<files.length; ctr++)
					{
						try
						{
							getNotifiable().setCurrent(ctr);

							tree = MedViewUtilities.loadTree(files[ctr]);

							if (tree.getNode("PID") != null)
							{
								filesNotToMapVector.add(files[ctr].getAbsolutePath());
							}
							else	// store strings instead of File's because of memory
							{
								filesToMapVector.add(files[ctr].getAbsolutePath());

								final String fileName = files[ctr].getName();

								SwingUtilities.invokeLater(new Runnable()
								{
									public void run()
									{
										filesInNeedOfMappingListModel.addElement(fileName);
									}
								});
							}
						}
						catch (IOException e)
						{
							System.err.print("Warning: file '" + files[ctr]);

							System.err.print("' could not be parsed because ");

							System.err.println("of an IO exception:");

							e.printStackTrace();
						}
					}
				}
			};

			MedViewDialogs.instance().startProgressMonitoring(PCodeMapper.this, runnable);

			if ((mappingField.getText() != null) && (mappingField.getText().length() != 0))
			{
				applyMappingsAction.setEnabled(true);
			}

			nrOfPatientsToMapLabel.setText(filesToMapVector.size() + "");

			nrOfPatientsNotToMapLabel.setText(filesNotToMapVector.size() + "");
		}

		public void actionPerformed(ActionEvent e)
		{
			Component parent = PCodeMapper.this;

			String path = mVD.createAndShowLoadMVDDialog(PCodeMapper.this);

			if (path != null) // null -> dialog cancelled
			{
				readForest(path);
			}
		}

		public ChangeForestAction()
		{
			super();

			putValue(Action.NAME, "...");

			putValue(Action.SHORT_DESCRIPTION, "Change location of mvd directory");
		}
	}

	private class ChangeMappingAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setPreferredSize(GUIConstants.FILECHOOSER_SIZE_STANDARD);

			fileChooser.setAcceptAllFileFilterUsed(false);

			fileChooser.setFileFilter(new MappingFileFilter());

			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			fileChooser.setCurrentDirectory(new File(mVDH.getUserHomeDirectory()));

			int state = fileChooser.showDialog(PCodeMapper.this, "Choose mapping file");

			if (state == JFileChooser.APPROVE_OPTION)
			{
				map.clear();

				File chosenFile = fileChooser.getSelectedFile();

				mappingField.setText(chosenFile.getAbsolutePath());

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						mappingField.setScrollOffset(0);
					}
				});

				try
				{
					String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

					FileInputStream inStream = new FileInputStream(chosenFile);

					InputStreamReader inReader = new InputStreamReader(inStream, enc);

					BufferedReader buffReader = new BufferedReader(inReader);

					String currLine = null; StringTokenizer t = null;

					while ((currLine = buffReader.readLine()) != null)
					{
						boolean c1 = currLine.startsWith("#");

						boolean c2 = currLine.startsWith(" ");

						boolean c3 = currLine.length() == 0;

						if (!(c1 || c2 || c3))
						{
							t = new StringTokenizer(currLine, "=", false);	// dont return delimiters

							String pCode = t.nextToken().trim();

							if (t.hasMoreTokens())
							{
								String pid = t.nextToken().trim();

								if (pid.length() == 12)
								{
									String first = pid.substring(0,8);

									String last = pid.substring(8,12);

									pid = first + "-" + last;
								}

								if (pid.length() == 13)
								{
									map.put(pCode, pid); // format: B00109390=193904234865
								}
							}
							else
							{
								String m1 = "Null second token for '" + pCode;

								String m2 = "' -> will not add PID node to ";

								String m3 = "corresponding tree file";

								System.out.println(m1 + m2 + m3);
							}
						}
					}

					if ((forestField.getText() != null) && (forestField.getText().length() != 0))
					{
						applyMappingsAction.setEnabled(true);
					}
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					String m = "Invalid map file: " + exc.getMessage();

					mVD.createAndShowErrorDialog(PCodeMapper.this, m);
				}
			}
		}

		public ChangeMappingAction()
		{
			super();

			putValue(Action.NAME, "...");

			putValue(Action.SHORT_DESCRIPTION, "Change location of mapping file");
		}
	}

	private class ApplyMappingsAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				NotifyingRunnable runnable = new NotifyingRunnable()
				{
					public void run()
					{
						getNotifiable().setTotal(filesToMapVector.size());

						getNotifiable().setDescription("Applying mappings");

						Enumeration enm = filesToMapVector.elements();

						int fileCounter = 1;

						while (enm.hasMoreElements())
						{
							getNotifiable().setCurrent(fileCounter++);

							File currFile = null;

							try
							{
								currFile = new File((String)enm.nextElement());

								Tree root = MedViewUtilities.loadTree(currFile);

								Tree pCodeNode = root.getNode("P-code");

								String pCode = pCodeNode.getFirstChild().getValue();

								if (map.containsKey(pCode))
								{
									String pid = (String) map.get(pCode);

									Tree pidNode = new TreeBranch("PID");

									pidNode.addChild(new TreeLeaf(pid));

									pCodeNode.getParent().addChild(pidNode);

									currFile.delete();					// delete old .tree file

									currFile.createNewFile();			// create new .tree file with same name

									FileOutputStream fos = new FileOutputStream(currFile);

									OutputStreamWriter writer = new OutputStreamWriter(fos);

									writer.write(root.toString());		// write tree with new PID node to file

									writer.flush();						// flush the output to the file
								}
							}
							catch (Exception exc)
							{
								String m1 = "Warning: file '" + currFile + "'";

								String m2 = " could not be mapped due to:";

								System.err.println(m1 + m2 + exc.getMessage());
							}
						}
					};
				};

				final Thread t = MedViewDialogs.instance().startProgressMonitoring(PCodeMapper.this, runnable);

				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							t.join();

							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									String currForestPath = forestField.getText();

									changeForestAction.readForest(currForestPath);
								}
							});
						}
						catch (Exception exc)
						{
							exc.printStackTrace();
						}
					}
				}).start();
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		public ApplyMappingsAction()
		{
			super();

			putValue(Action.NAME, "Apply Mappings");

			putValue(Action.SHORT_DESCRIPTION, "Applies the mappings to the files in the mvd");

			setEnabled(false); // initially
		}
	}

// ----------------------------------------------------------------------------------------------
// **********************************************************************************************
// ----------------------------------------------------------------------------------------------





// ----------------------------------------------------------------------------------------------
// **************************************** FILE FILTERS ****************************************
// ----------------------------------------------------------------------------------------------

	private class MappingFileFilter extends javax.swing.filechooser.FileFilter
	{
		public boolean accept(File f)
		{
			if (f.isDirectory()) { return true; }

			return f.getName().toLowerCase().endsWith(".map");
		}

		public String getDescription()
		{
			return "PCode to PID mapping files (.map)";
		}
	}

	private class TreeFileFilter implements java.io.FileFilter
	{
		public boolean accept(File pathName)
		{
			return pathName.getAbsolutePath().endsWith(".tree");
		}
	}

// ----------------------------------------------------------------------------------------------
// **********************************************************************************************
// ----------------------------------------------------------------------------------------------





// ----------------------------------------------------------------------------------------------
// **************************************** MAIN METHOD *****************************************
// ----------------------------------------------------------------------------------------------

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			String m1 = "Could not set system look ";

			String m2 = "and feel, using platform-independent";

			System.out.println(m1 + m2);
		}

		PCodeMapper m = new PCodeMapper();

		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String iconID = MedViewMediaConstants.FRAME_IMAGE_ICON;

		m.setIconImage(mVDH.getImage(iconID));

		m.show();
	}

// ----------------------------------------------------------------------------------------------
// **********************************************************************************************
// ----------------------------------------------------------------------------------------------

}
