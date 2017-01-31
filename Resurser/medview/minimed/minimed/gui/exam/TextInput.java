package minimed.gui.exam;


import minimed.core.models.InputModel;
import minimed.core.MinimedConstants;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.*;

/**
 * Creates a component of type 'text'. A <i>text</i> component is nothing more than an
 * input field allowing the user to enter a short text (such as a name or telephone number).
 * 
 * @author Andreas Andersson
 */
public class TextInput extends InputComposite {
	private Label description;
	private Label comment;
	private Text input;
    	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent A composite which will be the parent of the new instance
	 * @param pStyle The style of widget to construct
	 * @param pModel The model used to build the interval
	 */
	public TextInput(Composite pParent, int pStyle, InputModel pModel){
		super(pParent, pStyle, pModel);
	
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		this.setLayout(gridLayout);
		GridData gridInput = new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING);
		
		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		description = getGridWrapLabel(model.getDescription(), SWT.BOLD, 1);
		comment = getGridWrapLabel(model.getComment(), SWT.ITALIC, 1);
		
		input = new Text(this, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(gridInput);
		input.addFocusListener(new InputListener());
	}
	
	/**
	 * Used when resizing and/or removing or adding inputs 
	 * due to dependencies.
	 * 
	 * @param pWidth the desired with of the input.
	 * @return the preferred height of the input.
	 */
	public int getHeight(int pWidth) {
		return Math.max((int)1.5*this.computeSize(pWidth, SWT.DEFAULT, false).y, 60);
	}
	
	/**
	 * Returns the values that the user has chosen in the composite, as an array of String.
	 * 
	 * @return the values that has been chosen.
	 */
	public String[] getValues(){
		if(input.getText().equals("")){
			return new String[0];
		}
		String[] retval = new String[1];
		retval[0] = input.getText();
		return retval;
	}
	
	/**
	 * Sets the first string in the array as text in the
	 * input field.
	 * 
	 * @param pValues an array containing a single string to set. 
	 */
	public void setValues(String[] pValues) {
		if (pValues.length > 0) {
			input.setText(pValues[0]);
		}
	}

	/**
	 * Sets the focus to the input field. 
	 */
	public void focus() {
		input.setFocus();
	}
	
	/**
	 * If the field type is FIELD_TYPE_IDENTIFICATION, the format of the 
	 * given PID is verified. 
	 */
	private class InputListener extends FocusAdapter {
		/**
		 * Performs the check when focus is lost. 
		 */
		public void focusLost(FocusEvent pEvent) {
			if (model.getType() == MinimedConstants.FIELD_TYPE_IDENTIFICATION && !input.isFocusControl()) {
				String num = input.getText();
				boolean legal = true;
				if (num.length() != 13) {
					legal = false;
				} else {
					for (int i = 0 ; i < num.length(); i++) {
						if(((i >= 0 && i < 8)) || i > 8){
							legal = legal && Character.isDigit(num.charAt(i));
						} else {
							legal = legal && num.charAt(i) == '-';
						}
					}
				}
				if (!legal) {
					MessageBox error = new MessageBox(input.getShell(), SWT.ICON_ERROR | SWT.OK);
					error.setMessage("Felaktigt personnummer\n≈≈≈≈MMDD-XXXX");
					error.open();
				}
			}
		}
	}
}
