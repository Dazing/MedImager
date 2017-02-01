package minimed.gui.exam;


import minimed.core.models.InputModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A class for showing multivalue choices in the exam view. Shows the choises as either a list of 
 * checkboxes or an Look Up and Add (LUAA) view, where the user can type text into a text field and a list of
 * choises is scrolled accordingly to what is typed into the textfield. A list of what has been chosen
 * is shown in a box below the list of possible choises.<BR>
 * 
 * The number of possible values in the InputModel decides what kind of view is used. If the number of
 * values is less than or equal to LUAA_LIMIT checkboxes are used, otherwise the LUAA view is used.
 *
 * @author Joni Paananen
 */
public class MultiInput extends InputComposite {
	/**
	 * The limit for when the check boxes or LOA view is used.
	 */
	private static final int LUAA_LIMIT = 4;
	private Label description;
	private Label comment;
	private TreeMap termValues;
	private LinkedList chosenValues;
	private Button[] checkBoxes;
	private Text textInput; 
	private List selectList;
	private List selectedList;
	
	/**
	 * Constructs a new instance of this class given its parent, a style value describing its behavior and appearance and an InputModel describing what to be shown.
	 * The style value is either one of the style constants defined in class 
	 * SWT which is applicable to instances of this class, or must be built 
	 * by bitwise OR'ing together (that is, using the int "|" operator) two 
	 * or more of those SWT style constants. The class description lists the 
	 * style constants that are applicable to the class. Style bits are also 
	 * inherited from superclasses. 
	 * 
	 * @param pParent a widget which will be the parent of the new instance (cannot be null).
	 * @param pStyle the style of widget to construct.
	 * @param pModel the model on which to build the input.
	 */
	public MultiInput(Composite pParent, int pStyle, InputModel pModel){
		super(pParent, pStyle, pModel);
		
		String[] values = pModel.getPresetModel().getPresets();
		termValues = new TreeMap(String.CASE_INSENSITIVE_ORDER);

		for(int i = 0; i < values.length; i++){
			termValues.put(values[i], values[i]);
		}
		chosenValues = new LinkedList();
		buildChoises();
	}
	
	/**
	 * Creates either a list of checkboxes or a LUAA view.
	 */
	private void buildChoises(){
		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		description = getGridWrapLabel(model.getDescription(), SWT.BOLD, 1);
		comment = getGridWrapLabel(model.getComment(), SWT.ITALIC, 1);
		
		/* If the number of choises are less than or equal to LUAA_LIMIT. */
		if (termValues.size() <= LUAA_LIMIT) {
			/* Creates a list of checkboxes. */
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			this.setLayout(layout);
			
			GridData descData = new GridData();
			descData.widthHint = this.getShell().getClientArea().width;
			description.setLayoutData(descData);
			
			GridData commentData = new GridData();
			commentData.widthHint = this.getShell().getClientArea().width;
			comment.setLayoutData(commentData);
			
			checkBoxes = new Button[termValues.size()];
			int i = 0;
			SelectionListener boxListener = new CheckBoxListener();
			for (Iterator it = termValues.keySet().iterator(); it.hasNext();){
				checkBoxes[i] = new Button(this, SWT.CHECK);
				checkBoxes[i].addSelectionListener(boxListener);
				checkBoxes[i++].setText((String)it.next());	
			}
		} else {
			/* 
			 * If the number of choises are more than LUAA_LIMIT. 
			 * Creates Text widget for input, and two List widgets 
			 * for selecting and deselecting values.
			 */
			
			FormLayout layout = new FormLayout();
			this.setLayout(layout);
			
			textInput = new Text(this, SWT.BORDER);
			selectList = new List(this, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			selectList.setItems((String[])termValues.keySet().toArray(new String[0]));
			Label selected = new Label(this, SWT.NONE);
			selected.setText("Valda alternativ");
			selectedList = new List(this, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			
			/* Set layout data for all the widgets. */
			FormData descData = new FormData();
			descData.width = this.getShell().getClientArea().width;
			
			FormData commentData = new FormData();
			commentData.top = new FormAttachment(description, 0);
			commentData.width = this.getShell().getClientArea().width;
			
			FormData textInputData = new FormData();
			textInputData.top = new FormAttachment(comment, 0);
			textInputData.left = new FormAttachment(0,0);
			textInputData.right = new FormAttachment(100,0);
			
			FormData selectListData = new FormData();
			selectListData.top = new FormAttachment(textInput, 1);
			selectListData.bottom = new FormAttachment(70,0);
			selectListData.left = new FormAttachment(0,0);
			selectListData.right = new FormAttachment(100,0);
			
			FormData selectedData = new FormData();
			selectedData.top = new FormAttachment(selectList, 0);
			selectedData.left = new FormAttachment(0,0);
			selectedData.right = new FormAttachment(100,0);

			FormData selectedListData = new FormData();
			selectedListData.left = new FormAttachment(0,0);
			selectedListData.right = new FormAttachment(100, 0);
			selectedListData.bottom = new FormAttachment(100, 0);
			selectedListData.top = new FormAttachment(selected, 0);
			
			description.setLayoutData(descData);
			comment.setLayoutData(commentData);
			textInput.setLayoutData(textInputData);
			selectList.setLayoutData(selectListData);
			selected.setLayoutData(selectedData);
			selectedList.setLayoutData(selectedListData);
			
			/* Add listeners to the widgets. */
			textInput.addModifyListener(new TextModifyListener());
			selectList.addSelectionListener(new SelectListener());
			selectedList.addSelectionListener(new DeselectListener());
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
		
		/* Either check boxes or LUAA. */
		if (checkBoxes != null) {
			return inputSize+20*checkBoxes.length;	
		} else {
			/* A LUAA should take up an entire page. */
			return inputSize+this.computeSize(pWidth, SWT.DEFAULT, false).y;
		}
	}
	
	/**
	 * Returns the values that the user has chosen in the composite, as an array of String.
	 * 
	 * @return the values that has been chosen.
	 */
	public String[] getValues(){
		return (String[])chosenValues.toArray(new String[0]);
	}
	
	/**
	 * Presets the chosen values to those given in the string array.
	 * If any of the values given does not equal any of the term values
	 * obtained from the InputModel given at construction nothing vill 
	 * be chosen.
	 * 
	 * @param pValues the values to be preset.
	 */
	public void setValues(String[] pValues){
		/*
		 * Add the values to the internal representation of chosen values, 
		 * which is a linked list.
		 */
		chosenValues.clear();
		for(int i = 0; i < pValues.length; i++){
			/* Adds only the values that are term values */
			if(termValues.containsKey(pValues[i])){
				chosenValues.add(pValues[i]);
			}
		}
		/* If checkboxes are used */
		if(termValues.size() <= LUAA_LIMIT){
			for(int i = 0; i < checkBoxes.length; i++){
				if(chosenValues.contains(checkBoxes[i].getText())){
					checkBoxes[i].setSelection(true);
				}
			}
		}
		/* If a LUAA view is used. */
		else{
			selectedList.removeAll();
			selectedList.setItems((String[])chosenValues.toArray(new String[0]));
		}
	}
	
	/**
	 * If the view is in LUAA mode, the text input is given focus. Otherwise
	 * nothing happens.
	 */
	public void focus(){
		if(termValues.size() > LUAA_LIMIT) {
			textInput.setFocus();
		}
	}
	
	/**
	 * Listener for the textInput widget in the LUAA view. Filters out all nonmatching
	 * values from the selectList.
	 */
	private class TextModifyListener implements ModifyListener{
		/**
		 * Filters out all non matching values.
		 * 
		 * @param pEvent an event. 
		 */
		public void modifyText(ModifyEvent pEvent) {
			String text = ((Text)pEvent.getSource()).getText();
			if (!text.equals("")) {
				StringBuffer filter = new StringBuffer(text);
				int lastIndex = filter.length() - 1;
				filter.setCharAt(lastIndex, (char)(filter.charAt(lastIndex) + 1));
				Map subMap = termValues.subMap(text, filter.toString());
				selectList.setItems((String[])subMap.keySet().toArray(new String[0]));
			} else {
				selectList.setItems((String[])termValues.keySet().toArray(new String[0]));
			}
		}
	}
	
	/**
	 * Listener for the selectList List widget. Adds the selected value to chosenValues
	 * and updates the selectedList widget.
	 */
	private class SelectListener extends SelectionAdapter{
		/**
		 * Ass the selected values to chosenValues and updates the selectedList.
		 * 
		 * @param pEvent an event. 
		 */
		public void widgetSelected(SelectionEvent pEvent) {
			String[] selected = selectList.getSelection();
			if (selected.length > 0) {
				if (!chosenValues.contains(selected[0])) {
					selectedList.add(selected[0]);
					chosenValues.add(selected[0]);
				}
			}
			textInput.setFocus();
			textInput.setText("");
		}
	}
	
	/**
	 * Listener for the selectedList List widget. Removes the value both from the widget
	 * and chosenValues.
	 */
	private class DeselectListener extends SelectionAdapter {
		/**
		 * Removes the selected values form chosenValues and the selectedList. 
		 */
		public void widgetSelected(SelectionEvent pEvent) {
			int index = selectedList.getSelectionIndex();
			if (index > -1) {
				selectedList.remove(index);
				chosenValues.remove(index);
				selectedList.deselectAll();
				textInput.setFocus();
			}
		}
	}
	
	/**
	 * Listener for the checkboxes. Adds and removes checked/unchecked values from
	 * chosenValues.
	 */
	private class CheckBoxListener extends SelectionAdapter {
		/**
		 * Adds and removes checked/unchecked values from chosenValues. 
		 */
		public void widgetSelected(SelectionEvent e){
			Button box = (Button)e.getSource();
			if(box.getSelection()){
				if(!chosenValues.contains(box.getText())){
					chosenValues.add(box.getText());
				}
			}
			else{
				chosenValues.remove(box.getText());
			}
		}
	}
}
