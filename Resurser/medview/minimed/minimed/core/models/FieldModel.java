package minimed.core.models;


import minimed.core.MinimedConstants;

/**
 * Model class that contains all information about how input
 * can be made into an input field. Does NOT handle values
 * anymore. Modified version for MiniMed. 
 *
 * @author Nils Erichson
 */
public class FieldModel extends AbstractInputModel {
    protected int length;
    protected int fieldType;
    protected String comment;
    public static final int DEFAULT_LENGTH = 40;
	
    public int getFieldType() {
        return fieldType;
    }

    public int getLength() {
        return length;
    }
    
    public int getType() {
        return fieldType;
    }

    public String getComment() {
    	if (comment != null) {
    		return comment;
    	} else {
    		return "";
    	}
    }
    
    public void setLength(int in_length) {
        length = in_length;
    }

    public void setFieldType(int in_type) {
        if ((in_type >= MinimedConstants.FIELD_TYPE_FIRST) &&
        		(in_type <= MinimedConstants.FIELD_TYPE_LAST)) {
            fieldType = in_type;
        } else {
            System.err.println("Note: FieldModel.setType(invalid fieldtype)");
        }
    }
    
    public void setComment(String text) {
    	comment = text;
    }

    public boolean isIdentification() {
        return (getFieldType() == MinimedConstants.FIELD_TYPE_IDENTIFICATION);
    }

    public static boolean isSingleValueType(int type) {
        switch(type) {
            case MinimedConstants.FIELD_TYPE_SINGLE:

            case MinimedConstants.FIELD_TYPE_INTERVAL:

            case MinimedConstants.FIELD_TYPE_QUESTION:

            case MinimedConstants.FIELD_TYPE_NOTE:

            case MinimedConstants.FIELD_TYPE_IDENTIFICATION:
            {
                return true;
            }

            default:
            {
                return false;
            }
        }
    }

    public FieldModel(String initialName, int in_fieldType, PresetModel in_presetModel, String initialDescription, String initialText,int initialLength) {
        super(initialName,initialDescription,in_presetModel);

        length = initialLength;

        if ((in_fieldType < MinimedConstants.FIELD_TYPE_FIRST) ||
        		(in_fieldType > MinimedConstants.FIELD_TYPE_LAST)) {
            System.err.println("FieldModel(): Error: in_fieldType out of bounds (" + in_fieldType +"), setting to single");

            fieldType = MinimedConstants.FIELD_TYPE_SINGLE;
        } else {
            fieldType = in_fieldType;
        }

        putValue(initialText);
    }

    public FieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialText) {
        this(initialName, in_fieldType, in_presetModel, initialDescription, initialText, DEFAULT_LENGTH);
    }
}
