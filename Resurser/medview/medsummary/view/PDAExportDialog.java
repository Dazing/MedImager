package medview.medsummary.view;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.*;
import java.io.*;

import medview.datahandling.*;
import medview.common.data.*;
import medview.medsummary.model.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import medview.datahandling.examination.tree.*;

import misc.gui.components.*;
import misc.gui.constants.*;
import medview.datahandling.examination.*;

import medview.common.generator.*;

import se.chalmers.cs.medview.docgen.*;

/**
 * Class for showing a dialog window for export examinations to a PDA.
 * It puts and removes examinations from ExamDatabasesDir which is a subdirectory
 * to a pda-database directory specified in the preferences.
 * @author Mikael Lindström
 */

public class PDAExportDialog extends JDialog implements ActionListener 
{
	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();
	
	private JList rightList;
	
	private Frame parent;
	
	private JPanel topPanel;
	
	private TitledBorder border;
	
	private File root;
	
	private DefaultListModel listModel;
	
	private JTextField filterField;
	
	private JSplitPane splitPane;
	
	private JButton toLeft;
	
	private JPanel toLeftPanel;
	
	private JButton toRight;
	
	private JPanel toRightPanel;
	
	private MedSummaryModel model;
	
	private SearchableListPanel listPanel;

	public void setVisible(boolean isVisible) 
	{
		if (root==null) {
			showErrorMsg();
			return;
		}
		
		refresh();
		setLocationRelativeTo(super.getOwner());
		super.setVisible(isVisible);
	}
	
	/**
	 * Refreshes the list of examinations in the DatabasePath
	 */
	private void refresh() 
	{
		listModel.removeAllElements();
		Vector pidVector = new Vector();
		
		PatientIdentifier patient;
		
		File[] files = root.listFiles(new TreeFileFilter());
		for (int i = 0; i < files.length; i++) 
		{
			try{
				MedViewUtilities.PIDPCodePair pidpcode = MedViewUtilities.parsePIDPCode(files[i]);
			
				if(!pidVector.contains(pidpcode.getPCode()))
				{
					pidVector.add(pidpcode.getPCode());
					patient = new PatientIdentifier(pidpcode.getPCode(), pidpcode.getPID());
					listModel.addElement(patient);
				}
			} catch (Exception e)
			{
				
			}
		}
	}
	
	/**
	 * Takes care of button clicks.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if ("close".equals(actionCommand))
		{
			dispose();
		} else if ("toLeft".equals(actionCommand)) 
		{
			deleteExam();
			refresh();
		} else if ("toRight".equals(actionCommand)) {
			exportExam();
			refresh();
		}
	}
	
	/**
	 * Make a copy of a file
	 * @param src source file
	 * @param dst destination file
	 * @throws IOException
	 */
    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    /**
     * Generates a StyledDocument
     * @param pCode pcode of the patient
     * @param container the valuefields
     * @return a StyledDocument
     * @throws Exception
     */
    private StyledDocument getPreview(String pCode, ValueContainer container) throws Exception
    {
    	StyledDocument document = null;
    	try{
		ExaminationIdentifier[] ids = new ExaminationIdentifier[]
        {
			new MedViewExaminationIdentifier(new PatientIdentifier(pCode)) 
		};

		ValueContainer[] containers = new ValueContainer[] { container };

		MedViewGeneratorEngineBuilder engineBuilder = model.getGeneratorEngineBuilder(); 
			
		engineBuilder.buildIdentifiers(ids); // -> CouldNotBuildEngineException

		engineBuilder.buildValueContainers(containers); // -> CouldNotBuildEngineException

		GeneratorEngine engine = engineBuilder.getEngine(); // -> FurtherElementsRequiredException

		document = engine.generateDocument(); // -> CouldNotGenerateException
		
		engineBuilder.removeValueContainers();
		
		engineBuilder.removeIdentifiers();
		
		return document;
    	}
    	catch (Exception e){
    		throw new Exception(e.getMessage());
    	}
    }
	
    /**
     * Exports exams to treefiles and htmlfiles to DatabasePath
     */
	private boolean exportExam(){
		
		File src;
		File dest;
		File html;
		Tree tree;
		
		MVDHandler mvdh = new MVDHandler();
		mvdh.setExaminationDataLocation(model.getLocalExaminationDataLocation());
		
		TreeFileHandler tfh = new TreeFileHandler();
		 
		Object[] patients = listPanel.getSelectedEntries();
		
		for(int i=0;i<patients.length;i++){
		
			try {
				Object[] exams = mvdh.getExaminations((PatientIdentifier)patients[i]);
				
				for(int j=0;j<exams.length;j++){
					src = mvdh.getExaminationFile((ExaminationIdentifier)exams[j]);
			
					dest = new File(getPath(model) + File.separator + src.getName());
			
					copy(src, dest);
					
					tree = tfh.loadTreeFile(src);
					
					String pid = ((ExaminationIdentifier)exams[j]).getPID().getPCode().toString();
					
					ExaminationValueContainer container = new ExaminationValueTable(tree);
					
					StyledDocument document = getPreview(pid, MedViewGeneratorUtilities.wrapExaminationValueContainer(container));
					
					HTMLEditorKit htmlek = new HTMLEditorKit();
					
					html = new File(dest + ".html");
					
					OutputStream out = new FileOutputStream(html);
					
					htmlek.write(out, document, 0, document.getLength());
					
				}
		
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error when exporting examination", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		//fixar så att filerna inte blir låsta
		tfh=null;
		System.gc();
		
		return true;
	}
	
	/**
	 * Deletes the selected exams from DatabasePath
	 */
	private boolean deleteExam() 
	{
		TreeFileHandler tfh = new TreeFileHandler();
		tfh.setExaminationDataLocation(getPath(model));
		
		PatientIdentifier patient = (PatientIdentifier)rightList.getSelectedValue();
		
		try{
			Object[] exams = tfh.getExaminations(patient);
			File[] files = new File[exams.length];
			for(int i=0;i<exams.length;i++)
			{
				files[i] = tfh.getTreeFile((ExaminationIdentifier)exams[i]);
			}
			tfh = null;
			System.gc();
			for(int i=0;i<exams.length;i++)
			{
				deleteExam(files[i]);
			}
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error deleting exam", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
	
	/**
	 * Deletes the treefile and htmlfile from DatabasePath
	 * @param treefile the treefile that should be removed
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteExam(File treefile) throws IOException 
	{
		File htmlfile;
		
		htmlfile = new File(treefile.getAbsolutePath() + ".html");		
		
		treefile.delete();
		htmlfile.delete();
		
		return true;
	}
	
	/**
	 * Gets path to DatabasePath
	 * @param model MedSummaryModel currently being used
	 * @return String path to database
	 */
	private static String getPath(MedSummaryModel model) {
		String path = model.getLocalPDADataLocation();
		if (path != null) {
			path = path + File.separator + "DatabasePath" + File.separator;
			File pathFile = new File(path);
			if (!pathFile.exists()) 
			{
				if (!pathFile.mkdir()) 
				{
					path = null;
				}
			}
		}
		return path;
	}
	
	private void showErrorMsg() 
	{
		JOptionPane.showMessageDialog(parent, "Set PDA Database Location in preferences", "PDA Database Location not set", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Constructor
	 * @param owner Owner frame
	 * @param model MedSummaryModel currently being used
	 */
	public PDAExportDialog(Frame owner, MedSummaryModel model) 
	{
		
		super(owner, mVDH.getLanguageString(MedViewLanguageConstants.TITLE_PDAEXPORTDIALOG_LS_PROPERTY), true);
		
		parent = owner;
		this.model = model;
		
		// Set correct import dir
		String path = getPath(model);
		if (path==null) 
		{
			showErrorMsg();
			return;
		}
		
		root = new File(path);
		
		// Create a contentPane to add swing components to
		final JPanel contentPane = new JPanel(new BorderLayout());
		
		// Add scroll list
		
		listModel = new DefaultListModel();
		rightList = new JList(listModel);
		rightList.addListSelectionListener(new SelectListener());
		rightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightList.setLayoutOrientation(JList.VERTICAL);
		rightList.setVisibleRowCount(-1);
		rightList.setCellRenderer(new MyCellRenderer());
		
		PatientIdentifier[] initialEntries = null;

		if (model.isExaminationDataLocationSet())
		{
			try
			{
				initialEntries = model.getPatients(null);
			}
			catch (Exception exc)
			{
				initialEntries = new PatientIdentifier[0];
			}
		}
		else
		{
			initialEntries = new PatientIdentifier[0]; // model does not contain valid loc

		}

		final SearchableListModel listModel = new SearchableListModel(initialEntries);

		ImageIcon searchIcon = new ImageIcon("medview/datahandling/resources/images/18x18search.gif");
		listPanel = new SearchableListPanel(listModel, searchIcon);
		listPanel.addListSelectionListener(new SelectListener());
		listPanel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listPanel.setPreferredSize(new Dimension(150, 300));
		listPanel.setOpaque(false);
		
		JScrollPane listScrollerRight = new JScrollPane(rightList);
		JPanel centerButtons = new JPanel(new BorderLayout());
		JPanel leftSidePane = new JPanel(new BorderLayout());
		leftSidePane.add(listPanel, BorderLayout.CENTER);
		leftSidePane.add(centerButtons, BorderLayout.EAST);
		
		
		toRight = new JButton(mVDH.getImageIcon(MedViewMediaConstants.RIGHT_ARROW_IMAGE_ICON));
		toLeft = new JButton(mVDH.getImageIcon(MedViewMediaConstants.LEFT_ARROW_IMAGE_ICON));
		
		centerButtons.setLayout(new GridLayout(2,1));
		
		toRightPanel = new JPanel(new BorderLayout());
		toLeftPanel = new JPanel(new BorderLayout());
		
		toRight.addActionListener(this);
		toRight.setActionCommand("toRight");
		toLeft.addActionListener(this);
		toLeft.setActionCommand("toLeft");
		
		toRight.setPreferredSize(GUIConstants.BUTTON_SIZE_16x16);
		toLeft.setPreferredSize(GUIConstants.BUTTON_SIZE_16x16);
		
		toRightPanel.add(toRight, BorderLayout.SOUTH);
		toLeftPanel.add(toLeft, BorderLayout.NORTH);
		
		centerButtons.add(toRightPanel);
		centerButtons.add(toLeftPanel);
		
		listScrollerRight.setPreferredSize(new Dimension(150, 300));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSidePane, listScrollerRight);
		splitPane.setDividerLocation(150);
		
		Dimension minimumSize = new Dimension(100, 50);
		contentPane.setMinimumSize(minimumSize);
		
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		// Add buttons on the bottom
		final JPanel buttonPanel = new JPanel(new BorderLayout());
		
		final JButton okButton;
		//final JButton refreshButton;
		
		okButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_CLOSE_LS_PROPERTY));
		okButton.addActionListener(this);
		okButton.setActionCommand("close");
		
		final JPanel leftButtons = new JPanel(new FlowLayout());
		final JPanel rightButtons = new JPanel(new FlowLayout());
		
		rightButtons.add(okButton);
		
		buttonPanel.add(leftButtons, BorderLayout.WEST);
		buttonPanel.add(rightButtons, BorderLayout.EAST);
		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	
		// Other stuff
		refresh();
		setContentPane(contentPane);
		pack();
		
	}
	
	/**
	 * Renders a cell in the list
	 */
	 private class MyCellRenderer extends JLabel implements ListCellRenderer 
	 {   
	     // This is the only method defined by ListCellRenderer.
	     // We just reconfigure the JLabel each time we're called.
	     JLabel top;
	     JLabel bottom;
	     JPanel right;
	     
	     MyCellRenderer() 
	     {
	    	 setOpaque(true);
	     }

	     public Component getListCellRendererComponent(
	       JList list,
	       Object value,            // value to display
	       int index,               // cell index
	       boolean isSelected,      // is the cell selected
	       boolean cellHasFocus)    // the list and the cell have the focus
	     {
	    	 String text;

    		 text = ((PatientIdentifier)value).getPID();
	    	 setText(text);
	    	 
	   	   if (isSelected) 
	   	   {
	           setBackground(list.getSelectionBackground());
		       setForeground(list.getSelectionForeground());

		   } else 
	       {
		       setBackground(list.getBackground());
		       setForeground(list.getForeground());
		   }
	       return this;
	     }
	 }
	 
	 /**
	  * Filters out treefiles
	  */
	 private static class TreeFileFilter implements FileFilter 
	 {
		 public boolean accept(File f) 
		 {
			String fName = f.getName().toLowerCase();
			return fName.endsWith(".tree");
		 } 
	 }
	 
	 private class SelectListener implements ListSelectionListener
	 {
		 public void valueChanged(ListSelectionEvent e)
		 {
			 if(!((JList)e.getSource()).isSelectionEmpty())
			 {
				 if(e.getSource().equals(rightList))
				 {
					 listPanel.clearSelection();
				 }
				 else
				 {
					 rightList.clearSelection();
				 }
			 }
		 }
	 }
}
