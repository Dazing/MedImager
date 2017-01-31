package minimed.core.models;


import misc.foundation.HashVector;

/**
 * Describes one of the tabs for MedView.
 *
 * @author  Nils Erichson
 */
public class CategoryModel {
	private HashVector inputsTable; // Maps an input name to an input
	private String title; // Name of this tab
	
	/** Creates new CategoryModel */
	public CategoryModel(String inTitle) {
		super();
		
		title = new String(inTitle);
		inputsTable = new HashVector();
	}
	
	public CategoryModel() {
		this("Untitled");
	}
	
	public void addInput(InputModel input) {
		inputsTable.put(input.getName(),input);
	}
	
	public boolean containsInput(String catName) {
		return (inputsTable.containsKey(catName));
	}
	
	public InputModel getInput(String name) {
		return (InputModel) inputsTable.get(name);
	}
	
	/**
	 * Fetch an array of the field names.
	 */
	public String[] getInputNames() {
		String[] stringArray = new String[0];
		stringArray = (String[]) inputsTable.getKeyArray(stringArray);
		return stringArray; // Return as an array of strings
		
	}
	
	public InputModel[] getInputs() {        
		InputModel[] inputArray = new InputModel[inputsTable.size()];
		inputArray = (InputModel[]) inputsTable.getValueArray(inputArray);
		return inputArray; // Return as an array of strings                
	}
	
	public String getTitle() {
		return new String(title);
	}
	
	public void setTitle(String newTitle) {
		title = newTitle;
	}
	
	public void inputDown(String name) {        
		Object o = getInput(name);
		
		if (o != null) {
			inputsTable.moveRelative(o, +1);
		} else {
			System.err.println("inputUp: Could not move input [" + name + "] - could not find it...");
		}        
	}
	
	public void inputUp(String name) {
		Object o = getInput(name);
		
		if (o != null) {
			inputsTable.moveRelative(o, +1);
		} else {
			System.err.println("inputUp: Could not move input [" + name + "] - could not find it...");
		}                
	}
	
	public void moveInput(String name, int relativeAdjustment) {
		// Get the input named name
		InputModel i_model = (InputModel) inputsTable.get(name);
		
		inputsTable.moveRelative(i_model,relativeAdjustment);        
	}        
	
	public void removeInput(InputModel input) {
		inputsTable.removeByKey(input.getName());
	}
}
