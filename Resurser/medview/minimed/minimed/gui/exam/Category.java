package minimed.gui.exam;


import java.beans.PropertyChangeListener;
import minimed.core.datahandling.examination.tree.Tree;

/**
 * Interface for categories.
 * 
 * @author Jens Hultberg
 */
public interface Category {
		
	/**
	 * Builds and returns the category tree.
	 * 
	 * @return the category tree.
	 */
	public Tree getCategoryTree();
	
	/**
	 * Forces a new layout of all pages in the category. 
	 * 
	 * @param pChanged true if the visibility of some inputs has changed, false otherwise.
	 */
	public void layoutPages(boolean pChanged);
	
	/**
	 * Adds a PropertyChangeListener to all the inputs of the category, used
	 * for implementing dependencies between inputs. 
	 * 
	 * @param pListener the listener to add. 
	 */
	public void addPropertyChangeListener(PropertyChangeListener pListener);
	
	/**
	 * Used to set focus on the first input on the current page.
	 */
	public void focus();
			
	/**
	 * Sets the shell text to the title of the category. For example, 
	 * if the category title is <code>Oral Status</code>,
	 * the new shell text is <code>Oral Status</code>.
	 */
	public void setShellText();
	
	/**
	 * Sets the current page to the given index. If the index is out
	 * of bounds, nothing happens.
	 * 
	 * @param pIndex the index of the page to be set.
	 */
	public void setPage(int pIndex);
	
	/**
	 * Returns the title of the category
	 * 
	 * @return the title of the category.
	 */
	public String getTitle();
	
	/**
	 * Tries to switch to the previous page, returns true if the paging 
	 * succeeds. Returns false if the current page is the first page.
	 * 
	 * @return false if the current page is the first page, true otherwise.
	 */
	public boolean pageLeft();

	/**
	 * Tries to switch to the next page, returns true if the paging succeeds. Returns 
	 * false if the current page is the last page.
	 * 
	 * @return false if the current page is the last page, true otherwise.
	 */
	public boolean pageRight();
	
	/**
	 * Returns the number of pages in the category
	 * 
	 * @return the number of pages.
	 */
	public int getNumPages();
	
	/**
	 * Returns the current page of this CategoryComposite, as a 0 relative index.
	 * 
	 * @return the current page index.
	 */
	public int getCurrentPageIndex();
	
	/**
	 * Disposes the exam. 
	 */
	public void dispose();
}
