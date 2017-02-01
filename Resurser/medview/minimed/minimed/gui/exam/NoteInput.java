package minimed.gui.exam;


import minimed.core.models.InputModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Creates a component of type 'note'. A <i>note</i> component is just like a 'text' component,
 * but differs in the number of lines the input field spawns. 
 * 
 * @author Andreas Andersson
 */
public class NoteInput extends InputComposite {
	private Label description;
	private Label comment;
	private Text input;
	private GridLayout gridLayout;
	private GridData gridInput;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent A composite which will be the parent of the new instance
	 * @param pStyle The style of widget to construct
	 * @param pModel The model used to build the interval
	 */
	public NoteInput(Composite pParent, int pStyle, InputModel pModel){
		super(pParent, pStyle, pModel);
		
		gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		this.setLayout(gridLayout);
		
		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		description = getGridWrapLabel(model.getDescription(), SWT.BOLD, 1);
		comment = getGridWrapLabel(model.getComment(), SWT.ITALIC, 1);

		gridInput = new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING);
		gridInput.heightHint = 50;	//Should be about four lines of text
		input = new Text(this, (SWT.MULTI | SWT.WRAP | SWT.READ_ONLY));
		input.setLayoutData(gridInput);
	}
	
	/**
	 * Returns the values that the user has chosen in the composite, as an array of String.
	 * 
	 * @return The values that has been chosen
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
	 * Used when resizing and/or removing or adding inputs 
	 * due to dependencies.
	 * 
	 * @param pWidth the desired with of the input.
	 * @return the preferred height of the input.
	 */
	public int getHeight(int pWidth) {
		return Math.max((int)1.5*this.computeSize(pWidth, SWT.DEFAULT, false).y, 120);
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
}
