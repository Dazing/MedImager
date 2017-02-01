package minimed.gui.exam;


import minimed.core.models.InputModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Represents a selection of intervals between two consecutive values given
 * by a set of term-values. The term-values A, B, C and D would give the 
 * intervals "A-B", "B-C", "C-D".
 * 
 * Each interval is represented as a radio button. 
 * Only one button can be selected at the time. 
 * 
 * @author Jens Hultberg
 */
public class IntervalInput extends InputComposite implements SelectionListener {
	private Label description;
	private Label comment;
	private Button[] buttons;
	private Button selected;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent a composite which will be the parent of the new instance.
	 * @param pStyle the style of widget to construct.
	 * @param pModel the model used to build the interval.
	 */
	public IntervalInput(Composite pParent, int pStyle, InputModel pModel) {
		super(pParent, pStyle, pModel);
		
		/*
		 * Sets the layout for the IntervalInput and constructs
		 * the layout data used by its components. 
		 */
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		this.setLayout(layout);

		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		description = getGridWrapLabel(model.getDescription(), SWT.BOLD, 1);
		comment = getGridWrapLabel(model.getComment(), SWT.ITALIC, 1);

		/*
		 * Creates one button for each interval and adds
		 * this instance as a SelectionListener. 
		 */
		String[] termValues = model.getPresetModel().getPresets();
		buttons = new Button[termValues.length-1];
		for (int i = 0; i < termValues.length-1; i++) {
			buttons[i] = new Button(this, SWT.RADIO);
			buttons[i].setText(termValues[i]+"-"+termValues[i+1]);
			buttons[i].addSelectionListener(this);
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

		return inputSize+20*buttons.length;	

	}
	
	/**
	 * Returns an array containing only the interval selected by the user. 
	 * 
	 * @return an array containing only the selected interval. 
	 */
	public String[] getValues() {
		String[] result = new String[1];
		result[1] = selected.getText();
		return result;
	}
	
	/**
	 * Sets the given interval. 
	 * 
	 * @param pValues an array containing a single interval.
	 */
	public void setValues(String[] pValues) {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getText() == pValues[0]) {
				buttons[i].setSelection(true);
				selected = buttons[i];
			}
		}
	}
	
	/**
	 * Notifies dependees that the selected interval has changed.    
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetSelected(SelectionEvent pEvent) {
		Button source = (Button)pEvent.getSource();
		/* 
		 * Events are fired also when buttons are unselected 
		 * (due to the selection of an other button). These
		 * events are not of interest. 
		 */
		if (source.getSelection()) {
			selected = source;
			if (model.hasDependees()) {
				model.notifyDependees(selected.getText());
			}
		}
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}
}
