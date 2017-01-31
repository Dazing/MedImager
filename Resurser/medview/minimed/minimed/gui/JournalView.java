package minimed.gui;


import minimed.*;
import minimed.core.datahandling.examination.ExaminationIdentifier;
import minimed.core.datahandling.examination.ExaminationValueContainer;
import minimed.core.datahandling.examination.tree.TreeFileHandler;
import minimed.core.datahandling.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import java.io.*;
import java.util.TreeMap;
import java.text.DateFormat;

/**
 * A view for the journal browsing sceen.
 * 
 * @author Joni Paananen
 */
public class JournalView extends MinimedView {
	private TabFolder tabFolder;
	private Menu menubar;
	private Tree saveTree;
	private Tree databaseTree;
	private TreeFileHandler saveHandler;
	private TreeFileHandler databaseHandler;
	
	/**
	 * Creates the journal browsing view. The controller passed must implement selectionListener
	 * 
	 * @param pShell The shell used by the application.
	 * @param pModel The model used by the application.
	 * @param pController The controller which controls this view.
	 */
	public JournalView(Shell pShell, MinimedModel pModel, JournalController pController) {	
		super(pShell, pModel, pController);
		this.setLayout(new GridLayout());
		
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());
		
		createMenu();
		createCreatedTab();
		createTransferredTab();
	}
	
	/**
	 * Closes the view
	 */
	public void dispose() {
		super.dispose ();
		menubar.dispose();
	}
	
	/**
	 * Sets the models current exam (value container and file)
	 * Returns false if no exam is selected. 
	 * 
	 * @param openAsNew True means that the selected exam is to be "opened as new", which means that the exam is to be saved to a new file, but certain term values are transferred to the new exam.
	 * @return false if no exam is selected, true otherwise.
	 */
	public boolean chooseCurrentExam(boolean openAsNew){
		boolean isSelected = false;
		/* If the "Created" tab is selected. */
		if (saveTree.getSelectionCount() > 0 && tabFolder.getSelectionIndex() == 0) {
			TreeItem selected = saveTree.getSelection()[0];
			
			/* If the selected item is an examination */
			if (selected.getData() instanceof ExaminationIdentifier) {
				ExaminationIdentifier exam = (ExaminationIdentifier)selected.getData();
				ExaminationValueContainer cont = null;
				File treeFile = null;
				
				try {
					treeFile = saveHandler.getTreeFile(exam);
					/* If the examination is to be opened as new */
					if (openAsNew) {
						cont = model.getExaminationFromFile(treeFile, saveHandler);
					} 
					/* Else the valueContainer to be used is the original */
					else {
						cont = saveHandler.getExaminationValueContainer(exam);
					}
				} catch (Exception e) {
					MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.NO);
					error.setMessage(model.i18n("The exam has been deleted"));
					error.open();
					refreshSaved();
					return false;
				}
				if (!openAsNew){
					/* Sets the savefile in the model */
					model.setCurrentTreeFile(treeFile);
				}
				model.setExaminationValueContainer(cont);
				isSelected = true;
			}
		}
		/* If the transferred tab is selected */
		if (tabFolder.getSelectionIndex() == 1 && databaseTree.getSelectionCount() > 0) {
			TreeItem selected = databaseTree.getSelection()[0];
			if (selected.getData() instanceof ExaminationIdentifier) {
				ExaminationIdentifier exam = (ExaminationIdentifier)selected.getData();
				String file = null;
				try {
					/* If the examination is to be opened as new */
					if (openAsNew) {
						File f = databaseHandler.getTreeFile(exam);
						model.setExaminationValueContainer(model.getExaminationFromFile(f, databaseHandler));
						isSelected = true;
					} 
					/* Else a webbrowser is opened with the html file containing the exam */
					else {
						file = databaseHandler.getTreeFile(exam).getAbsolutePath();
						file = "file://" + file + ".html";
						org.eclipse.swt.program.Program.launch(file);
					}
				} catch(Exception e) {
					MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.NO);
					error.setMessage(model.i18n("The exam has been deleted"));
					error.open();
					refreshDatabase();
					return false;
				}
			}
		}
		return isSelected;
	}
	
	/**
	 * Deletes the current examination or patient, depending on which type of item is selected
	 */
	public void deleteCurrentExam() {
		TreeFileHandler handler;
		File treeFile = null;
		boolean hasHtml = false;
		Tree tree;
		/* If the saved tab is selected */
		if (tabFolder.getSelectionIndex() == 0) {
			handler = saveHandler;
			tree = saveTree;
		} 
		/* If the transferred tab is selected */
		else {
			handler = databaseHandler;
			tree = databaseTree;
			hasHtml = true;
		}
		
		/* If something is selected */
		if (tree.getSelectionCount() > 0) {
			
			/* If an examination is selected */
			if (tree.getSelection()[0].getData() instanceof ExaminationIdentifier) {

				MessageBox error = new MessageBox(this.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				error.setMessage(model.i18n("Are you sure you want to delete the exam?"));
				int answer = error.open();
				
				if (answer == SWT.YES) {
					try {
						treeFile = handler.getTreeFile((ExaminationIdentifier)tree.getSelection()[0].getData());
					} catch(Exception e){
						/* The exam is already deleted */
						if (hasHtml){
							refreshDatabase();
						} else {
							refreshSaved();
						}
						return;
					}
					
					handler = null;
					
					if (hasHtml) {
						/* dispose the treefilehandler so that it does not lock the file */
						databaseHandler = null;
						model.disposeDatabaseTreeFileHandler();
					} else {
						/* dispose the treefilehandler so that it does not lock the file */
						saveHandler = null;
						model.disposeSaveTreeFileHandler();
					}
					/* 
					 * The PDA garbage collecter is slow. Tries to delete 10 times
					 * with 100 ms intervals until it gives up
					 */
					for (int i = 0; i < 10 && !treeFile.delete(); i++) {
						try {
							Thread.sleep( 100 );
						} catch (InterruptedException e) {}						
					}
					
					/* If the file was not deleted */
					if(treeFile.exists()){
						error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
						error.setMessage(model.i18n("Could not delete the file."));
					}
					else{
						/* Rebuild the graphical tree */
						if (hasHtml) {
							String fileName = treeFile.getAbsolutePath();
							File HTML = new File(fileName + ".html");
							HTML.delete();
							refreshDatabase();
						} 
						else {
							refreshSaved();
						}
					}
					
				}
			} 
			
			/* If a patient i selected */
			else if (tree.getSelection()[0].getData() instanceof PatientIdentifier) {
				MessageBox question = new MessageBox(this.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				question.setMessage(model.i18n("Are you sure you want to delete the patient?"));
				int answer = question.open();
				
				if (answer == SWT.YES) {
					TreeItem[] exams = tree.getSelection()[0].getItems();
					File[] treeFiles = new File[exams.length];
					File[] HTMLFiles = new File[exams.length];
					for (int i = 0; i < exams.length; i++) {
						try {
							treeFiles[i] = handler.getTreeFile((ExaminationIdentifier)exams[i].getData());
						}
						/* Something unexpected has happened with the TreeFileHandler
						 * Exit, because this is a fatal error
						 */
						catch(Exception e) {
							showTreeError();
							e.printStackTrace();
							System.exit(1);
						}
						if (hasHtml && treeFiles[i] != null) {
							String fileName = treeFiles[i].getAbsolutePath();
							HTMLFiles[i] = new File(fileName + ".html");
						}
					}
					
					/* Dispose the tree file handler, so that it does not lock the files */
					handler = null;
					if (hasHtml) {
						databaseHandler = null;
						model.disposeDatabaseTreeFileHandler();
					} else {
						saveHandler = null;
						model.disposeSaveTreeFileHandler();
					}

					for (int i = 0; i < exams.length; i++) {
						/* 
						 * The PDA garbage collecter is slow. Tries to delete 10 times
						 * with 100 ms intervals until it gives up
						 */
						if(treeFiles[i] != null){
							for (int j = 0; !treeFiles[i].delete() && j < 10; j++) {
								try {
									Thread.sleep( 100 );
								} catch ( InterruptedException e ) {}						
							}
							/* Delete the html file if the deletion of the tree file succeded */
							if (hasHtml && treeFiles[i] != null && !treeFiles[i].exists()) {
								HTMLFiles[i].delete();
							}
						}
					}
					
					/* rebuild the graphical tree */
					if (hasHtml) {
						refreshDatabase();
					} 
					else {
						refreshSaved();
					}
				}
			}
		}
	}
	
	/**
	 * Refreshes the tree with examinations saved on the PDA.
	 */
	private void refreshSaved() {
		model.disposeSaveTreeFileHandler();
		saveTree.removeAll();
		try {
			saveHandler = model.getSaveTreeFileHandler();
		}
		/* Something unexpected has happened with the TreeFileHandler
		 * Exit, because this is a fatal error
		 */
		catch(Exception e) {
			showTreeError();
			e.printStackTrace();
			System.exit(1);
		}
		buildTreeFromHandler(saveTree, saveHandler);
	}
	
	/**
	 * Refreshes the tree with examinations transferred from the PC.
	 */
	private void refreshDatabase() {
		model.disposeDatabaseTreeFileHandler();
		databaseTree.removeAll();
		try {
			databaseHandler = model.getDatabaseTreeFileHandler();
		} 
		/* Something unexpected has happened with the TreeFileHandler
		 * Exit, because this is a fatal error
		 */
		catch(Exception e) {
			showTreeError();
			e.printStackTrace();
			System.exit(1);
		}
		buildTreeFromHandler(databaseTree, databaseHandler);
	}
	
	/**
	 * Creates the menu, adds the controller as listener.
	 */
	private void createMenu() {
		menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);
		MenuItem menu = new MenuItem(menubar, SWT.CASCADE);
		menu.setText(model.i18n("Menu"));
		
		Menu mainmenu = new Menu(shell, SWT.DROP_DOWN);
		menu.setMenu(mainmenu);
		
		MenuItem close = new MenuItem(mainmenu, SWT.PUSH);
		close.setText(model.i18n("Close"));
		close.addSelectionListener((SelectionListener)controller);
		
		MenuItem erase = new MenuItem(mainmenu, SWT.PUSH);
		erase.setText(model.i18n("Delete"));
		
		erase.addSelectionListener((SelectionListener)controller);
		
		MenuItem addExam = new MenuItem(mainmenu, SWT.PUSH);
		addExam.setText(model.i18n("Add Exam"));
		addExam.addSelectionListener((SelectionListener)controller);
		
		MenuItem open = new MenuItem(menubar, SWT.PUSH);
		open.setText(model.i18n("Open"));
		open.addSelectionListener((SelectionListener)controller);
		
		MenuItem openAsNew = new MenuItem(menubar, SWT.PUSH);
		openAsNew.setText(model.i18n("Open As New"));
		openAsNew.addSelectionListener((SelectionListener)controller);
	}
	
	/**
	 * Creates the tab with created exams
	 */
	private void createCreatedTab(){
		saveHandler = null;
		try {
			saveHandler = model.getSaveTreeFileHandler();
		} catch (IOException e) {
			MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
			error.setMessage(model.i18n("The exam save dir is not properly configured!"));
			error.open();
		}
		if (saveHandler != null) {
			saveTree = new Tree(tabFolder, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			buildTreeFromHandler(saveTree, saveHandler);
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(model.i18n("Created On PDA"));
			item.setControl(saveTree);
		}
	}
	
	/**
	 * Creates the tab with transferred exams. 
	 */
	private void createTransferredTab() {		
		databaseHandler = null;
		try {
			databaseHandler = model.getDatabaseTreeFileHandler();
		} catch (IOException e) {
			MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
			error.setMessage(model.i18n("The exam database dir is not properly configured!"));
			error.open();
		}
		if (databaseHandler != null) {
			databaseTree = new Tree(tabFolder, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			buildTreeFromHandler(databaseTree, databaseHandler);
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(model.i18n("Transferred From PC"));
			item.setControl(databaseTree);
		}
	}
	
	/**
	 * Builds a graphical tree from a tree file handler. The patients are at the first level,
	 * and the exams for each patient are subtrees to each patient
	 */
	private void buildTreeFromHandler(Tree tree, TreeFileHandler handler){
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TreeMap patients = null;
		PatientIdentifier[] pids = null;
		
		/* Get the patient identifiers */
		try {
			pids = handler.getPatients(); 
			patients = new TreeMap();
		} catch (Exception e) {
			showTreeError();
			e.printStackTrace();
			System.exit(1);
		}
		
		/* Add the pids (personnummer) to the treemap */
		for (int i = 0; i < pids.length; i++) {
			patients.put(pids[i].getPID(), pids[i]);
		}

		
		TreeItem[] journals = new TreeItem[patients.size()];
		PatientIdentifier[] ids = (PatientIdentifier[])patients.values().toArray(new PatientIdentifier[0]); 
		
		/* Add the patients to the tree, and exams to each patient */
		for (int i = 0; i < journals.length; i++) {
			
			/* Create the patient tree item */
			journals[i] = new TreeItem(tree, SWT.SINGLE);
			journals[i].setText(ids[i].getPID());
			journals[i].setData(ids[i]);
			
			ExaminationIdentifier[] examIDs = null;
			try {
				examIDs = handler.getExaminations(ids[i]);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				showTreeError();
				System.exit(1);
			}
			
			/* create the exam tree items */
			if(examIDs != null) {
				TreeItem exams[] = new TreeItem[examIDs.length];
				for (int j = 0; j < examIDs.length; j++) {
					exams[j] = new TreeItem(journals[i], SWT.SINGLE);
					String date = DateFormat.getInstance().format(examIDs[j].getTime());
					exams[j].setText(date);
					exams[j].setData(examIDs[j]);
					examIDs[j].getTime();
				}
			}
		}
	}
	
	/**
	 * Shows an error message.
	 */
	private void showTreeError() {
		MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
		error.setMessage(model.i18n("Fatal error, tree file reading failed. MiniMed will exit."));
		error.open();
	}
}