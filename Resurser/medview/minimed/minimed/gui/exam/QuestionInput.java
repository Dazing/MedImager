package minimed.gui.exam;


import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import minimed.core.models.*;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * A class for showing an InputModel of the type question
 * 
 * @author Joni Paananen
 */
public class QuestionInput extends InputComposite {
	private String[] values;
	private Button[] buttons;
	private Text answer;
	private Combo unitDD;
	private String[] chosen;
	private Label description;
	private Label comment;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent the parent for the QuestionInput.
	 * @param pStyle the SWT style to be used.
	 * @param pModel the InputModel to show.
	 */
	public QuestionInput(Composite pParent, int pStyle, InputModel pModel){
		super(pParent, pStyle, pModel);
		
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);

		chosen = new String[0];
		values = pModel.getPresetModel().getPresets();

		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		description = getGridWrapLabel(model.getDescription(), SWT.BOLD, 3);
		comment = getGridWrapLabel(model.getComment(), SWT.ITALIC, 3);
		
		/*
		 * Creates a linked list of the units found in the values[] array, if any,
		 * and a linked list choises of all other values.
		 */
		LinkedList units = new LinkedList();
		LinkedList choises = new LinkedList();
		for(int i = 0; i < values.length; i++){
			/* If the current value is a unit (begins with ?) */
			if(values[i].charAt(0) == '?'){
				if(values[i].length() > 2){
					units.add(values[i].substring(2));
				}
				/* If there is just an ? */
				else{
					units.add("");
				}
			}
			/* Otherwise its a normal value */
			else{
				choises.add(values[i]);
			}
		}
		
		/*
		 * Create an array of buttons, where the first values.length buttons are
		 * normal radio buttons, and the possible following button is a radio
		 * button, a text input field and a drop down menu to choose an unit from.
		 */
		buttons = new Button[values.length - units.size() + 1];
		RadioListener radioListener = new RadioListener();
		int i = 0;
		for(Iterator it = choises.iterator(); it.hasNext();){
				buttons[i] = new Button(this, SWT.RADIO);
				buttons[i].setText((String)it.next());
				GridData buttonData = new GridData();
				buttonData.horizontalSpan = 3;
				buttons[i].addSelectionListener(radioListener);
				buttons[i++].setLayoutData(buttonData);
		}
		/* Create the possible last special radio button. */
		if(!units.isEmpty()){
			buttons[buttons.length - 1] = new Button(this, SWT.RADIO);
			buttons[buttons.length - 1].addSelectionListener(radioListener);
			
			answer = new Text(this, SWT.BORDER);
			GridData answerData = new GridData();
			answerData.widthHint = 30;
			answer.setLayoutData(answerData);
			answer.addModifyListener(new AnswerListener());
			
			unitDD = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			unitDD.setItems((String[])units.toArray(new String[0]));
			unitDD.select(0);
			unitDD.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
			unitDD.addSelectionListener(new UnitsListener());
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
		inputSize += Math.max(1.5*description.getSize().y, 20);
		
		/* Adds the size of all the buttons. */ 
		inputSize += 20*buttons.length;
	
		/* Adds the size of the answer field. */
		if(answer != null)
			inputSize += answer.computeSize(pWidth, SWT.DEFAULT, false).y;
		
		return inputSize;
	}
	
	/**
	 * Returns the values chosen by the user in this InputComposite
	 */
	public String[] getValues() {
		return chosen;
	}
	/**
	 * Presets the selected value. Since this is a a single value input
	 * only the first element in <code> values</code> is used. If that
	 * value does not match any of the possible values given by the
	 * InputModel, nothing happens. If the
	 * given array is empty, behaviour is the same.
	 * 
	 * @param pValues the value to set, only the first element is used
	 */
	public void setValues(String[] pValues){
		if(pValues.length > 0){
			Button selected = null;
			boolean changed = false;
			
			/*
			 * Iterate over the normal radio button, excluding the last question
			 * radio button.
			 */
			for(int i = 0; i < buttons.length - 1; i++){
				/* If the give values mathes a radio button text */
				if(pValues[0].equalsIgnoreCase(buttons[i].getText())){
					buttons[i].setSelection(true);
					chosen = pValues;
					changed = true;
				}
				/* Keep track of a selected button to be able to deselect it
				 * if another button is selected
				 */
				else if(buttons[i].getSelection()){
					selected = buttons[i];
				}
			}
			/* If a radio button was selected */
			if(changed){
				buttons[buttons.length - 1].setSelection(false);
			}
			else{
				/* Check if the end of the value matches one of the units
				 * If it matches set the text field answer to the appropriate
				 * value
				 */
				for(int i = 0; i < unitDD.getItemCount(); i++){
					String unit = unitDD.getItem(i);
					if(pValues[0].length() > unit.length() + 1 && pValues[0].toLowerCase().endsWith(unit.toLowerCase())){
						unitDD.select(i);
						answer.setText(pValues[0].substring(0, pValues[0].length() - unit.length() - 1));
						buttons[buttons.length - 1].setSelection(true);
						chosen = pValues;
						changed = true;
					}
				}
			}
			/* If a new value has been set, and there was a selection previously */
			if(changed && selected != null){
				selected.setSelection(false);
			}
		}
	}
	
	/*
	 * A listener for the radio buttons
	 */
	private class RadioListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Button button = (Button)e.getSource();
			/*
			 * If the button creating this event is selected (deselecting also creates
			 * the same event)
			 */
			if(button.getSelection()){
				/* If it is the last "special" button */
				if(button.getText().equals("")){
					/* Consider an empty text input field as "Nothing chosen" */
					if(answer.getText().equals("")){
						chosen = new String[0];
					}
					else{
						chosen = new String[1];
						chosen[0] = answer.getText() + " " + unitDD.getItem(unitDD.getSelectionIndex());
					}
					answer.setFocus();
				}
				/* If it is a normal radio button */
				else{
					chosen = new String[1];
					chosen[0] = button.getText();
				}
			}
		}
	}
	
	/*
	 * A listener for the text input field
	 */
	private class AnswerListener implements ModifyListener{
		public void modifyText(ModifyEvent e){
			/* Make sure that the last button is selected */
			for(int i = 0; i < buttons.length - 1; i++)
				buttons[i].setSelection(false);
			buttons[buttons.length - 1].setSelection(true);
			/* Consider an empty field as "Nothing Chosen" */
			if(answer.getText().equals("")){
				chosen = new String[0];
			}
			else{
				chosen = new String[1];
				chosen[0] = answer.getText() + " " + unitDD.getItem(unitDD.getSelectionIndex());
			}
		}
	}
	/*
	 * A listener for the units drop down
	 */
	private class UnitsListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			/* Make sure that the last button is selected */
			for(int i = 0; i < buttons.length; i++){
				buttons[i].setSelection(false);
			}
			buttons[buttons.length - 1].setSelection(true);
			/* Consider an empty field as "Nothing Chosen" */
			if(answer.getText().equals("")){
				chosen = new String[0];
			}
			else{
				chosen = new String[1];
				chosen[0] = answer.getText() + " " + unitDD.getItem(unitDD.getSelectionIndex());
			}
		}
	}
}
