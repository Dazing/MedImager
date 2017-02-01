package minimed.gui.exam;


import minimed.core.models.InputModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import java.lang.Math;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Creates a component of type 'single'. A <i>single</i> component can be made up of either a 
 * series of radio buttons or, if the number of options exceed OPTIONS_LIMIT, a combo box.
 * 
 * @author Andreas Andersson
 */
public class SingleInput extends InputComposite implements SelectionListener {
	public static final int OPTIONS_LIMIT = 4;
	private Button[] buttons;
	private Button selectedButton;
	private Combo combo;
	private Combo selectedCombo;
	private GridLayout gridLayout;
	private Label description;
	private Label comment;
	
	/* 
	 * Type being created. Dynamically decided upon the number of options available.
	 * '1' for radio buttons
	 * '2' for combo box
	 */
	private int singleType;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent A composite which will be the parent of the new instance.
	 * @param pStyle The style of widget to construct.
	 * @param pModel The model used to build the interval.
	 */
	public SingleInput(Composite pParent, int pStyle, InputModel pModel){
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
		
		String[] termValues = model.getPresetModel().getPresets();
		buttons = new Button[termValues.length];
		
		/* Should we construct a series of radio buttons or a drop-down menu? */
		if(termValues.length <= OPTIONS_LIMIT){
			singleType = 1;
			for (int i = 0; i < termValues.length; i++) {
				buttons[i] = new Button(this, SWT.RADIO);
				buttons[i].setText(termValues[i]);
				buttons[i].addSelectionListener(this);
			}
		/* More than OPTIONS_LIMIT options, create a combo box... */
		} else {
			singleType = 2;
			combo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			for (int i = 0; i < termValues.length; i++) {
				combo.add(termValues[i]);
			}
			combo.addSelectionListener(this);
		}
	}
	
	/**
	 * Used when resizing and/or removing or adding inputs 
	 * due to dependencies.
	 * 
	 * @param pWidth the desired with of the input.
	 * @return the preferred height of the input.
	 */
	public int getHeight(int pWidth) {
		/* Computes the approximate size of the name and description. */
		int inputSize = 30;
		inputSize += Math.max((int)1.5*description.getSize().y, 20);
		
		if (singleType == 1) {
			return inputSize+20*buttons.length;	
		} else {
			return inputSize+combo.computeSize(pWidth, SWT.DEFAULT, false).y;
		}
	}
	
	/**
	 * Returns the values that the user has chosen in the composite, as an array of String.
	 * 
	 * @return the values that has been chosen.
	 */
	public String[] getValues(){
		String[] retval = new String[1];
		if (singleType == 1) {
			if(selectedButton == null){
				return new String[0];
			}
			retval[0] = selectedButton.getText();
			return retval;
		} else {
			if(selectedCombo == null){
				return new String[0];
			}
			retval[0] = selectedCombo.getText();
			return retval;
		}
	}

	public void setValues(String[] pValues){
		if(pValues.length > 0){
			if (singleType == 1) {
				for (int i = 0; i < buttons.length; i++) {
					if(buttons[i].getText().equalsIgnoreCase(pValues[0])){
						buttons[i].setSelection(true);
						if(selectedButton != null){
							selectedButton.setSelection(false);
						}
						selectedButton = buttons[i];
					}
				}
			}
			else {
				String[] values = combo.getItems();
				for(int i = 0; i < values.length; i++){
					if(values[i].equalsIgnoreCase(pValues[0])){
						combo.select(i);
						selectedCombo = combo;
					}
				}
			}
		}
	}

	/**
	 * Notifies dependees that the value of the input has changed.    
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		if (singleType == 1) {
			Button tmp = (Button)pEvent.getSource();
			
			if (tmp.getSelection()) {
				selectedButton = tmp;
				if (model.hasDependees()) {
					model.notifyDependees(selectedButton.getText());
				}
			}
		} else {
			Combo tmp = (Combo)pEvent.getSource();
			
			int retval = tmp.getSelectionIndex();
			boolean selected = ((retval != -1)?true:false);
			
			if (selected) {
				selectedCombo = tmp;
				if (model.hasDependees()) {
					model.notifyDependees(selectedCombo.getText());
				}
			}
       	}
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
}
