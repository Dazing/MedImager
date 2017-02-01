package medview.medrecords.components;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.*;
import medview.medrecords.data.*;
import medview.common.data.MedViewUtilities;
import medview.datahandling.*;

import javax.swing.border.*;

import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.tree.*;
//import java.text.SimpleDateFormat;
/**
 * Class for showing a dialog window for import of examinations from a PDA.
 * It looks for tree-files in a directory named ExamSaveDir which is a subdirectory
 * to a pda-database directory specified in the preferences.
 * @author Markus Johansson
 */
public class PDADialog extends JDialog implements ActionListener 
{
	
	// Vad används den här för?
	static final long serialVersionUID = 0;
	
	TreeFileHandler tfh;
	
	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();
	
	private ExaminationIdentifier rVal;
	
	private JList list;
	
	private Frame parent;
	
	private JPanel topPanel;
	
	private TitledBorder border;
	
	private PDAEventListener inform;
	
	private DefaultListModel listModel;
	
	private Vector allListData = new Vector();
	
	private JTextField filterField;
	
	private HashMap pidMap;
	
	private boolean valid;

	/**
	 * @return ExaminationIdentifier of the exam which was imported by the dialog
	 */
	public Object getValue() 
	{
		return rVal;
	}
	
	public void setVisible(boolean isVisible) 
	{
		refresh();	
		if (!valid) {
			showErrorMsg();
			return;
		}
	
		setLocationRelativeTo(super.getOwner());
		super.setVisible(isVisible);
	}
	
	/**
	 * Filters out examinations which don't match the filterString
	 * @param filterString search phrase to filter with, can be date and/or pid
	 */
	private void filter(String filterString) 
	{
		
		String[] filters = filterString.split(" ");
		
		if ("".equals(filterString))
		{
			border.setTitle(mVDH.getLanguageString(MedViewLanguageConstants.LABEL_SHOWING_ALL_LS_PROPERTY));
		} else 
		{
			border.setTitle(mVDH.getLanguageString(MedViewLanguageConstants.LABEL_FILTER_ON_LS_PROPERTY) + filterString);
		}
		
		topPanel.repaint();
		
		Enumeration e = allListData.elements();
		
		Vector store = new Vector();

		int count = 0;
		
		ExaminationIdentifier exam = null;
		
		while (e.hasMoreElements()) {
			for (int i = 0; i < filters.length; i++) 
			{
				exam = (ExaminationIdentifier)e.nextElement();
				
				// Man kan söka på PID och datum på lokalt format.
				String date = DateFormat.getInstance().format(exam.getTime());
				String searchString = exam.getPID().toString() + date;
			
				if (searchString.indexOf(filters[i]) != -1) {
					count++;
				}
			}
			if (count == filters.length) {
				store.addElement(exam);
			}
			count = 0;
		}
		
		listModel.removeAllElements();
		
		Iterator iter = store.iterator();
		while (iter.hasNext()) 
		{
			listModel.addElement(iter.next());
		}
	}
	
	/**
	 * Calls filter(filterString) with the value taken from the search field in the dialog.
	 */
	private void filter() 
	{
		filter(filterField.getText());
	}
	/**
	 * 
	 * Refreshes the list of examinations
	 * Refreshes the preferences object, database dir, examination files and finally filters on the filter string.
	 */
	private void refresh() 
	{
		String path = getPath();
		if (path != null) {
			valid = true;
			tfh = new TreeFileHandler(path);
		} else {
			valid = false;
			return;
		}
		listModel.removeAllElements();
		allListData = new Vector();
		pidMap = new HashMap();
		
		try {
			Object[] patients = tfh.getPatients();
			for (int i=0; i<patients.length; i++) {
				PatientIdentifier pid = (PatientIdentifier)patients[i];
				ExaminationIdentifier[] exams = tfh.getExaminations(pid);
				for (int j=0; j<exams.length; j++) {
					File file;
			    	try {
			    	 	file = tfh.getTreeFile(exams[j]);
			    	 	MedViewUtilities.PIDPCodePair pidpc0de = MedViewUtilities.parsePIDPCode(file);
			    	 	pidMap.put(exams[j], pidpc0de.getPID());
			    	 	file = null;
			    	} catch (Exception e) {
			    		pidMap.put(exams[j], "<no pid>");
			    	}
			    	file = null;
					
					allListData.addElement(exams[j]);
					listModel.addElement(exams[j]);
				}
			}
		} catch(Exception e) {
			System.out.println("<FEL>");
			System.out.println("path: " + getPath());
			System.out.println(e);
			e.printStackTrace();
			System.out.println("</FEL>");
		}
	
		filter();
	}
	
	/**
	 * Calls importExam(int listElement) with the selected in the list
	 */
	private void importExam() 
	{
		int index = list.getSelectedIndex();
		if (-1 != index) 
		{
			importExam(list.getSelectedIndex());
		}
	}
	
	/**
	 * Calls the pdaImportEvent-method on the PDAEventListener associated with this dialog window.
	 * @param listElement
	 * @see PDAEventListener
	 */
	private void importExam(int listElement) 
	{
		ExaminationIdentifier exam = (ExaminationIdentifier)(listModel.get(listElement));
		rVal = exam;                                                    
		inform.pdaImportEvent(new EventObject(this));

		dispose();
	}

	/**
	 * just for taking care of button clicks and such.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if ("import".equals(actionCommand)) 
		{
			importExam();
		} else if ("close".equals(actionCommand))
		{
			dispose();
		} else if ("delete".equals(actionCommand))
		{
			deleteExam();
		} else if ("refresh".equals(actionCommand)) 
		{
			refresh();
		} else if ("filter".equals(actionCommand)) 
		{
			refresh();
			filter(filterField.getText());
		}
	}
	
	/**
	 * 
	 * @return true if the deletion was successful, else false
	 */
	private boolean deleteExam() 
	{
		ExaminationIdentifier exam = (ExaminationIdentifier)(list.getSelectedValue());
		
		boolean r = false;
		try {
			r = deleteExam(exam);
			if (r) {
				listModel.removeElementAt(list.getSelectedIndex());
			}
		} catch (Exception e) 
		{
			System.out.println(e);
		}
		return r;
	}
	
	/**
	 * Deletes the tree-file associated with the examinationidentifier
	 * @param exam to be deleted
	 * @return true if the deletion was successful, else false
	 */
	public boolean deleteExam(ExaminationIdentifier exam) 
	{
		File name = null;
		try {		
			name = tfh.getTreeFile(exam);
		} catch (Exception e) {
			System.out.println(e);
		}
		PatientIdentifier pid = exam.getPID();
		
		int buttonPressed = JOptionPane.showConfirmDialog(null, mVDH.getLanguageString(MedViewLanguageConstants.QUESTION_CONFIRM_DELETE_FILE_LS_PROPERTY) + "\n" + name.getName() + "\nPID: " + pid + "\n" + mVDH.getLanguageString(MedViewLanguageConstants.OTHER_CREATION_TIME_LS_PROPTERY) + DateFormat.getInstance().format(exam.getTime()) + "?", "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (buttonPressed == JOptionPane.OK_OPTION)
		{
			tfh = null;

			System.gc();
			if(name.delete()) {
				tfh = new TreeFileHandler(getPath());
				return true;
			} else 
			{
				JOptionPane.showMessageDialog(null, "Kunde inte ta bort filen.", "Varning", JOptionPane.ERROR_MESSAGE);
				tfh = new TreeFileHandler(getPath());
				return false;
			}
		}
		return false;
	
	}
	
	/**
	 * 
	 * @return path of the PDAdatabase + /ExamSaveDir, home of the tree-files
	 */
	private static String getPath() {
		String path = PreferencesModel.instance().getPDADatabaseLocation();
		if (path != null) {
			path = path + File.separator + "ExamSaveDir" + File.separator;
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
		JOptionPane.showMessageDialog(parent, "Set a valid PDA Database Location in preferences", "PDA Database Location not valid", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 
	 * @param owner Owner frame
	 * @param el A PDAImportEvent is sent to this object when a reqeust for import is done
	 */
	public PDADialog(Frame owner, PDAEventListener el) 
	{
		
		super(owner, mVDH.getLanguageString(MedViewLanguageConstants.TITLE_PDADIALOG_LS_PROPERTY), true);
		
		
		// Who to inform when an import is requested
		inform = el;
		parent = owner;
		
		// Set correct import dir
		String path = getPath();
		valid = true;
		if (path==null) 
		{
			valid = false;
			//showErrorMsg();
			//return;
		}
		
		tfh = new TreeFileHandler(path);
		
		// Create a contentPane to add swing components to
		final JPanel contentPane = new JPanel(new BorderLayout());
		
		
		// Add the filter text field
		
		final ImageIcon icon = new ImageIcon("medview/datahandling/resources/images/18x18search.gif");

		filterField = new JTextField();
		filterField.addActionListener(new TextListener());
		
		final JButton filterButton;
		filterButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_REFRESH_LS_PROPERTY));
		filterButton.addActionListener(this);
		filterButton.setActionCommand("filter");
		
		final JPanel filterPanel = new JPanel(new BorderLayout());
		filterPanel.add(new JLabel(" ", icon, JLabel.LEFT), BorderLayout.WEST);
		filterPanel.add(filterField, BorderLayout.CENTER);
		filterPanel.add(filterButton, BorderLayout.EAST);
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.add(filterPanel, BorderLayout.NORTH);
		
		border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), mVDH.getLanguageString(MedViewLanguageConstants.LABEL_SHOWING_ALL_LS_PROPERTY));
		topPanel.setBorder(border);
		
		// Add scroll list
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setCellRenderer(new MyCellRenderer());
		
		MouseListener mouseListener = new MouseAdapter() 
		{
		     public void mouseClicked(MouseEvent e) 
		     {
		         if (e.getClickCount() == 2) 
		         {
		        	 int index = list.locationToIndex(e.getPoint());
		             importExam(index);
		          }
		     }
		};
		list.addMouseListener(mouseListener);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(280, 300));
		topPanel.add(listScroller, BorderLayout.CENTER);
		contentPane.add(topPanel, BorderLayout.CENTER);
		
		// Add buttons on the bottom
		final JPanel buttonPanel = new JPanel(new BorderLayout());
		
		JButton importButton;
		final JButton deleteButton;
		final JButton closeButton;
		//final JButton refreshButton;
		
		importButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_OPEN_LS_PROPERTY));
		importButton.addActionListener(this);
		importButton.setActionCommand("import");
		
		deleteButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_DELETE_LS_PROPERTY));
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("delete");
		closeButton = new JButton(mVDH.getLanguageString(MedViewLanguageConstants.BUTTON_CLOSE_LS_PROPERTY));
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		
		final JPanel leftButtons = new JPanel(new FlowLayout());
		final JPanel rightButtons = new JPanel(new FlowLayout());
		
		rightButtons.add(importButton);
		leftButtons.add(deleteButton);
		rightButtons.add(closeButton);
		
		buttonPanel.add(leftButtons, BorderLayout.WEST);
		buttonPanel.add(rightButtons, BorderLayout.EAST);
		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	
		
		// Other stuff
		
		refresh();
		setContentPane(contentPane);
		pack();
		//setSize(460,320);
		
	}
	
	/**
	 * Just for testing; instead of launching from MedRecords.
	 * Displays a parent window which the dialogue can be launched from.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		final JFrame mother = new JFrame("Mother frame");
		mother.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		final JLabel motherLabel = new JLabel("This is the mother frame");
		motherLabel.setAlignmentX(JLabel.CENTER);
		
		final PDADialog theDialog = new PDADialog(mother, new PDAEventListener()
			{
				public void pdaImportEvent(EventObject e) 
				{
					PDADialog diag = (PDADialog)(e.getSource());
					ExaminationIdentifier exam = (ExaminationIdentifier)diag.getValue();
					motherLabel.setText(exam.toString());
				}
			}
		);
		
		JButton motherButton = new JButton("Click to start ImportFromPDADialog");
		motherButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					theDialog.setVisible(true);
				}
			}
		);
		
		contentPane.add(motherLabel, BorderLayout.NORTH);
		contentPane.add(motherButton, BorderLayout.CENTER);
		
		mother.setContentPane(contentPane);
		
		mother.setSize(800,600);
		mother.setVisible(true);
	}
	/**
	 * Implement this class to receive import events.
	 * See main function how PDAEventListener can be used to
	 * receive PDA import Events. 
	 * @author Markus Johansson
	 * @return An eventobject which has the source of the PDADialog-object which has sent event.
	 */
	public static abstract class PDAEventListener implements EventListener 
	{
		public void pdaImportEvent(EventObject e) {};
	}
	
	private class TextListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			filter();
		}
	}
	
	/**
	 * 
	 * @author majohans
	 *
	 */
	 private class MyCellRenderer extends JPanel implements ListCellRenderer 
	 {
		 // Vad används den här för?
		 static final long serialVersionUID = 0;
		 
	     final ImageIcon journalIcon = new ImageIcon("medview/datahandling/resources/images/24x24journal.png");
	     
	     // This is the only method defined by ListCellRenderer.
	     // We just reconfigure the JLabel each time we're called.
	     JLabel top;
	     JLabel bottom;
	     JPanel right;
	     
	     MyCellRenderer() 
	     {
	    	 top = new JLabel();
	    	 bottom = new JLabel();
	    	 right = new JPanel(new BorderLayout());
	    	 JLabel iconLabel = new JLabel(" ");
	    	 iconLabel.setIcon(journalIcon);
	    	 
	    	 setLayout(new BorderLayout());
	    	 right.add(top, BorderLayout.NORTH);
	    	 right.add(bottom, BorderLayout.SOUTH);
	    	 add(iconLabel, BorderLayout.WEST);
	    	 add(right, BorderLayout.CENTER);
	    	 
	    	 // jag vet omg-fult
	    	 add(new JLabel(" "), BorderLayout.NORTH);
	    	 add(new JLabel(" "), BorderLayout.SOUTH);
	    	 
	    	 right.setOpaque(false);
	    	 setOpaque(true);
	     }

	     public Component getListCellRendererComponent(
	       JList list,
	       Object value,            // value to display
	       int index,               // cell index
	       boolean isSelected,      // is the cell selected
	       boolean cellHasFocus)    // the list and the cell have the focus
	     {
	    	ExaminationIdentifier exam = (ExaminationIdentifier) value;
	    	 
	    	 top.setText((String)pidMap.get(exam));
	    	 bottom.setText(DateFormat.getInstance().format(exam.getTime()));
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
}
