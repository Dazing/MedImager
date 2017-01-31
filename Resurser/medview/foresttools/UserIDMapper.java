package medview.foresttools;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;

import misc.foundation.*;

import misc.gui.components.*;
import misc.gui.constants.*;

import medview.common.dialogs.*;
import medview.common.data.*;

import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

import misc.foundation.io.*;

public class UserIDMapper extends MainShell
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
		return "User-ID-Mapper 1.0 Beta 1";
	}

	protected Insets getPadding()
	{
		return new Insets(5,5,5,5);
	}

	protected boolean usesStatusBar()
	{
		return false;
	}

	public UserIDMapper()
	{
		super();

		map = new HashMap();					// the map containing pcode -> pid

		filesToMapVector = new Vector();		// vector containing files in need of mapping

		// layout panel

		filesToMapListModel = new DefaultListModel();

		filesToMapList = new JList(filesToMapListModel);

		filesToMapList.setToolTipText("Files in selected mvd");

		JScrollPane jSP = new JScrollPane(filesToMapList);

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

		filesToMapDescLabel = new JLabel("Nr of tree files in mvd:");

		filesToMapLabel = new JLabel("0"); 					// '0' initially displayed

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

		mainPanel.add(filesToMapDescLabel, gbc);

		gbc.gridx = 2;

		gbc.gridy = 2;

		gbc.weightx = 0;

		gbc.weighty = 0;

		gbc.gridheight = 1;

		gbc.gridwidth = 1;

		gbc.fill = GridBagConstraints.NONE;

		gbc.anchor = GridBagConstraints.EAST;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(filesToMapLabel, gbc);

		gbc.gridx = 0;

		gbc.gridy = 3;

		gbc.weightx = 0;

		gbc.weighty = 1;

		gbc.gridheight = 1;

		gbc.gridwidth = 3;

		gbc.fill = GridBagConstraints.BOTH;

		gbc.anchor = GridBagConstraints.CENTER;

		gbc.insets = new Insets(0,0,cCS,0);

		mainPanel.add(jSP, gbc);

		gbc.gridx = 0;

		gbc.gridy = 4;

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

	private JLabel filesToMapLabel;

	private JLabel filesToMapDescLabel;


	private JButton changeForestButton;

	private JButton changeMappingButton;


	private JLabel forestLabel;

	private JLabel mappingLabel;

	private JTextField forestField;

	private JTextField mappingField;


	private JPanel mainPanel;

	private JPanel leftPanel;

	private JPanel rightPanel;


	private JList filesToMapList;

	private DefaultListModel filesToMapListModel;


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

			filesToMapListModel.clear();				// clear file listing

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

					for (int ctr=0; ctr<files.length; ctr++)
					{
						getNotifiable().setCurrent(ctr);

						filesToMapVector.add(files[ctr].getAbsolutePath());

						final String fileName = files[ctr].getName();

						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								filesToMapListModel.addElement(fileName);
							}
						});
					}
				}
			};

			mVD.startProgressMonitoring(UserIDMapper.this, runnable);

			if ((mappingField.getText() != null) && (mappingField.getText().length() != 0))
			{
				applyMappingsAction.setEnabled(true);
			}

			filesToMapLabel.setText(filesToMapVector.size() + "");
		}

		public void actionPerformed(ActionEvent e)
		{
			Component parent = UserIDMapper.this;

			String path = mVD.createAndShowLoadMVDDialog(UserIDMapper.this);

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

			int state = fileChooser.showDialog(UserIDMapper.this, "Choose mapping file");

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
							t = new StringTokenizer(currLine, "|", false);	// dont return delimiters

							String pc = t.nextToken().trim();

							String id = t.nextToken().trim();

							String cp = t.nextToken().trim();

							map.put(pc, new IDNamePair(id, cp));
						}
					}

					if ((forestField.getText() != null) && (forestField.getText().length() != 0))
					{
						applyMappingsAction.setEnabled(true);
					}

					inStream.close();
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					String m = "Invalid map file: " + exc.getMessage();

					mVD.createAndShowErrorDialog(UserIDMapper.this, m);
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
			NotifyingRunnable runnable = new NotifyingRunnable()
			{
				public void run()
				{
					Component parent = UserIDMapper.this;

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

							Tree pCodeNode = root.getNode("P-code"); // cannot be null

							Tree userIDNode = root.getNode("User-id"); // might be null

							Tree userNameNode = root.getNode("User-name"); // might be null

							Tree datumNode = root.getNode("Datum"); // cannot be null

							String pCode = pCodeNode.getFirstChild().getValue(); // this is pcode in file

							boolean pCodeInOldFormat = (pCode.length() <= 10); // determine if pcode is old

							int nrOffs = 0;

							for (; Character.isLetter(pCode.charAt(nrOffs)); nrOffs++); // find where löpnr begins

							String initPCode = pCode.substring(0, nrOffs);

							IDNamePair pair = (IDNamePair) map.get(initPCode);

							if (pair != null)
							{
								if (pCodeInOldFormat)
								{
									pCode = pair.getID() + "00" + pCode.substring(nrOffs);
								}
								else
								{
									pCode = pair.getID() + pCode.substring(nrOffs);	// now, pcode is the one we want
								}

								pCodeNode.removeAllChildren();

								pCodeNode.addChild(new TreeLeaf(pCode));

								if (userIDNode != null) // update user ID node
								{
									userIDNode.removeAllChildren();
								}
								else
								{
									userIDNode = new TreeBranch("User-id");

									pCodeNode.getParent().addChild(userIDNode);
								}

								userIDNode.addChild(new TreeLeaf(pair.getID()));

								if (userNameNode != null) // update care provider node
								{
									userNameNode.removeAllChildren();
								}
								else
								{
									userNameNode = new TreeBranch("User-name");

									pCodeNode.getParent().addChild(userNameNode);
								}

								userNameNode.addChild(new TreeLeaf(pair.getName()));

								String datumString = datumNode.getFirstChild().getValue();

								StringTokenizer tok = new StringTokenizer(datumString, " -:", false);

								String d = tok.nextToken(); // first token year, YYYY or YY

								if (d.length() == 4) { d = d.substring(2); }	// just include last two

								while (tok.hasMoreTokens())
								{
									d = d + tok.nextToken(); // concatenate rest of date
								}

								String dir = currFile.getParent();

								currFile.delete(); // delete old .tree file

								currFile = new File(dir, pCode + "_" + d + ".tree");

								currFile.createNewFile();

								FileOutputStream fos = new FileOutputStream(currFile);

								OutputStreamWriter writer = new OutputStreamWriter(fos);

								writer.write(root.toString());

								writer.flush();

								fos.close();
							}
						}
						catch (Exception exc)
						{
							String m1 = "Warning: file '" + currFile + "'";

							String m2 = " could not be mapped due to:";

							System.err.println(m1 + m2 + exc.getMessage());
						}
					};
				}
			};

			final Thread t = MedViewDialogs.instance().startProgressMonitoring(UserIDMapper.this, runnable);

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
								changeForestAction.readForest(forestField.getText()); // read forest again after mapping
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

	private class IDNamePair
	{
		public String getID()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public String toString()
		{
			return "[" + id + "," + name + "]";
		}

		public IDNamePair(String id, String name)
		{
			this.id = id;

			this.name = name;
		}

		private String id;

		private String name;
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
			return "User ID mapping files (.map)";
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

		UserIDMapper m = new UserIDMapper();

		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String iconID = MedViewMediaConstants.FRAME_IMAGE_ICON;

		m.setIconImage(mVDH.getImage(iconID));

		m.show();
	}

// ----------------------------------------------------------------------------------------------
// **********************************************************************************************
// ----------------------------------------------------------------------------------------------

}
