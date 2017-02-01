package minimed.core.models;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * The basic class for InputComponent models.
 * Modified version for MiniMed.
 *
 * @author Nils Erichson
 */
public abstract class AbstractInputModel implements InputModel {
	
	public static int i = 0;
	
	private Hashtable depValuesAndModels;
	
	private boolean visible = true;
	
	protected int type;

	private String name;

	private String description;

	private boolean editable = true;

	private PresetModel presetModel;

	protected Vector currentValues;

	private PropertyChangeSupport changeSupport;
	
	private boolean required = false;

	private boolean translateAble = true;

	private boolean identificationTerm = false;
		
	public AbstractInputModel(String initialName, String initialDescription, PresetModel in_presetModel) {
		changeSupport = new PropertyChangeSupport(this);

		currentValues = new Vector();

		setName(initialName);

		setDescription(initialDescription);

		if (in_presetModel == null)	{
			setPresetModel(new PresetModel(initialName));
		} else {
			setPresetModel(in_presetModel);
		}
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Adds a dependent model and what value it is dependent on. No checking is 
	 * perfomed to guarantee that the provided value is actually an real value
	 * for the dependent on input model.
	 * 
	 * @param depVal the input model is dependent on this value.
	 * @param model dependent input model.
	 */
	public void addDepModel(String depVal, InputModel model) {
		/* If first time method is invoked, create table. */
		if (depValuesAndModels == null) {
			depValuesAndModels = new Hashtable();
		}
		
		/* Check if previous models dependent on this depVal exists. */
		if (!depValuesAndModels.containsKey(depVal)) {
			ArrayList modellist = new ArrayList();
			modellist.add(model);
			depValuesAndModels.put(depVal, modellist);
		} else {
			ArrayList modellist = (ArrayList)depValuesAndModels.get(depVal);
			modellist.add(model);
			depValuesAndModels.put(depVal,modellist);
		}
	}
	
	public boolean hasDependees() {
		if (depValuesAndModels == null) {
			return false;
		} else if (depValuesAndModels.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setVisible() {
		/* If previous not visible, fire propertyChangeEvent. */
		if (!visible) {
			changeSupport.firePropertyChange("Visible", "unvisible", "visible");
		}
		
		visible = true;
	}
	
	public void setNotVisible() {
		// Set this input model to not visible
		visible = false;
		// Set all dependent input models to not visible
		if (depValuesAndModels != null) {
			Enumeration models = depValuesAndModels.elements();
			while (models.hasMoreElements()) {
				ArrayList inputmodels = (ArrayList)models.nextElement();
				for (int i = 0 ; i < inputmodels.size(); i ++) {
					InputModel thismodel = (InputModel)inputmodels.get(i);
					thismodel.setNotVisible();
				}
			}
		}
		
		// Fire not visible event
		changeSupport.firePropertyChange("Not visible", "visible", "unvisible");
	}
	
	public boolean notifyDependees(String depVal) {
		/* Does any dependent input models even exist? */
		if (depValuesAndModels == null) {
			return false;
		}
		
		/* Does any input models depend on depVal? */
		if (!depValuesAndModels.containsKey(depVal.toLowerCase())) {
			return false;
		}
		
		/* 
		 * Sets dependent input model to visible, and all other dependent
		 * input models to not visible
		 */
		Enumeration keys = depValuesAndModels.keys();
		
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			if (key.equalsIgnoreCase(depVal)) {
				ArrayList inputmodels = (ArrayList)depValuesAndModels.get(depVal.toLowerCase());
				for (int i = 0 ; i < inputmodels.size(); i ++) {
					InputModel thismodel = (InputModel)inputmodels.get(i);
					thismodel.setVisible();
				}
			} else {
				ArrayList inputmodels = (ArrayList)depValuesAndModels.get(key.toLowerCase());
				
				for (int i = 0 ; i < inputmodels.size(); i ++) {
					InputModel thismodel = (InputModel)inputmodels.get(i);
					thismodel.setNotVisible();
				}
			}
		}
		
		return true;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
    	changeSupport.removePropertyChangeListener(listener);
    }

	public void clear() {
		clear(true);
	}

	public void clear(boolean fireEvent) {
		currentValues.clear();

		if (fireEvent)	{
			changeSupport.firePropertyChange("Cleared Input", "cleared", this);
		}
	}

	public String getDescription() {
		return new String(description);
	}

	public String getName() {
		return new String(name);
	}

	public PresetModel getPresetModel() {
		return presetModel;
	}

	public int getType() {
		return type;
	}

	public int getValueCount() {
		return currentValues.size();
	}

	public String[] getValues() {
		String[] values = new String[currentValues.size()];
		values = (String[])currentValues.toArray(values);
		return values;
	}

	public String getValueText() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < currentValues.size(); i++) {
			sb.append((String)currentValues.get(i));

			 // don't append enter to the last, because that will create a new line!
			if (i < (currentValues.size() - 1)) {
				sb.append("\n");
			}
		}
		String result = sb.toString();

		return result;
	}

	public void removeBlankValues() {
		for (Iterator it = currentValues.iterator(); it.hasNext(); )
		{
			Object o = it.next();

			if (o instanceof String)
			{
				String s = (String)o;

				/* If value can be reduced to empty */
				if (s.trim().equals("")) {
					it.remove();
				}
			}
		}
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean isEditable) {
		editable = isEditable;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void setDescription(String in_description) {
		description = new String(in_description);
	}

	public void setPresetModel(PresetModel in_presetmodel) {
		presetModel = in_presetmodel;
	}

	public void putValue(String in_value) {
		putValue(in_value, true);
	}

	public void putValue(String inValue, boolean fireEvent) {
		if (!currentValues.contains(inValue)) {
			currentValues.add(inValue);
		}
	}

	public boolean removeValue(String v) {
		return removeValue(v, true);
	}

	public boolean removeValue(String v, boolean fireEvent)	{
		boolean didChange = currentValues.remove(v);

		return didChange;
	}

	public boolean isTranslateAble() {
		return translateAble;
	}

	public void setTranslateAble(boolean flag) {
		translateAble = flag;
	}

	public boolean isRequired()	{
		return required;
	}

	public void setRequired(boolean flag){
		required = flag;
	}

	public boolean isIdentification() {
		return identificationTerm;
	}

	public void setIdentification(boolean flag)	{
		identificationTerm = flag;
	}

	public String toString() {
		return (getName() + "(" + currentValues.size() + "values)");
	}
}
