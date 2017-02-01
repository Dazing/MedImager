package minimed.gui.exam;


import minimed.core.models.InputModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

/**
 * Represents a Visual Analog Scale (VAS).
 * 
 * The user is allowed to choose one out of one hundred values 
 * (between 0 and 100) on a non-graded scale. 
 * 
 * @author Jens Hultberg
 */
public class VASInput extends InputComposite implements SelectionListener {
	private Label description;
	private Label comment;
	private Scale scale;
	private Label min;
	private Label max;
	private boolean hasChanged;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent the parent composite to build the VAS on.
	 * @param pStyle the style of widget to construct.
	 * @param pModel the model used to build the VAS.
	 */
	public VASInput(Composite pParent, int pStyle, InputModel pModel) {
		super(pParent, pStyle, pModel);
		hasChanged = false;
		
		/*
		 * Sets the layout for the VASInput and constructs
		 * the layout data used by its components. 
		 */
		GridLayout gridLayout = new GridLayout(1, false);
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
		
		/* 
		 * Constructs a scale without grading and with values
		 * between 0 and 100. Also sets this instance of the 
		 * class as listener and sets the layout data.   
		 */
		scale = new Scale((Composite)this, SWT.HORIZONTAL); 
		scale.setMinimum(0);
		scale.setMaximum(100);
		
		/*
		 * Note! The page increment should be set to 100 according
		 * to the definition but this has been changed by request 
		 * from Kliniken för Oral Medicin, Sahlgrenska Universitetssjukhuset. 
		 */ 
		scale.setPageIncrement(10); 
		scale.addSelectionListener(this);
		GridData scaleData = new GridData(GridData.FILL_HORIZONTAL);
		scaleData.horizontalSpan = 2;
		scale.setLayoutData(scaleData);
		
		/* Creates labels with the preset limits (0 and 100). */
		min = new Label(this, SWT.NONE);
		min.setText("0");
		min.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		max = new Label(this, SWT.NONE);
		max.setText("100");
		max.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	}
	
	/**
	 * Returns an array containing only the value selected by the user. 
	 * The array contains a single String representing an int in the range
	 * between 0 and 100. 
	 * 
	 * @return an array containing only the selected value. 
	 */
	public String[] getValues() {
		String[] result = new String[0];
		if (hasChanged) {
			result = new String[1];
			result[0] = Integer.toString(scale.getSelection());
		} 
		return result;
	}
	
	/**
	 * Used when resizing and/or removing or adding inputs 
	 * due to dependencies.
	 * 
	 * @param pWidth the desired with of the input.
	 * @return the preferred height of the input.
	 */
	public int getHeight(int pWidth) {
		return Math.max((int)1.5*this.computeSize(pWidth, SWT.DEFAULT, false).y, 80);
	}
	
	/**
	 * Sets the given value to the scale.
	 * 
	 * @param pValues an array containing a single integer represented as a string. 
	 */
	public void setValues(String[] pValues) {
		scale.setSelection(Integer.parseInt(pValues[0]));
	}
	
	/**
	 * Notifies dependees that the value of the VAS has changed.    
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		hasChanged = true;
		if (model.hasDependees()) {
			model.notifyDependees(Integer.toString(((Scale)pEvent.getSource()).getSelection()));	
		}
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
}
