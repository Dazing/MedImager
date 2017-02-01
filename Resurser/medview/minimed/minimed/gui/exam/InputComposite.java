package minimed.gui.exam;


import java.beans.PropertyChangeListener;
import minimed.core.models.InputModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * A class used to show an inputfield in the examination. The appearance of the input field
 * is built upon the information in the given InputModel.
 * 
 * @author Joni Paananen
 */
public abstract class InputComposite extends Composite{
	protected InputModel model;
	
	/**
	 * Constructs a new instance of this class given its parent, a style value 
	 * describing its behavior and appearance and an InputModel describing what 
	 * to be shown. The style value is either one of the style constants defined 
	 * in class SWT which is applicable to instances of this class, or must be 
	 * built by bitwise OR'ing together (that is, using the int "|" operator) two 
	 * or more of those SWT style constants. The class description lists the 
	 * style constants that are applicable to the class. Style bits are also 
	 * inherited from superclasses. 
	 * 
	 * @param pParent a widget which will be the parent of the new instance (cannot be null)
	 * @param pStyle the style of widget to construct
	 * @param pModel the model on which to build the input
	 */
	public InputComposite(Composite pParent, int pStyle, InputModel pModel){
		super(pParent, pStyle);
		this.model = pModel;
	}
	
	/**
	 * Empty method supposed to be overridden by subclasses for which it is
	 * applicable. When overridden, the subclass should set focus to a main
	 * text input field.
	 */
	public void focus() {}
	
    /**
     * Returns true if the input model is visible, false otherwise.
     * 
     * @return true if the input model is visible, false otherwise.
     */
    public boolean visible() {
    	return model.isVisible();
    }
	
    /**
     * Returns the preferred height of the input, given a width hint. 
     * 
     * @param pWidth a width hint. 
     * @return the preferred height of the input. 
     */
    public abstract int getHeight(int pWidth);
    
    /**
     * Returns true if the input model has dependees, false otherwise.
     * 
     * @return true if the input model has dependees, false otherwise.
     */
    public boolean hasDependees() {
    	return model.hasDependees();
    }
    
    /**
     * Adds a property change listener to this input. 
     * 
     * @param pListener the listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener pListener) {
    	model.addPropertyChangeListener(pListener);
    }
    
    /**
     * Removes a property change listener from this input. 
     * 
     * @param pListener the listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener pListener) {
    	model.removePropertyChangeListener(pListener);
    }
    
    /**
     * Returns true if the input model is editable, false otherwise.
     * 
     * @return true if the input model is editable, false otherwise.
     */
    public boolean isEditable() {
    	return model.isEditable();
    }
    
    /**
     * Returns true if the input model is translatable, false otherwise.
     * 
     * @return true if the input model is translatable, false otherwise.
     */
    public boolean isTranslateAble() {
    	return model.isTranslateAble();
    }
    
    /**
     * Returns true if the input is required, false otherwise.
     * 
     * @return true if the input is required, false otherwise.
     */
    public boolean isRequired() {
    	return model.isRequired();
    }
    
    /**
     * Returns true if the input is an identification, false otherwise.
     * 
     * @return true if the input is an identification, false otherwise.
     */
    public boolean isIdentification() {
    	return model.isIdentification();
    }
    
    /**
     * Returns the name of the input.
     * 
     * @return the name of the input.
     */
    public String getInputName(){
    	return model.getName();
    }
    
    /**
     * Returns the chosen values of the input. An empty array is returned is no value has been chosen.
     * 
     * @return the array of chosen values
     */
    public abstract String[] getValues();

    /**
     * Sets the given values. 
     * 
     * @param pValues the values to set.
     */
    public abstract void setValues(String[] pValues);
    
    /**
     * Creates a font that fits the current OS, and has a specified style.
     * 
	 * The style value is either one of the style constants SWT.NORMAL, 
	 * SWT.BOLD, SWT.ITALIC defined in class SWT or must be built 
	 * by bitwise OR'ing together (that is, using the int "|" operator) two 
	 * or more of these SWT style constants. 
	 * 
     * @param pControl a control to get the font data from.
     * @param pStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>.
     * @return the font created with the given data.
     */
	protected Font getStyledFont(Control pControl, int pStyle) {
		FontData[] fd = pControl.getFont().getFontData();
		for(int i = 0; i < fd.length; i++) {
			fd[i].setStyle(pStyle);
		}
		Font result = new Font(pControl.getDisplay(), fd);
		return result;
	}
	
	/**
	 * Creates a label that automatically wraps text with a certain style.
	 * It is assumed that a <code>GridLayout</code> is used where the label is placed
	 * but the horizontal span may be specified.
	 * 
	 * @param pText the text to be shown.
	 * @param pFontStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>.
	 * @param pHorizontalSpan how many columns the label will span, this should be.
	 * 			equal to the total number of columns in the layout for this method.
	 * 			to function correctly.
	 * @return the label created from the given data.
	 */
	protected Label getGridWrapLabel(String pText, int pFontStyle, int pHorizontalSpan){
		return getStyledGridLabel(this, pText, SWT.WRAP, pFontStyle, GridData.FILL_HORIZONTAL, pHorizontalSpan);
	}
	
	/**
	 * Creates a label that with a certain style and with text of a certain style.
	 * It is assumed that a <code>GridLayout</code> is used where the label is placed, 
	 * but the desired horizontal span and style of the <code>GridData</code> may be specified.
	 * 
	 * @param pParent the parent of the label.  
	 * @param pText the text to be shown.
	 * @param pLabelStyle a SWT style, i.e. <code>SWT.WRAP</code>.
	 * @param pFontStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>.
	 * @param pGridDataStyle a style defined in <code>GridData</code>
	 * @param pHorizontalSpan how many columns the label will span, this should be.
	 * 			equal to the total number of columns in the layout for this method.
	 * 			to function correctly.
	 * @return the label created from the given data.
	 */
	protected Label getStyledGridLabel(Composite pParent, String pText, int pLabelStyle, int pFontStyle, int pGridDataStyle, int pHorizontalSpan) {
		Label l = new Label(pParent, pLabelStyle);
		l.setFont(getStyledFont(pParent, pFontStyle));
		l.setText(pText);
		GridData data = new GridData(pGridDataStyle);
		data.horizontalSpan = pHorizontalSpan;
		l.setLayoutData(data);
		return l;
	}
}
