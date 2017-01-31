package minimed.gui.exam;


import java.beans.PropertyChangeListener;
import minimed.core.MinimedConstants;
import minimed.core.datahandling.examination.ExaminationValueContainer;
import minimed.core.datahandling.examination.tree.Tree;
import minimed.core.datahandling.examination.tree.TreeBranch;
import minimed.core.datahandling.examination.tree.TreeLeaf;
import minimed.core.models.CategoryModel;
import minimed.core.models.InputModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A class for showing all InputModels belonging to a certain CategoryModel.
 * 
 * @author Joni Paananen, Jens Hultberg
 */
public class CategoryComposite extends Composite implements Category {
	private CategoryModel model;
	private InputComposite[] inputs;
	private StackLayout layout;
	private Composite[] pages;
	private int numPages;
	private int numInputs;
	private int currentPage;
	private Point shellSize;
	
	/**
	 * Creates a new CategoryComposite. The style is passed on to the super constructor,
	 * which is a <code>Composite</code>. The <code>ExaminationValueContainer</code> passed 
	 * is used to preset the values in the container on the underlying <code>InputComposites</code>. 
	 * All terms in the container which do not match any of the <code>InputComposites</code>
	 * are ignored.
	 * 
	 * @param pParent the Composite parent.
	 * @param pStyle the SWT style.
	 * @param pCategory the CategoryModel to represent.
	 * @param pContainer values that are to be preset in the category (may be null).
	 */
	public CategoryComposite(Composite pParent, int pStyle, CategoryModel pCategory, 
			ExaminationValueContainer pContainer){
		super(pParent, pStyle);
		model = pCategory;
		currentPage = 0;
		
		layout = new StackLayout();
		this.setLayout(layout);

		/* Creates an array of InputComposite, according to each inputs type. */
		InputModel[] models = model.getInputs();
		numInputs = models.length;
		inputs = new InputComposite[numInputs];
		for (int i = 0; i < numInputs; i++) {
			switch(models[i].getType()) {
				case MinimedConstants.FIELD_TYPE_SINGLE: 			inputs[i] = new SingleInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_MULTI: 			inputs[i] = new MultiInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_NOTE: 				inputs[i] = new NoteInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_INTERVAL: 			inputs[i] = new IntervalInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_IDENTIFICATION: 	inputs[i] = new TextInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_QUESTION: 			inputs[i] = new QuestionInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_TEXT: 				inputs[i] = new TextInput(this, SWT.NONE, models[i]); break;
				case MinimedConstants.FIELD_TYPE_VAS: 				inputs[i] = new VASInput(this, SWT.NONE, models[i]); break;
			}

			/* Presets values on the inputs, if present. */
			if (pContainer != null && pContainer.termHasValues(models[i].getName())) {
				try {
					inputs[i].setValues(pContainer.getValues(models[i].getName()));
				} catch(Exception e) {
					/* Do nothing for the moment. */
					e.printStackTrace();
				}
			}
		}
		
		/* 
		 * Does a layout of all inputs on several pages, each filled with 
		 * as many inputs as possible. 
		 */
		shellSize = this.getShell().getSize();
		layoutPages(shellSize.x, shellSize.y-50);
	}
	
	/**
	 * Adds a given <code>PropertyChangeListener</code> to all inputs in the category.
	 * Used for implementing dependencies between inputs.
	 * 
	 * @param pListener the listener to add. 
	 */
	public void addPropertyChangeListener(PropertyChangeListener pListener) {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].addPropertyChangeListener(pListener);
		}
	}
	
	/**
	 * Removes a given <code>PropertyChangeListener</code> from all inputs in the category.
	 * Used for implementing dependencies between inputs. 
	 * 
	 * @param pListener the listener to remove. 
	 */
	public void removePropertyChangeListener(PropertyChangeListener pListener) {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].removePropertyChangeListener(pListener);
		}
    }
	
	/**
	 * Not implemented.
	 */
	public void dispose() {}
	
	/**
	 * Called by ExamView when it is resized.
	 * 
	 * @param pChanged true if the visibility of some input has changed. 
	 */
	public void layoutPages(boolean pChanged) {
		Point p = this.getShell().getSize();
		
		/* 
		 * If the size has changed, or the visibility of some
		 * input has changed perform a new layout, otherwise do nothing. 
		 */
		if(!shellSize.equals(p) && p.x != 0 && p.y != 0) {
			shellSize = p;
			/* 
			 * Note! Give width and height (in that order) 
			 * when running on a PC and the opposite when 
			 * running on the PDA. 
			 */ 
			/* 
			 * Uses the size of the shell minus the approximate size
			 * of the menu and the navigation items on the lower part
			 * of the screen (arrows etc).  
			 */
			layoutPages(shellSize.x, shellSize.y-50);
		} else if (pChanged) {
			layoutPages(shellSize.x, shellSize.y-50);
		}
	}
	
	/**
	 * Lays out all inputs on pages. The number of 
	 * inputs per page depends on their size. 
	 * 
	 * Note! The long side of the PDA screen is considered
	 * its width and the short its height. 
	 * 
	 * @param pX the width of the composite.
	 * @param pY the height of the composite.
	 */
	private void layoutPages(int pX, int pY) {
		int pageNumber = 0;
		
		pages = new Composite[numInputs];
		pages[pageNumber] = new Composite(this, SWT.NONE); 
		pages[pageNumber].setLayout(new GridLayout());

		/*
		 * Calculates the preferred size of each input and fits as 
		 * many inputs as possible to each page. Only shows the ones
		 * who are marked as visible.
		 */
		int usedSpace = 0;
		for (int i = 0; i < numInputs; i++) {
			if (inputs[i].visible()) { 
				/* Calculates the preferred size of the input. */
				int inputSize = inputs[i].getHeight(pX);
				/* 
				 * Adds the input to the current page if it fits
				 * or creates a new page otherwise. 
				 */
				if (inputSize+usedSpace < pY || usedSpace == 0) {
					usedSpace += inputSize;
				} else {
					/* 
					 * Creates a new page and sets the used space of
					 * the new page to the size of the input. 
					 */
					pageNumber++;
					usedSpace = inputSize;
					pages[pageNumber] = new Composite(this, SWT.NONE);
					pages[pageNumber].setLayout(new GridLayout());
				}
				/* Places the input on the page and sets its layout data. */
				inputs[i].setParent(pages[pageNumber]);
				inputs[i].setVisible(true);
				inputs[i].setLayoutData(new GridData(GridData.FILL_BOTH));
				pages[pageNumber].layout(true, true);
			} else {
				/* Do nothing - invisible inputs are not added. */
			}
		}

		/* Sets the total number of pages. */
		numPages = pageNumber + 1;
		
		/* Puts the current page on top. */
		layout.topControl = pages[currentPage];
		this.layout(true);
	}
	
	/**
	 * Creates and returns a tree with all, answered, 
	 * questions of the category. 
	 * 
	 * @return a tree for the category.
	 */
	public Tree getCategoryTree() {
		Tree category = new TreeBranch(getTitle());
		for (int i = 0; i < inputs.length; i++) {
			String[] values = new String[0];
			
			/* Only retrieve values for visible inputs. */
			if (inputs[i].visible()) {
				values = inputs[i].getValues();
			} 
			
			/* If the input returned any values, these are added as leafs on a new branch. */
			if (values.length > 0) {
				Tree input = new TreeBranch(inputs[i].getInputName());
				category.addChild(input);
				for (int j = 0; j < values.length; j++) {
					Tree leaf = new TreeLeaf(values[j]);
					input.addChild(leaf);
				}
			}
		}
		return category;
	}
	
	/**
	 * Returns the current page of this CategoryComposite, as a 0 relative index
	 * 
	 * @return the current page index.
	 */
	public int getCurrentPageIndex() {
		return currentPage;
	}
	
	/**
	 * Returns the number of pages in the category
	 * 
	 * @return the number of pages.
	 */
	public int getNumPages() {
		return numPages;
	}
	
	/**
	 * Returns the title of the CategoryModel
	 * 
	 * @return the title of the category.
	 */
	public String getTitle() {
		return model.getTitle();
	}
	
	/**
	 * Tries to switch to the next page, returns true if the paging succeeds. Returns 
	 * false if the current page is the last page.
	 * 
	 * @return false if the current page is the last page, true otherwise.
	 */
	public boolean pageRight() {
		if (currentPage < numPages - 1) {
			layout.topControl = pages[++currentPage];
			this.layout();
			return true;
		}
		return false;
	}
	
	/**
	 * Tries to switch to the previous page, returns true if the paging 
	 * succeeds. Returns false if the current page is the first page.
	 * 
	 * @return false if the current page is the first page, true otherwise.
	 */
	public boolean pageLeft() {
		if (currentPage > 0) {
			layout.topControl = pages[--currentPage];
			this.layout();
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the current page to the given index. If the index is out
	 * of bounds, nothing happens.
	 * 
	 * @param pIndex the index of the page to be set.
	 */
	public void setPage(int pIndex) {
		if (pIndex >= 0 && pIndex < numPages) {
			currentPage = pIndex;
			layout.topControl = pages[currentPage];
			this.layout();
		}
	}
	
	/**
	 * Sets the shell text to the title of the category. For example, 
	 * if the category title is <code>Oral Status</code>,
	 * the new shell text is <code>Oral Status</code>.
	 */
	public void setShellText() {
		this.getShell().setText(model.getTitle());
	}
	
	/**
	 * Used to set focus on the first input on the current page.
	 */
	public void focus() {
		Control[] children = pages[currentPage].getChildren();
		((InputComposite)children[0]).focus();
	}
}
