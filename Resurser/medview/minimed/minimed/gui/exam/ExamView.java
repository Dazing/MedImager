package minimed.gui.exam;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import minimed.MinimedModel;
import minimed.core.datahandling.examination.tree.Tree;
import minimed.core.datahandling.examination.tree.TreeBranch;
import minimed.core.models.CategoryModel;
import minimed.core.models.ExaminationModel;
import minimed.gui.GUIConstants;
import minimed.gui.MinimedController;
import minimed.gui.MinimedView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * A class for viewing the Exam screen
 * 
 * @author Joni Paananen, Jens Hultberg
 */
public class ExamView extends MinimedView implements PropertyChangeListener, GUIConstants {
	private Menu menubar;
	private MenuItem categoryMenu;
	private List categoryChoise;
	private Category[] categories;
	private Composite categoryView;
	private StackLayout categoryViewLayout;
	private int currentIndex;
	private int numCategories;
	private Button left;
	private Button right;
	private Label info;
	private Combo pages;
	private boolean waitState = false;
		
	/**
	 * Creates an ExamView with the given parameters.
	 * 
	 * @param pShell the shell to use for showing the view
	 * @param pModel the model to base the view on
	 * @param pController the controller related to the view
	 */
	public ExamView(Shell pShell, MinimedModel pModel, MinimedController pController) {
		super(pShell, pModel, pController);
		
		/* 
		 * Gets the examination model from the MinimedModel. More checking should be done
		 * to assert that the file paths have been set correctly.
		 */
		ExaminationModel exam = null;
		try {
			exam = model.getExamination();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (exam == null) {
			MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
			error.setMessage(model.i18n("The path to the Term Values, Term Definitions, or Template file is incorrectly configured!"));
			error.open();
			((ExamController)controller).switchView(START);
		} else {
			currentIndex = 0;
			
			/*
			 * Creates the menubar and the menu withing. The dropdown menu contains
			 * a close button and shortcuts to all categories
			 */
			createMenu(exam.getCategories());
			
			/* Creates the gridlayout for the view. */
			GridLayout gridLayout = new GridLayout(4, false);
			gridLayout.marginHeight = 1;
			gridLayout.marginWidth = 1;
			this.setLayout(gridLayout);
			
			/* Gets the composite that shows the categories, and set the layout data for it. */
			categoryView = buildCategories(this, exam.getCategories());
			GridData catData = new GridData(GridData.FILL_BOTH);
			catData.horizontalSpan = 4;
			categoryView.setLayoutData(catData);

			/* Creates the two arrow buttons for paging, and the info label between. */
			createButtons();
			
			/* Adds a new listener which handles resizing. */
			this.addControlListener(new ControlAdapter() {
				/**
				 * Performs a new layout due to a resize event. 
				 */
				public void controlResized(ControlEvent pEvent) {
					categories[currentIndex].layoutPages(false);
					updatePages();
				}
			});	
		}
	}

	/**
	 * Called by an input which has changed visibility. 
	 * 
	 * @param pEvent an event fired by the input which has been changed. 
	 */
	public void propertyChange(PropertyChangeEvent pEvent) {
		/* 
		 * If an input already has signaled that the visibility has
		 * changed, all consecutive PropertyChangeEvents that are fired
		 * are ignored. 
		 */
		if (!waitState) {
			waitState = true;
			Timer timer = new Timer();
			TimerTask timerTask = new WaitTask(this);
			timer.schedule(timerTask,100);
		} 
	}
	
	/**
	 * Performs a forced page layout of the current category. 
	 */
	private void layoutPages() {
		categories[currentIndex].layoutPages(true);
		updatePages();
		waitState = false;
	}
	
	/**
	 * Disposes the view, the menubar and all children to the view
	 */
	public void dispose(){
		this.getShell().setText("Minimed");
		super.dispose();
		if(menubar != null)
			menubar.dispose();
	}
	
	/**
	 * Builds and saves a tree with the examination.
	 * 
	 * @return true if the save succeeded, false otherwise.
	 */
	public boolean saveTree(){
		Tree examination = new TreeBranch("Examination");
		for (int i = 0; i < categories.length; i++) {
			Tree category = categories[i].getCategoryTree();
			if (category.hasChildren()){
				examination.addChild(category);
			}
		}
		if( examination.hasChildren()){
			try {
				model.saveExamination(examination);
				return true;
			} catch(IOException e) {
				MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
				error.setMessage(model.i18n("The exam could not be saved. Make sure that PID (Person ID) is filled in.")); 
				error.open();
				return false;
			}
		} else {
			MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
			error.setMessage(model.i18n("The exam is empty and has not been saved."));
			error.open();
			return true;
		}
	}
	
	/**
	 * Creates the composite that contains the categories. StackLayout is used to
	 * show one category at the time
	 * 
	 * @param pParent the ExamView instance. 
	 * @param pModels the category models used to build the categories. 
	 */
	private Composite buildCategories(Composite pParent, CategoryModel[] pModels){
		Composite categoryView = new Composite(pParent, SWT.NONE);
		categoryViewLayout = new StackLayout();
		categoryView.setLayout(categoryViewLayout);

		categoryChoise = new List(categoryView, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		
		/* Adds the Mucos input. */
		if (model.getShowMucos().equals("yes")) {
			numCategories = pModels.length+1;
			categories = new Category[numCategories];
			categories[numCategories-1] = new MucosInput(categoryView, SWT.NONE, model.getExaminationValueContainer());
			categories[numCategories-1].addPropertyChangeListener(this);

        } else {
			numCategories = pModels.length;
			categories = new Category[numCategories];
		}
		
		/* Creates an array of CategoryComposites */
		for (int i = 0; i < pModels.length; i++) {
			categories[i] = new CategoryComposite(categoryView, SWT.NONE, pModels[i], model.getExaminationValueContainer());
			categories[i].addPropertyChangeListener(this);
			categoryChoise.add(pModels[i].getTitle());
		}
        if (model.getShowMucos().equals("yes")){
        categoryChoise.add(categories[numCategories-1].getTitle());
        }

        categoryChoise.setData("CategoryList");
		categoryChoise.addSelectionListener(new PageListener());
				
		/* Sets the first category to be on top */
		categoryViewLayout.topControl = (Control)categories[currentIndex];
		categories[currentIndex].focus();
		categoryView.layout();
		categories[currentIndex].setShellText();
				
		return categoryView;
	}
	
	/**
	 * Creates the menubar and the menu within.
	 */
	private void createMenu(CategoryModel[] models){
		menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);
		MenuItem menu = new MenuItem(menubar, SWT.CASCADE);
		menu.setText(model.i18n("Menu"));
		
		Menu mainmenu = new Menu(shell, SWT.DROP_DOWN);
		menu.setMenu(mainmenu);
		
		/* The "Save and close" option. */
		MenuItem close = new MenuItem(mainmenu, SWT.PUSH);
		close.setText(model.i18n("Save And Close"));
		close.setData("Spara och återgå");
		close.addSelectionListener((SelectionListener)controller);

		/* The "Save" option. */
		MenuItem save = new MenuItem(mainmenu, SWT.PUSH);
		save.setText(model.i18n("Save"));
		save.setData("Spara");
		save.addSelectionListener((SelectionListener)controller);
		
		/* The "Close without saving" option. */
		MenuItem noSave = new MenuItem(mainmenu, SWT.PUSH);
		noSave.setText(model.i18n("Close Without Saving"));
		noSave.setData("Återgå utan att spara");
		noSave.addSelectionListener((SelectionListener)controller);
		
		categoryMenu = new MenuItem(menubar, SWT.PUSH);
		categoryMenu.setData("Category");
		categoryMenu.setText("| " + models[currentIndex].getTitle() + "|");
		categoryMenu.addSelectionListener(new PageListener());
	}

	/**
	 * Updates the number of pages when the widget has been resized. 
	 */
	private void updatePages() {
		pages.removeAll();
		for (int i = 0; i < categories[currentIndex].getNumPages(); i++ ) {
			pages.add("" + (i + 1));
		}
		pages.select(categories[currentIndex].getCurrentPageIndex());
	}
	
	/**
	 * Creates a left and a right button, and an info label and page selecting
	 * drop down menu between them
	 */
	private void createButtons() {
		PageListener listener = new PageListener();
		GridData gridData = new GridData();
		gridData.heightHint = 20;
		
		/* 
		 * Creates the left button and sets the layout data, and adds a  
		 * SelectionListener. Also sets it to be disabled (as there is 
		 * no page to the left of the first page). 
		 */
		left = new Button(this, SWT.ARROW | SWT.LEFT);
		left.setData("left");
		left.setEnabled(false);
		left.setLayoutData(gridData);	
		left.addSelectionListener(listener);		
		
		/* Creates the info label. */
		info = new Label(this, SWT.RIGHT);
		info.setText(
				model.i18n("Category") + " (" 
				+ (currentIndex + 1)
				+ "/" + categories.length 
				+ "), " + model.i18n("page"));		
		info.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* Creates the page selecting drow down. */
		pages = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		for (int i = 0; i < categories[currentIndex].getNumPages(); i++ ) {
			pages.add("" + (i + 1));
		}
		pages.select(categories[currentIndex].getCurrentPageIndex());
		pages.setData("pageDD");
		pages.setLayoutData(new GridData(GridData.BEGINNING | GridData.GRAB_HORIZONTAL));
		pages.addSelectionListener(listener);
		
		/* Creates the right button and sets the layout data and a SelectionListener. */
		gridData = new GridData();
		gridData.heightHint = 20;
		right = new Button(this, SWT.ARROW | SWT.RIGHT);
		right.setData("right");
		right.setLayoutData(gridData);
		right.addSelectionListener(listener);
	}	
	
	/**
	 * Listener for the arrow buttons and category shortcuts in the menu
	 */
	private class PageListener extends SelectionAdapter {
		/**
		 * Handles events from the left and right buttons as well as the 
		 * category list.
		 * 
		 * @param pEvent an event.
		 */
		public void widgetSelected(SelectionEvent pEvent){
			Widget source = (Widget)pEvent.getSource();
			/* Modify flag indicating if the category has changed or not. */
			boolean catChanged = false;
			/* If the button pressed was the right arrow. */
			if (source.getData().equals("right")) {
				/* If the current page was the last page in the category. */
				if (!categories[currentIndex].pageRight() 
						&& currentIndex < categories.length - 1) {
					currentIndex++;
					catChanged = true;
					categories[currentIndex].layoutPages(false);
				} else {
					/* Set the page drop down to show the right number. */
					pages.select(categories[currentIndex].getCurrentPageIndex());
				}
			} else if(source.getData().equals("left")) {
				/* If the button pressed was the left arrow. */
				if (!categories[currentIndex].pageLeft() 
						&& currentIndex > 0) {
					/* If the current page was the first page in the category. */
					currentIndex--;
					categories[currentIndex].layoutPages(false);
					categories[currentIndex].setPage(categories[currentIndex].getNumPages() - 1);
					catChanged = true;
				} else {
					/* Set the page drop down to show the right number. */
					pages.select(categories[currentIndex].getCurrentPageIndex());
				}
			} else if (source.getData().equals("pageDD")) {
				/* If the page selection drop down was selected. */
				if (categories[currentIndex].getCurrentPageIndex() != pages.getSelectionIndex()) {
					/* If it was not this listener that made the selection */
					categories[currentIndex].setPage(pages.getSelectionIndex());
				}
			} else if (source.getData().equals("Category")) {
				categoryViewLayout.topControl = categoryChoise;
				categoryChoise.select(currentIndex);
				categoryView.layout();
			} else if (source.getData().equals("CategoryList")) {
				/* Otherwise a category shortcut was pressed. */
				currentIndex = categoryChoise.getSelectionIndex();
				categories[currentIndex].layoutPages(false);
				catChanged = true;
			}
			/* Sets the chosen category on top. */
			if (catChanged) {
				categoryViewLayout.topControl = (Control)categories[currentIndex];
				info.setText(
						model.i18n("Category") + " (" 
						+ (currentIndex + 1)
						+ "/" + categories.length + "), " + model.i18n("page"));	
				categories[currentIndex].setShellText();
				categoryMenu.setText("| " + categories[currentIndex].getTitle() + " |");
				
				pages.setItems(new String[0]);
				for (int i = 0; i < categories[currentIndex].getNumPages(); i++ ) {
					pages.add("" + (i + 1));
				}
				pages.select(categories[currentIndex].getCurrentPageIndex());
				categoryView.getShell().layout();
				categoryView.layout();
			}
			
			/* Enables/disables the left and right buttons according to the current page. */
			if (currentIndex > 0 ||	categories[currentIndex].getCurrentPageIndex() > 0) {
				left.setEnabled(true);
			} else {
				left.setEnabled(false);
			}
			if (currentIndex+1 < numCategories || 
					categories[currentIndex].getNumPages() > categories[currentIndex].getCurrentPageIndex()+1) {
				right.setEnabled(true);	
			} else {
				right.setEnabled(false);	
			}
			
			categories[currentIndex].focus();
		}
	}
	
	/**
	 * Used by propertyChange when the visibility of an input has changed.  
	 */
	private class WaitTask extends TimerTask {
		private ExamView v;
	
		/**
		 * Creates a new instance of this class.
		 * 
		 * @param pView the view that created the WaitTask.
		 */
		public WaitTask(ExamView pView) {
			v = pView;
		}
		
		/**
		 * Does a new page layout on the current category. 
		 */
		public void run() {
			if (v != null) {
				v.getDisplay().syncExec(
						new Runnable() {
							public void run(){
								v.layoutPages();
							}
						}
				);
			}		 
		}
	}
}