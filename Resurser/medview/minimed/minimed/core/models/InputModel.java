package minimed.core.models;


import java.beans.PropertyChangeListener;

/**
 * The basic class for InputComponent models.
 * Modified version for MiniMed.
 *
 * @author Nils Erichson, Nader Nazari, Andreas Nilsson
 */
public interface InputModel {
	public static final int INPUT_TYPE_LAST = 3;
	public static final int INPUT_TYPE_FIRST = 1;
	public static final int INPUT_TYPE_PLAQUE = 3;
    public static final int INPUT_TYPE_TEXTAREA = 1;
    public static final int INPUT_TYPE_PICTURECHOOSER = 2;
    
    /**
     * Returns true if input model is visible, false otherwise.
     * @return true if input model is visible, false otherwise.
     */
    public boolean isVisible();
    
    /**
     * Checks if the input model has any dependent input models.
     * @return True if this input model has dependent input models, false otherwise.
     */
    public boolean hasDependees();
        
    /**
     * Sets all input models dependent on value depVal to visible, and all
     * other dependent models to not visible. If no input models is dependent
     * on depVal, false is returned. An input model does not have to have
     * dependees on all its possible values.
     * 
     * @param depVal Value that has might have some dependent input models.
     * @return True if it existed an input model dependent on depVal, false otherwise.
     */
    public boolean notifyDependees(String depVal);
        
    /**
     * Sets this input model to not visible. If this model has dependent input
     * models, they are all set to not visible aswell. Note, calling this
     * function might recursively set a lot of input models to not visible,
     * hence classes using these input models should make necessary updates.
     */
    public void setNotVisible();
    
    /**
     * Sets this input model to visible.
     */
    public void setVisible();
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void clear();

    public void clear(boolean fireEvent);

    public String getDescription();
    
    /**
     * Returns the comment of the input. 
     * 
     * @return String the comment of the input. 
     */
    public String getComment();

    public String getName();

    public PresetModel getPresetModel();

    public int getType();

    public int getValueCount();

    public String[] getValues();

    public String getValueText();

    public boolean isEditable();
    
    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void setEditable(boolean isEditable);

    public void setName(String new_name);

    public void setDescription(String in_description);

    public void setPresetModel(PresetModel in_presetmodel);

    public boolean isTranslateAble();

    public void setTranslateAble(boolean flag);

    public boolean isRequired();

    public void setRequired(boolean flag);

    public boolean isIdentification();

    public void setIdentification(boolean flag);

    public String toString();
}
